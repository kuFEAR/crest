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

import org.codegist.crest.CRestConfig;
import org.codegist.crest.serializer.Serializer;
import org.codegist.crest.util.ComponentRegistry;

import java.lang.reflect.Type;

/**
 * @author laurent.gilles@codegist.org
 */
public class DefaultParamConfigBuilderFactory implements ParamConfigBuilderFactory {

    private final ComponentRegistry<Class<?>, Serializer> classSerializerRegistry;
    private final CRestConfig crestConfig;

    /**
     * @param crestConfig the crest config
     * @param classSerializerRegistry the serializer registry per class
     */
    public DefaultParamConfigBuilderFactory(CRestConfig crestConfig, ComponentRegistry<Class<?>, Serializer> classSerializerRegistry) {
        this.crestConfig = crestConfig;
        this.classSerializerRegistry = classSerializerRegistry;
    }

    /**
     * @inheritDoc
     */
    public ParamConfigBuilder newInstance(Class<?> type, Type genericType) {
        return new DefaultParamConfigBuilder(crestConfig, classSerializerRegistry, type, genericType);
    }

}
