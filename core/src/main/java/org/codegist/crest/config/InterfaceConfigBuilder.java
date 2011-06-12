package org.codegist.crest.config;

import org.codegist.crest.CRestProperty;
import org.codegist.crest.EntityWriter;
import org.codegist.crest.handler.ErrorHandler;
import org.codegist.crest.handler.ResponseHandler;
import org.codegist.crest.handler.RetryHandler;
import org.codegist.crest.interceptor.RequestInterceptor;
import org.codegist.crest.serializer.Serializer;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class InterfaceConfigBuilder extends AbstractConfigBuilder<InterfaceConfig> {

    private final Class interfaze;
    private final Map<Method, MethodConfigBuilder> builderCache;
    private String encoding;
    private RequestInterceptor globalInterceptor;

    // todo can we do without it ?
    public InterfaceConfigBuilder() {
        this(null,null);
    }
    /**
     * Given properties map can contains user-defined default values, that override interface predefined defauts.
     *
     * @param interfaze        interface to bind the config to
     * @param customProperties default values holder
     */
    public InterfaceConfigBuilder(Class interfaze, Map<String, Object> customProperties) {
        super(customProperties);
        this.interfaze = interfaze;
        this.builderCache = new HashMap<Method, MethodConfigBuilder>();
        if (interfaze != null) {
            for (Method m : interfaze.getDeclaredMethods()) {
                this.builderCache.put(m, new MethodConfigBuilder(this, m, customProperties));
            }
        }
    }

    /**
     * @inheritDoc
     */
    public InterfaceConfig build(boolean validateConfig, boolean isTemplate) {
        Map<Method, MethodConfig> mConfig = new HashMap<Method, MethodConfig>();
        for (Map.Entry<Method, MethodConfigBuilder> entry : builderCache.entrySet()) {
            mConfig.put(entry.getKey(), entry.getValue().build(validateConfig, isTemplate));
        }
        // make local copies so that we don't mess with builder state to be able to call build multiple times on it
        String encoding = this.encoding;
        RequestInterceptor globalInterceptor = this.globalInterceptor;

        if (!isTemplate) {
            encoding = defaultIfUndefined(encoding, CRestProperty.CONFIG_INTERFACE_DEFAULT_ENCODING, InterfaceConfig.DEFAULT_ENCODING);
            globalInterceptor = defaultIfUndefined(globalInterceptor, CRestProperty.CONFIG_INTERFACE_DEFAULT_GLOBAL_INTERCEPTOR, newInstance(InterfaceConfig.DEFAULT_GLOBAL_INTERCEPTOR));
        }
        
        return new DefaultInterfaceConfig(
                interfaze,
                encoding,
                globalInterceptor,
                mConfig
        );
    }

    @Override
    public InterfaceConfigBuilder setIgnoreNullOrEmptyValues(boolean ignoreNullOrEmptyValues) {
        for (MethodConfigBuilder b : builderCache.values()) {
            b.setIgnoreNullOrEmptyValues(ignoreNullOrEmptyValues);
        }
        super.setIgnoreNullOrEmptyValues(ignoreNullOrEmptyValues);
        return this;
    }

    public MethodConfigBuilder startMethodConfig(Method meth) {
        return this.builderCache.get(meth);
    }

    public InterfaceConfigBuilder setEncoding(String encoding) {
        if (ignore(encoding)) return this;
        this.encoding = replacePlaceholders(encoding);
        return this;
    }

    public InterfaceConfigBuilder setGlobalInterceptor(RequestInterceptor requestInterceptor) {
        if (ignore(requestInterceptor)) return this;
        this.globalInterceptor = requestInterceptor;
        return this;
    }

    public InterfaceConfigBuilder setGlobalInterceptor(String interceptorClassName) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        if (ignore(interceptorClassName)) return this;
        return setGlobalInterceptor((Class<? extends RequestInterceptor>) Class.forName(replacePlaceholders(interceptorClassName)));
    }

    public InterfaceConfigBuilder setGlobalInterceptor(Class<? extends RequestInterceptor> interceptorCls) throws IllegalAccessException, InstantiationException {
        if (ignore(interceptorCls)) return this;
        return setGlobalInterceptor(newInstance(interceptorCls));
    }


    /* METHODS SETTINGS METHODS */

    public InterfaceConfigBuilder setMethodsSocketTimeout(Long socketTimeout) {
        for (MethodConfigBuilder b : builderCache.values()) {
            b.setSocketTimeout(socketTimeout);
        }
        return this;
    }

    public InterfaceConfigBuilder setMethodsSocketTimeout(String socketTimeout) {
        for (MethodConfigBuilder b : builderCache.values()) {
            b.setSocketTimeout(socketTimeout);
        }
        return this;
    }

    public InterfaceConfigBuilder setMethodsConnectionTimeout(Long connectionTimeout) {
        for (MethodConfigBuilder b : builderCache.values()) {
            b.setConnectionTimeout(connectionTimeout);
        }
        return this;
    }

    public InterfaceConfigBuilder setMethodsConnectionTimeout(String connectionTimeout) {
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

    public InterfaceConfigBuilder addMethodsExtraParam(String name, String value, String destination, Map<String,Object> metaDatas) {
        for (MethodConfigBuilder b : builderCache.values()) {
            b.addExtraParam(name, value, destination, metaDatas);
        }
        return this;
    }

    public InterfaceConfigBuilder setMethodsRequestInterceptor(RequestInterceptor requestInterceptor) {
        for (MethodConfigBuilder b : builderCache.values()) {
            b.setRequestInterceptor(requestInterceptor);
        }
        return this;
    }

    public InterfaceConfigBuilder setMethodsRequestInterceptor(String requestInterceptorClassName) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        for (MethodConfigBuilder b : builderCache.values()) {
            b.setRequestInterceptor(requestInterceptorClassName);
        }
        return this;
    }

    public InterfaceConfigBuilder setMethodsRequestInterceptor(Class<? extends RequestInterceptor> requestInterceptorCls) throws IllegalAccessException, InstantiationException {
        for (MethodConfigBuilder b : builderCache.values()) {
            b.setRequestInterceptor(requestInterceptorCls);
        }
        return this;
    }

    public InterfaceConfigBuilder setMethodsResponseHandler(ResponseHandler responseHandler) {
        for (MethodConfigBuilder b : builderCache.values()) {
            b.setResponseHandler(responseHandler);
        }
        return this;
    }

    public InterfaceConfigBuilder setMethodsResponseHandler(String responseHandlerClassName) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        for (MethodConfigBuilder b : builderCache.values()) {
            b.setResponseHandler(responseHandlerClassName);
        }
        return this;
    }

    public InterfaceConfigBuilder setMethodsResponseHandler(Class<? extends ResponseHandler> responseHandlerClass) throws IllegalAccessException, InstantiationException {
        for (MethodConfigBuilder b : builderCache.values()) {
            b.setResponseHandler(responseHandlerClass);
        }
        return this;
    }

    public InterfaceConfigBuilder setMethodsErrorHandler(ErrorHandler errorHandler) {
        for (MethodConfigBuilder b : builderCache.values()) {
            b.setErrorHandler(errorHandler);
        }
        return this;
    }

    public InterfaceConfigBuilder setMethodsErrorHandler(String errorHandler) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        for (MethodConfigBuilder b : builderCache.values()) {
            b.setErrorHandler(errorHandler);
        }
        return this;
    }

    public InterfaceConfigBuilder setMethodsErrorHandler(Class<? extends ErrorHandler> errorHandler) throws IllegalAccessException, InstantiationException {
        for (MethodConfigBuilder b : builderCache.values()) {
            b.setErrorHandler(errorHandler);
        }
        return this;
    }

    public InterfaceConfigBuilder setMethodsRetryHandler(RetryHandler retryHandler) {
        for (MethodConfigBuilder b : builderCache.values()) {
            b.setRetryHandler(retryHandler);
        }
        return this;
    }

    public InterfaceConfigBuilder setMethodsRetryHandler(String retryHandler) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        for (MethodConfigBuilder b : builderCache.values()) {
            b.setRetryHandler(retryHandler);
        }
        return this;
    }

    public InterfaceConfigBuilder setMethodsRetryHandler(Class<? extends RetryHandler> retryHandler) throws IllegalAccessException, InstantiationException {
        for (MethodConfigBuilder b : builderCache.values()) {
            b.setRetryHandler(retryHandler);
        }
        return this;
    }

    public InterfaceConfigBuilder setMethodsEntityWriter(EntityWriter entityWriter) {
        for (MethodConfigBuilder b : builderCache.values()) {
            b.setEntityWriter(entityWriter);
        }
        return this;
    }

    public InterfaceConfigBuilder setMethodsEntityWriter(String bodyWriter) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        for (MethodConfigBuilder b : builderCache.values()) {
            b.setEntityWriter(bodyWriter);
        }
        return this;
    }

    public InterfaceConfigBuilder setMethodsEntityWriter(Class<? extends EntityWriter> bodyWriter) throws IllegalAccessException, InstantiationException {
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

    public InterfaceConfigBuilder setMethodsHttpMethod(String meth) {
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

    public InterfaceConfigBuilder setParamsSerializer(Serializer paramSerializer) {
        for (MethodConfigBuilder b : builderCache.values()) {
            b.setParamsSerializer(paramSerializer);
        }
        return this;
    }

    public InterfaceConfigBuilder setParamsSerializer(String paramSerializerClassName) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        for (MethodConfigBuilder b : builderCache.values()) {
            b.setParamsSerializer(paramSerializerClassName);
        }
        return this;
    }

    public InterfaceConfigBuilder setParamsSerializer(Class<? extends Serializer> paramSerializerCls) throws IllegalAccessException, InstantiationException {
        for (MethodConfigBuilder b : builderCache.values()) {
            b.setParamsSerializer(paramSerializerCls);
        }
        return this;
    }

    public InterfaceConfigBuilder setParamsName(String name) {
        for (MethodConfigBuilder b : builderCache.values()) {
            b.setParamsName(name);
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
