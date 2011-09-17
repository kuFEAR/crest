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

package org.codegist.crest.serializer;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

/**
 * Abstract serializer for scenarios where the deserializer will deserialize only to one type of object.
 * @author laurent.gilles@codegist.org
 */
public abstract class TypeDeserializer<T> implements Deserializer {

    /**
     * Deserialize the given input stream into the expected type using the given charset if applicable
     * @param stream stream to deserialize
     * @param charset charset to use to read the input stream if text-based
     * @return the deserialized input string
     * @throws Exception Any exception thrown during deserialization process
     */
    protected abstract T deserialize(InputStream stream, Charset charset) throws Exception;

    /**
     * @inheritDoc
     */
    public <T> T deserialize(Class<T> type, Type genericType, InputStream stream, Charset charset) throws Exception {
        return (T) deserialize(stream, charset);
    }

}
