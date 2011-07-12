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

/**
 * Properties of this interface are meant to be used as keys in the custom properties map of {@link org.codegist.crest.CRestContext}.
 *
 * @see org.codegist.crest.CRestBuilder#setProperties(java.util.Map)
 * @see org.codegist.crest.CRestBuilder#setProperty(String, Object)
 * @see org.codegist.crest.CRestBuilder#addProperties(java.util.Map)
 * @see org.codegist.crest.InterfaceContext#getProperties()
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public interface CRestProperty {


    /**
     * Indicates the currency level, or number of threads, the different components CRest can handle.
     * <p>Expects a Integer value
     * <p>Default to 1
     */
    String CREST_CONCURRENCY_LEVEL = "crest.config.concurrency.level";

    /**
     * override the default item separator (comma).
     * <p>Expects a String.
     *
     * @see org.codegist.crest.serializer.Serializer
     * @see org.codegist.crest.InterfaceContext#getProperties()
     */
    String QUERY_PARAM_COLLECTION_SEPARATOR = "crest.config.collection.query.separator";

    /**
     * override the default item separator (comma).
     * <p>Expects a String.
     *
     * @see org.codegist.crest.serializer.Serializer
     * @see org.codegist.crest.InterfaceContext#getProperties()
     */
    String PATH_PARAM_COLLECTION_SEPARATOR = "crest.config.collection.path.separator";

    /**
     * override the default item separator (comma).
     * <p>Expects a String.
     *
     * @see org.codegist.crest.serializer.Serializer
     * @see org.codegist.crest.InterfaceContext#getProperties()
     */
    String MATRIX_PARAM_COLLECTION_SEPARATOR = "crest.config.collection.matrix.separator";

    /**
     * override the default item separator (comma).
     * <p>Expects a String.
     *
     * @see org.codegist.crest.serializer.Serializer
     * @see org.codegist.crest.InterfaceContext#getProperties()
     */
    String HEADER_PARAM_COLLECTION_SEPARATOR = "crest.config.collection.header.separator";

    /**
     * override the default item separator (comma).
     * <p>Expects a String.
     *
     * @see org.codegist.crest.serializer.Serializer
     * @see org.codegist.crest.InterfaceContext#getProperties()
     */
    String COOKIE_PARAM_COLLECTION_SEPARATOR = "crest.config.collection.cookie.separator";

    /**
     * override the default item separator (comma).
     * <p>Expects a String.
     *
     * @see org.codegist.crest.serializer.Serializer
     * @see org.codegist.crest.InterfaceContext#getProperties()
     */
    String FORM_PARAM_COLLECTION_SEPARATOR = "crest.config.collection.form.separator";

    /*********************************************************
     *********************************************************
     ****** Serializer properties 
     *********************************************************
     *********************************************************/

    String SERIALIZER_XML_WRAPPER_ELEMENT_NAME = "serializer.xml.wrapper.element-name";

    /**
     * format the date with the given date format.
     * <p>Expects a String value of the following : Millis, Second, Minutes, Hours or Days to get the date as millisecond during from January 1, 1970, 00:00:00 GMT to this date or any {@link java.text.DateFormat} valid date format.
     * @see org.codegist.crest.serializer.Serializer
     * @see org.codegist.crest.InterfaceContext#getProperties()
     */
    String CREST_DATE_FORMAT = "serializer.date.format";


    /**
     * override the default boolean TRUE value ("true").
     * <p>Expects a String.
     *
     * @see org.codegist.crest.serializer.Serializer
     * @see org.codegist.crest.InterfaceContext#getProperties()
     */
    String CREST_BOOLEAN_TRUE = "serializer.boolean.true";

    /**
     * override the default boolean FALSE value ("false").
     * <p>Expects a String.
     *
     * @see org.codegist.crest.serializer.Serializer
     * @see org.codegist.crest.InterfaceContext#getProperties()
     */
    String CREST_BOOLEAN_FALSE = "serializer.boolean.false";

    /**
     * specify a type/serializer map to use for selection of serializer to apply for any given type
     * <p>Expects an instance of {@link java.util.Map}&lt;{@link java.lang.reflect.Type},{@link org.codegist.crest.serializer.Serializer}&gt;.
     *
     * @see org.codegist.crest.serializer.Serializer
     * @see org.codegist.crest.InterfaceContext#getProperties()
     */
    String SERIALIZER_CUSTOM_SERIALIZER_MAP = "serializer.serializers-map";


    /*********************************************************
     *********************************************************
     ****** OAuth Interceptor properties 
     *********************************************************
     *********************************************************/

    /**
     * specify the preconfigured consumer secret.
     * <p>Expects a string.
     *
     * @see org.codegist.crest.InterfaceContext#getProperties()
     */
    String OAUTH_CONSUMER_SECRET = "authentification.oauth.consumer.secret";

    /**
     * specify the preconfigured consumer key.
     * <p>Expects a string value.
     *
     * @see org.codegist.crest.InterfaceContext#getProperties()
     */
    String OAUTH_CONSUMER_KEY = "authentification.oauth.consumer.key";

    /**
     * specify the preconfigured access token key.
     * <p>Expects a string.
     *
     * @see org.codegist.crest.InterfaceContext#getProperties()
     */
    String OAUTH_ACCESS_TOKEN = "authentification.oauth.access.token";

    /**
     * specify the preconfigured access token secret.
     * <p>Expects a string.
     *
     * @see org.codegist.crest.InterfaceContext#getProperties()
     */
    String OAUTH_ACCESS_TOKEN_SECRET = "authentification.oauth.access.secret";

    /**
     * specify the preconfigured access token extras.
     * <p>Expects a java.lang.Map&lt;java.lang.String,java.lang.String&gt;.
     *
     * @see org.codegist.crest.InterfaceContext#getProperties()
     */
    String OAUTH_ACCESS_TOKEN_ATTRIBUTES = "authentification.oauth.access.attributes";


    /**
     * specify the access token refresh url that gets called when the preconfigure access token is expired.
     * <p>Expects a String.
     *
     * @see org.codegist.crest.InterfaceContext#getProperties()
     */
    String OAUTH_ACCESS_TOKEN_REFRESH_URL = "authentification.oauth.access.refresh-url";
    /**
     * specify the access token refresh url http method.
     * <p>Default to POST
     * <p>Expects a String.
     *
     * @see org.codegist.crest.InterfaceContext#getProperties()
     */
    String OAUTH_TOKEN_ACCESS_REFRESH_URL_METHOD = "authentification.oauth.access.refresh-url.method";

    /*********************************************************
     *********************************************************
     ****** Handlers config
     *********************************************************
     *********************************************************/

    /**
     * override the default max retry count {@link org.codegist.crest.handler.MaxAttemptRetryHandler#DEFAULT_MAX}.
     * <p>Expects an int.
     */
    String HANDLER_RETRY_MAX_ATTEMPTS = "handler.retry.attempts.max";

    /**
     * override the default max retry count {@link org.codegist.crest.handler.MaxAttemptRetryHandler#DEFAULT_MAX}.
     */
    String CONFIG_PLACEHOLDERS_MAP = "config.placeholders.map";

    /*********************************************************
     *********************************************************
     ****** InterfaceConfig default values override properties 
     *********************************************************
     *********************************************************/

    /**
     * override the default encoding {@link org.codegist.crest.config.InterfaceConfig#DEFAULT_ENCODING}.
     * <p>Expects a string.
     *
     * @see org.codegist.crest.InterfaceContext#getProperties()
     */
    String CONFIG_INTERFACE_DEFAULT_ENCODING = "config.interface.default.encoding";

    /**
     * override the default path {@link org.codegist.crest.config.InterfaceConfig#DEFAULT_PATH}.
     * <p>Expects a string.
     *
     * @see org.codegist.crest.InterfaceContext#getProperties()
     */
    String CONFIG_INTERFACE_DEFAULT_PATH = "config.interface.default.path";

    /*********************************************************
     *********************************************************
     ****** MethodConfig default values override properties 
     *********************************************************
     *********************************************************/

    /**
     * override the default socket timeout {@link org.codegist.crest.config.MethodConfig#DEFAULT_SO_TIMEOUT}.
     * <p>Expects milliseconds (long).
     *
     * @see org.codegist.crest.InterfaceContext#getProperties()
     */
    String CONFIG_METHOD_DEFAULT_SO_TIMEOUT = "config.method.default.socket-timeout";

    /**
     * override the default connection timeout {@link org.codegist.crest.config.MethodConfig#DEFAULT_CO_TIMEOUT}.
     * <p>Expects milliseconds (long).
     *
     * @see org.codegist.crest.InterfaceContext#getProperties()
     */
    String CONFIG_METHOD_DEFAULT_CO_TIMEOUT = "config.method.default.connection-timeout";

    /**
     * override the default url fragment {@link org.codegist.crest.config.MethodConfig#DEFAULT_PATH}.
     * <p>Expects a string.
     *
     * @see org.codegist.crest.InterfaceContext#getProperties()
     */
    String CONFIG_METHOD_DEFAULT_PATH = "config.method.default.path";

    /**
     * override the default end point {@link org.codegist.crest.config.InterfaceConfig#DEFAULT_ENDPOINT}.
     * <p>Expects a string.
     *
     * @see org.codegist.crest.InterfaceContext#getProperties()
     */
    String CONFIG_METHOD_DEFAULT_ENDPOINT = "config.method.default.end-point";

    /**
     * override the default http method {@link org.codegist.crest.config.MethodConfig#DEFAULT_HTTP_METHOD}.
     * <p>Expects a String
     *
     * @see org.codegist.crest.InterfaceContext#getProperties()
     */
    String CONFIG_METHOD_DEFAULT_HTTP_METHOD = "config.method.default.http-method";

    String CONFIG_METHOD_DEFAULT_CONTENT_TYPE = "config.method.default.content-type";

    String CONFIG_METHOD_DEFAULT_ACCEPT = "config.method.default.accept";

    /**
     * override the default method extra params {@link org.codegist.crest.config.MethodConfig#DEFAULT_EXTRA_PARAMS}.
     * <p>Expects an array of {@link org.codegist.crest.config.ParamConfig}
     *
     * @see org.codegist.crest.InterfaceContext#getProperties()
     */
    String CONFIG_METHOD_DEFAULT_EXTRA_PARAMS = "config.method.default.extra-params";

    /**
     * override the default response handler {@link org.codegist.crest.config.MethodConfig#DEFAULT_RESPONSE_HANDLER}.
     * <p>Expects an instance of {@link org.codegist.crest.handler.ResponseHandler}.
     *
     * @see org.codegist.crest.InterfaceContext#getProperties()
     */
    String CONFIG_METHOD_DEFAULT_RESPONSE_HANDLER = "config.method.default.response-handler";

    /**
     * override the default error handler {@link org.codegist.crest.config.MethodConfig#DEFAULT_ERROR_HANDLER}.
     * <p>Expects an instance of {@link org.codegist.crest.handler.ErrorHandler}.
     *
     * @see org.codegist.crest.InterfaceContext#getProperties()
     */
    String CONFIG_METHOD_DEFAULT_ERROR_HANDLER = "config.method.default.error-handler";

    /**
     * override the default io interceptor {@link org.codegist.crest.config.MethodConfig#DEFAULT_REQUEST_INTERCEPTOR}.
     * <p>Expects an instance of {@link org.codegist.crest.interceptor.RequestInterceptor}.
     *
     * @see org.codegist.crest.InterfaceContext#getProperties()
     */
    String CONFIG_METHOD_DEFAULT_REQUEST_INTERCEPTOR = "config.method.default.io-interceptor";

    /**
     * override the default method retry handler {@link org.codegist.crest.config.MethodConfig#DEFAULT_RETRY_HANDLER}.
     */
    String CONFIG_METHOD_DEFAULT_RETRY_HANDLER = "config.method.default.retry-handler";

    /**
     * override the default method response deserializer {@link org.codegist.crest.config.MethodConfig#DEFAULT_DESERIALIZERS}.
     */
    String CONFIG_METHOD_DEFAULT_DESERIALIZERS = "config.method.default.deserializer";

    String CONFIG_PARAM_DEFAULT_BODY_WRITER = "config.method.default.body-write";

    /*********************************************************
     *********************************************************
     ****** MethodParamConfig default values override properties
     *********************************************************
     *********************************************************/

    /**
     * override the default destination {@link org.codegist.crest.config.MethodParamConfig#DEFAULT_DESTINATION}.
     * <p>Expects a String
     *
     * @see org.codegist.crest.InterfaceContext#getProperties()
     */
    String CONFIG_PARAM_DEFAULT_TYPE = "config.param.default.type";

    String CONFIG_PARAM_DEFAULT_LIST_SEPARATOR = "config.param.default.list-separator";

    String CONFIG_PARAM_DEFAULT_METAS = "config.param.default.metas";

    /**
     * override the default param value {@link org.codegist.crest.config.MethodParamConfig#DEFAULT_VALUE}.
     * <p>Expects a String.
     *
     * @see org.codegist.crest.InterfaceContext#getProperties()
     */
    String CONFIG_PARAM_DEFAULT_VALUE = "config.param.default.value";


    /**
     * override the default serializer {@link org.codegist.crest.config.MethodParamConfig#DEFAULT_SERIALIZER}.
     * <p>Expects an instance of {@link org.codegist.crest.serializer.Serializer}.
     *
     * @see org.codegist.crest.serializer.Serializer
     * @see org.codegist.crest.InterfaceContext#getProperties()
     */
    String CONFIG_PARAM_DEFAULT_SERIALIZER = "config.param.default.serializer";

    /**
     * override the default encoded {@link org.codegist.crest.config.MethodParamConfig#DEFAULT_ENCODED}.
     * <p>Expects a boolean value.
     *
     * @see org.codegist.crest.InterfaceContext#getProperties()
     */
    String CONFIG_PARAM_DEFAULT_ENCODED = "config.param.default.encoded";

    /**
     * override the default name {@link org.codegist.crest.config.MethodParamConfig#DEFAULT_NAME}.
     * <p>Expects a string.
     *
     * @see org.codegist.crest.InterfaceContext#getProperties()
     */
    String CONFIG_PARAM_DEFAULT_NAME = "config.param.default.name";

    String CONFIG_PARAM_DEFAULT_PROCESSOR = "config.param.default.processor";
}

