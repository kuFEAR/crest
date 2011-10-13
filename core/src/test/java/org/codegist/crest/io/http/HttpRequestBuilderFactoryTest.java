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

package org.codegist.crest.io.http;

import org.codegist.crest.config.MethodConfig;
import org.codegist.crest.config.ParamConfig;
import org.codegist.crest.config.ParamType;
import org.codegist.crest.io.Request;
import org.codegist.crest.io.RequestBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author laurent.gilles@codegist.org
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(ParamType.class)
public class HttpRequestBuilderFactoryTest {

    private final RequestBuilder toTest = new HttpRequestBuilderFactory().create();

    @Test
    public void addParamsValueShouldAddParam(){
        for(ParamType type : ParamType.values()){
            addParamValueShouldAddParam(type);
        }
    }
    
    @Test
    public void addParamValueWithNoValueShouldAddParamWithDefaultValue(){
        for(ParamType type : ParamType.values()){
            addParamValueWithNoValueShouldAddParamWithDefaultValue(type);
        }
    }


    @Test
    public void addParamsValueWithNoValueShouldAddParamsWithDefaultValue(){
        for(ParamType type : ParamType.values()){
            addParamsValueWithNoValueShouldAddParamsWithDefaultValue(type);
        }
    }

    private void addParamsValueWithNoValueShouldAddParamsWithDefaultValue(ParamType type){
        MethodConfig mockMethodConfig = mock(MethodConfig.class);
        ParamConfig paramConfig1 = mock(ParamConfig.class);
        when(paramConfig1.getType()).thenReturn(type);
        when(paramConfig1.getDefaultValue()).thenReturn("def1");
        ParamConfig paramConfig2 = mock(ParamConfig.class);
        when(paramConfig2.getType()).thenReturn(type);
        when(paramConfig2.getDefaultValue()).thenReturn("def2");
        ParamConfig paramConfig3 = mock(ParamConfig.class);
        when(paramConfig3.getType()).thenReturn(type);
        when(paramConfig3.getDefaultValue()).thenReturn("def3");

        assertSame(toTest, toTest.addParams(new ParamConfig[]{paramConfig1,paramConfig2,paramConfig3}));

        Request actual = toTest.build(mockMethodConfig);
        assertSame(mockMethodConfig, actual.getMethodConfig());
        assertEquals(3, actual.getParams(type).size());

        assertEquals(paramConfig1, actual.getParams(type).get(0).getParamConfig());
        assertEquals(1, actual.getParams(type).get(0).getValue().size());
        assertEquals("def1", actual.getParams(type).get(0).getValue().iterator().next());

        assertEquals(paramConfig2, actual.getParams(type).get(1).getParamConfig());
        assertEquals(1, actual.getParams(type).get(1).getValue().size());
        assertEquals("def2", new ArrayList(actual.getParams(type).get(1).getValue()).get(0));

        assertEquals(paramConfig3, actual.getParams(type).get(2).getParamConfig());
        assertEquals(1, actual.getParams(type).get(2).getValue().size());
        assertEquals("def3", new ArrayList(actual.getParams(type).get(2).getValue()).get(0));
    }

    private void addParamValueWithNoValueShouldAddParamWithDefaultValue(ParamType type){
        MethodConfig mockMethodConfig = mock(MethodConfig.class);
        ParamConfig paramConfig1 = mock(ParamConfig.class);
        when(paramConfig1.getType()).thenReturn(type);
        when(paramConfig1.getDefaultValue()).thenReturn("def1");

        assertSame(toTest, toTest.addParam(paramConfig1));

        Request actual = toTest.build(mockMethodConfig);
        assertSame(mockMethodConfig, actual.getMethodConfig());
        assertEquals(1, actual.getParams(type).size());

        assertEquals(paramConfig1, actual.getParams(type).get(0).getParamConfig());
        assertEquals(1, actual.getParams(type).get(0).getValue().size());
        assertEquals("def1", actual.getParams(type).get(0).getValue().iterator().next());
    }


    private void addParamValueShouldAddParam(ParamType type){
        MethodConfig mockMethodConfig = mock(MethodConfig.class);
        ParamConfig paramConfig1 = mock(ParamConfig.class);
        when(paramConfig1.getType()).thenReturn(type);
        ParamConfig paramConfig2 = mock(ParamConfig.class);
        when(paramConfig2.getType()).thenReturn(type);

        assertSame(toTest, toTest.addParam(paramConfig1, Arrays.<Object>asList("value")));
        assertSame(toTest, toTest.addParam(paramConfig2, Arrays.<Object>asList("value1","value2")));

        Request actual = toTest.build(mockMethodConfig);
        assertSame(mockMethodConfig, actual.getMethodConfig());
        assertEquals(2, actual.getParams(type).size());

        assertEquals(paramConfig1, actual.getParams(type).get(0).getParamConfig());
        assertEquals(1, actual.getParams(type).get(0).getValue().size());
        assertEquals("value", actual.getParams(type).get(0).getValue().iterator().next());

        assertEquals(paramConfig2, actual.getParams(type).get(1).getParamConfig());
        assertEquals(2, actual.getParams(type).get(1).getValue().size());
        assertEquals("value1", new ArrayList(actual.getParams(type).get(1).getValue()).get(0));
        assertEquals("value2", new ArrayList(actual.getParams(type).get(1).getValue()).get(1));

    }



}
