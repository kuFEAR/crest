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

import org.codegist.crest.CRestException;
import org.codegist.crest.io.Request;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({CRestException.class})
public class ErrorDelegatorHandlerTest {

    private final ErrorDelegatorHandler toTest = new ErrorDelegatorHandler();

    @Test
    public void shouldUseCRestExceptionHandle() throws Exception {
        Exception e = new Exception();
        RuntimeException expected = new RuntimeException();
        mockStatic(CRestException.class);
        when(CRestException.handle(e)).thenReturn(expected);
        Request request = mock(Request.class);
        try {
            toTest.handle(request, e);
        } catch (RuntimeException e1) {
            assertSame(expected, e1);
            verifyNoMoreInteractions(request);
        }
    }
}
