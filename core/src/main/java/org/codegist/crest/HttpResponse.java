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

import org.codegist.common.collect.Maps;
import org.codegist.common.io.IOs;
import org.codegist.common.lang.Strings;
import org.codegist.common.lang.ToStringBuilder;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * Http response for the a HttpRequest.
 * <p>Response charset and mime type are retrieved on the Content-Type header.
 * <p>If no valid charset and mimetype are found, it defaults respectively with ISO-8859-1 and text/html
 * <p>If the response is GZipped, the Content-Encoding header must be set to gzip.
 *
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class HttpResponse {

    private static final String DEFAULT_MIME_TYPE = "text/html";
    private static final Charset DEFAULT_CHARSET = Charset.forName("ISO-8859-1");
    private final HttpRequest request;
    private final InputStream inputStream;
    private final int statusCode;
    private final String contentEncoding;
    private final String contentType;
    private final Charset charset;
    private final Map<String, List<String>> headers;

    private String responseString = null;

    public HttpResponse(HttpRequest request, int statusCode) {
        this(request, statusCode, null);
    }

    public HttpResponse(HttpRequest request, int statusCode, Map<String, List<String>> headers) {
        this(request, statusCode, headers, EMPTY_HTTP_RESOURCE);
    }

    /**
     * @param request    The original request
     * @param statusCode the response status code
     * @param headers    response headers.
     * @param resource   underlying http resource
     */
    public HttpResponse(HttpRequest request, int statusCode, Map<String, List<String>> headers, HttpResource resource) {
        this.request = request;
        this.statusCode = statusCode;
        this.headers = Maps.unmodifiable(headers);
        this.contentEncoding = getFirstHeaderFor(this.headers, "Content-Encoding");
        InputStream stream = resource != null ? new HttpResourceInputStream(resource) : null;
        if (resource != null && "gzip".equalsIgnoreCase(contentEncoding)) {
            try {
                this.inputStream = new GZIPInputStream(stream);
            } catch (IOException e) {
                throw new HttpException(e);
            }
        } else {
            this.inputStream = stream;
        }
        String[] contentTypes = extractContentTypeAndCharset(this.headers);
        if (contentTypes.length == 0) {
            this.contentType = DEFAULT_MIME_TYPE;
            this.charset = DEFAULT_CHARSET;
        } else {
            this.contentType = Strings.defaultIfBlank(contentTypes[0], DEFAULT_MIME_TYPE);
            if (Strings.isNotBlank(contentTypes[1])) {
                this.charset = Charset.forName(contentTypes[1]);
            } else {
                this.charset = DEFAULT_CHARSET;
            }
        }
    }

    /**
     * Get the response reader using the response charset (extracted from response header.)
     *
     * @return The response reader.
     * @throws IllegalStateException if {@link org.codegist.crest.HttpResponse#asString()} has already been called
     */
    public Reader asReader() throws IllegalStateException {
        if (inputStream == null) return null;
        if (responseString != null) {
            throw new IllegalStateException("Stream as already been consumed");
        }
        return new InputStreamReader(inputStream, charset);
    }

    /**
     * Get the response input stream. Use {@link HttpResponse#getCharset} to decode it.
     *
     * @return The response input stream.
     * @throws IllegalStateException if {@link org.codegist.crest.HttpResponse#asString()} has already been called
     */
    public InputStream asStream() {
        if (inputStream == null) return null;
        if (responseString != null) {
            throw new IllegalStateException("Stream as already been consumed");
        }
        return inputStream;
    }

    /**
     * Returns the response as string. Calling this method will consume the response stream and any call to {@link HttpResponse#asReader()}  or {@link org.codegist.crest.HttpResponse#asStream()} will throw an IllegalStateException.
     * <p>Can only be called if the reponse stream hasn't been consumed.
     *
     * @return the response as a string
     */
    public String asString() {
        if (inputStream == null) return null;
        if (responseString == null) {
            try {
                responseString = IOs.toString(inputStream, charset, true);
            } catch (IOException e) {
                throw new HttpException(e, this);
            }
        }
        return responseString;
    }

    public List<String> getHeader(String name) {
        List<String> header = headers.get(name);
        return header != null ? Collections.unmodifiableList(header) : Collections.<String>emptyList();
    }

    /**
     * @return Http status code
     */
    public int getStatusCode() {
        return statusCode;
    }


    public String getContentType() {
        return contentType;
    }

    public Charset getCharset() {
        return charset;
    }

    public String getContentEncoding() {
        return contentEncoding;
    }

    /**
     * @return The original request
     */
    public HttpRequest getRequest() {
        return request;
    }

    /**
     * Close the response.
     */
    public void close() {
        IOs.close(inputStream);
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append("statusCode", statusCode)
                .append("contentEncoding", contentEncoding)
                .append("mimeType", contentType)
                .append("charset", charset)
                .append("headers", headers)
                .append("request", request)
                .toString();
    }


    private static String[] extractContentTypeAndCharset(Map<String, List<String>> headers) {
        String contentType = getFirstHeaderFor(headers, "Content-Type");
        String[] contentTypes = contentType.split(";");
        String[] res = new String[2];
        if (contentTypes.length >= 1) {
            res[0] = contentTypes[0];
        }
        if (contentTypes.length >= 2) {
            if (contentTypes[1].contains("charset")) {
                res[1] = contentTypes[1].split("=")[1];
            }
        }
        return res;
    }


    private static String getFirstHeaderFor(Map<String, List<String>> headers, String name) {
        List<String> contentType = headers.get(name);
        if (contentType == null || contentType.isEmpty()) return "";
        return Strings.defaultIfBlank(contentType.get(0), "");
    }

    private static final HttpResource EMPTY_HTTP_RESOURCE = new HttpResource () {
        private final InputStream INPUT_STREAM = new ByteArrayInputStream(new byte[0]);
        public InputStream getContent() throws HttpException {
            return INPUT_STREAM;
        }

        public void release() throws HttpException {

        }
    };
}

