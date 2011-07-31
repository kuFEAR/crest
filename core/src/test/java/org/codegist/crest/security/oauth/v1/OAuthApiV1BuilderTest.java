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
import org.codegist.crest.config.MethodType;
import org.codegist.crest.io.http.HttpChannelFactory;
import org.codegist.crest.io.http.platform.HttpURLConnectionHttpChannelFactory;
import org.codegist.crest.security.oauth.OAuthToken;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(OAuthApiV1Builder.class)
public class OAuthApiV1BuilderTest {

    private static final OAuthToken token = mock(OAuthToken.class);
    private static final CRestBuilder mockCRestBuilder = mock(CRestBuilder.class);

    private final OAuthApiV1Builder toTest = new OAuthApiV1Builder(token, mockCRestBuilder);

    @Test
    public void shouldUseDefaults() throws Exception {
        FormOAuthInterface expectedOAuthInterface = mock(FormOAuthInterface.class);
        when(mockCRestBuilder.build(FormOAuthInterface.class)).thenReturn(expectedOAuthInterface);

        OAuthApiV1 mockBuilderResult = PowerMockito.mock(OAuthApiV1.class);
        whenNew(OAuthApiV1.class)
                .withArguments(MethodType.POST, "", "", "", expectedOAuthInterface, token, DefaultVariantProvider.INSTANCE)
                .thenReturn(mockBuilderResult);

        OAuthApiV1 actual = toTest.build();
        assertEquals(mockBuilderResult, actual);
        verify(mockCRestBuilder).placeholder("oauth.access-token-path", "");
        verify(mockCRestBuilder).placeholder("oauth.request-token-path", "");
        verify(mockCRestBuilder).placeholder("oauth.refresh-access-token-path", "");
        verify(mockCRestBuilder).setHttpChannelFactory(any(HttpURLConnectionHttpChannelFactory.class));
    }
    
    @Test
    public void shouldUseProvidedValues() throws Exception {
        String accessTokenUrl = "a";
        String requestTokenUrl = "b";
        String refreshTokenUrl = "c";
        MethodType methodType = MethodType.GET;
        HttpChannelFactory mockChannelFactory = mock(HttpChannelFactory.class);
        VariantProvider mockVariantProvider = mock(VariantProvider.class);
        QueryOAuthInterface expectedOAuthInterface = mock(QueryOAuthInterface.class);
        when(mockCRestBuilder.build(QueryOAuthInterface.class)).thenReturn(expectedOAuthInterface);

        OAuthApiV1 mockBuilderResult = PowerMockito.mock(OAuthApiV1.class);
        whenNew(OAuthApiV1.class)
                .withArguments(methodType, requestTokenUrl, accessTokenUrl, refreshTokenUrl, expectedOAuthInterface, token, mockVariantProvider)
                .thenReturn(mockBuilderResult);

        OAuthApiV1 actual = toTest
                .getRequestTokenFrom(requestTokenUrl)
                .getAccessTokenFrom(accessTokenUrl)
                .refreshAccessTokenFrom(refreshTokenUrl)
                .useGet()
                .using(mockChannelFactory)
                .using(mockVariantProvider)
                .build();
        assertEquals(mockBuilderResult, actual);
        verify(mockCRestBuilder).placeholder("oauth.access-token-path", accessTokenUrl);
        verify(mockCRestBuilder).placeholder("oauth.request-token-path", requestTokenUrl);
        verify(mockCRestBuilder).placeholder("oauth.refresh-access-token-path", refreshTokenUrl);
        verify(mockCRestBuilder).setHttpChannelFactory(mockChannelFactory);
    }

    @Before
    public void setupMocks() throws IOException {
        when(mockCRestBuilder.placeholder(anyString(), anyString())).thenReturn(mockCRestBuilder);
        when(mockCRestBuilder.setHttpChannelFactory(any(HttpChannelFactory.class))).thenReturn(mockCRestBuilder);
    }

}
