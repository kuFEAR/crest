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

import org.codegist.common.io.IOs;
import org.codegist.crest.security.oauth.OAuthToken;
import org.codegist.crest.serializer.TypeDeserializer;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;

import static org.codegist.common.collect.Maps.filter;
import static org.codegist.common.net.Urls.parseQueryString;

class TokenDeserializer extends TypeDeserializer<OAuthToken> {
    @Override
    protected OAuthToken deserialize(InputStream stream, Charset charset) throws Exception {
        Map<String, String> tokens = parseQueryString(IOs.toString(stream, charset, true));
        return new OAuthToken(
            tokens.get("oauth_token"),
            tokens.get("oauth_token_secret"),
            filter(tokens, "oauth_token", "oauth_token_secret")
        );
    }
}