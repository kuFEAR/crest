package org.codegist.crest.impl.config;

import org.codegist.crest.config.EntityWriter;
import org.codegist.crest.config.InterfaceConfig;
import org.codegist.crest.config.MethodConfig;
import org.codegist.crest.handler.ErrorHandler;
import org.codegist.crest.handler.ResponseHandler;
import org.codegist.crest.handler.RetryHandler;
import org.codegist.crest.impl.io.HttpMethod;
import org.codegist.crest.interceptor.RequestInterceptor;
import org.codegist.crest.serializer.Serializer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


@SuppressWarnings("unchecked")
public class InterfaceConfigBuilder extends ConfigBuilder<InterfaceConfig> {

    private final Class interfaze;
    private final Map<Method, MethodConfigBuilder> builderCache;

    /**
     * Given properties map can contains user-defined default values, that override interface predefined defauts.
     *
     * @param interfaze        interface to bind the config to
     * @param crestProperties default values holder
     */
    public InterfaceConfigBuilder(Class interfaze, Map<String, Object> crestProperties) {
        super(crestProperties);
        this.interfaze = interfaze;
        this.builderCache = new HashMap<Method, MethodConfigBuilder>();
        if (interfaze != null) {
            for (Method m : interfaze.getDeclaredMethods()) {
                this.builderCache.put(m, new MethodConfigBuilder(m, crestProperties));
            }
        }
    }

    /**
     * @inheritDoc
     */
    public InterfaceConfig build() throws Exception {
        Map<Method, MethodConfig> mConfig = new HashMap<Method, MethodConfig>();
        for (Map.Entry<Method, MethodConfigBuilder> entry : builderCache.entrySet()) {
            mConfig.put(entry.getKey(), entry.getValue().build());
        }
        return new DefaultInterfaceConfig(interfaze, mConfig);
    }

    public MethodConfigBuilder startMethodConfig(Method meth) {
        return this.builderCache.get(meth);
    }

    /* METHODS SETTINGS METHODS */


    public InterfaceConfigBuilder setMethodsCharset(String charset) {
        for (MethodConfigBuilder b : builderCache.values()) {
            b.setCharset(charset);
        }
        return this;
    }

    public InterfaceConfigBuilder setMethodsSocketTimeout(int socketTimeout) {
        for (MethodConfigBuilder b : builderCache.values()) {
            b.setSocketTimeout(socketTimeout);
        }
        return this;
    }

    public InterfaceConfigBuilder setMethodsConnectionTimeout(int connectionTimeout) {
        for (MethodConfigBuilder b : builderCache.values()) {
            b.setConnectionTimeout(connectionTimeout);
        }
        return this;
    }

    public InterfaceConfigBuilder addMethodsExtraFormParam(String name, String value) {
        for (MethodConfigBuilder b : builderCache.values()) {
            b.addExtraFormParam(name, value);
        }
        return this;
    }

    public InterfaceConfigBuilder addMethodsExtraMultiPartParam(String name, String value , String contentType, String fileName) {
        for (MethodConfigBuilder b : builderCache.values()) {
            b.addExtraMultiPartParam(name, value, contentType, fileName);
        }
        return this;
    }

    public InterfaceConfigBuilder addMethodsExtraHeaderParam(String name, String value) {
        for (MethodConfigBuilder b : builderCache.values()) {
            b.addExtraHeaderParam(name, value);
        }
        return this;
    }

    public InterfaceConfigBuilder addMethodsExtraPathParam(String name, String value) {
        for (MethodConfigBuilder b : builderCache.values()) {
            b.addExtraPathParam(name, value);
        }
        return this;
    }

    public InterfaceConfigBuilder addMethodsExtraQueryParam(String name, String value) {
        for (MethodConfigBuilder b : builderCache.values()) {
            b.addExtraQueryParam(name, value);
        }
        return this;
    }

    public InterfaceConfigBuilder addMethodsExtraCookieParam(String name, String value) {
        for (MethodConfigBuilder b : builderCache.values()) {
            b.addExtraCookieParam(name, value);
        }
        return this;
    }

    public InterfaceConfigBuilder addMethodsExtraMatrixParam(String name, String value) {
        for (MethodConfigBuilder b : builderCache.values()) {
            b.addExtraMatrixParam(name, value);
        }
        return this;
    }

    public InterfaceConfigBuilder setMethodsRequestInterceptor(Class<? extends RequestInterceptor> requestInterceptorCls) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        for (MethodConfigBuilder b : builderCache.values()) {
            b.setRequestInterceptor(requestInterceptorCls);
        }
        return this;
    }

    public InterfaceConfigBuilder setMethodsResponseHandler(Class<? extends ResponseHandler> responseHandlerClass) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        for (MethodConfigBuilder b : builderCache.values()) {
            b.setResponseHandler(responseHandlerClass);
        }
        return this;
    }

    public InterfaceConfigBuilder setMethodsErrorHandler(Class<? extends ErrorHandler> errorHandler) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        for (MethodConfigBuilder b : builderCache.values()) {
            b.setErrorHandler(errorHandler);
        }
        return this;
    }

    public InterfaceConfigBuilder setMethodsRetryHandler(Class<? extends RetryHandler> retryHandler) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        for (MethodConfigBuilder b : builderCache.values()) {
            b.setRetryHandler(retryHandler);
        }
        return this;
    }

    public InterfaceConfigBuilder setMethodsEntityWriter(Class<? extends EntityWriter> bodyWriter) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        for (MethodConfigBuilder b : builderCache.values()) {
            b.setEntityWriter(bodyWriter);
        }
        return this;
    }


    public InterfaceConfigBuilder setMethodsConsumes(String... mimeType) {
        for (MethodConfigBuilder b : builderCache.values()) {
            b.setConsumes(mimeType);
        }
        return this;
    }


    public InterfaceConfigBuilder setMethodsProduces(String contentType) {
        for (MethodConfigBuilder b : builderCache.values()) {
            b.setProduces(contentType);
        }
        return this;
    }

    public InterfaceConfigBuilder setMethodsHttpMethod(HttpMethod meth) {
        for (MethodConfigBuilder b : builderCache.values()) {
            b.setHttpMethod(meth);
        }
        return this;
    }


    public InterfaceConfigBuilder appendMethodsPath(String path) {
        for (MethodConfigBuilder b : builderCache.values()) {
            b.appendPath(path);
        }
        return this;
    }

    public InterfaceConfigBuilder setMethodsEndPoint(String endPoint) {
        for (MethodConfigBuilder b : builderCache.values()) {
            b.setEndPoint(endPoint);
        }
        return this;
    }



    /* PARAMS SETTINGS METHODS */
    public InterfaceConfigBuilder setParamsSerializer(Class<? extends Serializer> paramSerializerCls) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        for (MethodConfigBuilder b : builderCache.values()) {
            b.setParamsSerializer(paramSerializerCls);
        }
        return this;
    }

    public InterfaceConfigBuilder setParamsEncoded(boolean encoded) {
        for (MethodConfigBuilder b : builderCache.values()) {
            b.setParamsEncoded(encoded);
        }
        return this;
    }

    public InterfaceConfigBuilder setParamsListSeparator(String separator) {
        for (MethodConfigBuilder b : builderCache.values()) {
            b.setParamsListSeparator(separator);
        }
        return this;
    }
}