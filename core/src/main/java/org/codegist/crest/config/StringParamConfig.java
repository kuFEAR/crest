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

package org.codegist.crest.config;

import org.codegist.crest.serializer.Serializer;
import org.codegist.crest.serializer.ToStringSerializer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Map;

public class StringParamConfig implements ParamConfig {
            
    private static final Serializer<String> TO_STRING_SERIALIZER = new ToStringSerializer<String>();
    private final String name;
    private final String value;
    private final String destination;
    private final boolean encoded;

    public StringParamConfig(String name, String value, String destination, boolean encoded) {
        this.name = name;
        this.value = value;
        this.destination = destination;
        this.encoded = encoded;
    }

    public Type getValueGenericType() {
        return String.class;
    }

    public Class<?> getValueClass() {
        return String.class;
    }

    public String getName() {
        return name;
    }

    public String getDefaultValue() {
        return value;
    }

    public String getDestination() {
        return destination;
    }

    public Serializer getSerializer() {
        return TO_STRING_SERIALIZER;
    }

    public Boolean isEncoded() {
        return encoded;
    }

    public String getListSeparator() {
        return null;
    }

    public Map<String, Object> getMetaDatas() {
        return null;
    }

    public Map<Class<? extends Annotation>, Annotation> getAnnotations() {
        return null;
    }
}