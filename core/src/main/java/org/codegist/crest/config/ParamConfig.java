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
 *  ==================================================================
 *
 *  More information at http://www.codegist.org.
 */

package org.codegist.crest.config;

import org.codegist.crest.param.ParamProcessor;
import org.codegist.crest.serializer.Serializer;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Basic parameter configuration holder object for interface/method extra parameters. Extra parameters are added on top of the method arguments.
 * <p>Implementors must respect the following contract :
 * <p>- No method return null
 * <p>- Defaults values must either be taken from interface's defaults constant or from {@link org.codegist.crest.InterfaceContext#getProperties()}'s defaults overrides.
 *
 * @see MethodConfig
 * @see ParamConfig
 * @see InterfaceConfigFactory
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public interface ParamConfig {

    String PARAM_CONFIG_DEFAULT_TYPE = ParamConfig.class.getName() + "#param-type";
    String PARAM_CONFIG_DEFAULT_LIST_SEPARATOR = ParamConfig.class.getName() + "#list-separator";
    String PARAM_CONFIG_DEFAULT_METAS = ParamConfig.class.getName() + "#metas";
    String PARAM_CONFIG_DEFAULT_VALUE = ParamConfig.class.getName() + "#value";
    String PARAM_CONFIG_DEFAULT_SERIALIZER = ParamConfig.class.getName() + "#serializer";
    String PARAM_CONFIG_DEFAULT_ENCODED = ParamConfig.class.getName() + "#encoded";
    String PARAM_CONFIG_DEFAULT_NAME = ParamConfig.class.getName() + "#name";
    String PARAM_CONFIG_DEFAULT_PROCESSOR = ParamConfig.class.getName() + "#processor";

    String getName();

    Type getValueGenericType();

    Class<?> getValueClass();

    String getDefaultValue();

    ParamType getType();

    Map<String, Object> getMetaDatas();

    Serializer getSerializer();

    boolean isEncoded();

    ParamProcessor getParamProcessor();

}
