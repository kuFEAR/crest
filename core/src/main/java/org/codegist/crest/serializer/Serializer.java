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

package org.codegist.crest.serializer;

import org.codegist.crest.annotate.CRestComponent;

import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * <p>Serializers are used during serialization process of interface method's parameters</p>
 * <p>Serializers are CRest Components.</p>
 * @author laurent.gilles@codegist.org
 * @see org.codegist.crest.annotate.CRestComponent
 */
@CRestComponent
public interface Serializer<T> {

    /**
     * <p>Serializes the given object into the given outputstream.</p>
     * @param value value to serialize
     * @param charset charset to use to write in the outputstream is text-bases
     * @param out serialization destination
     * @throws Exception Any exception thrown during serialization
     */
    void serialize(T value, Charset charset, OutputStream out) throws Exception;

}
