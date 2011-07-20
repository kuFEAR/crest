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

import org.codegist.crest.CRest;
import org.codegist.crest.config.MethodType;
import org.codegist.crest.io.http.HttpChannelFactory;
import org.codegist.crest.security.oauth.OAuthToken;
import org.codegist.crest.serializer.ToStringSerializer;

import java.util.Map;

import static org.codegist.crest.CRestProperty.PARAM_CONFIG_DEFAULT_SERIALIZER;
import static org.codegist.crest.CRestProperty.get;
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

    public static OAuthApiV1 build(HttpChannelFactory channelFactory, Map<String, Object> crestProperties, OAuthenticatorV1 oAuthenticatorV1) throws Exception {
        MethodType methodType = MethodType.valueOf(get(crestProperties, CONFIG_OAUTH_METHOD, POST.name()));
        String requestTokenUrl = get(crestProperties, CONFIG_TOKEN_REQUEST_URL, "");
        String accessTokenUrl = get(crestProperties, CONFIG_TOKEN_ACCESS_URL, "");
        String refreshAccessTokenUrl = get(crestProperties, CONFIG_TOKEN_ACCESS_REFRESH_URL, "");

        OAuthInterface oauthInterface = CRest.placeholder("oauth.access-token-path", accessTokenUrl)
                                             .placeholder("oauth.request-token-path", requestTokenUrl)
                                             .placeholder("oauth.refresh-access-token-path", refreshAccessTokenUrl)
                                             .bindDeserializer(TokenDeserializer.class, OAuthToken.class)
                                             .setHttpChannelFactory(channelFactory)
                                             .setProperty(PARAM_CONFIG_DEFAULT_SERIALIZER, new ToStringSerializer())
                                             .build(methodType.equals(MethodType.POST) ? PostOAuthInterface.class : GetOAuthInterface.class);

        return new OAuthApiV1(
                methodType.name(),
                requestTokenUrl,
                accessTokenUrl,
                refreshAccessTokenUrl,
                oauthInterface,
                oAuthenticatorV1);
    }
}
