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

package org.codegist.crest.config.annotate.jaxrs;

import org.codegist.crest.CRestConfig;
import org.codegist.crest.config.InterfaceConfigBuilder;
import org.codegist.crest.config.MethodConfigBuilder;
import org.codegist.crest.config.annotate.StringBasedAnnotationHandler;

import javax.ws.rs.Consumes;

/**
 * @author laurent.gilles@codegist.org
 */
class ConsumesAnnotationHandler extends StringBasedAnnotationHandler<Consumes> {

    ConsumesAnnotationHandler(CRestConfig crestConfig) {
        super(crestConfig);
    }

    @Override
    public void handleInterfaceAnnotation(Consumes annotation, InterfaceConfigBuilder builder) {
        if(annotation.value().length >= 1) {
            builder.setMethodsProduces(ph(annotation.value()[0]));
        }
    }

    @Override
    public void handleMethodAnnotation(Consumes annotation, MethodConfigBuilder builder) {
        if(annotation.value().length >= 1) {
            builder.setProduces(ph(annotation.value()[0]));
        }
    }

}
