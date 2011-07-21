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
import org.codegist.crest.io.http.platform.HttpURLConnectionHttpChannelFactory;
import org.codegist.crest.security.oauth.OAuthToken;

import static org.codegist.common.lang.Strings.defaultIfEmpty;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public final class OAuthApiV1Builder {

    private OAuthApiV1Builder(){
        throw new IllegalStateException();
    }

    public static OAuthApiV1 build(OAuthToken consumerToken, MethodType methodType, String requestTokenUrl, String accessTokenUrl, String refreshAccessTokenUrl) throws Exception {
        HttpChannelFactory channelFactory = new HttpURLConnectionHttpChannelFactory();
        OAuthenticatorV1 oAuthenticatorV1 = new OAuthenticatorV1(consumerToken);
        return build(channelFactory, oAuthenticatorV1, methodType, requestTokenUrl, accessTokenUrl, refreshAccessTokenUrl);
    }

    public static OAuthApiV1 build(
            HttpChannelFactory channelFactory,
            OAuthenticatorV1 oAuthenticatorV1,
            MethodType methodType,
            String requestTokenUrl,
            String accessTokenUrl,
            String refreshAccessTokenUrl) throws Exception {

        OAuthInterface oauthInterface = CRest.placeholder("oauth.access-token-path", defaultIfEmpty(accessTokenUrl,""))
                                             .placeholder("oauth.request-token-path", defaultIfEmpty(requestTokenUrl,""))
                                             .placeholder("oauth.refresh-access-token-path", defaultIfEmpty(refreshAccessTokenUrl,""))
                                             .setHttpChannelFactory(channelFactory)
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
