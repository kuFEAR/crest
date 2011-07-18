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

package org.codegist.crest.io.http;

import org.codegist.crest.config.MethodType;
import org.codegist.crest.security.Authorization;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * @author laurent.gilles@codegist.org
 */
public class AuthorizationHttpChannelInitiator implements HttpChannelInitiator {

    private final HttpChannelInitiator delegate;
    private final Authorization authorization;
    private final Map<String, EntityParamExtractor> httpEntityParamsParsers;

    public AuthorizationHttpChannelInitiator(HttpChannelInitiator delegate, Authorization authorization, Map<String, EntityParamExtractor> httpEntityParamsParsers) {
        this.delegate = delegate;
        this.authorization = authorization;
        this.httpEntityParamsParsers = httpEntityParamsParsers;
    }

    public HttpChannel initiate(MethodType methodType, String url, Charset charset) throws IOException {
        return new AuthorizationHttpChannel(delegate.initiate(methodType, url, charset), authorization, methodType, url, charset, httpEntityParamsParsers);
    }
}
