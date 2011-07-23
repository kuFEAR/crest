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

package org.codegist.crest.config.annotate;

import org.codegist.crest.annotate.QueryParam;
import org.codegist.crest.config.InterfaceConfigBuilder;
import org.codegist.crest.config.MethodConfigBuilder;
import org.codegist.crest.config.ParamConfigBuilder;

import static org.codegist.crest.config.ParamType.QUERY;

/**
 * @author laurent.gilles@codegist.org
 */
class QueryParamAnnotationHandler extends ParamAnnotationHandler<QueryParam> {

    @Override
    public void handleInterfaceAnnotation(QueryParam annotation, InterfaceConfigBuilder builder) {
        builder.startMethodsExtraParamConfig()
                .setType(QUERY)
                .setName(annotation.value())
                .setDefaultValue(nullIfUnset(annotation.defaultValue()));
    }

    @Override
    public void handleMethodAnnotation(QueryParam annotation, MethodConfigBuilder builder) {
        builder.startExtraParamConfig()
                .setType(QUERY)
                .setName(annotation.value())
                .setDefaultValue(nullIfUnset(annotation.defaultValue()));
    }

    @Override
    public void handleParameterAnnotation(QueryParam annotation, ParamConfigBuilder builder) {
        builder.setType(QUERY)
                .setName(annotation.value())
                .setDefaultValue(nullIfUnset(annotation.defaultValue()));
    }
}
