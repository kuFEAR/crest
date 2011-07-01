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

package org.codegist.crest.config.annotate.jaxrs;

import org.codegist.crest.config.InterfaceConfigBuilder;
import org.codegist.crest.config.MethodConfigBuilder;
import org.codegist.crest.config.annotate.NoOpAnnotationHandler;

import javax.ws.rs.Consumes;

/**
 * @author laurent.gilles@codegist.org
 */
class ConsumesAnnotationHandler extends NoOpAnnotationHandler<Consumes> {

    @Override
    public void handleInterfaceAnnotation(Consumes annotation, InterfaceConfigBuilder builder) {
        if(annotation.value().length >= 1) {
            builder.setMethodsProduces(annotation.value()[0]);
        }
    }

    @Override
    public void handleMethodAnnotation(Consumes annotation, MethodConfigBuilder builder) {
        if(annotation.value().length >= 1) {
            builder.setProduces(annotation.value()[0]);
        }
    }

}
