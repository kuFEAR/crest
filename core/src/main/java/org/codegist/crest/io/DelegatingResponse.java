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

package org.codegist.crest.io;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

/**
 * {@link org.codegist.crest.io.Response} implementation that delegates all calls to the another {@link org.codegist.crest.io.Response}.
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class DelegatingResponse implements Response {

    private final Response delegate;

    /**
     * @param delegate Response to delegate all calls to
     */
    public DelegatingResponse(Response delegate) {
        this.delegate = delegate;
    }

    public Request getRequest() {
        return delegate.getRequest();
    }

    public int getStatusCode() throws Exception {
        return delegate.getStatusCode();
    }

    public String getContentType() throws Exception {
        return delegate.getContentType();
    }

    public Charset getCharset() throws Exception {
        return delegate.getCharset();
    }

    public String getContentEncoding() throws Exception {
        return delegate.getContentEncoding();
    }

    public Type getExpectedGenericType() {
        return delegate.getExpectedGenericType();
    }

    public Class<?> getExpectedType() {
        return delegate.getExpectedType();
    }

    public <T> T deserialize() throws Exception {
        return delegate.<T>deserialize();
    }

    public <T> T to(Class<T> type) throws Exception {
        return delegate.<T>to(type);
    }

    public <T> T to(Class<T> type, Type genericType) throws Exception {
        return delegate.<T>to(type, genericType);
    }

    public InputStream asStream() throws Exception {
        return delegate.asStream();
    }

    public void dispose() {
        delegate.dispose();
    }
}
