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

import org.codegist.common.codec.Base64;
import org.codegist.common.collect.Maps;
import org.codegist.common.lang.Disposables;
import org.codegist.common.lang.Strings;
import org.codegist.common.lang.Validate;
import org.codegist.common.log.Logger;
import org.codegist.crest.CRestException;
import org.codegist.crest.CRestProperty;
import org.codegist.crest.io.Response;
import org.codegist.crest.io.http.Pair;
import org.codegist.crest.io.RequestExecutor;
import org.codegist.crest.io.http.entity.EntityWriter;
import org.codegist.crest.io.http.entity.UrlEncodedFormEntityWriter;
import org.codegist.crest.io.http.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static java.util.Arrays.asList;
import static org.codegist.common.net.Urls.encode;
import static org.codegist.common.net.Urls.parseQueryString;
import static org.codegist.crest.util.Pairs.join;
import static org.codegist.crest.util.Pairs.sortByNameAndValues;

public class OAuthenticatorV1 implements OAuthenticator {

    public static final String CONFIG_TOKEN_ACCESS_REFRESH_URL = CRestProperty.OAUTH_ACCESS_TOKEN_REFRESH_URL;

    public static final String CONFIG_TOKEN_ACCESS_REFRESH_URL_METHOD = CRestProperty.OAUTH_TOKEN_ACCESS_REFRESH_URL_METHOD;

    public static final String CONFIG_TOKEN_REQUEST_URL = "authentification.oauth.access.io-url";

    public static final String CONFIG_TOKEN_REQUEST_URL_METHOD = "authentification.oauth.access.io-url.method";

    public static final String CONFIG_TOKEN_ACCESS_URL = "authentification.oauth.access.access-url";

    public static final String CONFIG_TOKEN_ACCESS_URL_METHOD = "authentification.oauth.access.access-url.method";

    public static final String CONFIG_OAUTH_CALLBACK = "authentification.oauth.io.callback";

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
    private final RequestExecutor httpRequestExecutor;
    private final String callback;

    private final String requestTokenUrl;
    private final HttpMethod requestTokenMeth;

    private final String accessTokenUrl;
    private final HttpMethod accessTokenMeth;

    private final String refreshAccessTokenUrl;
    private final HttpMethod refreshAccessTokenMeth;

    public OAuthenticatorV1(RequestExecutor httpRequestExecutor, OAuthToken consumerOAuthToken, VariantProvider variant) {
        this(httpRequestExecutor, consumerOAuthToken, variant, null);
    }

    public OAuthenticatorV1(RequestExecutor httpRequestExecutor, OAuthToken consumerOAuthToken, Map<String,Object> crestProperties) {
        this(httpRequestExecutor, consumerOAuthToken, new DefaultVariantProvider(), crestProperties);
    }

    public OAuthenticatorV1(RequestExecutor httpRequestExecutor, OAuthToken consumerOAuthToken, VariantProvider variant, Map<String, Object> crestProperties) {
        this.variant = variant;
        this.consumerOAuthToken = consumerOAuthToken;
        this.httpRequestExecutor = httpRequestExecutor;

        this.callback = Strings.defaultIfBlank((String) crestProperties.get(CONFIG_OAUTH_CALLBACK), "oob");

        this.requestTokenUrl = (String) crestProperties.get(CONFIG_TOKEN_REQUEST_URL);
        if (Strings.isNotBlank((String) crestProperties.get(CONFIG_TOKEN_REQUEST_URL_METHOD))) {
            requestTokenMeth = HttpMethod.valueOf(((String) crestProperties.get(CONFIG_TOKEN_REQUEST_URL_METHOD)));
        }else{
            requestTokenMeth = HttpMethod.POST;
        }

        this.accessTokenUrl = (String) crestProperties.get(CONFIG_TOKEN_ACCESS_URL);
        if (Strings.isNotBlank((String) crestProperties.get(CONFIG_TOKEN_ACCESS_URL_METHOD))){
            accessTokenMeth = HttpMethod.valueOf((String) crestProperties.get(CONFIG_TOKEN_ACCESS_URL_METHOD));
        }else{
            accessTokenMeth =  HttpMethod.POST;
        }

        this.refreshAccessTokenUrl = (String) crestProperties.get(CONFIG_TOKEN_ACCESS_REFRESH_URL);
        if (Strings.isNotBlank((String) crestProperties.get(CONFIG_TOKEN_ACCESS_REFRESH_URL_METHOD))){
            refreshAccessTokenMeth = HttpMethod.valueOf((String) crestProperties.get(CONFIG_TOKEN_ACCESS_REFRESH_URL_METHOD));
        }else{
            refreshAccessTokenMeth =  HttpMethod.POST;
        }
    }

    public List<Pair> oauth(OAuthToken accessOAuthToken, HttpMethod method, String url, Pair... parameters) {
        return oauth(accessOAuthToken, method, url, parameters, EMPTY_PAIRS);
    }
    
    private List<Pair> oauth(OAuthToken accessOAuthToken, HttpMethod method, String url, Pair[] parameters, Pair... extrasOAuthParams) {
        List<Pair> oauthParams = oauthParamsFor(accessOAuthToken, extrasOAuthParams); // generate base oauth params
        List<Pair> toSign = new ArrayList<Pair>(oauthParams);
        toSign.addAll(asList(parameters));
        String signature = sign(accessOAuthToken, url, method , toSign);
        oauthParams.add(pair("oauth_signature", signature));
        return oauthParams;
    }

    public OAuthToken getRequestToken() {
        Validate.notBlank(this.requestTokenUrl, "No io token url as been configured, please pass it in the config map, key=" + CONFIG_TOKEN_REQUEST_URL);
        OAuthToken token = getAccessToken(IGNORE_POISON, this.requestTokenUrl, requestTokenMeth, pair("oauth_callback", callback));
        LOGGER.debug("Request token token=%s", token);
        return token;
    }

    public OAuthToken getAccessToken(OAuthToken requestOAuthToken, String verifier) {
        Validate.notBlank(this.accessTokenUrl, "No access token url as been configured, please pass it in the config map, key=" + CONFIG_TOKEN_ACCESS_URL);

        OAuthToken token = getAccessToken(requestOAuthToken, this.accessTokenUrl, this.accessTokenMeth, pair("oauth_verifier", verifier));
        LOGGER.debug("Received access token=%s", token);
        return token;
    }

    public OAuthToken refreshAccessToken(OAuthToken requestOAuthToken, Pair... extrasOAuthParams) {
        Validate.notBlank(this.refreshAccessTokenUrl, "No refresh access token url as been configured, please pass it in the config map, key=" + CONFIG_TOKEN_ACCESS_REFRESH_URL);

        OAuthToken token = getAccessToken(requestOAuthToken, this.refreshAccessTokenUrl, this.refreshAccessTokenMeth, extrasOAuthParams);
        LOGGER.debug("Refreshed access token=%s", token);
        return token;
    }


    private OAuthToken getAccessToken(OAuthToken requestOAuthToken, String url, HttpMethod meth, Pair... extrasOAuthParams) {
        Response refreshTokenResponse = null;
        try {
            List<Pair> oauthParams = oauth(requestOAuthToken, meth, url, EMPTY_PAIRS, extrasOAuthParams);

            String dest = HttpMethod.GET.equals(meth) ? HttpRequest.DEST_QUERY : HttpRequest.DEST_FORM;

            HttpRequest.Builder request = new HttpRequest.Builder(url, ENTITY_WRITER, ENC).action(meth);
            for(Pair param : oauthParams){
                request.addParam(param.getName(), param.getValue(), dest, true);
            }

            refreshTokenResponse = httpRequestExecutor.execute(request.build());

            Map<String,String> result = parseQueryString(refreshTokenResponse.deserializeTo(String.class));
            return new OAuthToken(
                    result.get("oauth_token"),
                    result.get("oauth_token_secret"),
                    Maps.filter(result, "oauth_token", "oauth_token_secret")
            );
        } catch (Exception e) {
            throw CRestException.handle(e);
        } finally {
            if (refreshTokenResponse != null) {
                Disposables.dispose(refreshTokenResponse);
            }
        }
    }


    /**
     * The Signature Base String includes the io absolute URL, tying the signature to a specific endpoint. The URL used in the Signature Base String MUST include the scheme, authority, and path, and MUST exclude the query and fragment as defined by [RFC3986] section 3.<br>
     * If the absolute io URL is not available to the Service Provider (it is always available to the Consumer), it can be constructed by combining the scheme being used, the HTTP Host header, and the relative HTTP io URL. If the Host header is not available, the Service Provider SHOULD use the host name communicated to the Consumer in the documentation or other means.<br>
     * The Service Provider SHOULD document the form of URL used in the Signature Base String to avoid ambiguity due to URL normalization. Unless specified, URL scheme and authority MUST be lowercase and include the port number; http default port 80 and https default port 443 MUST be excluded.<br>
     * <br>
     * For example, the io:<br>
     * HTTP://Example.com:80/resource?id=123<br>
     * Is included in the Signature Base String as:<br>
     * http://example.com/resource
     *
     * @param url the url to be normalized
     * @return the Signature Base String
     * @see <a href="http://oauth.net/core/1.0#rfc.section.9.1.2">OAuth Core - 9.1.2.  Construct Request URL</a>
     */
    private static String constructRequestURL(String url) {
        int index = url.indexOf('?');
        if (-1 != index) {
            url = url.substring(0, index);
        }
        int slashIndex = url.indexOf("/", 8);
        String baseURL = url.substring(0, slashIndex).toLowerCase();
        int colonIndex = baseURL.indexOf(":", 8);
        if (-1 != colonIndex) {
            // url contains port number
            if (baseURL.startsWith("http://") && baseURL.endsWith(":80")) {
                // http default port 80 MUST be excluded
                baseURL = baseURL.substring(0, colonIndex);
            } else if (baseURL.startsWith("https://") && baseURL.endsWith(":443")) {
                // http default port 443 MUST be excluded
                baseURL = baseURL.substring(0, colonIndex);
            }
        }
        return baseURL + url.substring(slashIndex);
    }

    private String sign(OAuthToken accessOAuthToken, String url, HttpMethod method, List<Pair> oauthParams) {
        // first, sort the list without changing the one given
        List<Pair> sorted = sortByNameAndValues(oauthParams);

        String signMeth = method.toString();
        String signUri = constructRequestURL(url);
        String signParams = join(sorted, '&', '=', false, false);
        String signature = consumerOAuthToken.getSecret() + "&" + accessOAuthToken.getSecret();

        try {
            Mac mac = Mac.getInstance(SIGN_METH_4_J);
            mac.init(new SecretKeySpec(signature.getBytes(ENC), SIGN_METH_4_J));

            String data = signMeth + "&" + encode(signUri, ENC) + "&" + encode(signParams, ENC);
            String encoded = new String(Base64.encodeToByte(mac.doFinal(data.getBytes(ENC))), ENC);

            LOGGER.debug("Signature[data=\"%s\",signature=\"%s\",result=\"%s\"]", data, signature, encoded);
            return encoded;
        } catch(Exception e){
            throw CRestException.handle(e);
        }
    }


    private static Pair pair(String name, String value){
        return new Pair(name, value, CHARSET, false);
    }

    private List<Pair> oauthParamsFor(OAuthToken accessOAuthToken, Pair... extras) {
        return oauthParamsFor(SIGN_METH, accessOAuthToken, extras);
    }

    private List<Pair> oauthParamsFor(String signatureMethod, OAuthToken accessOAuthToken, Pair... extras) {
        List<Pair> params = new ArrayList<Pair>();
        if(accessOAuthToken != null && accessOAuthToken != IGNORE_POISON) {
            params.add(pair("oauth_token", accessOAuthToken.getToken()));
        }
        params.add(pair("oauth_consumer_key", consumerOAuthToken.getToken()));
        params.add(pair("oauth_signature_method", signatureMethod));
        params.add(pair("oauth_timestamp", variant.timestamp()));
        params.add(pair("oauth_nonce", variant.nonce()));
        params.add(pair("oauth_version", "1.0"));
        params.addAll(asList(extras));
        return params;
    }

    static interface VariantProvider {

        String timestamp();

        String nonce();

    }

    static class DefaultVariantProvider implements VariantProvider {
        private final Random rdm = new SecureRandom();

        public String timestamp() {
            return String.valueOf(System.currentTimeMillis() / 1000l);
        }

        public String nonce() {
            return String.valueOf(System.currentTimeMillis() + rdm.nextLong());
        }
    }

}
