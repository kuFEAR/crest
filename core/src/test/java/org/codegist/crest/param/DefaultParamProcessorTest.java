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

package org.codegist.crest.param;

import org.codegist.crest.config.ParamConfig;
import org.codegist.crest.serializer.Serializer;
import org.codegist.crest.serializer.ToStringSerializer;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.codegist.crest.test.util.Values.UTF8;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class DefaultParamProcessorTest {

    private final ParamProcessor toTest = DefaultParamProcessor.INSTANCE;

    @Test
    public void shouldProcessParamEncodingThem() throws Exception {
        Serializer serializer = new ToStringSerializer();
        ParamConfig paramConfig = mock(ParamConfig.class);
        when(paramConfig.getSerializer()).thenReturn(serializer);
        when(paramConfig.isEncoded()).thenReturn(false);
        when(paramConfig.getName()).thenReturn("p1");

        Param param = mock(Param.class);
        when(param.getParamConfig()).thenReturn(paramConfig);
        when(param.getValue()).thenReturn((Collection) asList("v 11", "v 12"));

        List<EncodedPair> actual = toTest.process(param, UTF8, true);
        assertEquals(2, actual.size());
        assertEquals("p1", actual.get(0).getName());
        assertEquals("v%2011", actual.get(0).getValue());
        assertEquals("p1", actual.get(1).getName());
        assertEquals("v%2012", actual.get(1).getValue());
    }

    @Test
    public void shouldProcessPreEncodedParams() throws Exception {
        Serializer serializer = new ToStringSerializer();
        ParamConfig paramConfig = mock(ParamConfig.class);
        when(paramConfig.getSerializer()).thenReturn(serializer);
        when(paramConfig.isEncoded()).thenReturn(true);
        when(paramConfig.getName()).thenReturn("p1");

        Param param = mock(Param.class);
        when(param.getParamConfig()).thenReturn(paramConfig);
        when(param.getValue()).thenReturn((Collection) asList("v%2011", "v%2012"));

        List<EncodedPair> actual = toTest.process(param, UTF8, true);
        assertEquals(2, actual.size());
        assertEquals("p1", actual.get(0).getName());
        assertEquals("v%2011", actual.get(0).getValue());
        assertEquals("p1", actual.get(1).getName());
        assertEquals("v%2012", actual.get(1).getValue());
    }



    @Test
    public void shouldProcessParamIgnoringEncodingFlag() throws Exception {
        Serializer serializer = new ToStringSerializer();
        ParamConfig paramConfig = mock(ParamConfig.class);
        when(paramConfig.getSerializer()).thenReturn(serializer);
        when(paramConfig.isEncoded()).thenReturn(false);
        when(paramConfig.getName()).thenReturn("p1");

        Param param = mock(Param.class);
        when(param.getParamConfig()).thenReturn(paramConfig);
        when(param.getValue()).thenReturn((Collection) asList("v 11", "v 12"));

        List<EncodedPair> actual = toTest.process(param, UTF8, false);
        assertEquals(2, actual.size());
        assertEquals("p1", actual.get(0).getName());
        assertEquals("v 11", actual.get(0).getValue());
        assertEquals("p1", actual.get(1).getName());
        assertEquals("v 12", actual.get(1).getValue());
    }

    @Test
    public void shouldProcessPreEncodedParamsIgnoringEncodingFlag() throws Exception {
        Serializer serializer = new ToStringSerializer();
        ParamConfig paramConfig = mock(ParamConfig.class);
        when(paramConfig.getSerializer()).thenReturn(serializer);
        when(paramConfig.isEncoded()).thenReturn(true);
        when(paramConfig.getName()).thenReturn("p1");

        Param param = mock(Param.class);
        when(param.getParamConfig()).thenReturn(paramConfig);
        when(param.getValue()).thenReturn((Collection) asList("v%2011", "v%2012"));

        List<EncodedPair> actual = toTest.process(param, UTF8, false);
        assertEquals(2, actual.size());
        assertEquals("p1", actual.get(0).getName());
        assertEquals("v%2011", actual.get(0).getValue());
        assertEquals("p1", actual.get(1).getName());
        assertEquals("v%2012", actual.get(1).getValue());
    }

    @Test
    public void shouldProcessEmptyParams() throws Exception {
        Serializer serializer = new ToStringSerializer();
        ParamConfig paramConfig = mock(ParamConfig.class);
        when(paramConfig.getSerializer()).thenReturn(serializer);
        when(paramConfig.isEncoded()).thenReturn(true);
        when(paramConfig.getName()).thenReturn("p1");

        Param param = mock(Param.class);
        when(param.getParamConfig()).thenReturn(paramConfig);
        when(param.getValue()).thenReturn(Collections.<Object>emptyList());

        List<EncodedPair> actual = toTest.process(param, UTF8, true);
        assertEquals(0, actual.size());
    }


}
