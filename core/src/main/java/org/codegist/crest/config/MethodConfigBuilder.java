package org.codegist.crest.config;

import org.codegist.common.lang.State;
import org.codegist.common.net.Urls;
import org.codegist.common.reflect.Methods;
import org.codegist.crest.CRestProperty;
import org.codegist.crest.EntityWriter;
import org.codegist.crest.UrlEncodedFormEntityWriter;
import org.codegist.crest.handler.ErrorHandler;
import org.codegist.crest.handler.ResponseHandler;
import org.codegist.crest.handler.RetryHandler;
import org.codegist.crest.http.HttpMethod;
import org.codegist.crest.http.HttpRequest;
import org.codegist.crest.interceptor.RequestInterceptor;
import org.codegist.crest.serializer.Deserializer;
import org.codegist.crest.serializer.DeserializerRegistry;
import org.codegist.crest.serializer.Serializer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

import static java.util.Arrays.asList;
import static org.codegist.common.collect.Arrays.join;

@SuppressWarnings("unchecked")
public class MethodConfigBuilder extends AbstractConfigBuilder<MethodConfig> {

    private final Method method;
    private final InterfaceConfigBuilder parent;
    private final Map<String, ParamConfigBuilder> extraParamBuilders = new LinkedHashMap<String, ParamConfigBuilder>();// TODO MULTIMAP ?
    private final ParamConfigBuilder[] methodParamConfigBuilders;
    private final DeserializerRegistry deserializerRegistry;
    private final ArrayList<String> pathParts = new ArrayList<String>();
    private final List<Deserializer> deserializers = new ArrayList<Deserializer>();
    private final boolean addSlashes;

    private HttpMethod meth;
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
        this.deserializerRegistry = getProperty(DeserializerRegistry.class.getName());
        this.parent = parent;
        this.method = method;
        this.addSlashes = !Boolean.FALSE.equals(getProperty(CRestProperty.CREST_URL_ADD_SLASHES));
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
        String path = Urls.normalizeSlashes(addSlashes ? join("/", paths) : join("", paths));
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
                entityWriter = newInstance(UrlEncodedFormEntityWriter.class);
            }
        }
        
        return new DefaultMethodConfig(
                method,
                RegexPathTemplate.create(path),
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

    public MethodConfigBuilder setAccepts(String... mimeTypes) {
        if (ignore(mimeTypes)) return this;
        State.notNull(deserializerRegistry, "Can't lookup a deserializer by mime-type. Please provide a DeserializerFactory");

        String[] mimes = new String[mimeTypes.length];
        for (int i = 0; i < mimeTypes.length; i++) {
            String mMime = replacePlaceholders(mimeTypes[i]);
            addDeserializer(deserializerRegistry.getForMimeType(mMime));
            mimes[i] = mMime;
        }
        addExtraHeaderParam("Accept", join(",", mimes));

        return this;
    }

    public MethodConfigBuilder setContentType(String contentType) {
        if (ignore(contentType)) return this;
        return addExtraHeaderParam("Content-Type", replacePlaceholders(contentType));
    }

    public MethodConfigBuilder addExtraFormParam(String name, String defaultValue) {
        return addExtraFormParam(name, defaultValue, Collections.<String, Object>emptyMap());
    }
    public MethodConfigBuilder addExtraFormParam(String name, String defaultValue, Map<String,Object> metas) {
        return addExtraParam(name, defaultValue, HttpRequest.DEST_FORM, metas);
    }

    public MethodConfigBuilder addExtraHeaderParam(String name, String defaultValue) {
        return addExtraHeaderParam(name, defaultValue, Collections.<String, Object>emptyMap());
    }
    public MethodConfigBuilder addExtraHeaderParam(String name, String defaultValue, Map<String,Object> metas) {
        return addExtraParam(name, defaultValue, HttpRequest.DEST_HEADER, metas);
    }

    public MethodConfigBuilder addExtraQueryParam(String name, String defaultValue) {
        return addExtraQueryParam(name, defaultValue, Collections.<String, Object>emptyMap());
    }
    public MethodConfigBuilder addExtraQueryParam(String name, String defaultValue, Map<String,Object> metas) {
        return addExtraParam(name, defaultValue, HttpRequest.DEST_QUERY, metas);
    }

    public MethodConfigBuilder addExtraPathParam(String name, String defaultValue) {
        return addExtraPathParam(name, defaultValue, Collections.<String, Object>emptyMap());
    }
    public MethodConfigBuilder addExtraPathParam(String name, String defaultValue, Map<String,Object> metas) {
        return addExtraParam(name, defaultValue, HttpRequest.DEST_PATH, metas);
    }

    public MethodConfigBuilder addExtraCookieParam(String name, String defaultValue) {
        return addExtraCookieParam(name, defaultValue, Collections.<String, Object>emptyMap());
    }
    public MethodConfigBuilder addExtraCookieParam(String name, String defaultValue, Map<String,Object> metas) {
        return addExtraParam(name, defaultValue, HttpRequest.DEST_COOKIE, metas);
    }

    public MethodConfigBuilder addExtraMatrixParam(String name, String defaultValue) {
        return addExtraMatrixParam(name, defaultValue, Collections.<String, Object>emptyMap());
    }
    public MethodConfigBuilder addExtraMatrixParam(String name, String defaultValue, Map<String,Object> metas) {
        return addExtraParam(name, defaultValue, HttpRequest.DEST_MATRIX, metas);
    }

    public MethodConfigBuilder addExtraParam(String name, String defaultValue, String dest, Map<String,Object> metaDatas) {
        if (ignore(name) && ignore(defaultValue) && ignore(dest)) return this;
        return startExtraParamConfig(name)
                .setDefaultValue(defaultValue)
                .setDestination(dest)
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

    public MethodConfigBuilder setHttpMethod(String meth) {
        if (ignore(meth)) return this;
        this.meth = HttpMethod.valueOf(replacePlaceholders(meth));
        return this;
    }

    public MethodConfigBuilder setSocketTimeout(Long socketTimeout) {
        if (ignore(socketTimeout)) return this;
        this.socketTimeout = socketTimeout;
        return this;
    }

    public MethodConfigBuilder setSocketTimeout(String socketTimeout) {
        if (ignore(socketTimeout)) return this;
        return setSocketTimeout(Long.parseLong(replacePlaceholders(socketTimeout)));
    }

    public MethodConfigBuilder setConnectionTimeout(Long connectionTimeout) {
        if (ignore(connectionTimeout)) return this;
        this.connectionTimeout = connectionTimeout;
        return this;
    }

    public MethodConfigBuilder setConnectionTimeout(String connectionTimeout) {
        if (ignore(connectionTimeout)) return this;
        return setConnectionTimeout(Long.parseLong(replacePlaceholders(connectionTimeout)));
    }

    public MethodConfigBuilder setRequestInterceptor(RequestInterceptor requestInterceptor) {
        if (ignore(requestInterceptor)) return this;
        this.requestInterceptor = requestInterceptor;
        return this;
    }

    public MethodConfigBuilder setRequestInterceptor(String interceptorClassName) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        if (ignore(interceptorClassName)) return this;
        return setRequestInterceptor((Class<? extends RequestInterceptor>) Class.forName(replacePlaceholders(interceptorClassName)));
    }

    public MethodConfigBuilder setRequestInterceptor(Class<? extends RequestInterceptor> interceptorCls) throws IllegalAccessException, InstantiationException {
        if (ignore(interceptorCls)) return this;
        return setRequestInterceptor(newInstance(interceptorCls));
    }

    public MethodConfigBuilder setResponseHandler(ResponseHandler responseHandler) {
        if (ignore(responseHandler)) return this;
        this.responseHandler = responseHandler;
        return this;
    }

    public MethodConfigBuilder setResponseHandler(String responseHandlerClassName) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        if (ignore(responseHandlerClassName)) return this;
        return setResponseHandler((Class<? extends ResponseHandler>) Class.forName(replacePlaceholders(responseHandlerClassName)));
    }

    public MethodConfigBuilder setResponseHandler(Class<? extends ResponseHandler> responseHandlerClass) throws IllegalAccessException, InstantiationException {
        if (ignore(responseHandlerClass)) return this;
        return setResponseHandler(newInstance(responseHandlerClass));
    }


    public MethodConfigBuilder setErrorHandler(ErrorHandler errorHandler) {
        if (ignore(errorHandler)) return this;
        this.errorHandler = errorHandler;
        return this;
    }

    public MethodConfigBuilder setErrorHandler(String methodHandlerClassName) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        if (ignore(methodHandlerClassName)) return this;
        return setErrorHandler((Class<? extends ErrorHandler>) Class.forName(replacePlaceholders(methodHandlerClassName)));
    }

    public MethodConfigBuilder setErrorHandler(Class<? extends ErrorHandler> methodHandlerClass) throws IllegalAccessException, InstantiationException {
        if (ignore(methodHandlerClass)) return this;
        return setErrorHandler(newInstance(methodHandlerClass));
    }


    public MethodConfigBuilder setRetryHandler(RetryHandler retryHandler) {
        if (ignore(retryHandler)) return this;
        this.retryHandler = retryHandler;
        return this;
    }

    public MethodConfigBuilder setRetryHandler(String retryHandlerClassName) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        if (ignore(retryHandlerClassName)) return this;
        return setRetryHandler((Class<? extends RetryHandler>) Class.forName(replacePlaceholders(retryHandlerClassName)));
    }

    public MethodConfigBuilder setRetryHandler(Class<? extends RetryHandler> retryHandlerClass) throws IllegalAccessException, InstantiationException {
        if (ignore(retryHandlerClass)) return this;
        return setRetryHandler(newInstance(retryHandlerClass));
    }

    public MethodConfigBuilder setEntityWriter(EntityWriter entityWriter) {
        if (ignore(entityWriter)) return this;
        this.entityWriter = entityWriter;
        return this;
    }

    public MethodConfigBuilder setEntityWriter(String bodyWriterClassName) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        if (ignore(bodyWriterClassName)) return this;
        return setEntityWriter((Class<? extends EntityWriter>) Class.forName(replacePlaceholders(bodyWriterClassName)));
    }

    public MethodConfigBuilder setEntityWriter(Class<? extends EntityWriter> bodyWriterClass) throws IllegalAccessException, InstantiationException {
        if (ignore(bodyWriterClass)) return this;
        return setEntityWriter(newInstance(bodyWriterClass));
    }

    public MethodConfigBuilder addDeserializer(Deserializer deserializer) {
        if (ignore(deserializer)) return this;
        this.deserializers.add(deserializer);
        return this;
    }

    public MethodConfigBuilder setDeserializers(Deserializer... deserializer) {
        if (ignore(deserializer)) return this;
        this.deserializers.clear();
        this.deserializers.addAll(asList(deserializer));
        return this;
    }


    /* PARAMS SETTINGS METHODS */

    public MethodConfigBuilder setParamsSerializer(Serializer paramSerializer) {
        for (ParamConfigBuilder b : methodParamConfigBuilders) {
            b.setSerializer(paramSerializer);
        }
        return this;
    }

    public MethodConfigBuilder setParamsSerializer(String paramSerializerClassName) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        for (ParamConfigBuilder b : methodParamConfigBuilders) {
            b.setSerializer(paramSerializerClassName);
        }
        return this;
    }

    public MethodConfigBuilder setParamsSerializer(Class<? extends Serializer> paramSerializer) throws IllegalAccessException, InstantiationException {
        for (ParamConfigBuilder b : methodParamConfigBuilders) {
            b.setSerializer(paramSerializer);
        }
        return this;
    }

    public MethodConfigBuilder setParamsName(String name) {
        for (ParamConfigBuilder b : methodParamConfigBuilders) {
            b.setName(name);
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
