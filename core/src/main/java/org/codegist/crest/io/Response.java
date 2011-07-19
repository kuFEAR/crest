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

package org.codegist.crest.io;

import org.codegist.common.lang.Disposable;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

/**
 * Response context, passed to the response handlers and error handlers.
 *
 * @see org.codegist.crest.handler.ResponseHandler
 * @see org.codegist.crest.handler.ErrorHandler
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public interface Response extends Disposable {

    Request getRequest();

    int getStatusCode() throws Exception;

    String getContentType() throws Exception;

    Charset getCharset() throws Exception;

    String getContentEncoding() throws Exception;

    Type getExpectedGenericType();

    Class<?> getExpectedType();

    <T> T deserialize() throws Exception;

    <T> T to(Class<T> type) throws Exception;

    <T> T to(Class<T> type, Type genericType) throws Exception;

}
