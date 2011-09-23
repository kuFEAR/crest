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

package org.codegist.crest.io.http;

import org.codegist.crest.io.Request;
import org.codegist.crest.serializer.ResponseDeserializer;
import org.codegist.crest.test.util.Classes;
import org.codegist.crest.test.util.Requests;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import static org.codegist.crest.test.util.Values.UTF8;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * @author laurent.gilles@codegist.org
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({HttpResponse.class,HttpResourceInputStream.class,HttpResource.class})
public class HttpResponseTest {

    private final Method method = Classes.byName(TestInterface.class, "get");
    private final ResponseDeserializer mockBaseResponseDeserializer = mock(ResponseDeserializer.class);
    private final ResponseDeserializer mockCustomResponseDeserializer = mock(ResponseDeserializer.class);
    private final HttpResourceInputStream mockHttpResourceInputStream = mock(HttpResourceInputStream.class);

    private final Request mockRequest = Requests.mockWith(method);
    private final HttpResource mockResource = mock(HttpResource.class);

    private final HttpResponse toTest;
    {
        try {
            whenNew(HttpResourceInputStream.class).withArguments(mockResource).thenReturn(mockHttpResourceInputStream);
            toTest = new HttpResponse(mockBaseResponseDeserializer, mockCustomResponseDeserializer, mockRequest, mockResource);
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    @Test
    public void disposeShouldDisposeHttpResourceInputStream() throws IOException {
        toTest.dispose();
        verify(mockHttpResourceInputStream).close();
    }

    @Test
    public void getStatusCodeShouldGetItFromHttpResource() throws IOException {
        when(mockResource.getStatusCode()).thenReturn(100);
        assertEquals(100, toTest.getStatusCode());
    }

    @Test
    public void getStatusMessageGetItFromHttpResource() throws IOException {
        when(mockResource.getStatusMessage()).thenReturn("value");
        assertEquals("value", toTest.getStatusMessage());
    }

    @Test
    public void getContentTypeGetItFromHttpResource() throws IOException {
        when(mockResource.getContentType()).thenReturn("value");
        assertEquals("value", toTest.getContentType());
    }

    @Test
    public void getCharsetGetItFromHttpResource() throws IOException {
        when(mockResource.getCharset()).thenReturn(UTF8);
        assertEquals(UTF8, toTest.getCharset());
    }

    @Test
    public void getContentEncodingGetItFromHttpResource() throws IOException {
        when(mockResource.getContentEncoding()).thenReturn("value");
        assertEquals("value", toTest.getContentEncoding());
    }

    @Test
    public void getExpectedGenericTypeGetItFromMethodConfigMethod() throws IOException {
        assertSame(method.getGenericReturnType(), toTest.getExpectedGenericType());
    }

    @Test
    public void getExpectedTypeGetItFromMethodConfigMethod() throws IOException {
        assertSame(method.getReturnType(), toTest.getExpectedType());
    }

    @Test
    public void getRequestShouldReturnRequest() throws IOException {
        assertSame(mockRequest, toTest.getRequest());
    }

    @Test
    public void asStreamShouldReturnHttpResourceInputStream() {
        assertSame(mockHttpResourceInputStream, toTest.asStream());
    }

    @Test
    public void deserializeShouldUseBaseResponseDeserializer() throws Exception {
        when(mockBaseResponseDeserializer.deserialize(toTest)).thenReturn(10);
        assertEquals(10, toTest.deserialize());
    }

    @Test
    public void toWithClassShouldUseCustomResponseDeserializerWithGivenType() throws Exception {
        ArgumentCaptor<DelegatingResponse> arg = ArgumentCaptor.forClass(DelegatingResponse.class);
        when(mockCustomResponseDeserializer.deserialize(arg.capture())).thenReturn(10);
        assertEquals(10, toTest.to((Class)Integer.class));
        assertEquals(Integer.class, arg.getValue().getExpectedGenericType());
        assertEquals(Integer.class, arg.getValue().getExpectedType());
        assertSame(toTest, Classes.getFieldValue(arg.getValue(), "delegate"));
    }

    @Test
    public void toWithClassAndTypeShouldUseCustomResponseDeserializerWithGivenType() throws Exception {
        Method m = Classes.byName(TestInterface.class, "get2");
        ArgumentCaptor<DelegatingResponse> arg = ArgumentCaptor.forClass(DelegatingResponse.class);
        when(mockCustomResponseDeserializer.deserialize(arg.capture())).thenReturn(10);
        assertEquals(10, toTest.to(m.getReturnType(), m.getGenericReturnType()));
        assertEquals(m.getGenericReturnType(), arg.getValue().getExpectedGenericType());
        assertEquals(m.getReturnType(), arg.getValue().getExpectedType());
        assertSame(toTest, Classes.getFieldValue(arg.getValue(), "delegate"));
    }





    public interface TestInterface  {
        List<String> get();
        Set<String> get2();
    }

}
