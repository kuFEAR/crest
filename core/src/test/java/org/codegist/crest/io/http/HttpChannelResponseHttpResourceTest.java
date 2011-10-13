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

import org.codegist.crest.io.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.zip.GZIPInputStream;

import static org.codegist.crest.test.util.Values.UTF8;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * @author laurent.gilles@codegist.org
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(HttpChannelResponseHttpResource.class)
public class HttpChannelResponseHttpResourceTest {

    private final HttpChannel.Response mockResponse = mock(HttpChannel.Response.class);

    private HttpChannelResponseHttpResource  newToTest(){
        org.apache.log4j.Logger.getLogger(Response.class).setLevel(org.apache.log4j.Level.INFO);
        try {
            return new HttpChannelResponseHttpResource(mockResponse);
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    @Test
    public void getStatusMessageShouldReturnResponseOne() throws IOException {
        when(mockResponse.getStatusMessage()).thenReturn("value");
        HttpChannelResponseHttpResource toTest = newToTest();
        assertEquals("value", toTest.getStatusMessage());
    }

    @Test
    public void getStatusCodeShouldReturnResponseOne() throws IOException {
        when(mockResponse.getStatusCode()).thenReturn(123);
        HttpChannelResponseHttpResource toTest = newToTest();
        assertEquals(123, toTest.getStatusCode());
    }

    @Test
    public void closeShouldCloseResponse() throws IOException {
        newToTest().close();
        verify(mockResponse).close();
    }

    @Test
    public void getEntityShouldReturnZippedWrappedIfContentEncodingIsGZIP() throws Exception {
        InputStream stream = mock(InputStream.class);
        GZIPInputStream expected = mock(GZIPInputStream.class);
        when(mockResponse.getContentEncoding()).thenReturn("gzip");
        when(mockResponse.getEntity()).thenReturn(stream);
        whenNew(GZIPInputStream.class).withArguments(stream).thenReturn(expected);

        HttpChannelResponseHttpResource toTest = newToTest();
        assertSame(expected, toTest.getEntity());
        assertEquals("gzip", toTest.getContentEncoding());
    }

    @Test
    public void getEntityShouldReturnResponseEntity() throws Exception {
        InputStream expected = mock(InputStream.class);
        when(mockResponse.getEntity()).thenReturn(expected);

        HttpChannelResponseHttpResource toTest = newToTest();
        assertSame(expected, toTest.getEntity());
    }

    @Test
    public void shouldParseContentTypeAndCharsetWhenGiven() throws IOException {
        when(mockResponse.getContentType()).thenReturn("plain/text; charset=utf-8");
        HttpChannelResponseHttpResource toTest = newToTest();
        assertEquals("plain/text", toTest.getContentType());
        assertEquals(UTF8, toTest.getCharset());
    }
    @Test
    public void shouldParseContentTypeAndNoCharset() throws IOException {
        when(mockResponse.getContentType()).thenReturn("plain/text");
        HttpChannelResponseHttpResource toTest = newToTest();
        assertEquals("plain/text", toTest.getContentType());
        assertEquals(Charset.forName("ISO-8859-1"), toTest.getCharset());
    }
    @Test
    public void shouldDefaultContentTypeAndCharset() throws IOException {
        HttpChannelResponseHttpResource toTest = newToTest();
        assertEquals("text/html", toTest.getContentType());
        assertEquals(Charset.forName("ISO-8859-1"), toTest.getCharset());
    }
}
