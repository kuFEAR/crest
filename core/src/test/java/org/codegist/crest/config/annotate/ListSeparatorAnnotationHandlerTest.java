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

import org.codegist.crest.annotate.ListSeparator;
import org.junit.Test;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class ListSeparatorAnnotationHandlerTest extends AnnotationBaseTest<ListSeparator> {

    private final ListSeparatorAnnotationHandler toTest = new ListSeparatorAnnotationHandler();

    public ListSeparatorAnnotationHandlerTest() {
        super(ListSeparator.class);
    }

    @Test
    public void handleInterfaceAnnotationShouldSetParamsListSeparator() throws Exception {
        when(mockAnnotation.value()).thenReturn("a");
        toTest.handleInterfaceAnnotation(mockAnnotation, mockInterfaceConfigBuilder);
        verify(mockAnnotation).value();
        verify(mockInterfaceConfigBuilder).setParamsListSeparator("a");
    }

    @Test
    public void handleMethodsAnnotationShouldSetListSeparator() throws Exception {
        when(mockAnnotation.value()).thenReturn("a");
        toTest.handleMethodAnnotation(mockAnnotation, mockMethodConfigBuilder);
        verify(mockAnnotation).value();
        verify(mockMethodConfigBuilder).setParamsListSeparator("a");
    }

    @Test
    public void handleParametersAnnotationShouldSetListSeparator() throws Exception {
        when(mockAnnotation.value()).thenReturn("a");
        toTest.handleParameterAnnotation(mockAnnotation, mockParamConfigBuilder);
        verify(mockAnnotation).value();
        verify(mockParamConfigBuilder).setListSeparator("a");
    }

    @Override
    public AnnotationHandler<ListSeparator> getToTest() {
        return toTest;
    }
}
