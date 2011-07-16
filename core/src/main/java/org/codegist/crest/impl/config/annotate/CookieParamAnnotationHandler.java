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

package org.codegist.crest.impl.config.annotate;

import org.codegist.crest.annotate.CookieParam;
import org.codegist.crest.impl.config.InterfaceConfigBuilder;
import org.codegist.crest.impl.config.MethodConfigBuilder;
import org.codegist.crest.impl.config.ParamConfigBuilder;

/**
 * @author laurent.gilles@codegist.org
 */
class CookieParamAnnotationHandler extends NoOpAnnotationHandler<CookieParam> {

    @Override
    public void handleInterfaceAnnotation(CookieParam annotation, InterfaceConfigBuilder builder) {
        builder.addMethodsExtraCookieParam(annotation.value(), annotation.defaultValue());
    }

    @Override
    public void handleMethodAnnotation(CookieParam annotation, MethodConfigBuilder builder) {
        builder.addExtraCookieParam(annotation.value(), annotation.defaultValue());
    }
                  
    @Override
    public void handleParameterAnnotation(CookieParam annotation, ParamConfigBuilder builder) {
        builder.forCookie()
                .setName(annotation.value())
                .setDefaultValue(annotation.defaultValue());
    }
    
}
