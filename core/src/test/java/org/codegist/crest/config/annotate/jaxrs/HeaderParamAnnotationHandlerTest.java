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

import org.codegist.crest.config.ParamType;
import org.codegist.crest.config.annotate.AnnotationHandler;
import org.codegist.crest.config.annotate.ParamOnlyAnnotationBaseTest;
import org.junit.Test;

import javax.ws.rs.HeaderParam;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class HeaderParamAnnotationHandlerTest extends ParamOnlyAnnotationBaseTest<HeaderParam> {

    private final HeaderParamAnnotationHandler toTest = new HeaderParamAnnotationHandler(crestConfig);

    public HeaderParamAnnotationHandlerTest() {
        super(HeaderParam.class);
    }

    @Test
    public void handleParameterAnnotationShouldSetTypeAndName() throws Exception {
        when(mockParamConfigBuilder.setType(any(ParamType.class))).thenReturn(mockParamConfigBuilder);
        when(mockAnnotation.value()).thenReturn("a");
        toTest.handleParameterAnnotation(mockAnnotation, mockParamConfigBuilder);
        verify(mockParamConfigBuilder).setType(ParamType.HEADER);
        verify(mockParamConfigBuilder).setName("a");
        verify(mockAnnotation).value();
    }

    @Test
    public void handleParameterAnnotationShouldMergePlaceholdersAndSetTypeAndName() throws Exception {
        when(mockParamConfigBuilder.setType(any(ParamType.class))).thenReturn(mockParamConfigBuilder);
        when(mockAnnotation.value()).thenReturn(VAL_WITH_PH);
        toTest.handleParameterAnnotation(mockAnnotation, mockParamConfigBuilder);
        verify(mockParamConfigBuilder).setType(ParamType.HEADER);
        verify(mockParamConfigBuilder).setName(EXPECTED_MERGE_VAL);
        verify(mockAnnotation).value();
    }

    @Override
    public AnnotationHandler<HeaderParam> getToTest() {
        return toTest;
    }
}
