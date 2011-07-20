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

package org.codegist.crest.io;

import org.codegist.common.log.Logger;
import org.codegist.crest.CRestException;
import org.codegist.crest.serializer.Deserializer;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;

import static org.codegist.common.io.IOs.toByteArray;
import static org.codegist.common.lang.Validate.isTrue;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class ResponseDeserializerByDeserializers implements ResponseDeserializer {

    private static final Logger LOG = Logger.getLogger(ResponseDeserializerByDeserializers.class);

    public <T> T deserialize(Response response) throws Exception {
        Deserializer[] deserializers = response.getRequest().getMethodConfig().getDeserializers();
        isTrue(deserializers.length > 0, "No pre-configured deserializers found for request's method, cancelling deserialization");

        InputStream pStream = response.asStream();
        if (deserializers.length > 1) {// user specific unique expected mime type, worse scenario, need to dump response in memory to retry if deserialization fails
            pStream = new ByteArrayInputStream(toByteArray(pStream, true));
        }
        for (Deserializer deserializer : deserializers) { /*  */
            try {
                LOG.debug("Trying to deserialize response with user specified deserializer: %s.", deserializer);
                return deserializer.<T>deserialize(
                        (Class<T>) response.getExpectedType(),
                        response.getExpectedGenericType(),
                        pStream,
                        response.getCharset());
            } catch (Exception e) {
                LOG.warn(e, "Failed to deserialize response with user specified deserializer: %s. Trying next.", deserializer);
            }
        }
        throw new CRestException("Could not deserialize response with given deserializers " + Arrays.toString(deserializers));
    }
}
