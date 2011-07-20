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

package org.codegist.crest.io.http;

import org.codegist.common.collect.Arrays;
import org.codegist.common.lang.Disposable;
import org.codegist.common.lang.Disposables;
import org.codegist.common.log.Logger;
import org.codegist.crest.config.MethodConfig;
import org.codegist.crest.config.MethodType;
import org.codegist.crest.config.PathBuilder;
import org.codegist.crest.io.*;
import org.codegist.crest.param.EncodedPair;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Iterator;

import static org.codegist.common.lang.Strings.isNotBlank;
import static org.codegist.crest.config.ParamType.*;
import static org.codegist.crest.io.http.HttpConstants.HTTP_BAD_REQUEST;
import static org.codegist.crest.util.Pairs.join;

/**
 * @author laurent.gilles@codegist.org
 */
public class HttpRequestExecutor implements RequestExecutor, Disposable {

    private static final Logger LOGGER = Logger.getLogger(HttpRequestExecutor.class);
    private final HttpChannelFactory channelFactory;
    private final ResponseDeserializer baseResponseDeserializer;
    private final ResponseDeserializer customTypeResponseDeserializer;

    public HttpRequestExecutor(HttpChannelFactory channelFactory, ResponseDeserializer baseResponseDeserializer, ResponseDeserializer customTypeResponseDeserializer) {
        this.channelFactory = channelFactory;
        this.baseResponseDeserializer = baseResponseDeserializer;
        this.customTypeResponseDeserializer = customTypeResponseDeserializer;
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
        MethodType methodType = mc.getType();
        HttpChannel httpChannel = channelFactory.open(methodType, url, charset);

        int coTimeout = mc.getConnectionTimeout();
        LOGGER.debug("Set Connection Timeout: %d ", coTimeout);
        httpChannel.setConnectionTimeout(coTimeout);

        int soTimeout = mc.getSocketTimeout();
        LOGGER.debug("Set Socket Timeout: %d ", soTimeout);
        httpChannel.setSocketTimeout(soTimeout);

        if(mc.getProduces() != null) {
            LOGGER.debug("Set Content-Type: %d ", mc.getProduces());
            httpChannel.setContentType(mc.getProduces());
        }

        String[] consumes = mc.getConsumes();
        if(consumes.length > 0) {
            String accept = Arrays.join(",", consumes);
            LOGGER.debug("Set Accept: %d ", accept);
            httpChannel.setAccept(accept);
        }

        Iterator<EncodedPair> headers = request.getEncodedParamsIterator(HEADER);
        while(headers.hasNext()){
            EncodedPair encoded = headers.next();
            String name = encoded.getName();
            String value = encoded.getValue();
            LOGGER.debug("Header %s: %s ", name, value);
            httpChannel.addHeader(name, value);
        }

        Iterator<EncodedPair> cookies = request.getEncodedParamsIterator(COOKIE);
        while(cookies.hasNext()){
            EncodedPair encoded = cookies.next();
            String name = encoded.getName();
            String value = encoded.getValue();
            LOGGER.debug("%s: %s ", name, value);
            httpChannel.addHeader(name, value);
        }

        if(methodType.hasEntity()) {
            String contentType = mc.getEntityWriter().getContentType(request);
            if(isNotBlank(contentType)) {
                if(mc.getProduces() == null) {
                    LOGGER.debug("Entity Content-Type : %s", contentType);
                    httpChannel.setContentType(contentType);
                }else{
                    LOGGER.debug("Entity Content-Type : %s (ignored as previously set)", contentType);
                }
            }
            httpChannel.writeEntityWith(new RequestEntityWriter(request, LOGGER));
        }

        HttpChannel.Response response = httpChannel.send();
        return new HttpResponse(baseResponseDeserializer, customTypeResponseDeserializer, request, new HttpChannelResponseHttpResource(response));
    }


    private String getUrlFor(Request request) throws Exception {
        MethodConfig mc = request.getMethodConfig();
        Charset charset = mc.getCharset();
        PathBuilder pathBuilder = mc.getPathTemplate().getBuilder(charset);

        Iterator<EncodedPair> pathParamsIterator = request.getEncodedParamsIterator(PATH);
        while(pathParamsIterator.hasNext()){
            EncodedPair encoded = pathParamsIterator.next();
            pathBuilder.merge(encoded.getName(), encoded.getValue(), true);
        }

        String query = join(request.getEncodedParamsIterator(QUERY), '&');
        if(isNotBlank(query)) {
            query = "?" + query;
        }

        String matrix = join(request.getEncodedParamsIterator(MATRIX), ';');
        if(isNotBlank(matrix)) {
            matrix = ";" + matrix;
        }

        return pathBuilder.build() + matrix + query;
    }

    public void dispose() {
        Disposables.dispose(channelFactory);
    }
}
