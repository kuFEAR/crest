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

package org.codegist.crest.security.oauth;

import org.codegist.crest.io.http.HttpMethod;
import org.codegist.crest.io.http.Pair;

import java.util.List;


/**
 * OAuth authentificator interface
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public interface OAuthenticator {

    /**
     * Signs the given io using the given access token and the optional additional oauth headers.
     * @param accessOAuthToken Access token to be used
     */
    List<Pair> oauth(OAuthToken accessOAuthToken, HttpMethod method, String url, Pair... parameters) throws Exception;

    /**
     * Fires a get io token to the preconfigured url
     * @return A new io token
     */
    OAuthToken getRequestToken() throws Exception;

    /**
     * Exchanges the given io token with a new access token using the given verifier
     * @param requestOAuthToken io token to exchange
     * @param verifier verifier
     * @return new access token
     */
    OAuthToken getAccessToken(OAuthToken requestOAuthToken, String verifier) throws Exception;

    /**
     * Refreshs the given access token if it has expired. Include optional extra oauth header from the extra field of the token.
     * @param accessOAuthToken expired access token
     * @param extrasOAuthParams extras oauth paramst
     * @see OAuthToken#getAttributes()
     * @return a new access token
     */
    OAuthToken refreshAccessToken(OAuthToken accessOAuthToken, Pair... extrasOAuthParams) throws Exception;

}
