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

package org.codegist.crest.http;

import org.codegist.common.log.Logger;
import org.codegist.crest.config.PathBuilder;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import static org.codegist.common.lang.Strings.isNotBlank;
import static org.codegist.crest.http.HttpParamProcessor.process;

/**
 * @author laurent.gilles@codegist.org
 */
public class DefaultHttpRequestExecutor implements HttpRequestExecutor {

    private static final Logger LOGGER = Logger.getLogger(DefaultHttpRequestExecutor.class);
    private final HttpChannelInitiator channelInitiator;

    public DefaultHttpRequestExecutor(HttpChannelInitiator channelInitiator) {
        this.channelInitiator = channelInitiator;
    }

    public HttpResponse execute(HttpRequest request) throws IOException {

        Charset charset = request.getCharset();
        String url = getUrlFor(request);

        LOGGER.debug("Initiating HTTP Channel: %s %s", request.getMeth(), url);
        LOGGER.trace(request);

        HttpChannel httpChannel = channelInitiator.initiate(request.getMeth(), url, request.getCharset());

        if(request.getConnectionTimeout() != null) {
            int timeout = request.getConnectionTimeout().intValue();
            LOGGER.debug("Set Connection Timeout: %d ", timeout);
            httpChannel.setConnectionTimeout(timeout);
        }

        if(request.getSocketTimeout() != null) {
            int timeout = request.getSocketTimeout().intValue();
            LOGGER.debug("Set Socket Timeout: %d ", timeout);
            httpChannel.setSocketTimeout(timeout);
        }

        if(request.getContentType() != null) {
            LOGGER.debug("Set Content-Type: %d ", request.getContentType());
            httpChannel.setContentType(request.getContentType());
        }

        if(request.getAccept() != null) {
            LOGGER.debug("Set Accept: %d ", request.getAccept());
            httpChannel.setAccept(request.getAccept());
        }

        for(HttpParam header : request.getHeaderParams()){
            for(Pair encoded : process(header, charset)){
                String name = encoded.getName();
                String value = "\"" + encoded.getValue() + "\"";
                LOGGER.debug("Header %s: %s ", name, value);
                httpChannel.addHeader(name, value);
            }
        }

        for(HttpParam header : request.getCookieParams()){
            StringBuilder sb = new StringBuilder();
            boolean first = true;
            for(Pair encoded : process(header, charset)){
                if(!first) {
                    sb.append(",");
                }
                sb.append(encoded.getName()).append("=\"").append(encoded.getValue()).append("\"");
                first = false;
            }
            String cookie = sb.toString();
            if(cookie.length() > 0) {
                LOGGER.debug("Cookie: %s ", cookie);
                httpChannel.addHeader("Cookie", cookie);
            }
        }

        if(request.getMeth().hasEntity()) {
            String contentType = request.getEntityWriter().getContentType(request);
            if(isNotBlank(contentType)) {
                if(request.getContentType() == null) {
                    LOGGER.debug("Entity Content-Type : %s", contentType);
                    httpChannel.setHeader("Content-Type", contentType);
                }else{
                    LOGGER.debug("Entity Content-Type : %s (ignored as previously set)", contentType);
                }
            }
            httpChannel.writeEntityWith(new HttpRequestEntityWriter(request, LOGGER));
        }

        int statusCode = httpChannel.send();
        return new HttpResponse(request, statusCode, new HttpChannelHttpResource(httpChannel));
    }


    private String getUrlFor(HttpRequest request){
        PathBuilder pathBuilder = request.getPathBuilder();

        Charset charset = request.getCharset();
        String matrix = buildUrl(request.getMatrixParams(), charset, ";");
        String query = buildUrl(request.getQueryParams(), charset, "&");

        for(HttpParam param : request.getPathParams()){
            for(Pair encoded : process(param, charset)){
                pathBuilder.merge(encoded.getName(), encoded.getValue(), true);
            }
        }

        if(isNotBlank(query)) {
            query = "?" + query;
        }

        if(isNotBlank(matrix)) {
            matrix = ";" + matrix;
        }

        return pathBuilder.build() + matrix + query;
    }
    private String buildUrl(List<HttpParam> params, Charset charset, String sep){
        boolean first = true;
        StringBuilder sb = new StringBuilder();
        for(HttpParam param : params){
            for(Pair encoded : process(param, charset)){
                if(!first) {
                    sb.append(sep);
                }
                sb.append(encoded.getName()).append("=").append(encoded.getValue());
                first = false;
            }
        }
        return sb.toString();
    }
}
