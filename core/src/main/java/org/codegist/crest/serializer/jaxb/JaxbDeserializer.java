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

package org.codegist.crest.serializer.jaxb;

import org.codegist.crest.serializer.Deserializer;
import org.codegist.crest.serializer.DeserializerException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * @author laurent.gilles@codegist.org
 */
public class JaxbDeserializer implements Deserializer {

    public static final String POOL_RETRIEVAL_MAX_WAIT_PROP = JaxbFactory.POOL_RETRIEVAL_MAX_WAIT_PROP;
    public static final String CUSTOM_JAXB = JaxbFactory.CUSTOM_JAXB;
    public static final String MODEL_PACKAGE = JaxbFactory.MODEL_PACKAGE;
    public static final String MODEL_FACTORY_CLASS = JaxbFactory.MODEL_FACTORY_CLASS;

    private final Jaxb jaxb;

    public JaxbDeserializer(Map<String, Object> config) {
        this.jaxb = JaxbFactory.create(config);
    }

    public <T> T deserialize(Class<T> type, Type genericType, InputStream stream, Charset charset) throws DeserializerException {
        return jaxb.<T>unmarshal(type, genericType, new InputStreamReader(stream, charset));
    }


}
