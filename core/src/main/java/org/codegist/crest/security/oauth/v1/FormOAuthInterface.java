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

@POST
@Encoded
@Deserializer(OAuthTokenDeserializer.class)
interface FormOAuthInterface extends OAuthInterface {

    @Path("{oauth.access-token-path}")
    OAuthToken getAccessToken(
            @FormParam("oauth_consumer_key") String oauthConsumerKey,
            @FormParam("oauth_signature_method") String oauthSignatureMethod,
            @FormParam("oauth_timestamp") String oauthTimestamp,
            @FormParam("oauth_nonce") String oauthNonce,
            @FormParam("oauth_version") String oauthVersion,
            @FormParam("oauth_verifier") String oauthVerifier,
            @FormParam("oauth_signature") String oauthSignature
    );

    @Path("{oauth.request-token-path}")
    OAuthToken getRequestToken(
            @FormParam("oauth_consumer_key") String oauthConsumerKey,
            @FormParam("oauth_signature_method") String oauthSignatureMethod,
            @FormParam("oauth_timestamp") String oauthTimestamp,
            @FormParam("oauth_nonce") String oauthNonce,
            @FormParam("oauth_version") String oauthVersion,
            @FormParam("oauth_callback") String oauthCallback,
            @FormParam("oauth_signature") String oauthSignature
    );

    @Path("{oauth.refresh-access-token-path}")
    OAuthToken refreshAccessToken(
            @FormParam("oauth_token") String oauthToken,
            @FormParam("oauth_consumer_key") String oauthConsumerKey,
            @FormParam("oauth_signature_method") String oauthSignatureMethod,
            @FormParam("oauth_timestamp") String oauthTimestamp,
            @FormParam("oauth_nonce") String oauthNonce,
            @FormParam("oauth_version") String oauthVersion,
            @FormParam("oauth_session_handle") String oauthSessionHandle,
            @FormParam("oauth_signature") String oauthSignature
    );
}
