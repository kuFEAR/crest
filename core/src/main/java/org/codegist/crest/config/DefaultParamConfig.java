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

package org.codegist.crest.config;

import org.codegist.common.lang.ToStringBuilder;
import org.codegist.crest.io.http.param.ParamProcessor;
import org.codegist.crest.io.http.param.ParamType;
import org.codegist.crest.serializer.Serializer;

import java.lang.reflect.Type;
import java.util.Map;

import static org.codegist.common.collect.Maps.unmodifiable;

/**
 * Default immutable in-memory implementation of {@link ParamConfig}
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
    private final Boolean encoded;
    private final ParamProcessor paramProcessor;


    DefaultParamConfig(Type type, Class<?> clazz, String name, String defaultValue, ParamType paramType, Map<String,Object> metadatas, Serializer serializer, Boolean encoded, ParamProcessor paramProcessor) {
        this.genericType = type;
        this.clazz = clazz;
        this.name = name;
        this.defaultValue = defaultValue;
        this.paramType = paramType;
        this.serializer = serializer;
        this.encoded = encoded;
        this.paramProcessor = paramProcessor;
        this.metadatas = unmodifiable(metadatas, false);
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

    public Boolean isEncoded() {
        return encoded;
    }

    public ParamProcessor getParamProcessor() {
        return paramProcessor;
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append("clazz", clazz)
                .append("genericType", genericType)
                .append("name", name)
                .append("defaultValue", defaultValue)
                .append("paramType", paramType)
                .append("metadatas", metadatas)
                .append("serializer", serializer)
                .append("encoded", encoded)
                .append("paramProcessor", paramProcessor)
                .toString();
    }
}
