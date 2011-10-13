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

package org.codegist.crest.serializer;

import org.codegist.crest.CRestException;
import org.codegist.crest.io.Response;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class ResponseDeserializerCompositeTest {

    private final Response mockResponse = mock(Response.class);
    private final ResponseDeserializer mockResponseDeserializer1 = mock(ResponseDeserializer.class);
    private final ResponseDeserializer mockResponseDeserializer2 = mock(ResponseDeserializer.class);
    private final ResponseDeserializerComposite toTest = new ResponseDeserializerComposite(mockResponseDeserializer1, mockResponseDeserializer2);


    @Test(expected = CRestException.class)
    public void shouldIterateOverResponseDeserializersAsLongAsIllegalArgumentExceptionIsThrown() throws Exception {
        Exception e1 = new IllegalArgumentException();
        Exception e2 = new IllegalArgumentException();
        when(mockResponseDeserializer1.deserialize(mockResponse)).thenThrow(e1);
        when(mockResponseDeserializer2.deserialize(mockResponse)).thenThrow(e2);

        try {
            toTest.deserialize(mockResponse);
            fail();
        } catch (Exception e) {
            assertSame(e2, e.getCause());
            throw e;
        }
    }

    @Test
    public void shouldIterateOverResponseDeserializersUntilSuccess() throws Exception {
        when(mockResponseDeserializer1.deserialize(mockResponse)).thenThrow(new IllegalArgumentException());
        when(mockResponseDeserializer2.deserialize(mockResponse)).thenReturn("hello");

        String actual = toTest.deserialize(mockResponse);

        assertEquals("hello", actual);
    }
}
