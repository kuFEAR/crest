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

import org.codegist.crest.http.HttpChannel;
import org.codegist.crest.http.HttpEntityWriter;
import org.codegist.crest.http.HttpMethod;
import org.codegist.crest.http.Pair;
import org.codegist.crest.security.Authorization;
import org.codegist.crest.security.AuthorizationToken;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @author laurent.gilles@codegist.org
 */
public class AuthorizationHttpChannel implements HttpChannel {

    private final Authorization authenticatorManager;
    private final HttpChannel delegate;
    private final List<Pair> parameters = new ArrayList<Pair>();
    private final String url;
    private final Charset charset;
    private final HttpMethod method;
    private String contentType;
    private HttpEntityWriter httpEntityWriter;

    public AuthorizationHttpChannel(HttpChannel delegate, Authorization authenticatorManager, HttpMethod method, String url, Charset charset) {
        this.url = url;
        this.method = method;
        this.charset = charset;
        this.delegate = delegate;
        this.authenticatorManager = authenticatorManager;
        String[] split = url.split("\\?");
        if(split.length == 2) {
            this.parameters.addAll(parse(split[1]));
        }
    }

    private void authenticate() throws IOException {
        AuthorizationToken token = authenticatorManager.authorize(method, url, parameters.toArray(new Pair[parameters.size()]));
        delegate.setHeader("Authorization", token.getName() + " " + token.getValue());
    }

    private static List<Pair> parse(String urlEncoded){
        List<Pair> pairs = new ArrayList<Pair>();
        String[] split = urlEncoded.split("&");
        for(String param : split){
            String[] paramSplit = param.split("=");
            pairs.add(new Pair(paramSplit[0], paramSplit[1]));
        }
        return pairs;
    }

    public int send() throws IOException {
        if(contentType.startsWith("application/x-www-form-urlencoded")) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            httpEntityWriter.writeEntityTo(out);
            String formContent = out.toString(charset.displayName());
            this.parameters.addAll(parse(formContent));
        }
        authenticate();
        return this.delegate.send();
    }

    public void setContentType(String value) throws IOException {
        this.delegate.setContentType(value);
        this.contentType = value;
    }

    public void writeEntityWith(HttpEntityWriter httpEntityWriter) throws IOException {
        this.delegate.writeEntityWith(httpEntityWriter);
        this.httpEntityWriter = httpEntityWriter;
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
}
