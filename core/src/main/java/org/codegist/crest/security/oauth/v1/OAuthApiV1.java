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

import org.codegist.crest.param.EncodedPair;
import org.codegist.crest.security.oauth.OAuthApi;
import org.codegist.crest.security.oauth.OAuthToken;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.codegist.crest.security.oauth.v1.OAuthsV1.*;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
class OAuthApiV1 implements OAuthApi {

    private static final EncodedPair CALLBACK;
    private final String method;
    private final String requestTokenUrl;
    private final String accessTokenUrl;
    private final String refreshAccessTokenUrl;
    private final OAuthInterface oauthInterface;
    private final OAuthenticatorV1 oauth;

    public OAuthApiV1(String method, String requestTokenUrl, String accessTokenUrl, String refreshAccessTokenUrl, OAuthInterface oauthInterface, OAuthenticatorV1 oauth) {
        this.method = method;
        this.requestTokenUrl = requestTokenUrl;
        this.accessTokenUrl = accessTokenUrl;
        this.refreshAccessTokenUrl = refreshAccessTokenUrl;
        this.oauthInterface = oauthInterface;
        this.oauth = oauth;
    }

    public OAuthToken getRequestToken() throws Exception {
        Map<String,String> params = asMap(oauth.oauth(IGNORE_POISON, method, requestTokenUrl, EMPTY_HTTP_PAIRS, CALLBACK));
        return oauthInterface.getRequestToken(
                params.get("oauth_consumer_key"),
                params.get("oauth_signature_method"),
                params.get("oauth_timestamp"),
                params.get("oauth_nonce"),
                params.get("oauth_version"),
                params.get("oauth_callback"),
                params.get("oauth_signature")
        );
    }

    public OAuthToken getAccessToken(OAuthToken requestOAuthToken, String verifier) throws Exception {
        Map<String,String> params = asMap(oauth.oauth(requestOAuthToken, method, accessTokenUrl, EMPTY_HTTP_PAIRS,  pair("oauth_verifier", verifier)));
        return oauthInterface.getAccessToken(
                params.get("oauth_token"),
                params.get("oauth_consumer_key"),
                params.get("oauth_signature_method"),
                params.get("oauth_timestamp"),
                params.get("oauth_nonce"),
                params.get("oauth_version"),
                params.get("oauth_verifier"),
                params.get("oauth_signature")
        );
    }

    public OAuthToken refreshAccessToken(OAuthToken token) throws Exception {
        Map<String,String> params = asMap(oauth.oauth(token, method, refreshAccessTokenUrl, EMPTY_HTTP_PAIRS,  token.getAttribute("oauth_session_handle")));
        return oauthInterface.refreshAccessToken(
                params.get("oauth_token"),
                params.get("oauth_consumer_key"),
                params.get("oauth_signature_method"),
                params.get("oauth_timestamp"),
                params.get("oauth_nonce"),
                params.get("oauth_version"),
                params.get("oauth_session_handle"),
                params.get("oauth_signature")
        );
    }

    private Map<String, String> asMap(List<EncodedPair> pairs) {
        Map<String, String> map = new HashMap<String, String>();
        for(EncodedPair pair : pairs){
            map.put(pair.getName(), pair.getValue());
        }
        return map;
    }

    static{
        try {
            CALLBACK = pair("oauth_callback", "oob");
        } catch (UnsupportedEncodingException e) {
            throw new ExceptionInInitializerError(e);
        }
    }


}
