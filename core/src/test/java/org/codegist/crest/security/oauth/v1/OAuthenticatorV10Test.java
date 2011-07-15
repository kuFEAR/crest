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

import org.codegist.common.net.Urls;
import org.codegist.crest.io.Response;
import org.codegist.crest.io.http.*;
import org.codegist.crest.io.http.entity.UrlEncodedFormEntityWriter;
import org.codegist.crest.security.oauth.OAuthToken;
import org.codegist.crest.security.oauth.OAuthenticator;
import org.codegist.crest.util.Encoders;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;
import static org.codegist.common.net.Urls.decode;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.*;

/**
* Test values are taken from <a href="http://oauth.net/core/1.0/">http://oauth.net/core/1.0/</a>
*
* @author Laurent Gilles (laurent.gilles@codegist.org)
*/
public class OAuthenticatorV10Test {

    private static final String OOB = "oob";
    private static final String SIGN_ALGO = "HMAC-SHA1";
    private static final String VERSION = "1.0";
    private static final String CONSUMER_TOKEN_KEY = "dpf43f3p2l4k3l03";
    private static final String CONSUMER_TOKEN_SECRET = "kd94hf93k423kf44";
    private static final String ACCESS_TOKEN_STRING = "nnch734d00sl2jdk";
    private static final String ACCESS_TOKEN_SECRET = "pfkkdhi9sl3r4s00";
    private static final String NONCE = "kllo9940pd9333jh";
    private static final String TIMESTAMP = "1191242096";
    private static final String REQUEST_TOK_URL = "http://127.0.0.1/request";
    private static final String ACCESS_TOK_URL = "http://127.0.0.1/access";
    private static final String REFRESH_TOK_URL = "http://127.0.0.1/refresh";
    private static final String  REQUEST_TOKEN_RESPONSE_TOKEN = "someToken";
    private static final String  REQUEST_TOKEN_RESPONSE_SECRET = "someSecret";
    private static final String  REQUEST_TOKEN_RESPONSE_EXTRA1_NAME = "oauth_extra1";
    private static final String  REQUEST_TOKEN_RESPONSE_EXTRA2_NAME = "oauth_extra2";
    private static final String  REQUEST_TOKEN_RESPONSE_EXTRA1 = "extra1";
    private static final String  REQUEST_TOKEN_RESPONSE_EXTRA2 = "extra2";
    private static final String REQUEST_TOKEN_RESPONSE = format("oauth_token=%s&oauth_token_secret=%s&%s=%s&%s=%s", REQUEST_TOKEN_RESPONSE_TOKEN, REQUEST_TOKEN_RESPONSE_SECRET, REQUEST_TOKEN_RESPONSE_EXTRA1_NAME, REQUEST_TOKEN_RESPONSE_EXTRA1, REQUEST_TOKEN_RESPONSE_EXTRA2_NAME, REQUEST_TOKEN_RESPONSE_EXTRA2);
    private static final OAuthToken CONSUMER_TOKEN = new OAuthToken(CONSUMER_TOKEN_KEY, CONSUMER_TOKEN_SECRET);
    private static final OAuthToken ACCESS_TOKEN = new OAuthToken(ACCESS_TOKEN_STRING, ACCESS_TOKEN_SECRET);
    private static final VariantProvider VARIANT_PROVIDER_STUB = new VariantProviderStub(NONCE, TIMESTAMP);

    private final HttpRequestExecutor mockHttpRequestExecutor = mock(HttpRequestExecutor.class);
    private final Map<String,Object> crestProperties = new HashMap<String, Object>();
    private final OAuthenticatorV1 toTest = newToTest();

    @Test(expected = IllegalArgumentException.class)
    public void testGetRequestOAuthTokenMissingParams() throws Exception {
        toTest.getRequestToken();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRefreshOAuthTokenMissingParams() throws Exception  {
        toTest.refreshAccessToken(new OAuthToken("a","b"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAccessOAuthTokenMissingParams() throws Exception {
        toTest.getAccessToken(new OAuthToken("a","b"), "123");
    }

    @Test
    public void shouldGetARequestTokenUsingGET() throws Exception {
        shouldGetARequestTokenUsing(HttpMethod.GET, "6TtAwFMnByClAdAxmA+feRdrtxA=");
    }
    @Test
    public void shouldGetARequestTokenUsingPOST() throws Exception {
        shouldGetARequestTokenUsing(HttpMethod.POST, "KUkl3Z4v1zbpjyjtKdQ81nzWlkg=");
    }

    private void shouldGetARequestTokenUsing(HttpMethod method, String expectedSignature) throws Exception {
        HttpResponse response = mock(HttpResponse.class);
        when(response.deserializeTo(String.class)).thenReturn(REQUEST_TOKEN_RESPONSE);
        when(mockHttpRequestExecutor.execute(any(HttpRequest.class))).thenReturn(response);
        OAuthenticatorV1 toTest = newToTest(getRequestTokenProps(method));

        OAuthToken actual = toTest.getRequestToken();

        ArgumentCaptor<HttpRequest> requestCaptor = forClass(HttpRequest.class);
        verify(mockHttpRequestExecutor).execute(requestCaptor.capture());
        HttpRequest actualRequest = requestCaptor.getValue();

        List<HttpParam> params = null;
        switch(method){
            case GET:
                params=actualRequest.getQueryParams();
                break;
            case POST:
                params=actualRequest.getFormParams();
                break;
            default:
                fail();
        }
        assertExpectations(params, actual, actualRequest, method,expectedSignature);
    }

    private void assertExpectations(List<HttpParam> params, OAuthToken actual, HttpRequest actualRequest, HttpMethod expectedMethod, String expectedSignature) throws Exception {
        assertNotNull(actual);
        assertEquals(REQUEST_TOKEN_RESPONSE_TOKEN, actual.getToken());
        assertEquals(REQUEST_TOKEN_RESPONSE_SECRET, actual.getSecret());
        assertEquals(REQUEST_TOKEN_RESPONSE_EXTRA1_NAME, actual.getAttribute(REQUEST_TOKEN_RESPONSE_EXTRA1_NAME).getName());
        assertEquals(REQUEST_TOKEN_RESPONSE_EXTRA1, actual.getAttribute(REQUEST_TOKEN_RESPONSE_EXTRA1_NAME).getValue());
        assertEquals(REQUEST_TOKEN_RESPONSE_EXTRA2_NAME, actual.getAttribute(REQUEST_TOKEN_RESPONSE_EXTRA2_NAME).getName());
        assertEquals(REQUEST_TOKEN_RESPONSE_EXTRA2, actual.getAttribute(REQUEST_TOKEN_RESPONSE_EXTRA2_NAME).getValue());

        assertEquals(REQUEST_TOK_URL, actualRequest.getPathBuilder().build());
        assertEquals(expectedMethod, actualRequest.getHttpMethod());
        assertEquals(CONSUMER_TOKEN_KEY, getParamValue(params, "oauth_consumer_key"));
        assertEquals(SIGN_ALGO, getParamValue(params, "oauth_signature_method"));
        assertEquals(TIMESTAMP, getParamValue(params, "oauth_timestamp"));
        assertEquals(NONCE, getParamValue(params, "oauth_nonce"));
        assertEquals(VERSION, getParamValue(params, "oauth_version"));
        assertEquals(OOB, getParamValue(params, "oauth_callback"));
        assertEquals(expectedSignature, decode(getParamValue(params, "oauth_signature"), "utf-8"));
    }
//    @Test
//    public void testGetAccessOAuthTokenGET() throws IOException, URISyntaxException {
//        RestService restService = mock(RestService.class);
//        Map<String,Object> config = crestProperties{{
//            put(OAuthenticatorV1.CONFIG_OAuthToken_ACCESS_URL, ACCESS_TOK_URL);
//            put(OAuthenticatorV1.CONFIG_OAuthToken_ACCESS_URL_METHOD, "GET");
//        }};
//        HttpResponse response = mock(HttpResponse.class);
//        when(response.asString()).thenReturn("");
//        when(restService.exec(any(HttpRequest.class))).thenReturn(response);
//        OAuthenticator oauth = new OAuthenticatorV1(restService, CONSUMER_TOKEN, config, mockVariantProvider);
//        oauth.getAccessOAuthToken(new OAuthToken("abc","cde"), "123");
//        verify(restService).exec(argThat(new HttpRequestMatcher(new HttpRequest.Builder(ACCESS_TOK_URL, new UrlEncodedFormEntityWriter())
//                .using("GET")
//                .addQueryParam("oauth_consumer_key","dpf43f3p2l4k3l03")
//                .addQueryParam("oauth_signature_method","HMAC-SHA1")
//                .addQueryParam("oauth_timestamp","1191242096")
//                .addQueryParam("oauth_nonce","kllo9940pd9333jh")
//                .addQueryParam("oauth_version","1.0")
//                .addQueryParam("oauth_OAuthToken","abc")
//                .addQueryParam("oauth_verifier","123")
//                .addQueryParam("oauth_signature","3hSAQLbH48EoF/DCakzqqixn3q0=")
//                .build())));
//    }
//    @Test
//    public void testGetAccessOAuthTokenPOST() throws IOException, URISyntaxException {
//        RestService restService = mock(RestService.class);
//        Map<String,Object> config = crestProperties{{
//            put(OAuthenticatorV1.CONFIG_OAuthToken_ACCESS_URL, ACCESS_TOK_URL);
//            put(OAuthenticatorV1.CONFIG_OAuthToken_ACCESS_URL_METHOD, "POST");
//        }};
//        HttpResponse response = mock(HttpResponse.class);
//        when(response.asString()).thenReturn("");
//        when(restService.exec(any(HttpRequest.class))).thenReturn(response);
//        OAuthenticator oauth = new OAuthenticatorV1(restService, CONSUMER_TOKEN, config, mockVariantProvider);
//        oauth.getAccessOAuthToken(new OAuthToken("abc","cde"), "123");
//        verify(restService).exec(argThat(new HttpRequestMatcher(new HttpRequest.Builder(ACCESS_TOK_URL, new UrlEncodedFormEntityWriter())
//                .using("POST")
//                .addFormParam("oauth_consumer_key","dpf43f3p2l4k3l03")
//                .addFormParam("oauth_signature_method","HMAC-SHA1")
//                .addFormParam("oauth_timestamp","1191242096")
//                .addFormParam("oauth_nonce","kllo9940pd9333jh")
//                .addFormParam("oauth_version","1.0")
//                .addFormParam("oauth_OAuthToken","abc")
//                .addFormParam("oauth_verifier","123")
//                .addFormParam("oauth_signature","x8U4ouTFJDV+ITR5zjxw6HLZekI=")
//                .build())));
//    }
//    @Test
//    public void testRefreshAccessOAuthTokenGET() throws IOException, URISyntaxException {
//        RestService restService = mock(RestService.class);
//        Map<String,Object> config = crestProperties{{
//            put(OAuthenticatorV1.CONFIG_OAuthToken_ACCESS_REFRESH_URL, REFRESH_TOK_URL);
//            put(OAuthenticatorV1.CONFIG_OAuthToken_ACCESS_REFRESH_URL_METHOD, "GET");
//        }};
//        HttpResponse response = mock(HttpResponse.class);
//        when(response.asString()).thenReturn("");
//        when(restService.exec(any(HttpRequest.class))).thenReturn(response);
//        OAuthenticator oauth = new OAuthenticatorV1(restService, CONSUMER_TOKEN, config, mockVariantProvider);
//        oauth.refreshAccessOAuthToken(new OAuthToken("abc","cde", new HashMap<String, String>(){{put("extra","456");}}), "extra");
//        verify(restService).exec(argThat(new HttpRequestMatcher(new HttpRequest.Builder(REFRESH_TOK_URL, new UrlEncodedFormEntityWriter())
//                .using("GET")
//                .addQueryParam("oauth_consumer_key","dpf43f3p2l4k3l03")
//                .addQueryParam("oauth_signature_method","HMAC-SHA1")
//                .addQueryParam("oauth_timestamp","1191242096")
//                .addQueryParam("oauth_nonce","kllo9940pd9333jh")
//                .addQueryParam("oauth_version","1.0")
//                .addQueryParam("oauth_OAuthToken","abc")
//                .addQueryParam("extra","456")
//                .addQueryParam("oauth_signature","CLLlJYMnogkO3e1Z4OKnjcYaxSg=")
//                .build())));
//    }
//    @Test
//    public void testRefreshAccessOAuthTokenPOST() throws IOException, URISyntaxException {
//        RestService restService = mock(RestService.class);
//        Map<String,Object> config = crestProperties{{
//            put(OAuthenticatorV1.CONFIG_OAuthToken_ACCESS_REFRESH_URL, REFRESH_TOK_URL);
//            put(OAuthenticatorV1.CONFIG_OAuthToken_ACCESS_REFRESH_URL_METHOD, "POST");
//        }};
//        HttpResponse response = mock(HttpResponse.class);
//        when(response.asString()).thenReturn("");
//        when(restService.exec(any(HttpRequest.class))).thenReturn(response);
//        OAuthenticator oauth = new OAuthenticatorV1(restService, CONSUMER_TOKEN, config, mockVariantProvider);
//        oauth.refreshAccessOAuthToken(new OAuthToken("abc","cde", new HashMap<String, String>(){{put("extra","456");}}), "extra");
//        verify(restService).exec(argThat(new HttpRequestMatcher(new HttpRequest.Builder(REFRESH_TOK_URL, new UrlEncodedFormEntityWriter())
//                .using("POST")
//                .addFormParam("oauth_consumer_key","dpf43f3p2l4k3l03")
//                .addFormParam("oauth_signature_method","HMAC-SHA1")
//                .addFormParam("oauth_timestamp","1191242096")
//                .addFormParam("oauth_nonce","kllo9940pd9333jh")
//                .addFormParam("oauth_version","1.0")
//                .addFormParam("oauth_OAuthToken","abc")
//                .addFormParam("extra","456")
//                .addFormParam("oauth_signature","Ayymy4Mxku5qVba67IuyKWEZ8Zw=")
//                .build())));
//    }
//
//    @Test
//    public void testGenerateSignature() {
//        OAuthenticatorV1 oauth = new OAuthenticatorV1(mockHttpRequestExecutor, new OAuthToken("OAuthToken", "djr9rjt0jd78jf88"));
//        assertEquals("djr9rjt0jd78jf88&jjd999tj88uiths3", oauth.generateSignature("jjd999tj88uiths3"));
//        assertEquals("djr9rjt0jd78jf88&jjd99$tj88uiths3", oauth.generateSignature("jjd99$tj88uiths3"));
//        assertEquals("djr9rjt0jd78jf88&", oauth.generateSignature(""));
//    }
//
//
//    /**
//     * Test with values from <a href="http://oauth.net/core/1.0/#anchor30">http://oauth.net/core/1.0/#anchor30</a>
//     */
//    @Test
//    public void testAuthentificationHeaders() throws URISyntaxException, MalformedURLException, UnsupportedEncodingException {
//        HashMap<String, Object> config = crestProperties {{
//            put(OAuthenticatorV1.CONFIG_OAUTH_PARAM_DEST, "header");
//        }};
//        OAuthenticator oauth = new OAuthenticatorV1(mockHttpRequestExecutor, CONSUMER_TOKEN, config, mockVariantProvider);
//        HttpRequest.Builder requestBuilder = new HttpRequest.Builder("http://photos.example.net/photos", new UrlEncodedFormEntityWriter())
//                .addQueryParam("file", "vacation.jpg")
//                .addQueryParam("size", "original");
//
//        oauth.sign(ACCESS_TOKEN, requestBuilder);
//        HttpRequest io = requestBuilder.build();
//
//
//        assertNotNull(io.getHeaderParams());
//        assertEquals(2, io.getHeaderParams().size());
//        assertNotNull(io.getHeaderParamMap().get("Authorization"));
//        assertEquals("http://photos.example.net/photos?file=vacation.jpg&size=original", io.getUrl());
//        assertEquals("OAuth oauth_consumer_key=\"dpf43f3p2l4k3l03\",oauth_signature_method=\"HMAC-SHA1\",oauth_timestamp=\"1191242096\",oauth_nonce=\"kllo9940pd9333jh\",oauth_version=\"1.0\",oauth_OAuthToken=\"nnch734d00sl2jdk\",oauth_signature=\"tR3%2BTy81lMeYAr%2FFid0kMTYa%2FWM%3D\"", io.getHeaderParamMap().get("Authorization").get(0).getValue().asString());
//    }
//
//    /**
//     * Test with values from <a href="http://oauth.net/core/1.0/#anchor30">http://oauth.net/core/1.0/#anchor30</a>
//     */
//    @Test
//    public void testAuthentificationQueryString() throws URISyntaxException, MalformedURLException, UnsupportedEncodingException {
//        Map<String, Object> config = crestProperties {{
//            put(OAuthenticatorV1.CONFIG_OAUTH_PARAM_DEST, "url");
//        }};
//        OAuthenticator oauth = new OAuthenticatorV1(mockHttpRequestExecutor, CONSUMER_TOKEN, config, mockVariantProvider);
//        HttpRequest.Builder requestBuilder = new HttpRequest.Builder("http://photos.example.net/photos", new UrlEncodedFormEntityWriter())
//                .addQueryParam("file", "vacation.jpg")
//                .addQueryParam("size", "original");
//
//        oauth.sign(ACCESS_TOKEN, requestBuilder);
//        HttpRequest io = requestBuilder.build();
//
//
//        assertNotNull(io.getHeaderParams());
//        assertEquals(1, io.getHeaderParams().size());
//        assertEquals("http://photos.example.net/photos?file=vacation.jpg&size=original&oauth_consumer_key=dpf43f3p2l4k3l03&oauth_signature_method=HMAC-SHA1&oauth_timestamp=1191242096&oauth_nonce=kllo9940pd9333jh&oauth_version=1.0&oauth_OAuthToken=nnch734d00sl2jdk&oauth_signature=tR3%2BTy81lMeYAr%2FFid0kMTYa%2FWM%3D", io.getUrl());
//    }



    private static Map<String,Object> getRequestTokenProps(HttpMethod method){
        Map<String,Object> props = new HashMap<String, Object>();
        props.put(OAuthenticatorV1.CONFIG_TOKEN_REQUEST_URL, REQUEST_TOK_URL);
        props.put(OAuthenticatorV1.CONFIG_TOKEN_REQUEST_URL_METHOD, method.name());
        return props;
    }

    private OAuthenticatorV1 newToTest(){
        return newToTest(Collections.<String, Object>emptyMap());
    }

    private OAuthenticatorV1 newToTest(Map<String,Object> crestProperties){
        Map<String,Object> props = new HashMap<String, Object>(this.crestProperties);
        props.putAll(crestProperties);
        return new OAuthenticatorV1(mockHttpRequestExecutor, CONSUMER_TOKEN, props, VARIANT_PROVIDER_STUB);
    }

    private static class VariantProviderStub implements VariantProvider {
        private final String nonce;
        private final String timestamp;

        private VariantProviderStub(String nonce, String timestamp) {
            this.nonce = nonce;
            this.timestamp = timestamp;
        }

        public String timestamp() {
            return timestamp;
        }

        public String nonce() {
            return nonce;
        }
    }

    private static String getParamValue(List<HttpParam> params, String name){
        for(HttpParam param : params){
            if(name.equals(param.getConfig().getName())) {
                assertEquals(1, param.getValue().size());
                return param.getValue().iterator().next().toString();
            }
        }
        return null;
    }
}
