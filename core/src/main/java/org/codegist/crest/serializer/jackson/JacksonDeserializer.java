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

package org.codegist.crest.serializer.jackson;

import org.codegist.common.io.IOs;
import org.codegist.crest.serializer.Deserializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * @author laurent.gilles@codegist.org
 */
public class JacksonDeserializer implements Deserializer {

    private static final String PREFIX = JacksonDeserializer.class.getName();
    public static final String OBJECT_MAPPER_PROP = PREFIX + JacksonFactory.JACKSON_OBJECT_MAPPER;
    public static final String FEATURES_PROP = PREFIX + JacksonFactory.JACKSON_FEATURES;

    private final ObjectMapper jackson;

    public JacksonDeserializer(Map<String, Object> crestProperties) {
        this.jackson = JacksonFactory.createDeserializer(crestProperties, getClass());
    }

    public <T> T deserialize(Class<T> type, Type genericType, InputStream stream, Charset charset) throws IOException {
        try {
            return jackson.<T>readValue(new InputStreamReader(stream, charset), TypeFactory.type(genericType));
        } finally {
            IOs.close(stream);
        }
    }
}
