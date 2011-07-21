package org.codegist.crest.config;

import org.codegist.crest.entity.EntityWriter;
import org.codegist.crest.handler.ErrorHandler;
import org.codegist.crest.handler.ResponseHandler;
import org.codegist.crest.handler.RetryHandler;
import org.codegist.crest.interceptor.RequestInterceptor;
import org.codegist.crest.serializer.Deserializer;
import org.codegist.crest.serializer.Serializer;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

class DefaultInterfaceConfigBuilder extends ConfigBuilder implements InterfaceConfigBuilder {

    private final Class interfaze;
    private final Map<Method, MethodConfigBuilder> methodBuilders;

    /**
     * Given properties map can contains user-defined default values, that override interface predefined defauts.
     *
     * @param interfaze        interface to bind the config to
     * @param crestProperties default values holder
     */
    public DefaultInterfaceConfigBuilder(Class interfaze, Map<String, Object> crestProperties) {
        super(crestProperties);
        this.interfaze = interfaze;
        this.methodBuilders = new HashMap<Method, MethodConfigBuilder>();
        for (Method m : interfaze.getDeclaredMethods()) {
            this.methodBuilders.put(m, new DefaultMethodConfigBuilder(m, crestProperties));
        }
    }

    /**
     * @inheritDoc
     */
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


    public InterfaceConfigBuilder setMethodsCharset(String charset) {
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

    public InterfaceConfigBuilder setMethodsRequestInterceptor(Class<? extends RequestInterceptor> requestInterceptorCls)  {
        for (MethodConfigBuilder b : methodBuilders.values()) {
            b.setRequestInterceptor(requestInterceptorCls);
        }
        return this;
    }

    public InterfaceConfigBuilder setMethodsResponseHandler(Class<? extends ResponseHandler> responseHandlerClass)  {
        for (MethodConfigBuilder b : methodBuilders.values()) {
            b.setResponseHandler(responseHandlerClass);
        }
        return this;
    }

    public InterfaceConfigBuilder setMethodsErrorHandler(Class<? extends ErrorHandler> errorHandler)  {
        for (MethodConfigBuilder b : methodBuilders.values()) {
            b.setErrorHandler(errorHandler);
        }
        return this;
    }

    public InterfaceConfigBuilder setMethodsRetryHandler(Class<? extends RetryHandler> retryHandler)  {
        for (MethodConfigBuilder b : methodBuilders.values()) {
            b.setRetryHandler(retryHandler);
        }
        return this;
    }

    public InterfaceConfigBuilder setMethodsEntityWriter(Class<? extends EntityWriter> bodyWriter)  {
        for (MethodConfigBuilder b : methodBuilders.values()) {
            b.setEntityWriter(bodyWriter);
        }
        return this;
    }


    public InterfaceConfigBuilder setMethodsConsumes(String... mimeType) {
        for (MethodConfigBuilder b : methodBuilders.values()) {
            b.setConsumes(mimeType);
        }
        return this;
    }

    public InterfaceConfigBuilder setMethodsDeserializer(Class<? extends Deserializer> deserializer) {
        for (MethodConfigBuilder b : methodBuilders.values()) {
            b.setDeserializer(deserializer);
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

    public InterfaceConfigBuilder setParamsSerializer(Class<? extends Serializer> paramSerializerCls) {
        for (MethodConfigBuilder b : methodBuilders.values()) {
            b.setParamsSerializer(paramSerializerCls);
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

    private static final class CompositeParamConfigBuilder implements ParamConfigBuilder {

        private final ParamConfigBuilder[] builders;

        private CompositeParamConfigBuilder(ParamConfigBuilder[] builders) {
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

        public ParamConfigBuilder setSerializer(Class<? extends Serializer> serializer) {
            for(ParamConfigBuilder builder : builders){
                builder.setSerializer(serializer);
            }
            return this;
        }
    }
}
