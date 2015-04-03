/*
 * Copyright 2011 CodeGist.org
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

import org.codegist.common.io.IOs;
import org.codegist.common.log.Logger;
import org.codegist.crest.io.Response;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

/**
 * @author laurent.gilles@codegist.org
 */
class HttpChannelResponseHttpResource implements HttpResource {

    private static final Logger RESPONSE_LOGGER = Logger.getLogger(Response.class);
    private final HttpChannel.Response response;
    private final InputStream inputStream;
    private final String contentEncoding;
    private final Charset charset;
    private final String contentType;
    private final Map<String, List<String>> headerFields;

    HttpChannelResponseHttpResource(HttpChannel.Response response) throws IOException {
        this.response = response;
        ContentType ct = new ContentType(response.getContentType());
        this.contentType = ct.mimeType;
        this.headerFields = response.getHeaderFields();
        this.charset = ct.charset;
        this.contentEncoding = response.getContentEncoding();
        this.inputStream = getEntity(response, charset);
    }

    public String getContentType() {
        return contentType;
    }

    public Charset getCharset() {
        return charset;
    }

    public String getHeaderField(String field) throws IOException {
        StringBuilder header = new StringBuilder();
        for (String headerValue : headerFields.get(field)) {
            try {
                header.append(headerValue).append("\n");
            } catch (NoSuchElementException e) {
                e.printStackTrace();
            }
        }
        return String.valueOf(header);
    }

    public Map<String, List<String>> getHeaderFields() {
        return headerFields;
    }

    public String getContentEncoding() {
        return contentEncoding;
    }

    public InputStream getEntity() throws IOException {
        return inputStream;
    }

    public String getStatusMessage() throws IOException {
        return response.getStatusMessage();
    }

    public int getStatusCode() throws IOException {
        return response.getStatusCode();
    }

    public void close() throws IOException {
        response.close();
    }


    private static InputStream getEntity(HttpChannel.Response response, Charset charset) throws IOException {
        InputStream stream = "gzip".equals(response.getContentEncoding()) ? new GZIPInputStream(response.getEntity()) : response.getEntity();
        if (!RESPONSE_LOGGER.isTraceOn()) {
            return stream;
        } else {
            byte[] dump = IOs.toByteArray(stream, true);
            RESPONSE_LOGGER.trace("Received Http Response");
            RESPONSE_LOGGER.trace(new String(dump, charset));
            return new ByteArrayInputStream(dump);
        }
    }

    private static final class ContentType {

        public static final Pattern SEMICOLON = Pattern.compile(";");
        public static final Pattern EQUAL = Pattern.compile("=");
        private static final String DEFAULT_MIME_TYPE = "text/html";
        private static final Charset DEFAULT_CHARSET = Charset.forName("ISO-8859-1");

        private final String mimeType;
        private final Charset charset;

        private ContentType(String contentType) {
            String pMimeType = DEFAULT_MIME_TYPE;
            Charset pCharset = DEFAULT_CHARSET;
            if (contentType != null) {
                String[] contentTypes = SEMICOLON.split(contentType);

                if (contentTypes.length >= 1) {
                    pMimeType = contentTypes[0];
                }
                if (contentTypes.length >= 2 && contentTypes[1].contains("charset")) {
                    pCharset = Charset.forName(EQUAL.split(contentTypes[1])[1]);
                }
            }
            this.mimeType = pMimeType;
            this.charset = pCharset;
        }
    }
}
