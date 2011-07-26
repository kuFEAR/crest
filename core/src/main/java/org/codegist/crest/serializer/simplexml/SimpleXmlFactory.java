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

import static org.codegist.crest.CRestProperty.getBooleanFalse;
import static org.codegist.crest.CRestProperty.getBooleanTrue;
import static org.codegist.crest.CRestProperty.getDateFormat;

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
        String trueVal = getBooleanTrue(crestProperties);
        String falseVal = getBooleanFalse(crestProperties);
        String dateFormat = getDateFormat(crestProperties);

        Map<Class, Transform> registry = new HashMap<Class, Transform>();
        registry.put(Date.class, new DateMatcher(dateFormat));
        registry.put(Boolean.class, new BooleanMatcher(trueVal, falseVal));

        return new Persister(new MatcherRegistry(registry));
    }

}
