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

package org.codegist.crest.security.http;

import org.codegist.crest.http.HttpChannel;
import org.codegist.crest.http.HttpChannelInitiator;
import org.codegist.crest.http.HttpMethod;
import org.codegist.crest.security.Authorization;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author laurent.gilles@codegist.org
 */
public class AuthorizationHttpChannelInitiator implements HttpChannelInitiator {

    private final HttpChannelInitiator delegate;
    private final Authorization authorization;

    public AuthorizationHttpChannelInitiator(HttpChannelInitiator delegate, Authorization authorization) {
        this.delegate = delegate;
        this.authorization = authorization;
    }

    public HttpChannel initiate(HttpMethod method, String url, Charset charset) throws IOException {
        return new AuthorizationHttpChannel(delegate.initiate(method, url, charset), authorization, method, url, charset);
    }
}
