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

import org.codegist.common.lang.Objects;
import org.codegist.crest.config.MethodConfig;
import org.codegist.crest.config.ParamConfig;

import java.util.Collections;
import java.util.Map;

/**
 * @see org.codegist.crest.CRestBuilder#setProperties(java.util.Map)
 * @see org.codegist.crest.CRestBuilder#setProperty(String, Object)
 * @see org.codegist.crest.CRestBuilder#addProperties(java.util.Map)
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public final class CRestProperty {

    public static final String CREST_DATE_FORMAT_DEFAULT = "yyyy-MM-dd'T'HH:mm:ssZ";
    public static final String CREST_BOOLEAN_TRUE_DEFAULT = "true";
    public static final String CREST_BOOLEAN_FALSE_DEFAULT = "false";
    public static final int CREST_CONCURRENCY_LEVEL_DEFAULT = 1;
    public static final int CREST_RETRY_ATTEMPTS_DEFAULT = 0;

    private CRestProperty() {
        throw new IllegalStateException();
    }

    public static int getRetryAttempts(Map<String,Object> crestProperties){
        return get(crestProperties, CREST_RETRY_ATTEMPTS, CREST_RETRY_ATTEMPTS_DEFAULT);
    }

    public static String getDateFormat(Map<String,Object> crestProperties){
        return get(crestProperties, CREST_DATE_FORMAT, CREST_DATE_FORMAT_DEFAULT);
    }

    public static String getBooleanTrue(Map<String,Object> crestProperties){
        return get(crestProperties, CREST_BOOLEAN_TRUE, CREST_BOOLEAN_TRUE_DEFAULT);
    }

    public static String getBooleanFalse(Map<String,Object> crestProperties){
        return get(crestProperties, CREST_BOOLEAN_FALSE, CREST_BOOLEAN_FALSE_DEFAULT);
    }

    public static String[] getBooleanFormat(Map<String,Object> crestProperties){
        return new String[]{
                getBooleanTrue(crestProperties),
                getBooleanFalse(crestProperties),
        };
    }

    public static int getConcurrencyLevel(Map<String,Object> crestProperties){
        return get(crestProperties, CREST_CONCURRENCY_LEVEL, CREST_CONCURRENCY_LEVEL_DEFAULT);
    }

    public static Map<String,String> getPlaceholders(Map<String,Object> crestProperties){
        return get(crestProperties, CREST_ANNOTATION_PLACEHOLDERS, Collections.<String, String>emptyMap());
    }

    public static <T> T get(Map<String,Object> crestProperties, Class<?> propName){
        return CRestProperty.<T>get(crestProperties, propName.getName());
    }

    public static <T> T get(Map<String,Object> crestProperties, String propName){
        return CRestProperty.<T>get(crestProperties, propName, null);
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(Map<String,Object> crestProperties, String propName, T defaultIfNotFound){
        return Objects.<T>defaultIfNull((T) crestProperties.get(propName), defaultIfNotFound);
    }


    private static final String C = CRest.class.getName();

    public static final String CREST_CONCURRENCY_LEVEL = C + "#concurrency-level";

    public static final String CREST_DATE_FORMAT = C + "#date-format";

    public static final String CREST_BOOLEAN_TRUE = C + "#boolean-format.true";

    public static final String CREST_BOOLEAN_FALSE = C + "#boolean-format.false";

    public static final String CREST_UNAUTHORIZED_STATUS_CODE = C + "#status-code.unauthorized";

    public static final String CREST_RETRY_ATTEMPTS = C + "#retry-attempts";

    public static final String CREST_ANNOTATION_PLACEHOLDERS = C + "#annotation-placeholders";

    /*********************************************************
     *********************************************************
     ****** MethodConfig default values override properties 
     *********************************************************
     *********************************************************/

    private static final String MC = MethodConfig.class.getName();

    public static final String METHOD_CONFIG_DEFAULT_CHARSET = MC + "#charset";

    public static final String METHOD_CONFIG_DEFAULT_SO_TIMEOUT = MC + "#socket-timeout";

    public static final String METHOD_CONFIG_DEFAULT_CO_TIMEOUT = MC + "#connection-timeout";

    public static final String METHOD_CONFIG_DEFAULT_PATH = MC + "#path";

    public static final String METHOD_CONFIG_DEFAULT_HTTP_METHOD = MC + "#http-method";

    public static final String METHOD_CONFIG_DEFAULT_CONTENT_TYPE = MC + "#content-type";

    public static final String METHOD_CONFIG_DEFAULT_ACCEPT = MC + "#accept";

    public static final String METHOD_CONFIG_DEFAULT_EXTRA_PARAMS = MC + "#extra-params";

    public static final String METHOD_CONFIG_DEFAULT_RESPONSE_HANDLER = MC + "#response-handler";

    public static final String METHOD_CONFIG_DEFAULT_ERROR_HANDLER = MC + "#error-handler";

    public static final String METHOD_CONFIG_DEFAULT_REQUEST_INTERCEPTOR = MC + "#request-interceptor";

    public static final String METHOD_CONFIG_DEFAULT_RETRY_HANDLER = MC + "#retry-handler";

    public static final String METHOD_CONFIG_DEFAULT_DESERIALIZERS = MC + "#deserializer";

    public static final String METHOD_CONFIG_DEFAULT_ENTITY_WRITER = MC + "#body-writer";

    /*********************************************************
     *********************************************************
     ****** MethodParamConfig default values override properties
     *********************************************************
     *********************************************************/

    private static final String PC = ParamConfig.class.getName();

    public static final String PARAM_CONFIG_DEFAULT_TYPE = PC + "#param-type";

    public static final String PARAM_CONFIG_DEFAULT_LIST_SEPARATOR = PC + "#list-separator";

    public static final String PARAM_CONFIG_DEFAULT_METAS = PC + "#metas";

    public static final String PARAM_CONFIG_DEFAULT_VALUE = PC + "#value";

    public static final String PARAM_CONFIG_DEFAULT_SERIALIZER = PC + "#serializer";

    public static final String PARAM_CONFIG_DEFAULT_ENCODED = PC + "#encoded";

    public static final String PARAM_CONFIG_DEFAULT_NAME = PC + "#name";

    public static final String PARAM_CONFIG_DEFAULT_PROCESSOR = PC + "#processor";
}

