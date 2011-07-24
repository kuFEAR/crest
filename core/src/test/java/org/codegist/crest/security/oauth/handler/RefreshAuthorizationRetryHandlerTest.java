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

package org.codegist.crest.security.oauth.handler;

import org.codegist.crest.io.RequestException;
import org.codegist.crest.io.Response;
import org.codegist.crest.security.Authorization;
import org.codegist.crest.security.handler.RefreshAuthorizationRetryHandler;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.codegist.crest.CRestProperty.CREST_MAX_ATTEMPTS;
import static org.codegist.crest.CRestProperty.CREST_UNAUTHORIZED_STATUS_CODE;
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

    private final Map<String,Object> crestProperties = new HashMap<String, Object>(){{
        put(CREST_UNAUTHORIZED_STATUS_CODE, UNAUTHORIZED_STATUS_CODE);
        put(Authorization.class.getName(), mockAuthorization);
    }};

    private final RefreshAuthorizationRetryHandler toTest = new RefreshAuthorizationRetryHandler(crestProperties);

    @Test
    public void shouldNotRetryIndependentlyFromRequestExceptionContentAsDefaultMaxIsOne() throws Exception {
        assertFalse(toTest.retry(mockRequestException, 2));
    }

    @Test
    public void shouldRetryAndRefreshUpUntilMaxWhenRequestExceptionDoHaveAResponseWithAnUnauthorizedStatus() throws Exception {
        crestProperties.put(CREST_MAX_ATTEMPTS, 5);
        RefreshAuthorizationRetryHandler toTest = new RefreshAuthorizationRetryHandler(crestProperties);
        shouldRetryUpUntilMaxWhenRequestExceptionDoHaveAResponse(toTest, 5, UNAUTHORIZED_STATUS_CODE);
        verify(mockAuthorization, times(4)).refresh();
    }

    @Test
    public void shouldRetryButDontRefreshUpUntilMaxWhenRequestExceptionDoHaveAResponseRefreshAuthorizationRetryHandlerWithAStatusDifferentThanUnauthorized() throws Exception {
        crestProperties.put(CREST_MAX_ATTEMPTS, 5);
        RefreshAuthorizationRetryHandler toTest = new RefreshAuthorizationRetryHandler(crestProperties);
        shouldRetryUpUntilMaxWhenRequestExceptionDoHaveAResponse(toTest, 5, 123);
        verify(mockAuthorization, never()).refresh();
    }

    @Test
    public void shouldRetryButDontRefreshUpUntilMaxWhenRequestExceptionDoesNotHaveAResponse() throws Exception {
        crestProperties.put(CREST_MAX_ATTEMPTS, 5);
        RefreshAuthorizationRetryHandler toTest = new RefreshAuthorizationRetryHandler(crestProperties);
        when(mockRequestException.hasResponse()).thenReturn(false);
        assertTrue(toTest.retry(mockRequestException, 5));
        assertFalse(toTest.retry(mockRequestException, 6));
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
