/*
 * Copyright 2010 CodeGist.org
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 * ===================================================================
 *
 * More information at http://www.codegist.org.
 */

package org.codegist.crest;

import org.codegist.common.log.Logger;
import org.codegist.common.log.LoggingOutputStream;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Simple RestService implementation based on JDK's {@link java.net.HttpURLConnection}.
 *
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 * @see java.net.HttpURLConnection
 */
public class DefaultRestService implements RestService {

    private final static String USER_AGENT = "CodeGist-CRest Agent";
    private static final Logger logger = Logger.getLogger(DefaultRestService.class);

    public HttpResponse exec(HttpRequest request) throws HttpException {
        HttpURLConnection connection = null;
        boolean inError = false;
        try {
            connection = toHttpURLConnection(request);
            logger.debug("%4s %s", request.getMeth(), connection.getURL());
            logger.trace(request);
            if (connection.getResponseCode() >= 400) {
                throw new HttpException(connection.getResponseMessage(), new HttpResponse(request, connection.getResponseCode(), connection.getHeaderFields()));
            }
            HttpResponse response = new HttpResponse(request, connection.getResponseCode(), connection.getHeaderFields(), new HttpResourceImpl(connection));
            logger.trace("HTTP Response %s", response);
            return response;
        } catch (HttpException e) {
            inError = true;
            throw e;
        } catch (Throwable e) {
            inError = true;
            throw new HttpException(e, new HttpResponse(request, -1));
        } finally {
            if (inError) {
                if (connection != null) connection.disconnect();
            }
        }
    }

    static HttpURLConnection toHttpURLConnection(HttpRequest request) throws IOException {
        String url = request.getUrl();
        HttpURLConnection con = newConnection(url, request.getMeth());
        
        if (request.getConnectionTimeout() != null && request.getConnectionTimeout() >= 0)
            con.setConnectTimeout(request.getConnectionTimeout().intValue());

        if (request.getSocketTimeout() != null && request.getSocketTimeout() >= 0)
            con.setReadTimeout(request.getSocketTimeout().intValue());

        for (HttpParam param: request.getHeaderParams()) {
            con.addRequestProperty(param.getName(), param.getValue().asString());
        }

        if(request.hasEntity()) {
            con.setDoOutput(true);
            OutputStream os = !logger.isTraceOn() ? con.getOutputStream() : new LoggingOutputStream(con.getOutputStream(), logger);

            DataOutputStream out = new DataOutputStream(os);
            request.getEntityWriter().writeTo(request, out);
            os.flush();
            os.close();
        }
                       
        return con;
    }

    protected static HttpURLConnection newConnection(String url, String method) throws IOException {
        HttpURLConnection con;
        con = (HttpURLConnection) new URL(url).openConnection();
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestMethod(method);
        return con;
    }

    private class HttpResourceImpl implements HttpResource {

        private final HttpURLConnection connection;

        public HttpResourceImpl(HttpURLConnection connection) {
            this.connection = connection;
        }

        public InputStream getContent() throws HttpException{
            try {
                return connection.getInputStream();
            } catch (IOException e) {
                throw new HttpException(e);
            }
        }

        public void release() throws HttpException{
            connection.disconnect();
        }
    }
}
