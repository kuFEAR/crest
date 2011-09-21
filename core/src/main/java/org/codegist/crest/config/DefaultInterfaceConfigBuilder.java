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

import org.codegist.common.lang.ToStringBuilder;
import org.codegist.crest.CRestConfig;
import org.codegist.crest.entity.EntityWriter;
import org.codegist.crest.handler.ErrorHandler;
import org.codegist.crest.handler.ResponseHandler;
import org.codegist.crest.handler.RetryHandler;
import org.codegist.crest.interceptor.RequestInterceptor;
import org.codegist.crest.serializer.Deserializer;
import org.codegist.crest.serializer.Serializer;
import org.codegist.crest.util.ComponentRegistry;

import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

class DefaultInterfaceConfigBuilder extends ConfigBuilder implements InterfaceConfigBuilder {

    private final Class interfaze;
    private final Map<Method, MethodConfigBuilder> methodBuilders;


    public DefaultInterfaceConfigBuilder(Class interfaze, CRestConfig crestConfig, ComponentRegistry<String,Deserializer> mimeDeserializerRegistry, ComponentRegistry<Class<?>, Serializer> classSerializerRegistry) {
        super(crestConfig);
        this.interfaze = interfaze;
        this.methodBuilders = new HashMap<Method, MethodConfigBuilder>();
        for (Method m : interfaze.getDeclaredMethods()) {
            this.methodBuilders.put(m, new DefaultMethodConfigBuilder(this, m, crestConfig, mimeDeserializerRegistry, classSerializerRegistry));
        }
    }

    public InterfaceConfig build() throws Exception {
        Map<Method, MethodConfig> mConfig = new HashMap<Method, MethodConfig>();
        for (Map.Entry<Method, MethodConfigBuilder> entry : methodBuilders.entrySet()) {
            mConfig.put(entry.getKey(), entry.getValue().build());
        }
        return new DefaultInterfaceConfig(interfaze, mConfig);
    }

    public MethodConfigBuilder startMethodConfig(Method meth) {
        return this.methodBuilders.get(meth);
    }

    /* METHODS SETTINGS METHODS */


    public InterfaceConfigBuilder setMethodsCharset(Charset charset) {
        for (MethodConfigBuilder b : methodBuilders.values()) {
            b.setCharset(charset);
        }
        return this;
    }

    public InterfaceConfigBuilder setMethodsSocketTimeout(int socketTimeout) {
        for (MethodConfigBuilder b : methodBuilders.values()) {
            b.setSocketTimeout(socketTimeout);
        }
        return this;
    }

    public InterfaceConfigBuilder setMethodsConnectionTimeout(int connectionTimeout) {
        for (MethodConfigBuilder b : methodBuilders.values()) {
            b.setConnectionTimeout(connectionTimeout);
        }
        return this;
    }

    public InterfaceConfigBuilder setMethodsRequestInterceptor(Class<? extends RequestInterceptor> requestInterceptorClass)  {
        for (MethodConfigBuilder b : methodBuilders.values()) {
            b.setRequestInterceptor(requestInterceptorClass);
        }
        return this;
    }

    public InterfaceConfigBuilder setMethodsResponseHandler(Class<? extends ResponseHandler> responseHandlerClass)  {
        for (MethodConfigBuilder b : methodBuilders.values()) {
            b.setResponseHandler(responseHandlerClass);
        }
        return this;
    }

    public InterfaceConfigBuilder setMethodsErrorHandler(Class<? extends ErrorHandler> errorHandlerClass)  {
        for (MethodConfigBuilder b : methodBuilders.values()) {
            b.setErrorHandler(errorHandlerClass);
        }
        return this;
    }

    public InterfaceConfigBuilder setMethodsRetryHandler(Class<? extends RetryHandler> retryHandlerClass)  {
        for (MethodConfigBuilder b : methodBuilders.values()) {
            b.setRetryHandler(retryHandlerClass);
        }
        return this;
    }

    public InterfaceConfigBuilder setMethodsEntityWriter(Class<? extends EntityWriter> entityWriterClass)  {
        for (MethodConfigBuilder b : methodBuilders.values()) {
            b.setEntityWriter(entityWriterClass);
        }
        return this;
    }


    public InterfaceConfigBuilder setMethodsConsumes(String... mimeTypes) {
        for (MethodConfigBuilder b : methodBuilders.values()) {
            b.setConsumes(mimeTypes);
        }
        return this;
    }

    public InterfaceConfigBuilder setMethodsDeserializer(Class<? extends Deserializer> deserializerClass) {
        for (MethodConfigBuilder b : methodBuilders.values()) {
            b.setDeserializer(deserializerClass);
        }
        return this;
    }

    public InterfaceConfigBuilder setMethodsProduces(String contentType) {
        for (MethodConfigBuilder b : methodBuilders.values()) {
            b.setProduces(contentType);
        }
        return this;
    }

    public InterfaceConfigBuilder setMethodsType(MethodType meth) {
        for (MethodConfigBuilder b : methodBuilders.values()) {
            b.setType(meth);
        }
        return this;
    }


    public InterfaceConfigBuilder appendMethodsPath(String path) {
        for (MethodConfigBuilder b : methodBuilders.values()) {
            b.appendPath(path);
        }
        return this;
    }

    public InterfaceConfigBuilder setMethodsEndPoint(String endPoint) {
        for (MethodConfigBuilder b : methodBuilders.values()) {
            b.setEndPoint(endPoint);
        }
        return this;
    }



    /* PARAMS SETTINGS METHODS */

    public ParamConfigBuilder startMethodsExtraParamConfig() {
        Iterator<MethodConfigBuilder> methodConfigBuilders = methodBuilders.values().iterator();
        ParamConfigBuilder[] builders = new ParamConfigBuilder[methodBuilders.size()];
        int i = 0;
        while(methodConfigBuilders.hasNext()){
            builders[i++] = methodConfigBuilders.next().startExtraParamConfig();
        }
        return new CompositeParamConfigBuilder(builders);
    }

    public InterfaceConfigBuilder setParamsSerializer(Class<? extends Serializer> paramSerializerClass) {
        for (MethodConfigBuilder b : methodBuilders.values()) {
            b.setParamsSerializer(paramSerializerClass);
        }
        return this;
    }

    public InterfaceConfigBuilder setParamsEncoded(boolean encoded) {
        for (MethodConfigBuilder b : methodBuilders.values()) {
            b.setParamsEncoded(encoded);
        }
        return this;
    }

    public InterfaceConfigBuilder setParamsListSeparator(String separator) {
        for (MethodConfigBuilder b : methodBuilders.values()) {
            b.setParamsListSeparator(separator);
        }
        return this;
    }

    static final class CompositeParamConfigBuilder implements ParamConfigBuilder {

        private final ParamConfigBuilder[] builders;

        CompositeParamConfigBuilder(ParamConfigBuilder[] builders) {
            this.builders = builders.clone();
        }

        public ParamConfig build() throws Exception {
            throw new UnsupportedOperationException();
        }

        public ParamConfigBuilder setName(String name) {
            for(ParamConfigBuilder builder : builders){
                builder.setName(name);
            }
            return this;
        }

        public ParamConfigBuilder setDefaultValue(String defaultValue) {
            for(ParamConfigBuilder builder : builders){
                builder.setDefaultValue(defaultValue);
            }
            return this;
        }

        public ParamConfigBuilder setType(ParamType type) {
            for(ParamConfigBuilder builder : builders){
                builder.setType(type);
            }
            return this;
        }

        public ParamConfigBuilder setListSeparator(String listSeparator) {
            for(ParamConfigBuilder builder : builders){
                builder.setListSeparator(listSeparator);
            }
            return this;
        }

        public ParamConfigBuilder setEncoded(boolean encoded) {
            for(ParamConfigBuilder builder : builders){
                builder.setEncoded(encoded);
            }
            return this;
        }

        public ParamConfigBuilder setMetaDatas(Map<String, Object> metadatas) {
            for(ParamConfigBuilder builder : builders){
                builder.setMetaDatas(metadatas);
            }
            return this;
        }

        public ParamConfigBuilder setSerializer(Class<? extends Serializer> serializerClass) {
            for(ParamConfigBuilder builder : builders){
                builder.setSerializer(serializerClass);
            }
            return this;
        }

        public ParamConfigBuilder forCookie() {
            for(ParamConfigBuilder builder : builders){
                builder.forCookie();
            }
            return this;
        }

        public ParamConfigBuilder forQuery() {
            for(ParamConfigBuilder builder : builders){
                builder.forQuery();
            }
            return this;
        }

        public ParamConfigBuilder forPath() {
            for(ParamConfigBuilder builder : builders){
                builder.forPath();
            }
            return this;
        }

        public ParamConfigBuilder forForm() {
            for(ParamConfigBuilder builder : builders){
                builder.forForm();
            }
            return this;
        }

        public ParamConfigBuilder forHeader() {
            for(ParamConfigBuilder builder : builders){
                builder.forHeader();
            }
            return this;
        }

        public ParamConfigBuilder forMatrix() {
            for(ParamConfigBuilder builder : builders){
                builder.forMatrix();
            }
            return this;
        }

        public ParamConfigBuilder forMultiPart() {
            for(ParamConfigBuilder builder : builders){
                builder.forMultiPart();
            }
            return this;
        }


    }

    @Override
    public String toString() {
        return new ToStringBuilder("Interface").append("interface", interfaze).toString();
    }
}
