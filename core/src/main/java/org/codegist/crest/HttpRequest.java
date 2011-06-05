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

import org.codegist.common.lang.EqualsBuilder;
import org.codegist.common.lang.HashCodeBuilder;
import org.codegist.common.lang.Strings;
import org.codegist.common.lang.ToStringBuilder;
import org.codegist.crest.config.PathBuilder;
import org.codegist.crest.config.PathTemplate;
import org.codegist.crest.serializer.Serializer;
import org.codegist.crest.serializer.SerializerException;
import org.codegist.crest.serializer.UrlEncodedHttpParamSerializer;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class HttpRequest {

    public static final String DEST_QUERY = "query";
    public static final String DEST_PATH = "path";
    public static final String DEST_MATRIX = "matrix";
    public static final String DEST_FORM = "form";
    public static final String DEST_HEADER = "header";
    public static final String DEST_COOKIE = "cookie";

    public static final String HTTP_GET = "GET";
    public static final String HTTP_POST = "POST";
    public static final String HTTP_PUT = "PUT";
    public static final String HTTP_DELETE = "DELETE";
    public static final String HTTP_HEAD = "HEAD";
    public static final String HTTP_OPTIONS = "OPTIONS";

    private final String meth;
    private final String url;
    private final Long socketTimeout;
    private final Long connectionTimeout;
    private final String encoding;
    private final HttpParamMap headerParams;
    private final HttpParamMap formParams;
    private final EntityWriter entityWriter;
    private final RequestContext requestContext;

    private HttpRequest(RequestContext requestContext, String meth, String url, Long socketTimeout, Long connectionTimeout, String encoding, HttpParamMap headerParams, HttpParamMap formParams, EntityWriter entityWriter) {
        this.requestContext = requestContext;
        this.meth = meth;
        this.url = url;
        this.socketTimeout = socketTimeout;
        this.connectionTimeout = connectionTimeout;
        this.encoding = encoding;
        this.headerParams = headerParams;
        this.formParams = formParams;
        this.entityWriter = entityWriter;
    }

    public boolean hasEntity(){
        return HTTP_POST.equals(meth) || HTTP_PUT.equals(meth);
    }

    public String getMeth() {
        return meth;
    }

    public String getUrl() {
        return url;
    }

    public Long getSocketTimeout() {
        return socketTimeout;
    }

    public Long getConnectionTimeout() {
        return connectionTimeout;
    }

    public String getEncoding() {
        return encoding;
    }

    public Charset getEncodingAsCharset() {
        try {
            return Charset.forName(getEncoding());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public HttpParamMap getHeaderParamMap() {
        return new HttpParamMap(headerParams);
    }

    public List<HttpParam> getHeaderParams() {
        HttpParamMap headers;
        if(entityWriter != null){
            headers = new HttpParamMap(entityWriter.getHeaders(this));
        }else{
            headers = new  HttpParamMap();
        }
        headers.setAll(headerParams);
        return headers.allValues();
    }

    public HttpParamMap getFormParamMap() {
        return new HttpParamMap(formParams);
    }

    public EntityWriter getEntityWriter() {
        return entityWriter;
    }

    public RequestContext getRequestContext() {
        return requestContext;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HttpRequest that = (HttpRequest) o;
        return new EqualsBuilder()
                .append(formParams, that.formParams)
                .append(connectionTimeout, that.connectionTimeout)
                .append(encoding, that.encoding)
                .append(headerParams, that.headerParams)
                .append(meth, that.meth)
                .append(socketTimeout, that.socketTimeout)
                .append(url, that.url)
                .equals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(url)
                .append(meth)
                .append(socketTimeout)
                .append(connectionTimeout)
                .append(encoding)
                .append(headerParams)
                .append(formParams)
                .hashCode();
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append("meth", meth)
                .append("url", url)
                .append("socketTimeout", socketTimeout)
                .append("connectionTimeout", connectionTimeout)
                .append("encoding", encoding)
                .append("headerParams", headerParams)
                .append("formParams", formParams)
                .append("requestContext", requestContext)
                .toString();
    }


    /**
     * Handy builder for HttpRequest objects.
     * <p>The default call :
     * <code><pre>
     * HttpRequest request = new HttpRequest.Builder("http://127.0.0.1").build();
     * </pre></code>
     * <p>Will create an GET utf-8 HttpRequest object.
     */
    public static class Builder {

        
        private static final String MATRIX_SEP = ";";
        private static final String ENCODING = "UTF-8";
        static final String METH = HttpRequest.HTTP_GET;

        private final PathBuilder pathBuilder;
        private final String encoding;
        private final Charset charset;
        private final EntityWriter entityWriter;
        private final HttpParamMap headerParams;
        private final HttpParamMap matrixParams;
        private final HttpParamMap queryParams;
        private final HttpParamMap pathParams;
        private final HttpParamMap formParams;
        private final HttpParamMap cookieParams;

        private String meth = METH;
        private Long socketTimeout = null;
        private Long connectionTimeout = null;
        private RequestContext requestContext;

        private Serializer<Map<String,List<HttpParam>>> matrixParamsSerializer;
        private Serializer<Map<String,List<HttpParam>>> queryParamsSerializer;
        private Serializer<List<HttpParam>> pathParamsSerializer;
        private Serializer<List<HttpParam>> headerParamsSerializer;
        private Serializer<HttpParam> cookieParamSerializer;
        private Serializer<Map<String,List<HttpParam>>> cookieParamsSerializer;


        /**
         * Creates a GET request pointing to the given url
         *
         * @throws URISyntaxException Invalid url
         */
        public Builder(PathTemplate pathTemplate, EntityWriter entityWriter) throws UnsupportedEncodingException {
            this(pathTemplate, entityWriter, ENCODING);
        }

        /**
         * Creates a GET request pointing to the given url
         *
         * @param encoding Url encoding
         * @throws URISyntaxException Invalid url
         */
        public Builder(PathTemplate pathTemplate, EntityWriter entityWriter, String encoding) throws UnsupportedEncodingException {
            this.pathBuilder = pathTemplate.getBuilder(encoding);
            this.entityWriter = entityWriter;
            this.encoding = encoding;
            this.charset = Charset.forName(encoding);
            this.headerParams = new HttpParamMap();
            this.queryParams = new HttpParamMap();
            this.matrixParams = new HttpParamMap();
            this.pathParams = new HttpParamMap();
            this.formParams = new HttpParamMap();
            this.cookieParams = new HttpParamMap();

            this.queryParamsSerializer = UrlEncodedHttpParamSerializer.createDefaultForMap("&");
            this.matrixParamsSerializer = UrlEncodedHttpParamSerializer.createDefaultForMap(MATRIX_SEP);
            this.pathParamsSerializer = null;

            this.cookieParamSerializer = UrlEncodedHttpParamSerializer.createSingleParamSerializer();
            this.headerParamsSerializer = UrlEncodedHttpParamSerializer.createParamValuesSerializer(",");
            this.cookieParamsSerializer = null;
        }

        /**
         * Creates a GET request pointing to the given url
         *
         * @param url
         * @throws URISyntaxException Invalid url
         */
        public Builder(String url, EntityWriter entityWriter) throws UnsupportedEncodingException {
            this(url, entityWriter, ENCODING);
        }

        /**
         * Creates a GET request pointing to the given url
         *
         * @param url
         * @param encoding Url encoding
         * @throws URISyntaxException Invalid url
         */
        public Builder(String url, EntityWriter entityWriter, String encoding) throws UnsupportedEncodingException {
            this(new SimplePathTemplate(url), entityWriter, encoding);
        }

        public HttpRequest build() throws URISyntaxException {
            return new HttpRequest(
                    requestContext,
                    meth,
                    buildBaseUrlString(),
                    socketTimeout,
                    connectionTimeout,
                    encoding,
                    getHeaders(),
                    formParams,
                    entityWriter
            );
        }

        private HttpParamMap getHeaders() {
            HttpParamMap merged = new HttpParamMap();

            for(Map.Entry<String,List<HttpParam>> entry : headerParams.entrySet()){
                String value = headerParamsSerializer.serialize(entry.getValue(), charset);
                merged.set(new HttpParam(entry.getKey(), new StringValue(value), true));
            }
            if(!cookieParams.isEmpty()) {
                // TODO figure out a cleaner way to do it, no if statement
                if(cookieParamsSerializer == null) {
                    for(HttpParam cookie : cookieParams.allValues()){
                        merged.put(new HttpParam("Cookie", cookieParamSerializer.serialize(cookie, charset), true));
                    }
                }else{
                    merged.put(new HttpParam("Cookie", cookieParamsSerializer.serialize(cookieParams, charset), true));
                }
            }

            return merged;
        }
        
        private String buildBaseUrlString() {
            // TODO figure out a cleaner way to do it, no if statement
            if(pathParamsSerializer == null) {
                for (HttpParam pathTemplate : pathParams.allValues()) {
                    pathBuilder.merge(pathTemplate.getName(), pathTemplate.getValue().asString(), pathTemplate.isEncoded());
                }
            }else{
                for (Map.Entry<String,List<HttpParam>> entry : pathParams.entrySet()) {
                    String value = pathParamsSerializer.serialize(entry.getValue(), charset);
                    pathBuilder.merge(entry.getKey(), value, true);
                }
            }
            String url = pathBuilder.build();
            if (matrixParams.size() > 0) {  
                    url += MATRIX_SEP + matrixParamsSerializer.serialize(matrixParams,charset);
            }
            if (queryParams.size() > 0) {
                    url += "?" + queryParamsSerializer.serialize(queryParams,charset);
            }
            return url;
        }

        public Builder mergeMatrixMultiValuedParam(String valueSeparator) throws UnsupportedEncodingException {
            if(Strings.isNotBlank(valueSeparator)) {
                this.matrixParamsSerializer = UrlEncodedHttpParamSerializer.createCollectionMergingForMap(MATRIX_SEP, valueSeparator);
            }
            return this;
        }
        public Builder mergeQueryMultiValuedParam(String valueSeparator) throws UnsupportedEncodingException {
            if(Strings.isNotBlank(valueSeparator)) {
                this.queryParamsSerializer = UrlEncodedHttpParamSerializer.createCollectionMergingForMap("&", valueSeparator);
            }
            return this;
        }
        public Builder mergePathMultiValuedParam(String valueSeparator) throws UnsupportedEncodingException {
            if(Strings.isNotBlank(valueSeparator)) {
                this.pathParamsSerializer = UrlEncodedHttpParamSerializer.createParamValuesSerializer(valueSeparator);
            }
            return this;
        }
        public Builder mergeHeaderMultiValuedParam(String valueSeparator) throws UnsupportedEncodingException {
            if(Strings.isNotBlank(valueSeparator)) {
                this.headerParamsSerializer = UrlEncodedHttpParamSerializer.createParamValuesSerializer(valueSeparator);
            }
            return this;
        }
        public Builder mergeCookieMultiValuedParam(String valueSeparator) throws UnsupportedEncodingException {
            if(Strings.isNotBlank(valueSeparator)) {
                this.cookieParamsSerializer = UrlEncodedHttpParamSerializer.createCollectionMergingForMap(",", valueSeparator);
            }
            return this;
        }

        public Builder within(RequestContext requestContext){
            this.requestContext = requestContext;
            return this;
        }

        /**
         * @param timeout connection and socket timeout used for the resulting request.
         * @return current builder
         */
        public Builder timeoutAfter(Long timeout) {
            return timeoutConnectionAfter(timeout).timeoutSocketAfter(timeout);
        }

        /**
         * @param timeout socket timeout used for the resulting request.
         * @return current builder
         */
        public Builder timeoutSocketAfter(Long timeout) {
            this.socketTimeout = timeout;
            return this;
        }

        /**
         * @param timeout connection timeout used for the resulting request.
         * @return current builder
         */
        public Builder timeoutConnectionAfter(Long timeout) {
            this.connectionTimeout = timeout;
            return this;
        }

        /**
         * @param meth Http method to use to the resulting request.
         * @return current builder
         */
        public Builder using(String meth) {
            this.meth = meth;
            return this;
        }

        /**
         * Adds a request header to the resulting request's headerParams
         *
         * @param name  Header name
         * @param value Header value
         * @return current builder
         */
        public Builder addMatrixParam(String name, String value, Map<String,Object> metas, boolean encode) {
            return addMatrixParam(name, new StringValue(value, metas), encode);
        }
        public Builder addMatrixParam(String name, String value, Map<String,Object> metas) {
            return addMatrixParam(name, value, metas, false);
        }
        public Builder addMatrixParam(String name, Value value, boolean encode) {
            matrixParams.put(new HttpParam(name, value, encode));
            return this;
        }
        public Builder addMatrixParam(String name, Value value) {
            return addMatrixParam(name, value, false);
        }

        /**
         * Adds a request header to the resulting request's headerParams
         *
         * @param name  Header name
         * @param value Header value
         * @return current builder
         */
        public Builder addHeaderParam(String name, String value, Map<String,Object> metas, boolean encode) {
            return addHeaderParam(name, new StringValue(value, metas), encode);
        }
        public Builder addHeaderParam(String name, String value, Map<String,Object> metas) {
            return addHeaderParam(name, value, metas, true);
        }
        public Builder addHeaderParam(String name, String value, boolean encode) {
            return addHeaderParam(name, value, Collections.<String, Object>emptyMap(), encode);
        }
        public Builder addHeaderParam(String name, String value) {
            return addHeaderParam(name, value, Collections.<String, Object>emptyMap(), true);
        }
        public Builder addHeaderParam(String name, Value value, boolean encode) {
            headerParams.put(new HttpParam(name, value, encode));
            return this;
        }
        public Builder addHeaderParam(String name, Value value) {
            return addHeaderParam(name, value, true);
        }

        /**
         * Adds a parameter to the resulting request's path string
         * <p>Name is the path template placeholder where the given value will be merged.
         *
         * @param name  path parameter name
         * @param value path parameter value
         * @return current builder
         */
        public Builder addPathParam(String name, String value, Map<String,Object> metas, boolean encode) {
            return addPathParam(name, new StringValue(value,metas ), encode);
        }
        public Builder addPathParam(String name, String value, Map<String,Object> metas) {
            return addPathParam(name, value, metas, false);
        }
        public Builder addPathParam(String name, Value value, boolean encode) {
            this.pathParams.put(new HttpParam(name, value, encode));
            return this;
        }
        public Builder addPathParam(String name, Value value) {
            return addPathParam(name, value, false);
        }

        /**
         * Adds a parameter to the resulting request's query string.
         *
         * @param name  query string parameter name or placeholder name
         * @param value query string parameter value or placeholder name
         * @return current builder
         */
        public Builder addQueryParam(String name, String value, Map<String,Object> metas, boolean encoded) {
            return addQueryParam(name, new StringValue(value, metas), encoded);
        }
        public Builder addQueryParam(String name, String value, Map<String,Object> metas) {
            return addQueryParam(name, value, metas, false);
        }
        public Builder addQueryParam(String name, String value, boolean encoded) {
            return addQueryParam(name, value, Collections.<String, Object>emptyMap(), encoded);
        }
        public Builder addQueryParam(String name, String value) {
            return addQueryParam(name, value, Collections.<String, Object>emptyMap(), false);
        }
        public Builder addQueryParam(String name, Value value, boolean encoded) {
            queryParams.put(new HttpParam(name, value, encoded));
            return this;
        }
        public Builder addQueryParam(String name, Value value) {
            return addQueryParam(name, value, false);
        }

        /**
         * Adds a body parameter to the resulting request's body parameters
         *
         * @param name  query string parameter name
         * @param value query string parameter value
         * @return current builder
         */
        public Builder addFormParam(String name, Value value, boolean encoded) {
            formParams.put(new HttpParam(name, value, encoded));
            return this;
        }
        public Builder addFormParam(String name, Value value) {
            return addFormParam(name, value, false);
        }
        public Builder addFormParam(String name, String value, Map<String,Object> metas) {
            return addFormParam(name, new StringValue(value, metas), false);
        }
        public Builder addFormParam(String name, String value) {
            return addFormParam(name, value, Collections.<String, Object>emptyMap());
        }


        public Builder addCookieParam(String name, String value, Map<String,Object> metas, boolean encoded) {
            return addCookieParam(name, new StringValue(value, metas), encoded);
        }
        public Builder addCookieParam(String name, String value, Map<String,Object> metas) {
            return addCookieParam(name, value, metas, false);
        }
        public Builder addCookieParam(String name, Value value, boolean encoded) {
            cookieParams.put(new HttpParam(name, value, encoded));
            return this;
        }
        public Builder addCookieParam(String name, Value value) {
            return addCookieParam(name, value, false);
        }


        public Builder addParam(String name, String value, String dest, Map<String,Object> metas)  {
            return addParam(name, new StringValue(value, metas), dest);
        }
        
        public Builder addParam(String name, Value value, String dest) {
            return addParam(name, value, dest, false);
        }

        public Builder addParam(String name, String value, String dest, Map<String,Object> metas, boolean encoded) {
            return addParam(name, new StringValue(value, metas), dest, encoded);
        }
        /**
         * Adds a parameter to the given destination in the final http request
         *
         * @param name  name of the parameter
         * @param value value of the parameter
         * @param dest  parameter destination
         * @return current builder
         */
        public Builder addParam(String name, Value value, String dest, boolean encoded) {
            dest = dest.toLowerCase();
            if (DEST_QUERY.equals(dest)) {
                return addQueryParam(name, value, encoded);
            } else if (DEST_PATH.equals(dest)) {
                return addPathParam(name, value, encoded);
            } else if (DEST_FORM.equals(dest)) {
                return addFormParam(name, value, encoded);
            } else if (DEST_HEADER.equals(dest)) {
                return addHeaderParam(name, value, encoded);
            } else if (DEST_COOKIE.equals(dest)) {
                return addCookieParam(name, value, encoded);
            } else if (DEST_MATRIX.equals(dest)) {
                return addMatrixParam(name, value, encoded);
            } else {
                throw new IllegalStateException("Unsupported destination ! (dest=" + dest + ")");
            }
        }

        public String getMeth() {
            return meth;
        }

        public String getBaseUrl() {
            return buildBaseUrlString();
        }

        public HttpParamMap getHeaderParams() {
            return headerParams;
        }

        public HttpParamMap getFormParams() {
            return formParams;
        }

        public HttpParamMap getQueryParams() {
            return queryParams;
        }

        public HttpParamMap getPathParams() {
            return pathParams;
        }

        public Long getSocketTimeout() {
            return socketTimeout;
        }

        public Long getConnectionTimeout() {
            return connectionTimeout;
        }

        public String getEncoding() {
            return encoding;
        }

        private static class SimplePathTemplate implements PathTemplate {
            private final String path;

            private SimplePathTemplate(String path) {
                this.path = path;
            }

            public PathBuilder getBuilder(String encoding) {
                return new SimplePathBuilder(path);
            }

            public String getUrlTemplate() {
                return path;
            }
        }

        private static class SimplePathBuilder implements PathBuilder {
            private final String path;

            private SimplePathBuilder(String path) {
                this.path = path;
            }

            public PathBuilder merge(String templateName, String templateValue, boolean encoded) {
                return this;
            }

            public String build() {
                return path;
            }
        }


    }


}

