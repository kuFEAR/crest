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

package org.codegist.crest.io.http;

import org.codegist.crest.io.Request;
import org.codegist.crest.io.Response;
import org.junit.Test;

import java.io.InputStream;
import java.lang.reflect.Type;

import static org.codegist.crest.test.util.Values.UTF8;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

/**
 * @author laurent.gilles@codegist.org
 */
public class DelegatingResponseTest {

    private final Response mockResponse = mock(Response.class);
    private final DelegatingResponse toTest = new DelegatingResponse(mockResponse);

    @Test
    public void getRequestShouldDelegates(){
        Request mockRequest = mock(Request.class);
        when(mockResponse.getRequest()).thenReturn(mockRequest);
        assertSame(mockRequest, toTest.getRequest());
    }

    @Test
    public void getStatusCodeShouldDelegates() throws Exception {
        when(mockResponse.getStatusCode()).thenReturn(123);
        assertEquals(123, toTest.getStatusCode());
    }

    @Test
    public void getContentTypeShouldDelegates() throws Exception {
        when(mockResponse.getContentType()).thenReturn("value");
        assertEquals("value", toTest.getContentType());
    }

    @Test
    public void getCharsetShouldDelegates() throws Exception {
        when(mockResponse.getCharset()).thenReturn(UTF8);
        assertEquals(UTF8, toTest.getCharset());
    }

    @Test
    public void getContentEncodingShouldDelegates() throws Exception {
        when(mockResponse.getContentEncoding()).thenReturn("value");
        assertEquals("value", toTest.getContentEncoding());
    }

    @Test
    public void getExpectedGenericTypeShouldDelegates() throws Exception {
        when(mockResponse.getExpectedGenericType()).thenReturn((Type)Object.class);
        assertEquals(Object.class, toTest.getExpectedGenericType());
    }

    @Test
    public void getExpectedTypeShouldDelegates() throws Exception {
        when(mockResponse.getExpectedType()).thenReturn((Class)Object.class);
        assertEquals(Object.class, toTest.getExpectedType());
    }

    @Test
    public void deserializeShouldDelegates() throws Exception {
        when(mockResponse.deserialize()).thenReturn(10);
        assertEquals(10, toTest.deserialize());
    }

    @Test
    public void toWithClassShouldDelegates() throws Exception {
        when(mockResponse.to(Object.class)).thenReturn(10);
        assertEquals(10, toTest.to(Object.class));
    }

    @Test
    public void toWithClassAndTypeShouldDelegates() throws Exception {
        when(mockResponse.to(Object.class,(Type)Object.class)).thenReturn(10);
        assertEquals(10, toTest.to(Object.class, (Type)Object.class));
    }

    @Test
    public void asStreamShouldDelegates() throws Exception {
        InputStream expected = mock(InputStream.class);
        when(mockResponse.asStream()).thenReturn(expected);
        assertSame(expected, toTest.asStream());
    }

    @Test
    public void disposeShouldDelegates() throws Exception {
        toTest.dispose();
        verify(mockResponse).dispose();
    }
}
