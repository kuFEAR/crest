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

import org.codegist.crest.http.HttpResponse;
import org.codegist.crest.serializer.DeserializationManager;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Default internal immutable implementation of ResponseContext
 *
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
class DefaultResponseContext implements ResponseContext {

    private final HttpResponse response;
    private final RequestContext context;
    private final DeserializationManager deserializationManager;

    public DefaultResponseContext(DeserializationManager deserializationManager, RequestContext context, HttpResponse response) {
        this.deserializationManager = deserializationManager;
        this.context = context;
        this.response = response;
    }

    public HttpResponse getResponse() {
        return response;
    }

    public Type getExpectedGenericType() {
        return context.getMethod().getGenericReturnType();
    }

    public Class<?> getExpectedType() {
        return context.getMethod().getReturnType();
    }

    public RequestContext getRequestContext() {
        return context;
    }

    public <T> T deserialize() throws IOException {
        return this.<T>deserializeTo((Class<T>) getExpectedType(), getExpectedGenericType());
    }
    
    public <T> T deserializeTo(Class<T> type, Type genericType) throws IOException {
        return deserializationManager.<T>deserializeTo(
                type,
                genericType,
                response.asStream(),
                response.getContentType(),
                response.getCharset(),
                context.getMethodConfig().getDeserializers()
        );
    }
}
