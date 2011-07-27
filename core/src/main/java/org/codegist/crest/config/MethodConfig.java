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

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public interface MethodConfig {

    String METHOD_CONFIG_DEFAULT_CHARSET = MethodConfig.class.getName() + "#charset";
    String METHOD_CONFIG_DEFAULT_SO_TIMEOUT = MethodConfig.class.getName() + "#socket-timeout";
    String METHOD_CONFIG_DEFAULT_CO_TIMEOUT = MethodConfig.class.getName() + "#connection-timeout";
    String METHOD_CONFIG_DEFAULT_PATH = MethodConfig.class.getName() + "#path";
    String METHOD_CONFIG_DEFAULT_HTTP_METHOD = MethodConfig.class.getName() + "#http-method";
    String METHOD_CONFIG_DEFAULT_PRODUCES = MethodConfig.class.getName() + "#produces";
    String METHOD_CONFIG_DEFAULT_CONSUMES = MethodConfig.class.getName() + "#consumes";
    String METHOD_CONFIG_DEFAULT_EXTRA_PARAMS = MethodConfig.class.getName() + "#extra-params";
    String METHOD_CONFIG_DEFAULT_RESPONSE_HANDLER = MethodConfig.class.getName() + "#response-handler";
    String METHOD_CONFIG_DEFAULT_ERROR_HANDLER = MethodConfig.class.getName() + "#error-handler";
    String METHOD_CONFIG_DEFAULT_REQUEST_INTERCEPTOR = MethodConfig.class.getName() + "#request-interceptor";
    String METHOD_CONFIG_DEFAULT_RETRY_HANDLER = MethodConfig.class.getName() + "#retry-handler";
    String METHOD_CONFIG_DEFAULT_DESERIALIZERS = MethodConfig.class.getName() + "#deserializer";
    String METHOD_CONFIG_DEFAULT_ENTITY_WRITER = MethodConfig.class.getName() + "#body-writer";
    
    Charset getCharset();

    Method getMethod();

    ResponseHandler getResponseHandler();

    ErrorHandler getErrorHandler();

    RequestInterceptor getRequestInterceptor();

    String getProduces();

    String[] getConsumes();

    int getSocketTimeout();

    int getConnectionTimeout();

    RetryHandler getRetryHandler();

    Deserializer[] getDeserializers();

    /**
     * URL fragment specific to this methods.
     * <p>This value can contain placeholders that points to method arguments. For a path as /my-path/{2}/{0}/{2}.json?my-param={1}, any {n} placeholder will be replaced with the serialized parameter found at the respective method argument index when using the default parameter injector.
     *
     * @return the method url fragment
     */
    PathTemplate getPathTemplate();

    MethodType getType();

    EntityWriter getEntityWriter();

    /**
     * Return the method's extra static parameter list
     * @return method's extra parameters
     */
    ParamConfig[] getExtraParams();

    /**
     * Get the MethodParamConfig object holding the configuration of the method's arguments at the requested index.
     *
     * @param index
     * @return The param config object at the specified index, null if not found.
     */
    ParamConfig getParamConfig(int index);

    /**
     * @return The param count.
     */
    int getParamCount();

}
