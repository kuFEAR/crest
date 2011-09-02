package org.codegist.crest.config;

import org.codegist.common.lang.State;
import org.codegist.common.lang.ToStringBuilder;
import org.codegist.common.net.Urls;
import org.codegist.crest.CRestConfig;
import org.codegist.crest.entity.EntityWriter;
import org.codegist.crest.entity.UrlEncodedFormEntityWriter;
import org.codegist.crest.entity.multipart.MultiPartEntityWriter;
import org.codegist.crest.handler.*;
import org.codegist.crest.interceptor.NoOpRequestInterceptor;
import org.codegist.crest.interceptor.RequestInterceptor;
import org.codegist.crest.serializer.Deserializer;
import org.codegist.crest.serializer.Serializer;
import org.codegist.crest.util.MultiParts;
import org.codegist.crest.util.Registry;

import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.codegist.common.collect.Arrays.*;
import static org.codegist.crest.config.MethodConfig.*;

class DefaultMethodConfigBuilder extends ConfigBuilder implements MethodConfigBuilder {

    private static final String ENDPOINT_MSG = new StringBuilder("End-point is mandatory. This is probably due to a missing or empty @EndPoint annotation.\n")
                                                         .append("Either provide an @EndPoint annotation or build a CRest instance as follow:\n\n")
                                                         .append("   String defaultEndPoint = ...;\n")
                                                         .append("   CRest crest = CRest.property(MethodConfig.METHOD_CONFIG_DEFAULT_ENDPOINT, defaultEndPoint).build();\n")
                                                         .append("\nLocation information:\n%s")
                                                         .toString();

    private final InterfaceConfigBuilder parent;
    private final Method method;
    private final List<ParamConfigBuilder> extraParamBuilders = new ArrayList<ParamConfigBuilder>();
    private final List<ParamConfigBuilder> methodParamConfigBuilders = new ArrayList<ParamConfigBuilder>();
    private final Registry<String,Deserializer> mimeDeserializerRegistry;
    private final Registry<Class<?>,Serializer> classSerializerRegistry;
    private final ParamConfig[] extraParams;

    private String endPoint = null;
    private String produces = null;
    private Charset charset = Charset.forName("UTF-8");
    private MethodType meth = MethodType.getDefault();
    private Integer socketTimeout = 20000;
    private Integer connectionTimeout = 20000;
    private Class<? extends RequestInterceptor> requestInterceptor = NoOpRequestInterceptor.class;
    private Class<? extends ResponseHandler> responseHandler = DefaultResponseHandler.class;
    private Class<? extends ErrorHandler> errorHandler = ErrorDelegatorHandler.class;
    private Class<? extends RetryHandler> retryHandler = MaxAttemptRetryHandler.class;
    private Class<? extends EntityWriter> entityWriter = null;
    private final List<Class<? extends Deserializer>> deserializers = new ArrayList<Class<? extends Deserializer>>();
    private final List<String> pathSegments = new ArrayList<String>();
    private final List<String> consumes = new ArrayList<String>(asList("*/*"));

    DefaultMethodConfigBuilder(InterfaceConfigBuilder parent, Method method, CRestConfig crestConfig, Registry<String,Deserializer> mimeDeserializerRegistry, Registry<Class<?>, Serializer> classSerializerRegistry) {
        super(crestConfig);
        this.parent = parent;
        this.method = method;
        this.mimeDeserializerRegistry = mimeDeserializerRegistry;
        this.classSerializerRegistry = classSerializerRegistry;

        for (int i = 0; i < method.getParameterTypes().length; i++) {
            ParamConfigBuilder pcb = new DefaultParamConfigBuilder(this, crestConfig, classSerializerRegistry, method.getParameterTypes()[i], method.getGenericParameterTypes()[i]);
            this.methodParamConfigBuilders.add(pcb);
        }

        this.endPoint = override(METHOD_CONFIG_DEFAULT_ENDPOINT, this.endPoint);
        this.produces = override(METHOD_CONFIG_DEFAULT_PRODUCES, this.produces);
        this.charset = override(METHOD_CONFIG_DEFAULT_CHARSET, this.charset);
        this.meth = override(METHOD_CONFIG_DEFAULT_TYPE, this.meth);
        this.socketTimeout = override(METHOD_CONFIG_DEFAULT_SO_TIMEOUT, this.socketTimeout);
        this.connectionTimeout = override(METHOD_CONFIG_DEFAULT_CO_TIMEOUT, this.connectionTimeout);
        this.requestInterceptor = override(METHOD_CONFIG_DEFAULT_REQUEST_INTERCEPTOR, this.requestInterceptor);
        this.responseHandler = override(METHOD_CONFIG_DEFAULT_RESPONSE_HANDLER, this.responseHandler);
        this.errorHandler = override(METHOD_CONFIG_DEFAULT_ERROR_HANDLER, this.errorHandler);
        this.retryHandler = override(METHOD_CONFIG_DEFAULT_RETRY_HANDLER, this.retryHandler);
        this.entityWriter = override(METHOD_CONFIG_DEFAULT_ENTITY_WRITER, this.entityWriter);
        this.extraParams = override(METHOD_CONFIG_DEFAULT_EXTRA_PARAMS, new ParamConfig[0]);

        List<Class<? extends Deserializer>> pDeserializers = override(METHOD_CONFIG_DEFAULT_DESERIALIZERS, this.deserializers);
        if(pDeserializers != this.deserializers) {
            this.deserializers.clear();
            this.deserializers.addAll(pDeserializers);
        }

        List<String> pConsumes = override(METHOD_CONFIG_DEFAULT_CONSUMES, this.consumes);
        if(pConsumes != this.consumes) {
            this.consumes.clear();
            this.consumes.addAll(pConsumes);
        }

        List<String> pPathSegments = override(METHOD_CONFIG_DEFAULT_PATH, this.pathSegments);
        if(pPathSegments != this.pathSegments) {
            this.pathSegments.clear();
            this.pathSegments.addAll(pPathSegments);
        }
    }

    /**
     * @inheritDoc
     */
    public MethodConfig build() throws Exception {
        validate();
        ParamConfig[] pConfigMethod = buildParams(methodParamConfigBuilders);
        ParamConfig[] pExtraParams = merge(ParamConfig.class, extraParams, buildParams(extraParamBuilders));
        ParamConfig[] allParams = merge(ParamConfig.class, pConfigMethod, pExtraParams);

        return new DefaultMethodConfig(
                charset,
                method,
                RegexPathTemplate.create(buildPath()),
                produces,
                arrify(this.consumes, String.class),
                meth,
                socketTimeout,
                connectionTimeout,
                getEntityWriter(allParams),
                instantiate(requestInterceptor),
                instantiate(responseHandler),
                instantiate(errorHandler),
                instantiate(retryHandler),
                getDeserializers(),
                pConfigMethod,
                pExtraParams
        );
    }

    private void validate(){
        State.notBlank(endPoint, ENDPOINT_MSG, this);
    }

    private Deserializer[] getDeserializers(){
        List<Deserializer> pDeserializers = new ArrayList<Deserializer>();

        for(Class<? extends Deserializer> deserializerCls : this.deserializers){
            pDeserializers.add(instantiate(deserializerCls));
        }
        for(String consume : consumes){
            if("*/*".equals(consume)) {
                continue;
            }
            pDeserializers.add(mimeDeserializerRegistry.get(consume));
        }

        return arrify(pDeserializers, Deserializer.class);
    }

    private EntityWriter getEntityWriter(ParamConfig[] params){
        if(this.entityWriter != null) {
            return instantiate(this.entityWriter);
        }else if(!meth.hasEntity()) {
            return null;
        }else if(MultiParts.hasMultiPart(params)) {
            return instantiate(MultiPartEntityWriter.class);
        }else{
            return instantiate(UrlEncodedFormEntityWriter.class);
        }
    }

    private String buildPath(){
        List<String> pPathParts = new ArrayList<String>(pathSegments);
        pPathParts.add(0, endPoint);
        String[] paths = arrify(pPathParts, String.class);
        return paths.length > 1 ? Urls.normalizeSlashes(join("/", paths)) : paths[0];
    }

    private static ParamConfig[] buildParams(List<ParamConfigBuilder> paramConfigBuilders) throws Exception {
        ParamConfig[] pc = new ParamConfig[paramConfigBuilders.size()];
        for (int i = 0; i < paramConfigBuilders.size(); i++) {
            pc[i] = paramConfigBuilders.get(i).build();
        }
        return pc;
    }

    public ParamConfigBuilder startParamConfig(int index) {
        return methodParamConfigBuilders.get(index);
    }

    public ParamConfigBuilder startExtraParamConfig() {
        ParamConfigBuilder pcb = new DefaultParamConfigBuilder(this, getCRestConfig(), classSerializerRegistry, String.class, String.class);
        extraParamBuilders.add(pcb);
        return pcb;
    }


    public MethodConfigBuilder setConsumes(String... mimeTypes) {
        this.consumes.clear();
        this.consumes.addAll(asList(mimeTypes));
        return this;
    }

    public MethodConfigBuilder setCharset(Charset charset){
        this.charset = charset;
        return this;
    }

    public MethodConfigBuilder setProduces(String contentType) {
        this.produces = contentType;
        return this;
    }

    public MethodConfigBuilder appendPath(String path) {
        pathSegments.add(path);
        return this;
    }

    public MethodConfigBuilder setEndPoint(String endPoint) {
        this.endPoint = endPoint;
        return this;
    }

    public MethodConfigBuilder setType(MethodType meth) {
        this.meth = meth;
        return this;
    }

    public MethodConfigBuilder setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
        return this;
    }

    public MethodConfigBuilder setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
        return this;
    }

    public MethodConfigBuilder setDeserializer(Class<? extends Deserializer> deserializer) {
        this.deserializers.clear();
        this.deserializers.add(deserializer);
        return this;
    }

    public MethodConfigBuilder setRequestInterceptor(Class<? extends RequestInterceptor> interceptorCls)  {
        this.requestInterceptor = interceptorCls;
        return this;
    }

    public MethodConfigBuilder setResponseHandler(Class<? extends ResponseHandler> responseHandlerClass)   {
        this.responseHandler  = responseHandlerClass;
        return this;
    }

    public MethodConfigBuilder setErrorHandler(Class<? extends ErrorHandler> methodHandlerClass)   {
        this.errorHandler = methodHandlerClass;
        return this;
    }

    public MethodConfigBuilder setRetryHandler(Class<? extends RetryHandler> retryHandlerClass)   {
        this.retryHandler = retryHandlerClass;
        return this;
    }

    public MethodConfigBuilder setEntityWriter(Class<? extends EntityWriter> bodyWriterClass)   {
        this.entityWriter = bodyWriterClass;
        return this;
    }

    /* PARAMS SETTINGS METHODS */

    public MethodConfigBuilder setParamsSerializer(Class<? extends Serializer> paramSerializer) {
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

    @Override
    public String toString() {
        return new ToStringBuilder("Method")
                .append("method", method)
                .append("interface", parent)
                .toString();
    }
}
