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

import org.codegist.crest.CRestProperty;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.transform.Matcher;
import org.simpleframework.xml.transform.Transform;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author laurent.gilles@codegist.org
 */
final class SimpleXmlFactory {

    static final String SERIALIZER =  "#serializer";

    private SimpleXmlFactory(){
        throw new IllegalStateException();
    }

    static Serializer createSerializer(Map<String,Object> crestProperties, Class<?> source){
        return create(crestProperties, source);
    }

    static Serializer createDeserializer(Map<String,Object> crestProperties, Class<?> source){
        return create(crestProperties, source);
    }

    private static Serializer create(Map<String,Object> crestProperties, Class<?> source){
        String prefix = source.getName();
        Serializer serializer =  CRestProperty.get(crestProperties, prefix + "#" + SERIALIZER);
        if(serializer != null) {
            return serializer;
        }

        MatcherRegistry.Builder registry = new MatcherRegistry.Builder();
        registry.bind(Date.class, new DateMatcher(CRestProperty.getDateFormat(crestProperties)));

        String trueVal = CRestProperty.getBooleanTrue(crestProperties);
        String falseVal = CRestProperty.getBooleanFalse(crestProperties);
        registry.bind(Boolean.class, new BooleanMatcher(trueVal, falseVal));

        if(registry.hasTransformers()) {
            serializer = new Persister(registry.build());
        }else{
            serializer = new Persister();
        }
        return serializer;
    }

    private static final class MatcherRegistry implements Matcher {
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

    private static final class BooleanMatcher implements Transform<Boolean> {
        private final String trueVal;
        private final String falseVal;

        private BooleanMatcher(String trueVal, String falseVal) {
            this.trueVal = trueVal;
            this.falseVal = falseVal;
        }

        public Boolean read(String value) {
            return !falseVal.equals(value);
        }

        public String write(Boolean value) {
            return Boolean.TRUE.equals(value) ? trueVal : falseVal;
        }
    }

    private static final class DateMatcher implements Transform<Date> {
        private final DateFormat df;

        private DateMatcher(String format) {
            this.df = new SimpleDateFormat(format);
        }

        public synchronized Date read(String value) throws ParseException {
            return df.parse(value);
        }

        public synchronized String write(Date value) {
            return df.format(value);
        }
    }
}
