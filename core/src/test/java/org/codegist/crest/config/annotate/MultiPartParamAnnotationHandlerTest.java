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

import org.codegist.crest.annotate.FormParam;
import org.codegist.crest.annotate.MultiPartParam;
import org.codegist.crest.config.ParamConfigBuilder;
import org.codegist.crest.config.ParamType;
import org.codegist.crest.util.MultiParts;
import org.junit.After;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class MultiPartParamAnnotationHandlerTest extends AnnotationBaseTest<MultiPartParam> {

    private final MultiPartParamAnnotationHandler toTest = new MultiPartParamAnnotationHandler();
    private final ParamConfigBuilder mockParamConfigBuilder = mock(ParamConfigBuilder.class);

    public MultiPartParamAnnotationHandlerTest() {
        super(MultiPartParam.class);
    }

    @Test
    public void handleInterfaceAnnotationShouldSetParamsTypeAndNameAndDefaultValue() throws Exception {
        when(mockInterfaceConfigBuilder.startMethodsExtraParamConfig()).thenReturn(mockParamConfigBuilder);
        when(mockParamConfigBuilder.setType(any(ParamType.class))).thenReturn(mockParamConfigBuilder);
        when(mockParamConfigBuilder.setName(any(String.class))).thenReturn(mockParamConfigBuilder);
        when(mockParamConfigBuilder.setDefaultValue(anyString())).thenReturn(mockParamConfigBuilder);
        when(mockParamConfigBuilder.setMetaDatas(any(Map.class))).thenReturn(mockParamConfigBuilder);
        when(mockAnnotation.value()).thenReturn("a");
        when(mockAnnotation.defaultValue()).thenReturn("b");
        when(mockAnnotation.contentType()).thenReturn("c");
        when(mockAnnotation.fileName()).thenReturn("d");
        toTest.handleInterfaceAnnotation(mockAnnotation, mockInterfaceConfigBuilder);
        verify(mockInterfaceConfigBuilder).startMethodsExtraParamConfig();
        verify(mockParamConfigBuilder).setType(ParamType.FORM);
        verify(mockParamConfigBuilder).setName("a");
        verify(mockParamConfigBuilder).setDefaultValue("b");
        verify(mockParamConfigBuilder).setMetaDatas(MultiParts.toMetaDatas("c", "d"));
        verify(mockAnnotation).value();
        verify(mockAnnotation).defaultValue();
        verify(mockAnnotation).contentType();
        verify(mockAnnotation).fileName();
    }

    @Test
    public void handleInterfaceAnnotationShouldSetParamsTypeAndName() throws Exception {
       when(mockInterfaceConfigBuilder.startMethodsExtraParamConfig()).thenReturn(mockParamConfigBuilder);
        when(mockParamConfigBuilder.setType(any(ParamType.class))).thenReturn(mockParamConfigBuilder);
        when(mockParamConfigBuilder.setName(any(String.class))).thenReturn(mockParamConfigBuilder);
        when(mockParamConfigBuilder.setDefaultValue(anyString())).thenReturn(mockParamConfigBuilder);
        when(mockParamConfigBuilder.setMetaDatas(any(Map.class))).thenReturn(mockParamConfigBuilder);
        when(mockAnnotation.value()).thenReturn("a");
        when(mockAnnotation.defaultValue()).thenReturn("");
        when(mockAnnotation.contentType()).thenReturn("");
        when(mockAnnotation.fileName()).thenReturn("");
        toTest.handleInterfaceAnnotation(mockAnnotation, mockInterfaceConfigBuilder);
        verify(mockInterfaceConfigBuilder).startMethodsExtraParamConfig();
        verify(mockParamConfigBuilder).setType(ParamType.FORM);
        verify(mockParamConfigBuilder).setName("a");
        verify(mockParamConfigBuilder).setDefaultValue(null);
        verify(mockParamConfigBuilder).setMetaDatas(MultiParts.toMetaDatas(null,null));
        verify(mockAnnotation).value();
        verify(mockAnnotation).defaultValue();
        verify(mockAnnotation).contentType();
        verify(mockAnnotation).fileName();
    }

    @Test
    public void handleMethodAnnotationShouldSetParamsTypeAndNameAndDefaultValue() throws Exception {
        when(mockMethodConfigBuilder.startExtraParamConfig()).thenReturn(mockParamConfigBuilder);
        when(mockParamConfigBuilder.setType(any(ParamType.class))).thenReturn(mockParamConfigBuilder);
        when(mockParamConfigBuilder.setName(any(String.class))).thenReturn(mockParamConfigBuilder);
        when(mockParamConfigBuilder.setDefaultValue(anyString())).thenReturn(mockParamConfigBuilder);
        when(mockParamConfigBuilder.setMetaDatas(any(Map.class))).thenReturn(mockParamConfigBuilder);
        when(mockAnnotation.value()).thenReturn("a");
        when(mockAnnotation.defaultValue()).thenReturn("b");
        when(mockAnnotation.contentType()).thenReturn("c");
        when(mockAnnotation.fileName()).thenReturn("d");
        toTest.handleMethodAnnotation(mockAnnotation, mockMethodConfigBuilder);
        verify(mockMethodConfigBuilder).startExtraParamConfig();
        verify(mockParamConfigBuilder).setType(ParamType.FORM);
        verify(mockParamConfigBuilder).setName("a");
        verify(mockParamConfigBuilder).setDefaultValue("b");
        verify(mockParamConfigBuilder).setMetaDatas(MultiParts.toMetaDatas("c", "d"));
        verify(mockAnnotation).value();
        verify(mockAnnotation).defaultValue();
        verify(mockAnnotation).contentType();
        verify(mockAnnotation).fileName();
    }

    @Test
    public void handleMethodAnnotationShouldSetParamsTypeAndName() throws Exception {
        when(mockMethodConfigBuilder.startExtraParamConfig()).thenReturn(mockParamConfigBuilder);
        when(mockParamConfigBuilder.setType(any(ParamType.class))).thenReturn(mockParamConfigBuilder);
        when(mockParamConfigBuilder.setName(any(String.class))).thenReturn(mockParamConfigBuilder);
        when(mockParamConfigBuilder.setDefaultValue(anyString())).thenReturn(mockParamConfigBuilder);
        when(mockParamConfigBuilder.setMetaDatas(any(Map.class))).thenReturn(mockParamConfigBuilder);
        when(mockAnnotation.value()).thenReturn("a");
        when(mockAnnotation.defaultValue()).thenReturn("");
        when(mockAnnotation.contentType()).thenReturn("");
        when(mockAnnotation.fileName()).thenReturn("");
        toTest.handleMethodAnnotation(mockAnnotation, mockMethodConfigBuilder);
        verify(mockMethodConfigBuilder).startExtraParamConfig();
        verify(mockParamConfigBuilder).setType(ParamType.FORM);
        verify(mockParamConfigBuilder).setName("a");
        verify(mockParamConfigBuilder).setDefaultValue(null);
        verify(mockParamConfigBuilder).setMetaDatas(MultiParts.toMetaDatas(null,null));
        verify(mockAnnotation).value();
        verify(mockAnnotation).defaultValue();
        verify(mockAnnotation).contentType();
        verify(mockAnnotation).fileName();
    }

    @Test
    public void handleParameterAnnotationShouldSetTypeAndNameAndDefaultValue() throws Exception {
        when(super.mockParamConfigBuilder.setType(any(ParamType.class))).thenReturn(super.mockParamConfigBuilder);
        when(super.mockParamConfigBuilder.setName(any(String.class))).thenReturn(super.mockParamConfigBuilder);
        when(super.mockParamConfigBuilder.setDefaultValue(anyString())).thenReturn(super.mockParamConfigBuilder);
        when(super.mockParamConfigBuilder.setMetaDatas(any(Map.class))).thenReturn(super.mockParamConfigBuilder);
        when(mockAnnotation.value()).thenReturn("a");
        when(mockAnnotation.defaultValue()).thenReturn("b");
        when(mockAnnotation.contentType()).thenReturn("c");
        when(mockAnnotation.fileName()).thenReturn("d");
        toTest.handleParameterAnnotation(mockAnnotation, super.mockParamConfigBuilder);
        verify(super.mockParamConfigBuilder).setType(ParamType.FORM);
        verify(super.mockParamConfigBuilder).setName("a");
        verify(super.mockParamConfigBuilder).setDefaultValue("b");
        verify(super.mockParamConfigBuilder).setMetaDatas(MultiParts.toMetaDatas("c", "d"));
        verify(mockAnnotation).value();
        verify(mockAnnotation).defaultValue();
        verify(mockAnnotation).contentType();
        verify(mockAnnotation).fileName();
    }

    @Test
    public void handleParameterAnnotationShouldSetTypeAndName() throws Exception {
        when(super.mockParamConfigBuilder.setType(any(ParamType.class))).thenReturn(super.mockParamConfigBuilder);
        when(super.mockParamConfigBuilder.setName(any(String.class))).thenReturn(super.mockParamConfigBuilder);
        when(super.mockParamConfigBuilder.setDefaultValue(anyString())).thenReturn(super.mockParamConfigBuilder);
        when(super.mockParamConfigBuilder.setMetaDatas(any(Map.class))).thenReturn(super.mockParamConfigBuilder);
        when(mockAnnotation.value()).thenReturn("a");
        when(mockAnnotation.defaultValue()).thenReturn("");
        when(mockAnnotation.contentType()).thenReturn("");
        when(mockAnnotation.fileName()).thenReturn("");
        toTest.handleParameterAnnotation(mockAnnotation, super.mockParamConfigBuilder);
        verify(super.mockParamConfigBuilder).setType(ParamType.FORM);
        verify(super.mockParamConfigBuilder).setName("a");
        verify(super.mockParamConfigBuilder).setDefaultValue(null);
        verify(super.mockParamConfigBuilder).setMetaDatas(MultiParts.toMetaDatas(null,null));
        verify(mockAnnotation).value();
        verify(mockAnnotation).defaultValue();
        verify(mockAnnotation).contentType();
        verify(mockAnnotation).fileName();
    }

    @After
    public void noMoreInteractions(){
        super.noMoreInteractions();
        verifyNoMoreInteractions(mockParamConfigBuilder);
    }

    @Override
    public AnnotationHandler<MultiPartParam> getToTest() {
        return toTest;
    }
}
