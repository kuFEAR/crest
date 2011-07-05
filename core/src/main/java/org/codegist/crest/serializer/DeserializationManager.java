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

import org.codegist.common.io.IOs;
import org.codegist.common.log.Logger;
import org.codegist.crest.CRestException;
import org.codegist.crest.Registry;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

/**
 * @author laurent.gilles@codegist.org
 */
public final class DeserializationManager {

    private static final Logger LOG = Logger.getLogger(DeserializationManager.class);

    private final Registry<String,Deserializer> registryMime;

    public DeserializationManager(Registry<String,Deserializer> registryMime) {
        this.registryMime = registryMime;
    }

    public <T> T deserializeTo(Class<T> type, Type genericType, InputStream stream, String mimeType, Charset charset, Deserializer... deserializers) {
        try {
            if (deserializers.length > 0) {
                if (deserializers.length > 1) {// user specific unique expected mime type, worse scenario, need to dump response in memory to retry if deserialization fails
                    try {
                        stream = new ByteArrayInputStream(IOs.toByteArray(stream, true));
                    } catch (IOException e) {
                        throw CRestException.handle(e);
                    }
                }
                for (Deserializer deserializer : deserializers) { /*  */
                    try {
                        LOG.debug("Trying to deserialize response with user specified deserializer : %s.", deserializer);
                        return deserializer.<T>deserialize(type, genericType, stream, charset);
                    } catch (CRestException e) {
                        LOG.warn("Failed to deserialize response with user specified deserializer : %s. Trying next.", deserializer);
                    }
                }
                throw new CRestException("Could not deserialize response with given deserializers! (received content-type=" + mimeType + ")");
            }

            /* Either the user hasn't specified an expected mime type or deserialization attempts failed, we fallback to the server's Content-Type*/
            LOG.debug("Trying to deserialize response to server's Mime Type: %s.", mimeType);
            return registryMime.getFor(mimeType).<T>deserialize(type, genericType, stream, charset);
        } finally {
            IOs.close(stream);
        }
    }
}
