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

package org.codegist.crest.http;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
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
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.ProxySelectorRoutePlanner;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.codegist.common.io.EmptyInputStream;
import org.codegist.common.lang.Disposable;
import org.codegist.common.lang.Objects;
import org.codegist.common.log.Logger;
import org.codegist.crest.CRestProperty;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ProxySelector;
import java.util.Map;

/**
* @author Laurent Gilles (laurent.gilles@codegist.org)
*/
public class HttpClientHttpChannelInitiator implements HttpChannelInitiator, Disposable {

    private final HttpClient client;

    private HttpClientHttpChannelInitiator(HttpClient client) {
        this.client = client;
    }

    public HttpChannel initiate(HttpMethod method, String url) {
        HttpUriRequest request;
        switch(method) {
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
                throw new IllegalArgumentException("Method " + method + " not supported");
        }
        return new HttpClientHttpChannel(client, request);
    }

    public static HttpChannelInitiator newHttpChannelInitiator(Map<String, Object> customProperties) {
        int concurrencyLevel = Objects.defaultIfNull((Integer) customProperties.get(CRestProperty.CREST_CONCURRENCY_LEVEL), 1);
        return newHttpChannelInitiator(concurrencyLevel, concurrencyLevel);
    }

    public static HttpChannelInitiator newHttpChannelInitiator(int maxConcurrentConnection, int maxConnectionPerRoute) {
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
            schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

            ClientConnectionManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);
            httpClient = new DefaultHttpClient(cm, params);
        } else {
            httpClient = new DefaultHttpClient();
        }
        httpClient.setRoutePlanner(new ProxySelectorRoutePlanner(httpClient.getConnectionManager().getSchemeRegistry(), ProxySelector.getDefault()));
        return new HttpClientHttpChannelInitiator(httpClient);
    }

    public void dispose() {
        client.getConnectionManager().shutdown();
    }





    private static class HttpClientHttpChannel implements HttpChannel, Disposable {

        private static final Logger LOGGER = Logger.getLogger(HttpClientHttpChannel.class);
        private final HttpClient client;
        private final HttpUriRequest request;
        private volatile org.apache.http.HttpResponse response;

        private HttpClientHttpChannel(HttpClient client, HttpUriRequest request){
            this.request = request;
            this.client = client;
        }

        public void setConnectionTimeout(int timeout) throws IOException {
            HttpConnectionParams.setConnectionTimeout(request.getParams(), timeout);
        }

        public void setSocketTimeout(int timeout) throws IOException  {
            HttpConnectionParams.setSoTimeout(request.getParams(), timeout);
        }

        public void setHeader(String name, String value) throws IOException  {
            request.setHeader(name, value);
        }

        public void addHeader(String name, String value) throws IOException  {
            request.addHeader(name, value);
        }

        public void writeEntityWith(HttpEntityWriter httpEntityWriter)  throws IOException {
            ((HttpEntityEnclosingRequest) request).setEntity(new HttpEntityWriterHttpEntity(httpEntityWriter));
        }

        public int send()  throws IOException {
            response = client.execute(request);
            return response.getStatusLine().getStatusCode();
        }

        public InputStream getResponseStream() throws IOException  {
            HttpEntity entity = response.getEntity();
            InputStream stream = entity != null ? entity.getContent() : EmptyInputStream.INSTANCE;
            return stream;
        }

        public String readContentType() {
            Header header = response.getFirstHeader("Content-Type");
            if(header != null) {
                return header.getValue();
            }else{
                return null;
            }
        }

        public String readContentEncoding() {
            Header header = response.getFirstHeader("Content-Encoding");
            if(header != null) {
                return header.getValue();
            }else{
                return null;
            }
        }


        public void dispose() {
            try {
                if(response.getEntity() != null) {
                    response.getEntity().consumeContent();
                }

            } catch (IOException e) {
                LOGGER.warn(e, "Failed to consume content for request %s", request);
            } finally {
                request.abort();
            }
        }

        private class HttpEntityWriterHttpEntity extends AbstractHttpEntity {

            private final HttpEntityWriter writer;

            private HttpEntityWriterHttpEntity(HttpEntityWriter writer) {
                this.writer = writer;
            }

            public boolean isRepeatable() {
                return false;
            }

            public long getContentLength() {
                return -1;
            }

            public InputStream getContent() throws IOException, IllegalStateException {
                throw new UnsupportedOperationException();
            }

            public void writeTo(OutputStream outstream) throws IOException {
                writer.writeEntityTo(outstream);
            }

            public boolean isStreaming() {
                return true;
            }
        }


    }

}
