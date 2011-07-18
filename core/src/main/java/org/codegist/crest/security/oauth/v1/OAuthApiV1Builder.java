/*
 * Copyright 2010 CodeGist.org
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 *
 *  ===================================================================
 *
 *  More information at http://www.codegist.org.
 */

package org.codegist.crest.security.oauth.v1;

import org.codegist.common.reflect.JdkProxyFactory;
import org.codegist.crest.CRest;
import org.codegist.crest.DefaultCRest;
import org.codegist.crest.config.*;
import org.codegist.crest.config.annotate.AnnotationHandler;
import org.codegist.crest.config.annotate.CRestAnnotations;
import org.codegist.crest.config.annotate.NoOpAnnotationHandler;
import org.codegist.crest.io.RequestExecutor;
import org.codegist.crest.io.http.HttpChannelInitiator;
import org.codegist.crest.io.http.HttpRequestBuilderFactory;
import org.codegist.crest.io.http.HttpRequestExecutor;
import org.codegist.crest.security.oauth.OAuthToken;
import org.codegist.crest.serializer.DeserializationManager;
import org.codegist.crest.serializer.Deserializer;
import org.codegist.crest.serializer.ToStringSerializer;
import org.codegist.crest.util.Registry;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import static org.codegist.crest.CRestProperty.*;
import static org.codegist.crest.config.MethodType.POST;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public final class OAuthApiV1Builder {

    public static final String CONFIG_OAUTH_METHOD = OAuthApiV1.class.getName() + "#oauth.method";
    public static final String CONFIG_TOKEN_ACCESS_REFRESH_URL = OAuthApiV1.class.getName() + "#access.refresh.url";
    public static final String CONFIG_TOKEN_REQUEST_URL = OAuthApiV1.class.getName() + "#request.url";
    public static final String CONFIG_TOKEN_ACCESS_URL = OAuthApiV1.class.getName() + "#access.url";

    private OAuthApiV1Builder(){
        throw new IllegalStateException();
    }

    public static OAuthApiV1 build(HttpChannelInitiator channelInitiator, Map<String, Object> crestProperties, OAuthenticatorV1 oAuthenticatorV1) throws Exception {
        MethodType methodType = MethodType.valueOf(get(crestProperties, CONFIG_OAUTH_METHOD, POST.name()));
        String requestTokenUrl = get(crestProperties, CONFIG_TOKEN_REQUEST_URL, "");
        String accessTokenUrl = get(crestProperties, CONFIG_TOKEN_ACCESS_URL, "");
        String refreshAccessTokenUrl = get(crestProperties, CONFIG_TOKEN_ACCESS_REFRESH_URL, "");

        HashMap<String,String> pl = new HashMap<String, String>();
        pl.put("oauth.access-token-path", accessTokenUrl);
        pl.put("oauth.request-token-path", requestTokenUrl);
        pl.put("oauth.refresh-access-token-path", refreshAccessTokenUrl);

        Map<String,Object> props = new HashMap<String, Object>();
        props.put(CREST_ANNOTATION_PLACEHOLDERS, pl);
        props.put(PARAM_CONFIG_DEFAULT_SERIALIZER, new ToStringSerializer());

        Registry<Class<? extends Annotation>, AnnotationHandler> handlers = new Registry.Builder<Class<? extends Annotation>, AnnotationHandler>(crestProperties, AnnotationHandler.class)
                            .defaultAs(new NoOpAnnotationHandler())
                            .register(CRestAnnotations.MAPPING).build();

        InterfaceConfigBuilderFactory icbf = new DefaultInterfaceConfigBuilderFactory(crestProperties);
        InterfaceConfigFactory configFactory = new AnnotationDrivenInterfaceConfigFactory(icbf, handlers, false);

        Class<? extends OAuthInterface> oauthInterfaceCls = methodType.equals(MethodType.POST) ? PostOAuthInterface.class : GetOAuthInterface.class;
        Registry<String, Deserializer> mimeDeserializerRegistry = new Registry.Builder<String, Deserializer>(props, Deserializer.class).build();
        Registry<Class<?>, Deserializer> classDeserializerRegistry = new Registry.Builder<Class<?>, Deserializer>(props, Deserializer.class).register(TokenDeserializer.class, OAuthToken.class).build();

        RequestExecutor requestExecutor = new HttpRequestExecutor(channelInitiator, new DeserializationManager(mimeDeserializerRegistry, classDeserializerRegistry));

        CRest crest = new DefaultCRest(new JdkProxyFactory(), requestExecutor, new HttpRequestBuilderFactory(), configFactory);
        OAuthInterface oauthInterface = crest.build(oauthInterfaceCls);

        return new OAuthApiV1(
                methodType.name(),
                requestTokenUrl,
                accessTokenUrl,
                refreshAccessTokenUrl,
                oauthInterface,
                oAuthenticatorV1);
    }
}
