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

import org.codegist.crest.CRestProperty;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import java.util.Map;

final class JacksonFactory {

    static final String JACKSON_OBJECT_MAPPER = "#user-object-mapper";
    static final String JACKSON_FEATURES = "#features";

    private JacksonFactory(){
        throw new IllegalStateException();
    }

    static ObjectMapper createDeserializer(Map<String,Object> crestProperties, Class<?> source){
        String prefix = source.getName();
        ObjectMapper mapper = getObjectMapper(crestProperties, prefix);
        if(crestProperties.containsKey(prefix + JACKSON_FEATURES)) {
            for (Map.Entry<String, Boolean> entry : CRestProperty.<Map<String, Boolean>>get(crestProperties, prefix + JACKSON_FEATURES).entrySet()) {
                mapper = mapper.configure(DeserializationConfig.Feature.valueOf(entry.getKey()), entry.getValue());
            }
        }else{
            mapper = mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }
        return mapper;
    }

    static ObjectMapper createSerializer(Map<String,Object> crestProperties, Class<?> source){
        String prefix = source.getName();
        ObjectMapper mapper = getObjectMapper(crestProperties, prefix);
        if(crestProperties.containsKey(prefix + JACKSON_FEATURES)) {
            for (Map.Entry<String, Boolean> entry : CRestProperty.<Map<String, Boolean>>get(crestProperties, prefix + JACKSON_FEATURES).entrySet()) {
                mapper = mapper.configure(SerializationConfig.Feature.valueOf(entry.getKey()), entry.getValue());
            }
        }
        return mapper;
    }

    private static ObjectMapper getObjectMapper(Map<String,Object> crestProperties, String prefix){
        ObjectMapper mapper = CRestProperty.get(crestProperties, prefix + JACKSON_OBJECT_MAPPER);
        if(mapper == null){
            mapper = new ObjectMapper();
        }
        return mapper;
    }

}
