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
import org.codegist.crest.CRestException;
import org.codegist.crest.io.Response;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class DefaultResponseHandlerTest {

    private final CRestConfig crestConfig = mock(CRestConfig.class);
    {
        when(crestConfig.<Object>get(DefaultResponseHandler.MIN_ERROR_STATUS_CODE_PROP, Integer.MAX_VALUE)).thenReturn(123);
    }
    private final DefaultResponseHandler toTest = new DefaultResponseHandler(crestConfig);

    @Test
    public void shouldUseResponseDeserialize() throws Exception {
        Response response = mock(Response.class);
        when(response.deserialize()).thenReturn("hello");
        Object actual = toTest.handle(response);
        assertEquals("hello", actual);
    }

    @Test
    public void shouldThrowExceptionFor400PlusResponseStatus() throws Exception {
        Response response = mock(Response.class);
        when(response.getStatusCode()).thenReturn(123);
        when(response.to(String.class)).thenReturn("hello");
        try {
            toTest.handle(response);
            fail();
        } catch (CRestException e) {
            assertEquals("Response Status Code:123\nResponse: hello", e.getMessage());
        }

    }
}
