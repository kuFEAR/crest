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

import org.codegist.crest.io.http.apache.HttpClientHttpChannelFactory;
import org.codegist.crest.security.oauth.OAuthApi;
import org.codegist.crest.security.oauth.OAuthToken;
import org.codegist.crest.security.oauth.v1.OAuthApiV1Builder;
import org.codegist.crest.security.oauth.v1.OAuthenticatorV1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class OAuthHelper {

    public static void main(String[] args) throws Exception {
        // Flickr
//        OAuthHelper.doAccessTokenRetrievalWorkflow(
//        "",
//        "",
//        "http://www.flickr.com/services/oauth",
//        "request_token",
//        "access_token",
//        "http://www.flickr.com/services/oauth/authorize?oauth_token=%s");
        // Yahoo
        OAuthHelper.doAccessTokenRetrievalWorkflow(
        "dj0yJmk9aldLMDF5aVZHZ29uJmQ9WVdrOVJWTmtZbmg1TlRRbWNHbzlNVEkxTkRRMk9EazJNZy0tJnM9Y29uc3VtZXJzZWNyZXQmeD04Mw--",
        "57508d207cf3b6ca583d491dba9a8d33162da3cf",
        "https://api.login.yahoo.com/oauth/v2/get_request_token",
        "https://api.login.yahoo.com/oauth/v2/get_token",
        "https://api.login.yahoo.com/oauth/v2/get_token",
        "https://api.login.yahoo.com/oauth/v2/request_auth?oauth_token=%s");
//
        // Twitter
//        OAuthHelper.doAccessTokenRetrievalWorkflow(
//                "",
//                "",
//
//                "https://api.twitter.com/oauth/request_token",
//                "https://api.twitter.com/oauth/access_token",
//                "http://api.twitter.com/oauth/authorize?oauth_token=%s");
    }


    private static void doAccessTokenRetrievalWorkflow(String consumerTok, String consumerSecret,String requestUrl, String accessUrl, String refreshUrl, String redirect) throws Exception {
        OAuthToken consumerOAuthToken = new OAuthToken(consumerTok, consumerSecret);
        Map<String, Object> config = new HashMap<String, Object>();
        config.put(OAuthApiV1Builder.CONFIG_TOKEN_REQUEST_URL, requestUrl);
        config.put(OAuthApiV1Builder.CONFIG_TOKEN_ACCESS_URL, accessUrl);
        config.put(OAuthApiV1Builder.CONFIG_TOKEN_ACCESS_REFRESH_URL, refreshUrl);

        OAuthApi api = OAuthApiV1Builder.build(HttpClientHttpChannelFactory.newHttpChannelInitiator(), config, new OAuthenticatorV1(consumerOAuthToken));

        
        OAuthToken tok = api.getRequestToken();

        System.out.println("RequestToken=" + tok);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("goto " + String.format(redirect, tok.getToken()));
        System.out.println("Input verifier :");
        OAuthToken accessOAuthToken = api.getAccessToken(tok, br.readLine());
        System.out.println("Token  =" + accessOAuthToken);
        accessOAuthToken = api.refreshAccessToken(accessOAuthToken);
        System.out.println("Token  Refreshed=" + accessOAuthToken);
    }

}
