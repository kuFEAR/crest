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
import org.codegist.crest.io.Response;
import org.codegist.crest.util.Registry;

import static org.codegist.common.lang.Validate.isTrue;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class ResponseDeserializerByClass implements ResponseDeserializer {

    private static final String CLASS_NOT_FOUND_ERROR = new StringBuilder("Cannot deserialize response to class '%s', cancelling deserialization.\n")
        .append("This happens after response's Content-Type based deserialization have failed deserializing the response because of an unknown or not present response Content-Type.\n")
        .append("CRest has a predefined list of known classes for common data type (ei:primitives, InputStream, Reader etc...). ")
        .append("These deserializers are used when CRest cannot deserialize the response based on the server response's content-type.\n")
        .append("The method return type does not fall in the predefined list of known types. You can write your own deserializer and bind it as follow:\n\n")
        .append("  CRest crest = new CRestBuilder().bindDeserializer(MyOwnTypeDeserializer.class, MyOwnType.class).build();\n")
        .append("or, if the server can provide a Content-Type:\n")
        .append("  CRest crest = new CRestBuilder().bindDeserializer(MyOwnTypeDeserializer.class, \"the-content-type\").build();").toString();

    private static final Logger LOG = Logger.getLogger(ResponseDeserializerByClass.class);
    private final Registry<Class<?>, Deserializer> classDeserializerRegistry;

    public ResponseDeserializerByClass(Registry<Class<?>, Deserializer> classDeserializerRegistry) {
        this.classDeserializerRegistry = classDeserializerRegistry;
    }

    public <T> T deserialize(Response response) throws Exception {
        Class<T> type = (Class<T>) response.getExpectedType();
        isTrue(classDeserializerRegistry.contains(type), CLASS_NOT_FOUND_ERROR, type);
        LOG.debug("Trying to deserialize response to Type: %s.", type);
        return classDeserializerRegistry.get(type).<T>deserialize(
                type,
                response.getExpectedGenericType(),
                response.asStream(),
                response.getCharset());
    }
}
