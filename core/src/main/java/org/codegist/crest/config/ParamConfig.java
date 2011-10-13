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

import org.codegist.crest.param.ParamProcessor;
import org.codegist.crest.serializer.Serializer;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * <p>Reflects a REST interface method's parameter configuration</p>
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public interface ParamConfig {

    /**
     * <p>CRestConfig property to override the default parameter type.</p>
     * <p>Can be overridden by setting this property as follow:</p>
     * <code><pre>
     * ParamType type = ...;
     * CRest crest = CRest.property(ParamConfig.PARAM_CONFIG_DEFAULT_TYPE, type).buid();
     * </pre></code>
     * <p>Default is QUERY</p>
     * <p>Expects a {@link org.codegist.crest.config.ParamType}</p>
     */
    String PARAM_CONFIG_DEFAULT_TYPE = ParamConfig.class.getName() + "#type";

    /**
     * <p>CRestConfig property to override the default list separator of array/Collection parameters.</p>
     * <p>Can be overridden by setting this property as follow:</p>
     * <code><pre>
     * String listSeparator = ...;
     * CRest crest = CRest.property(ParamConfig.PARAM_CONFIG_DEFAULT_LIST_SEPARATOR, listSeparator).buid();
     * </pre></code>
     * <p>Default behavior is to add as many key/value parameters with the same key as they are values in array/Collection</p>
     * <p>Expects a String</p>
     */
    String PARAM_CONFIG_DEFAULT_LIST_SEPARATOR = ParamConfig.class.getName() + "#list-separator";

    /**
     * <p>CRestConfig property to override the default parameter metadatas.</p>
     * <p>Can be overridden by setting this property as follow:</p>
     * <code><pre>
     * Map&lt;String,Object&gt; metadatas = ...;
     * CRest crest = CRest.property(ParamConfig.PARAM_CONFIG_DEFAULT_METAS, metadatas).buid();
     * </pre></code>
     * <p>Default is empty</p>
     * <p>Expects a {@link java.util.Map}&lt;{@link String},{@link Object}&gt;</p>
     */
    String PARAM_CONFIG_DEFAULT_METAS = ParamConfig.class.getName() + "#metas";

    /**
     * <p>CRestConfig property to override the default parameter value.</p>
     * <p>Can be overridden by setting this property as follow:</p>
     * <code><pre>
     * String value = ...;
     * CRest crest = CRest.property(ParamConfig.PARAM_CONFIG_DEFAULT_VALUE, value).buid();
     * </pre></code>
     * <p>Default is empty</p>
     * <p>Expects a String</p>
     */
    String PARAM_CONFIG_DEFAULT_VALUE = ParamConfig.class.getName() + "#value";

    /**
     * <p>CRestConfig property to specify the default deserializers to use. This will override the default serialization selection process.</p>
     * <p>Can be overridden by setting this property as follow:</p>
     * <code><pre>
     * Class&lt;? extends Serializer&gt; serializerClass = ...;
     * CRest crest = CRest.property(ParamConfig.PARAM_CONFIG_DEFAULT_SERIALIZER, serializerClass).buid();
     * </pre></code>
     * <p>Default is automatically inferred from the context</p>
     * <p>Expects a {@link Class}&lt;? extends {@link org.codegist.crest.serializer.Serializer}&gt;</p>
     */
    String PARAM_CONFIG_DEFAULT_SERIALIZER = ParamConfig.class.getName() + "#serializer";

    /**
     * <p>CRestConfig property to override the default parameter pre-encoded flag.</p>
     * <p>Can be overridden by setting this property as follow:</p>
     * <code><pre>
     * Boolean encoded = ...;
     * CRest crest = CRest.property(ParamConfig.PARAM_CONFIG_DEFAULT_ENCODED, encoded).buid();
     * </pre></code>
     * <p>Default is False</p>
     * <p>Expects a Boolean</p>
     */
    String PARAM_CONFIG_DEFAULT_ENCODED = ParamConfig.class.getName() + "#encoded";

    /**
     * <p>CRestConfig property to override the default parameter name.</p>
     * <p>Can be overridden by setting this property as follow:</p>
     * <code><pre>
     * String name = ...;
     * CRest crest = CRest.property(ParamConfig.PARAM_CONFIG_DEFAULT_NAME, name).buid();
     * </pre></code>
     * <p>Default is empty</p>
     * <p>Expects a String</p>
     */
    String PARAM_CONFIG_DEFAULT_NAME = ParamConfig.class.getName() + "#name";

    /**
     * <p>CRestConfig property to specify the default parameter processor to use. This will override the default processor selection process.</p>
     * <p>Can be overridden by setting this property as follow:</p>
     * <code><pre>
     * Class&lt;? extends ParamProcessor&gt; processorClass = ...;
     * CRest crest = CRest.property(ParamConfig.PARAM_CONFIG_DEFAULT_PROCESSOR, processorClass).buid();
     * </pre></code>
     * <p>Default is automatically inferred from the context</p>
     * <p>Expects a {@link Class}&lt;? extends {@link org.codegist.crest.param.ParamProcessor}&gt;</p>
     */
    String PARAM_CONFIG_DEFAULT_PROCESSOR = ParamConfig.class.getName() + "#processor";

    /**
     * Indicates the REST interface method's parameter name
     */
    String getName();

    /**
     * Indicates the REST interface method's parameter generic type
     */
    Type getValueGenericType();

    /**
     * Indicates the REST interface method's parameter class type
     */
    Class<?> getValueClass();

    /**
     * Indicates the REST interface method's parameter default value to be used if not passed
     */
    String getDefaultValue();

    /**
     * Indicates the REST interface method's parameter type
     */
    ParamType getType();

    /**
     * Indicates the REST interface method's parameter metadatas
     */
    Map<String, Object> getMetaDatas();

    /**
     * Indicates the REST interface method's parameter serializer
     */
    Serializer getSerializer();

    /**
     * Indicates whether the REST interface method's parameter value is pre-encoded
     */
    boolean isEncoded();

    /**
     * Indicates the REST interface method's parameter processor
     */
    ParamProcessor getParamProcessor();

}
