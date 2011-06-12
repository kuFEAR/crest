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
import java.nio.charset.Charset;
import java.util.zip.GZIPInputStream;

/**
 * @author laurent.gilles@codegist.org
 */
class HttpChannelHttpResource implements HttpResource {

    private final HttpChannel channel;
    private final String contentEncoding;
    private final Charset charset;
    private final String contentType;
    private final boolean zipped;

    HttpChannelHttpResource(HttpChannel channel) throws IOException {
        this.channel = channel;
        ContentType ct = new ContentType(channel.readContentType());
        this.contentType = ct.mimeType;
        this.charset = ct.charset;
        this.contentEncoding =  channel.readContentEncoding();
        this.zipped = "gzip".equals(contentEncoding);
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

    public InputStream getContent() throws IOException {
        return !zipped ? channel.read() : new GZIPInputStream(channel.read());
    }

    public void release() throws IOException {
        channel.dispose();
    }

    private static final class ContentType {

        private static final String DEFAULT_MIME_TYPE = "text/html";
        private static final Charset DEFAULT_CHARSET = Charset.forName("ISO-8859-1");

        private final String mimeType;
        private final Charset charset;

        private ContentType(String contentType) {
            String mimeType = DEFAULT_MIME_TYPE;
            Charset charset = DEFAULT_CHARSET;
            if(contentType != null) {
                String[] contentTypes = contentType.split(";");

                if (contentTypes.length >= 1) {
                    mimeType = contentTypes[0];
                }
                if (contentTypes.length >= 2) {
                    if (contentTypes[1].contains("charset")) {
                        charset = Charset.forName(contentTypes[1].split("=")[1]);
                    }
                }
            }
            this.mimeType = mimeType;
            this.charset = charset;
        }
    }
}
