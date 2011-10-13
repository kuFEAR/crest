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

package org.codegist.crest.security.oauth;

import org.codegist.crest.config.MethodType;
import org.codegist.crest.param.EncodedPair;

import java.util.List;


/**
 * @author laurent.gilles@codegist.org
 */
public interface OAuthenticator {

    /**
     * Generates the OAuth authorization parameters for the given request
     * @param accessOAuthToken access token to use
     * @param methodType request method type
     * @param url request url
     * @param parameters request parameters
     * @return the list OAuth authorization parameters
     * @throws Exception Any exception thrown during the oauth process
     */
    List<EncodedPair> oauth(OAuthToken accessOAuthToken, MethodType methodType, String url, EncodedPair... parameters) throws Exception;


}
