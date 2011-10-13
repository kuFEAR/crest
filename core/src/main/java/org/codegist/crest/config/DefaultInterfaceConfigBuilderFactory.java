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
import org.codegist.crest.serializer.Deserializer;
import org.codegist.crest.serializer.Serializer;
import org.codegist.crest.util.ComponentRegistry;

/**
 * @author laurent.gilles@codegist.org
 */
public class DefaultInterfaceConfigBuilderFactory implements InterfaceConfigBuilderFactory {

    private final ComponentRegistry<String, Deserializer> mimeDeserializerRegistry;
    private final ComponentRegistry<Class<?>, Serializer> classSerializerRegistry;
    private final CRestConfig crestConfig;

    /**
     * @param crestConfig the crest config
     * @param mimeDeserializerRegistry the deserializer registry per mime type
     * @param classSerializerRegistry the serializer registry per class tyoe
     */
    public DefaultInterfaceConfigBuilderFactory(CRestConfig crestConfig, ComponentRegistry<String, Deserializer> mimeDeserializerRegistry, ComponentRegistry<Class<?>, Serializer> classSerializerRegistry) {
        this.crestConfig = crestConfig;
        this.mimeDeserializerRegistry = mimeDeserializerRegistry;
        this.classSerializerRegistry = classSerializerRegistry;
    }

    public InterfaceConfigBuilder newInstance(Class<?> interfaze) {
        return new DefaultInterfaceConfigBuilder(interfaze, crestConfig, mimeDeserializerRegistry, classSerializerRegistry);
    }
}
