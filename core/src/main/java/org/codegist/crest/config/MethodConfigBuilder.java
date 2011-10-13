/*
 * Copyright 2011 CodeGist.org
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

import java.nio.charset.Charset;

/**
 * {@link org.codegist.crest.config.MethodConfig} builder
 * @author laurent.gilles@codegist.org
 */
public interface MethodConfigBuilder {

    /**
     * Returns an instance of {@link MethodConfig}
     * @return an instance of {@link MethodConfig}
     * @throws Exception Any exception thrown during the instanciation process
     */
    MethodConfig build() throws Exception;

    /**
     * Indicates the encoding used for parameter url-encoding and request entity.
     * @param charset encoding used for parameter url-encoding and request entity
     * @return current builder
     */
    MethodConfigBuilder setCharset(Charset charset);

    /**
     * Defines the media types that the method will handle. Also used to set the request's Accept header.
     * @param mimeTypes the media types that the method will handle
     * @return current builder
     */
    MethodConfigBuilder setConsumes(String... mimeTypes);

    /**
     * Binds a deserializer for the REST interface's method return type
     * @param deserializerClass the deserializer for the REST interface's method return type
     * @return current builder
     */
    MethodConfigBuilder setDeserializer(Class<? extends Deserializer> deserializerClass);

    /**
     * Defines the media type that the REST interface's method will produce, sets the request's Content-Type header
     * @param contentType the media types that the REST interface's method will produce
     * @return current builder
     */
    MethodConfigBuilder setProduces(String contentType);

    /**
     * Starts to configure the REST interface method's parameter at the given index
     * @param index REST interface method's parameter
     * @return parameter config builder
     */
    ParamConfigBuilder startParamConfig(int index);

    /**
     * Starts to configure an extra parameter for the REST interface's method
     * @return current builder
     */
    ParamConfigBuilder startExtraParamConfig();

    /**
     * Appends an URI path segment to the REST interface's method path
     * @param path an URI path segment to append to the REST interface's method path
     * @return current builder
     */
    MethodConfigBuilder appendPath(String path);

    /**
     * Indicates service end-point
     * @param endPoint the service end-point
     * @return current builder
     */
    MethodConfigBuilder setEndPoint(String endPoint);

    /**
     * Indicates the HTTP Method the REST interface's method will issue
     * @param meth the HTTP Method the REST interface's method will issue
     * @return current builder
     */
    MethodConfigBuilder setType(MethodType meth);

    /**
     * Indicates the socket timeout the REST interface's method will use
     * @param socketTimeout the socket timeout the REST interface's method will use
     * @return current builder
     */
    MethodConfigBuilder setSocketTimeout(int socketTimeout);

    /**
     * Indicates the connection timeout the REST interface's method will use
     * @param connectionTimeout the connection timeout the REST interface's method will use
     * @return current builder
     */
    MethodConfigBuilder setConnectionTimeout(int connectionTimeout);

    /**
     * Binds a request interceptor for the REST interface's method, intercepting any request before it gets fired
     * @param requestInterceptorClass request interceptor of the REST interface's method
     * @return current builder
     */
    MethodConfigBuilder setRequestInterceptor(Class<? extends RequestInterceptor> requestInterceptorClass);

    /**
     * Binds a response handler for the REST interface's method
     * @param responseHandlerClass the response handler of the REST interface's method
     * @return current builder
     */
    MethodConfigBuilder setResponseHandler(Class<? extends ResponseHandler> responseHandlerClass);

    /**
     * Binds an error handler for the REST interface's method
     * @param errorHandlerClass the response handler of the REST interface's method
     * @return current builder
     */
    MethodConfigBuilder setErrorHandler(Class<? extends ErrorHandler> errorHandlerClass);

    /**
     * Binds a retry handler for the REST interface's method
     * @param retryHandlerClass the retry handler of the REST interface's method
     * @return current builder
     */
    MethodConfigBuilder setRetryHandler(Class<? extends RetryHandler> retryHandlerClass);

    /**
     * Binds a entity writer for the REST interface's method
     * @param entityWriterClass the entity writer of the REST interface's method
     * @return current builder
     */
    MethodConfigBuilder setEntityWriter(Class<? extends EntityWriter> entityWriterClass);

    /**
     * Binds a parameter serializer for the REST interface method's parameters
     * @param serializerClass parameter serializer of the REST interface method's parameters
     * @return current builder
     */
    MethodConfigBuilder setParamsSerializer(Class<? extends Serializer> serializerClass);

    /**
     * Indicates whether the REST interface method's parameters are pre-encoded or not
     * @param encoded pre-encoded flag for the REST interface method's parameters
     * @return current builder
     */
    MethodConfigBuilder setParamsEncoded(boolean encoded);

    /**
     * Defines a string to use for joining array/Collection values for all REST interface method's parameters
     * @param listSeparator separator to use for joining array/Collection values for all REST interface method's parameters
     * @return current builder
     */
    MethodConfigBuilder setParamsListSeparator(String listSeparator);

}
