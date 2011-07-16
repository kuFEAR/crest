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

package org.codegist.crest.impl.io.platform;

import org.codegist.common.io.EmptyInputStream;
import org.codegist.common.log.Logger;
import org.codegist.crest.impl.io.HttpChannel;
import org.codegist.crest.impl.io.HttpEntityWriter;
import org.codegist.crest.impl.io.HttpMethod;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import static org.codegist.crest.impl.io.HttpConstants.HTTP_BAD_REQUEST;

public final class HttpURLConnectionHttpChannel implements HttpChannel {

    private static final Logger LOG = Logger.getLogger(HttpURLConnectionHttpChannel.class);
    private final HttpURLConnection con;
    private final HttpMethod httpMethod;
    private HttpEntityWriter httpEntityWriter;


    public HttpURLConnectionHttpChannel(HttpURLConnection con, HttpMethod httpMethod){
        this.con = con;
        this.httpMethod = httpMethod;
    }

    public void setSocketTimeout(int timeout) throws IOException {
        con.setReadTimeout(timeout);
    }

    public void setConnectionTimeout(int timeout) throws IOException {
        con.setConnectTimeout(timeout);
    }

    public void setHeader(String name, String value) throws IOException {
        con.setRequestProperty(name, value);
    }

    public void addHeader(String name, String value) throws IOException {
        con.addRequestProperty(name, value);
    }

    public void setContentType(String value) throws IOException {
        setHeader("Content-Type", value);
    }

    public void setAccept(String value) throws IOException {
        setHeader("Accept", value);
    }

    public void writeEntityWith(HttpEntityWriter httpEntityWriter) throws IOException {
        this.httpEntityWriter = httpEntityWriter;
    }

    public Response send() throws IOException {
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("User-Agent", "CodeGist-CRest Agent");
        if(httpMethod.hasEntity()) {
            if(httpEntityWriter.getContentLength() >= 0) {
               con.setRequestProperty("Content-Length", String.valueOf(httpEntityWriter.getContentLength()));
            }
            con.setDoOutput(true);
            OutputStream os = con.getOutputStream();
            httpEntityWriter.writeEntityTo(os);
            os.flush();
            os.close();
        }
        return new HttpURLResponse(con);
    }

    private static final class HttpURLResponse implements Response {

        private final HttpURLConnection con;

        private HttpURLResponse(HttpURLConnection con) {
            this.con = con;
        }

        public int getStatusCode() throws IOException {
            return con.getResponseCode();
        }

        public String getStatusMessage() throws IOException {
            return con.getResponseMessage();
        }

        public InputStream getStream() throws IOException {
            if(getStatusCode() >= HTTP_BAD_REQUEST) {
                return EmptyInputStream.INSTANCE;
            }else{
                return con.getInputStream();
            }
        }

        public String getContentType() {
            return con.getHeaderField("Content-Type");
        }

        public String getContentEncoding() {
            return con.getHeaderField("Content-Encoding");
        }

        public void close() {
            LOG.trace("Disconnecting...");
            con.disconnect();
        }
    }

}