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

import org.codegist.crest.CRestConfig;
import org.codegist.crest.config.ParamConfigBuilder;
import org.codegist.crest.config.annotate.StringBasedAnnotationHandler;

import javax.ws.rs.FormParam;

import static org.codegist.crest.config.ParamType.FORM;

/**
 * @author laurent.gilles@codegist.org
 */
class FormParamAnnotationHandler extends StringBasedAnnotationHandler<FormParam> {

    FormParamAnnotationHandler(CRestConfig crestConfig) {
        super(crestConfig);
    }

    @Override
    public void handleParameterAnnotation(FormParam annotation, ParamConfigBuilder builder) {
        builder.setType(FORM).setName(ph(annotation.value()));
    }
    
}
