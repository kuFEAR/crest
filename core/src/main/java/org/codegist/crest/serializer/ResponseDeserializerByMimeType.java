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
public class ResponseDeserializerByMimeType implements ResponseDeserializer {

    private static final Logger LOG = Logger.getLogger(ResponseDeserializerByMimeType.class);
    private final Registry<String, Deserializer> mimeDeserializerRegistry;

    public ResponseDeserializerByMimeType(Registry<String, Deserializer> mimeDeserializerRegistry) {
        this.mimeDeserializerRegistry = mimeDeserializerRegistry;
    }

    public <T> T deserialize(Response response) throws Exception {
        String mimeType = response.getContentType();
        isTrue(mimeDeserializerRegistry.contains(mimeType), "Unknown mimeType %s, cancelling deserialization", mimeType);
        LOG.debug("Trying to deserialize response to Mime Type: %s.", mimeType);
        return mimeDeserializerRegistry.get(mimeType).<T>deserialize(
                (Class<T>) response.getExpectedType(),
                response.getExpectedGenericType(),
                response.asStream(),
                response.getCharset());
    }

}
