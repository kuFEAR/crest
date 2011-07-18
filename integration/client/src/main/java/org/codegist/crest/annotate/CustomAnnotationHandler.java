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

package org.codegist.crest.annotate;

import org.codegist.crest.config.InterfaceConfigBuilder;
import org.codegist.crest.config.MethodConfigBuilder;
import org.codegist.crest.config.ParamConfigBuilder;
import org.codegist.crest.config.ParamType;
import org.codegist.crest.config.annotate.AnnotationHandler;

import static org.codegist.crest.config.ParamType.QUERY;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class CustomAnnotationHandler implements AnnotationHandler<CustomAnnotation> {
    public void handleInterfaceAnnotation(CustomAnnotation annotation, InterfaceConfigBuilder builder) throws Exception {
        builder.startMethodsExtraParamConfig()
                .setType(QUERY)
                .setName(annotation.name())
                .setDefaultValue(annotation.value());

    }

    public void handleMethodAnnotation(CustomAnnotation annotation, MethodConfigBuilder builder) throws Exception {
         builder.startExtraParamConfig()
                .setType(QUERY)
                .setName(annotation.name())
                .setDefaultValue(annotation.value());
    }

    public void handleParameterAnnotation(CustomAnnotation annotation, ParamConfigBuilder builder) throws Exception {
         builder.setDefaultValue(annotation.value());
    }
}
