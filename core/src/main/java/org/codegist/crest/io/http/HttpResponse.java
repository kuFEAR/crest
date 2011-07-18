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
import org.codegist.crest.CRestException;
import org.codegist.crest.io.Request;
import org.codegist.crest.io.Response;
import org.codegist.crest.serializer.DeserializationManager;
import org.codegist.crest.serializer.Deserializer;

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
public class HttpResponse implements Response, Disposable {

    private final DeserializationManager deserializationManager;
    private final Request request;
    private final InputStream inputStream;
    private final HttpResource resource;

    public HttpResponse(DeserializationManager deserializationManager, Request request, HttpResource resource) throws IOException {
        this.deserializationManager = deserializationManager;
        this.request = request;
        this.resource = resource;
        this.inputStream = new HttpResourceInputStream(resource);
    }

    public InputStream asStream() {
        return inputStream;
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

    public <T> T deserialize() throws Exception {
        Deserializer[] deserializers = request.getMethodConfig().getDeserializers();
        if(deserializers.length > 0) {
            // try with preconfigured deserializer if present
            return deserializationManager.deserializeByDeserializers(
                    (Class<T>) getExpectedType(),
                    getExpectedGenericType(),
                    asStream(),
                    getCharset(),
                    deserializers
            );
        }else if(deserializationManager.isMimeTypeKnown(getContentType())){
            // if no pre-defined deserializers, try by mime type if known
            return deserializationManager.deserializeByMimeType(
                    (Class<T>) getExpectedType(),
                    getExpectedGenericType(),
                    asStream(),
                    getCharset(),
                    getContentType()
            );
        }else if(deserializationManager.isClassTypeKnown(getExpectedType())){
            // if no pre-defined deserializers, mime type is unknown, try by class type if known
            return deserializationManager.deserializeByClassType(
                    (Class<T>) getExpectedType(),
                    getExpectedGenericType(),
                    asStream(),
                    getCharset()
            );
        } else {
            throw new CRestException("Can't deserializer response to " + getExpectedType());
        }
    }

    public <T> T deserializeTo(Class<T> type) throws Exception {
        return deserializeTo(type, type);
    }

    public <T> T deserializeTo(Class<T> type, Type genericType) throws Exception {
        if(deserializationManager.isClassTypeKnown(type)){
            return deserializationManager.deserializeByClassType(
                    type,
                    genericType,
                    asStream(),
                    getCharset()
            );
        }else{
            return deserializationManager.deserializeByMimeType(
                    type,
                    genericType,
                    asStream(),
                    getCharset(),
                    getContentType()
            );
        }
    }

    public void dispose() {
        IOs.close(inputStream);
    }

}

