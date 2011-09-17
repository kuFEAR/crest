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

import org.codegist.common.lang.State;
import org.codegist.crest.config.MethodType;
import org.codegist.crest.param.EncodedPair;
import org.codegist.crest.security.Authorization;
import org.codegist.crest.security.AuthorizationToken;

import java.util.List;

import static org.codegist.crest.util.Pairs.join;

/**
 * <p>OAuth implementation of the {@link org.codegist.crest.security.Authorization} interface.</p>
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class OAuthorization implements Authorization {

    private final OAuthenticator oauth;
    private final OAuthApi oauthApi;
    private volatile OAuthToken accessOAuthToken;

    /**
     * @param accessOAuthToken Access token to use to authentify requests
     * @param oauth oauthenticator instance
     */
    public OAuthorization(OAuthToken accessOAuthToken, OAuthenticator oauth) {
        this(accessOAuthToken, oauth, null);        
    }

    /**
     *
     * @param accessOAuthToken Access token to use to authentify requests
     * @param oauth oauthenticator instance
     * @param oauthApi oauth api to use to refresh access token if it expires
     */
    public OAuthorization(OAuthToken accessOAuthToken, OAuthenticator oauth, OAuthApi oauthApi) {
        this.oauth = oauth;
        this.oauthApi = oauthApi;
        this.accessOAuthToken = accessOAuthToken;
    }

    /**
     * @inheritDoc
     */
    public AuthorizationToken authorize(MethodType methodType, String url, EncodedPair... parameters)  throws Exception{
        List<EncodedPair> oauthParams = oauth.oauth(this.accessOAuthToken, methodType, url, parameters);
        return new AuthorizationToken("OAuth", join(oauthParams, ',', '=', false, true));
    }

    public void refresh()  throws Exception{
        State.notNull(oauthApi, "AccessToken refresh impossible, you must specify the oauth_session_handle and refresh access token url, see CRestBuilder.");
        this.accessOAuthToken = oauthApi.refreshAccessToken(this.accessOAuthToken);
    }

}
