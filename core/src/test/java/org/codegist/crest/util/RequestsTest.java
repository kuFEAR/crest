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
import org.codegist.crest.io.Request;
import org.codegist.crest.io.RequestBuilder;
import org.codegist.crest.io.RequestBuilderFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Params.class)
public class RequestsTest extends NonInstanciableClassTest {
    public RequestsTest() {
        super(Requests.class);
    }

    private final RequestBuilderFactory mockRequestBuilderFactory = mock(RequestBuilderFactory.class);
    private final RequestBuilder mockRequestBuilder = mock(RequestBuilder.class);
    private final MethodConfig mockMethodConfig = mock(MethodConfig.class);

    @Test
    public void shouldBuildARequestFromGivenParams(){
        Object[] args = new Object[]{"1",null};
        ParamConfig[] extraparams = new ParamConfig[]{};
        ParamConfig pc1 = mock(ParamConfig.class);
        ParamConfig pc2 = mock(ParamConfig.class);
        Request expected = mock(Request.class);

        when(mockMethodConfig.getExtraParams()).thenReturn(extraparams);
        when(mockRequestBuilderFactory.create()).thenReturn(mockRequestBuilder);
        when(mockRequestBuilder.params(extraparams)).thenReturn(mockRequestBuilder);

        mockStatic(Params.class);
        when(mockMethodConfig.getParamCount()).thenReturn(args.length);
        when(mockMethodConfig.getParamConfig(0)).thenReturn(pc1);
        when(Params.isNull("1")).thenReturn(false);
        when(pc1.getDefaultValue()).thenReturn("");

        when(mockMethodConfig.getParamConfig(1)).thenReturn(pc2);
        when(Params.isNull(null)).thenReturn(true);
        when(pc2.getDefaultValue()).thenReturn(null);

        when(mockRequestBuilder.build(mockMethodConfig)).thenReturn(expected);

        Request actual = Requests.from(mockRequestBuilderFactory, mockMethodConfig, args);

        assertSame(expected, actual);
        verify(mockRequestBuilder).addParam(pc1, "1");

    }
}
