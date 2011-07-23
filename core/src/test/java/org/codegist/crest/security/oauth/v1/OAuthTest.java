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

import org.codegist.crest.param.EncodedPair;
import org.codegist.crest.param.SimpleEncodedPair;
import org.codegist.crest.security.oauth.OAuthToken;
import org.junit.After;
import org.junit.Before;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.codegist.common.net.Urls.decode;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public abstract class OAuthTest {

    protected final VariantProvider mockVariantProvider = mock(VariantProvider.class);

    public static final Charset UTF_8 = Charset.forName("utf-8");
    public static final String GET_REQUEST_TOKEN_URL = "http://some-host:8182/get/request-token";
    public static final String GET_ACCESS_TOKEN_URL = "http://some-host:8182/get/access-token";
    public static final String REFRESH_ACCESS_TOKEN_URL = "http://some-host:8182/refresh/access-token";
    public static final String URL_NO_QUERY = "http://some-host:8182/some/path";

    public static final EncodedPair[] PAIRS = {new SimpleEncodedPair("some", "query2"),new SimpleEncodedPair("string", "for%20test"),new SimpleEncodedPair("some", "query1")};

    public static final String consumerKey= "dpf43f3p2l4k3l03";
    public static final String consumerSecret= "kd94hf93k423kf44";

    public static final String accessTokenKey= "nnch734d00sl2jdk";
    public static final String accessTokenSecret= "pfkkdhi9sl3r4s00";
    public static final String signatureMethod = "HMAC-SHA1";
    public static final String version = "1.0";
    public static final OAuthToken consumerToken = new OAuthToken(consumerKey, consumerSecret);
    public static final OAuthToken accessToken = new OAuthToken(accessTokenKey, accessTokenSecret);

    public static final String nonce= "kllo9940pd9333jh";
    public static final String timestamp= "1191242096";


    @Before
    public void setupVariantProvider(){
        when(mockVariantProvider.nonce()).thenReturn(nonce);
        when(mockVariantProvider.timestamp()).thenReturn(timestamp);
    }

    @After
    public void verifyVariantProvider(){
        verify(mockVariantProvider).nonce();
        verify(mockVariantProvider).timestamp();
    }


    public static Map<String, String> asDecodedParamMap(List<EncodedPair> pairs) {
        Map<String, String> map = new HashMap<String, String>();
        for(EncodedPair pair : pairs){
            map.put(decode(pair.getName(), UTF_8), decode(pair.getValue(), UTF_8));
        }
        return map;
    }



    public static void assertExpectedOAuthPairs(String expectedSignature, List<EncodedPair> actualPairs){
        Map<String, String> pairs = asDecodedParamMap(actualPairs);
        assertEquals(7, actualPairs.size());
        assertEquals(expectedSignature, pairs.get("oauth_signature"));
        assertEquals(version, pairs.get("oauth_version"));
        assertEquals(consumerKey, pairs.get("oauth_consumer_key"));
        assertEquals(accessTokenKey, pairs.get("oauth_token"));
        assertEquals(timestamp, pairs.get("oauth_timestamp"));
        assertEquals(nonce, pairs.get("oauth_nonce"));
        assertEquals(signatureMethod, pairs.get("oauth_signature_method"));
    }
}
