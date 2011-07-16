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

import org.codegist.crest.annotate.ListSeparator;
import org.codegist.crest.impl.config.InterfaceConfigBuilder;
import org.codegist.crest.impl.config.MethodConfigBuilder;
import org.codegist.crest.impl.config.ParamConfigBuilder;

/**
 * @author laurent.gilles@codegist.org
 */
class ListSeparatorAnnotationHandler extends NoOpAnnotationHandler<ListSeparator> {

    @Override
    public void handleInterfaceAnnotation(ListSeparator annotation, InterfaceConfigBuilder builder) {
        builder.setParamsListSeparator(annotation.value());
    }

    @Override
    public void handleMethodAnnotation(ListSeparator annotation, MethodConfigBuilder builder) {
        builder.setParamsListSeparator(annotation.value());
    }
                  
    @Override
    public void handleParameterAnnotation(ListSeparator annotation, ParamConfigBuilder builder) {
        builder.setListSeparator(annotation.value());
    }
    
}
