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
import org.codegist.crest.config.CRestAnnotationDrivenInterfaceConfigFactory;
import org.codegist.crest.config.InterfaceConfigFactory;
import org.codegist.crest.config.OverridingInterfaceConfigFactory;
import org.codegist.crest.http.*;
import org.codegist.crest.serializer.*;
import org.codegist.crest.serializer.jackson.JacksonDeserializer;
import org.codegist.crest.serializer.jackson.JacksonSerializer;
import org.codegist.crest.serializer.jaxb.JaxbDeserializer;
import org.codegist.crest.serializer.jaxb.JaxbSerializer;
import org.codegist.crest.serializer.jaxb.XmlEncodedFormJaxbSerializer;
import org.codegist.crest.serializer.simplexml.SimpleXmlDeserializer;
import org.codegist.crest.serializer.simplexml.SimpleXmlSerializer;
import org.codegist.crest.serializer.simplexml.XmlEncodedFormSimpleXmlSerializer;

import java.lang.reflect.Type;
import java.util.*;

import static java.util.Arrays.asList;
import static org.codegist.crest.CRestProperty.*;

/**
 * <p>The default build :
 * <code><pre>
 * CRest crest = new CRestBuilder().build();
 * </pre></code>
 * <p>will create {@link org.codegist.crest.CRest} with the following features :
 * <p>- Annotation driven configuration handled by {@link org.codegist.crest.config.CRestAnnotationDrivenInterfaceConfigFactory}, lookup for annotation in package {@link org.codegist.crest.annotate}.
 * <p>- Raw response return, meaning the given interface method return type must be either java.io.String, java.io.InputStream or java.io.Reader.
 * <p>- HTTP calls handled by {@link org.codegist.crest.DefaultRestService}
 * <p>- Uses JDK dynamics proxies to instanciates given interfaces
 * <p/>
 * <p>This default configuration has the benefit to not require any third party dependencies, but is not the recommanded one.
 * <p>For best performances, it is recommended to use the CGLib proxy factory, {@link org.codegist.common.reflect.CglibProxyFactory} (requires cglib available in the classpath) and the apache http client backed rest service {@link org.codegist.crest.HttpClientRestService}, see {@link CRestBuilder#useHttpClientRestService()}.
 *
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 * @see org.codegist.crest.config.CRestAnnotationDrivenInterfaceConfigFactory
 * @see org.codegist.crest.config.PropertiesDrivenInterfaceConfigFactory
 * @see org.codegist.crest.DefaultRestService
 * @see org.codegist.crest.HttpClientRestService
 * @see org.codegist.common.reflect.CglibProxyFactory
 * @see org.codegist.common.reflect.JdkProxyFactory
 * @see DefaultCRest
 */
public class CRestBuilder {

    private static final String[] FORM_XML_ENCODED_MIME_TYPES = {"application/form-xmlencoded"};
    private static final String[] FORM_JSON_ENCODED_MIME_TYPES = {"application/form-jsonencoded"};

    private final static String DEFAULT_JSON_ACCEPT_HEADER = "application/json";
    private final static String DEFAULT_XML_ACCEPT_HEADER = "application/xml";

    private final static String[] DEFAULT_PLAINTEXT_MIMETYPES = {"plain/text"};
    private final static String[] DEFAULT_XML_MIMETYPES = {DEFAULT_XML_ACCEPT_HEADER, "text/xml"};
    private final static String[] DEFAULT_JSON_MIMETYPES= {DEFAULT_JSON_ACCEPT_HEADER, "text/javascript", "text/json"};

    private final static String DEFAULT_XML_WRAPPER_ELEMENT_NAME = "formdata";

    private final static int RET_TYPE_JSON = 0;
    private final static int RET_TYPE_XML = 1;
//    private final static int RET_TYPE_CUSTOM = 2;
    private final static int RET_TYPE_RAW = 3;

    private final static int PROXY_TYPE_JDK = 0;
    private final static int PROXY_TYPE_CGLIB = 1;

    private final static int DESERIALIZER_XML_JAXB = 1;
    private final static int DESERIALIZER_XML_SIMPLEXML = 2;
    private final static int DESERIALIZER_XML_CUSTOM = 3;

    private final static int DESERIALIZER_JSON_JACKSON = 1;
    private final static int DESERIALIZER_JSON_CUSTOM = 2;

    private final static int SERIALIZER_XML_JAXB = 1;
    private final static int SERIALIZER_XML_SIMPLEXML = 2;
    private final static int SERIALIZER_XML_CUSTOM = 3;

    private final static int SERIALIZER_JSON_JACKSON = 1;
    private final static int SERIALIZER_JSON_CUSTOM = 2;

    private String xmlWrapperElementName = DEFAULT_XML_WRAPPER_ELEMENT_NAME;

    private int retType = RET_TYPE_RAW;
    private int proxyType = PROXY_TYPE_JDK;
    private int xmlDeserializer = DESERIALIZER_XML_JAXB;
    private int jsonDeserializer = DESERIALIZER_JSON_JACKSON;
    private int xmlSerializer = SERIALIZER_XML_JAXB;
    private int jsonSerializer = SERIALIZER_JSON_JACKSON;

    private Deserializer customXmlDeserializer;
    private Deserializer customJsonDeserializer;
    private Serializer customXmlSerializer;
    private Serializer customJsonSerializer;
    private final DeserializerRegistry.Builder deserializerBuilder = new DeserializerRegistry.Builder();
    private final SerializerRegistry.Builder serializerBuilder = new SerializerRegistry.Builder();
    private final Map<String, Object> xmlDeserializerConfig = new HashMap<String, Object>();
    private final Map<String, Object> jsonDeserializerConfig = new HashMap<String, Object>();
    private final Map<String, Object> xmlSerializerConfig = new HashMap<String, Object>();
    private final Map<String, Object> jsonSerializerConfig = new HashMap<String, Object>();
    private final Set<String> plainTextMimes = new HashSet<String>(asList(DEFAULT_PLAINTEXT_MIMETYPES));
    private final Set<String> xmlMimes = new HashSet<String>(asList(DEFAULT_XML_MIMETYPES));
    private final Set<String> jsonMimes = new HashSet<String>(asList(DEFAULT_JSON_MIMETYPES));

    private InterfaceConfigFactory overridesFactory = null;

    private Map<String, Object> customProperties = new HashMap<String, Object>();
    private Map<String, String> placeholders = new HashMap<String, String>();
    private Map<Type, Serializer> serializersMap = new HashMap<Type, Serializer>();

    private HttpChannelInitiator httpChannelInitiator;

    private boolean useHttpClient = false;

    public CRest build() {
        CRestContext context = buildContext();
        return new DefaultCRest(context);
    }

    CRestContext buildContext() {
        customProperties = Maps.defaultsIfNull(customProperties);

        ProxyFactory proxyFactory = buildProxyFactory();
        Maps.putIfNotPresent(customProperties, ProxyFactory.class.getName(), proxyFactory);

        DeserializerRegistry deserializerRegistry = buildDeserializerRegistry();
        DeserializationManager deserializationManager = new DeserializationManager(deserializerRegistry);
        Maps.putIfNotPresent(customProperties, DeserializerRegistry.class.getName(), deserializerRegistry);
        Maps.putIfNotPresent(customProperties, DeserializationManager.class.getName(), deserializationManager);

        SerializerRegistry serializerRegistry = buildSerializerRegistry();
        Maps.putIfNotPresent(customProperties, SerializerRegistry.class.getName(), serializerRegistry);

        HttpChannelInitiator httpChannelInitiator = buildHttpChannelInitiator();
        HttpRequestExecutor httpRequestExecutor = new DefaultHttpRequestExecutor(httpChannelInitiator);
        Maps.putIfNotPresent(customProperties, HttpRequestExecutor.class.getName(), httpRequestExecutor);

//        AuthentificationManager authentificationManager = buildAuthentificationManager(restService);
//        Maps.putIfNotPresent(customProperties, AuthentificationManager.class.getName(), authentificationManager);

        InterfaceConfigFactory configFactory = buildInterfaceConfigFactory();

//        if (authentificationManager != null) {
//            RequestInterceptor authentificationInterceptor = new AuthentificationInterceptor(authentificationManager);
//            try {
//                configFactory = new OverridingInterfaceConfigFactory(configFactory, new InterfaceConfigBuilder()
//                        .setGlobalInterceptor(authentificationInterceptor)
//                        .buildTemplate());
//            } catch (Exception e) {
//                throw new CRestException(e);
//            }
//        }
        Maps.putIfNotPresent(customProperties, InterfaceConfigFactory.class.getName(), configFactory);

        /* Put then in the properties. These are not part of the API */
        Maps.putIfNotPresent(customProperties, CRestProperty.SERIALIZER_CUSTOM_SERIALIZER_MAP, Maps.unmodifiable(serializersMap));
        Maps.putIfNotPresent(customProperties, CRestProperty.CONFIG_PLACEHOLDERS_MAP, Maps.unmodifiable(placeholders));
        Maps.putIfNotPresent(customProperties, CRestProperty.SERIALIZER_XML_WRAPPER_ELEMENT_NAME, xmlWrapperElementName);

        /* Defaults the deserializer for all methods */
        switch (retType) {
            case RET_TYPE_JSON:
                Maps.putIfNotPresent(customProperties, CRestProperty.CONFIG_METHOD_DEFAULT_DESERIALIZERS, deserializerRegistry.getForMimeType(DEFAULT_JSON_ACCEPT_HEADER));
                break;
            case RET_TYPE_XML:
                Maps.putIfNotPresent(customProperties, CRestProperty.CONFIG_METHOD_DEFAULT_DESERIALIZERS, deserializerRegistry.getForMimeType(DEFAULT_XML_ACCEPT_HEADER));
                break;
//            case RET_TYPE_CUSTOM:
//                Maps.putIfNotPresent(customProperties, CRestProperty.CONFIG_METHOD_DEFAULT_DESERIALIZERS, deserializerRegistry.getForMimeType(customMime));
//                break;
            case RET_TYPE_RAW:
                break;
        }

        return new DefaultCRestContext(httpRequestExecutor, proxyFactory, configFactory, customProperties);
    }

    private HttpChannelInitiator buildHttpChannelInitiator() {
        if (httpChannelInitiator == null) {
            if (useHttpClient) {
                return HttpClientHttpChannelInitiator.newHttpChannelInitiator(customProperties);
            } else {
                return new HttpURLConnectionHttpChannelInitiator();
            }
        } else {
            return httpChannelInitiator;
        }
    }

    private ProxyFactory buildProxyFactory() {
        switch (proxyType) {
            default:
            case PROXY_TYPE_JDK:
                return new JdkProxyFactory();
            case PROXY_TYPE_CGLIB:
                return new CglibProxyFactory();
        }
    }

    private DeserializerRegistry buildDeserializerRegistry() {
        Class<? extends Deserializer> jsonDeserializer = getJsonDeserializerClass();
        Class<? extends Deserializer> xmlDeserializer = getXmlDeserializerClass();

        if (jsonDeserializer != null) {
            Map<String,Object> config = copyProperties(customProperties, jsonDeserializerConfig, CREST_CONCURRENCY_LEVEL);
            deserializerBuilder.register(jsonDeserializer, jsonMimes.toArray(new String[jsonMimes.size()]), config);
        } else {
            deserializerBuilder.register(customJsonDeserializer, jsonMimes.toArray(new String[jsonMimes.size()]));
        }
        if (xmlDeserializer != null) {
            Map<String,Object> config = copyProperties(customProperties, xmlDeserializerConfig, CREST_CONCURRENCY_LEVEL);
            deserializerBuilder.register(xmlDeserializer, xmlMimes.toArray(new String[xmlMimes.size()]), config);
        } else {
            deserializerBuilder.register(customXmlDeserializer, xmlMimes.toArray(new String[xmlMimes.size()]));
        }

        deserializerBuilder.register(PlainTextDeserializer.class, plainTextMimes.toArray(new String[plainTextMimes.size()]));

        return deserializerBuilder.build(customProperties);
    }

    private SerializerRegistry buildSerializerRegistry() {
        Class<? extends Serializer> jsonSerializer = getJsonSerializerClass();
        Class<? extends Serializer> xmlSerializer = getXmlSerializerClass();

        if (jsonSerializer != null) {
            Map<String,Object> config = copyProperties(customProperties, jsonSerializerConfig, CREST_CONCURRENCY_LEVEL);
            serializerBuilder.register(jsonSerializer, jsonMimes.toArray(new String[jsonMimes.size()]), config);
        } else {
            serializerBuilder.register(customJsonSerializer, jsonMimes.toArray(new String[jsonMimes.size()]));
        }
        if (xmlSerializer != null) {
            Map<String,Object> config = copyProperties(customProperties, xmlSerializerConfig, CREST_CONCURRENCY_LEVEL);
            serializerBuilder.register(xmlSerializer, xmlMimes.toArray(new String[xmlMimes.size()]), config);
        } else {
            serializerBuilder.register(customXmlSerializer, xmlMimes.toArray(new String[xmlMimes.size()]));
        }

        // TODO make it configurable
        serializerBuilder.register(getXmlEncodedFormSerializerClass(), FORM_XML_ENCODED_MIME_TYPES);

        return serializerBuilder.build(customProperties);
    }

    private static Map<String, Object> copyProperties(Map<String, Object> src, Map<String, Object> dest, String... props) {
        dest = new HashMap<String, Object>(dest);
        for (String prop : props) {
            if (!src.containsKey(prop)) continue;
            dest.put(prop, src.get(prop));
        }
        return dest;
    }

    private Class<? extends Deserializer> getXmlDeserializerClass() {
        switch (this.xmlDeserializer) {
            case DESERIALIZER_XML_JAXB:
                return JaxbDeserializer.class;
            case DESERIALIZER_XML_SIMPLEXML:
                return SimpleXmlDeserializer.class;
            default:
                return null;
        }
    }

    private Class<? extends Deserializer> getJsonDeserializerClass() {
        switch (this.jsonDeserializer) {
            case DESERIALIZER_JSON_JACKSON:
                return JacksonDeserializer.class;
            default:
                return null;
        }
    }

    private Class<? extends Serializer> getXmlSerializerClass() {
        switch (this.xmlSerializer) {
            case SERIALIZER_XML_JAXB:
                return JaxbSerializer.class;
            case SERIALIZER_XML_SIMPLEXML:
                return SimpleXmlSerializer.class;
            default:
                return null;
        }
    }
    private Class<? extends Serializer> getXmlEncodedFormSerializerClass() {
        switch (this.xmlSerializer) {
            case SERIALIZER_XML_JAXB:
                return XmlEncodedFormJaxbSerializer.class;
            case SERIALIZER_XML_SIMPLEXML:
                return XmlEncodedFormSimpleXmlSerializer.class;
            default:
                return null;
        }
    }

    private Class<? extends Serializer> getJsonSerializerClass() {
        switch (this.jsonSerializer) {
            case SERIALIZER_JSON_JACKSON:
                return JacksonSerializer.class;
            default:
                return null;
        }
    }

    private InterfaceConfigFactory buildInterfaceConfigFactory() {
        InterfaceConfigFactory configFactory = new CRestAnnotationDrivenInterfaceConfigFactory();
//        switch (configType) {
//            default:
//            case CFG_TYPE_ANNO:
//                if (properties != null) {
//                    configFactory = new OverridingInterfaceConfigFactory(
//                            baseConfigFactory,
//                            new PropertiesDrivenInterfaceConfigFactory(properties, true));
//                } else if (document != null) {
//                    configFactory = new OverridingInterfaceConfigFactory(
//                            baseConfigFactory,
//                            new XmlDrivenInterfaceConfigFactory(document, true));
//                } else {
//                    configFactory = baseConfigFactory;
//                }
//                break;
//            case CFG_TYPE_PROP:
//                configFactory = new PropertiesDrivenInterfaceConfigFactory(properties);
//                break;
//            case CFG_TYPE_XML:
//                configFactory = new XmlDrivenInterfaceConfigFactory(document);
//                break;
//        }
        if (overridesFactory != null) {
            configFactory = new OverridingInterfaceConfigFactory(configFactory, overridesFactory);
        }
        return configFactory;
    }


//    private AuthentificationManager buildAuthentificationManager(RestService restService) {
//        String consumerKey = (String) customProperties.get(OAUTH_CONSUMER_KEY);
//        String consumerSecret = (String) customProperties.get(OAUTH_CONSUMER_SECRET);
//        String accessTok = (String) customProperties.get(OAUTH_ACCESS_TOKEN);
//        String accessTokenSecret = (String) customProperties.get(OAUTH_ACCESS_TOKEN_SECRET);
//        Map<String, String> accessTokenExtras = (Map<String, String>) customProperties.get(OAUTH_ACCESS_TOKEN_EXTRAS);
//
//        if (Strings.isBlank(consumerKey)
//                || Strings.isBlank(consumerSecret)
//                || Strings.isBlank(accessTok)
//                || Strings.isBlank(accessTokenSecret)) return null;
//
//        Token consumerToken = new Token(consumerKey, consumerSecret);
//        OAuthenticator authenticator = new OAuthenticatorV10(restService, consumerToken, customProperties);
//        Token accessToken = new Token(accessTok, accessTokenSecret, accessTokenExtras);
//
//        return new OAuthentificationManager(authenticator, accessToken);
//    }

    public CRestBuilder useHttpClientRestService() {
        this.useHttpClient = true;
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
     * Sets a custom property every services build with the resulting CRest instance will be passed.
     *
     * @param name  property key
     * @param value property value
     * @return current builder
     */
    public CRestBuilder setProperty(String name, Object value) {
        customProperties.put(name, value);
        return this;
    }

    /**
     * Sets a custom serializer for the given type the resulting CRest instance will use to serialize method arguments.
     * <p>The given type reflects the given Interface type, polymorphism is not considered.
     *
     * @param type       Type to seralize
     * @param serializer Serializer
     * @return current builder
     */
    public CRestBuilder setSerializer(Type type, Serializer serializer) {
        serializersMap.put(type, serializer);
        return this;
    }

    /**
     * Adds all custom properties every services build with the resulting CRest instance will be passed.
     *
     * @param customProperties properties map
     * @return current builder
     */
    public CRestBuilder addProperties(Map<String, Object> customProperties) {
        this.customProperties.putAll(customProperties);
        return this;
    }

    /**
     * Sets a custom properties every services build with the resulting CRest instance will be passed.
     *
     * @param customProperties properties map
     * @return current builder
     */
    public CRestBuilder setProperties(Map<String, Object> customProperties) {
        this.customProperties = customProperties;
        return this;
    }
//
//    /**
//     * Resulting CRest instance will handle annotated configurated interfaces.
//     *
//     * @return current builder
//     * @see org.codegist.crest.config.CRestAnnotationDrivenInterfaceConfigFactory
//     */
//    public CRestBuilder withAnnotatedConfig() {
//        this.configType = CFG_TYPE_ANNO;
//        this.properties = null;
//        return this;
//    }
//
//    /**
//     * Resulting CRest instance will handle properties based configuration.
//     * <p>Given properties must be able to configure any possible interface given to the resulting CRest instance.
//     *
//     * @param props configuration properties
//     * @return current builder
//     */
//    public CRestBuilder withPropertiesConfig(Map<String, String> props) {
//        this.configType = CFG_TYPE_PROP;
//        this.properties = props;
//        return this;
//    }
//
//    /**
//     * Resulting CRest instance will handle xml based configuration.
//     * <p>Given xml must be able to configure any possible interface given to the resulting CRest instance.
//     *
//     * @param document xml configuration document
//     * @return current builder
//     */
//    public CRestBuilder withXmlConfig(Document document) {
//        this.configType = CFG_TYPE_XML;
//        this.document = document;
//        return this;
//    }

//    /**
//     * Resulting CRest instance will overrides any configuration resulting from its current {@link org.codegist.crest.config.InterfaceConfigFactory} with the given properties.
//     * <p>Properties must be formatted as documentated in {@link org.codegist.crest.config.PropertiesDrivenInterfaceConfigFactory}
//     * <p>Can be used for instance to override the server end-point for differents devs environment.
//     *
//     * @param props properties
//     * @return current builder
//     * @see org.codegist.crest.config.PropertiesDrivenInterfaceConfigFactory
//     */
//    public CRestBuilder overrideDefaultConfigWith(Map<String, String> props) {
//        this.properties = props;
//        return this;
//    }
//
//    /**
//     * Resulting CRest instance will overrides any configuration resulting from its current {@link org.codegist.crest.config.InterfaceConfigFactory} with the given xml configuration.
//     * <p>Document must be formatted as documentated in {@link org.codegist.crest.config.XmlDrivenInterfaceConfigFactory}
//     * <p>Can be used for instance to override the server end-point for differents devs environment.
//     *
//     * @param document xml configuration
//     * @return current builder
//     * @see org.codegist.crest.config.XmlDrivenInterfaceConfigFactory
//     */
//    public CRestBuilder overrideDefaultConfigWith(Document document) {
//        this.document = document;
//        return this;
//    }

    /**
     * Resulting CRest instance will overrides any configuration resulting from its internal {@link org.codegist.crest.config.InterfaceConfigFactory} with the configuration issued by the given overridesFactory.
     * <p>This factory is meant to returns template configs, thus can return configuration with null values that will be interpreted as fallbacking to the current  {@link org.codegist.crest.config.InterfaceConfigFactory}.
     *
     * @param overridesFactory config overrider factory
     * @return current builder
     */
    public CRestBuilder overrideDefaultConfigWith(InterfaceConfigFactory overridesFactory) {
        this.overridesFactory = overridesFactory;
        return this;
    }


    /**
     * Resulting CRest instance will use native jdk proxies to build interface instances.
     *
     * @return current builder
     * @see org.codegist.common.reflect.JdkProxyFactory
     */
    public CRestBuilder useJdkProxies() {
        this.proxyType = PROXY_TYPE_JDK;
        return this;
    }

    /**
     * Resulting CRest instance will use cglib proxies to build interface instances. (requires cglib available in the classpath)
     *
     * @return current builder
     * @see org.codegist.common.reflect.CglibProxyFactory
     */
    public CRestBuilder useCglibProxies() {
        this.proxyType = PROXY_TYPE_CGLIB;
        return this;
    }

    /**
     * <p>Authentification parameters are added to the request headers.
     * <p>See  {@link CRestBuilder#usePreauthentifiedOAuth(String, String, String, String, boolean)}
     *
     * @param consumerKey       Consumer key
     * @param consumerSecret    Consumer secret
     * @param accessToken       Preauthentified access token
     * @param accessTokenSecret Preauthentified access token secret
     * @return current builder
     * @see CRestBuilder#usePreauthentifiedOAuth(String, String, String, String, boolean)
     */
    public CRestBuilder usePreauthentifiedOAuth(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret) {
        return usePreauthentifiedOAuth(consumerKey, consumerSecret, accessToken, accessTokenSecret, true);
    }

    /**
     * Resulting CRest instance will authentify every requests using OAuth (http://oauth.net/) authentification mechanism, using a pre-authentified access token and consumer information.
     *
     * @param consumerKey         Consumer key
     * @param consumerSecret      Consumer secret
     * @param accessToken         Preauthentified access token
     * @param accessTokenSecret   Preauthentified access token secret
     * @param authParamsInHeaders If true, adds the authentification information into the request headers, otherwise in the query string
     * @return current builder
     */
    public CRestBuilder usePreauthentifiedOAuth(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret, boolean authParamsInHeaders) {
        this.customProperties = Maps.defaultsIfNull(customProperties);
        setProperty(OAUTH_CONSUMER_KEY, consumerKey);
        setProperty(OAUTH_CONSUMER_SECRET, consumerSecret);
        setProperty(OAUTH_ACCESS_TOKEN, accessToken);
        setProperty(OAUTH_ACCESS_TOKEN_SECRET, accessTokenSecret);
        setProperty(OAUTH_PARAM_DEST, authParamsInHeaders ? "header" : "url");
        return this;
    }

    /**
     * Sets date serializer format to the given format.
     * <p>Shortcut to builder.setProperty(CRestProperty.SERIALIZER_DATE_FORMAT, format)
     *
     * @param format Date format to use
     * @return current builder
     * @see CRestProperty#SERIALIZER_DATE_FORMAT
     */
    public CRestBuilder setDateSerializerFormat(String format) {
        return setProperty(SERIALIZER_DATE_FORMAT, format);
    }

    /**
     * Sets how boolean should be serialized.
     * <p>Shortcut to:
     * <p>builder.setProperty(CRestProperty.SERIALIZER_BOOLEAN_TRUE, trueSerialized)
     * <p>builder.setProperty(CRestProperty.SERIALIZER_BOOLEAN_FALSE, falseSerialized)
     *
     * @param trueSerialized  String representing serialized form of TRUE
     * @param falseSerialized String representing serialized form of FALSE
     * @return current builder
     * @see CRestProperty#SERIALIZER_BOOLEAN_TRUE
     * @see CRestProperty#SERIALIZER_BOOLEAN_FALSE
     */
    public CRestBuilder setBooleanSerializer(String trueSerialized, String falseSerialized) {
        return setProperty(SERIALIZER_BOOLEAN_TRUE, trueSerialized)
                .setProperty(SERIALIZER_BOOLEAN_FALSE, falseSerialized);
    }


    /**
     * Sets a placeholder key/value that will be used to replace interface config eg:
     * <p>Calling
     * <pre><code>
     *      new CRestBuilder()
     *          .setConfigPlaceholder("my.server", "127.0.0.1")
     *          .setConfigPlaceholder("my.port", "8080");
     * </code></pre>
     * <p>will replace any place holder found in any interface location, eg:
     * <code>@EndPoint("http://{my.server}:{my.port}")</code>
     * <br>or
     * <p>for properties files: <code>service.test.end-point=http://{my.server}:{my.port}</code>
     *
     * @param placeholder Placeholder key
     * @param value       Placeholder value
     * @return current builder
     * @see CRestProperty#CONFIG_PLACEHOLDERS_MAP
     */
    public CRestBuilder setConfigPlaceholder(String placeholder, String value) {
        placeholders.put(placeholder, value);
        return this;
    }

    public CRestBuilder bindJsonDeserializerWith(String... mimeTypes) {
        this.jsonMimes.addAll(asList(mimeTypes));
        return this;
    }

    public CRestBuilder bindXmlDeserializerWith(String... mimeTypes) {
        this.xmlMimes.addAll(asList(mimeTypes));
        return this;
    }

    public CRestBuilder bindPlainTextDeserializerWith(String... mimeTypes) {
        this.plainTextMimes.addAll(asList(mimeTypes));
        return this;
    }

    public CRestBuilder bindDeserializer(Deserializer deserializer, String... mimeTypes) {
        this.deserializerBuilder.register(deserializer, mimeTypes);
        return this;
    }

    public CRestBuilder deserializeXmlWith(Deserializer deserializer) {
        this.xmlDeserializer = DESERIALIZER_XML_CUSTOM;
        this.customXmlDeserializer = deserializer;
        return this;
    }

    public CRestBuilder deserializeXmlWithJaxb() {
        this.xmlDeserializer = DESERIALIZER_XML_JAXB;
        this.xmlDeserializerConfig.clear();
        return this;
    }

    public CRestBuilder deserializeXmlWithJaxb(Map<String, Object> jaxbConfig) {
        deserializeXmlWithJaxb();
        this.xmlDeserializerConfig.clear();
        this.xmlDeserializerConfig.putAll(jaxbConfig);
        return this;
    }

    public CRestBuilder deserializeXmlWithSimpleXml() {
        this.xmlDeserializer = DESERIALIZER_XML_SIMPLEXML;
        this.xmlDeserializerConfig.clear();
        return this;
    }

    public CRestBuilder deserializeXmlWithSimpleXml(String dateFormat) {
        deserializeXmlWithSimpleXml();
        this.xmlDeserializerConfig.put(SimpleXmlDeserializer.DATE_FORMAT_PROP, dateFormat);
        return this;
    }

    public CRestBuilder deserializeXmlWithSimpleXml(String trueVal, String falseVal) {
        deserializeXmlWithSimpleXml();
        this.xmlDeserializerConfig.put(SimpleXmlDeserializer.BOOLEAN_FORMAT_PROP, trueVal + ":" + falseVal);
        return this;
    }

    public CRestBuilder deserializeXmlWithSimpleXml(String dateFormat, String trueVal, String falseVal, boolean strict) {
        deserializeXmlWithSimpleXml();
        this.xmlDeserializerConfig.put(SimpleXmlDeserializer.BOOLEAN_FORMAT_PROP, trueVal + ":" + falseVal);
        this.xmlDeserializerConfig.put(SimpleXmlDeserializer.DATE_FORMAT_PROP, dateFormat);
        this.xmlDeserializerConfig.put(SimpleXmlDeserializer.STRICT_PROP, strict);
        return this;
    }

    public CRestBuilder deserializeXmlWithSimpleXml(Map<String, Object> config) {
        deserializeXmlWithSimpleXml();
        this.xmlDeserializerConfig.clear();
        this.xmlDeserializerConfig.putAll(config);
        return this;
    }

    public CRestBuilder deserializerJsonWith(Deserializer deserializer) {
        this.jsonDeserializer = DESERIALIZER_JSON_CUSTOM;
        this.customJsonDeserializer = deserializer;
        return this;
    }

    public CRestBuilder deserializerJsonWithJackson() {
        this.jsonDeserializer = DESERIALIZER_JSON_JACKSON;
        this.jsonDeserializerConfig.clear();
        return this;
    }

    public CRestBuilder deserializerJsonWithJackson(Map<String, Object> config) {
        deserializerJsonWithJackson();
        this.jsonDeserializerConfig.clear();
        this.jsonDeserializerConfig.putAll(config);
        return this;
    }


    public CRestBuilder serializeXmlWith(Serializer serializer) {
        this.xmlSerializer = SERIALIZER_XML_CUSTOM;
        this.customXmlSerializer = serializer;
        return this;
    }

    public CRestBuilder serializeXmlWithJaxb() {
        this.xmlSerializer = SERIALIZER_XML_JAXB;
        this.xmlSerializerConfig.clear();
        return this;
    }

    public CRestBuilder serializeXmlWithJaxb(Map<String, Object> jaxbConfig) {
        serializeXmlWithJaxb();
        this.xmlSerializerConfig.clear();
        this.xmlSerializerConfig.putAll(jaxbConfig);
        return this;
    }

    public CRestBuilder serializeXmlWithSimpleXml() {
        this.xmlSerializer = SERIALIZER_XML_SIMPLEXML;
        this.xmlSerializerConfig.clear();
        return this;
    }

    public CRestBuilder serializeXmlWithSimpleXml(String dateFormat) {
        serializeXmlWithSimpleXml();
        this.xmlSerializerConfig.put(SimpleXmlSerializer.DATE_FORMAT_PROP, dateFormat);
        return this;
    }

    public CRestBuilder serializeXmlWithSimpleXml(String trueVal, String falseVal) {
        serializeXmlWithSimpleXml();
        this.xmlSerializerConfig.put(SimpleXmlSerializer.BOOLEAN_FORMAT_PROP, trueVal + ":" + falseVal);
        return this;
    }

    public CRestBuilder serializeXmlWithSimpleXml(String dateFormat, String trueVal, String falseVal) {
        serializeXmlWithSimpleXml();
        this.xmlSerializerConfig.put(SimpleXmlSerializer.BOOLEAN_FORMAT_PROP, trueVal + ":" + falseVal);
        this.xmlSerializerConfig.put(SimpleXmlSerializer.DATE_FORMAT_PROP, dateFormat);
        return this;
    }

    public CRestBuilder serializeXmlWithSimpleXml(Map<String, Object> config) {
        serializeXmlWithSimpleXml();
        this.xmlSerializerConfig.clear();
        this.xmlSerializerConfig.putAll(config);
        return this;
    }


    public CRestBuilder serializerJsonWith(Serializer serializer) {
        this.jsonSerializer = SERIALIZER_JSON_CUSTOM;
        this.customJsonSerializer = serializer;
        return this;
    }

    public CRestBuilder serializerJsonWithJackson() {
        this.jsonSerializer = SERIALIZER_JSON_JACKSON;
        this.jsonSerializerConfig.clear();
        return this;
    }

    public CRestBuilder serializerJsonWithJackson(Map<String, Object> config) {
        serializerJsonWithJackson();
        this.jsonSerializerConfig.clear();
        this.jsonSerializerConfig.putAll(config);
        return this;
    }
}