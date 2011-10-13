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

import java.lang.reflect.Method;
import java.util.Map;

import static java.util.Collections.unmodifiableMap;

/**
 * Default immutable in-memory implementation of {@link org.codegist.crest.config.InterfaceConfig}
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
class DefaultInterfaceConfig implements InterfaceConfig {

    private final Class<?> interfaze;
    private final Map<Method, MethodConfig> cache;

    DefaultInterfaceConfig(Class<?> interfaze, Map<Method, MethodConfig> cache) {
        this.interfaze = interfaze;
        this.cache = unmodifiableMap(cache);
    }

    public Class<?> getInterface() {
        return interfaze;
    }

    public MethodConfig getMethodConfig(Method meth) {
        return cache.get(meth);
    }

}
