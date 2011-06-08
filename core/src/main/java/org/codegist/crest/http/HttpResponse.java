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

package org.codegist.crest.http;

import org.codegist.common.io.IOs;
import org.codegist.common.lang.Strings;
import org.codegist.common.lang.ToStringBuilder;

import java.io.*;
import java.nio.charset.Charset;

/**
 * Http response for the a HttpRequest.
 * <p>Response charset and mime type are retrieved on the Content-Type header.
 * <p>If no valid charset and mimetype are found, it defaults respectively with ISO-8859-1 and text/html
 * <p>If the response is GZipped, the Content-Encoding header must be set to gzip.
 *
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class HttpResponse {

    private final HttpRequest request;
    private final InputStream inputStream;
    private final HttpResource resource;
    private final int statusCode;

    private String responseString = null;

//    public HttpResponse(HttpRequest request, int statusCode) throws IOException {
//        this(request, statusCode, EMPTY_HTTP_RESOURCE);
//    }

    public HttpResponse(HttpRequest request, int statusCode, HttpResource resource) throws IOException {
        this.request = request;
        this.statusCode = statusCode;
        this.resource = resource;
        this.inputStream = new HttpResourceInputStream(resource);
    }

    public Reader asReader() throws IllegalStateException, IOException {
        if (inputStream == null) return null;
        if (responseString != null) {
            throw new IllegalStateException("Stream as already been consumed");
        }
        return new InputStreamReader(inputStream, resource.getCharset());
    }

    public InputStream asStream() {
        if (inputStream == null) return null;
        if (responseString != null) {
            throw new IllegalStateException("Stream as already been consumed");
        }
        return inputStream;
    }

    public String asString() throws IOException {
        if (inputStream == null) return null;
        if (responseString == null) {
            responseString = IOs.toString(inputStream, resource.getCharset(), true);
        }
        return responseString;
    }

    public int getStatusCode() {
        return statusCode;
    }


    public String getContentType() throws IOException {
        return resource.getContentType();
    }

    public Charset getCharset() throws IOException {
        return resource.getCharset();
    }

    public String getContentEncoding() throws IOException {
        return resource.getContentEncoding();
    }

    public HttpRequest getRequest() {
        return request;
    }

    public void close() {
        IOs.close(inputStream);
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append("statusCode", statusCode)
                .append("resource", resource)
                .append("request", request)
                .toString();
    }

    private static final HttpResource EMPTY_HTTP_RESOURCE = new HttpResource () {
        private final InputStream INPUT_STREAM = new ByteArrayInputStream(new byte[0]);
        public InputStream getContent() throws HttpException {
            return INPUT_STREAM;
        }

        public Charset getCharset() throws IOException {
            return null;
        }

        public String getContentType() throws IOException {
            return null;
        }

        public String getContentEncoding() throws IOException {
            return null;
        }

        public void release() throws HttpException {

        }
    };
}

