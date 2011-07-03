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

package org.codegist.crest.security.http;

import org.codegist.common.io.IOs;
import org.codegist.crest.http.*;
import org.codegist.crest.security.Authorization;
import org.codegist.crest.security.AuthorizationToken;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.codegist.common.lang.Strings.isNotBlank;

/**
 * @author laurent.gilles@codegist.org
 */
public class AuthorizationHttpChannel implements HttpChannel {

    private final Authorization authenticatorManager;
    private final Map<String,HttpEntityParamsParser> httpEntityParamsParsers;
    private final HttpChannel delegate;
    private final List<Pair> parameters = new ArrayList<Pair>();
    private final String url;
    private final Charset charset;
    private final HttpMethod method;
    private String contentType = "";
    private String fullContentType = "";
    private HttpEntityWriter httpEntityWriter;

    public AuthorizationHttpChannel(HttpChannel delegate, Authorization authenticatorManager, HttpMethod method, String url, Charset charset, Map<String,HttpEntityParamsParser> httpEntityParamsParsers) {
        this.url = url;
        this.method = method;
        this.charset = charset;
        this.delegate = delegate;
        this.httpEntityParamsParsers = httpEntityParamsParsers;
        this.authenticatorManager = authenticatorManager;
        String[] split = url.split("\\?");
        if(split.length == 2) {
            this.parameters.addAll(Pairs.parseUrlEncoded(split[1]));
        }
    }

    private void authenticate() throws IOException {
        AuthorizationToken token = authenticatorManager.authorize(method, url, parameters.toArray(new Pair[parameters.size()]));
        delegate.setHeader("Authorization", token.getName() + " " + token.getValue());
    }

    public int send() throws IOException {
        if(hasEntityParser()) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            httpEntityWriter.writeEntityTo(out);
            List<Pair> entityParams = httpEntityParamsParsers.get(contentType).parse(fullContentType, charset, new ByteArrayInputStream(out.toByteArray()));
            this.parameters.addAll(entityParams);
        }
        authenticate();
        return this.delegate.send();
    }

    public void setContentType(String contentType) throws IOException {
        this.delegate.setContentType(contentType);
        this.contentType = contentType.split(";")[0].trim();
        this.fullContentType = contentType;
    }

    public void writeEntityWith(HttpEntityWriter httpEntityWriter) throws IOException {
        this.httpEntityWriter = hasEntityParser() ? new CachingHttpEntityWriter(httpEntityWriter) : httpEntityWriter;
        this.delegate.writeEntityWith(this.httpEntityWriter);
    }

    public InputStream getResponseStream() throws IOException {
        return this.delegate.getResponseStream();
    }

    public String getResponseContentType() throws IOException {
        return this.delegate.getResponseContentType();
    }

    public String getResponseContentEncoding() throws IOException {
        return this.delegate.getResponseContentEncoding();
    }

    public void dispose() {
        this.delegate.dispose();
    }

    public void addHeader(String name, String value) throws IOException {
        this.delegate.addHeader(name, value);
    }

    public void setHeader(String name, String value) throws IOException {
        this.delegate.setHeader(name, value);
    }

    public void setAccept(String value) throws IOException {
        this.delegate.setAccept(value);
    }

    public void setSocketTimeout(int timeout) throws IOException {
        this.delegate.setSocketTimeout(timeout);
    }

    public void setConnectionTimeout(int timeout) throws IOException {
        this.delegate.setConnectionTimeout(timeout);
    }

    private boolean hasEntityParser(){
        return httpEntityParamsParsers.containsKey(contentType);
    }


    private class CachingHttpEntityWriter implements HttpEntityWriter {

        private final HttpEntityWriter delegate;
        private Integer contentLength;
        private byte[] entityContent;

        private CachingHttpEntityWriter(HttpEntityWriter delegate) {
            this.delegate = delegate;
        }

        public void writeEntityTo(OutputStream out) throws IOException {
            if(entityContent == null) {
                ByteArrayOutputStream cache = new ByteArrayOutputStream();
                delegate.writeEntityTo(cache);
                this.entityContent = cache.toByteArray();
            }
            IOs.copy(new ByteArrayInputStream(entityContent), out);
        }

        public int getContentLength() {
            if(contentLength == null) {
                contentLength = delegate.getContentLength();
            }
            return contentLength;
        }
    }
}
