package org.codegist.crest.config;

import org.codegist.common.lang.State;
import org.codegist.common.net.Urls;
import org.codegist.crest.CRestProperty;
import org.codegist.crest.handler.ErrorHandler;
import org.codegist.crest.handler.ResponseHandler;
import org.codegist.crest.handler.RetryHandler;
import org.codegist.crest.interceptor.RequestInterceptor;
import org.codegist.crest.io.http.HttpMethod;
import org.codegist.crest.io.http.entity.EntityWriter;
import org.codegist.crest.io.http.entity.MultiPartEntityWriter;
import org.codegist.crest.io.http.entity.UrlEncodedFormEntityWriter;
import org.codegist.crest.io.http.param.ParamType;
import org.codegist.crest.serializer.Deserializer;
import org.codegist.crest.serializer.Serializer;
import org.codegist.crest.util.MultiParts;
import org.codegist.crest.util.Registry;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static org.codegist.common.collect.Arrays.arrify;
import static org.codegist.common.collect.Arrays.join;
import static org.codegist.crest.CRestProperty.*;
import static org.codegist.crest.config.MethodConfig.*;
import static org.codegist.crest.io.http.param.ParamType.*;

@SuppressWarnings("unchecked")
public class MethodConfigBuilder extends ConfigBuilder<MethodConfig> {

    private final Method method;
    private final InterfaceConfigBuilder parent;
    private final Map<String, ParamConfigBuilder> extraParamBuilders = new LinkedHashMap<String, ParamConfigBuilder>();
    private final ParamConfigBuilder[] methodParamConfigBuilders;
    private final Registry<String,Deserializer> mimeDeserializerRegistry;
    private final List<String> pathParts = new ArrayList<String>();
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

    MethodConfigBuilder(InterfaceConfigBuilder parent, Method method, Map<String, Object> crestProperties) {
        super(crestProperties);
        this.parent = parent;
        this.method = method;
        this.methodParamConfigBuilders = new ParamConfigBuilder[method.getParameterTypes().length];
        this.mimeDeserializerRegistry = CRestProperty.get(crestProperties, Registry.class.getName() + "#deserializers-per-mime");

        for (int i = 0; i < this.methodParamConfigBuilders.length; i++) {
            this.methodParamConfigBuilders[i] = new ParamConfigBuilder(this, crestProperties, method.getParameterTypes()[i], method.getGenericParameterTypes()[i]);
        }
    }

    /**
     * @inheritDoc
     */
    public MethodConfig build() throws Exception {
        ParamConfig[] pConfigMethod = new ParamConfig[methodParamConfigBuilders.length];
        for (int i = 0; i < methodParamConfigBuilders.length; i++) {
            pConfigMethod[i] = this.methodParamConfigBuilders[i].build();
        }
        Map<String, ParamConfig> extraParams = new LinkedHashMap<String, ParamConfig>();
        for (ParamConfigBuilder b : extraParamBuilders.values()) {
            ParamConfig bpc = b.build();
            extraParams.put(bpc.getName(), bpc);
        }

        List<String> fullPathPart = new ArrayList<String>(pathParts);
        fullPathPart.add(0, endPoint);
        String[] paths = arrify(fullPathPart, String.class);

        // make local copies so that we don't mess with builder state to be able to call build multiple times on it
        String path = Urls.normalizeSlashes(join("/", paths));
        Deserializer[] pDeserializers = arrify(this.deserializers, Deserializer.class);

        path = defaultIfUndefined(path, METHOD_CONFIG_DEFAULT_PATH, DEFAULT_PATH);
        HttpMethod pMeth = defaultIfUndefined(this.meth, METHOD_CONFIG_DEFAULT_HTTP_METHOD, DEFAULT_HTTP_METHOD);
        String pContentType = defaultIfUndefined(this.contentType, METHOD_CONFIG_DEFAULT_CONTENT_TYPE, DEFAULT_CONTENT_TYPE);
        String pAccept = defaultIfUndefined(this.accept, METHOD_CONFIG_DEFAULT_ACCEPT, DEFAULT_ACCEPT);
        ParamConfig[] defs = defaultIfUndefined(null, METHOD_CONFIG_DEFAULT_EXTRA_PARAMS, DEFAULT_EXTRA_PARAMS);
        for (ParamConfig def : defs) {
            if (extraParams.containsKey(def.getName())) {
                continue;
            }
            extraParams.put(def.getName(), def);
        }
        Long pSocketTimeout = defaultIfUndefined(this.socketTimeout, METHOD_CONFIG_DEFAULT_SO_TIMEOUT, DEFAULT_SO_TIMEOUT);
        Long pConnectionTimeout = defaultIfUndefined(this.connectionTimeout, METHOD_CONFIG_DEFAULT_CO_TIMEOUT, DEFAULT_CO_TIMEOUT);
        RequestInterceptor pRequestInterceptor = defaultIfUndefined(this.requestInterceptor, METHOD_CONFIG_DEFAULT_REQUEST_INTERCEPTOR, DEFAULT_REQUEST_INTERCEPTOR);
        ResponseHandler pResponseHandler = defaultIfUndefined(this.responseHandler, METHOD_CONFIG_DEFAULT_RESPONSE_HANDLER, DEFAULT_RESPONSE_HANDLER);
        ErrorHandler pErrorHandler = defaultIfUndefined(this.errorHandler, METHOD_CONFIG_DEFAULT_ERROR_HANDLER, DEFAULT_ERROR_HANDLER);
        RetryHandler pRetryHandler = defaultIfUndefined(this.retryHandler, METHOD_CONFIG_DEFAULT_RETRY_HANDLER, DEFAULT_RETRY_HANDLER);
        EntityWriter pEntityWriter = defaultIfUndefined(this.entityWriter, METHOD_CONFIG_DEFAULT_BODY_WRITER, DEFAULT_BODY_WRITER);
        pDeserializers = defaultIfUndefined(pDeserializers, METHOD_CONFIG_DEFAULT_DESERIALIZERS, DEFAULT_DESERIALIZERS);

        if(pEntityWriter == null && pMeth.hasEntity()) {
            Class<? extends EntityWriter> entityWriterCls = UrlEncodedFormEntityWriter.class;
            for(ParamConfig cfg : pConfigMethod){
                if(MultiParts.hasMultiPart(cfg.getMetaDatas())) {
                    entityWriterCls = MultiPartEntityWriter.class;
                    break;
                }
            }
            pEntityWriter = newInstance(entityWriterCls);
        }

        return new DefaultMethodConfig(
                method,
                RegexPathTemplate.create(path),
                pContentType,
                pAccept,
                pMeth,
                pSocketTimeout,
                pConnectionTimeout,
                pEntityWriter,
                pRequestInterceptor,
                pResponseHandler,
                pErrorHandler,
                pRetryHandler,
                pDeserializers,
                pConfigMethod,
                extraParams.values().toArray(new ParamConfig[extraParams.size()])
        );
    }

    public InterfaceConfigBuilder endMethodConfig() {
        return parent;
    }

    public MethodConfigBuilder setConsumes(String... mimeTypes) {
        State.notNull(mimeDeserializerRegistry, "Can't lookup a deserializer by mime-type. Please provide a DeserializerFactory");

        String[] mimes = new String[mimeTypes.length];
        for (int i = 0; i < mimeTypes.length; i++) {
            String mMime = replacePlaceholders(mimeTypes[i]);
            this.deserializers.add(mimeDeserializerRegistry.get(mMime));
            mimes[i] = mMime;
        }
        this.accept = join(",", mimes);
        return this;
    }

    public MethodConfigBuilder setProduces(String contentType) {
        this.contentType = replacePlaceholders(contentType);
        return this;
    }

    public MethodConfigBuilder addExtraMultiPartParam(String name, String defaultValue, String contentType, String fileName) {
        return addExtraParam(name, defaultValue, FORM, MultiParts.toMetaDatas(contentType, fileName));
    }

    public MethodConfigBuilder addExtraFormParam(String name, String defaultValue) {
        return addExtraParam(name, defaultValue, FORM);
    }

    public MethodConfigBuilder addExtraHeaderParam(String name, String defaultValue) {
        return addExtraParam(name, defaultValue, HEADER);
    }

    public MethodConfigBuilder addExtraQueryParam(String name, String defaultValue) {
        return addExtraParam(name, defaultValue, QUERY);
    }

    public MethodConfigBuilder addExtraPathParam(String name, String defaultValue) {
        return addExtraParam(name, defaultValue, PATH);
    }

    public MethodConfigBuilder addExtraCookieParam(String name, String defaultValue) {
        return addExtraParam(name, defaultValue, COOKIE);
    }

    public MethodConfigBuilder addExtraMatrixParam(String name, String defaultValue) {
        return addExtraParam(name, defaultValue, MATRIX);
    }

    public MethodConfigBuilder addExtraParam(String name, String defaultValue, ParamType type) {
        return addExtraParam(name, defaultValue, type, Collections.<String, Object>emptyMap());
    }
    public MethodConfigBuilder addExtraParam(String name, String defaultValue, ParamType type, Map<String,Object> metaDatas) {
        return startExtraParamConfig(name)
                .setDefaultValue(defaultValue)
                .setType(type)
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
            builder = new ParamConfigBuilder(this, getCRestProperties()).setName(name);
            extraParamBuilders.put(name, builder);
        }
        return builder;
    }

    public MethodConfigBuilder appendPath(String path) {
        pathParts.add(replacePlaceholders(path));
        return this;
    }

    public MethodConfigBuilder setEndPoint(String endPoint) {
        this.endPoint = replacePlaceholders(endPoint);
        return this;
    }

    public MethodConfigBuilder setHttpMethod(HttpMethod meth) {
        this.meth = meth;
        return this;
    }

    public MethodConfigBuilder setSocketTimeout(Long socketTimeout) {
        this.socketTimeout = socketTimeout;
        return this;
    }

    public MethodConfigBuilder setConnectionTimeout(Long connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
        return this;
    }

    public MethodConfigBuilder setRequestInterceptor(Class<? extends RequestInterceptor> interceptorCls) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        this.requestInterceptor = newInstance(interceptorCls);
        return this;
    }

    public MethodConfigBuilder setResponseHandler(Class<? extends ResponseHandler> responseHandlerClass) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException  {
        this.responseHandler  = newInstance(responseHandlerClass);
        return this;
    }

    public MethodConfigBuilder setErrorHandler(Class<? extends ErrorHandler> methodHandlerClass) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException  {
        this.errorHandler = newInstance(methodHandlerClass);
        return this;
    }

    public MethodConfigBuilder setRetryHandler(Class<? extends RetryHandler> retryHandlerClass) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException  {
        this.retryHandler = newInstance(retryHandlerClass);
        return this;
    }

    public MethodConfigBuilder setEntityWriter(Class<? extends EntityWriter> bodyWriterClass) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException  {
        this.entityWriter = newInstance(bodyWriterClass);
        return this;
    }

    /* PARAMS SETTINGS METHODS */

    public MethodConfigBuilder setParamsSerializer(Class<? extends Serializer> paramSerializer) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
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
