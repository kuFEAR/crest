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

import org.junit.Test;

import java.lang.annotation.Annotation;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public abstract class InterfaceOnlyAnnotationBaseTest<A extends Annotation> extends AnnotationBaseTest<A> {

    public InterfaceOnlyAnnotationBaseTest(Class<A> annotation, AnnotationHandler<A> toTest) {
        super(annotation, toTest);
    }

    @Test
    public void handleParamAnnotationShouldDoNothing() throws Exception {
        toTest.handleParameterAnnotation(mockAnnotation, mockParamConfigBuilder);
    }

    @Test
    public void handleMethodAnnotationShouldDoNothing() throws Exception {
        toTest.handleMethodAnnotation(mockAnnotation, mockMethodConfigBuilder);
    }

}
