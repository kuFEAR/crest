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

import org.apache.http.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.codegist.common.io.EmptyInputStream;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class HttpClientHttpChannelTest {

    private final HttpClient client = mock(HttpClient.class);
    private final HttpEntityEnclosingRequestBase request = mock(HttpEntityEnclosingRequestBase.class);
    private final HttpClientHttpChannel toTest = new HttpClientHttpChannel(client, request);

    @Test
    public void setConnetionTimeoutShouldSetHttpClientRequestParams() throws IOException {
        HttpParams params = mock(HttpParams.class);
        when(request.getParams()).thenReturn(params);
        toTest.setConnectionTimeout(123);
        verify(params).setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 123);
    }

    @Test
    public void setSocketTimeoutShouldSetHttpClientRequestParams() throws IOException {
        HttpParams params = mock(HttpParams.class);
        when(request.getParams()).thenReturn(params);
        toTest.setSocketTimeout(123);
        verify(params).setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 123);
    }

    @Test
    public void setHeaderShouldSetHttpClientRequestHeader() throws IOException {
        toTest.setHeader("name", "value");
        verify(request).setHeader("name", "value");
    }

    @Test
    public void addHeaderShouldSetHttpClientRequestHeader() throws IOException {
        toTest.addHeader("name", "value");
        verify(request).addHeader("name", "value");
    }

    @Test
    public void setContentTypeShouldSetHttpClientRequestHeader() throws IOException {
        toTest.setContentType("value");
        verify(request).setHeader("Content-Type", "value");
    }

    @Test
    public void setAcceptShouldSetHttpClientRequestHeader() throws IOException {
        toTest.setAccept("value");
        verify(request).setHeader("Accept", "value");
    }

    @Test
    public void writeEntityWithShouldOverrideHttpClientEntity() throws IOException, NoSuchFieldException, IllegalAccessException {
        OutputStream out = mock(OutputStream.class);
        HttpEntityWriter writer = mock(HttpEntityWriter.class);
        ArgumentCaptor<HttpEntity> entityCaptor = ArgumentCaptor.forClass(HttpEntity.class);

        when(writer.getContentLength()).thenReturn(123);

        toTest.writeEntityWith(writer);
        verify(request).setEntity(entityCaptor.capture());

        HttpEntity entity = entityCaptor.getValue();
        assertEquals(123, entity.getContentLength());
        assertFalse(entity.isRepeatable());
        assertTrue(entity.isStreaming());
        try {
            entity.getContent();
            fail();
        } catch (UnsupportedOperationException e) {
        }
        entity.writeTo(out);
        verify(writer).writeEntityTo(out);
    }

    @Test
    public void sendShouldWrapHttpClientResponse() throws IOException {
        HttpResponse clientResponse = mock(HttpResponse.class);
        HttpEntity httpEntity = mock(HttpEntity.class);
        InputStream inputStream = mock(InputStream.class);
        StatusLine statusLine = new BasicStatusLine(mock(ProtocolVersion.class), 123, "reason");
        Header contentType = new BasicHeader("somename", "contentType");
        Header contentEncoding = new BasicHeader("somename", "contentEncoding");

        when(httpEntity.getContent()).thenReturn(inputStream);
        when(client.execute(request)).thenReturn(clientResponse);

        HttpChannel.Response response = toTest.send();
        assertNull(response.getContentType());
        assertNull(response.getContentEncoding());
        assertSame(EmptyInputStream.INSTANCE, response.getEntity());

        when(clientResponse.getStatusLine()).thenReturn(statusLine);
        when(clientResponse.getFirstHeader("Content-Type")).thenReturn(contentType);
        when(clientResponse.getFirstHeader("Content-Encoding")).thenReturn(contentEncoding);
        when(clientResponse.getEntity()).thenReturn(httpEntity);

        assertSame(inputStream, response.getEntity());
        assertEquals("contentType", response.getContentType());
        assertEquals("contentEncoding", response.getContentEncoding());
        assertEquals(123, response.getStatusCode());
        assertEquals("reason", response.getStatusMessage());

        response.close();
        verify(httpEntity).consumeContent();
        verify(request).abort();
    }

}
