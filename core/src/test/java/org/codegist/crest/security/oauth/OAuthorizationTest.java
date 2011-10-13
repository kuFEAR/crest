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

package org.codegist.crest.security.oauth;

import org.codegist.crest.config.MethodType;
import org.codegist.crest.param.EncodedPair;
import org.codegist.crest.security.AuthorizationToken;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.codegist.crest.config.MethodType.GET;
import static org.codegist.crest.util.Pairs.toPreEncodedPair;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class OAuthorizationTest {

    private final OAuthToken mockToken = mock(OAuthToken.class);
    private final OAuthenticator mockOAuthenticator = mock(OAuthenticator.class);
    private final OAuthApi mockOAuthApi = mock(OAuthApi.class);
    private final OAuthorization toTest = new OAuthorization(mockToken, mockOAuthenticator, mockOAuthApi);

    @Test
    public void shouldGetAnAuthorizationTokenBasedOnOAuthenticatorResult() throws Exception {
        List<EncodedPair> pairs = asList(
                toPreEncodedPair("a","a%20val"),
                toPreEncodedPair("b","b%20val")
        );
        when(mockOAuthenticator.oauth(any(OAuthToken.class), any(MethodType.class), anyString(), any(EncodedPair[].class))).thenReturn(pairs);

        MethodType argMethodType = GET;
        String argUrl = "url";
        EncodedPair argPair = toPreEncodedPair("c","c%20val");

        AuthorizationToken actual = toTest.authorize(argMethodType, argUrl, argPair);

        assertNotNull(actual);
        assertEquals("OAuth", actual.getName());
        assertEquals("a=\"a%20val\",b=\"b%20val\"", actual.getValue());
        verify(mockOAuthenticator).oauth(mockToken, argMethodType, argUrl, argPair);
    }

    @Test
    public void shouldRefreshInnerAccessTokenUsingOAuthApiResult() throws Exception {
        OAuthToken refreshedToken = mock(OAuthToken.class);
        when(mockOAuthApi.refreshAccessToken(any(OAuthToken.class))).thenReturn(refreshedToken);

        toTest.refresh();

        verify(mockOAuthApi).refreshAccessToken(mockToken);

        // run it another time to assert next time the refreshed token has been used
        toTest.refresh();
        
        verify(mockOAuthApi).refreshAccessToken(refreshedToken);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowAnIllegalStateExceptionWhenTryingToRefreshTokenIfNoOAuthApiHasBeenPassed() throws Exception {
        OAuthorization toTest = new OAuthorization(mockToken, mockOAuthenticator);
        toTest.refresh();
    }
}
