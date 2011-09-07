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

package org.codegist.crest.util;

import org.codegist.crest.NonInstanciableClassTest;
import org.codegist.crest.config.MethodConfig;
import org.codegist.crest.config.ParamConfig;
import org.codegist.crest.interceptor.RequestInterceptor;
import org.codegist.crest.io.Request;
import org.codegist.crest.io.RequestBuilder;
import org.codegist.crest.io.RequestBuilderFactory;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class RequestsTest extends NonInstanciableClassTest {
    public RequestsTest() {
        super(Requests.class);
    }

    private final RequestBuilderFactory mockRequestBuilderFactory = mock(RequestBuilderFactory.class);
    private final RequestBuilder mockRequestBuilder = mock(RequestBuilder.class);
    private final MethodConfig mockMethodConfig = mock(MethodConfig.class);

    @Test
    public void shouldBuildARequestFromGivenParams() throws Exception {
        Object[] args = new Object[]{"1",new Object[]{"a","b"}, new Object[]{null}, asList("a","b", "c"), null};

        ParamConfig[] extraparams = {};
        ParamConfig pc1 = mock(ParamConfig.class);
        ParamConfig pc2 = mock(ParamConfig.class);
        ParamConfig pc3 = mock(ParamConfig.class);
        ParamConfig pc4 = mock(ParamConfig.class);
        ParamConfig pc5 = mock(ParamConfig.class);
        RequestInterceptor requestInterceptor = mock(RequestInterceptor.class);
        Request expected = mock(Request.class);

        when(mockRequestBuilderFactory.create()).thenReturn(mockRequestBuilder);
        when(mockMethodConfig.getExtraParams()).thenReturn(extraparams);
        when(mockRequestBuilder.addParams(extraparams)).thenReturn(mockRequestBuilder);


        when(mockMethodConfig.getParamCount()).thenReturn(args.length);

        when(mockMethodConfig.getParamConfig(0)).thenReturn(pc1);
        when(mockMethodConfig.getParamConfig(1)).thenReturn(pc2);
        when(mockMethodConfig.getParamConfig(2)).thenReturn(pc3);
        when(mockMethodConfig.getParamConfig(3)).thenReturn(pc4);
        when(mockMethodConfig.getParamConfig(4)).thenReturn(pc5);
        when(mockMethodConfig.getRequestInterceptor()).thenReturn(requestInterceptor);
        when(pc5.getDefaultValue()).thenReturn("default");


        when(mockRequestBuilder.build(mockMethodConfig)).thenReturn(expected);

        Request actual = Requests.from(mockRequestBuilderFactory, mockMethodConfig, args);

        assertSame(expected, actual);
        verify(mockRequestBuilder).addParams(extraparams);
        verify(mockRequestBuilder).addParam(pc1, Collections.<Object>singleton("1"));
        verify(mockRequestBuilder).addParam(pc2, Arrays.<Object>asList("a","b"));
        verify(mockRequestBuilder).addParam(pc4, Arrays.<Object>asList("a","b","c"));
        verify(mockRequestBuilder).addParam(pc5);
        verify(requestInterceptor).beforeFire(mockRequestBuilder, mockMethodConfig, args);
        verify(mockRequestBuilder).build(mockMethodConfig);
        verifyNoMoreInteractions(mockRequestBuilder);

    }
}
