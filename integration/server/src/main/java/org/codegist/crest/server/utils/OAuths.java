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

package org.codegist.crest.server.utils;

import com.sun.jersey.api.core.HttpRequestContext;
import com.sun.jersey.oauth.server.NonceManager;
import com.sun.jersey.oauth.server.OAuthException;
import com.sun.jersey.oauth.server.OAuthServerRequest;
import com.sun.jersey.oauth.signature.OAuthParameters;
import com.sun.jersey.oauth.signature.OAuthSecrets;
import com.sun.jersey.oauth.signature.OAuthSignature;
import com.sun.jersey.oauth.signature.OAuthSignatureException;

import javax.ws.rs.core.Response;

/**
 * @author laurent.gilles@codegist.org
 */
public class OAuths {

    public static final String OAUTH_CONSUMER_KEY = "ConsumerKey";
    public static final String OAUTH_CONSUMER_SECRET = "ConsumerSecret";
    public static final String OAUTH_SESSION_HANDLE = "SessionHandle";
    public static final String OAUTH_EXPIRED_ACCESS_TOKEN = "ExpiredAccessToken";
    public static final String OAUTH_EXPIRED_ACCESS_SECRET = "ExpiredAccessTokenSecret";
    public static final String OAUTH_ACCESS_TOKEN = "AccessToken";
    public static final String OAUTH_ACCESS_SECRET = "AccessTokenSecret";

    private static final long MAX_AGE = 60 * 60 * 1000; // one houre
    private static final NonceManager nonces = new NonceManager(MAX_AGE, 100);

    public static void validateRefresh(HttpRequestContext containerRequest){
        validate(containerRequest, OAUTH_CONSUMER_KEY, OAUTH_CONSUMER_SECRET, OAUTH_EXPIRED_ACCESS_TOKEN, OAUTH_EXPIRED_ACCESS_SECRET);
    }

    public static void validate(HttpRequestContext containerRequest){
        validate(containerRequest, OAUTH_CONSUMER_KEY, OAUTH_CONSUMER_SECRET, OAUTH_ACCESS_TOKEN, OAUTH_ACCESS_SECRET);
    }
    public static void validate(HttpRequestContext containerRequest,String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret){
         // Read the OAuth parameters from the io
        OAuthServerRequest request = new OAuthServerRequest(containerRequest);
        OAuthParameters params = new OAuthParameters();
        params.readRequest(request);
        String sessionHandle = containerRequest.getFormParameters().getFirst("oauth_session_handle");
        if(sessionHandle != null) {
            if(!OAUTH_SESSION_HANDLE.equals(sessionHandle)) {
                throw newUnauthorizedException();
            }else{
               params.put("oauth_session_handle", sessionHandle);
            }
        }

        // Set the secret(s), against which we will verify the io
        OAuthSecrets secrets = new OAuthSecrets();
        secrets.setConsumerSecret(consumerSecret);
        secrets.setTokenSecret(accessTokenSecret);
        // ... secret setting code ...

        // Check that the timestamp has not expired
        String timestampStr = params.getTimestamp();
        String nonceStr = params.getNonce();
        // ... timestamp checking code ...

        if(!(consumerKey.equals(params.getConsumerKey()) && accessToken.equals(params.getToken()))){
            throw newUnauthorizedException();
        }

        if (!nonces.verify(params.getToken(), timestampStr, nonceStr)) {
            throw newUnauthorizedException();
        }
        // Verify the signature
        try {
            if(!OAuthSignature.verify(request, params, secrets)) {
                throw newUnauthorizedException();
            }
        } catch (OAuthSignatureException e) {
            throw newBadRequestException();
        }
    }



    private static AuthException newBadRequestException() throws OAuthException {
        return new AuthException(Response.Status.BAD_REQUEST, null);
    }

    private static AuthException newUnauthorizedException() throws OAuthException {
        return new AuthException(Response.Status.UNAUTHORIZED, "OAuth realm=\"default\"");
    }
}
