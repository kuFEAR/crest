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

package org.codegist.crest.io.http;

import org.codegist.common.io.IOs;
import org.codegist.crest.CRestException;
import org.codegist.crest.config.MethodType;
import org.codegist.crest.param.EncodedPair;
import org.codegist.crest.security.Authorization;
import org.codegist.crest.security.AuthorizationToken;
import org.codegist.crest.util.Pairs;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.codegist.common.collect.Arrays.arrify;

/**
 * @author laurent.gilles@codegist.org
 */
public class AuthorizationHttpChannel implements HttpChannel {

    private final Authorization authenticatorManager;
    private final Map<String, EntityParamExtractor> httpEntityParamExtrator;
    private final HttpChannel delegate;
    private final List<EncodedPair> parameters = new ArrayList<EncodedPair>();
    private final String url;
    private final Charset charset;
    private final MethodType methodType;
    private String contentType = null;
    private String fullContentType = null;
    private HttpEntityWriter httpEntityWriter = null;

    public AuthorizationHttpChannel(HttpChannel delegate, Authorization authenticatorManager, MethodType methodType, String url, Charset charset, Map<String, EntityParamExtractor> httpEntityParamExtrator) throws UnsupportedEncodingException {
        this.url = url;
        this.methodType = methodType;
        this.charset = charset;
        this.delegate = delegate;
        this.httpEntityParamExtrator = httpEntityParamExtrator;
        this.authenticatorManager = authenticatorManager;
        String[] split = url.split("\\?");
        if(split.length == 2) {
            this.parameters.addAll(Pairs.fromUrlEncoded(split[1]));
        }
    }

    private void authenticate() throws IOException {
        AuthorizationToken token;
        try {
            token = authenticatorManager.authorize(methodType.name(), url, arrify(parameters, EncodedPair.class));
        } catch (Exception e) {
            throw CRestException.handle(e);
        }
        delegate.setHeader("Authorization", token.getName() + " " + token.getValue());
    }

    public Response send() throws IOException {
        if(hasEntityParamExtrator()) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            httpEntityWriter.writeEntityTo(out);
            List<EncodedPair> entityParams = httpEntityParamExtrator.get(contentType).extract(fullContentType, charset, new ByteArrayInputStream(out.toByteArray()));
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
        this.httpEntityWriter = hasEntityParamExtrator() ? new RewritableHttpEntityWriter(httpEntityWriter) : httpEntityWriter;
        this.delegate.writeEntityWith(this.httpEntityWriter);
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

    private boolean hasEntityParamExtrator(){
        return httpEntityParamExtrator.containsKey(contentType);
    }

    private static final class RewritableHttpEntityWriter implements HttpEntityWriter {

        private final HttpEntityWriter delegate;
        private Integer contentLength;
        private byte[] entityContent;

        private RewritableHttpEntityWriter(HttpEntityWriter delegate) {
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
