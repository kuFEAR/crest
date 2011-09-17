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
 * <b>CRest</b>'s internal rest interface method call execution response
 */
public interface Response extends Disposable {

    /**
     * @return original request
     */
    Request getRequest();

    int getStatusCode() throws Exception;

    String getContentType() throws Exception;

    Charset getCharset() throws Exception;

    String getContentEncoding() throws Exception;

    /**
     * @return expected method generic return type
     */
    Type getExpectedGenericType();

    /**
     *
     * @return expected method return type
     */
    Class<?> getExpectedType();

    /**
     * @param <T> Method's return type
     * @return the deserialized response to the expected method return type
     * @throws Exception Any exception thrown during deserialization process
     */
    <T> T deserialize() throws Exception;

    /**
     * @param type type to deserialize the response to
     * @param <T> type to deserialize the response to
     * @return deserialized response
     * @throws Exception Any exception thrown during deserialization process
     */
    <T> T to(Class<T> type) throws Exception;

    /**
     *
     * @param type type to deserialize the response to
     * @param genericType generic type to deserialize the response to
     * @param <T> type to deserialize the response to
     * @return deserialized response
     * @throws Exception Any exception thrown during deserialization process
     */
    <T> T to(Class<T> type, Type genericType) throws Exception;

    /**
     * @return underlying response entity stream
     * @throws Exception Any exception thrown during transaction
     */
    InputStream asStream() throws Exception;
}
