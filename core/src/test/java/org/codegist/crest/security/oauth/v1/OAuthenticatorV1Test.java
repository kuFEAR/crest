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
import org.junit.Test;

import java.util.List;

import static org.codegist.crest.config.MethodType.GET;
import static org.codegist.crest.config.MethodType.POST;

/**
 * @author laurent.gilles@codegist.org
 */
public class OAuthenticatorV1Test extends OAuthTest {


    private final OAuthenticatorV1 toTest = new OAuthenticatorV1(consumerToken, mockVariantProviderStub);

    @Test
    public void shouldSignWithNoParametersUsingPOST() throws Exception {
        List<EncodedPair> actualPairs = toTest.oauth(accessToken, POST, URL_NO_QUERY);
        assertExpectedOAuthPairs("u1OEdJRPW3l/KpXj+/2kR9fPM98=", actualPairs);
    }

    @Test
    public void shouldSignWithNoParametersUsingGET() throws Exception {
        List<EncodedPair> actualPairs = toTest.oauth(accessToken, GET, URL_NO_QUERY);
        assertExpectedOAuthPairs("ZDHmQuapd0+WSUeoRxqf0D5L3wo=", actualPairs);
    }

    @Test
    public void shouldSignWithParametersUsingPOST() throws Exception {
        List<EncodedPair> actualPairs = toTest.oauth(accessToken, POST, URL_NO_QUERY, PAIRS);
        assertExpectedOAuthPairs("GsNK9QyHNkxAFC+I9AWQ5glonkc=", actualPairs);
    }

    @Test
    public void shouldSignWithParametersUsingGET() throws Exception {
        List<EncodedPair> actualPairs = toTest.oauth(accessToken, GET, URL_NO_QUERY, PAIRS);
        assertExpectedOAuthPairs("0XLRn8ZZdVOxrCy62dzsjD+GOjg=", actualPairs);
    }
}
