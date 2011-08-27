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

package org.codegist.crest.io.http;

import org.codegist.crest.config.MethodConfig;
import org.codegist.crest.config.ParamType;
import org.codegist.crest.param.Param;
import org.codegist.crest.param.ParamProcessors;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.codegist.crest.test.util.Values.UTF8;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * @author laurent.gilles@codegist.org
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ParamProcessors.class})
public class HttpRequestTest {

    private final MethodConfig methodConfig = mock(MethodConfig.class);
    private final List<Param> headerParams = asList(mock(Param.class));
    private final List<Param> matrixParams= asList(mock(Param.class));
    private final List<Param> queryParams= asList(mock(Param.class));
    private final List<Param> pathParams= asList(mock(Param.class));
    private final List<Param> cookieParams= asList(mock(Param.class));
    private final List<Param> formParams= asList(mock(Param.class));
    private final Map<ParamType, List<Param>> map = new HashMap<ParamType, List<Param>>();
    {
        when(methodConfig.getCharset()).thenReturn(UTF8);
        map.put(ParamType.COOKIE, cookieParams);
        map.put(ParamType.FORM, formParams);
        map.put(ParamType.HEADER, headerParams);
        map.put(ParamType.MATRIX, matrixParams);
        map.put(ParamType.PATH, pathParams);
        map.put(ParamType.QUERY, queryParams);
    }
    private final HttpRequest toTest = new HttpRequest(methodConfig, headerParams, matrixParams, queryParams, pathParams, cookieParams, formParams);

    @Test
    public void getParamsShouldReturnThem(){
        for(ParamType type : ParamType.values()){
            getParamsShouldReturnExpectedList(type);
        }
    }
    @Test
    public void getEncodedParamsIteratorShouldReturnIteratorOverExpectedList(){
        for(ParamType type : ParamType.values()){
            getEncodedParamsIteratorShouldReturnIteratorOverExpectedList(type);
        }
    }
    @Test
    public void toStringShouldReturnSomethingUsefull(){
        assertEquals(String.format("HttpRequest[methodConfig=Mock for MethodConfig, hashCode: %d,headerParams=[Mock for Param, hashCode: %d],matrixParams=[Mock for Param, hashCode: %d],queryParams=[Mock for Param, hashCode: %d],pathParams=[Mock for Param, hashCode: %d],cookieParams=[Mock for Param, hashCode: %d],formParams=[Mock for Param, hashCode: %d]]",
                methodConfig.hashCode(),
                headerParams.get(0).hashCode(),
                matrixParams.get(0).hashCode(),
                queryParams.get(0).hashCode(),
                pathParams.get(0).hashCode(),
                cookieParams.get(0).hashCode(),
                formParams.get(0).hashCode())
        , toTest.toString());
    }
    
    private void getEncodedParamsIteratorShouldReturnIteratorOverExpectedList(ParamType type){
        Iterator expected = mock(Iterator.class);
        mockStatic(ParamProcessors.class);
        when(ParamProcessors.iterate(map.get(type), UTF8)).thenReturn(expected);
        assertSame(expected, toTest.getEncodedParamsIterator(type));
    }

    private void getParamsShouldReturnExpectedList(ParamType type){
        assertEquals(map.get(type), toTest.getParams(type));
    }
}
