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

public interface OAuthInterface {

    OAuthToken getAccessToken(
            String oauth_token,
            String oauth_consumer_key,
            String oauth_signature_method,
            String oauth_timestamp,
            String oauth_nonce,
            String oauth_version,
            String oauth_verifier,
            String oauth_signature
    );

    OAuthToken getRequestToken(
            String oauth_consumer_key,
            String oauth_signature_method,
            String oauth_timestamp,
            String oauth_nonce,
            String oauth_version,
            String oauth_callback,
            String oauth_signature
    );

    OAuthToken refreshAccessToken(
            String oauth_token,
            String oauth_consumer_key,
            String oauth_signature_method,
            String oauth_timestamp,
            String oauth_nonce,
            String oauth_version,
            String oauth_session_handle,
            String oauth_signature
    );
}