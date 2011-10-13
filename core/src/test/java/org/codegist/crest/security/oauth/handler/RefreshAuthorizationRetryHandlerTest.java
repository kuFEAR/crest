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

package org.codegist.crest.security.oauth.handler;

import org.codegist.crest.CRestConfig;
import org.codegist.crest.io.RequestException;
import org.codegist.crest.io.Response;
import org.codegist.crest.security.Authorization;
import org.codegist.crest.security.handler.RefreshAuthorizationRetryHandler;
import org.codegist.crest.test.util.CRestConfigs;
import org.junit.Before;
import org.junit.Test;

import static org.codegist.crest.security.handler.RefreshAuthorizationRetryHandler.UNAUTHORIZED_STATUS_CODE_PROP;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class RefreshAuthorizationRetryHandlerTest {

    private static final int UNAUTHORIZED_STATUS_CODE = 567;
    private final Authorization mockAuthorization = mock(Authorization.class);
    private final RequestException mockRequestException = mock(RequestException.class);
    private final Response mockResponse = mock(Response.class);

    private final CRestConfig mockCRestConfig = CRestConfigs.mockDefaultBehavior();

    @Before
    public void setupMocks(){
        when(mockCRestConfig.<Object>get(UNAUTHORIZED_STATUS_CODE_PROP)).thenReturn(UNAUTHORIZED_STATUS_CODE);
        when(mockCRestConfig.<Object>get(Authorization.class)).thenReturn(mockAuthorization);
    }

    @Test
    public void shouldNotRetryIndependentlyFromRequestExceptionContentAsDefaultMaxIsOne() throws Exception {
        assertFalse(new RefreshAuthorizationRetryHandler(mockCRestConfig).retry(mockRequestException, 3));
    }

    @Test
    public void shouldRetryAndRefreshUpUntilMaxWhenRequestExceptionDoHaveAResponseWithAnUnauthorizedStatus() throws Exception {
        when(mockCRestConfig.getMaxAttempts()).thenReturn(5);
        RefreshAuthorizationRetryHandler toTest = new RefreshAuthorizationRetryHandler(mockCRestConfig);
        shouldRetryUpUntilMaxWhenRequestExceptionDoHaveAResponse(toTest, 6, UNAUTHORIZED_STATUS_CODE);
        verify(mockAuthorization, times(5)).refresh();
    }

    @Test
    public void shouldRetryButDontRefreshUpUntilMaxWhenRequestExceptionDoHaveAResponseRefreshAuthorizationRetryHandlerWithAStatusDifferentThanUnauthorized() throws Exception {
        when(mockCRestConfig.getMaxAttempts()).thenReturn(5);
        RefreshAuthorizationRetryHandler toTest = new RefreshAuthorizationRetryHandler(mockCRestConfig);
        shouldRetryUpUntilMaxWhenRequestExceptionDoHaveAResponse(toTest, 6, 123);
        verify(mockAuthorization, never()).refresh();
    }

    @Test
    public void shouldRetryButDontRefreshUpUntilMaxWhenRequestExceptionDoesNotHaveAResponse() throws Exception {
        when(mockCRestConfig.getMaxAttempts()).thenReturn(5);
        RefreshAuthorizationRetryHandler toTest = new RefreshAuthorizationRetryHandler(mockCRestConfig);
        when(mockRequestException.hasResponse()).thenReturn(false);
        assertTrue(toTest.retry(mockRequestException, 6));
        assertFalse(toTest.retry(mockRequestException, 7));
        verify(mockAuthorization, never()).refresh();
    }

    private void shouldRetryUpUntilMaxWhenRequestExceptionDoHaveAResponse(RefreshAuthorizationRetryHandler toTest, int max, int status) throws Exception {
        when(mockRequestException.hasResponse()).thenReturn(true);
        when(mockRequestException.getResponse()).thenReturn(mockResponse);
        when(mockResponse.getStatusCode()).thenReturn(status);

        for(int i = 2/* starts at 2*/; i <= max; i++){
            assertTrue(toTest.retry(mockRequestException, i));
        }
        assertFalse(toTest.retry(mockRequestException, max + 1));
    }
}
