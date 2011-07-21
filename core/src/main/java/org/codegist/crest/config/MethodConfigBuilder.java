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

package org.codegist.crest.config;

import org.codegist.crest.entity.EntityWriter;
import org.codegist.crest.handler.ErrorHandler;
import org.codegist.crest.handler.ResponseHandler;
import org.codegist.crest.handler.RetryHandler;
import org.codegist.crest.interceptor.RequestInterceptor;
import org.codegist.crest.serializer.Deserializer;
import org.codegist.crest.serializer.Serializer;

/**
 * @author laurent.gilles@codegist.org
 */
public interface MethodConfigBuilder {

    MethodConfig build() throws Exception;

    MethodConfigBuilder setCharset(String charset);

    MethodConfigBuilder setConsumes(String... mimeTypes);

    MethodConfigBuilder setDeserializer(Class<? extends Deserializer> deserializer);

    MethodConfigBuilder setProduces(String contentType);

    ParamConfigBuilder startParamConfig(int index);

    ParamConfigBuilder startExtraParamConfig();

    MethodConfigBuilder appendPath(String path);

    MethodConfigBuilder setEndPoint(String endPoint);

    MethodConfigBuilder setType(MethodType meth);

    MethodConfigBuilder setSocketTimeout(int socketTimeout);

    MethodConfigBuilder setConnectionTimeout(int connectionTimeout);

    MethodConfigBuilder setRequestInterceptor(Class<? extends RequestInterceptor> interceptorCls);

    MethodConfigBuilder setResponseHandler(Class<? extends ResponseHandler> responseHandlerClass);

    MethodConfigBuilder setErrorHandler(Class<? extends ErrorHandler> methodHandlerClass);

    MethodConfigBuilder setRetryHandler(Class<? extends RetryHandler> retryHandlerClass);

    MethodConfigBuilder setEntityWriter(Class<? extends EntityWriter> bodyWriterClass);

    MethodConfigBuilder setParamsSerializer(Class<? extends Serializer> paramSerializer);

    MethodConfigBuilder setParamsEncoded(boolean encoded);

    MethodConfigBuilder setParamsListSeparator(String listSeparator);

}
