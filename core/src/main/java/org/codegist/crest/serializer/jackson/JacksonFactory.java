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

package org.codegist.crest.serializer.jackson;

import org.codegist.crest.CRestConfig;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import java.util.HashMap;
import java.util.Map;

final class JacksonFactory {

    static final String JACKSON_OBJECT_MAPPER = "#user-object-mapper";
    static final String JACKSON_DESERIALIZER_CONFIG = "#deserializer-config";
    static final String JACKSON_SERIALIZER_CONFIG= "#serializer-config";

    private static final Map<SerializationConfig.Feature, Boolean> DEFAULT_SERIALIZER_CONFIG = new HashMap<SerializationConfig.Feature, Boolean>();
    private static final Map<DeserializationConfig.Feature, Boolean> DEFAULT_DESERIALIZER_CONFIG = new HashMap<DeserializationConfig.Feature, Boolean>();
    static{
        DEFAULT_DESERIALIZER_CONFIG.put(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private JacksonFactory(){
        throw new IllegalStateException();
    }

    static ObjectMapper createObjectMapper(CRestConfig crestConfig, Class<?> source){
        String prefix = source.getName();

        ObjectMapper mapper = crestConfig.get(prefix + JACKSON_OBJECT_MAPPER);
        if(mapper != null){
            return mapper;
        }
        
        mapper = new ObjectMapper();

        Map<DeserializationConfig.Feature, Boolean> deserConfig = crestConfig.get(prefix + JACKSON_DESERIALIZER_CONFIG, DEFAULT_DESERIALIZER_CONFIG);
        for(Map.Entry<DeserializationConfig.Feature,Boolean> feature : deserConfig.entrySet()){
            mapper.configure(feature.getKey(), feature.getValue());
        }

        Map<SerializationConfig.Feature, Boolean> serConfig = crestConfig.get(prefix + JACKSON_SERIALIZER_CONFIG, DEFAULT_SERIALIZER_CONFIG);
        for(Map.Entry<SerializationConfig.Feature,Boolean> feature : serConfig.entrySet()){
            mapper.configure(feature.getKey(), feature.getValue());
        }

        return mapper;
    }

}
