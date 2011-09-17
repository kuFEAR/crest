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

package org.codegist.crest.security.oauth;

/**
 * OAuth token access API
 * @author laurent.gilles@codegist.org
 */
public interface OAuthApi {

    /**
     * Retrieves a request token
     * @return the request token
     * @throws Exception Any exception thrown during requests process
     */
    OAuthToken getRequestToken() throws Exception;

    /**
     * Exchange a request token with verifier for an access token
     * @param requestOAuthToken request token to exchange
     * @param verifier verifier issued by the OAuth server
     * @return the access token
     * @throws Exception Any exception thrown during the exchange process
     */
    OAuthToken getAccessToken(OAuthToken requestOAuthToken, String verifier) throws Exception;

    /**
     * <p>Refreshes the expired access token.</p>
     * @param accessToken expired access token
     * @return a new access token
     * @throws Exception Any exception thrown during the refresh process
     */
    OAuthToken refreshAccessToken(OAuthToken accessToken) throws Exception;
}
