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

import org.codegist.common.io.IOs;
import org.codegist.crest.config.MethodType;
import org.codegist.crest.param.EncodedPair;
import org.codegist.crest.security.Authorization;
import org.codegist.crest.security.AuthorizationToken;
import org.codegist.crest.test.util.Classes;
import org.codegist.crest.util.Pairs;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.codegist.crest.test.util.Values.UTF8;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.doAnswer;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Pairs.class)
public class AuthorizationHttpChannelTest {

    private final HttpChannel delegate = mock(HttpChannel.class);
    private final Authorization authenticatorManager = mock(Authorization.class);
    private final MethodType methodType = MethodType.getDefault();
    private final EntityParamExtractor entityParamExtractor = mock(EntityParamExtractor.class);
    private final String url = "url?query";
    private final List<EncodedPair> urlParams = asList(
            Pairs.toPreEncodedPair("p1", "v2"),
            Pairs.toPreEncodedPair("p1", "v21"),
            Pairs.toPreEncodedPair("p2", "v2")
    );
    private final Map<String, EntityParamExtractor> httpEntityParamExtrator = new HashMap<String, EntityParamExtractor>();
    {
        httpEntityParamExtrator.put("content-type-1", entityParamExtractor);
        mockStatic(Pairs.class);
        when(Pairs.fromUrlEncoded("query")).thenReturn(urlParams);
    }

    private final AuthorizationHttpChannel toTest = new AuthorizationHttpChannel(delegate, authenticatorManager, methodType, url, UTF8, httpEntityParamExtrator);


    @Test
    public void addHeaderShouldDelegate() throws IOException {
        toTest.addHeader("name", "value");
        verify(delegate).addHeader("name", "value");
    }
    @Test
    public void setHeaderShouldDelegate() throws IOException {
        toTest.setHeader("name", "value");
        verify(delegate).setHeader("name", "value");
    }
    @Test
    public void setAcceptShouldDelegate() throws IOException {
        toTest.setAccept("value");
        verify(delegate).setAccept("value");
    }
    @Test
    public void setSocketTimeoutShouldDelegate() throws IOException {
        toTest.setSocketTimeout(123);
        verify(delegate).setSocketTimeout(123);
    }
    @Test
    public void setConnectionTimeoutShouldDelegate() throws IOException {
        toTest.setConnectionTimeout(123);
        verify(delegate).setConnectionTimeout(123);
    }

    @Test
    public void sendShouldUseEntityParamExtractorWhenContentTypeHasOneAndAuthenticateWithExtractedParams() throws Exception, NoSuchFieldException, IllegalAccessException {
        HttpEntityWriter mockHttpEntityWriter = mock(HttpEntityWriter.class);
        HttpChannel.Response expected = mock(HttpChannel.Response.class);
        AuthorizationToken token = new AuthorizationToken("name", "value");
        ArgumentCaptor<ByteArrayInputStream> baisCaptore = ArgumentCaptor.forClass(ByteArrayInputStream.class);
        ArgumentCaptor<HttpEntityWriter> entityCaptor = ArgumentCaptor.forClass(HttpEntityWriter.class);
        List<EncodedPair> list = asList(
                Pairs.toPreEncodedPair("p4", "v4"),
                Pairs.toPreEncodedPair("p4", "v41")
        );
        EncodedPair[] pairsToAuthenticate = {
                urlParams.get(0),
                urlParams.get(1),
                urlParams.get(2),
                list.get(0),
                list.get(1)
        };
        doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((ByteArrayOutputStream)invocation.getArguments()[0]).write("hello".getBytes());
                return null;
            }
        }).when(mockHttpEntityWriter).writeEntityTo(any(ByteArrayOutputStream.class));
        when(entityParamExtractor.extract(eq("content-type-1;charset=utf-8"), eq(UTF8), baisCaptore.capture())).thenReturn(list);

        when(authenticatorManager.authorize(MethodType.getDefault(), url, pairsToAuthenticate)).thenReturn(token);
        when(delegate.send()).thenReturn(expected);
        when(delegate.send()).thenReturn(expected);
        when(mockHttpEntityWriter.getContentLength()).thenReturn(123);
        toTest.setContentType("content-type-1;charset=utf-8");
        toTest.writeEntityWith(mockHttpEntityWriter);

        assertSame(expected, toTest.send());
        assertEquals("hello", IOs.toString(baisCaptore.getValue()));

        InOrder inOrder = inOrder(delegate);
        inOrder.verify(delegate).writeEntityWith(entityCaptor.capture());
        inOrder.verify(delegate).setHeader("Authorization", "name value");
        inOrder.verify(delegate).send();
        assertTrue(entityCaptor.getValue().getClass().getName().contains("RewritableHttpEntityWriter"));
        assertSame(mockHttpEntityWriter, Classes.getFieldValue(entityCaptor.getValue(), "delegate"));
        assertSame(123, entityCaptor.getValue().getContentLength());

    }


}
