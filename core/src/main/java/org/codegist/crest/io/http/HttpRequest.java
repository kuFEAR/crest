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

package org.codegist.crest.io.http;

import org.codegist.common.lang.ToStringBuilder;
import org.codegist.crest.annotate.Param;
import org.codegist.crest.io.Request;
import org.codegist.crest.config.*;
import org.codegist.crest.io.http.entity.EntityWriter;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.util.Collections.unmodifiableList;
import static org.codegist.common.lang.Objects.asCollection;
import static org.codegist.crest.io.http.HttpParamProcessor.iterateProcess;
import static org.codegist.crest.util.Params.isNull;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class HttpRequest implements Request {

    public static final String DEST_QUERY = "query";
    public static final String DEST_PATH = "path";
    public static final String DEST_MATRIX = "matrix";
    public static final String DEST_FORM = "form";
    public static final String DEST_HEADER = "header";
    public static final String DEST_COOKIE = "cookie";

    private final Request request;

    private final HttpMethod meth;
    private final PathBuilder pathBuilder;
    private final String contentType;
    private final String accept;
    private final Long socketTimeout;
    private final Long connectionTimeout;
    private final String encoding;
    private final Charset charset;
    private final EntityWriter entityWriter;

    private final List<HttpParam> headerParams;
    private final List<HttpParam> matrixParams;
    private final List<HttpParam> queryParams;
    private final List<HttpParam> pathParams;
    private final List<HttpParam> cookieParams;
    private final List<HttpParam> formParams;

    public HttpRequest(Request request, HttpMethod meth, PathBuilder pathBuilder, String contentType, String accept, Long socketTimeout, Long connectionTimeout, String encoding, Charset charset, EntityWriter entityWriter, List<HttpParam> headerParams, List<HttpParam> matrixParams, List<HttpParam> queryParams, List<HttpParam> pathParams, List<HttpParam> cookieParams, List<HttpParam> formParams) {
        this.request = request;
        this.meth = meth;
        this.pathBuilder = pathBuilder;
        this.contentType = contentType;
        this.accept = accept;
        this.socketTimeout = socketTimeout;
        this.connectionTimeout = connectionTimeout;
        this.encoding = encoding;
        this.charset = charset;
        this.entityWriter = entityWriter;
        this.headerParams = unmodifiableList(headerParams);
        this.matrixParams = unmodifiableList(matrixParams);
        this.queryParams = unmodifiableList(queryParams);
        this.pathParams = unmodifiableList(pathParams);
        this.cookieParams = unmodifiableList(cookieParams);
        this.formParams = unmodifiableList(formParams);
    }

    public InterfaceConfig getInterfaceConfig() {
        return request.getInterfaceConfig();
    }

    public MethodConfig getMethodConfig() {
        return request.getMethodConfig();
    }

    public Object[] getArgs() {
        return request.getArgs();
    }

    public HttpMethod getMeth() {
        return meth;
    }

    public PathBuilder getPathBuilder() {
        return pathBuilder;
    }

    public String getContentType() {
        return contentType;
    }

    public String getAccept() {
        return accept;
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

    public List<HttpParam> getFormParams() {
        return formParams;
    }

    public Iterator<Pair> iterateProcessedHeaders() {
        return iterateProcess(headerParams, charset);
    }

    public Iterator<Pair> iterateProcessedMatrixes() {
        return iterateProcess(matrixParams, charset);
    }

    public Iterator<Pair> iterateProcessedQueries() {
        return iterateProcess(queryParams, charset);
    }

    public Iterator<Pair> iterateProcessedPaths() {
        return iterateProcess(pathParams, charset);
    }

    public Iterator<Pair> iterateProcessedCookies() {
        return iterateProcess(cookieParams, charset);
    }

    public Iterator<Pair> iterateProcessedForms() {
        return iterateProcess(formParams, charset);
    }

    public static HttpRequest from(Request request){
        MethodConfig mc = request.getMethodConfig();

        HttpRequest.Builder builder = new HttpRequest.Builder(mc.getPathTemplate(), mc.getBodyWriter(), request.getInterfaceConfig().getEncoding())
                .socketTimeout(mc.getSocketTimeout())
                .connectionTimeout(mc.getConnectionTimeout())
                .contentType(mc.getContentType())
                .accepts(mc.getAccept())
                .action(mc.getHttpMethod())
                .params(mc.getExtraParams());

        for (int i = 0; i < mc.getParamCount(); i++) {
            Object value = request.getArgs()[i];
            ParamConfig pc = mc.getParamConfig(i);
            if(isNull(value) && pc.getDefaultValue() == null) {
                continue;
            }
            builder.addParam(mc.getParamConfig(i), value);
        }
        return builder.build(request);
    }

    public static class Builder {

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

        private String contentType;
        private String accept;
        private HttpMethod meth = METH;
        private Long socketTimeout = null;
        private Long connectionTimeout = null;

        public Builder(PathTemplate pathTemplate, EntityWriter entityWriter, String encoding) {
            this.pathBuilder = pathTemplate.getBuilder(encoding);
            this.entityWriter = entityWriter;
            this.encoding = encoding;
            this.charset = Charset.forName(encoding);
        }

        public Builder(String url, EntityWriter entityWriter, String encoding) {
            this(new SimplePathTemplate(url), entityWriter, encoding);
        }

        public HttpRequest build() {
            return build(null);
        }

        public HttpRequest build(Request request) {
            return new HttpRequest(
                    request,
                    meth,
                    pathBuilder,
                    contentType,
                    accept,
                    socketTimeout,
                    connectionTimeout,
                    encoding,
                    charset,
                    entityWriter,
                    headerParams,
                    matrixParams,
                    queryParams,
                    pathParams,
                    cookieParams,
                    formParams
            );
        }

        public Builder socketTimeout(Long timeout) {
            this.socketTimeout = timeout;
            return this;
        }

        public Builder connectionTimeout(Long timeout) {
            this.connectionTimeout = timeout;
            return this;
        }

        public Builder action(HttpMethod meth) {
            this.meth = meth;
            return this;
        }

        public Builder accepts(String accept){
            this.accept = accept;
            return this;
        }

        public Builder contentType(String contentType){
            this.contentType = contentType;
            return this;
        }

        public Builder params(ParamConfig[] paramConfigs) {
            for(ParamConfig param : paramConfigs){
                addParam(param);
            }
            return this;
        }

        public Builder addParam(String name, String value, String dest, boolean encoded) {
            return addParam(new StringParamConfig(name, value, dest, encoded));
        }

        public Builder addParam(ParamConfig paramConfig) {
            return addParam(paramConfig, paramConfig.getDefaultValue());
        }

        public Builder addParam(ParamConfig paramConfig, Object value) {
            String dest = paramConfig.getDestination().toLowerCase();
            List<HttpParam> params;
            if (DEST_QUERY.equals(dest)) {
                params = queryParams;
            } else if (DEST_PATH.equals(dest)) {
                params = pathParams;
            } else if (DEST_FORM.equals(dest)) {
                params = formParams;
            } else if (DEST_HEADER.equals(dest)) {
                params = headerParams;
            } else if (DEST_COOKIE.equals(dest)) {
                params = cookieParams;
            } else if (DEST_MATRIX.equals(dest)) {
                params = matrixParams;
            } else {
                throw new IllegalStateException("Unsupported destination ! (dest=" + dest + ")");
            }
            params.add(new HttpParam(paramConfig, asCollection(value)));
            return this;
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


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("meth", meth)
                .append("pathBuilder", pathBuilder)
                .append("contentType", contentType)
                .append("accept", accept)
                .append("socketTimeout", socketTimeout)
                .append("connectionTimeout", connectionTimeout)
                .append("encoding", encoding)
                .append("charset", charset)
                .append("entityWriter", entityWriter)
                .append("headerParams", headerParams)
                .append("matrixParams", matrixParams)
                .append("queryParams", queryParams)
                .append("pathParams", pathParams)
                .append("cookieParams", cookieParams)
                .append("formParams", formParams)
                .toString();
    }

}

