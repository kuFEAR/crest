/*
 * Copyright 2010 CodeGist.org
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 * ===================================================================
 *
 * More information at http://www.codegist.org.
 */

package org.codegist.crest;

import org.codegist.common.reflect.JdkProxyFactory;
import org.codegist.common.reflect.ProxyFactory;
import org.codegist.crest.config.*;
import org.codegist.crest.config.annotate.AnnotationHandler;
import org.codegist.crest.config.annotate.CRestAnnotations;
import org.codegist.crest.config.annotate.NoOpAnnotationHandler;
import org.codegist.crest.config.annotate.jaxrs.JaxRsAnnotations;
import org.codegist.crest.io.RequestBuilderFactory;
import org.codegist.crest.io.RequestExecutor;
import org.codegist.crest.io.RetryingRequestExecutor;
import org.codegist.crest.io.http.*;
import org.codegist.crest.io.http.platform.HttpURLConnectionHttpChannelFactory;
import org.codegist.crest.security.Authorization;
import org.codegist.crest.security.basic.BasicAuthorization;
import org.codegist.crest.security.handler.RefreshAuthorizationRetryHandler;
import org.codegist.crest.security.oauth.OAuthApi;
import org.codegist.crest.security.oauth.OAuthToken;
import org.codegist.crest.security.oauth.OAuthorization;
import org.codegist.crest.security.oauth.v1.OAuthApiV1Builder;
import org.codegist.crest.security.oauth.v1.OAuthenticatorV1;
import org.codegist.crest.serializer.*;
import org.codegist.crest.serializer.jackson.JacksonDeserializer;
import org.codegist.crest.serializer.jaxb.JaxbDeserializer;
import org.codegist.crest.serializer.primitive.*;
import org.codegist.crest.util.ComponentFactory;
import org.codegist.crest.util.ComponentRegistry;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.lang.annotation.Annotation;
import java.util.*;

import static java.util.Collections.singletonMap;
import static org.codegist.common.collect.Arrays.arrify;
import static org.codegist.common.collect.Collections.asSet;
import static org.codegist.common.collect.Maps.putIfAbsent;
import static org.codegist.crest.CRestConfig.*;
import static org.codegist.crest.io.http.HttpConstants.HTTP_UNAUTHORIZED;
import static org.codegist.crest.util.Placeholders.compile;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class CRestBuilder {

    private final Map<String, Object> crestProperties = new HashMap<String, Object>();
    private final RequestBuilderFactory requestBuilderFactory = new HttpRequestBuilderFactory();
    private final Map<String, String> placeholders = new HashMap<String, String>();
    private final Map<String, EntityParamExtractor> httpEntityParamExtrators = new HashMap<String, EntityParamExtractor>(singletonMap("application/x-www-form-urlencoded", new UrlEncodedFormEntityParamExtractor()));

    private final Set<String> plainTextMimes = asSet("plain/text");
    private final Set<String> xmlMimes = asSet("application/xml", "text/xml");
    private final Set<String> jsonMimes = asSet("application/json", "application/javascript", "text/javascript", "text/json");

    private final Map<String, Object> xmlDeserializerConfig = new HashMap<String, Object>();

    private final Map<String, Object> jsonDeserializerConfig = new HashMap<String, Object>();

    private final ComponentRegistry.Builder<Class<? extends Annotation>, AnnotationHandler> annotationHandlerBuilder = new ComponentRegistry.Builder<Class<? extends Annotation>, AnnotationHandler>()
                            .defaultAs(NoOpAnnotationHandler.class)
                            .register(CRestAnnotations.getMapping());

    private final ComponentRegistry.Builder<String,Deserializer> mimeDeserializerBuilder = new ComponentRegistry.Builder<String,Deserializer>();
    private final ComponentRegistry.Builder<Class<?>,Deserializer> classDeserializerBuilder = new ComponentRegistry.Builder<Class<?>,Deserializer>()
                            .register(VoidDeserializer.class, Void.class, void.class)
                            .register(ByteArrayDeserializer.class, byte[].class)
                            .register(StringDeserializer.class, String.class)
                            .register(ByteWrapperDeserializer.class, Byte.class)
                            .register(BytePrimitiveDeserializer.class, byte.class)
                            .register(ShortWrapperDeserializer.class, Short.class)
                            .register(ShortPrimitiveDeserializer.class, short.class)
                            .register(IntegerWrapperDeserializer.class, Integer.class)
                            .register(IntegerPrimitiveDeserializer.class, int.class)
                            .register(LongWrapperDeserializer.class, Long.class)
                            .register(LongPrimitiveDeserializer.class, long.class)
                            .register(DoubleWrapperDeserializer.class, Double.class)
                            .register(DoublePrimitiveDeserializer.class, double.class)
                            .register(FloatWrapperDeserializer.class, Float.class)
                            .register(FloatPrimitiveDeserializer.class, float.class)
                            .register(BooleanWrapperDeserializer.class, Boolean.class)
                            .register(BooleanPrimitiveDeserializer.class, boolean.class)
                            .register(CharacterWrapperDeserializer.class, Character.class)
                            .register(CharacterPrimitiveDeserializer.class, char.class)
                            .register(InputStreamDeserializer.class, InputStream.class)
                            .register(ReaderDeserializer.class, Reader.class);

    private final ComponentRegistry.Builder<Class<?>,Serializer> classSerializerBuilder = new ComponentRegistry.Builder<Class<?>,Serializer>()
                            .defaultAs(ToStringSerializer.class)
                            .register(DateSerializer.class, Date.class)
                            .register(BooleanSerializer.class, Boolean.class, boolean.class)
                            .register(FileSerializer.class, File.class)
                            .register(InputStreamSerializer.class, InputStream.class)
                            .register(ReaderSerializer.class, Reader.class);


    private Class<? extends ProxyFactory> proxyFactoryClass = JdkProxyFactory.class;
    private Class<? extends Deserializer> xmlDeserializer = JaxbDeserializer.class;
    private Class<? extends Deserializer> jsonDeserializer = JacksonDeserializer.class;
    private Class<? extends HttpChannelFactory> httpChannelFactoryClass = HttpURLConnectionHttpChannelFactory.class;
    private HttpChannelFactory httpChannelFactory;
    private ProxyFactory proxyFactory;
    private String auth;
    private String username;
    private String password;
    private OAuthToken consumerOAuthToken;
    private OAuthToken accessOAuthToken;
    private String accessTokenRefreshUrl;

    public CRest build() {
        putIfAbsentAndNotNull(crestProperties, CRestConfig.class.getName() + "#placeholders", compile(placeholders));
        CRestConfig crestConfig = new DefaultCRestConfig(crestProperties);

        ProxyFactory proxyFactory = getInstance(this.proxyFactory, this.proxyFactoryClass, crestConfig);
        HttpChannelFactory plainChannelFactory = getInstance(this.httpChannelFactory, this.httpChannelFactoryClass, crestConfig);

        Authorization authorization = buildAuthorization(plainChannelFactory);
        putIfAbsentAndNotNull(crestProperties, Authorization.class.getName(), authorization);

        ComponentRegistry<String,Deserializer> mimeDeserializerRegistry = buildDeserializerRegistry(crestConfig);
        ComponentRegistry<Class<?>,Deserializer> classDeserializerRegistry = classDeserializerBuilder.build(crestConfig);

        ResponseDeserializer mimeResponseDeserializer = new ResponseDeserializerByMimeType(mimeDeserializerRegistry);
        ResponseDeserializer classResponseDeserializer = new ResponseDeserializerByClass(classDeserializerRegistry);
        ResponseDeserializer deserializersResponseDeserializer = new ResponseDeserializerByDeserializers();

        ResponseDeserializer baseResponseDeserializer = new ResponseDeserializerComposite(deserializersResponseDeserializer, mimeResponseDeserializer, classResponseDeserializer);
        ResponseDeserializer customTypeResponseDeserializer = new ResponseDeserializerComposite(classResponseDeserializer, mimeResponseDeserializer);

        RequestExecutor requestExecutor = buildRequestExecutor(plainChannelFactory, authorization, baseResponseDeserializer, customTypeResponseDeserializer);

        ComponentRegistry<Class<?>,Serializer> classSerializerRegistry = classSerializerBuilder.build(crestConfig);
        InterfaceConfigBuilderFactory icbf = new DefaultInterfaceConfigBuilderFactory(crestConfig, mimeDeserializerRegistry, classSerializerRegistry);

        if(JaxRsAnnotations.isJaxRsAware()) {
            this.annotationHandlerBuilder.register(JaxRsAnnotations.getMapping());
        }

        InterfaceConfigFactory configFactory = new AnnotationDrivenInterfaceConfigFactory(icbf, annotationHandlerBuilder.build(crestConfig));

        return new DefaultCRest(proxyFactory, requestExecutor, requestBuilderFactory, configFactory);
    }

    public <T> T build(Class<T> interfaze) {
        return build().build(interfaze);
    }

    private static <K,V> void putIfAbsentAndNotNull(Map<K, V> map, K key, V value){
        if(value == null) {
            return;
        }
        putIfAbsent(map, key, value);
    }

    private static <T> T getInstance(T defaultIfSet, Class<? extends T> klass, CRestConfig crestConfig) {
        if(defaultIfSet != null) {
            return defaultIfSet;
        }
        try {
            return ComponentFactory.instantiate(klass, crestConfig);
        } catch (Exception e) {
            throw CRestException.handle(e);
        }
    }

    private RequestExecutor buildRequestExecutor(HttpChannelFactory plainChannelFactory, Authorization authorization, ResponseDeserializer baseResponseDeserializer, ResponseDeserializer customTypeResponseDeserializer){
        HttpChannelFactory channelFactory = plainChannelFactory;
        if(authorization != null) {
            channelFactory = new AuthorizationHttpChannelFactory(plainChannelFactory, authorization, httpEntityParamExtrators);
        }
        return new RetryingRequestExecutor(new HttpRequestExecutor(channelFactory, baseResponseDeserializer, customTypeResponseDeserializer));
    }

    private ComponentRegistry<String,Deserializer> buildDeserializerRegistry(CRestConfig crestConfig) {
        mimeDeserializerBuilder.register(jsonDeserializer, arrify(jsonMimes, String.class), jsonDeserializerConfig);
        mimeDeserializerBuilder.register(xmlDeserializer, arrify(xmlMimes, String.class), xmlDeserializerConfig);
        mimeDeserializerBuilder.register(StringDeserializer.class, arrify(plainTextMimes, String.class));
        return mimeDeserializerBuilder.build(crestConfig);
    }

    private Authorization buildAuthorization(HttpChannelFactory channelFactory) {
        if("oauth".equals(auth)) {
            return buildOAuthorization(channelFactory);
        }else if("basic".equals(auth)) {
            return buildBasicAuthorization();
        }else{
            return null;
        }
    }

    private Authorization buildBasicAuthorization() {
        return new BasicAuthorization(username, password);
    }

    private Authorization buildOAuthorization(HttpChannelFactory channelFactory) {
        OAuthenticatorV1 authenticator ;
        try {
            authenticator = new OAuthenticatorV1(consumerOAuthToken);
        } catch (Exception e) {
            throw CRestException.handle(e);
        }
        OAuthApi oAuthApi = buildOAuthApi(channelFactory);
        if(oAuthApi == null) {
            return new OAuthorization(accessOAuthToken, authenticator);
        }else{
            return new OAuthorization(accessOAuthToken, authenticator, oAuthApi);
        }
    }

    private OAuthApi buildOAuthApi(HttpChannelFactory channelFactory) {
        if(accessTokenRefreshUrl == null) {
            return null;
        }

        int indexOfProtocol = accessTokenRefreshUrl.indexOf("://") + 3;
        int indexOfServer = accessTokenRefreshUrl.indexOf('/', indexOfProtocol);
        String endPoint = accessTokenRefreshUrl.substring(0, indexOfServer);
        String path = accessTokenRefreshUrl.substring(indexOfServer);

        return new OAuthApiV1Builder(consumerOAuthToken, endPoint)
                .refreshAccessTokenFrom(path)
                .using(channelFactory)
                .build();
    }

    public CRestBuilder setProxyFactory(Class<? extends ProxyFactory> proxyFactory) {
        this.proxyFactoryClass = proxyFactory;
        return this;
    }

    public CRestBuilder setProxyFactory(ProxyFactory proxyFactory) {
        this.proxyFactory = proxyFactory;
        return this;
    }

    public CRestBuilder setHttpChannelFactory(Class<? extends HttpChannelFactory> httpChannelFactory) {
        this.httpChannelFactoryClass = httpChannelFactory;
        return this;
    }

    public CRestBuilder setHttpChannelFactory(HttpChannelFactory httpChannelFactory) {
        this.httpChannelFactory = httpChannelFactory;
        return this;
    }

    public CRestBuilder setConcurrencyLevel(int maxThread) {
        return property(CREST_CONCURRENCY_LEVEL, maxThread);
    }

    public CRestBuilder dateFormat(String format) {
        return property(CREST_DATE_FORMAT, format);
    }

    public CRestBuilder booleanFormat(String trueSerialized, String falseSerialized) {
        return property(CREST_BOOLEAN_TRUE, trueSerialized).property(CREST_BOOLEAN_FALSE, falseSerialized);
    }


    public CRestBuilder addProperties(Map<String, Object> crestProperties) {
        this.crestProperties.putAll(crestProperties);
        return this;
    }

    public CRestBuilder endpoint(String endpoint) {
        return property(MethodConfig.METHOD_CONFIG_DEFAULT_ENDPOINT, endpoint);
    }
    
    public CRestBuilder property(String name, Object value) {
        return addProperties(singletonMap(name, value));
    }

    public CRestBuilder setProperties(Map<String, Object> crestProperties) {
        this.crestProperties.clear();
        return addProperties(crestProperties);
    }

    public CRestBuilder addPlaceholders(Map<String,String> placeholders) {
        this.placeholders.putAll(placeholders);
        return this;
    }

    public CRestBuilder placeholder(String placeholder, String value) {
        return addPlaceholders(singletonMap(placeholder, value));
    }

    public CRestBuilder setPlaceholders(Map<String,String> placeholders) {
        this.placeholders.clear();
        return addPlaceholders(placeholders);
    }

    public <A extends Annotation> CRestBuilder bindAnnotationHandler(Class<? extends AnnotationHandler<A>> handler, Class<A> annotationCls){
        return bindAnnotationHandler(handler, annotationCls, Collections.<String, Object>emptyMap());
    }
    
    public <A extends Annotation> CRestBuilder bindAnnotationHandler(Class<? extends AnnotationHandler<A>> handler, Class<A> annotationCls, Map<String, Object> config){
        annotationHandlerBuilder.register(handler, new Class[]{annotationCls}, config);
        return this;
    }

    public CRestBuilder bindDeserializer(Class<? extends Deserializer> deserializer, String... mimeTypes) {
        return bindDeserializer(deserializer, mimeTypes, Collections.<String, Object>emptyMap());
    }
    public CRestBuilder bindDeserializer(Class<? extends Deserializer> deserializer, String[] mimeTypes, Map<String, Object> config) {
        this.mimeDeserializerBuilder.register(deserializer, mimeTypes, config);
        return this;
    }

    public CRestBuilder bindDeserializer(Class<? extends Deserializer> deserializer, Class<?>... classes) {
        return bindDeserializer(deserializer, classes, Collections.<String, Object>emptyMap());
    }

    public CRestBuilder bindDeserializer(Class<? extends Deserializer> deserializer, Class<?>[] classes, Map<String, Object> config) {
        this.classDeserializerBuilder.register(deserializer, classes, config);
        return this;
    }

    public CRestBuilder bindSerializer(Class<? extends Serializer> serializer, Class<?>... classes){
        return bindSerializer(serializer, classes, Collections.<String, Object>emptyMap());
    }
    public CRestBuilder bindSerializer(Class<? extends Serializer> serializer, Class<?>[] classes, Map<String, Object> config){
        classSerializerBuilder.register(serializer, classes, config);
        return this;
    }


    public CRestBuilder oauth(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret) {
        return oauth(consumerKey,consumerSecret,accessToken, accessTokenSecret, null, null);
    }

    public CRestBuilder oauth(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret, String sessionHandle, String accessTokenRefreshUrl) {
        this.auth = "oauth";
        if(sessionHandle != null) {
            this.accessOAuthToken = new OAuthToken(accessToken, accessTokenSecret, singletonMap("oauth_session_handle", sessionHandle));
        }else{
            this.accessOAuthToken = new OAuthToken(accessToken, accessTokenSecret);
        }
        if(accessTokenRefreshUrl != null) {
            property(MethodConfig.METHOD_CONFIG_DEFAULT_RETRY_HANDLER, RefreshAuthorizationRetryHandler.class);
            property(RefreshAuthorizationRetryHandler.UNAUTHORIZED_STATUS_CODE_PROP, HTTP_UNAUTHORIZED);
        }
        this.accessTokenRefreshUrl = accessTokenRefreshUrl;
        this.consumerOAuthToken = new OAuthToken(consumerKey, consumerSecret);
        return this;
    }

    public CRestBuilder basicAuth(String username, String password) {
        this.auth = "basic";
        this.username = username;
        this.password = password;
        return this;
    }

    public CRestBuilder extractsEntityAuthParamsWith(String entityContentType, EntityParamExtractor entityParamExtractor){
        this.httpEntityParamExtrators.put(entityContentType, entityParamExtractor);
        return this;
    }

    public CRestBuilder bindJsonDeserializerWith(String... mimeTypes) {
        this.jsonMimes.addAll(asSet(mimeTypes));
        return this;
    }

    public CRestBuilder bindXmlDeserializerWith(String... mimeTypes) {
        this.xmlMimes.addAll(asSet(mimeTypes));
        return this;
    }

    public CRestBuilder bindPlainTextDeserializerWith(String... mimeTypes) {
        this.plainTextMimes.addAll(asSet(mimeTypes));
        return this;
    }


    public CRestBuilder deserializeXmlWith(Class<? extends Deserializer> deserializer) {
        return deserializeXmlWith(deserializer, Collections.<String, Object>emptyMap());
    }

    public CRestBuilder deserializeXmlWith(Class<? extends Deserializer> deserializer, Map<String, Object> config) {
        this.xmlDeserializer = deserializer;
        this.xmlDeserializerConfig.clear();
        this.xmlDeserializerConfig.putAll(config);
        return this;
    }


    public CRestBuilder deserializerJsonWith(Class<? extends Deserializer> deserializer) {
        return deserializerJsonWith(deserializer, Collections.<String, Object>emptyMap());
    }

    public CRestBuilder deserializerJsonWith(Class<? extends Deserializer> deserializer, Map<String, Object> config) {
        this.jsonDeserializer = deserializer;
        this.jsonDeserializerConfig.clear();
        this.jsonDeserializerConfig.putAll(config);
        return this;
    }

}