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

package org.codegist.crest.security;

import org.codegist.common.lang.Objects;
import org.codegist.crest.HttpRequest;
import org.codegist.crest.oauth.OAuthenticator;
import org.codegist.crest.oauth.Param;
import org.codegist.crest.oauth.Token;

import java.util.Collections;
import java.util.Map;

/**
 * OAuth authentification manager implementation.
 * <p>Refresh implementation is based on oauth_session_handle token extra parameter
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class OAuthentificationManager implements AuthentificationManager {

    private final OAuthenticator oauth;
    private Token accessToken;

    public OAuthentificationManager(OAuthenticator oauth, Token accessToken) {
        this.oauth = oauth;
        this.accessToken = accessToken;
    }

    @Override
    public void sign(HttpRequest.Builder request) {
        sign(request, Collections.<String, Object>emptyMap());
    }

    @Override
    public void sign(HttpRequest.Builder request, Map<String, Object> properties) {
        Param[] params = new Param[properties.size()];
        int i = 0;
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            Object val = Objects.defaultIfNull(entry.getValue(), "");
            params[i++] = new Param(entry.getKey(), val.toString());
        }
        oauth.sign(accessToken, request, params);
    }

    @Override
    public void refresh() {
        accessToken = oauth.refreshAccessToken(accessToken, "oauth_session_handle");
    }
}
