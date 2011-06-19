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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
* @author Laurent Gilles (laurent.gilles@codegist.org)
*/
public class HttpURLConnectionHttpChannelInitiator implements HttpChannelInitiator {

    public HttpChannel initiate(HttpMethod method, String url) throws IOException {
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        con.setRequestMethod(method.name());
        return new HttpURLConnectionHttpChannel(con, method.hasEntity());
    }


    private static class HttpURLConnectionHttpChannel implements HttpChannel {

        private final HttpURLConnection con;
        private final boolean hasEntity;
        private volatile HttpEntityWriter httpEntityWriter;


        private HttpURLConnectionHttpChannel(HttpURLConnection con, boolean hasEntity){
            this.con = con;
            this.hasEntity = hasEntity;
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

        public void writeEntityWith(HttpEntityWriter httpEntityWriter) throws IOException {
            this.httpEntityWriter = httpEntityWriter;
        }

        public int send() throws IOException {
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("User-Agent", "CodeGist-CRest Agent");
            if(hasEntity) {
                con.setDoOutput(true);
                OutputStream os = con.getOutputStream();
                httpEntityWriter.writeEntityTo(os);
                os.flush();
                os.close();
            }
            return con.getResponseCode();
        }

        public InputStream getResponseStream() throws IOException {
            return con.getInputStream();
        }

        public String readContentType() {
            return con.getHeaderField("Content-Type");
        }

        public String readContentEncoding() {
            return con.getHeaderField("Content-Encoding");
        }

        public void dispose() {
            con.disconnect();
        }

    }

}
