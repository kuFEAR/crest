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

import org.codegist.crest.security.oauth.OAuthToken;

interface OAuthInterface {

    OAuthToken getAccessToken(
            String oauthToken,
            String oauthConsumerKey,
            String oauthSignatureMethod,
            String oauthTimestamp,
            String oauthNonce,
            String oauthVersion,
            String oauthVerifier,
            String oauthSignature
    );

    OAuthToken getRequestToken(
            String oauthConsumerKey,
            String oauthSignatureMethod,
            String oauthTimestamp,
            String oauthNonce,
            String oauthVersion,
            String oauthCallback,
            String oauthSignature
    );

    OAuthToken refreshAccessToken(
            String oauthToken,
            String oauthConsumerKey,
            String oauthSignatureMethod,
            String oauthTimestamp,
            String oauthNonce,
            String oauthVersion,
            String oauthSessionHandle,
            String oauthSignature
    );
}