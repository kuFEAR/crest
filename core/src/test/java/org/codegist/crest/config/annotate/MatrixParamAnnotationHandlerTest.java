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

import org.codegist.crest.annotate.MatrixParam;
import org.codegist.crest.config.ParamConfigBuilder;
import org.codegist.crest.config.ParamType;
import org.junit.After;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class MatrixParamAnnotationHandlerTest extends AnnotationBaseTest<MatrixParam> {

    private final MatrixParamAnnotationHandler toTest = new MatrixParamAnnotationHandler(crestConfig);
    private final ParamConfigBuilder mockParamConfigBuilder = mock(ParamConfigBuilder.class);

    public MatrixParamAnnotationHandlerTest() {
        super(MatrixParam.class);
    }

    @Test
    public void handleInterfaceAnnotationShouldSetParamsTypeAndNameAndDefaultValue() throws Exception {
        when(mockInterfaceConfigBuilder.startMethodsExtraParamConfig()).thenReturn(mockParamConfigBuilder);
        when(mockParamConfigBuilder.setType(any(ParamType.class))).thenReturn(mockParamConfigBuilder);
        when(mockParamConfigBuilder.setName(any(String.class))).thenReturn(mockParamConfigBuilder);
        when(mockAnnotation.value()).thenReturn("a");
        when(mockAnnotation.defaultValue()).thenReturn("b");
        toTest.handleInterfaceAnnotation(mockAnnotation, mockInterfaceConfigBuilder);
        verify(mockInterfaceConfigBuilder).startMethodsExtraParamConfig();
        verify(mockParamConfigBuilder).setType(ParamType.MATRIX);
        verify(mockParamConfigBuilder).setName("a");
        verify(mockParamConfigBuilder).setDefaultValue("b");
        verify(mockAnnotation).value();
        verify(mockAnnotation).defaultValue();
    }

    @Test
    public void handleInterfaceAnnotationShouldSetParamsTypeAndName() throws Exception {
        when(mockInterfaceConfigBuilder.startMethodsExtraParamConfig()).thenReturn(mockParamConfigBuilder);
        when(mockParamConfigBuilder.setType(any(ParamType.class))).thenReturn(mockParamConfigBuilder);
        when(mockParamConfigBuilder.setName(any(String.class))).thenReturn(mockParamConfigBuilder);
        when(mockAnnotation.value()).thenReturn("a");
        when(mockAnnotation.defaultValue()).thenReturn("");
        toTest.handleInterfaceAnnotation(mockAnnotation, mockInterfaceConfigBuilder);
        verify(mockInterfaceConfigBuilder).startMethodsExtraParamConfig();
        verify(mockParamConfigBuilder).setType(ParamType.MATRIX);
        verify(mockParamConfigBuilder).setName("a");
        verify(mockParamConfigBuilder).setDefaultValue(null);
        verify(mockAnnotation).value();
        verify(mockAnnotation).defaultValue();
    }

    @Test
    public void handleInterfaceAnnotationShouldMergePlaceholdersAndSetParamsTypeAndNameAndDefaultValue() throws Exception {
        when(mockInterfaceConfigBuilder.startMethodsExtraParamConfig()).thenReturn(mockParamConfigBuilder);
        when(mockParamConfigBuilder.setType(any(ParamType.class))).thenReturn(mockParamConfigBuilder);
        when(mockParamConfigBuilder.setName(any(String.class))).thenReturn(mockParamConfigBuilder);
        when(mockAnnotation.value()).thenReturn(VAL_WITH_PH + "a");
        when(mockAnnotation.defaultValue()).thenReturn(VAL_WITH_PH + "b");
        toTest.handleInterfaceAnnotation(mockAnnotation, mockInterfaceConfigBuilder);
        verify(mockInterfaceConfigBuilder).startMethodsExtraParamConfig();
        verify(mockParamConfigBuilder).setType(ParamType.MATRIX);
        verify(mockParamConfigBuilder).setName(EXPECTED_MERGE_VAL + "a");
        verify(mockParamConfigBuilder).setDefaultValue(EXPECTED_MERGE_VAL + "b");
        verify(mockAnnotation).value();
        verify(mockAnnotation).defaultValue();
    }

    @Test
    public void handleMethodAnnotationShouldSetParamsTypeAndNameAndDefaultValue() throws Exception {
        when(mockMethodConfigBuilder.startExtraParamConfig()).thenReturn(mockParamConfigBuilder);
        when(mockParamConfigBuilder.setType(any(ParamType.class))).thenReturn(mockParamConfigBuilder);
        when(mockParamConfigBuilder.setName(any(String.class))).thenReturn(mockParamConfigBuilder);
        when(mockAnnotation.value()).thenReturn("a");
        when(mockAnnotation.defaultValue()).thenReturn("b");
        toTest.handleMethodAnnotation(mockAnnotation, mockMethodConfigBuilder);
        verify(mockMethodConfigBuilder).startExtraParamConfig();
        verify(mockParamConfigBuilder).setType(ParamType.MATRIX);
        verify(mockParamConfigBuilder).setName("a");
        verify(mockParamConfigBuilder).setDefaultValue("b");
        verify(mockAnnotation).value();
        verify(mockAnnotation).defaultValue();
    }

    @Test
    public void handleMethodAnnotationShouldSetParamsTypeAndName() throws Exception {
        when(mockMethodConfigBuilder.startExtraParamConfig()).thenReturn(mockParamConfigBuilder);
        when(mockParamConfigBuilder.setType(any(ParamType.class))).thenReturn(mockParamConfigBuilder);
        when(mockParamConfigBuilder.setName(any(String.class))).thenReturn(mockParamConfigBuilder);
        when(mockAnnotation.value()).thenReturn("a");
        when(mockAnnotation.defaultValue()).thenReturn("");
        toTest.handleMethodAnnotation(mockAnnotation, mockMethodConfigBuilder);
        verify(mockMethodConfigBuilder).startExtraParamConfig();
        verify(mockParamConfigBuilder).setType(ParamType.MATRIX);
        verify(mockParamConfigBuilder).setName("a");
        verify(mockParamConfigBuilder).setDefaultValue(null);
        verify(mockAnnotation).value();
        verify(mockAnnotation).defaultValue();
    }

    @Test
    public void handleMethodAnnotationShouldMergePlaceholdersAndSetParamsTypeAndNameAndDefaultValue() throws Exception {
        when(mockMethodConfigBuilder.startExtraParamConfig()).thenReturn(mockParamConfigBuilder);
        when(mockParamConfigBuilder.setType(any(ParamType.class))).thenReturn(mockParamConfigBuilder);
        when(mockParamConfigBuilder.setName(any(String.class))).thenReturn(mockParamConfigBuilder);
        when(mockAnnotation.value()).thenReturn(VAL_WITH_PH + "a");
        when(mockAnnotation.defaultValue()).thenReturn(VAL_WITH_PH + "b");
        toTest.handleMethodAnnotation(mockAnnotation, mockMethodConfigBuilder);
        verify(mockMethodConfigBuilder).startExtraParamConfig();
        verify(mockParamConfigBuilder).setType(ParamType.MATRIX);
        verify(mockParamConfigBuilder).setName(EXPECTED_MERGE_VAL + "a");
        verify(mockParamConfigBuilder).setDefaultValue(EXPECTED_MERGE_VAL + "b");
        verify(mockAnnotation).value();
        verify(mockAnnotation).defaultValue();
    }

    @Test
    public void handleParameterAnnotationShouldSetTypeAndNameAndDefaultValue() throws Exception {
        when(super.mockParamConfigBuilder.setType(any(ParamType.class))).thenReturn(super.mockParamConfigBuilder);
        when(super.mockParamConfigBuilder.setName(any(String.class))).thenReturn(super.mockParamConfigBuilder);
        when(super.mockParamConfigBuilder.setDefaultValue(anyString())).thenReturn(super.mockParamConfigBuilder);
        when(mockAnnotation.value()).thenReturn("a");
        when(mockAnnotation.defaultValue()).thenReturn("b");
        toTest.handleParameterAnnotation(mockAnnotation, super.mockParamConfigBuilder);
        verify(super.mockParamConfigBuilder).setType(ParamType.MATRIX);
        verify(super.mockParamConfigBuilder).setName("a");
        verify(super.mockParamConfigBuilder).setDefaultValue("b");
        verify(mockAnnotation).value();
        verify(mockAnnotation).defaultValue();
    }

    @Test
    public void handleParameterAnnotationShouldSetTypeAndName() throws Exception {
        when(super.mockParamConfigBuilder.setType(any(ParamType.class))).thenReturn(super.mockParamConfigBuilder);
        when(super.mockParamConfigBuilder.setName(any(String.class))).thenReturn(super.mockParamConfigBuilder);
        when(super.mockParamConfigBuilder.setDefaultValue(anyString())).thenReturn(super.mockParamConfigBuilder);
        when(mockAnnotation.value()).thenReturn("a");
        when(mockAnnotation.defaultValue()).thenReturn("");
        toTest.handleParameterAnnotation(mockAnnotation, super.mockParamConfigBuilder);
        verify(super.mockParamConfigBuilder).setType(ParamType.MATRIX);
        verify(super.mockParamConfigBuilder).setName("a");
        verify(super.mockParamConfigBuilder).setDefaultValue(null);
        verify(mockAnnotation).value();
        verify(mockAnnotation).defaultValue();
    }

    @Test
    public void handleParameterAnnotationShouldMergePlaceholdersAndSetTypeAndNameAndDefaultValue() throws Exception {
        when(super.mockParamConfigBuilder.setType(any(ParamType.class))).thenReturn(super.mockParamConfigBuilder);
        when(super.mockParamConfigBuilder.setName(any(String.class))).thenReturn(super.mockParamConfigBuilder);
        when(super.mockParamConfigBuilder.setDefaultValue(anyString())).thenReturn(super.mockParamConfigBuilder);
        when(mockAnnotation.value()).thenReturn(VAL_WITH_PH + "a");
        when(mockAnnotation.defaultValue()).thenReturn(VAL_WITH_PH + "b");
        toTest.handleParameterAnnotation(mockAnnotation, super.mockParamConfigBuilder);
        verify(super.mockParamConfigBuilder).setType(ParamType.MATRIX);
        verify(super.mockParamConfigBuilder).setName(EXPECTED_MERGE_VAL + "a");
        verify(super.mockParamConfigBuilder).setDefaultValue(EXPECTED_MERGE_VAL + "b");
        verify(mockAnnotation).value();
        verify(mockAnnotation).defaultValue();
    }

    @After
    public void noMoreInteractions(){
        super.noMoreInteractions();
        verifyNoMoreInteractions(mockParamConfigBuilder);
    }

    @Override
    public AnnotationHandler<MatrixParam> getToTest() {
        return toTest;
    }
}
