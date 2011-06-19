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

package org.codegist.crest.serializer.simplexml;

import org.codegist.common.collect.Maps;
import org.codegist.crest.CRestProperty;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.transform.Matcher;
import org.simpleframework.xml.transform.Transform;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author laurent.gilles@codegist.org
 */
public class SimpleXmlFactory {

    private static final String SERIALIZER_PREFIX = "simplexml-serializer";
    private static final String DESERIALIZER_PREFIX = "simplexml-deserializer";

    static final String USER_SERIALIZER_PROP =  "user-serializer";

    public static Serializer createSerializer(Map<String,Object> cfg){
        return create(cfg, SERIALIZER_PREFIX);
    }

    static Serializer createDeserializer(Map<String,Object> cfg){
        return create(cfg, DESERIALIZER_PREFIX);
    }

    private static Serializer create(Map<String,Object> cfg, String prefix){
        Map<String,Object> config = Maps.defaultsIfNull(cfg);

        Serializer serializer;
        if(config.containsKey(prefix + "#" + USER_SERIALIZER_PROP)) {
            serializer = (org.simpleframework.xml.Serializer) config.get(prefix + "#" + USER_SERIALIZER_PROP);
        }else{
            MatcherRegistry.Builder registry = new MatcherRegistry.Builder();
            if(config.containsKey(CRestProperty.CREST_DATE_FORMAT)) {
                registry.bind(Date.class, new DateMatcher((String) config.get(CRestProperty.CREST_DATE_FORMAT)));
            }
            if(config.containsKey(CRestProperty.CREST_BOOLEAN_FALSE) && config.containsKey(CRestProperty.CREST_BOOLEAN_TRUE)) {
                String trueVal = ((String)config.get(CRestProperty.CREST_BOOLEAN_TRUE));
                String falseVal = ((String)config.get(CRestProperty.CREST_BOOLEAN_FALSE));
                registry.bind(Boolean.class, new BooleanMatcher(trueVal, falseVal));
            }
            if(registry.hasTransformers()) {
                serializer = new Persister(registry.build());
            }else{
                serializer = new Persister();
            }
        }
        return serializer;
    }

    private static class MatcherRegistry implements Matcher {
        private final Map<Class, Transform> transformerMap;

        private MatcherRegistry(Map<Class, Transform> transformerMap) {
            this.transformerMap = transformerMap;
        }

        private static class Builder {
            private final Map<Class, Transform> transformerMap = new HashMap<Class, Transform>();

            public <T> Builder bind(Class<T> clazz, Transform<T> transform) {
                transformerMap.put(clazz, transform);
                return this;
            }

            public boolean hasTransformers(){
                return !transformerMap.isEmpty();
            }

            public MatcherRegistry build() {
                return new MatcherRegistry(transformerMap);
            }
        }

        public Transform match(Class type) throws Exception {
            return transformerMap.get(type);
        }
    }

    private static class BooleanMatcher implements Transform<Boolean> {
        private final String trueVal;
        private final String falseVal;

        private BooleanMatcher(String trueVal, String falseVal) {
            this.trueVal = trueVal;
            this.falseVal = falseVal;
        }

        public Boolean read(String value) throws Exception {
            return !falseVal.equals(value);
        }

        public String write(Boolean value) throws Exception {
            return Boolean.TRUE.equals(value) ? trueVal : falseVal;
        }
    }

    public static class DateMatcher implements Transform<Date> {
        private final DateFormat DF;

        private DateMatcher(String format) {
            this.DF = new SimpleDateFormat(format);
        }

        public synchronized Date read(String value) throws Exception {
            return DF.parse(value);
        }

        public synchronized String write(Date value) throws Exception {
            return DF.format(value);
        }
    }
}
