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

import org.codegist.crest.param.ParamProcessor;
import org.codegist.crest.serializer.Serializer;

import java.lang.reflect.Type;
import java.util.Map;

import static java.util.Collections.unmodifiableMap;

/**
 * Default immutable in-memory implementation of {@link org.codegist.crest.config.ParamConfig}
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
class DefaultParamConfig implements ParamConfig {

    private final Type genericType;
    private final Class<?> clazz;
    private final String name;
    private final String defaultValue;
    private final ParamType paramType;
    private final Map<String,Object> metadatas;
    private final Serializer serializer;
    private final boolean encoded;
    private final ParamProcessor paramProcessor;


    DefaultParamConfig(Type type, Class<?> clazz, String name, String defaultValue, ParamType paramType, Map<String,Object> metadatas, Serializer serializer, boolean encoded, ParamProcessor paramProcessor) {
        this.genericType = type;
        this.clazz = clazz;
        this.name = name;
        this.defaultValue = defaultValue;
        this.paramType = paramType;
        this.serializer = serializer;
        this.encoded = encoded;
        this.paramProcessor = paramProcessor;
        this.metadatas = unmodifiableMap(metadatas);
    }

    public Type getValueGenericType() {
        return genericType;
    }

    public Class<?> getValueClass() {
        return clazz;
    }

    public String getName() {
        return name;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public ParamType getType() {
        return paramType;
    }

    public Map<String, Object> getMetaDatas() {
        return metadatas;
    }

    public Serializer getSerializer() {
        return serializer;
    }

    public boolean isEncoded() {
        return encoded;
    }

    public ParamProcessor getParamProcessor() {
        return paramProcessor;
    }
}
