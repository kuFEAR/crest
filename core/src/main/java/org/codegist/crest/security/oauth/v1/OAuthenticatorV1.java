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

package org.codegist.crest.security.oauth.v1;

import org.codegist.crest.param.EncodedPair;
import org.codegist.crest.security.oauth.OAuthToken;
import org.codegist.crest.security.oauth.OAuthenticator;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.codegist.crest.security.oauth.v1.OAuthsV1.*;

public class OAuthenticatorV1 implements OAuthenticator {

    private final VariantProvider variant;
    private final OAuthToken consumerOAuthToken;

    public OAuthenticatorV1(OAuthToken consumerOAuthToken) {
        this(consumerOAuthToken, new DefaultVariantProvider());
    }

    OAuthenticatorV1(OAuthToken consumerOAuthToken, VariantProvider variant) {
        this.variant = variant;
        this.consumerOAuthToken = consumerOAuthToken;
    }

    public List<EncodedPair> oauth(OAuthToken accessOAuthToken, String action, String url, EncodedPair... parameters) throws InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
        return oauth(accessOAuthToken, action, url, parameters, EMPTY_HTTP_PAIRS);
    }

    List<EncodedPair> oauth(OAuthToken accessOAuthToken, String action, String url, EncodedPair[] parameters, EncodedPair... extrasOAuthParams) throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException {
        List<EncodedPair> oauthParams = oauthParamsFor(accessOAuthToken, extrasOAuthParams); // generate base oauth params
        List<EncodedPair> toSign = new ArrayList<EncodedPair>(oauthParams);
        toSign.addAll(asList(parameters));
        String signature = sign(consumerOAuthToken, accessOAuthToken, SIGN_METH_4_J, ENC, url, action , toSign);
        oauthParams.add(pair("oauth_signature", signature));
        return oauthParams;
    }

    private List<EncodedPair> oauthParamsFor(OAuthToken accessOAuthToken, EncodedPair... extras) throws UnsupportedEncodingException {
        List<EncodedPair> params = new ArrayList<EncodedPair>();
        if(accessOAuthToken != null && accessOAuthToken != IGNORE_POISON) {
            params.add(pair("oauth_token", accessOAuthToken.getToken()));
        }
        params.add(pair("oauth_consumer_key", consumerOAuthToken.getToken()));
        params.add(pair("oauth_signature_method", SIGN_METH));
        params.add(pair("oauth_timestamp", variant.timestamp()));
        params.add(pair("oauth_nonce", variant.nonce()));
        params.add(pair("oauth_version", "1.0"));
        params.addAll(asList(extras));
        return params;
    }
}
