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

import org.codegist.crest.CRestBuilder;
import org.codegist.crest.io.http.HttpChannelFactory;
import org.codegist.crest.security.oauth.OAuthToken;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;

import static org.codegist.crest.config.MethodType.GET;
import static org.codegist.crest.config.MethodType.POST;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class OAuthApiV1Test extends OAuthTest {

    private final OAuthToken mockOAuthToken = mock(OAuthToken.class);
    private static final String sessionHandle = "dgoidhgodhgodfuhgou";
    private final OAuthToken token = new OAuthToken("sdfsg978687df6g","d876gdf87g8", Collections.singletonMap("oauth_session_handle", sessionHandle));
    private final String verifier = "gdfg87fdg9";
    private final OAuthInterface mockOAuthInterface = mock(OAuthInterface.class);
    private final CRestBuilder mockCRestBuilder = mock(CRestBuilder.class);
    private final HttpChannelFactory mockHttpChannelFactory = mock(HttpChannelFactory.class);

    private final OAuthApiV1Builder toTestBuilder = new OAuthApiV1Builder(consumerToken, mockCRestBuilder)
            .getAccessTokenFrom(GET_ACCESS_TOKEN_URL)
            .getRequestTokenFrom(GET_REQUEST_TOKEN_URL)
            .refreshAccessTokenFrom(REFRESH_ACCESS_TOKEN_URL)
            .using(mockHttpChannelFactory)
            .using(mockVariantProviderStub);

    @Test
    public void shouldGetARequestTokenUsingGET() throws Exception {
        OAuthToken actual = toTestBuilder.using(GET).build().getRequestToken();

        assertEquals(mockOAuthToken, actual);
        verify(mockCRestBuilder).build(GetOAuthInterface.class);
        verify(mockOAuthInterface).getRequestToken(consumerKey,
                signatureMethod,
                timestamp,
                nonce,
                version,
                "oob",
                "YZnn5PUIwRIvEaRAIYjQb5xIjho%3D");
    }

    @Test
    public void shouldGetARequestTokenUsingPOST() throws Exception {
        OAuthToken actual = toTestBuilder.using(POST).build().getRequestToken();

        assertEquals(mockOAuthToken, actual);
        verify(mockCRestBuilder).build(PostOAuthInterface.class);
        verify(mockOAuthInterface).getRequestToken(consumerKey,
                signatureMethod,
                timestamp,
                nonce,
                version,
                "oob",
                "B4SlbjK%2FDDdvKtP1YQSH1CI61xs%3D");
    }


    @Test
    public void shouldGetAnAccessTokenUsingGET() throws Exception {
        OAuthToken actual = toTestBuilder.using(GET).build().getAccessToken(token, verifier);

        assertEquals(mockOAuthToken, actual);
        verify(mockCRestBuilder).build(GetOAuthInterface.class);
        verify(mockOAuthInterface).getAccessToken(
                token.getToken(),
                signatureMethod,
                timestamp,
                nonce,
                version,
                verifier,
                "rcFomzZTgPWGoiQov%2FNHOa0mvwE%3D");
    }


    @Test
    public void shouldGetAnAccessTokenUsingPOST() throws Exception {
        OAuthToken actual = toTestBuilder.using(POST).build().getAccessToken(token, verifier);

        assertEquals(mockOAuthToken, actual);
        verify(mockCRestBuilder).build(PostOAuthInterface.class);
        verify(mockOAuthInterface).getAccessToken(
                token.getToken(),
                signatureMethod,
                timestamp,
                nonce,
                version,
                verifier,
                "lkywwGVYjBtyiNRjiulpfRK2Qoo%3D");
    }


    @Test
    public void shouldRefreshAnAccessTokenUsingGET() throws Exception {
        OAuthToken actual = toTestBuilder.using(GET).build().refreshAccessToken(token);

        assertEquals(mockOAuthToken, actual);
        verify(mockCRestBuilder).build(GetOAuthInterface.class);
        verify(mockOAuthInterface).refreshAccessToken(
                token.getToken(),
                consumerKey,
                signatureMethod,
                timestamp,
                nonce,
                version,
                sessionHandle,
                "Vy4e1G1RP2XrIQ7MVS2atACp%2FWc%3D");
    }


    @Test
    public void shouldRefreshAnAccessTokenUsingPOST() throws Exception {
        OAuthToken actual = toTestBuilder.using(POST).build().refreshAccessToken(token);

        assertEquals(mockOAuthToken, actual);
        verify(mockCRestBuilder).build(PostOAuthInterface.class);
        verify(mockOAuthInterface).refreshAccessToken(
                token.getToken(),
                consumerKey,
                signatureMethod,
                timestamp,
                nonce,
                version,
                sessionHandle,
                "hZmlZPXeBOZAF1oXM%2FR5IUOqPm4%3D");
    }



    @Before
    public void setupMocks() throws IOException {
        when(mockCRestBuilder.placeholder(anyString(), anyString())).thenReturn(mockCRestBuilder);
        when(mockCRestBuilder.setHttpChannelFactory(any(HttpChannelFactory.class))).thenReturn(mockCRestBuilder);
        when(mockCRestBuilder.build((Class<?>)anyObject())).thenReturn(mockOAuthInterface);

        when(mockOAuthInterface.getRequestToken(anyString(),anyString(),anyString(),anyString(),anyString(),anyString(),anyString())).thenReturn(mockOAuthToken);
        when(mockOAuthInterface.getAccessToken(anyString(),anyString(),anyString(),anyString(),anyString(),anyString(),anyString())).thenReturn(mockOAuthToken);
        when(mockOAuthInterface.refreshAccessToken(anyString(),anyString(),anyString(),anyString(),anyString(),anyString(),anyString(),anyString())).thenReturn(mockOAuthToken);
    }

    @After
    public void verifyMocks() throws IOException {
        verify(mockCRestBuilder).placeholder("oauth.access-token-path", GET_ACCESS_TOKEN_URL);
        verify(mockCRestBuilder).placeholder("oauth.request-token-path", GET_REQUEST_TOKEN_URL);
        verify(mockCRestBuilder).placeholder("oauth.refresh-access-token-path", REFRESH_ACCESS_TOKEN_URL);
        verify(mockCRestBuilder).setHttpChannelFactory(mockHttpChannelFactory);
    }

}
