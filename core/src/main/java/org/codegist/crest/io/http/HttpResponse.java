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

package org.codegist.crest.io.http;

import org.codegist.common.io.IOs;
import org.codegist.common.lang.Disposable;
import org.codegist.crest.io.DelegatingResponse;
import org.codegist.crest.io.Request;
import org.codegist.crest.io.Response;
import org.codegist.crest.serializer.ResponseDeserializer;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

/**
 * Http response for the a HttpRequest.
 * <p>Response charset and mime type are retrieved on the Content-Type header.
 * <p>If no valid charset and mimetype are found, it defaults respectively with ISO-8859-1 and text/html
 * <p>If the response is GZipped, the Content-Encoding header must be set to gzip.
 *
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
class HttpResponse implements Response, Disposable {

    private final ResponseDeserializer baseResponseDeserializer;
    private final ResponseDeserializer customTypeResponseDeserializer;
    private final Request request;
    private final InputStream inputStream;
    private final HttpResource resource;

    public HttpResponse(ResponseDeserializer baseResponseDeserializer, ResponseDeserializer customTypeResponseDeserializer, Request request, HttpResource resource) throws IOException {
        this.baseResponseDeserializer = baseResponseDeserializer;
        this.customTypeResponseDeserializer = customTypeResponseDeserializer;
        this.request = request;
        this.resource = resource;
        this.inputStream = new HttpResourceInputStream(resource);
    }

    public int getStatusCode() throws IOException {
        return resource.getStatusCode();
    }

    public String getStatusMessage() throws IOException {
        return resource.getStatusMessage();
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

    public Type getExpectedGenericType() {
        return request.getMethodConfig().getMethod().getGenericReturnType();
    }

    public Class<?> getExpectedType() {
        return request.getMethodConfig().getMethod().getReturnType();
    }

    public Request getRequest() {
        return request;
    }

    public InputStream asStream() {
        return inputStream;
    }

    public <T> T deserialize() throws Exception {
        return baseResponseDeserializer.<T>deserialize(this);
    }

    public <T> T to(Class<T> type) throws Exception {
        return to(type, type);
    }

    public <T> T to(Class<T> type, Type genericType) throws Exception {
        return customTypeResponseDeserializer.<T>deserialize(new ExpectedTypeOverriderResponse(this, type, genericType));
    }

    public void dispose() {
        IOs.close(inputStream);
    }

    private static final class ExpectedTypeOverriderResponse extends DelegatingResponse {

        private final Class<?> clazz;
        private final Type type;

        private ExpectedTypeOverriderResponse(Response delegate, Class<?> clazz, Type type) {
            super(delegate);
            this.clazz = clazz;
            this.type = type;
        }

        public Type getExpectedGenericType() {
            return type;
        }

        public Class<?> getExpectedType() {
            return clazz;
        }

    }
}

