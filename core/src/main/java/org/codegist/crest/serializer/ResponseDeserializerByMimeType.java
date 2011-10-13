/*
 * Copyright 2011 CodeGist.org
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
import org.codegist.crest.util.ComponentRegistry;

import static org.codegist.common.lang.Validate.isTrue;

/**
 * Response deserializer that uses a component registry of deserializers by response Content-Type mime-type.
 * @author laurent.gilles@codegist.org
 */
public class ResponseDeserializerByMimeType implements ResponseDeserializer {

    private static final String MIME_TYPE_NOT_FOUND_ERROR = new StringBuilder("Cannot deserialize response to response's mimeType '%s', cancelling deserialization.\n")
        .append("CRest has a predefined list of 'known' mime-type for common data type (ei:xml, json, plaintext). ")
        .append("If the server response if effectively one of these common types, but not part of CRest's default mime type lists, then you can build a CRest instance of follow:\n\n")
        .append("  CRest crest = new CRestBuilder().bindJsonDeserializerWith(\"server-given-mime-type\").build();\n")
        .append("or\n")
        .append("  CRest crest = new CRestBuilder().bindXmlDeserializerWith(\"server-given-mime-type\").build();\n")
        .append("or\n")
        .append("  CRest crest = new CRestBuilder().bindPlainTextDeserializerWith(\"server-given-mime-type\").build();\n\n")
        .append("This will add \"server-given-mime-type\" mime type to the prefedined list of common mime for the respective deserializer.\n")
        .append("Otherwise, if the mime type represent a custom type, then you can write your own deserializer and bind it as follow:\n\n")
        .append("  CRest crest = new CRestBuilder().bindDeserializer(MyOwnTypeDeserializer.class, \"server-given-mime-type\").build();").toString();

    private static final Logger LOG = Logger.getLogger(ResponseDeserializerByMimeType.class);
    private final ComponentRegistry<String, Deserializer> mimeDeserializerRegistry;

    /**
     * @param mimeDeserializerRegistry deserializers mime type registry to use
     */
    public ResponseDeserializerByMimeType(ComponentRegistry<String, Deserializer> mimeDeserializerRegistry) {
        this.mimeDeserializerRegistry = mimeDeserializerRegistry;
    }

    /**
     * @inheritDoc
     */
    public <T> T deserialize(Response response) throws Exception {
        String mimeType = response.getContentType();
        isTrue(mimeDeserializerRegistry.contains(mimeType), MIME_TYPE_NOT_FOUND_ERROR, mimeType);
        LOG.debug("Trying to deserialize response to Mime Type: %s.", mimeType);
        return mimeDeserializerRegistry.get(mimeType).<T>deserialize(
                (Class<T>) response.getExpectedType(),
                response.getExpectedGenericType(),
                response.asStream(),
                response.getCharset());
    }

}
