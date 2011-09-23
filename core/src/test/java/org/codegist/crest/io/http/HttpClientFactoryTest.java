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

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerPNames;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.ProxySelectorRoutePlanner;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.codegist.crest.CRestConfig;
import org.codegist.crest.test.util.CRestConfigs;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.net.ProxySelector;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({HttpClientFactory.class, Scheme.class, PlainSocketFactory.class,SSLSocketFactory.class,ThreadSafeClientConnManager.class, ConnPerRouteBean.class, BasicHttpParams.class, ProxySelector.class,SchemeRegistry.class, HttpClientHttpChannelFactory.class})
public class HttpClientFactoryTest {

    private final CRestConfig crestConfig = CRestConfigs.mockDefaultBehavior();


    @Test
    public void createWithOneShouldCreateDefaultHttpClient() throws Exception {

        DefaultHttpClient expected = mock(DefaultHttpClient.class);
        ProxySelectorRoutePlanner planner = mock(ProxySelectorRoutePlanner.class);
        ClientConnectionManager clientConnectionManager = mock(ClientConnectionManager.class);
        SchemeRegistry schemeRegistry = mock(SchemeRegistry.class);
        ProxySelector proxySelector = mock(ProxySelector.class);

        when(expected.getConnectionManager()).thenReturn(clientConnectionManager);
        when(clientConnectionManager.getSchemeRegistry()).thenReturn(schemeRegistry);

        mockStatic(ProxySelector.class);
        when(ProxySelector.getDefault()).thenReturn(proxySelector);

        whenNew(DefaultHttpClient.class).withNoArguments().thenReturn(expected);
        whenNew(ProxySelectorRoutePlanner.class)
                .withArguments(schemeRegistry, proxySelector).thenReturn(planner);

        HttpClient actual = HttpClientFactory.create(crestConfig, getClass());
        assertSame(expected, actual);

        verify(expected).setRoutePlanner(planner);
    }

    @Test
    public void createUserSpecificHttpClientShouldReturnIt() throws Exception {
        HttpClient expected = mock(HttpClient.class);
        when(crestConfig.get(getClass().getName() + HttpClientFactory.HTTP_CLIENT)).thenReturn(expected);
        assertSame(expected, HttpClientFactory.create(crestConfig, getClass()));
    }

    @Test
    public void createWithMoreThanOneShouldCreateDefaultHttpClientWithConnectionManagerSetup() throws Exception {
        DefaultHttpClient expected = mock(DefaultHttpClient.class);
        ProxySelectorRoutePlanner planner = mock(ProxySelectorRoutePlanner.class);
        ClientConnectionManager clientConnectionManager = mock(ClientConnectionManager.class);
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        ProxySelector proxySelector = mock(ProxySelector.class);
        BasicHttpParams httpParams = mock(BasicHttpParams.class);
        ConnPerRouteBean routeBean = mock(ConnPerRouteBean.class);
        PlainSocketFactory plainSocketFactory = mock(PlainSocketFactory.class);
        SSLSocketFactory sslSocketFactory = mock(SSLSocketFactory.class);
        Scheme plainScheme = new Scheme("http", plainSocketFactory, 80);
        Scheme sslScheme = new Scheme("https", sslSocketFactory, 443);
        ThreadSafeClientConnManager threadSafeClientConnManager = mock(ThreadSafeClientConnManager.class);

        when(expected.getConnectionManager()).thenReturn(clientConnectionManager);
        when(clientConnectionManager.getSchemeRegistry()).thenReturn(schemeRegistry);

        mockStatic(ProxySelector.class);
        when(ProxySelector.getDefault()).thenReturn(proxySelector);

        mockStatic(PlainSocketFactory.class);
        when(PlainSocketFactory.getSocketFactory()).thenReturn(plainSocketFactory);

        mockStatic(SSLSocketFactory.class);
        when(SSLSocketFactory.getSocketFactory()).thenReturn(sslSocketFactory);

        whenNew(SchemeRegistry.class).withNoArguments().thenReturn(schemeRegistry);
        whenNew(Scheme.class).withArguments("http", plainSocketFactory, 80).thenReturn(plainScheme);
        whenNew(Scheme.class).withArguments("https", sslSocketFactory, 443).thenReturn(sslScheme);
        whenNew(ThreadSafeClientConnManager.class).withArguments(httpParams, schemeRegistry).thenReturn(threadSafeClientConnManager);
        whenNew(ConnPerRouteBean.class).withArguments(2).thenReturn(routeBean);
        whenNew(BasicHttpParams.class).withNoArguments().thenReturn(httpParams);
        whenNew(DefaultHttpClient.class).withArguments(threadSafeClientConnManager, httpParams).thenReturn(expected);
        whenNew(ProxySelectorRoutePlanner.class)
                .withArguments(schemeRegistry, proxySelector).thenReturn(planner);

        when(crestConfig.getConcurrencyLevel()).thenReturn(2);
        HttpClient actual = HttpClientFactory.create(crestConfig, getClass());
        assertSame(expected, actual);

        verify(expected).setRoutePlanner(planner);
        verify(httpParams).setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        verify(httpParams).setParameter(ConnManagerPNames.MAX_CONNECTIONS_PER_ROUTE, routeBean);
        verify(httpParams).setIntParameter(ConnManagerPNames.MAX_TOTAL_CONNECTIONS, 2);
        assertSame(plainScheme, schemeRegistry.getScheme("http"));
        assertSame(sslScheme, schemeRegistry.getScheme("https"));
    }

}
