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

package org.codegist.crest.security.oauth.v1;

import org.codegist.crest.config.MethodType;
import org.codegist.crest.param.EncodedPair;
import org.codegist.crest.security.oauth.OAuthToken;
import org.codegist.crest.security.oauth.OAuthenticator;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * OAuth V1.0 implementation of {@link org.codegist.crest.security.oauth.OAuthenticator}
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class OAuthenticatorV1 implements OAuthenticator {

    private final VariantProvider variantProvider;
    private final OAuthToken consumerOAuthToken;

    /**
     *
     * @param consumerOAuthToken Consumer token to use
     */
    public OAuthenticatorV1(OAuthToken consumerOAuthToken) {
        this(consumerOAuthToken, DefaultVariantProvider.INSTANCE);
    }

    OAuthenticatorV1(OAuthToken consumerOAuthToken, VariantProvider variantProvider) {
        this.variantProvider = variantProvider;
        this.consumerOAuthToken = consumerOAuthToken;
    }

    /**
     * @inheritDoc
     */
    public List<EncodedPair> oauth(OAuthToken accessOAuthToken, MethodType methodType, String url, EncodedPair... parameters) throws InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
        return OAuthsV1.oauth(variantProvider, consumerOAuthToken, accessOAuthToken, methodType, url, parameters);
    }
}
