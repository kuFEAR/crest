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

package org.codegist.crest.http;

import org.codegist.common.lang.Objects;
import org.codegist.crest.EntityWriter;
import org.codegist.crest.RequestContext;
import org.codegist.crest.config.ParamConfig;
import org.codegist.crest.config.PathBuilder;
import org.codegist.crest.config.PathTemplate;
import org.codegist.crest.config.StringParamConfig;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

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

    private final HttpMethod meth;
    private final PathBuilder pathBuilder;
    private final Long socketTimeout;
    private final Long connectionTimeout;
    private final String encoding;
    private final Charset charset;
    private final EntityWriter entityWriter;
    private final RequestContext requestContext;

    private final List<HttpParam> headerParams;
    private final List<HttpParam> matrixParams;
    private final List<HttpParam> queryParams;
    private final List<HttpParam> pathParams;
    private final List<HttpParam> cookieParams;
    private final List<HttpParam> formParam;


    public HttpRequest(HttpMethod meth, PathBuilder pathBuilder, Long socketTimeout, Long connectionTimeout, String encoding, Charset charset, EntityWriter entityWriter, RequestContext requestContext, List<HttpParam> headerParams, List<HttpParam> matrixParams, List<HttpParam> queryParams, List<HttpParam> pathParams, List<HttpParam> cookieParams, List<HttpParam> formParam) {
        this.meth = meth;
        this.pathBuilder = pathBuilder;
        this.socketTimeout = socketTimeout;
        this.connectionTimeout = connectionTimeout;
        this.encoding = encoding;
        this.charset = charset;
        this.entityWriter = entityWriter;
        this.requestContext = requestContext;
        this.headerParams = headerParams;
        this.matrixParams = matrixParams;
        this.queryParams = queryParams;
        this.pathParams = pathParams;
        this.cookieParams = cookieParams;
        this.formParam = formParam;
    }

    public HttpMethod getMeth() {
        return meth;
    }

    public PathBuilder getPathBuilder() {
        return pathBuilder;
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

    public Charset getCharset() {
        return charset;
    }

    public EntityWriter getEntityWriter() {
        return entityWriter;
    }

    public RequestContext getRequestContext() {
        return requestContext;
    }

    public List<HttpParam> getHeaderParams() {
        return headerParams;
    }

    public List<HttpParam> getMatrixParams() {
        return matrixParams;
    }

    public List<HttpParam> getQueryParams() {
        return queryParams;
    }

    public List<HttpParam> getPathParams() {
        return pathParams;
    }

    public List<HttpParam> getCookieParams() {
        return cookieParams;
    }

    public List<HttpParam> getFormParam() {
        return formParam;
    }

    public static class Builder {

        private static final String ENCODING = "UTF-8";
        static final HttpMethod METH = HttpMethod.GET;

        private final PathBuilder pathBuilder;
        private final String encoding;
        private final Charset charset;
        private final EntityWriter entityWriter;

        private final List<HttpParam> headerParams = new ArrayList<HttpParam>();
        private final List<HttpParam> matrixParams = new ArrayList<HttpParam>();
        private final List<HttpParam> queryParams = new ArrayList<HttpParam>();
        private final List<HttpParam> pathParams = new ArrayList<HttpParam>();
        private final List<HttpParam> cookieParams = new ArrayList<HttpParam>();
        private final List<HttpParam> formParams = new ArrayList<HttpParam>();

        private HttpMethod meth = METH;
        private Long socketTimeout = null;
        private Long connectionTimeout = null;
        private RequestContext requestContext;

        public Builder(PathTemplate pathTemplate, EntityWriter entityWriter) throws UnsupportedEncodingException {
            this(pathTemplate, entityWriter, ENCODING);
        }

        public Builder(PathTemplate pathTemplate, EntityWriter entityWriter, String encoding) throws UnsupportedEncodingException {
            this.pathBuilder = pathTemplate.getBuilder(encoding);
            this.entityWriter = entityWriter;
            this.encoding = encoding;
            this.charset = Charset.forName(encoding);
        }

        public Builder(String url, EntityWriter entityWriter) throws UnsupportedEncodingException {
            this(url, entityWriter, ENCODING);
        }

        public Builder(String url, EntityWriter entityWriter, String encoding) throws UnsupportedEncodingException {
            this(new SimplePathTemplate(url), entityWriter, encoding);
        }

        public HttpRequest build() throws URISyntaxException {
            return new HttpRequest(
                    meth,
                    pathBuilder,
                    socketTimeout,
                    connectionTimeout,
                    encoding,
                    charset,
                    entityWriter,
                    requestContext,
                    headerParams,
                    matrixParams,
                    queryParams,
                    pathParams,
                    cookieParams,
                    formParams
            );
        }

        public Builder within(RequestContext requestContext){
            this.requestContext = requestContext;
            return this;
        }

        public Builder timeoutAfter(Long timeout) {
            return timeoutConnectionAfter(timeout).timeoutSocketAfter(timeout);
        }

        public Builder timeoutSocketAfter(Long timeout) {
            this.socketTimeout = timeout;
            return this;
        }

        public Builder timeoutConnectionAfter(Long timeout) {
            this.connectionTimeout = timeout;
            return this;
        }

        public Builder using(HttpMethod meth) {
            this.meth = meth;
            return this;
        }       
        
        private Builder addHttpParam(List<HttpParam> params, ParamConfig paramConfig, Object value){
            params.add(new HttpParam(paramConfig, Objects.asCollection(value)));
            return this;
        }

        public Builder addHeaderParam(String name, String value) {
            return addHeaderParam(name, value, false);
        }
        public Builder addHeaderParam(String name, String value, boolean encoded) {
            return addParam(name, value, HttpRequest.DEST_HEADER, encoded);
        }

        public Builder addQueryParam(String name, String value) {
            return addQueryParam(name, value, false);
        }
        public Builder addQueryParam(String name, String value, boolean encoded) {
            return addParam(name, value, HttpRequest.DEST_QUERY, encoded);
        }

        public Builder addCookieParam(String name, String value) {
            return addCookieParam(name, value, false);
        }
        public Builder addCookieParam(String name, String value, boolean encoded) {
            return addParam(name, value, HttpRequest.DEST_COOKIE, encoded);
        }

        public Builder addFormParam(String name, String value) {
            return addFormParam(name, value, false);
        }
        public Builder addFormParam(String name, String value, boolean encoded) {
            return addParam(name, value, HttpRequest.DEST_FORM, encoded);
        }

        public Builder addPathParam(String name, String value) {
            return addPathParam(name, value, false);
        }
        public Builder addPathParam(String name, String value, boolean encoded) {
            return addParam(name, value, HttpRequest.DEST_PATH, encoded);
        }

        public Builder addMatrixParam(String name, String value) {
            return addMatrixParam(name, value, false);
        }
        public Builder addMatrixParam(String name, String value, boolean encoded) {
            return addParam(name, value, HttpRequest.DEST_MATRIX, encoded);
        }


        public Builder addParam(String name, String value, String dest, boolean encoded) {
            return addParam(new StringParamConfig(name, value, dest, encoded));
        }
        public Builder addParam(ParamConfig paramConfig) {
            return addParam(paramConfig, paramConfig.getDefaultValue());
        }

        public Builder addParam(ParamConfig paramConfig, Object value) {
            String dest = paramConfig.getDestination().toLowerCase();
            if (DEST_QUERY.equals(dest)) {
                return addHttpParam(queryParams, paramConfig, value);
            } else if (DEST_PATH.equals(dest)) {
                return addHttpParam(pathParams, paramConfig, value);
            } else if (DEST_FORM.equals(dest)) {
                return addHttpParam(formParams, paramConfig, value);
            } else if (DEST_HEADER.equals(dest)) {
                return addHttpParam(headerParams, paramConfig, value);
            } else if (DEST_COOKIE.equals(dest)) {
                return addHttpParam(cookieParams, paramConfig, value);
            } else if (DEST_MATRIX.equals(dest)) {
                return addHttpParam(matrixParams, paramConfig, value);
            } else {
                throw new IllegalStateException("Unsupported destination ! (dest=" + dest + ")");
            }
        }

        public HttpMethod getMeth() {
            return meth;
        }

        /* TODO ALL THAT OAUTH BUSINESS STINKS, REFACTOR IT */
        public List<HttpParam> getHeaderParams() {
            return headerParams;
        }

        public List<HttpParam> getFormParams() {
            return formParams;
        }

        public List<HttpParam> getQueryParams() {
            return queryParams;
        }

        public List<HttpParam> getPathParams() {
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

