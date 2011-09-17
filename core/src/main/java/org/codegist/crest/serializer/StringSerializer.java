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

import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * Abstract serializer for scenarios where the serialized value can be dumped in memory (String)
 * @author laurent.gilles@codegist.org
 */
public abstract class StringSerializer<V> implements Serializer<V> {

    /**
     * Returns the string representation of the given value
     * @param value object to serialize
     * @param charset charset to use for serialization if applicable
     * @return the serialized object
     * @throws Exception Any exception thrown during serialization process
     */
    public abstract String serialize(V value, Charset charset) throws Exception;

    /**
     * @inheritDoc
     */
    public void serialize(V value, Charset charset, OutputStream out) throws Exception {
        out.write(serialize(value, charset).getBytes(charset));
    }

}
