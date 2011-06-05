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

package org.codegist.crest.oauth;

import org.codegist.common.codec.Base64;
import org.codegist.common.collect.Maps;
import org.codegist.common.lang.Strings;
import org.codegist.common.lang.Validate;
import org.codegist.common.log.Logger;
import org.codegist.common.net.Urls;
import org.codegist.crest.*;
import org.codegist.crest.serializer.UrlEncodedHttpParamSerializer;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.*;

import static org.codegist.common.net.Urls.encode;

/**
 * OAuth v1.0 authentificator implementation
 * TODO : tidy up, explode in different specilized classes: more cohesion and less coupling please!!
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class OAuthenticatorV10 implements OAuthenticator {

    public static final String CONFIG_TOKEN_ACCESS_REFRESH_URL = CRestProperty.OAUTH_ACCESS_TOKEN_REFRESH_URL;

    public static final String CONFIG_TOKEN_ACCESS_REFRESH_URL_METHOD = CRestProperty.OAUTH_TOKEN_ACCESS_REFRESH_URL_METHOD;

    public static final String CONFIG_OAUTH_PARAM_DEST = CRestProperty.OAUTH_PARAM_DEST;

    public static final String CONFIG_TOKEN_REQUEST_URL = "authentification.oauth.access.request-url";

    public static final String CONFIG_TOKEN_REQUEST_URL_METHOD = "authentification.oauth.access.request-url.method";

    public static final String CONFIG_TOKEN_ACCESS_URL = "authentification.oauth.access.access-url";

    public static final String CONFIG_TOKEN_ACCESS_URL_METHOD = "authentification.oauth.access.access-url.method";

    public static final String CONFIG_OAUTH_CALLBACK = "authentification.oauth.request.callback";

    private final static Logger LOGGER = Logger.getLogger(OAuthenticatorV10.class);

    private final static String ENC = "UTF-8";
    private final static Charset CHARSET = Charset.forName(ENC);

    private final static String SIGN_METH = "HMAC-SHA1";
    private final static String SIGN_METH_4_J = "HmacSHA1";
    private final VariantProvider variant;

    private final EntityWriter entityWriter;

    private final Token consumerToken;
    private final RestService restService;
    private final String callback;
    private final boolean toHeaders;

    private final String requestTokenUrl;
    private final String requestTokenMeth;

    private final String accessTokenUrl;
    private final String accessTokenMeth;

    private final String refreshAccessTokenUrl;
    private final String refreshAccessTokenMeth;

    private final UrlEncodedHttpParamSerializer<Map<String, List<HttpParam>>> paramSerializerAmpNoQuote;
    private final UrlEncodedHttpParamSerializer<Map<String, List<HttpParam>>> paramSerializerComaQuote;


    public OAuthenticatorV10(RestService restService, Token consumerToken, VariantProvider variant) {
        this(restService, consumerToken, null, variant);
    }

    public OAuthenticatorV10(RestService restService, Token consumerToken, Map<String, Object> customProperties, VariantProvider variant) {
        this.variant = variant;
        this.consumerToken = consumerToken;
        this.restService = restService;
        customProperties = Maps.defaultsIfNull(customProperties);

        this.entityWriter = new UrlEncodedFormEntityWriter(customProperties);
        this.callback = Strings.defaultIfBlank((String) customProperties.get(CONFIG_OAUTH_CALLBACK), "oob");
        this.toHeaders = !"url".equals(customProperties.get(CONFIG_OAUTH_PARAM_DEST));

        this.requestTokenUrl = (String) customProperties.get(CONFIG_TOKEN_REQUEST_URL);
        if (Strings.isNotBlank((String) customProperties.get(CONFIG_TOKEN_REQUEST_URL_METHOD)))
            requestTokenMeth = (String) customProperties.get(CONFIG_TOKEN_REQUEST_URL_METHOD);
        else
            requestTokenMeth = HttpRequest.HTTP_POST;

        this.accessTokenUrl = (String) customProperties.get(CONFIG_TOKEN_ACCESS_URL);
        if (Strings.isNotBlank((String) customProperties.get(CONFIG_TOKEN_ACCESS_URL_METHOD)))
            accessTokenMeth = (String) customProperties.get(CONFIG_TOKEN_ACCESS_URL_METHOD);
        else
            accessTokenMeth = HttpRequest.HTTP_POST;

        this.refreshAccessTokenUrl = (String) customProperties.get(CONFIG_TOKEN_ACCESS_REFRESH_URL);
        if (Strings.isNotBlank((String) customProperties.get(CONFIG_TOKEN_ACCESS_REFRESH_URL_METHOD)))
            refreshAccessTokenMeth = (String) customProperties.get(CONFIG_TOKEN_ACCESS_REFRESH_URL_METHOD);
        else
            refreshAccessTokenMeth = HttpRequest.HTTP_POST;

        String multiValuedParamSeparator = (String) customProperties.get(CRestProperty.FORM_PARAM_COLLECTION_SEPARATOR); // TODO SHOULD USE PER TYPE (Header,Form,Query)
        if(multiValuedParamSeparator == null) {
            this.paramSerializerAmpNoQuote = UrlEncodedHttpParamSerializer.createDefaultForMap("&");
            this.paramSerializerComaQuote = UrlEncodedHttpParamSerializer.createDefaultForMap(",", false, true);
        }else{
            this.paramSerializerAmpNoQuote = UrlEncodedHttpParamSerializer.createCollectionMergingForMap("&", multiValuedParamSeparator);
            this.paramSerializerComaQuote = UrlEncodedHttpParamSerializer.createCollectionMergingForMap(",", multiValuedParamSeparator, false, true);
        }
    }

    public OAuthenticatorV10(RestService restService, Token consumerToken, Map<String,Object> customProperties) {
        this(restService, consumerToken, customProperties, new DefaultVariantProvider());
    }

    public OAuthenticatorV10(RestService restService, Token consumerToken) {
        this(restService, consumerToken, (Map<String,Object>) null);
    }


    public Token getRequestToken() {
        Validate.notBlank(this.requestTokenUrl, "No request token url as been configured, please pass it in the config map, key=" + CONFIG_TOKEN_REQUEST_URL);
        HttpResponse refreshTokenResponse = null;
        try {
            HttpRequest.Builder request = new HttpRequest.Builder(this.requestTokenUrl, entityWriter, ENC).using(requestTokenMeth);

            HttpParamMap oauthParams = newBaseOAuthParams();
            oauthParams.put(new HttpParam("oauth_callback", callback, false));
            String sign = generateSignature(new Token("",""), request, oauthParams);
            oauthParams.put(new HttpParam("oauth_signature", sign, false));

            String dest = HttpRequest.HTTP_GET.equals(requestTokenMeth) ?HttpRequest.DEST_QUERY : HttpRequest.DEST_FORM;
            for(HttpParam param : oauthParams.allValues()){
                request.addParam(param.getName(), param.getValue(), dest, param.isEncoded());
            }
            refreshTokenResponse = restService.exec(request.build());
            Map<String,String> result = Urls.parseQueryString(refreshTokenResponse.asString());
            Token token = new Token(
                    result.get("oauth_token"),
                    result.get("oauth_token_secret"),
                    Maps.filter(result, "oauth_token", "oauth_token_secret")
            );
            LOGGER.debug("Received request token=%s", token);
            return token;
        } catch (Exception e) {
            throw new OAuthException(e);
        } finally {
            if (refreshTokenResponse != null) {
                refreshTokenResponse.close();
            }
        }
    }

    public Token refreshAccessToken(Token requestToken, String... includeExtras) {
        Validate.notBlank(this.refreshAccessTokenUrl, "No refresh access token url as been configured, please pass it in the config map, key=" + CONFIG_TOKEN_ACCESS_REFRESH_URL);
        HttpParamMap params = new HttpParamMap();
        for(String extra : includeExtras){
            if(requestToken.getExtra(extra) == null) continue;
            params.put(new HttpParam(extra, requestToken.getExtra(extra), false));
        }
        Token token = getAccessToken(this.refreshAccessTokenUrl, this.refreshAccessTokenMeth, requestToken, params);
        LOGGER.debug("Refreshed access token=%s", token);
        return token;
    }
    
    public Token getAccessToken(Token requestToken, String verifier) {
        Validate.notBlank(this.accessTokenUrl, "No access token url as been configured, please pass it in the config map, key=" + CONFIG_TOKEN_ACCESS_URL);
        HttpParamMap set = new HttpParamMap();
        set.put(new HttpParam("oauth_verifier", verifier, false));
        Token token = getAccessToken(this.accessTokenUrl, this.accessTokenMeth, requestToken, set);
        LOGGER.debug("Received access token=%s", token);
        return token;
    }

    private Token getAccessToken(String url, String meth, Token requestToken, HttpParamMap extras) {
        HttpResponse refreshTokenResponse = null;
        try {
            HttpRequest.Builder request = new HttpRequest.Builder(url, entityWriter, ENC).using(meth);

            HttpParamMap oauthParams = newBaseOAuthParams();
            oauthParams.put(new HttpParam("oauth_token", requestToken.getToken(), false));
            if(extras != null) oauthParams.putAll(extras);

            String sign = generateSignature(requestToken, request, oauthParams);
            oauthParams.put(new HttpParam("oauth_signature", sign, false));

            String dest = HttpRequest.HTTP_GET.equals(meth) ? HttpRequest.DEST_QUERY : HttpRequest.DEST_FORM;
            for(HttpParam param : oauthParams.allValues()){
                request.addParam(param.getName(), param.getValue(), dest, param.isEncoded());
            }

            refreshTokenResponse = restService.exec(request.build());
            Map<String,String> result = Urls.parseQueryString(refreshTokenResponse.asString());
            return new Token(
                    result.get("oauth_token"),
                    result.get("oauth_token_secret"),
                    Maps.filter(result, "oauth_token", "oauth_token_secret")
            );
        } catch (Exception e) {
            throw new OAuthException(e);
        } finally {
            if (refreshTokenResponse != null) {
                refreshTokenResponse.close();
            }
        }
    }

    public void sign(Token accessToken, HttpRequest.Builder request, HttpParam... extraHeaders) {
        HttpParamMap extraHeaderMap = new HttpParamMap();
        for(HttpParam header : extraHeaders){
            extraHeaderMap.put(header);
        }
        try {
            sign(accessToken, request, extraHeaderMap);
        } catch (UnsupportedEncodingException e) {
            throw new OAuthException(e);
        }
    }

    private void sign(Token accessToken, HttpRequest.Builder request, HttpParamMap extraHeaders) throws UnsupportedEncodingException {

        HttpParamMap oauthParams = newBaseOAuthParams(); // generate base oauth params
        oauthParams.put(new HttpParam("oauth_token", accessToken.getToken(), false));
        oauthParams.putAll(extraHeaders);

        // Generate params for signature, these params contains the query string and body params on top of the already existing one.
        HttpParamMap signatureParams = new HttpParamMap();
        signatureParams.putAll(oauthParams);
        signatureParams.putAll(extractOAuthParams(request));

        String signature = generateSignature(accessToken, request, signatureParams);

        // Add signature to the base param list
        oauthParams.put(new HttpParam("oauth_signature", signature, false));

        if (toHeaders) {
            request.addHeaderParam("Authorization", generateOAuthHeader(oauthParams));
        } else {
            for (HttpParam p : oauthParams.allValues()) {
                request.addParam(p.getName(), p.getValue(), HttpRequest.DEST_QUERY, p.isEncoded()); // TODO this isEncoded must be carefully checked all over the place!!
            }
        }
    }

    private String generateOAuthHeader(HttpParamMap oauthParams) throws UnsupportedEncodingException {
        return "OAuth " + paramSerializerComaQuote.serialize(oauthParams, CHARSET);
    }

    private HttpParamMap newBaseOAuthParams() {
        return newBaseOAuthParams(SIGN_METH);
    }
    private HttpParamMap newBaseOAuthParams(String signatureMethod) {
        HttpParamMap params = new HttpParamMap();
        params.put(new HttpParam("oauth_consumer_key", consumerToken.getToken(), false));
        params.put(new HttpParam("oauth_signature_method", signatureMethod, false));
        params.put(new HttpParam("oauth_timestamp", variant.timestamp(), false));
        params.put(new HttpParam("oauth_nonce", variant.nonce(), false));
        params.put(new HttpParam("oauth_version", "1.0", false));
        return params;
    }


    private static HttpParamMap extractOAuthParams(HttpRequest.Builder builder) {
        HttpParamMap params = new HttpParamMap();
        if (builder.getQueryParams() != null) {
            params.putAll(builder.getQueryParams());
        }
        if (builder.getFormParams() != null)
            for (HttpParam param : builder.getFormParams().allValues()) {
                // if is multipart, ignore  todo remove that once @MultiPartParam is introduced
                params.put(param);
            }

        return params;
    }

    /**
     * The Signature Base String includes the request absolute URL, tying the signature to a specific endpoint. The URL used in the Signature Base String MUST include the scheme, authority, and path, and MUST exclude the query and fragment as defined by [RFC3986] section 3.<br>
     * If the absolute request URL is not available to the Service Provider (it is always available to the Consumer), it can be constructed by combining the scheme being used, the HTTP Host header, and the relative HTTP request URL. If the Host header is not available, the Service Provider SHOULD use the host name communicated to the Consumer in the documentation or other means.<br>
     * The Service Provider SHOULD document the form of URL used in the Signature Base String to avoid ambiguity due to URL normalization. Unless specified, URL scheme and authority MUST be lowercase and include the port number; http default port 80 and https default port 443 MUST be excluded.<br>
     * <br>
     * For example, the request:<br>
     * HTTP://Example.com:80/resource?id=123<br>
     * Is included in the Signature Base String as:<br>
     * http://example.com/resource
     *
     * @param url the url to be normalized
     * @return the Signature Base String
     * @see <a href="http://oauth.net/core/1.0#rfc.section.9.1.2">OAuth Core - 9.1.2.  Construct Request URL</a>
     */
    private static String constructRequestURL(String url) {
        int index = url.indexOf("?");
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

    String generateSignature(Token accessToken, HttpRequest.Builder request, HttpParamMap params) {
        try {
            // first, sort the list without changing the one given
            SortedMap<String,List<HttpParam>> sorted = getSortedCopy(params);

            String signMeth = String.valueOf(request.getMeth());
            String signUri = constructRequestURL(request.getBaseUrl());

            String signParams = paramSerializerAmpNoQuote.serialize(sorted, CHARSET);

            // format the signature content
            String data = signMeth + "&" + encode(signUri, ENC) + "&" + encode(signParams, ENC);

            Mac mac = Mac.getInstance(SIGN_METH_4_J);
            String signature = generateSignature(accessToken.getSecret());
            mac.init(new SecretKeySpec(signature.getBytes(ENC), SIGN_METH_4_J));

            String encoded = new String(Base64.encodeToByte(mac.doFinal(data.getBytes(ENC))), ENC);
            LOGGER.debug("Signature[data=\"%s\",signature=\"%s\",result=\"%s\"]", data, signature, encoded);
            return encoded;
        } catch (Exception e) {
            throw new OAuthException(e);
        }
    }
    String generateSignature(String tokenSecret){
        return (consumerToken.getSecret() + "&" + tokenSecret);
    }


    private static final Comparator<HttpParam> HTTP_PARAM_COMPARATOR = new Comparator<HttpParam>() {
        public int compare(HttpParam o1, HttpParam o2) {
            int i = o1.getName().compareTo(o2.getName());
            return i != 0 ? i : o1.getValue().asString().compareTo(o2.getValue().asString());
        }
    };

    SortedMap<String,List<HttpParam>> getSortedCopy(HttpParamMap map){
       TreeMap<String,List<HttpParam>> sorted = new TreeMap<String,List<HttpParam>>();
       for(Map.Entry<String,List<HttpParam>> entry : map.entrySet()){
           List<HttpParam> sortedValueCopy = new ArrayList<HttpParam>(entry.getValue());
           Collections.sort(sortedValueCopy, HTTP_PARAM_COMPARATOR);
           sorted.put(entry.getKey(), sortedValueCopy);
       }
       return sorted;
    }

    static interface VariantProvider {

        String timestamp();

        String nonce();

    }

    static class DefaultVariantProvider implements VariantProvider {
        private final Random RDM = new SecureRandom();

        public String timestamp() {
            return String.valueOf(System.currentTimeMillis() / 1000l);
        }

        public String nonce() {
            return String.valueOf(System.currentTimeMillis() + RDM.nextLong());
        }
    }

}
