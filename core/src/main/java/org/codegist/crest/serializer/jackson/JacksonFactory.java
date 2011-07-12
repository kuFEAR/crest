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

import org.codegist.common.lang.Validate;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import java.util.Map;

final class JacksonFactory {

    static final String USER_OBJECT_MAPPER_PROP = "user-object-mapper";
    static final String DESERIALIZATION_CONFIG_MAP_PROP = "deserialization-config-map";
    static final String SERIALIZATION_CONFIG_MAP_PROP = "serialization-config-map";

    private JacksonFactory(){
        throw new IllegalStateException();
    }

    static ObjectMapper createDeserializer(Map<String,Object> config){
        Validate.notNull(config, "Config must not be null");
        String prefix = "jackson-deserializer#";
        ObjectMapper mapper;
        if(config.containsKey(prefix + USER_OBJECT_MAPPER_PROP)) {
            mapper = (ObjectMapper) config.get(prefix + USER_OBJECT_MAPPER_PROP);
        }else{
            mapper = new ObjectMapper();
        }
        if(config.containsKey(prefix + DESERIALIZATION_CONFIG_MAP_PROP)) {
            Map<String, Boolean> deserializationConfig = (Map<String, Boolean>) config.get(prefix + DESERIALIZATION_CONFIG_MAP_PROP);
            for (Map.Entry<String, Boolean> entry : deserializationConfig.entrySet()) {
                mapper = mapper.configure(DeserializationConfig.Feature.valueOf(entry.getKey()), entry.getValue());
            }
        }else{
            mapper = mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }
        return mapper;
    }

    static ObjectMapper createSerializer(Map<String,Object> config){
        Validate.notNull(config, "Config must not be null");
        String prefix = "jackson-serializer#";
        ObjectMapper mapper;
        if(config.containsKey(prefix + USER_OBJECT_MAPPER_PROP)) {
            mapper = (ObjectMapper) config.get(prefix + USER_OBJECT_MAPPER_PROP);
        }else{
            mapper = new ObjectMapper();
        }
        if(config.containsKey(prefix + SERIALIZATION_CONFIG_MAP_PROP)) {
            Map<String, Boolean> serializationConfig = (Map<String, Boolean>) config.get(prefix + SERIALIZATION_CONFIG_MAP_PROP);
            for (Map.Entry<String, Boolean> entry : serializationConfig.entrySet()) {
                mapper = mapper.configure(SerializationConfig.Feature.valueOf(entry.getKey()), entry.getValue());
            }
        }
        return mapper;
    }

}
