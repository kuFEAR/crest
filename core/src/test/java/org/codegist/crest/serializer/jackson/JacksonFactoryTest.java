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

package org.codegist.crest.serializer.jackson;

import org.codegist.crest.NonInstanciableClassTest;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * @author laurent.gilles@codegist.org
 */
public class JacksonFactoryTest extends NonInstanciableClassTest {

    private final Map<String,Object> crestProperties = new HashMap<String, Object>();


    public JacksonFactoryTest() {
        super(JacksonFactory.class);
    }

    @Test
    public void shouldNotCreateAnObjectMapperButUseTheCustomOn(){
        ObjectMapper mockObjectMapper = mock(ObjectMapper.class);
        crestProperties.put(JacksonFactoryTest.class.getName() + JacksonFactory.JACKSON_OBJECT_MAPPER, mockObjectMapper);
        ObjectMapper actual = JacksonFactory.createObjectMapper(crestProperties, getClass());
        assertEquals(mockObjectMapper, actual);
    }

    @Test
    public void shouldCreateAnObjectMapperWithDefaultConfig(){
        ObjectMapper actual = JacksonFactory.createObjectMapper(crestProperties, getClass());
        assertNotNull(actual);
        assertFalse(actual.getDeserializationConfig().isEnabled(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES));
    }

    @Test
    public void shouldCreateAnObjectMapperWithGivenConfig(){
        Map<DeserializationConfig.Feature, Boolean> deserialization = new HashMap<DeserializationConfig.Feature, Boolean>();
        Map<SerializationConfig.Feature, Boolean> serialization = new HashMap<SerializationConfig.Feature, Boolean>();
        deserialization.put(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        deserialization.put(DeserializationConfig.Feature.AUTO_DETECT_SETTERS, false);
        serialization.put(SerializationConfig.Feature.AUTO_DETECT_IS_GETTERS, false);
        serialization.put(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);

        crestProperties.put(JacksonFactoryTest.class.getName() + JacksonFactory.JACKSON_DESERIALIZER_CONFIG, deserialization);
        crestProperties.put(JacksonFactoryTest.class.getName() + JacksonFactory.JACKSON_SERIALIZER_CONFIG, serialization);

        ObjectMapper actual = JacksonFactory.createObjectMapper(crestProperties, getClass());
        assertNotNull(actual);
        assertTrue(actual.getDeserializationConfig().isEnabled(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES));
        assertFalse(actual.getDeserializationConfig().isEnabled(DeserializationConfig.Feature.AUTO_DETECT_SETTERS));
        assertFalse(actual.getSerializationConfig().isEnabled(SerializationConfig.Feature.AUTO_DETECT_IS_GETTERS));
        assertFalse(actual.getSerializationConfig().isEnabled(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS));
    }
}
