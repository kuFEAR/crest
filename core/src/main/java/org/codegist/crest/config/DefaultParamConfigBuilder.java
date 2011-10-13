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

package org.codegist.crest.config;

import org.codegist.common.lang.State;
import org.codegist.common.lang.ToStringBuilder;
import org.codegist.common.reflect.Types;
import org.codegist.crest.CRestConfig;
import org.codegist.crest.param.ParamProcessor;
import org.codegist.crest.param.ParamProcessors;
import org.codegist.crest.serializer.Serializer;
import org.codegist.crest.util.ComponentRegistry;
import org.codegist.crest.util.MultiParts;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static org.codegist.crest.config.ParamConfig.*;
import static org.codegist.crest.config.ParamType.COOKIE;
import static org.codegist.crest.config.ParamType.HEADER;

@SuppressWarnings("unchecked")
class DefaultParamConfigBuilder extends ConfigBuilder implements ParamConfigBuilder {

    private final MethodConfigBuilder parent;
    private final Class<?> clazz;
    private final Type genericType;
    private final ComponentRegistry<Class<?>, Serializer> classSerializerRegistry;
    private final Class<? extends ParamProcessor> paramProcessor;
    private final Map<String,Object> metas = new HashMap<String,Object>();

    private String name = null;
    private String defaultValue = null;
    private ParamType type = ParamType.getDefault();
    private String listSeparator = null;
    private Class<? extends Serializer> serializer = null;
    private Boolean encoded = false;


    DefaultParamConfigBuilder(CRestConfig crestConfig, ComponentRegistry<Class<?>, Serializer> classSerializerRegistry, Class<?> clazz, Type genericType) {
        this(crestConfig, classSerializerRegistry, clazz, genericType, null);
    }
    
    DefaultParamConfigBuilder(CRestConfig crestConfig, ComponentRegistry<Class<?>, Serializer> classSerializerRegistry, Class<?> clazz, Type genericType, MethodConfigBuilder parent) {
        super(crestConfig);
        this.parent = parent;
        this.clazz = Types.getComponentClass(clazz, genericType);
        this.genericType = Types.getComponentType(clazz, genericType);
        this.classSerializerRegistry = classSerializerRegistry;

        this.name = override(PARAM_CONFIG_DEFAULT_NAME, this.name);
        this.defaultValue = override(PARAM_CONFIG_DEFAULT_VALUE, this.defaultValue);
        this.type = override(PARAM_CONFIG_DEFAULT_TYPE, this.type);
        this.listSeparator = override(PARAM_CONFIG_DEFAULT_LIST_SEPARATOR, this.listSeparator);
        this.serializer = override(PARAM_CONFIG_DEFAULT_SERIALIZER, this.serializer);
        this.encoded = override(PARAM_CONFIG_DEFAULT_ENCODED, this.encoded);
        this.paramProcessor = override(PARAM_CONFIG_DEFAULT_PROCESSOR, null);

        Map<String,Object> pMetas = override(PARAM_CONFIG_DEFAULT_METAS, this.metas);
        if(pMetas != this.metas) {
            this.metas.clear();
            this.metas.putAll(pMetas);
        }
    }

    /**
     * @inheritDoc
     */
    public ParamConfig build() throws Exception {
        validate();
        return new DefaultParamConfig(
                genericType,
                clazz,
                name,
                defaultValue,
                type,
                metas,
                serializer != null ?  instantiate(serializer) : classSerializerRegistry.get(clazz),
                (COOKIE.equals(type) || HEADER.equals(type)) ? true : encoded,
                paramProcessor != null ? instantiate(paramProcessor) : ParamProcessors.newInstance(type, listSeparator));
    }

    private void validate(){
        State.notBlank(name, "Parameter name is mandatory. This is probably due to a missing or empty named param annotation (one of the following: @CookieParam, @FormParam, @HeaderParam, @MatrixParam, @MultiPartParam, @PathParam, @QueryParam).\nLocation information:\n%s", this);
    }

    public ParamConfigBuilder setName(String name) {
        this.name = name;
        return this;
    }


    public ParamConfigBuilder setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public ParamConfigBuilder setType(ParamType type) {
        this.type = type;
        return this;
    }

    public ParamConfigBuilder setListSeparator(String listSeparator) {
        this.listSeparator = listSeparator;
        return this;
    }

    public ParamConfigBuilder setEncoded(boolean encoded) {
        this.encoded = encoded;
        return this;
    }

    public ParamConfigBuilder setMetaDatas(Map<String,Object> metadatas) {
        this.metas.clear();
        this.metas.putAll(metadatas);
        return this;
    }

    public ParamConfigBuilder setSerializer(Class<? extends Serializer> serializerClass) {
        this.serializer = serializerClass;
        return this;
    }

    public ParamConfigBuilder forCookie() {
        return setType(ParamType.COOKIE);
    }

    public ParamConfigBuilder forQuery() {
        return setType(ParamType.QUERY);
    }

    public ParamConfigBuilder forPath() {
        return setType(ParamType.PATH);
    }

    public ParamConfigBuilder forForm() {
        return setType(ParamType.FORM);
    }

    public ParamConfigBuilder forHeader() {
        return setType(ParamType.HEADER);
    }

    public ParamConfigBuilder forMatrix() {
        return setType(ParamType.MATRIX);
    }

    public ParamConfigBuilder forMultiPart() {
        MultiParts.asMultipart(this.metas);
        return forForm();
    }

    @Override
    public String toString() {
        return new ToStringBuilder("Param")
                .append("class", clazz)
                .append("type", type)
                .append("method", parent)
                .toString();
    }
}