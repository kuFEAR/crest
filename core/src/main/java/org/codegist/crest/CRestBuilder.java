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

import org.codegist.common.collect.Maps;
import org.codegist.common.reflect.CglibProxyFactory;
import org.codegist.common.reflect.JdkProxyFactory;
import org.codegist.common.reflect.ProxyFactory;
import org.codegist.crest.config.AnnotationDrivenInterfaceConfigFactory;
import org.codegist.crest.config.InterfaceConfigFactory;
import org.codegist.crest.config.annotate.AnnotationHandler;
import org.codegist.crest.config.annotate.CRestAnnotations;
import org.codegist.crest.config.annotate.NoOpAnnotationHandler;
import org.codegist.crest.config.annotate.jaxrs.JaxRsAnnotations;
import org.codegist.crest.io.RequestExecutor;
import org.codegist.crest.io.RetryingRequestExecutor;
import org.codegist.crest.io.http.HttpChannelInitiator;
import org.codegist.crest.io.http.HttpRequestExecutor;
import org.codegist.crest.io.http.apache.HttpClientHttpChannelInitiator;
import org.codegist.crest.io.http.platform.HttpURLConnectionHttpChannelInitiator;
import org.codegist.crest.security.Authorization;
import org.codegist.crest.security.basic.BasicAuthorization;
import org.codegist.crest.security.http.AuthorizationHttpChannelInitiator;
import org.codegist.crest.security.http.HttpEntityParamExtractor;
import org.codegist.crest.security.oauth.OAuthToken;
import org.codegist.crest.security.oauth.OAuthenticator;
import org.codegist.crest.security.oauth.OAuthorization;
import org.codegist.crest.security.oauth.UrlEncodedFormEntityParamExtractor;
import org.codegist.crest.security.oauth.v1.OAuthenticatorV1;
import org.codegist.crest.serializer.*;
import org.codegist.crest.serializer.jackson.JacksonDeserializer;
import org.codegist.crest.serializer.jaxb.JaxbDeserializer;
import org.codegist.crest.serializer.primitive.*;
import org.codegist.crest.serializer.simplexml.SimpleXmlDeserializer;
import org.codegist.crest.util.Registry;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.util.*;

import static java.util.Collections.singletonMap;
import static org.codegist.common.collect.Arrays.arrify;
import static org.codegist.common.collect.Collections.asSet;
import static org.codegist.common.collect.Maps.putIfAbsent;
import static org.codegist.crest.CRestProperty.*;

/**
 * <p>The default build :
 * <code><pre>
 * CRest crest = new CRestBuilder().build();
 * </pre></code>
 * <p>will create {@link org.codegist.crest.CRest} with the following features :
 * <p>This default configuration has the benefit to not require any third party dependencies, but is not the recommanded one.
 * <p>For best performances, it is recommended to use the CGLib proxy factory, {@link org.codegist.common.reflect.CglibProxyFactory} (requires cglib available in the classpath) and the apache http client backed rest service
 *
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 * @see org.codegist.common.reflect.CglibProxyFactory
 * @see org.codegist.common.reflect.JdkProxyFactory
 * @see DefaultCRest
 */
public class CRestBuilder {

    private final Map<String, Object> crestProperties = new HashMap<String, Object>();
    private final Map<String, String> placeholders = new HashMap<String, String>();
    private final Map<String, HttpEntityParamExtractor> httpEntityParamExtrators = new HashMap<String, HttpEntityParamExtractor>(singletonMap("application/x-www-form-urlencoded", new UrlEncodedFormEntityParamExtractor()));

    private final Set<String> plainTextMimes = asSet("plain/text");
    private final Set<String> xmlMimes = asSet("application/xml", "text/xml");
    private final Set<String> jsonMimes = asSet("application/json", "text/javascript", "text/json");

    private final Map<String, Object> xmlDeserializerConfig = new HashMap<String, Object>();

    private final Map<String, Object> jsonDeserializerConfig = new HashMap<String, Object>();

    private final Registry.Builder<Class<? extends Annotation>,AnnotationHandler> annotationHandlerBuilder = new Registry.Builder<Class<? extends Annotation>, AnnotationHandler>(crestProperties, AnnotationHandler.class)
                            .defaultAs(new NoOpAnnotationHandler())
                            .register(CRestAnnotations.MAPPING);

    private final Registry.Builder<String,Deserializer> mimeDeserializerBuilder = new Registry.Builder<String,Deserializer>(crestProperties, Deserializer.class);
    private final Registry.Builder<Class<?>,Deserializer> classDeserializerBuilder = new Registry.Builder<Class<?>,Deserializer>(crestProperties, Deserializer.class)
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

    private final Registry.Builder<Class<?>,Serializer> classSerializerBuilder = new Registry.Builder<Class<?>,Serializer>(crestProperties, Serializer.class)
                            .defaultAs(new ToStringSerializer())
                            .register(DateSerializer.class, Date.class)
                            .register(BooleanSerializer.class, Boolean.class, boolean.class)
                            .register(FileSerializer.class, File.class)
                            .register(InputStreamSerializer.class, InputStream.class)
                            .register(ReaderSerializer.class, Reader.class);


    private ProxyFactory proxyFactory = new JdkProxyFactory();
    private Class<? extends Deserializer> xmlDeserializer = JaxbDeserializer.class;
    private Class<? extends Deserializer> jsonDeserializer = JacksonDeserializer.class;
    private boolean useHttpClient = false;
    private HttpChannelInitiator httpChannelInitiator;
    private String auth;
    private String username;
    private String password;
    private OAuthToken consumerOAuthToken;
    private OAuthToken accessOAuthToken;

    public CRest build() {
        Registry<String,Deserializer> mimeDeserializerRegistry = buildDeserializerRegistry();
        Registry<Class<?>,Serializer> classSerializerRegistry = classSerializerBuilder.build();
        Registry<Class<?>,Deserializer> classDeserializerRegistry = classDeserializerBuilder.build();

        DeserializationManager deserializationManager = new DeserializationManager(mimeDeserializerRegistry, classDeserializerRegistry);

        HttpChannelInitiator plainChannelInitiator = buildHttpChannelInitiator();
        Authorization authorization = buildAuthorization(plainChannelInitiator, deserializationManager);
        RequestExecutor requestExecutor = buildRequestExecutor(plainChannelInitiator, authorization, deserializationManager);

        InterfaceConfigFactory configFactory = new AnnotationDrivenInterfaceConfigFactory(crestProperties, annotationHandlerBuilder.build(), false);

        putIfAbsent(crestProperties, ProxyFactory.class.getName(), proxyFactory);
        putIfAbsent(crestProperties, Registry.class.getName() + "#deserializers-per-mime", mimeDeserializerRegistry);
        putIfAbsent(crestProperties, Registry.class.getName() + "#deserializers-per-class", classDeserializerRegistry);
        putIfAbsent(crestProperties, Registry.class.getName() + "#serializers-per-class", classSerializerRegistry);
        putIfAbsent(crestProperties, DeserializationManager.class.getName(), deserializationManager);
        putIfAbsent(crestProperties, RequestExecutor.class.getName(), requestExecutor);
        putIfAbsent(crestProperties, Authorization.class.getName(), authorization);
        putIfAbsent(crestProperties, InterfaceConfigFactory.class.getName(), configFactory);
        putIfAbsent(crestProperties, CREST_ANNOTATION_PLACEHOLDERS, Maps.unmodifiable(placeholders));

        return new DefaultCRest(proxyFactory, requestExecutor, configFactory, deserializationManager);
    }

    private HttpChannelInitiator buildHttpChannelInitiator() {
        if (httpChannelInitiator == null) {
            if (useHttpClient) {
                return HttpClientHttpChannelInitiator.newHttpChannelInitiator(crestProperties);
            } else {
                return new HttpURLConnectionHttpChannelInitiator();
            }
        } else {
            return httpChannelInitiator;
        }
    }

    private RequestExecutor buildRequestExecutor(HttpChannelInitiator plainChannelInitiator, Authorization authorization, DeserializationManager deserializationManager){
        HttpChannelInitiator channelInitiator = plainChannelInitiator;
        if(authorization != null) {
            channelInitiator = new AuthorizationHttpChannelInitiator(plainChannelInitiator, authorization, httpEntityParamExtrators);
        }
        return new RetryingRequestExecutor(new HttpRequestExecutor(channelInitiator, deserializationManager));
    }

    private Registry<String,Deserializer> buildDeserializerRegistry() {
        mimeDeserializerBuilder.register(jsonDeserializer, arrify(jsonMimes, String.class), jsonDeserializerConfig);
        mimeDeserializerBuilder.register(xmlDeserializer, arrify(xmlMimes, String.class), xmlDeserializerConfig);
        mimeDeserializerBuilder.register(StringDeserializer.class, arrify(plainTextMimes, String.class));
        return mimeDeserializerBuilder.build();
    }

    private Authorization buildAuthorization(HttpChannelInitiator channelInitiator, DeserializationManager deserializationManager) {
        if("oauth".equals(auth)) {
            return buildOAuthorization(channelInitiator, deserializationManager);
        }else if("basic".equals(auth)) {
            return buildBasicAuthorization();
        }else{
            return null;
        }
    }
    
    private Authorization buildBasicAuthorization() {
        try {
            return new BasicAuthorization(username, password);
        } catch (UnsupportedEncodingException e) {
            throw CRestException.handle(e);
        }
    }

    private Authorization buildOAuthorization(HttpChannelInitiator channelInitiator, DeserializationManager deserializationManager) {
        HttpRequestExecutor httpExecutor = new HttpRequestExecutor(channelInitiator, deserializationManager);
        OAuthenticator authenticator = new OAuthenticatorV1(httpExecutor, consumerOAuthToken, crestProperties);
        return new OAuthorization(authenticator, accessOAuthToken);
    }












    public CRestBuilder bind(Deserializer deserializer, String... mimeTypes) {
        this.mimeDeserializerBuilder.register(deserializer, mimeTypes);
        return this;
    }
    public CRestBuilder bind(Deserializer deserializer, Class<?>... classes) {
        this.classDeserializerBuilder.register(deserializer, classes);
        return this;
    }

    public CRestBuilder bindDeserializer(Class<? extends Deserializer> deserializer, String... mimeTypes) {
        this.mimeDeserializerBuilder.register(deserializer, mimeTypes);
        return this;
    }
    public CRestBuilder bindDeserializer(Class<? extends Deserializer> deserializer, Class<?>... classes) {
        this.classDeserializerBuilder.register(deserializer, classes);
        return this;
    }

    public CRestBuilder bind(Serializer serializer, Class<?>... classes){
        classSerializerBuilder.register(serializer, classes);
        return this;
    }

    public CRestBuilder bindSerializer(Class<? extends Serializer> serializer, Class<?>... classes){
        classSerializerBuilder.register(serializer, classes);
        return this;
    }

    public <A extends Annotation> CRestBuilder bind(Class<A> annotationCls, AnnotationHandler<A> handler){
        annotationHandlerBuilder.register(handler, annotationCls);
        return this;
    }
    public <A extends Annotation> CRestBuilder bind(Class<A> annotationCls, Class<? extends AnnotationHandler<A>> handler){
        annotationHandlerBuilder.register(handler, annotationCls);
        return this;
    }

    public CRestBuilder useHttpClient() {
        this.useHttpClient = true;
        return this;
    }

    /**
     * Resulting CRest instance will use cglib proxies to build interface instances. (requires cglib available in the classpath)
     *
     * @return current builder
     * @see org.codegist.common.reflect.CglibProxyFactory
     */
    public CRestBuilder useCglibProxies() {
        this.proxyFactory = new CglibProxyFactory();
        return this;
    }

    public CRestBuilder jaxrsAware(){
        this.annotationHandlerBuilder.register(JaxRsAnnotations.MAPPING);
        return this;
    }

    public CRestBuilder setHttpChannelInitiator(HttpChannelInitiator httpChannelInitiator) {
        this.httpChannelInitiator = httpChannelInitiator;
        return this;
    }

    /**
     * Sets the concurrency level the interfaces built with the resulting CRest instance will support.
     *
     * @param maxThread Thread count
     * @return current builder
     */
    public CRestBuilder setConcurrencyLevel(int maxThread) {
        return setProperty(CREST_CONCURRENCY_LEVEL, maxThread);
    }


    /**
     * Adds all custom properties every services build with the resulting CRest instance will be passed.
     *
     * @param crestProperties properties map
     * @return current builder
     */
    public CRestBuilder addProperties(Map<String, Object> crestProperties) {
        this.crestProperties.putAll(crestProperties);
        return this;
    }

    /**
     * Sets a custom property every services build with the resulting CRest instance will be passed.
     *
     * @param name  property key
     * @param value property value
     * @return current builder
     */
    public CRestBuilder setProperty(String name, Object value) {
        return addProperties(singletonMap(name, value));
    }

    /**
     * Sets a custom properties every services build with the resulting CRest instance will be passed.
     *
     * @param crestProperties properties map
     * @return current builder
     */
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

    /**
     * Resulting CRest instance will authentify every requests using OAuth (http://oauth.net/) authentification mechanism, using a pre-authentified access token and consumer information.
     *
     * @param consumerKey         Consumer key
     * @param consumerSecret      Consumer secret
     * @param accessToken         Preauthentified access token
     * @param accessTokenSecret   Preauthentified access token secret
     * @return current builder
     */
    public CRestBuilder oauth(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret) {
        return oauth(consumerKey,consumerSecret,accessToken, accessTokenSecret, Collections.<String, String>emptyMap());
    }

    public CRestBuilder oauth(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret, Map<String,String> accessTokenAttributes) {
        this.auth = "oauth";
        this.consumerOAuthToken = new OAuthToken(consumerKey, consumerSecret);
        this.accessOAuthToken = new OAuthToken(accessToken, accessTokenSecret, accessTokenAttributes);
        return this;
    }

    public CRestBuilder basicAuth(String username, String password) {
        this.auth = "basic";
        this.username = username;
        this.password = password;
        return this;
    }

    public CRestBuilder extractsEntityAuthParamsWith(String entityContentType, HttpEntityParamExtractor httpEntityParamExtractor){
        this.httpEntityParamExtrators.put(entityContentType, httpEntityParamExtractor);
        return this;
    }

    /**
     * Sets date serializer format to the given format.
     * <p>Shortcut to builder.setProperty(CRestProperty.CREST_DATE_FORMAT, format)
     *
     * @param format Date format to use
     * @return current builder
     * @see CRestProperty#CREST_DATE_FORMAT
     */
    public CRestBuilder dateFormat(String format) {
        return setProperty(CREST_DATE_FORMAT, format);
    }

    /**
     * Sets how boolean should be serialized.
     * <p>Shortcut to:
     * <p>builder.setProperty(CRestProperty.CREST_BOOLEAN_TRUE, trueSerialized)
     * <p>builder.setProperty(CRestProperty.CREST_BOOLEAN_FALSE, falseSerialized)
     *
     * @param trueSerialized  String representing serialized form of TRUE
     * @param falseSerialized String representing serialized form of FALSE
     * @return current builder
     * @see CRestProperty#CREST_BOOLEAN_TRUE
     * @see CRestProperty#CREST_BOOLEAN_FALSE
     */
    public CRestBuilder booleanFormat(String trueSerialized, String falseSerialized) {
        return setProperty(CREST_BOOLEAN_TRUE, trueSerialized).setProperty(CREST_BOOLEAN_FALSE, falseSerialized);
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

    public CRestBuilder deserializeXmlWith(Class<? extends Deserializer> deserializer, Map<String, Object> config) {
        this.xmlDeserializer = deserializer;
        this.xmlDeserializerConfig.clear();
        this.xmlDeserializerConfig.putAll(config);
        return this;
    }

    public CRestBuilder deserializerJsonWith(Class<? extends Deserializer> deserializer, Map<String, Object> config) {
        this.jsonDeserializer = deserializer;
        this.jsonDeserializerConfig.clear();
        this.jsonDeserializerConfig.putAll(config);
        return this;
    }

    public CRestBuilder deserializeXmlWith(Class<? extends Deserializer> deserializer) {
        return deserializeXmlWith(deserializer, Collections.<String, Object>emptyMap());
    }

    public CRestBuilder deserializeXmlWithJaxb() {
        return deserializeXmlWithJaxb(Collections.<String, Object>emptyMap());
    }

    public CRestBuilder deserializeXmlWithJaxb(Map<String, Object> jaxbConfig) {
        return deserializeXmlWith(JaxbDeserializer.class, jaxbConfig);
    }

    public CRestBuilder deserializeXmlWithSimpleXml() {
        return deserializeXmlWithSimpleXml(Collections.<String, Object>emptyMap());
    }

    public CRestBuilder deserializeXmlWithSimpleXml(boolean strict) {
        return deserializeXmlWithSimpleXml(Collections.<String, Object>singletonMap(SimpleXmlDeserializer.STRICT_PROP, strict));
    }

    public CRestBuilder deserializeXmlWithSimpleXml(Map<String, Object> config) {
        return deserializeXmlWith(SimpleXmlDeserializer.class, config);
    }

    public CRestBuilder deserializerJsonWith(Class<? extends Deserializer> deserializer) {
        return deserializerJsonWith(deserializer, Collections.<String, Object>emptyMap());
    }

    public CRestBuilder deserializerJsonWithJackson() {
        return deserializerJsonWithJackson(Collections.<String, Object>emptyMap());
    }

    public CRestBuilder deserializerJsonWithJackson(Map<String, Object> config) {
        return deserializerJsonWith(JacksonDeserializer.class, config);
    }

}