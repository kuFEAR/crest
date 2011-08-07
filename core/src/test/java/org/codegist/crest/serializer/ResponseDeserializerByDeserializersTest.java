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

package org.codegist.crest.serializer;

import org.codegist.common.io.IOs;
import org.codegist.crest.CRestException;
import org.codegist.crest.config.MethodConfig;
import org.codegist.crest.io.Request;
import org.codegist.crest.io.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.codegist.crest.util.Values.SOME_STRING_UTF8_BYTES;
import static org.codegist.crest.util.Values.UTF8;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({IOs.class, ResponseDeserializerByDeserializers.class})
public class ResponseDeserializerByDeserializersTest {

    private final ResponseDeserializerByDeserializers toTest = new ResponseDeserializerByDeserializers();

    private final InputStream mockStream = mock(InputStream.class);
    private final ByteArrayInputStream mockDumpStream = mock(ByteArrayInputStream.class);
    private final Request mockRequest = mock(Request.class);
    private final MethodConfig mockMethodConfig = mock(MethodConfig.class);
    private final Response mockResponse = mock(Response.class);

    {
        try {
            when(mockResponse.asStream()).thenReturn(mockStream);
            when(mockResponse.getRequest()).thenReturn(mockRequest);
            when(mockResponse.getCharset()).thenReturn(UTF8);
            when(mockResponse.getExpectedType()).thenReturn((Class) String.class);
            when(mockResponse.getExpectedGenericType()).thenReturn(String.class);
            when(mockRequest.getMethodConfig()).thenReturn(mockMethodConfig);
            whenNew(ByteArrayInputStream.class).withArguments(SOME_STRING_UTF8_BYTES).thenReturn(mockDumpStream);
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenResponseDoesNotHaveAnyDeserializers() throws Exception {
        when(mockMethodConfig.getDeserializers()).thenReturn(new Deserializer[0]);
        toTest.deserialize(mockResponse);
    }

    @Test
    public void shouldUseResponseStreamWhenOneDeserializerIsGiven() throws Exception {
        Deserializer mockDeserializer1 = mock(Deserializer.class);
        when(mockMethodConfig.getDeserializers()).thenReturn(new Deserializer[]{mockDeserializer1});
        when(mockDeserializer1.deserialize(String.class, String.class, mockStream, UTF8)).thenReturn("hi");

        String actual = toTest.deserialize(mockResponse);

        assertEquals("hi", actual);
    }

    @Test
    public void shouldDumpInputStreamInMemoryWhenMoreThanOneDeserializerIsGiven() throws Exception {
        Deserializer mockDeserializer1 = mock(Deserializer.class);
        Deserializer mockDeserializer2 = mock(Deserializer.class);
        when(mockMethodConfig.getDeserializers()).thenReturn(new Deserializer[]{mockDeserializer1, mockDeserializer2});
        when(mockDeserializer1.deserialize(String.class, String.class, mockDumpStream, UTF8)).thenThrow(new Exception());
        when(mockDeserializer2.deserialize(String.class, String.class, mockDumpStream, UTF8)).thenReturn("hi");

        mockStatic(IOs.class);
        when(IOs.toByteArray(mockStream, true)).thenReturn(SOME_STRING_UTF8_BYTES);

        String actual = toTest.deserialize(mockResponse);

        assertEquals("hi", actual);
        verify(mockDumpStream).mark(0);
        verify(mockDumpStream).reset();
    }

    @Test(expected = CRestException.class)
    public void shouldShouleThrowLastExceptionWhenAllDeserializerHaveFailed() throws Exception {
        Exception e1 = new Exception();
        Exception e2 = new Exception();
        Deserializer mockDeserializer1 = mock(Deserializer.class);
        Deserializer mockDeserializer2 = mock(Deserializer.class);
        when(mockMethodConfig.getDeserializers()).thenReturn(new Deserializer[]{mockDeserializer1, mockDeserializer2});
        when(mockDeserializer1.deserialize(String.class, String.class, mockDumpStream, UTF8)).thenThrow(e1);
        when(mockDeserializer2.deserialize(String.class, String.class, mockDumpStream, UTF8)).thenThrow(e2);

        mockStatic(IOs.class);
        when(IOs.toByteArray(mockStream, true)).thenReturn(SOME_STRING_UTF8_BYTES);

        try {
            toTest.deserialize(mockResponse);
            fail();
        } catch (Exception e) {
            verify(mockDumpStream, times(2)).mark(0);
            verify(mockDumpStream, times(2)).reset();
            assertSame(e2, e.getCause());
            throw e;
        }
    }
}
