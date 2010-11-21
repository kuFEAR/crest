package org.codegist.crest;

import org.codegist.common.lang.Strings;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * RestService implementation based on ASF {@link org.apache.http.client.HttpClient}.
 * <p>This implementation is preferable to the default one {@link org.codegist.crest.DefaultRestService}.
 *
 * @see org.apache.http.client.HttpClient
 */
public class HttpClientRestService implements RestService {

    private final HttpClient http;

    /**
     * Construct a HttpClientRestService based on {@link org.apache.http.impl.client.DefaultHttpClient#DefaultHttpClient()}.
     */
    public HttpClientRestService() {
        this(new DefaultHttpClient());
    }

    public HttpClientRestService(HttpClient http) {
        this.http = http;

    }

    public void shutdown() {
        http.getConnectionManager().shutdown();
    }

    public static RestService newRestService(int maxConcurrentConnection, int maxConnectionPerRoute) {
        HttpClient httpClient;
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
        return new HttpClientRestService(httpClient);
    }

    public HttpResponse exec(HttpRequest httpRequest) throws HttpException {
        HttpUriRequest request;
        try {
            request = toHttpUriRequest(httpRequest);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        org.apache.http.HttpResponse response;
        HttpEntity entity = null;
        boolean inError = false;
        try {
            response = http.execute(request);
            if (response == null) {
                throw new HttpException("No Response!", new HttpResponse(httpRequest, -1));
            }
            entity = response.getEntity();
            if (entity != null) {
                HttpResponse res = new HttpResponse(httpRequest, response.getStatusLine().getStatusCode(),
                        entity.getContent(),
                        entity.getContentEncoding() != null ? entity.getContentEncoding().getValue() : null);
                if (res.getStatusCode() != HttpStatus.SC_OK) {
                    throw new HttpException(response.getStatusLine().getReasonPhrase(), res);
                } else {
                    return res;
                }
            } else if (httpRequest.getMeth().equals(HttpMethod.HEAD)) {
                return new HttpResponse(httpRequest, response.getStatusLine().getStatusCode());
            } else {
                throw new HttpException(response.getStatusLine().getReasonPhrase(), new HttpResponse(httpRequest, response.getStatusLine().getStatusCode()));
            }
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

    private static HttpUriRequest toHttpUriRequest(HttpRequest request) throws UnsupportedEncodingException {
        HttpUriRequest uriRequest;


        String queryString = "";
        if (request.getQueryParams() != null) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            for (Map.Entry<String, String> entry : request.getQueryParams().entrySet()) {
                params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            String qs = URLEncodedUtils.format(params, request.getEncoding());
            queryString = Strings.isNotBlank(qs) ? ("?" + qs) : "";
        }
        String uri = request.getUri().toString() + queryString;

        switch (request.getMeth()) {
            default:
            case GET:
                uriRequest = new HttpGet(uri);
                break;
            case POST:
                uriRequest = new HttpPost(uri);
                break;
            case PUT:
                uriRequest = new HttpPut(uri);
                break;
            case DELETE:
                uriRequest = new HttpDelete(uri);
                break;
            case HEAD:
                uriRequest = new HttpHead(uri);
                break;
        }
        if (uriRequest instanceof HttpEntityEnclosingRequestBase) {
            HttpEntityEnclosingRequestBase enclosingRequestBase = ((HttpEntityEnclosingRequestBase) uriRequest);
            HttpEntity entity;
            if (Params.isForUpload(request.getBodyParams().values())) {
                MultipartEntity multipartEntity = new MultipartEntity();
                for (Map.Entry<String, Object> param : request.getBodyParams().entrySet()) {
                    ContentBody body;
                    if (param.getValue() instanceof InputStream) {
                        body = new InputStreamBody((InputStream) param.getValue(), param.getKey());
                    } else if (param.getValue() instanceof File) {
                        body = new FileBody((File) param.getValue());
                    } else if (param.getValue() != null) {
                        body = new StringBody(param.getValue().toString(), request.getEncodingAsCharset());
                    } else {
                        body = new StringBody(null);
                    }
                    multipartEntity.addPart(param.getKey(), body);
                }
                entity = multipartEntity;
            } else {
                List<NameValuePair> params = new ArrayList<NameValuePair>(request.getBodyParams().size());
                for (Map.Entry<String, Object> param : request.getBodyParams().entrySet()) {
                    params.add(new BasicNameValuePair(param.getKey(), param.getValue() != null ? param.getValue().toString() : null));
                }
                entity = new UrlEncodedFormEntity(params, request.getEncoding());
            }

            enclosingRequestBase.setEntity(entity);
        }

        if (request.getHeaders() != null && !request.getHeaders().isEmpty()) {
            for (Map.Entry<String, String> header : request.getHeaders().entrySet()) {
                uriRequest.setHeader(header.getKey(), header.getValue());
            }
        }

        if (request.getConnectionTimeout() != null && request.getConnectionTimeout() >= 0) {
            HttpConnectionParams.setConnectionTimeout(uriRequest.getParams(), request.getConnectionTimeout().intValue());
        }

        if (request.getSocketTimeout() != null && request.getSocketTimeout() >= 0) {
            HttpConnectionParams.setSoTimeout(uriRequest.getParams(), request.getSocketTimeout().intValue());
        }

        return uriRequest;
    }
}
