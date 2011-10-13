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

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author laurent.gilles@codegist.org
 */
public class HttpResourceInputStreamTest {
    
    private final InputStream mockInputStream = mock(InputStream.class);
    private final HttpResource mockHttpResource = mock(HttpResource.class);
    private final HttpResourceInputStream toTest;
    
    {
        try {
            when(mockHttpResource.getEntity()).thenReturn(mockInputStream);
            toTest = new HttpResourceInputStream(mockHttpResource);
        }catch(Exception e){
            throw new ExceptionInInitializerError(e);
        }
    }

    @Test
    public void closeShouldCloseHttpResourceAndCallSuperJustAtFirstCall() throws IOException {
        toTest.close();
        toTest.close();
        verify(mockHttpResource).close();
        verify(mockInputStream).close();
    }

    @Test
    public void finalizeShouldClose() throws Throwable {
        toTest.finalize();
        verify(mockHttpResource).close();
        verify(mockInputStream).close();
    }
    
    @Test
    public void readShouldDelegate() throws IOException {
        when(mockInputStream.read()).thenReturn(10);
        assertEquals(10, toTest.read());
    }

    @Test
    public void readWithBytesShouldDelegate() throws IOException {
        when(mockInputStream.read(new byte[]{1,2})).thenReturn(10);
        assertEquals(10, toTest.read(new byte[]{1,2}));
    }

    @Test
    public void readWithArgsShouldDelegate() throws IOException {
        when(mockInputStream.read(new byte[]{1,2},2,3)).thenReturn(10);
        assertEquals(10, toTest.read(new byte[]{1,2},2,3));
    }

    @Test
    public void skipShouldDelegate() throws IOException {
        when(mockInputStream.skip(10)).thenReturn(100l);
        assertEquals(100l, toTest.skip(10));
    }

    @Test
    public void availableShouldDelegate() throws IOException {
        when(mockInputStream.available()).thenReturn(10);
        assertEquals(10, toTest.available());
    }

    @Test
    public void markShouldDelegate() {
        toTest.mark(10);
        verify(mockInputStream).mark(10);
    }

    @Test
    public void resetShouldDelegate() throws IOException {
        toTest.reset();
        verify(mockInputStream).reset();
    }

    @Test
    public void markSupportedShouldDelegate() {
        when(mockInputStream.markSupported()).thenReturn(true);
        assertTrue(toTest.markSupported());
    }

    @Test
    public void hashCodeShouldDelegate() {
        assertEquals(mockInputStream.hashCode(), toTest.hashCode());
    }

    @Test
    public void equalsShouldDelegate() {
        assertTrue(toTest.equals(toTest));
        assertFalse(toTest.equals(new Object()));
    }

    @Test
    public void toStringShouldDelegate() {
        assertEquals(mockInputStream.toString(), toTest.toString());
    }
    
    
}
