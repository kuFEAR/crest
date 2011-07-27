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

import org.codegist.common.log.Logger;
import org.codegist.crest.CRestConfig;
import org.codegist.crest.io.Response;
import org.codegist.crest.util.Registry;

import static org.codegist.common.lang.Validate.isTrue;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class ResponseDeserializerByClass implements ResponseDeserializer {

    private static final Logger LOG = Logger.getLogger(ResponseDeserializerByClass.class);
    private final Registry<Class<?>, Deserializer> classDeserializerRegistry;

    public ResponseDeserializerByClass(Registry<Class<?>, Deserializer> classDeserializerRegistry) {
        this.classDeserializerRegistry = classDeserializerRegistry;
    }

    public <T> T deserialize(CRestConfig crestConfig,Response response) throws Exception {
        Class<T> type = (Class<T>) response.getExpectedType();
        isTrue(classDeserializerRegistry.contains(type), "Unknown class %s, cancelling deserialization", type);
        LOG.debug("Trying to deserialize response to Type: %s.", type);
        return classDeserializerRegistry.get(type, crestConfig).<T>deserialize(
                type,
                response.getExpectedGenericType(),
                response.asStream(),
                response.getCharset());
    }
}
