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

import java.lang.reflect.Method;
import java.nio.charset.Charset;

/**
 * <p>Reflects a REST interface's method configuration</p>
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public interface MethodConfig {

    /**
     * <p>CRestConfig property to provide a default end-point.</p>
     * <p>Can be overridden by setting this property as follow:</p>
     * <code><pre>
     * String endPoint = ...;
     * CRest crest = CRest.property(MethodConfig.METHOD_CONFIG_DEFAULT_ENDPOINT, endPoint).buid();
     * </pre></code>
     * <p>Default expects the REST interface to specified the @EndPoint annotation. Defaulting it removes the need for the annotation.</p>
     * <p>Expects a String</p>
     */
    String METHOD_CONFIG_DEFAULT_ENDPOINT = MethodConfig.class.getName() + "#endpoint";

    /**
     * <p>CRestConfig property to override the default charset.</p>
     * <p>Can be overridden by setting this property as follow:</p>
     * <code><pre>
     * Charset charset = ...;
     * CRest crest = CRest.property(MethodConfig.METHOD_CONFIG_DEFAULT_CHARSET, charset).buid();
     * </pre></code>
     * <p>Default is UTF-8</p>
     * <p>Expects a {@link java.nio.charset.Charset}</p>
     */
    String METHOD_CONFIG_DEFAULT_CHARSET = MethodConfig.class.getName() + "#charset";

    /**
     * <p>CRestConfig property to override the default socket timeout.</p>
     * <p>Can be overridden by setting this property as follow:</p>
     * <code><pre>
     * Integer timeout = ...;
     * CRest crest = CRest.property(MethodConfig.METHOD_CONFIG_DEFAULT_SO_TIMEOUT, timeout).buid();
     * </pre></code>
     * <p>Default is 20000 milliseconds</p>
     * <p>Expects a {@link java.lang.Integer} that represents milliseconds</p>
     */
    String METHOD_CONFIG_DEFAULT_SO_TIMEOUT = MethodConfig.class.getName() + "#socket-timeout";

    /**
     * <p>CRestConfig property to override the default connection timeout.</p>
     * <p>Can be overridden by setting this property as follow:</p>
     * <code><pre>
     * Integer timeout = ...;
     * CRest crest = CRest.property(MethodConfig.METHOD_CONFIG_DEFAULT_CO_TIMEOUT, timeout).buid();
     * </pre></code>
     * <p>Default is 20000 milliseconds</p>
     * <p>Expects a {@link java.lang.Integer} that represents milliseconds</p>
     */
    String METHOD_CONFIG_DEFAULT_CO_TIMEOUT = MethodConfig.class.getName() + "#connection-timeout";

    /**
     * <p>CRestConfig property to specify a default base path.</p>
     * <p>Can be overridden by setting this property as follow:</p>
     * <code><pre>
     * List&lt;String&gt; pathSegments = ...;
     * CRest crest = CRest.property(MethodConfig.METHOD_CONFIG_DEFAULT_PATH, pathSegments).buid();
     * </pre></code>
     * <p>Default is empty</p>
     * <p>Expects a {@link java.util.List}&lt;{@link String}&gt; that holds the path segments</p>
     */
    String METHOD_CONFIG_DEFAULT_PATH = MethodConfig.class.getName() + "#path";

    /**
     * <p>CRestConfig property to override the default http method.</p>
     * <p>Can be overridden by setting this property as follow:</p>
     * <code><pre>
     * MethodType type = ...;
     * CRest crest = CRest.property(MethodConfig.METHOD_CONFIG_DEFAULT_TYPE, type).buid();
     * </pre></code>
     * <p>Default is GET</p>
     * <p>Expects a {@link org.codegist.crest.config.MethodType}</p>
     */
    String METHOD_CONFIG_DEFAULT_TYPE = MethodConfig.class.getName() + "#type";

    /**
     * <p>CRestConfig property to specify a default media type that the methods will produce.</p>
     * <p>Can be overridden by setting this property as follow:</p>
     * <code><pre>
     * String produces = ...;
     * CRest crest = CRest.property(MethodConfig.METHOD_CONFIG_DEFAULT_PRODUCES, produces).buid();
     * </pre></code>
     * <p>Default is automatically inferred from the context</p>
     * <p>Expects a String</p>
     */
    String METHOD_CONFIG_DEFAULT_PRODUCES = MethodConfig.class.getName() + "#produces";

    /**
     * <p>CRestConfig property to specify a default media type that the methods will handle.</p>
     * <p>Can be overridden by setting this property as follow:</p>
     * <code><pre>
     * List&lt;String&gt; consumes = ...;
     * CRest crest = CRest.property(MethodConfig.METHOD_CONFIG_DEFAULT_CONSUMES, consumes).buid();
     * </pre></code>
     * <p>Default is automatically inferred from the context</p>
     * <p>Expects a {@link java.util.List}&lt;{@link String}&gt; that holds the media types</p>
     */
    String METHOD_CONFIG_DEFAULT_CONSUMES = MethodConfig.class.getName() + "#consumes";

    /**
     * <p>CRestConfig property to specify a list of default extra parameters to add to all requests.</p>
     * <p>Can be overridden by setting this property as follow:</p>
     * <code><pre>
     * ParamConfig[] parameters = ...;
     * CRest crest = CRest.property(MethodConfig.METHOD_CONFIG_DEFAULT_EXTRA_PARAMS, parameters).buid();
     * </pre></code>
     * <p>Default is empty</p>
     * <p>Expects an array of {@link org.codegist.crest.config.ParamConfig}</p>
     */
    String METHOD_CONFIG_DEFAULT_EXTRA_PARAMS = MethodConfig.class.getName() + "#extra-params";

    /**
     * <p>CRestConfig property to override the default response handler.</p>
     * <p>Can be overridden by setting this property as follow:</p>
     * <code><pre>
     * Class&lt;? extends ResponseHandler&gt; responseHandlerClass = ...;
     * CRest crest = CRest.property(MethodConfig.METHOD_CONFIG_DEFAULT_RESPONSE_HANDLER, responseHandlerClass).buid();
     * </pre></code>
     * <p>Default is {@link org.codegist.crest.handler.DefaultResponseHandler}</p>
     * <p>Expects {@link org.codegist.crest.handler.ResponseHandler} subclass</p>
     */
    String METHOD_CONFIG_DEFAULT_RESPONSE_HANDLER = MethodConfig.class.getName() + "#response-handler";

    /**
     * <p>CRestConfig property to override the default error handler.</p>
     * <p>Can be overridden by setting this property as follow:</p>
     * <code><pre>
     * Class&lt;? extends ErrorHandler&gt; errorHandlerClass = ...;
     * CRest crest = CRest.property(MethodConfig.METHOD_CONFIG_DEFAULT_ERROR_HANDLER, errorHandlerClass).buid();
     * </pre></code>
     * <p>Default is {@link org.codegist.crest.handler.ErrorDelegatorHandler}</p>
     * <p>Expects {@link org.codegist.crest.handler.ErrorHandler} subclass</p>
     */
    String METHOD_CONFIG_DEFAULT_ERROR_HANDLER = MethodConfig.class.getName() + "#error-handler";

    /**
     * <p>CRestConfig property to override the default request interceptor.</p>
     * <p>Can be overridden by setting this property as follow:</p>
     * <code><pre>
     * Class&lt;? extends RequestInterceptor&gt; requestInterceptorClass = ...;
     * CRest crest = CRest.property(MethodConfig.METHOD_CONFIG_DEFAULT_REQUEST_INTERCEPTOR, requestInterceptorClass).buid();
     * </pre></code>
     * <p>Default is {@link org.codegist.crest.interceptor.NoOpRequestInterceptor}</p>
     * <p>Expects {@link org.codegist.crest.interceptor.RequestInterceptor} subclass</p>
     */
    String METHOD_CONFIG_DEFAULT_REQUEST_INTERCEPTOR = MethodConfig.class.getName() + "#request-interceptor";

    /**
     * <p>CRestConfig property to override the default retry handler.</p>
     * <p>Can be overridden by setting this property as follow:</p>
     * <code><pre>
     * Class&lt;? extends RetryHandler&gt; retryHandlerClass = ...;
     * CRest crest = CRest.property(MethodConfig.METHOD_CONFIG_DEFAULT_RETRY_HANDLER, retryHandlerClass).buid();
     * </pre></code>
     * <p>Default is {@link org.codegist.crest.handler.MaxAttemptRetryHandler}</p>
     * <p>Expects {@link org.codegist.crest.handler.RetryHandler} subclass</p>
     */
    String METHOD_CONFIG_DEFAULT_RETRY_HANDLER = MethodConfig.class.getName() + "#retry-handler";

    /**
     * <p>CRestConfig property to specify the default deserializers to use. This will override the default deserialization process.</p>
     * <p>Can be overridden by setting this property as follow:</p>
     * <code><pre>
     * List&lt;Class&lt;? extends Deserializer&gt;&gt; deserializerClasses = ...;
     * CRest crest = CRest.property(MethodConfig.METHOD_CONFIG_DEFAULT_DESERIALIZERS, deserializerClasses).buid();
     * </pre></code>
     * <p>Default is automatically inferred from the context (ei: server response's Content-Type)</p>
     * <p>Expects a {@link java.util.List}&lt;{@link Class}&lt;{@link org.codegist.crest.serializer.Deserializer}&gt;&gt;</p>
     */
    String METHOD_CONFIG_DEFAULT_DESERIALIZERS = MethodConfig.class.getName() + "#deserializer";

    /**
     * <p>CRestConfig property to specify the default entity writer to use. This will override the default entity writer selection process.</p>
     * <p>Can be overridden by setting this property as follow:</p>
     * <code><pre>
     * Class&lt;? extends EntityWriter&gt; entityWriterClass = ...;
     * CRest crest = CRest.property(MethodConfig.METHOD_CONFIG_DEFAULT_ENTITY_WRITER, entityWriterClass).buid();
     * </pre></code>
     * <p>Default is automatically inferred from the context</p>
     * <p>Expects {@link org.codegist.crest.entity.EntityWriter} subclass</p>
     */
    String METHOD_CONFIG_DEFAULT_ENTITY_WRITER = MethodConfig.class.getName() + "#entity-writer";

    /**
     * Indicates the encoding used for parameter url-encoding and request entity
     */
    Charset getCharset();

    /**
     * Original REST interface's method
     */
    Method getMethod();

    /**
     * Method's response handler
     */
    ResponseHandler getResponseHandler();

    /**
     * Method's error handler
     */
    ErrorHandler getErrorHandler();

    /**
     * Method's request interceptor
     */
    RequestInterceptor getRequestInterceptor();

    /**
     * Media type that the methods will produce, will be used in the request's Content-Type header if the method's EntityWriter doesn't overrides it
     */
    String getProduces();

    /**
     * Media types that the method will handle. Also used to set the request's Accept header.
     */
    String[] getConsumes();

    /**
     * Method's socket timeout
     */
    int getSocketTimeout();

    /**
     * Method's connection timeout
     */
    int getConnectionTimeout();

    /**
     * Method's return handler
     */
    RetryHandler getRetryHandler();

    /**
     * Method's deserializers. If set, will override the default deserialization process. If not set, the deserializer will be chosen in function of the server response's Content-Type
     */
    Deserializer[] getDeserializers();

    /**
     * Method's URI path template.
     */
    PathTemplate getPathTemplate();

    /**
     * Method's HTTP type
     */
    MethodType getType();

    /**
     * Method's entity writer
     */
    EntityWriter getEntityWriter();

    /**
     * Method's extra parameters that will be added by default for all requests
     */
    ParamConfig[] getExtraParams();

    /**
     * Returns the method's parameter configuration for the argument at the given index
     */
    ParamConfig getParamConfig(int index);

    /**
     * The method's parameter count
     */
    int getParamCount();

}
