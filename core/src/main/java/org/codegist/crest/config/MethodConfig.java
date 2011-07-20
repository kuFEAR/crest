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
import org.codegist.crest.handler.*;
import org.codegist.crest.interceptor.NoOpRequestInterceptor;
import org.codegist.crest.interceptor.RequestInterceptor;
import org.codegist.crest.serializer.Deserializer;

import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public interface MethodConfig {

     String DEFAULT_CHARSET = "UTF-8";

    /**
     * Default socket timeout applied when non specified.
     *
     * @see org.codegist.crest.config.MethodConfig#getSocketTimeout()
     */
    int DEFAULT_SO_TIMEOUT = 20000;

    /**
     * Default connection timeout applied when non specified.
     *
     * @see MethodConfig#getConnectionTimeout()
     */
    int DEFAULT_CO_TIMEOUT = 20000;

    /**
     * Default url fragment applied when non specified.
     *
     * @see MethodConfig#getPathTemplate()
     */
    String DEFAULT_PATH = "";

    String DEFAULT_PRODUCES = null;

    List<String> DEFAULT_CONSUMES = unmodifiableList(asList("*/*"));

    /**
     * Default method extra params.
     *
     * @see org.codegist.crest.config.MethodConfig#getExtraParams()
     */
    ParamConfig[] DEFAULT_EXTRA_PARAMS = new ParamConfig[0];

    /**
     * Default http method applied when non specified.
     *
     */
    MethodType DEFAULT_METHOD_TYPE = MethodType.getDefault();

    /**
     * Default response handler applied when non specified.
     *
     * @see MethodConfig#getResponseHandler()
     */
    Class<? extends ResponseHandler> DEFAULT_RESPONSE_HANDLER = DefaultResponseHandler.class;

    /**
     * Default error handler applied when non specified.
     *
     * @see MethodConfig#getErrorHandler()
     */
    Class<? extends ErrorHandler> DEFAULT_ERROR_HANDLER = ErrorDelegatorHandler.class;

    /**
     * Default io interceptor applied when non specified.
     *
     * @see MethodConfig#getRequestInterceptor()
     */
    Class<? extends RequestInterceptor> DEFAULT_REQUEST_INTERCEPTOR = NoOpRequestInterceptor.class;

    /**
     * Default retry handler applied when non specified.
     *
     * @see org.codegist.crest.config.MethodConfig#getRetryHandler()
     */
    Class<? extends RetryHandler> DEFAULT_RETRY_HANDLER = MaxAttemptRetryHandler.class;

    /**
     * Default response deserializer applied when non specified.
     *
     * @see org.codegist.crest.config.MethodConfig#getDeserializers()
     */
    Class<? extends Deserializer>[] DEFAULT_DESERIALIZERS = new Class[0];

    Class<? extends EntityWriter> DEFAULT_ENTITY_WRITER = null;

    /*##############################################################################*/

    /**
     * @return The method being configured by the current object
     */
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
