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

package org.codegist.crest.io.http.apache;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.codegist.crest.config.MethodType;
import org.codegist.crest.io.http.HttpChannel;
import org.codegist.crest.test.util.Values;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({HttpClientHttpChannelFactory.class,HttpClientHttpChannel.class})
public class HttpClientHttpChannelFactoryTest {

    private final HttpClient client = mock(HttpClient.class);
    private final HttpClientHttpChannelFactory toTest = new HttpClientHttpChannelFactory(client);

    @Test
    public void openShouldCreateAHttpClientHttpChannelInstanceWithClientForGET() throws Exception {
        HttpGet expectedRequest = mock(HttpGet.class);
        whenNew(HttpGet.class).withArguments("url").thenReturn(expectedRequest);
        openShouldCreateAHttpClientHttpChannelInstanceWithClientFor(expectedRequest, MethodType.GET);
    }

    @Test
    public void openShouldCreateAHttpClientHttpChannelInstanceWithClientForPOST() throws Exception {
        HttpPost expectedRequest = mock(HttpPost.class);
        whenNew(HttpPost.class).withArguments("url").thenReturn(expectedRequest);
        openShouldCreateAHttpClientHttpChannelInstanceWithClientFor(expectedRequest, MethodType.POST);
    }

    @Test
    public void openShouldCreateAHttpClientHttpChannelInstanceWithClientForPUT() throws Exception {
        HttpPut expectedRequest = mock(HttpPut.class);
        whenNew(HttpPut.class).withArguments("url").thenReturn(expectedRequest);
        openShouldCreateAHttpClientHttpChannelInstanceWithClientFor(expectedRequest, MethodType.PUT);
    }

    @Test
    public void openShouldCreateAHttpClientHttpChannelInstanceWithClientForDELETE() throws Exception {
        HttpDelete expectedRequest = mock(HttpDelete.class);
        whenNew(HttpDelete.class).withArguments("url").thenReturn(expectedRequest);
        openShouldCreateAHttpClientHttpChannelInstanceWithClientFor(expectedRequest, MethodType.DELETE);
    }

    @Test
    public void openShouldCreateAHttpClientHttpChannelInstanceWithClientForOPTIONS() throws Exception {
        HttpOptions expectedRequest = mock(HttpOptions.class);
        whenNew(HttpOptions.class).withArguments("url").thenReturn(expectedRequest);
        openShouldCreateAHttpClientHttpChannelInstanceWithClientFor(expectedRequest, MethodType.OPTIONS);
    }

    @Test
    public void openShouldCreateAHttpClientHttpChannelInstanceWithClientForHEAD() throws Exception {
        HttpHead expectedRequest = mock(HttpHead.class);
        whenNew(HttpHead.class).withArguments("url").thenReturn(expectedRequest);
        openShouldCreateAHttpClientHttpChannelInstanceWithClientFor(expectedRequest, MethodType.HEAD);
    }

    @Test
    public void disposeShouldShutdownConnectionManager(){
        ClientConnectionManager clientConnectionManager = mock(ClientConnectionManager.class);
        when(client.getConnectionManager()).thenReturn(clientConnectionManager);

        toTest.dispose();

        verify(clientConnectionManager).shutdown();
    }

    @Test
    public void finalizeShouldShutdownConnectionManager() throws Throwable {
        ClientConnectionManager clientConnectionManager = mock(ClientConnectionManager.class);
        when(client.getConnectionManager()).thenReturn(clientConnectionManager);

        toTest.finalize();

        verify(clientConnectionManager).shutdown();
    }



    private void openShouldCreateAHttpClientHttpChannelInstanceWithClientFor(HttpUriRequest expectedRequest, MethodType methodType) throws Exception {
        HttpClientHttpChannel expected = mock(HttpClientHttpChannel.class);
        HttpParams params = mock(HttpParams.class);

        when(expectedRequest.getParams()).thenReturn(params);
        whenNew(HttpClientHttpChannel.class).withArguments(client, expectedRequest).thenReturn(expected);

        HttpChannel actual = toTest.open(methodType, "url", Values.UTF8);

        assertSame(expected,actual);
        verify(params).setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, "UTF-8");
    }
}
