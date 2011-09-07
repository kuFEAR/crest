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
 *  ==================================================================
 *
 *  More information at http://www.codegist.org.
 */

package org.codegist.crest.serializer;

import org.codegist.crest.annotate.CRestComponent;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

/**
 * <p>Deserializers are used during response deserialization process</p>
 * <p>Deserializers are CRest Components.</p>
 * @author laurent.gilles@codegist.org
 * @see org.codegist.crest.annotate.CRestComponent
 */
@CRestComponent
public interface Deserializer {

    /**
     * <p>Deserializes the given input stream to the given type.</p>
     * <p>Implementation is responsible for closing the given input stream.</p>
     * @param type the type to deserialize to
     * @param genericType the generic type to deserialize to
     * @param stream the input stream to deserialize
     * @param charset the input stream charset if text-based
     * @param <T> the type to deserialize to
     * @return the deserialized input stream
     * @throws Exception Any exception thrown during deserialization
     */
    <T> T deserialize(Class<T> type, Type genericType, InputStream stream, Charset charset) throws Exception;

}
