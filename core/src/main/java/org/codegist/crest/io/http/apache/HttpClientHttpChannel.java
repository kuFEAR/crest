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

package org.codegist.crest.io.http.apache;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.params.HttpConnectionParams;
import org.codegist.common.io.EmptyInputStream;
import org.codegist.common.log.Logger;
import org.codegist.crest.io.http.HttpChannel;
import org.codegist.crest.io.http.HttpEntityWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class HttpClientHttpChannel implements HttpChannel {

    private static final Logger LOGGER = Logger.getLogger(HttpClientHttpChannel.class);
    private final HttpClient client;
    private final HttpUriRequest request;

    public HttpClientHttpChannel(HttpClient client, HttpUriRequest request){
        this.request = request;
        this.client = client;
    }

    public void setConnectionTimeout(int timeout) throws IOException {
        HttpConnectionParams.setConnectionTimeout(request.getParams(), timeout);
    }

    public void setSocketTimeout(int timeout) throws IOException  {
        HttpConnectionParams.setSoTimeout(request.getParams(), timeout);
    }

    public void setHeader(String name, String value) throws IOException  {
        request.setHeader(name, value);
    }

    public void addHeader(String name, String value) throws IOException  {
        request.addHeader(name, value);
    }

    public void setContentType(String value) throws IOException {
        setHeader("Content-Type", value);
    }

    public void setAccept(String value) throws IOException {
        setHeader("Accept", value);
    }

    public void writeEntityWith(HttpEntityWriter httpEntityWriter)  throws IOException {
        ((HttpEntityEnclosingRequest) request).setEntity(new HttpEntityWriterHttpEntity(httpEntityWriter));
    }

    public Response send()  throws IOException {
        return new HttpClientResponse(request, client.execute(request));
    }

    private static class HttpClientResponse implements Response {

        private final HttpUriRequest request;
        private final org.apache.http.HttpResponse response;

        private HttpClientResponse(HttpUriRequest request, HttpResponse response) {
            this.request = request;
            this.response = response;
        }

        public InputStream getStream() throws IOException  {
            HttpEntity entity = response.getEntity();
            return entity != null ? entity.getContent() : EmptyInputStream.INSTANCE;
        }

        public String getContentType() {
            Header header = response.getFirstHeader("Content-Type");
            if(header != null) {
                return header.getValue();
            }else{
                return null;
            }
        }

        public String getContentEncoding() {
            Header header = response.getFirstHeader("Content-Encoding");
            if(header != null) {
                return header.getValue();
            }else{
                return null;
            }
        }

        public int getStatusCode() throws IOException {
            return response.getStatusLine().getStatusCode();
        }

        public String getStatusMessage() throws IOException {
            return response.getStatusLine().getReasonPhrase();
        }

        public void close() {
            try {
                if(response.getEntity() != null) {
                    response.getEntity().consumeContent();
                }
            } catch (IOException e) {
                LOGGER.warn(e, "Failed to consume content for io %s", request);
            } finally {
                request.abort();
            }
        }
    }

    private class HttpEntityWriterHttpEntity extends AbstractHttpEntity {

        private final HttpEntityWriter writer;

        private HttpEntityWriterHttpEntity(HttpEntityWriter writer) {
            this.writer = writer;
        }

        public boolean isRepeatable() {
            return false;
        }

        public long getContentLength() {
            return writer.getContentLength();
        }

        public InputStream getContent() throws IOException, IllegalStateException {
            throw new UnsupportedOperationException();
        }

        public void writeTo(OutputStream outstream) throws IOException {
            writer.writeEntityTo(outstream);
        }

        public boolean isStreaming() {
            return true;
        }
    }


}