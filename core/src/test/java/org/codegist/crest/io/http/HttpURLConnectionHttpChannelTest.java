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

import org.codegist.crest.config.MethodType;
import org.codegist.crest.io.http.HttpChannel;
import org.codegist.crest.io.http.HttpEntityWriter;
import org.codegist.crest.io.http.HttpURLConnectionHttpChannel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({HttpURLConnection.class})
public class HttpURLConnectionHttpChannelTest {

    private final HttpURLConnection httpURLConnection = mock(HttpURLConnection.class);
    private final MethodType methodType = MethodType.POST;
    private final HttpURLConnectionHttpChannel toTest = new HttpURLConnectionHttpChannel(httpURLConnection, methodType);
    {
        verify(httpURLConnection).setRequestProperty("Connection", "Keep-Alive");
        verify(httpURLConnection).setRequestProperty("User-Agent", "CodeGist-CRest Agent");
    }

    @Test
    public void setSocketTimeoutShouldSetItIntoHttpUrlConnection() throws IOException {
        toTest.setSocketTimeout(123);
        verify(httpURLConnection).setReadTimeout(123);
    }
    @Test
    public void setConnectionTimeoutShouldSetItIntoHttpUrlConnection() throws IOException {
        toTest.setConnectionTimeout(123);
        verify(httpURLConnection).setConnectTimeout(123);
    }
    @Test
    public void setHeaderShouldSetItIntoHttpUrlConnection() throws IOException {
        toTest.setHeader("name", "value");
        verify(httpURLConnection).setRequestProperty("name", "value");
    }
    @Test
    public void addHeaderShouldSetItIntoHttpUrlConnection() throws IOException {
        toTest.addHeader("name", "value");
        verify(httpURLConnection).addRequestProperty("name", "value");
    }
    @Test
    public void setContentTypeShouldSetItIntoHttpUrlConnection() throws IOException {
        toTest.setContentType("value");
        verify(httpURLConnection).setRequestProperty("Content-Type", "value");
    }
    @Test
    public void setAcceptShouldSetItIntoHttpUrlConnection() throws IOException {
        toTest.setAccept("value");
        verify(httpURLConnection).setRequestProperty("Accept", "value");
    }

    @Test
    public void sendShouldFireRequest() throws IOException {
        HttpEntityWriter writer = mock(HttpEntityWriter.class);
        OutputStream out = mock(OutputStream.class);
        InputStream inputStream = mock(InputStream.class);
        InputStream errorStream = mock(InputStream.class);

        when(writer.getContentLength()).thenReturn(123);
        when(httpURLConnection.getOutputStream()).thenReturn(out);

        toTest.writeEntityWith(writer);
        HttpChannel.Response actual = toTest.send();

        InOrder inOrder = inOrder(writer,httpURLConnection, out);
        inOrder.verify(httpURLConnection).setRequestProperty("Content-Length", "123");
        inOrder.verify(httpURLConnection).setDoOutput(true);
        inOrder.verify(writer).writeEntityTo(out);
        inOrder.verify(out).flush();
        inOrder.verify(out).close();


        when(httpURLConnection.getErrorStream()).thenReturn(errorStream);
        when(httpURLConnection.getInputStream()).thenReturn(inputStream);
        when(httpURLConnection.getResponseMessage()).thenReturn("message");
        when(httpURLConnection.getContentType()).thenReturn("contentType");
        when(httpURLConnection.getContentEncoding()).thenReturn("contentEncoding");
        when(httpURLConnection.getResponseCode()).thenReturn(200);

        assertEquals(200, actual.getStatusCode());
        assertSame(inputStream, actual.getEntity());

        when(httpURLConnection.getResponseCode()).thenReturn(400);
        assertEquals(400, actual.getStatusCode());
        assertSame(errorStream, actual.getEntity());


        assertEquals("message", actual.getStatusMessage());
        assertEquals("contentType", actual.getContentType());
        assertEquals("contentEncoding", actual.getContentEncoding());

        actual.close();
        verify(httpURLConnection).disconnect();

    }
}
