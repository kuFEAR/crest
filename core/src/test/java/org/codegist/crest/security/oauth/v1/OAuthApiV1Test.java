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

import org.codegist.crest.config.MethodType;
import org.codegist.crest.security.oauth.OAuthToken;
import org.junit.Test;

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


    @Test
    public void shouldGetARequestTokenUsingGET() throws Exception {
        OAuthInterface mockOAuthInterface = mockGetOAuthInterface();
        OAuthToken actual = newToTest(GET, mockOAuthInterface).getRequestToken();

        assertEquals(mockOAuthToken, actual);
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
        OAuthInterface mockOAuthInterface = mockPostOAuthInterface();
        OAuthToken actual = newToTest(POST, mockOAuthInterface).getRequestToken();

        assertEquals(mockOAuthToken, actual);
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
        OAuthInterface mockOAuthInterface = mockGetOAuthInterface();
        OAuthToken actual = newToTest(GET, mockOAuthInterface).getAccessToken(token, verifier);

        assertEquals(mockOAuthToken, actual);
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
        OAuthInterface mockOAuthInterface = mockPostOAuthInterface();
        OAuthToken actual = newToTest(POST, mockOAuthInterface).getAccessToken(token, verifier);

        assertEquals(mockOAuthToken, actual);
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
        OAuthInterface mockOAuthInterface = mockGetOAuthInterface();
        OAuthToken actual = newToTest(GET, mockOAuthInterface).refreshAccessToken(token);

        assertEquals(mockOAuthToken, actual);
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
        OAuthInterface mockOAuthInterface = mockPostOAuthInterface();
        OAuthToken actual = newToTest(POST, mockOAuthInterface).refreshAccessToken(token);

        assertEquals(mockOAuthToken, actual);
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


    private OAuthApiV1 newToTest(MethodType methodType, OAuthInterface expectedInterface){
        return new OAuthApiV1(
                methodType,
                GET_REQUEST_TOKEN_URL,
                GET_ACCESS_TOKEN_URL,
                REFRESH_ACCESS_TOKEN_URL,
                expectedInterface,
                consumerToken,
                mockVariantProvider);
    }

    private OAuthInterface mockGetOAuthInterface(){
        OAuthInterface mockOAuthInterface = mock(GetOAuthInterface.class);
        setupMockOAuthInterface(mockOAuthInterface);
        return mockOAuthInterface;
    }

    private OAuthInterface mockPostOAuthInterface(){
        OAuthInterface mockOAuthInterface = mock(PostOAuthInterface.class);
        setupMockOAuthInterface(mockOAuthInterface);
        return mockOAuthInterface;
    }

    private void setupMockOAuthInterface(OAuthInterface mockOAuthInterface){
        when(mockOAuthInterface.getRequestToken(anyString(),anyString(),anyString(),anyString(),anyString(),anyString(),anyString())).thenReturn(mockOAuthToken);
        when(mockOAuthInterface.getAccessToken(anyString(),anyString(),anyString(),anyString(),anyString(),anyString(),anyString())).thenReturn(mockOAuthToken);
        when(mockOAuthInterface.refreshAccessToken(anyString(),anyString(),anyString(),anyString(),anyString(),anyString(),anyString(),anyString())).thenReturn(mockOAuthToken);
    }


}
