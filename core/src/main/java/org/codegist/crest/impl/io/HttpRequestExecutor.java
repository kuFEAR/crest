/*
 * Copyright 2010 CodeGist.org
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 *
 *  ===================================================================
 *
 *  More information at http://www.codegist.org.
 */

package org.codegist.crest.impl.io;

import org.codegist.common.lang.Disposable;
import org.codegist.common.lang.Disposables;
import org.codegist.common.log.Logger;
import org.codegist.crest.config.MethodConfig;
import org.codegist.crest.config.PathBuilder;
import org.codegist.crest.io.Request;
import org.codegist.crest.io.RequestException;
import org.codegist.crest.io.RequestExecutor;
import org.codegist.crest.io.Response;
import org.codegist.crest.param.EncodedPair;
import org.codegist.crest.serializer.DeserializationManager;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Iterator;

import static org.codegist.common.lang.Strings.isNotBlank;
import static org.codegist.crest.impl.io.HttpConstants.HTTP_BAD_REQUEST;
import static org.codegist.crest.impl.io.HttpParamType.*;
import static org.codegist.crest.impl.param.HttpParamProcessors.iterate;
import static org.codegist.crest.util.Pairs.join;

/**
 * @author laurent.gilles@codegist.org
 */
public class HttpRequestExecutor implements RequestExecutor, Disposable {

    private static final Logger LOGGER = Logger.getLogger(HttpRequestExecutor.class);
    private final HttpChannelInitiator channelInitiator;
    private final DeserializationManager deserializationManager;

    public HttpRequestExecutor(HttpChannelInitiator channelInitiator, DeserializationManager deserializationManager) {
        this.channelInitiator = channelInitiator;
        this.deserializationManager = deserializationManager;
    }

    public Response execute(Request request) throws Exception {
        HttpResponse response;
        try {
            response = doExecute(request);
            if(response.getStatusCode() >= HTTP_BAD_REQUEST) {
                throw new RequestException(response.getStatusMessage(), response);
            }
            return response;
        }catch(IOException e){
            throw new RequestException(e);
        }
    }

    private HttpResponse doExecute(Request request) throws IOException, Exception {
        String url = getUrlFor(request);
        MethodConfig mc = request.getMethodConfig();
        Charset charset = mc.getCharset();

        LOGGER.debug("Initiating HTTP Channel: %s %s", mc.getType(), url);
        LOGGER.trace(request);
        HttpMethod method = HttpMethod.valueOf(mc.getType());
        HttpChannel httpChannel = channelInitiator.initiate(method, url, charset);

        int coTimeout = mc.getConnectionTimeout();
        LOGGER.debug("Set Connection Timeout: %d ", coTimeout);
        httpChannel.setConnectionTimeout(coTimeout);

        int soTimeout = mc.getSocketTimeout();
        LOGGER.debug("Set Socket Timeout: %d ", soTimeout);
        httpChannel.setSocketTimeout(soTimeout);

        if(mc.getContentType() != null) {
            LOGGER.debug("Set Content-Type: %d ", mc.getContentType());
            httpChannel.setContentType(mc.getContentType());
        }

        if(mc.getAccept() != null) {
            LOGGER.debug("Set Accept: %d ", mc.getAccept());
            httpChannel.setAccept(mc.getAccept());
        }

        Iterator<EncodedPair> headers = iterate(request.getParams(HEADER.name()), charset);
        while(headers.hasNext()){
            EncodedPair encoded = headers.next();
            String name = encoded.getName();
            String value = encoded.getValue();
            LOGGER.debug("Header %s: %s ", name, value);
            httpChannel.addHeader(name, value);
        }

        Iterator<EncodedPair> cookies = iterate(request.getParams(COOKIE.name()), charset);
        while(cookies.hasNext()){
            EncodedPair encoded = cookies.next();
            String name = encoded.getName();
            String value = encoded.getValue();
            LOGGER.debug("%s: %s ", name, value);
            httpChannel.addHeader(name, value);
        }

        if(method.hasEntity()) {
            String contentType = mc.getEntityWriter().getContentType(request);
            if(isNotBlank(contentType)) {
                if(mc.getContentType() == null) {
                    LOGGER.debug("Entity Content-Type : %s", contentType);
                    httpChannel.setContentType(contentType);
                }else{
                    LOGGER.debug("Entity Content-Type : %s (ignored as previously set)", contentType);
                }
            }
            httpChannel.writeEntityWith(new RequestEntityWriter(request, LOGGER));
        }

        HttpChannel.Response response = httpChannel.send();
        return new HttpResponse(deserializationManager, request, new HttpChannelResponseHttpResource(response));
    }


    private String getUrlFor(Request request) throws Exception {
        MethodConfig mc = request.getMethodConfig();
        Charset charset = mc.getCharset();
        Iterator<EncodedPair> queryParamsIterator = iterate(request.getParams(QUERY.name()), charset);
        Iterator<EncodedPair> matrixesParamsIterator = iterate(request.getParams(MATRIX.name()), charset);
        Iterator<EncodedPair> pathParamsIterator = iterate(request.getParams(PATH.name()), charset);

        PathBuilder pathBuilder = mc.getPathTemplate().getBuilder(charset);
        while(pathParamsIterator.hasNext()){
            EncodedPair encoded = pathParamsIterator.next();
            pathBuilder.merge(encoded.getName(), encoded.getValue(), true);
        }

        String query = join(queryParamsIterator, '&');
        if(isNotBlank(query)) {
            query = "?" + query;
        }

        String matrix = join(matrixesParamsIterator, ';');
        if(isNotBlank(matrix)) {
            matrix = ";" + matrix;
        }

        return pathBuilder.build() + matrix + query;
    }

    public void dispose() {
        Disposables.dispose(channelInitiator);
    }
}
