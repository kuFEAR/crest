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

package org.codegist.crest.handler;

import org.codegist.crest.CRestConfig;
import org.codegist.crest.io.RequestException;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class MaxAttemptRetryHandlerTest {

    private final CRestConfig mockCRestConfig = mock(CRestConfig.class);
    {
        when(mockCRestConfig.getMaxAttempts()).thenReturn(2);
    }
    private final MaxAttemptRetryHandler toTest = new MaxAttemptRetryHandler(mockCRestConfig);

    @Test
    public void shouldUseGivenMaxValue() throws Exception {
        RequestException exception = mock(RequestException.class);
        assertTrue(toTest.retry(exception, 1));
        assertTrue(toTest.retry(exception, 2));
        assertFalse(toTest.retry(exception, 3));
    }
}
