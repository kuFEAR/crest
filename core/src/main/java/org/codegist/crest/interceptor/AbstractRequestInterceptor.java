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

package org.codegist.crest.interceptor;

import org.codegist.crest.CRestConfig;
import org.codegist.crest.config.ParamConfigBuilder;
import org.codegist.crest.config.ParamConfigBuilderFactory;

import java.lang.reflect.Type;

/**
 * Empty io interceptor. Does nothing.
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public abstract class AbstractRequestInterceptor implements RequestInterceptor {

    private final ParamConfigBuilderFactory paramConfigBuilderFactory;

    /**
     * @param crestConfig the crest config
     */
    public AbstractRequestInterceptor(CRestConfig crestConfig) {
        this.paramConfigBuilderFactory = crestConfig.get(ParamConfigBuilderFactory.class);
    }

    /**
     * Returns a new param config for the given class type
     * @param type param config class
     * @return the new param config
     */
    public ParamConfigBuilder newParamConfig(Class<?> type){
        return newParamConfig(type, type);
    }

    /**
     * Returns a new param config for the given class and generic type
     * @param type param config class
     * @param genericType param config generic type
     * @return the new param config
     */
    public ParamConfigBuilder newParamConfig(Class<?> type, Type genericType){
        return paramConfigBuilderFactory.newInstance(type, genericType);
    }
}
