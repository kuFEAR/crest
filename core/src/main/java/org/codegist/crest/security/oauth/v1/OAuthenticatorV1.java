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

import org.codegist.common.collect.Maps;
import org.codegist.common.lang.Disposables;
import org.codegist.common.lang.Validate;
import org.codegist.common.log.Logger;
import org.codegist.crest.io.Response;
import org.codegist.crest.io.http.*;
import org.codegist.crest.io.http.entity.EntityWriter;
import org.codegist.crest.io.http.entity.UrlEncodedFormEntityWriter;
import org.codegist.crest.io.http.param.ParamType;
import org.codegist.crest.security.oauth.OAuthToken;
import org.codegist.crest.security.oauth.OAuthenticator;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.codegist.common.collect.Maps.filter;
import static org.codegist.common.net.Urls.parseQueryString;
import static org.codegist.crest.CRestProperty.get;
import static org.codegist.crest.io.http.HttpMethod.GET;
import static org.codegist.crest.io.http.HttpMethod.POST;
import static org.codegist.crest.io.http.param.ParamType.FORM;
import static org.codegist.crest.io.http.param.ParamType.QUERY;
import static org.codegist.crest.security.oauth.v1.OAuthsV1.sign;

public class OAuthenticatorV1 implements OAuthenticator {

    public static final String CONFIG_TOKEN_ACCESS_REFRESH_URL = OAuthenticatorV1.class.getName() + "#access.refresh.url";
    public static final String CONFIG_TOKEN_ACCESS_REFRESH_URL_METHOD = OAuthenticatorV1.class.getName() + "#access.refresh.url.method";
    public static final String CONFIG_TOKEN_REQUEST_URL = OAuthenticatorV1.class.getName() + "#request.url";
    public static final String CONFIG_TOKEN_REQUEST_URL_METHOD = OAuthenticatorV1.class.getName() + "#request.url.method";
    public static final String CONFIG_TOKEN_ACCESS_URL = OAuthenticatorV1.class.getName() + "#access.url";
    public static final String CONFIG_TOKEN_ACCESS_URL_METHOD = OAuthenticatorV1.class.getName() + "#access.url.method";
    public static final String CONFIG_TOKEN_ACCESS_CALLBACK = OAuthenticatorV1.class.getName() + "#access.callback";

    private static final Logger LOGGER = Logger.getLogger(OAuthenticatorV1.class);
    private static final OAuthToken IGNORE_POISON = new OAuthToken("","");
    private static final String ENC = "UTF-8";
    private static final Charset CHARSET = Charset.forName(ENC);
    private static final String SIGN_METH = "HMAC-SHA1";
    private static final String SIGN_METH_4_J = "HmacSHA1";
    private static final EntityWriter ENTITY_WRITER = new UrlEncodedFormEntityWriter();
    private static final Pair[] EMPTY_PAIRS = new Pair[0];

    private final VariantProvider variant;
    private final OAuthToken consumerOAuthToken;
    private final HttpRequestExecutor httpRequestExecutor;
    private final String callback;
    private final String requestTokenUrl;
    private final HttpMethod requestTokenMeth;
    private final String accessTokenUrl;
    private final HttpMethod accessTokenMeth;
    private final String refreshAccessTokenUrl;
    private final HttpMethod refreshAccessTokenMeth;

    public OAuthenticatorV1(HttpRequestExecutor httpRequestExecutor, OAuthToken consumerOAuthToken, Map<String,Object> crestProperties) {
        this(httpRequestExecutor, consumerOAuthToken, crestProperties, new DefaultVariantProvider());
    }

    OAuthenticatorV1(HttpRequestExecutor httpRequestExecutor, OAuthToken consumerOAuthToken, Map<String, Object> crestProperties, VariantProvider variant) {
        this.variant = variant;
        this.consumerOAuthToken = consumerOAuthToken;
        this.httpRequestExecutor = httpRequestExecutor;
        this.callback = get(crestProperties, CONFIG_TOKEN_ACCESS_CALLBACK, "oob");
        this.requestTokenUrl = get(crestProperties, CONFIG_TOKEN_REQUEST_URL);
        this.requestTokenMeth = HttpMethod.valueOf(get(crestProperties, CONFIG_TOKEN_REQUEST_URL_METHOD, POST.name()));
        this.accessTokenUrl = get(crestProperties, CONFIG_TOKEN_ACCESS_URL);
        this.accessTokenMeth = HttpMethod.valueOf(get(crestProperties, CONFIG_TOKEN_ACCESS_URL_METHOD, POST.name()));
        this.refreshAccessTokenUrl = get(crestProperties, CONFIG_TOKEN_ACCESS_REFRESH_URL);
        this.refreshAccessTokenMeth = HttpMethod.valueOf(get(crestProperties, CONFIG_TOKEN_ACCESS_REFRESH_URL_METHOD, POST.name()));
    }

    public List<Pair> oauth(OAuthToken accessOAuthToken, HttpMethod method, String url, Pair... parameters) throws InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
        return oauth(accessOAuthToken, method, url, parameters, EMPTY_PAIRS);
    }

    public OAuthToken getRequestToken() throws Exception {
        Validate.notBlank(this.requestTokenUrl, "No request token url as been configured, please pass it in the config map, key=%s", CONFIG_TOKEN_REQUEST_URL);
        OAuthToken token = getAccessToken(IGNORE_POISON, this.requestTokenUrl, requestTokenMeth, pair("oauth_callback", callback));
        LOGGER.debug("Request token token=%s", token);
        return token;
    }

    public OAuthToken getAccessToken(OAuthToken requestOAuthToken, String verifier) throws Exception {
        Validate.notBlank(this.accessTokenUrl, "No access token url as been configured, please pass it in the config map, key=%s", CONFIG_TOKEN_ACCESS_URL);

        OAuthToken token = getAccessToken(requestOAuthToken, this.accessTokenUrl, this.accessTokenMeth, pair("oauth_verifier", verifier));
        LOGGER.debug("Received access token=%s", token);
        return token;
    }

    public OAuthToken refreshAccessToken(OAuthToken requestOAuthToken, Pair... extrasOAuthParams) throws Exception {
        Validate.notBlank(this.refreshAccessTokenUrl, "No refresh access token url as been configured, please pass it in the config map, key=%s", CONFIG_TOKEN_ACCESS_REFRESH_URL);

        OAuthToken token = getAccessToken(requestOAuthToken, this.refreshAccessTokenUrl, this.refreshAccessTokenMeth, extrasOAuthParams);
        LOGGER.debug("Refreshed access token=%s", token);
        return token;
    }


    private OAuthToken getAccessToken(OAuthToken requestOAuthToken, String url, HttpMethod meth, Pair... extrasOAuthParams) throws Exception {
        HttpResponse refreshTokenResponse = null;
        try {
            List<Pair> oauthParams = oauth(requestOAuthToken, meth, url, EMPTY_PAIRS, extrasOAuthParams);

            ParamType type = GET.equals(meth) ? QUERY : FORM;

            HttpRequest.Builder request = new HttpRequest.Builder(url, ENTITY_WRITER, ENC).action(meth);
            for(Pair param : oauthParams){
                request.addParam(param.getName(), param.getValue(), type, true);
            }

            refreshTokenResponse = httpRequestExecutor.execute(request.build());

            Map<String,String> result = parseQueryString(refreshTokenResponse.deserializeTo(String.class));
            return new OAuthToken(
                    result.get("oauth_token"),
                    result.get("oauth_token_secret"),
                    filter(result, "oauth_token", "oauth_token_secret")
            );
        } finally {
           Disposables.dispose(refreshTokenResponse);
        }
    }

    private List<Pair> oauth(OAuthToken accessOAuthToken, HttpMethod method, String url, Pair[] parameters, Pair... extrasOAuthParams) throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException {
        List<Pair> oauthParams = oauthParamsFor(accessOAuthToken, extrasOAuthParams); // generate base oauth params
        List<Pair> toSign = new ArrayList<Pair>(oauthParams);
        toSign.addAll(asList(parameters));
        String signature = sign(consumerOAuthToken, accessOAuthToken, SIGN_METH_4_J, ENC, url, method , toSign);
        oauthParams.add(pair("oauth_signature", signature));
        return oauthParams;
    }

    private List<Pair> oauthParamsFor(OAuthToken accessOAuthToken, Pair... extras) throws UnsupportedEncodingException {
        List<Pair> params = new ArrayList<Pair>();
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

    private static Pair pair(String name, String value) throws UnsupportedEncodingException {
        return new Pair(name, value, CHARSET, false);
    }
}
