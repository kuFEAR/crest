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

package org.codegist.crest.io;

import org.codegist.crest.config.MethodConfig;
import org.codegist.crest.handler.RetryHandler;
import org.junit.Test;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

/**
 * @author laurent.gilles@codegist.org
 */
public class RetryingRequestExecutorTest {

    private final Request request = mock(Request.class);
    private final Response expected = mock(Response.class);
    private final MethodConfig methodConfig = mock(MethodConfig.class);
    private final RetryHandler retryHandler = mock(RetryHandler.class);
    private final RequestExecutor mockRequestExecutor = mock(RequestExecutor.class);
    private final RetryingRequestExecutor toTest = new RetryingRequestExecutor(mockRequestExecutor);

    {
        when(request.getMethodConfig()).thenReturn(methodConfig);
        when(methodConfig.getRetryHandler()).thenReturn(retryHandler);
    }

    @Test
    public void executeShouldRetryUntilSuccessUsingRetryHandler() throws Exception {
        RequestException requestException1 = mock(RequestException.class);
        RequestException requestException2 = mock(RequestException.class);
        when(retryHandler.retry(requestException1, 2)).thenReturn(true);
        when(retryHandler.retry(requestException2, 3)).thenReturn(true);
        when(mockRequestExecutor.execute(request)).thenThrow(requestException1, requestException2).thenReturn(expected);

        Response actual = toTest.execute(request);
        assertSame(expected, actual);

        verify(requestException1).dispose();
        verify(requestException2).dispose();
    }

    @Test
    public void executeShouldRetryUntilRetryHandlerReturnsFalseAndRethrowLastException() throws Exception {
        RequestException requestException1 = mock(RequestException.class);
        RequestException requestException2 = mock(RequestException.class);
        when(retryHandler.retry(requestException1, 2)).thenReturn(true);
        when(retryHandler.retry(requestException2, 3)).thenReturn(false);
        when(mockRequestExecutor.execute(request)).thenThrow(requestException1, requestException2);

        try {
            toTest.execute(request);
        } catch (Exception e) {
            assertSame(requestException2, e);
            verify(requestException1).dispose();
            verifyNoMoreInteractions(requestException2);
        }
    }

    @Test
    public void executeShouldCancelAllIfDelegateThrowUnexpectedException() throws Exception {
        Exception expected = mock(Exception.class);
        when(mockRequestExecutor.execute(request)).thenThrow(expected);

        try {
            toTest.execute(request);
        } catch (Exception e) {
            assertSame(expected, e);
        }
    }
}
