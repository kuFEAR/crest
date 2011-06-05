/*
 * Copyright 2010 CodeGist.org
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 * ===================================================================
 *
 * More information at http://www.codegist.org.
 */

package org.codegist.crest;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
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
import org.codegist.common.lang.Disposable;
import org.codegist.common.lang.Objects;
import org.codegist.common.log.Logger;
import org.codegist.common.log.LoggingOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ProxySelector;
import java.util.*;

/**
 * RestService implementation based on ASF {@link org.apache.http.client.HttpClient}.
 * <p>This implementation is preferable to the default one {@link org.codegist.crest.DefaultRestService}.
 *
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 * @see org.apache.http.client.HttpClient
 */
public class HttpClientRestService implements RestService, Disposable {

    private static final Logger logger = Logger.getLogger(HttpClientRestService.class);
    public static final String HTTP_CLIENT_PROP = HttpClientRestService.class.getName() + "#http-client";
    private final HttpClient http;


    public HttpClientRestService() {
        this(new DefaultHttpClient());
    }

    /**
     * Construct a HttpClientRestService based on {@link org.apache.http.impl.client.DefaultHttpClient#DefaultHttpClient()}.
     */
    public HttpClientRestService(HttpClient http) {
        this(toConfig(http));
    }

    public HttpClientRestService(Map<String, Object> customProperties) {
        HttpClient http = (HttpClient) customProperties.get(HTTP_CLIENT_PROP);
        if (http == null) {
            http = new DefaultHttpClient();
        }
        this.http = http;
    }

    static Map<String, Object> toConfig(HttpClient http) {
        Map<String, Object> config = new HashMap<String, Object>();
        config.put(HTTP_CLIENT_PROP, http);
        return config;
    }

    public HttpResponse exec(HttpRequest httpRequest) throws HttpException {
        HttpUriRequest request = toHttpUriRequest(httpRequest);
        org.apache.http.HttpResponse response;
        HttpEntity entity = null;
        boolean inError = false;
        try {
            logger.debug("%4s %s", httpRequest.getMeth(), request.getURI());
            logger.trace(request);
            response = http.execute(request);

            if (response == null) {
                throw new HttpException("No Response!", new HttpResponse(httpRequest, -1));
            }

            entity = response.getEntity();
            HttpResponse res;
            if (entity != null) {
                res = new HttpResponse(
                        httpRequest,
                        response.getStatusLine().getStatusCode(),
                        toHeaders(response.getAllHeaders()),
                        new HttpResourceImpl(request, entity));
                if (res.getStatusCode() != HttpStatus.SC_OK) {
                    throw new HttpException(response.getStatusLine().getReasonPhrase(), res);
                }
            } else if (httpRequest.getMeth().equals(HttpRequest.HTTP_HEAD)) {
                res = new HttpResponse(httpRequest,
                        response.getStatusLine().getStatusCode(),
                        toHeaders(response.getAllHeaders()));
            } else {
                throw new HttpException(response.getStatusLine().getReasonPhrase(),
                        new HttpResponse(httpRequest,
                                response.getStatusLine().getStatusCode(),
                                toHeaders(response.getAllHeaders())));
            }
            logger.trace("HTTP Response %s", response);
            return res;
        } catch (HttpException e) {
            inError = true;
            throw e;
        } catch (Throwable e) {
            inError = true;
            throw new HttpException(e, new HttpResponse(httpRequest, -1));
        } finally {
            if (inError) {
                if (entity != null) {
                    try {
                        entity.consumeContent();
                    } catch (IOException e1) {
                        //ignore
                    }
                }
                request.abort();
            }
        }
    }

    private static final Map<String, Class<? extends HttpUriRequest>> METH_MAP = new HashMap<String, Class<? extends HttpUriRequest>>();{
        METH_MAP.put(HttpRequest.HTTP_GET, HttpGet.class);
        METH_MAP.put(HttpRequest.HTTP_POST, HttpPost.class);
        METH_MAP.put(HttpRequest.HTTP_PUT, HttpPut.class);
        METH_MAP.put(HttpRequest.HTTP_DELETE, HttpDelete.class);
        METH_MAP.put(HttpRequest.HTTP_HEAD, HttpHead.class);
        METH_MAP.put(HttpRequest.HTTP_OPTIONS, HttpOptions.class);
    }

    private static Map<String, List<String>> toHeaders(Header[] headers) {
        if (headers == null) return Collections.emptyMap();
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        for (Header h : headers) {
            map.put(h.getName(), Arrays.asList(h.getValue()));/*is that good enough ?????*/
        }
        return map;
    }

    private HttpUriRequest toHttpUriRequest(HttpRequest request) {
        try {
            String url = request.getUrl();

            Class<? extends HttpUriRequest> uriRequestClass = METH_MAP.containsKey(request.getMeth()) ? METH_MAP.get(request.getMeth()) : HttpGet.class;
            HttpUriRequest uriRequest = uriRequestClass.getConstructor(String.class).newInstance(url);

            if (request.getConnectionTimeout() != null && request.getConnectionTimeout() >= 0) {
                HttpConnectionParams.setConnectionTimeout(uriRequest.getParams(), request.getConnectionTimeout().intValue());
            }

            if (request.getSocketTimeout() != null && request.getSocketTimeout() >= 0) {
                HttpConnectionParams.setSoTimeout(uriRequest.getParams(), request.getSocketTimeout().intValue());
            }

            for (HttpParam param : request.getHeaderParams()) {
                uriRequest.addHeader(param.getName(), param.getValue().asString());
            }

            if (uriRequest instanceof HttpEntityEnclosingRequestBase && request.hasEntity()) {
                ((HttpEntityEnclosingRequestBase) uriRequest).setEntity(new HttpRequestHttpEntity(request));
            }

            return uriRequest;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new HttpException(e);
        }
    }

    public static RestService newRestService(Map<String, Object> customProperties) {
        int concurrencyLevel = Objects.defaultIfNull((Integer) customProperties.get(CRestProperty.CREST_CONCURRENCY_LEVEL), 1);
        return newRestService(concurrencyLevel, concurrencyLevel);
    }

    public static RestService newRestService(int maxConcurrentConnection, int maxConnectionPerRoute) {
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
        return new HttpClientRestService(httpClient);
    }

    public void dispose() {
        http.getConnectionManager().shutdown();
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            // should not rely on this though...
            dispose();
        } finally {
            super.finalize();
        }
    }

    HttpClient getHttpClient() {
        return http;
    }

    private class HttpResourceImpl implements HttpResource {

        private final Logger logger = Logger.getLogger(HttpResourceImpl.class);
        private final HttpUriRequest request;
        private final HttpEntity entity;

        public HttpResourceImpl(HttpUriRequest request, HttpEntity entity) {
            this.request = request;
            this.entity = entity;
        }

        public InputStream getContent() throws HttpException {
            try {
                return entity.getContent();
            } catch (IOException e) {
                throw new HttpException(e);
            }
        }

        public void release() throws HttpException {
            try {
                entity.consumeContent();
            } catch (IOException e) {
                logger.warn(e, "Failed to consume content for request %s", request);
            } finally {
                request.abort();
            }
        }
    }

    private class HttpRequestHttpEntity extends AbstractHttpEntity {

        private final HttpRequest request;

        private HttpRequestHttpEntity(HttpRequest request) {
            this.request = request;
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
            OutputStream os = !logger.isTraceOn() ? outstream : new LoggingOutputStream(outstream, logger);
            request.getEntityWriter().writeTo(request, os);
        }

        public boolean isStreaming() {
            return true;
        }
    }
}

