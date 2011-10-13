/*
 * Copyright 2011 CodeGist.org
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

package org.codegist.crest;

import org.codegist.crest.security.oauth.OAuthApi;
import org.codegist.crest.security.oauth.OAuthToken;
import org.codegist.crest.security.oauth.v1.OAuthApiV1Builder;

import java.io.BufferedReader;
import java.io.InputStreamReader;

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
        "https://api.login.yahoo.com",
        "",
        "",
        "/oauth/v2/get_request_token",
        "/oauth/v2/get_token",
        "/oauth/v2/get_token",
        "/oauth/v2/request_auth?oauth_token=%s");
//
        // Twitter
//        OAuthHelper.doAccessTokenRetrievalWorkflow(
//                "https://api.twitter.com",
//                "",
//                "",
//
//                "/oauth/request_token",
//                "/oauth/access_token",
//                "/oauth/authorize?oauth_token=%s");
    }


    private static void doAccessTokenRetrievalWorkflow(String endpoint, String consumerTok, String consumerSecret,String requestUrl, String accessUrl, String refreshUrl, String redirect) throws Exception {

        OAuthApi api = new OAuthApiV1Builder(new OAuthToken(consumerTok, consumerSecret), endpoint)
                        .useGet()
                        .getRequestTokenFrom(requestUrl)
                        .getAccessTokenFrom(accessUrl)
                        .refreshAccessTokenFrom(refreshUrl)
                        .build();

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
