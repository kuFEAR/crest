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

import org.codegist.common.lang.Disposables;
import org.codegist.crest.config.MethodConfig;
import org.codegist.crest.config.MethodType;
import org.codegist.crest.config.ParamType;
import org.codegist.crest.entity.EntityWriter;
import org.codegist.crest.io.Request;
import org.codegist.crest.io.RequestException;
import org.codegist.crest.param.EncodedPair;
import org.codegist.crest.serializer.ResponseDeserializer;
import org.codegist.crest.util.Pairs;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.util.Iterator;

import static java.util.Arrays.asList;
import static org.codegist.crest.test.util.Values.UTF8;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * @author laurent.gilles@codegist.org
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({HttpRequestExecutor.class,Disposables.class,HttpRequests.class})
public class HttpRequestExecutorTest {

    private final HttpChannelFactory mockChannelFactory = mock(HttpChannelFactory.class);
    private final HttpChannel mockChannel = mock(HttpChannel.class);
    private final ResponseDeserializer mockBaseResponseDeserializer = mock(ResponseDeserializer.class);
    private final ResponseDeserializer mockCustomTypeResponseDeserializer = mock(ResponseDeserializer.class);
    private final HttpRequestExecutor toTest = new HttpRequestExecutor(mockChannelFactory, mockBaseResponseDeserializer, mockCustomTypeResponseDeserializer);

    @Test
    public void executeShouldThrowRequestExceptionForAnyIOException() throws Exception {
        Request request = mock(Request.class);
        IOException expected = new IOException();

        mockStatic(HttpRequests.class);
        when(HttpRequests.toUrl(request)).thenThrow(expected);


        try {
            toTest.execute(request);
        }catch(RequestException e){
            assertNull(e.getResponse());
            assertSame(expected, e.getCause());
        }
    }


    @Test
    public void executeShouldThrowRequestExceptionIfStatusCodeGreaterOrEqualsToBadRequest() throws Exception {
        Request request = mock(Request.class);
        HttpResponse httpResponse= mock(HttpResponse.class);
        HttpChannel.Response response= mock(HttpChannel.Response.class);
        HttpChannelResponseHttpResource httpChannelResponseHttpResource = mock(HttpChannelResponseHttpResource.class);
        EntityWriter entityWriter = mock(EntityWriter.class);
        MethodConfig methodConfig = mock(MethodConfig.class);
        RequestEntityWriter requestEntityWriter = mock(RequestEntityWriter.class);

        Iterator<EncodedPair> headers = asList(
                Pairs.toPreEncodedPair("h1", "v1"),
                Pairs.toPreEncodedPair("h2", "v2")
        ).iterator();
        Iterator<EncodedPair> cookies = asList(
                Pairs.toPreEncodedPair("Cookie", "c1=v1"),
                Pairs.toPreEncodedPair("Cookie", "c2=v2")
        ).iterator();

        mockStatic(HttpRequests.class);
        when(HttpRequests.toUrl(request)).thenReturn("some-url");
        when(request.getMethodConfig()).thenReturn(methodConfig);
        when(methodConfig.getCharset()).thenReturn(UTF8);
        when(methodConfig.getType()).thenReturn(MethodType.POST);
        when(methodConfig.getConnectionTimeout()).thenReturn(10);
        when(methodConfig.getSocketTimeout()).thenReturn(11);
        when(methodConfig.getProduces()).thenReturn("produces");
        when(methodConfig.getConsumes()).thenReturn(new String[]{"consumes1","consumes2"});
        when(methodConfig.getEntityWriter()).thenReturn(entityWriter);
        when(mockChannelFactory.open(MethodType.POST, "some-url", UTF8)).thenReturn(mockChannel);
        when(request.getEncodedParamsIterator(ParamType.HEADER)).thenReturn(headers);
        when(request.getEncodedParamsIterator(ParamType.COOKIE)).thenReturn(cookies);
        when(entityWriter.getContentType(request)).thenReturn("content-type");
        whenNew(RequestEntityWriter.class).withArguments(request).thenReturn(requestEntityWriter);
        when(mockChannel.send()).thenReturn(response);
        whenNew(HttpChannelResponseHttpResource.class).withArguments(response).thenReturn(httpChannelResponseHttpResource);
        whenNew(HttpResponse.class).withArguments(
                mockBaseResponseDeserializer,
                mockCustomTypeResponseDeserializer,
                request,
                httpChannelResponseHttpResource
        ).thenReturn(httpResponse);
        when(httpResponse.getStatusCode()).thenReturn(400);
        when(httpResponse.getStatusMessage()).thenReturn("some message");


        try {
            toTest.execute(request);
        }catch(RequestException e){
            assertSame(httpResponse, e.getResponse());
            assertEquals("some message", e.getMessage());
        }

        InOrder inOrder = inOrder(mockChannel);
        inOrder.verify(mockChannel).setConnectionTimeout(10);
        inOrder.verify(mockChannel).setSocketTimeout(11);
        inOrder.verify(mockChannel).setContentType("produces");
        inOrder.verify(mockChannel).setAccept("consumes1,consumes2");
        inOrder.verify(mockChannel).addHeader("h1", "v1");
        inOrder.verify(mockChannel).addHeader("h2", "v2");
        inOrder.verify(mockChannel).addHeader("Cookie", "c1=v1");
        inOrder.verify(mockChannel).addHeader("Cookie", "c2=v2");
        inOrder.verify(mockChannel).writeEntityWith(requestEntityWriter);
        inOrder.verify(mockChannel).send();
        verifyNoMoreInteractions(mockChannel);
    }

    @Test
    public void executeShouldOrchestrateCallsToChannelFactoryWithValuesFromGivenEntityRequest() throws Exception {
        Request request = mock(Request.class);
        HttpResponse httpResponse= mock(HttpResponse.class);
        HttpChannel.Response response= mock(HttpChannel.Response.class);
        HttpChannelResponseHttpResource httpChannelResponseHttpResource = mock(HttpChannelResponseHttpResource.class);
        EntityWriter entityWriter = mock(EntityWriter.class);
        MethodConfig methodConfig = mock(MethodConfig.class);
        RequestEntityWriter requestEntityWriter = mock(RequestEntityWriter.class);

        Iterator<EncodedPair> headers = asList(
                Pairs.toPreEncodedPair("h1", "v1"),
                Pairs.toPreEncodedPair("h2", "v2")
        ).iterator();
        Iterator<EncodedPair> cookies = asList(
                Pairs.toPreEncodedPair("Cookie", "c1=v1"),
                Pairs.toPreEncodedPair("Cookie", "c2=v2")
        ).iterator();

        mockStatic(HttpRequests.class);
        when(HttpRequests.toUrl(request)).thenReturn("some-url");
        when(request.getMethodConfig()).thenReturn(methodConfig);
        when(methodConfig.getCharset()).thenReturn(UTF8);
        when(methodConfig.getType()).thenReturn(MethodType.POST);
        when(methodConfig.getConnectionTimeout()).thenReturn(10);
        when(methodConfig.getSocketTimeout()).thenReturn(11);
        when(methodConfig.getProduces()).thenReturn("produces");
        when(methodConfig.getConsumes()).thenReturn(new String[]{"consumes1","consumes2"});
        when(methodConfig.getEntityWriter()).thenReturn(entityWriter);
        when(mockChannelFactory.open(MethodType.POST, "some-url", UTF8)).thenReturn(mockChannel);
        when(request.getEncodedParamsIterator(ParamType.HEADER)).thenReturn(headers);
        when(request.getEncodedParamsIterator(ParamType.COOKIE)).thenReturn(cookies);
        when(entityWriter.getContentType(request)).thenReturn("content-type");
        whenNew(RequestEntityWriter.class).withArguments(request).thenReturn(requestEntityWriter);
        when(mockChannel.send()).thenReturn(response);
        whenNew(HttpChannelResponseHttpResource.class).withArguments(response).thenReturn(httpChannelResponseHttpResource);
        whenNew(HttpResponse.class).withArguments(
                mockBaseResponseDeserializer,
                mockCustomTypeResponseDeserializer,
                request,
                httpChannelResponseHttpResource
        ).thenReturn(httpResponse);

        assertSame(httpResponse, toTest.execute(request));

        InOrder inOrder = inOrder(mockChannel);
        inOrder.verify(mockChannel).setConnectionTimeout(10);
        inOrder.verify(mockChannel).setSocketTimeout(11);
        inOrder.verify(mockChannel).setContentType("produces");
        inOrder.verify(mockChannel).setAccept("consumes1,consumes2");
        inOrder.verify(mockChannel).addHeader("h1", "v1");
        inOrder.verify(mockChannel).addHeader("h2", "v2");
        inOrder.verify(mockChannel).addHeader("Cookie", "c1=v1");
        inOrder.verify(mockChannel).addHeader("Cookie", "c2=v2");
        inOrder.verify(mockChannel).writeEntityWith(requestEntityWriter);
        inOrder.verify(mockChannel).send();
        verifyNoMoreInteractions(mockChannel);
    }


    @Test
    public void executeShouldOrchestrateCallsToChannelFactoryWithValuesFromGivenEntityRequestWithNullProduces() throws Exception {
        Request request = mock(Request.class);
        HttpResponse httpResponse= mock(HttpResponse.class);
        HttpChannel.Response response= mock(HttpChannel.Response.class);
        HttpChannelResponseHttpResource httpChannelResponseHttpResource = mock(HttpChannelResponseHttpResource.class);
        EntityWriter entityWriter = mock(EntityWriter.class);
        MethodConfig methodConfig = mock(MethodConfig.class);
        RequestEntityWriter requestEntityWriter = mock(RequestEntityWriter.class);

        Iterator<EncodedPair> headers = asList(
                Pairs.toPreEncodedPair("h1", "v1"),
                Pairs.toPreEncodedPair("h2", "v2")
        ).iterator();
        Iterator<EncodedPair> cookies = asList(
                Pairs.toPreEncodedPair("Cookie", "c1=v1"),
                Pairs.toPreEncodedPair("Cookie", "c2=v2")
        ).iterator();

        mockStatic(HttpRequests.class);
        when(HttpRequests.toUrl(request)).thenReturn("some-url");
        when(request.getMethodConfig()).thenReturn(methodConfig);
        when(methodConfig.getCharset()).thenReturn(UTF8);
        when(methodConfig.getType()).thenReturn(MethodType.POST);
        when(methodConfig.getConnectionTimeout()).thenReturn(10);
        when(methodConfig.getSocketTimeout()).thenReturn(11);
        when(methodConfig.getProduces()).thenReturn(null);
        when(methodConfig.getConsumes()).thenReturn(new String[]{"consumes1","consumes2"});
        when(methodConfig.getEntityWriter()).thenReturn(entityWriter);
        when(mockChannelFactory.open(MethodType.POST, "some-url", UTF8)).thenReturn(mockChannel);
        when(request.getEncodedParamsIterator(ParamType.HEADER)).thenReturn(headers);
        when(request.getEncodedParamsIterator(ParamType.COOKIE)).thenReturn(cookies);
        when(entityWriter.getContentType(request)).thenReturn("content-type");
        whenNew(RequestEntityWriter.class).withArguments(request).thenReturn(requestEntityWriter);
        when(mockChannel.send()).thenReturn(response);
        whenNew(HttpChannelResponseHttpResource.class).withArguments(response).thenReturn(httpChannelResponseHttpResource);
        whenNew(HttpResponse.class).withArguments(
                mockBaseResponseDeserializer,
                mockCustomTypeResponseDeserializer,
                request,
                httpChannelResponseHttpResource
        ).thenReturn(httpResponse);

        assertSame(httpResponse, toTest.execute(request));

        InOrder inOrder = inOrder(mockChannel);
        inOrder.verify(mockChannel).setConnectionTimeout(10);
        inOrder.verify(mockChannel).setSocketTimeout(11);
        inOrder.verify(mockChannel).setAccept("consumes1,consumes2");
        inOrder.verify(mockChannel).addHeader("h1", "v1");
        inOrder.verify(mockChannel).addHeader("h2", "v2");
        inOrder.verify(mockChannel).addHeader("Cookie", "c1=v1");
        inOrder.verify(mockChannel).addHeader("Cookie", "c2=v2");
        inOrder.verify(mockChannel).setContentType("content-type");
        inOrder.verify(mockChannel).writeEntityWith(requestEntityWriter);
        inOrder.verify(mockChannel).send();
        verifyNoMoreInteractions(mockChannel);
    }

    @Test
    public void executeShouldOrchestrateCallsToChannelFactoryWithValuesFromGivenEntityLessRequest() throws Exception {
        Request request = mock(Request.class);
        HttpResponse httpResponse= mock(HttpResponse.class);
        HttpChannel.Response response= mock(HttpChannel.Response.class);
        HttpChannelResponseHttpResource httpChannelResponseHttpResource = mock(HttpChannelResponseHttpResource.class);
        MethodConfig methodConfig = mock(MethodConfig.class);

        Iterator<EncodedPair> headers = asList(
                Pairs.toPreEncodedPair("h1", "v1"),
                Pairs.toPreEncodedPair("h2", "v2")
        ).iterator();
        Iterator<EncodedPair> cookies = asList(
                Pairs.toPreEncodedPair("Cookie", "c1=v1"),
                Pairs.toPreEncodedPair("Cookie", "c2=v2")
        ).iterator();

        mockStatic(HttpRequests.class);
        when(HttpRequests.toUrl(request)).thenReturn("some-url");
        when(request.getMethodConfig()).thenReturn(methodConfig);
        when(methodConfig.getCharset()).thenReturn(UTF8);
        when(methodConfig.getType()).thenReturn(MethodType.GET);
        when(methodConfig.getConnectionTimeout()).thenReturn(10);
        when(methodConfig.getSocketTimeout()).thenReturn(11);
        when(methodConfig.getProduces()).thenReturn("produces");
        when(methodConfig.getConsumes()).thenReturn(new String[]{"consumes1","consumes2"});
        when(mockChannelFactory.open(MethodType.GET, "some-url", UTF8)).thenReturn(mockChannel);
        when(request.getEncodedParamsIterator(ParamType.HEADER)).thenReturn(headers);
        when(request.getEncodedParamsIterator(ParamType.COOKIE)).thenReturn(cookies);
        when(mockChannel.send()).thenReturn(response);
        whenNew(HttpChannelResponseHttpResource.class).withArguments(response).thenReturn(httpChannelResponseHttpResource);
        whenNew(HttpResponse.class).withArguments(
                mockBaseResponseDeserializer,
                mockCustomTypeResponseDeserializer,
                request,
                httpChannelResponseHttpResource
        ).thenReturn(httpResponse);

        assertSame(httpResponse, toTest.execute(request));

        InOrder inOrder = inOrder(mockChannel);
        inOrder.verify(mockChannel).setConnectionTimeout(10);
        inOrder.verify(mockChannel).setSocketTimeout(11);
        inOrder.verify(mockChannel).setContentType("produces");
        inOrder.verify(mockChannel).setAccept("consumes1,consumes2");
        inOrder.verify(mockChannel).addHeader("h1", "v1");
        inOrder.verify(mockChannel).addHeader("h2", "v2");
        inOrder.verify(mockChannel).addHeader("Cookie", "c1=v1");
        inOrder.verify(mockChannel).addHeader("Cookie", "c2=v2");
        inOrder.verify(mockChannel).send();
        verifyNoMoreInteractions(mockChannel);
    }

    @Test
    public void finalizeShouldDisposeChannelFactory() throws Throwable {
        toTest.finalize();
        verifyStatic();
        Disposables.dispose(mockChannelFactory);
    }

}
