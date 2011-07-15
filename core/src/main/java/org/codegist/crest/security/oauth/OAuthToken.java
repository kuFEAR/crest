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

import org.codegist.common.lang.ToStringBuilder;
import org.codegist.crest.io.http.Pair;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Map;

import static java.util.Collections.unmodifiableMap;

/**
 * OAuth Token information holder
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class OAuthToken {

    private final String token;
    private final String secret;
    private final Map<String,String> attributes;

    public OAuthToken(String token, String secret) {
        this(token, secret, Collections.<String, String>emptyMap());
    }
    public OAuthToken(String token, String secret, Map<String,String> attributes) {
        this.token = token;
        this.secret = secret;
        this.attributes = unmodifiableMap(attributes);
    }

    public String getToken() {
        return token;
    }

    public String getSecret() {
        return secret;
    }

    public Pair getAttribute(String name) throws UnsupportedEncodingException {
        return new Pair(name, attributes.get(name));
    }

    /**
     * @return Extra non-oauth-specification compliant fields returned by the oauth service.
     */
    public Map<String,String> getAttributes(){
        return attributes;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("token", token)
                .append("secret", secret)
                .append("attributes", attributes)
                .toString();
    }
}
