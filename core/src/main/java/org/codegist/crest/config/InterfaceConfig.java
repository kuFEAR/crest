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

import java.lang.reflect.Method;

/**
 * <p>Reflects a REST interface configuration</p>
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public interface InterfaceConfig {

    /**
     * Returns the original REST interface
     * @return The interface being configured by the current object.
     */
    Class<?> getInterface();

    /**
     * Returns the REST method configuration for the given REST interface method
     * @param meth Method to retrieve the config for
     * @return The method config object for the given method, null if not found.
     */
    MethodConfig getMethodConfig(Method meth);

}
