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

import org.codegist.crest.serializer.Serializer;

import java.util.Map;

/**
 * {@link org.codegist.crest.config.ParamConfig} builder
 * @author laurent.gilles@codegist.org
 */
public interface ParamConfigBuilder {

    /**
     * Returns an instance of {@link ParamConfig}
     * @return an instance of {@link ParamConfig}
     * @throws Exception Any exception thrown during the instanciation process
     */
    ParamConfig build() throws Exception;

    /**
     * Indicates the name of the REST interface method's parameter.
     * @param name the name of the REST interface method's parameter
     * @return current builder
     */
    ParamConfigBuilder setName(String name);

    /**
     * Indicates the default value to be used if no value is provider for the REST interface method's parameter.
     * @param defaultValue the default value to be used for the REST interface method's parameter.
     * @return current builder
     */
    ParamConfigBuilder setDefaultValue(String defaultValue);

    /**
     * Defines a string to use for joining array/Collection values for the REST interface method's parameter
     * @param listSeparator separator to use for joining array/Collection values for the REST interface method's parameter
     * @return current builder
     */
    ParamConfigBuilder setListSeparator(String listSeparator);

    /**
     * Indicates whether the REST interface method's parameter value is pre-encoded or not
     * @param encoded pre-encoded flag for the REST interface method's parameter value
     * @return current builder
     */
    ParamConfigBuilder setEncoded(boolean encoded);

    /**
     * Indicates the metadatas of the REST interface method's paramete
     * @param metadatas the metadatas of the REST interface method's paramete
     * @return current builder
     */
    ParamConfigBuilder setMetaDatas(Map<String,Object> metadatas);

    /**
     * Binds a parameter serializer for the REST interface method's parameter
     * @param serializerClass parameter serializer of the REST interface method's parameter
     * @return current builder
     */
    ParamConfigBuilder setSerializer(Class<? extends Serializer> serializerClass);

    /**
     * Indicates the REST interface method parameter's type
     * @param type the REST interface method parameter's type
     * @return current builder
     */
    ParamConfigBuilder setType(ParamType type);

    /**
     * Sets the current parameter to be a HTTP Cookie
     * @return current builder
     */
    ParamConfigBuilder forCookie();

    /**
     * Sets the current parameter to be a URI Query parameter
     * @return current builder
     */
    ParamConfigBuilder forQuery();

    /**
     * Sets the current parameter to be a URI path segment parameter
     * @return current builder
     */
    ParamConfigBuilder forPath();

    /**
     * Sets the current parameter to be a form parameter
     * @return current builder
     */
    ParamConfigBuilder forForm();

    /**
     * Sets the current parameter to be a multipart parameter
     * @return current builder
     */
    ParamConfigBuilder forMultiPart();

    /**
     * Sets the current parameter to be a HTTP Header
     * @return current builder
     */
    ParamConfigBuilder forHeader();

    /**
     * Sets the current parameter to be a URI matrix parameter 
     * @return current builder
     */
    ParamConfigBuilder forMatrix();
}
