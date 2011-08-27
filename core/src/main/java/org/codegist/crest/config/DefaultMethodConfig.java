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

package org.codegist.crest.config;

import org.codegist.crest.entity.EntityWriter;
import org.codegist.crest.handler.ErrorHandler;
import org.codegist.crest.handler.ResponseHandler;
import org.codegist.crest.handler.RetryHandler;
import org.codegist.crest.interceptor.RequestInterceptor;
import org.codegist.crest.serializer.Deserializer;

import java.lang.reflect.Method;
import java.nio.charset.Charset;

class DefaultMethodConfig implements MethodConfig {

    private final Charset charset;
    private final Method method;
    private final PathTemplate path;
    private final String produces;
    private final String[] consumes;
    private final MethodType type;
    private final int socketTimeout;
    private final int connectionTimeout;
    private final EntityWriter entityWriter;
    private final RequestInterceptor requestInterceptor;
    private final ResponseHandler responseHandler;
    private final ErrorHandler errorHandler;
    private final RetryHandler retryHandler;
    private final Deserializer[] deserializers;
    private final ParamConfig[] extraParams;
    private final ParamConfig[] methodParamConfigs;

    DefaultMethodConfig(Charset charset, Method method, PathTemplate path, String produces, String[] consumes, MethodType type, int socketTimeout, int connectionTimeout, EntityWriter entityWriter, RequestInterceptor requestInterceptor, ResponseHandler responseHandler, ErrorHandler errorHandler, RetryHandler retryHandler, Deserializer[] deserializers, ParamConfig[] methodParamConfigs, ParamConfig[] extraParams) {
        this.charset = charset;
        this.method = method;
        this.path = path;
        this.produces = produces;
        this.consumes = consumes.clone();
        this.type = type;
        this.socketTimeout = socketTimeout;
        this.connectionTimeout = connectionTimeout;
        this.entityWriter = entityWriter;
        this.requestInterceptor = requestInterceptor;
        this.responseHandler = responseHandler;
        this.errorHandler = errorHandler;
        this.retryHandler = retryHandler;
        this.deserializers = deserializers.clone();
        this.methodParamConfigs = methodParamConfigs.clone();
        this.extraParams = extraParams.clone();
    }

    public Charset getCharset() {
        return charset;
    }

    public PathTemplate getPathTemplate() {
        return path;
    }

    public ResponseHandler getResponseHandler() {
        return responseHandler;
    }

    public String getProduces() {
        return produces;
    }

    public String[] getConsumes() {
        return consumes.clone();
    }

    public Method getMethod() {
        return method;
    }

    public MethodType getType() {
        return type;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public EntityWriter getEntityWriter() {
        return entityWriter;
    }

    public RequestInterceptor getRequestInterceptor() {
        return requestInterceptor;
    }

    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }

    public RetryHandler getRetryHandler() {
        return retryHandler;
    }

    public Deserializer[] getDeserializers() {
        return deserializers.clone();
    }

    public ParamConfig getParamConfig(int index) {
        return methodParamConfigs[index];
    }

    public int getParamCount() {
        return methodParamConfigs.length;
    }

    public ParamConfig[] getExtraParams() {
        return extraParams.clone();
    }

}
