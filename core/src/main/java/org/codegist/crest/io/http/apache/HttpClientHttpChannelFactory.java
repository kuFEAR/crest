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

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.ProxySelectorRoutePlanner;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.codegist.common.lang.Disposable;
import org.codegist.crest.CRestProperty;
import org.codegist.crest.config.MethodType;
import org.codegist.crest.io.http.HttpChannel;
import org.codegist.crest.io.http.HttpChannelFactory;

import java.net.ProxySelector;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Map;

/**
* @author Laurent Gilles (laurent.gilles@codegist.org)
*/
public final class HttpClientHttpChannelFactory implements HttpChannelFactory, Disposable {

    private static final int HTTP_PORT = 80;
    private static final int HTTPS_PORT = 443;
    private final HttpClient client;

    private HttpClientHttpChannelFactory(HttpClient client) {
        this.client = client;
    }

    public HttpChannel open(MethodType methodType, String url, Charset charset) {
        HttpUriRequest request;
        switch(methodType) {
            case GET:
                request = new HttpGet(url);
            break;
            case POST:
                request = new HttpPost(url);
            break;
            case PUT:
                request = new HttpPut(url);
            break;
            case DELETE:
                request = new HttpDelete(url);
            break;
            case OPTIONS:
                request = new HttpOptions(url);
            break;
            case HEAD:
                request = new HttpHead(url);
            break;
            default:
                throw new IllegalArgumentException("Method " + methodType + " not supported");
        }
        return new HttpClientHttpChannel(client, request);
    }

    public static HttpChannelFactory newHttpChannelInitiator() {
        return newHttpChannelInitiator(Collections.<String, Object>emptyMap());
    }
    
    public static HttpChannelFactory newHttpChannelInitiator(Map<String, Object> crestProperties) {
        int concurrencyLevel = CRestProperty.getConcurrencyLevel(crestProperties);
        return newHttpChannelInitiator(concurrencyLevel, concurrencyLevel);
    }

    public static HttpChannelFactory newHttpChannelInitiator(int maxConcurrentConnection, int maxConnectionPerRoute) {
        DefaultHttpClient httpClient;
        if (maxConcurrentConnection > 1 || maxConnectionPerRoute > 1) {
            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            if (maxConnectionPerRoute > 1) {
                ConnManagerParams.setMaxConnectionsPerRoute(params, new ConnPerRouteBean(maxConnectionPerRoute));
            }
            if (maxConcurrentConnection > 1) {
                ConnManagerParams.setMaxTotalConnections(params, maxConcurrentConnection);
            } else {
                ConnManagerParams.setMaxTotalConnections(params, 1);
            }

            SchemeRegistry schemeRegistry = new SchemeRegistry();
            schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), HTTP_PORT));
            schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), HTTPS_PORT));

            ClientConnectionManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);
            httpClient = new DefaultHttpClient(cm, params);
        } else {
            httpClient = new DefaultHttpClient();
        }

        httpClient.setRoutePlanner(new ProxySelectorRoutePlanner(httpClient.getConnectionManager().getSchemeRegistry(), ProxySelector.getDefault()));
        return new HttpClientHttpChannelFactory(httpClient);
    }

    public void dispose() {
        client.getConnectionManager().shutdown();
    }

}
