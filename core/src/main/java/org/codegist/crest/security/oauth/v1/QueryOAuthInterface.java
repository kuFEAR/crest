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

import org.codegist.crest.annotate.*;
import org.codegist.crest.security.oauth.OAuthToken;

@GET
@Encoded
@EndPoint("{oauth.end-point}")
@Deserializer(OAuthTokenDeserializer.class)
interface QueryOAuthInterface extends OAuthInterface {

    @Path("{oauth.access-token.path}")
    OAuthToken getAccessToken(
            @QueryParam("oauth_consumer_key") String oauthConsumerKey,
            @QueryParam("oauth_signature_method") String oauthSignatureMethod,
            @QueryParam("oauth_timestamp") String oauthTimestamp,
            @QueryParam("oauth_nonce") String oauthNonce,
            @QueryParam("oauth_version") String oauthVersion,
            @QueryParam("oauth_verifier") String oauthVerifier,
            @QueryParam("oauth_signature") String oauthSignature
    );

    @Path("{oauth.request-token.path}")
    OAuthToken getRequestToken(
            @QueryParam("oauth_consumer_key") String oauthConsumerKey,
            @QueryParam("oauth_signature_method") String oauthSignatureMethod,
            @QueryParam("oauth_timestamp") String oauthTimestamp,
            @QueryParam("oauth_nonce") String oauthNonce,
            @QueryParam("oauth_version") String oauthVersion,
            @QueryParam("oauth_callback") String oauthCallback,
            @QueryParam("oauth_signature") String oauthSignature
    );

    @Path("{oauth.refresh-access-token.path}")
    OAuthToken refreshAccessToken(
            @QueryParam("oauth_token") String oauthToken,
            @QueryParam("oauth_consumer_key") String oauthConsumerKey,
            @QueryParam("oauth_signature_method") String oauthSignatureMethod,
            @QueryParam("oauth_timestamp") String oauthTimestamp,
            @QueryParam("oauth_nonce") String oauthNonce,
            @QueryParam("oauth_version") String oauthVersion,
            @QueryParam("oauth_session_handle") String oauthSessionHandle,
            @QueryParam("oauth_signature") String oauthSignature
    );
}
