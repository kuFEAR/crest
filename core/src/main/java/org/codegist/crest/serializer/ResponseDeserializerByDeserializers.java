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
import org.codegist.crest.CRestException;
import org.codegist.crest.config.MethodConfig;
import org.codegist.crest.io.Response;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;

import static org.codegist.common.io.IOs.toByteArray;
import static org.codegist.common.lang.Validate.isTrue;

/**
 * <p>Response deserializer that uses the {@link org.codegist.crest.config.MethodConfig#getDeserializers()} list to try to deserialize the response.</p>
 * <p>In most cases, the deserializer array will contain only one item and the deserialization can be done in a streaming fashion.</p>
 * <p>But if for some reason a rest interface has been annotated with {@link org.codegist.crest.annotate.Consumes} with more than one value, then the response deserialize will first dump the response in memory in order to try to deserialize with deserializers until first success</p>
 * @author laurent.gilles@codegist.org
 * @see org.codegist.crest.annotate.Consumes
 */
public class ResponseDeserializerByDeserializers implements ResponseDeserializer {

    private static final Logger LOG = Logger.getLogger(ResponseDeserializerByDeserializers.class);
    private static final String NO_DESERIALIZERS_ERROR = new StringBuilder("No deserializers have been configured for the method config (%s), cancelling deserialization.\n")
        .append("This happens after both response's Content-Type and method's return type based deserializations have failed deserializing the response.\n")
        .append("As a last attempt, CRest will try to use the predefined deserializer for the method, defined using @Consumes(\"some-mime-type\") or @Deserializer(MyDeserializer.class) annotations. ")
        .append("If not present, then deserialization is not possible at this point.\n")
        .append("To fix it, try one of the following:\n")
        .append("  - Ensure the response has a known Content-Type.\n")
        .append("  - If response Content-Type is unknown, bind it through CRestBuilder using either a common deserializer or providing your own.\n")
        .append("  - If response Content-Type cannot be changed, bind a deserializer either through @Consumes(\"some-mime-type\") or @Deserializer(MyDeserializer.class) annotation.").toString();

    /**
     * @inheritDoc
     */
    public <T> T deserialize(Response response) throws Exception {
        MethodConfig mc = response.getRequest().getMethodConfig();
        Deserializer[] deserializers = mc.getDeserializers();
        isTrue(deserializers.length > 0, NO_DESERIALIZERS_ERROR, mc);

        InputStream pStream = response.asStream();
        boolean isDumped;
        if (deserializers.length > 1) {
            // user specified more than one serializer: worse case scenario, need to dump response in memory to retry if deserialization fails
            pStream = new ByteArrayInputStream(toByteArray(pStream, true));
            isDumped = true;
        }else{
            isDumped = false;
        }
        Exception deserializationException = null;
        for (Deserializer deserializer : deserializers) {
            try {
                LOG.debug("Trying to deserialize response with user specified deserializer: %s.", deserializer);
                return deserializer.<T>deserialize(
                        (Class<T>) response.getExpectedType(),
                        response.getExpectedGenericType(),
                        pStream,
                        response.getCharset());
            } catch (Exception e) {
                LOG.warn(e, "Failed to deserialize response with user specified deserializer: %s. Trying next.", deserializer);
                deserializationException = e;
                if(isDumped) {
                    pStream.mark(0);
                    pStream.reset();
                }
            }
        }
        throw new CRestException("Could not deserialize response with given deserializers " + Arrays.toString(deserializers), deserializationException);
    }
}
