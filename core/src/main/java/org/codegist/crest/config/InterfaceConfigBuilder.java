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

import java.lang.reflect.Method;
import java.nio.charset.Charset;

/**
 * {@link InterfaceConfig} builder
 * @author laurent.gilles@codegist.org
 */
public interface InterfaceConfigBuilder {

    /**
     * Returns an instance of {@link InterfaceConfig}
     * @return an instance of {@link InterfaceConfig}
     * @throws Exception Any exception thrown during the instanciation process
     */
    InterfaceConfig build() throws Exception;

    /**
     * Starts to configure the given REST interface's method
     * @param meth REST interface method to configure
     * @return method config builder
     */
    MethodConfigBuilder startMethodConfig(Method meth);

    /**
     * Indicates the encoding used for parameter url-encoding and request entity for all REST interface's methods.
     * @param charset encoding used for parameter url-encoding and request entity for all REST interface's methods
     * @return current builder
     */
    InterfaceConfigBuilder setMethodsCharset(Charset charset);

    /**
     * Indicates the socket timeout for all REST interface's methods
     * @param socketTimeout the socket timeout for all REST interface's methods
     * @return current builder
     */
    InterfaceConfigBuilder setMethodsSocketTimeout(int socketTimeout);

    /**
     * Indicates the connection timeout for all REST interface's methods
     * @param connectionTimeout the connection timeout for all REST interface's methods
     * @return current builder
     */
    InterfaceConfigBuilder setMethodsConnectionTimeout(int connectionTimeout);

    /**
     * Binds a request interceptor, intercepting any request before it gets fired for all REST interface's methods 
     * @param requestInterceptorClass the request interceptor for all REST interface's methods
     * @return current builder
     */
    InterfaceConfigBuilder setMethodsRequestInterceptor(Class<? extends RequestInterceptor> requestInterceptorClass);

    /**
     * Binds a response handler for all REST interface's methods
     * @param responseHandlerClass the response handler for all REST interface's methods
     * @return current builder
     */
    InterfaceConfigBuilder setMethodsResponseHandler(Class<? extends ResponseHandler> responseHandlerClass);

    /**
     * Binds an error handler for all REST interface's methods
     * @param errorHandlerClass the response handler for all REST interface's methods
     * @return current builder
     */
    InterfaceConfigBuilder setMethodsErrorHandler(Class<? extends ErrorHandler> errorHandlerClass);

    /**
     * Binds a retry handler for all REST interface's methods
     * @param retryHandlerClass the retry handler for all REST interface's methods
     * @return current builder
     */
    InterfaceConfigBuilder setMethodsRetryHandler(Class<? extends RetryHandler> retryHandlerClass);

    /**
     * Binds a entity writer for all REST interface's methods
     * @param entityWriterClass the entity writer for all REST interface's methods
     * @return current builder
     */
    InterfaceConfigBuilder setMethodsEntityWriter(Class<? extends EntityWriter> entityWriterClass);
                                                                  
    /**
     * Binds a deserializer for all interface method return types
     * @param deserializerClass the deserializer for all interface method return types
     * @return current builder
     */
    InterfaceConfigBuilder setMethodsDeserializer(Class<? extends Deserializer> deserializerClass);
    
    /**
     * Defines the media types that all REST interface's methods will handle. Also used to set the request's Accept header.
     * @param mimeTypes the media types that all REST interface's methods will handle
     * @return current builder
     */
    InterfaceConfigBuilder setMethodsConsumes(String... mimeTypes);

    /**
     * Defines the media type that all REST interface's methods will produce, sets the request's Content-Type header
     * @param contentType the media types that all REST interface's methods will produce
     * @return current builder
     */
    InterfaceConfigBuilder setMethodsProduces(String contentType);

    /**
     * Indicates HTTP Method all REST interface's methods will issue
     * @param meth the HTTP Method all REST interface's methods will issue
     * @return current builder
     */
    InterfaceConfigBuilder setMethodsType(MethodType meth);

    /**
     * Appends an URI path segment for all REST interface's methods
     * @param path an URI path segment to append to all REST interface's methods
     * @return current builder
     */
    InterfaceConfigBuilder appendMethodsPath(String path);

    /**
     * Indicates the service end-point for all REST interface's methods
     * @param endPoint the service end-point for all REST interface's methods
     * @return current builder
     */
    InterfaceConfigBuilder setMethodsEndPoint(String endPoint);

    /**
     * Starts to configure an extra parameter for all REST interface's methods
     * @return current builder
     */
    ParamConfigBuilder startMethodsExtraParamConfig();

    /**
     * Binds a parameter serializer for all REST interface method's parameters
     * @param paramSerializerClass parameter serializer for all REST interface method's parameters
     * @return current builder
     */
    InterfaceConfigBuilder setParamsSerializer(Class<? extends Serializer> paramSerializerClass);

    /**
     * Indicates whether all REST interface method's parameters are pre-encoded or not
     * @param encoded pre-encoded flag for all REST interface method's parameters
     * @return current builder
     */
    InterfaceConfigBuilder setParamsEncoded(boolean encoded);

    /**
     * Defines a string to use for joining array/Collection values for all REST interface method's parameters
     * @param separator separator to use for joining array/Collection values for all REST interface method's parameters
     * @return current builder
     */
    InterfaceConfigBuilder setParamsListSeparator(String separator);
}
