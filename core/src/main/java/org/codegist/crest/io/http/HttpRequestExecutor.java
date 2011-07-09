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

import org.codegist.common.lang.Disposable;
import org.codegist.common.lang.Disposables;
import org.codegist.common.log.Logger;
import org.codegist.crest.*;
import org.codegist.crest.config.PathBuilder;
import org.codegist.crest.io.Request;
import org.codegist.crest.io.RequestException;
import org.codegist.crest.io.RequestExecutor;
import org.codegist.crest.io.Response;
import org.codegist.crest.serializer.DeserializationManager;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Iterator;

import static org.codegist.common.lang.Strings.isNotBlank;
import static org.codegist.crest.io.http.HttpParamProcessor.process;
import static org.codegist.crest.io.http.HttpRequest.from;
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

    public Response execute(Request request) throws RequestException {
        HttpResponse response;
        try {
            response = doExecute(from(request));
            if(response.getStatusCode() >= 400) {
                throw new RequestException(response.getStatusMessage(), response);
            }
            return response;
        }catch(IOException e){
            throw new RequestException(e);
        }catch(RuntimeException e){
            throw CRestException.handle(e);
        }
    }

    private HttpResponse doExecute(HttpRequest httpRequest) throws IOException, RequestException {
        Charset charset = httpRequest.getCharset();
        String url = getUrlFor(httpRequest);

        LOGGER.debug("Initiating HTTP Channel: %s %s", httpRequest.getMeth(), url);
        LOGGER.trace(httpRequest);

        HttpChannel httpChannel = channelInitiator.initiate(httpRequest.getMeth(), url, httpRequest.getCharset());

        if(httpRequest.getConnectionTimeout() != null) {
            int timeout = httpRequest.getConnectionTimeout().intValue();
            LOGGER.debug("Set Connection Timeout: %d ", timeout);
            httpChannel.setConnectionTimeout(timeout);
        }

        if(httpRequest.getSocketTimeout() != null) {
            int timeout = httpRequest.getSocketTimeout().intValue();
            LOGGER.debug("Set Socket Timeout: %d ", timeout);
            httpChannel.setSocketTimeout(timeout);
        }

        if(httpRequest.getContentType() != null) {
            LOGGER.debug("Set Content-Type: %d ", httpRequest.getContentType());
            httpChannel.setContentType(httpRequest.getContentType());
        }

        if(httpRequest.getAccept() != null) {
            LOGGER.debug("Set Accept: %d ", httpRequest.getAccept());
            httpChannel.setAccept(httpRequest.getAccept());
        }

        Iterator<Pair> headers = httpRequest.iterateProcessedHeaders();
        while(headers.hasNext()){
            Pair encoded = headers.next();
            String name = encoded.getName();
            String value = encoded.getValue();
            LOGGER.debug("Header %s: %s ", name, value);
            httpChannel.addHeader(name, value);
        }

        for(HttpParam header : httpRequest.getCookieParams()){
            String cookie = join(process(header, charset), ',');
            if(cookie.length() > 0) {
                LOGGER.debug("Cookie: %s ", cookie);
                httpChannel.addHeader("Cookie", cookie);
            }
        }

        if(httpRequest.getMeth().hasEntity()) {
            String contentType = httpRequest.getEntityWriter().getContentType(httpRequest);
            if(isNotBlank(contentType)) {
                if(httpRequest.getContentType() == null) {
                    LOGGER.debug("Entity Content-Type : %s", contentType);
                    httpChannel.setContentType(contentType);
                }else{
                    LOGGER.debug("Entity Content-Type : %s (ignored as previously set)", contentType);
                }
            }
            httpChannel.writeEntityWith(new HttpRequestEntityWriter(httpRequest, LOGGER));
        }

        HttpChannel.Response response = httpChannel.send();
        return new HttpResponse(deserializationManager, httpRequest, new HttpChannelResponseHttpResource(response));
    }


    private String getUrlFor(HttpRequest request){
        Iterator<Pair> queryParamsIterator = request.iterateProcessedQueries();
        Iterator<Pair> matrixesParamsIterator = request.iterateProcessedMatrixes();
        Iterator<Pair> pathParamsIterator = request.iterateProcessedPaths();

        PathBuilder pathBuilder = request.getPathBuilder();
        while(pathParamsIterator.hasNext()){
            Pair encoded = pathParamsIterator.next();
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
