package org.codegist.crest.config;

import org.codegist.common.lang.State;
import org.codegist.common.net.Urls;
import org.codegist.common.reflect.Methods;
import org.codegist.crest.*;
import org.codegist.crest.handler.ErrorHandler;
import org.codegist.crest.handler.ResponseHandler;
import org.codegist.crest.handler.RetryHandler;
import org.codegist.crest.http.HttpMethod;
import org.codegist.crest.http.HttpRequest;
import org.codegist.crest.interceptor.RequestInterceptor;
import org.codegist.crest.serializer.Deserializer;
import org.codegist.crest.serializer.Registry;
import org.codegist.crest.serializer.Serializer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

import static org.codegist.common.collect.Arrays.join;

@SuppressWarnings("unchecked")
public class MethodConfigBuilder extends ConfigBuilder<MethodConfig> {

    private final Method method;
    private final InterfaceConfigBuilder parent;
    private final Map<String, ParamConfigBuilder> extraParamBuilders = new LinkedHashMap<String, ParamConfigBuilder>();
    private final ParamConfigBuilder[] methodParamConfigBuilders;
    private final Registry<String,Deserializer> mimeDeserializerRegistry;
    private final ArrayList<String> pathParts = new ArrayList<String>();
    private final List<Deserializer> deserializers = new ArrayList<Deserializer>();

    private HttpMethod meth;
    private String contentType;
    private String accept;
    private Long socketTimeout;
    private Long connectionTimeout;
    private RequestInterceptor requestInterceptor;
    private ResponseHandler responseHandler;
    private ErrorHandler errorHandler;
    private RetryHandler retryHandler;
    private String endPoint;
    private EntityWriter entityWriter;

    MethodConfigBuilder(InterfaceConfigBuilder parent, Method method, Map<String, Object> customProperties) {
        super(customProperties);
        this.mimeDeserializerRegistry = getProperty(Registry.class.getName() + "#deserializers-per-mime");
        this.parent = parent;
        this.method = method;
        this.methodParamConfigBuilders = new ParamConfigBuilder[method.getParameterTypes().length];

        for (int i = 0; i < this.methodParamConfigBuilders.length; i++) {
            Map<Class<? extends Annotation>, Annotation> paramAnnotation = Methods.getParamsAnnotation(method, i);
            this.methodParamConfigBuilders[i] = new ParamConfigBuilder(this, customProperties, method.getParameterTypes()[i], method.getGenericParameterTypes()[i], paramAnnotation);
        }
    }

    /**
     * @inheritDoc
     */
    public MethodConfig build(boolean validateConfig, boolean isTemplate) {
        ParamConfig[] pConfigMethod = new ParamConfig[methodParamConfigBuilders.length];
        for (int i = 0; i < methodParamConfigBuilders.length; i++) {
            pConfigMethod[i] = this.methodParamConfigBuilders[i].build(validateConfig, isTemplate);
        }
        Map<String, ParamConfig> extraParams = new LinkedHashMap<String, ParamConfig>();
        for (ParamConfigBuilder b : extraParamBuilders.values()) {
            ParamConfig bpc = b.build(validateConfig, isTemplate);
            extraParams.put(bpc.getName(), bpc);
        }

        List<String> fullPathPart = (List<String>) pathParts.clone();
        fullPathPart.add(0, endPoint);
        String[] paths = fullPathPart.toArray(new String[fullPathPart.size()]);

        // make local copies so that we don't mess with builder state to be able to call build multiple times on it
        String path = Urls.normalizeSlashes(join("/", paths));
        String contentType = this.contentType;
        String accept = this.accept;
        HttpMethod meth = this.meth;
        Long socketTimeout = this.socketTimeout;
        Long connectionTimeout = this.connectionTimeout;
        RequestInterceptor requestInterceptor = this.requestInterceptor;
        ResponseHandler responseHandler = this.responseHandler;
        ErrorHandler errorHandler = this.errorHandler;
        RetryHandler retryHandler = this.retryHandler;
        EntityWriter entityWriter = this.entityWriter;
        Deserializer[] deserializers = this.deserializers.toArray(new Deserializer[this.deserializers.size()]);

        if (!isTemplate) {
            path = defaultIfUndefined(path, CRestProperty.CONFIG_METHOD_DEFAULT_PATH, MethodConfig.DEFAULT_PATH);
            meth = defaultIfUndefined(meth, CRestProperty.CONFIG_METHOD_DEFAULT_HTTP_METHOD, MethodConfig.DEFAULT_HTTP_METHOD);
            contentType = defaultIfUndefined(contentType, CRestProperty.CONFIG_METHOD_DEFAULT_CONTENT_TYPE, MethodConfig.DEFAULT_CONTENT_TYPE);
            accept = defaultIfUndefined(accept, CRestProperty.CONFIG_METHOD_DEFAULT_ACCEPT, MethodConfig.DEFAULT_ACCEPT);
            ParamConfig[] defs = defaultIfUndefined(null, CRestProperty.CONFIG_METHOD_DEFAULT_EXTRA_PARAMS, MethodConfig.DEFAULT_EXTRA_PARAMs);
            for (ParamConfig def : defs) {
                if (extraParams.containsKey(def.getName())) continue;
                extraParams.put(def.getName(), def);
            }
            socketTimeout = defaultIfUndefined(socketTimeout, CRestProperty.CONFIG_METHOD_DEFAULT_SO_TIMEOUT, MethodConfig.DEFAULT_SO_TIMEOUT);
            connectionTimeout = defaultIfUndefined(connectionTimeout, CRestProperty.CONFIG_METHOD_DEFAULT_CO_TIMEOUT, MethodConfig.DEFAULT_CO_TIMEOUT);
            requestInterceptor = defaultIfUndefined(requestInterceptor, CRestProperty.CONFIG_METHOD_DEFAULT_REQUEST_INTERCEPTOR, newInstance(MethodConfig.DEFAULT_REQUEST_INTERCEPTOR));
            responseHandler = defaultIfUndefined(responseHandler, CRestProperty.CONFIG_METHOD_DEFAULT_RESPONSE_HANDLER, newInstance(MethodConfig.DEFAULT_RESPONSE_HANDLER));
            errorHandler = defaultIfUndefined(errorHandler, CRestProperty.CONFIG_METHOD_DEFAULT_ERROR_HANDLER, newInstance(MethodConfig.DEFAULT_ERROR_HANDLER));
            retryHandler = defaultIfUndefined(retryHandler, CRestProperty.CONFIG_METHOD_DEFAULT_RETRY_HANDLER, newInstance(MethodConfig.DEFAULT_RETRY_HANDLER));
            entityWriter = defaultIfUndefined(entityWriter, CRestProperty.CONFIG_PARAM_DEFAULT_BODY_WRITER, newInstance(MethodConfig.DEFAULT_BODY_WRITER));
            deserializers = defaultIfUndefined(deserializers, CRestProperty.CONFIG_METHOD_DEFAULT_DESERIALIZERS, newInstance(MethodConfig.DEFAULT_DESERIALIZERS));

            if(entityWriter == null && meth.hasEntity()) {
                Class<? extends EntityWriter> entityWriterCls = UrlEncodedFormEntityWriter.class;
                for(ParamConfig cfg : pConfigMethod){
                    if(MultiParts.hasMultiPart(cfg.getMetaDatas())) {
                        entityWriterCls = MultiPartEntityWriter.class;
                        break;
                    }
                }
                entityWriter = newInstance(entityWriterCls);
            }
        }
        
        return new DefaultMethodConfig(
                method,
                RegexPathTemplate.create(path),
                contentType,
                accept,
                meth,
                socketTimeout,
                connectionTimeout,
                entityWriter,
                requestInterceptor,
                responseHandler,
                errorHandler,
                retryHandler,
                deserializers,
                pConfigMethod,
                extraParams.values().toArray(new ParamConfig[extraParams.size()])
        );
    }

    public InterfaceConfigBuilder endMethodConfig() {
        return parent;
    }

    @Override
    public MethodConfigBuilder setIgnoreNullOrEmptyValues(boolean ignoreNullOrEmptyValues) {
        for (ParamConfigBuilder b : methodParamConfigBuilders) {
            b.setIgnoreNullOrEmptyValues(ignoreNullOrEmptyValues);
        }
        for (ParamConfigBuilder b : extraParamBuilders.values()) {
            b.setIgnoreNullOrEmptyValues(ignoreNullOrEmptyValues);
        }
        super.setIgnoreNullOrEmptyValues(ignoreNullOrEmptyValues);
        return this;
    }

    public MethodConfigBuilder setConsumes(String... mimeTypes) {
        if (ignore(mimeTypes)) return this;
        State.notNull(mimeDeserializerRegistry, "Can't lookup a deserializer by mime-type. Please provide a DeserializerFactory");

        String[] mimes = new String[mimeTypes.length];
        for (int i = 0; i < mimeTypes.length; i++) {
            String mMime = replacePlaceholders(mimeTypes[i]);
            this.deserializers.add(mimeDeserializerRegistry.getFor(mMime));
            mimes[i] = mMime;
        }
        this.accept = join(",", mimes);
        return this;
    }

    public MethodConfigBuilder setProduces(String contentType) {
        if (ignore(contentType)) return this;
        this.contentType = replacePlaceholders(contentType);
        return this;
    }

    public MethodConfigBuilder addExtraMultiPartParam(String name, String defaultValue, String contentType, String fileName) {
        return addExtraParam(name, defaultValue, HttpRequest.DEST_FORM, MultiParts.toMetaDatas(contentType, fileName));
    }

    public MethodConfigBuilder addExtraFormParam(String name, String defaultValue) {
        return addExtraParam(name, defaultValue, HttpRequest.DEST_FORM);
    }

    public MethodConfigBuilder addExtraHeaderParam(String name, String defaultValue) {
        return addExtraParam(name, defaultValue, HttpRequest.DEST_HEADER);
    }

    public MethodConfigBuilder addExtraQueryParam(String name, String defaultValue) {
        return addExtraParam(name, defaultValue, HttpRequest.DEST_QUERY);
    }

    public MethodConfigBuilder addExtraPathParam(String name, String defaultValue) {
        return addExtraParam(name, defaultValue, HttpRequest.DEST_PATH);
    }

    public MethodConfigBuilder addExtraCookieParam(String name, String defaultValue) {
        return addExtraParam(name, defaultValue, HttpRequest.DEST_COOKIE);
    }

    public MethodConfigBuilder addExtraMatrixParam(String name, String defaultValue) {
        return addExtraParam(name, defaultValue, HttpRequest.DEST_MATRIX);
    }

    public MethodConfigBuilder addExtraParam(String name, String defaultValue, String dest) {
        return addExtraParam(name, defaultValue, dest, Collections.<String, Object>emptyMap());
    }
    public MethodConfigBuilder addExtraParam(String name, String defaultValue, String dest, Map<String,Object> metaDatas) {
        if (ignore(name) && ignore(defaultValue) && ignore(dest)) return this;
        return startExtraParamConfig(name)
                .setDefaultValue(defaultValue)
                .setDestination(dest)
                .setEncoded(true)
                .setMetaDatas(metaDatas)
                .endParamConfig();
    }

    public ParamConfigBuilder startParamConfig(int index) {
        return methodParamConfigBuilders[index];
    }

    public ParamConfigBuilder startExtraParamConfig(String name) {
        ParamConfigBuilder builder = extraParamBuilders.get(name);
        if (builder == null) {
            extraParamBuilders.put(name, builder = new ParamConfigBuilder(this, customProperties).setName(name));
        }
        return builder;
    }

    public MethodConfigBuilder appendPath(String path) {
        if (ignore(path)) return this;
        pathParts.add(replacePlaceholders(path));
        return this;
    }

    public MethodConfigBuilder setEndPoint(String endPoint) {
        if (ignore(endPoint)) return this;
        this.endPoint = replacePlaceholders(endPoint);
        return this;
    }

    public MethodConfigBuilder setHttpMethod(HttpMethod meth) {
        if (ignore(meth)) return this;
        this.meth = meth;
        return this;
    }

    public MethodConfigBuilder setSocketTimeout(Long socketTimeout) {
        if (ignore(socketTimeout)) return this;
        this.socketTimeout = socketTimeout;
        return this;
    }

    public MethodConfigBuilder setConnectionTimeout(Long connectionTimeout) {
        if (ignore(connectionTimeout)) return this;
        this.connectionTimeout = connectionTimeout;
        return this;
    }

    public MethodConfigBuilder setRequestInterceptor(Class<? extends RequestInterceptor> interceptorCls) {
        if (ignore(interceptorCls)) return this;
        this.requestInterceptor = newInstance(interceptorCls);
        return this;
    }

    public MethodConfigBuilder setResponseHandler(Class<? extends ResponseHandler> responseHandlerClass) {
        if (ignore(responseHandlerClass)) return this;
        this.responseHandler  = newInstance(responseHandlerClass);
        return this;
    }

    public MethodConfigBuilder setErrorHandler(Class<? extends ErrorHandler> methodHandlerClass) {
        if (ignore(methodHandlerClass)) return this;
        this.errorHandler = newInstance(methodHandlerClass);
        return this;
    }

    public MethodConfigBuilder setRetryHandler(Class<? extends RetryHandler> retryHandlerClass) {
        if (ignore(retryHandlerClass)) return this;
        this.retryHandler = newInstance(retryHandlerClass);
        return this;
    }

    public MethodConfigBuilder setEntityWriter(Class<? extends EntityWriter> bodyWriterClass) {
        if (ignore(bodyWriterClass)) return this;
        this.entityWriter = newInstance(bodyWriterClass);
        return this;
    }

    /* PARAMS SETTINGS METHODS */

    public MethodConfigBuilder setParamsSerializer(Class<? extends Serializer> paramSerializer)  {
        for (ParamConfigBuilder b : methodParamConfigBuilders) {
            b.setSerializer(paramSerializer);
        }
        return this;
    }

    public MethodConfigBuilder setParamsEncoded(boolean encoded) {
        for (ParamConfigBuilder b : methodParamConfigBuilders) {
            b.setEncoded(encoded);
        }
        return this;
    }

    public MethodConfigBuilder setParamsListSeparator(String listSeparator) {
        for (ParamConfigBuilder b : methodParamConfigBuilders) {
            b.setListSeparator(listSeparator);
        }
        return this;
    }
}
