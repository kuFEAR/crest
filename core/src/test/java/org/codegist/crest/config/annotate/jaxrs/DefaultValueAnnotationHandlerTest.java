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

import org.codegist.crest.config.annotate.AnnotationHandler;
import org.codegist.crest.config.annotate.ParamOnlyAnnotationBaseTest;
import org.junit.Test;

import javax.ws.rs.DefaultValue;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class DefaultValueAnnotationHandlerTest extends ParamOnlyAnnotationBaseTest<DefaultValue> {

    private final DefaultValueAnnotationHandler toTest = new DefaultValueAnnotationHandler(crestConfig);

    public DefaultValueAnnotationHandlerTest() {
        super(DefaultValue.class);
    }

    @Test
    public void handleParameterAnnotationShouldFillDefaultValueWithAnnotationValue() throws Exception {
        when(mockAnnotation.value()).thenReturn("a");
        when(mockParamConfigBuilder.setDefaultValue("a")).thenReturn(mockParamConfigBuilder);
        toTest.handleParameterAnnotation(mockAnnotation, mockParamConfigBuilder);
        verify(mockAnnotation).value();
        verify(mockParamConfigBuilder).setDefaultValue("a");
    }
    @Test
    public void handleParameterAnnotationShouldMergePlaceholdersAndFillDefaultValueWithAnnotationValue() throws Exception {
        when(mockAnnotation.value()).thenReturn(VAL_WITH_PH);
        when(mockParamConfigBuilder.setDefaultValue(EXPECTED_MERGE_VAL)).thenReturn(mockParamConfigBuilder);
        toTest.handleParameterAnnotation(mockAnnotation, mockParamConfigBuilder);
        verify(mockAnnotation).value();
        verify(mockParamConfigBuilder).setDefaultValue(EXPECTED_MERGE_VAL);
    }

    @Override
    public AnnotationHandler<DefaultValue> getToTest() {
        return toTest;
    }
}
