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

package org.codegist.crest.interceptor;

import org.codegist.crest.CRestConfig;
import org.codegist.crest.config.MethodConfig;
import org.codegist.crest.config.ParamConfigBuilder;
import org.codegist.crest.config.ParamConfigBuilderFactory;
import org.codegist.crest.io.RequestBuilder;
import org.junit.Test;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class AbstractRequestInterceptorTest {

    private final ParamConfigBuilderFactory paramConfigBuilderFactory = mock(ParamConfigBuilderFactory.class);
    private final CRestConfig config = mock(CRestConfig.class);
    {
        when(config.get(ParamConfigBuilderFactory.class)).thenReturn(paramConfigBuilderFactory);
    }

    private final AbstractRequestInterceptor toTest = new AbstractRequestInterceptor(config){
        public void beforeFire(RequestBuilder requestBuilder, MethodConfig methodConfig, Object[] args) throws Exception {

        }
    };
    

    @Test
    public void newParamConfigShouldBuildANewParamConfigWithGivenArg(){
        ParamConfigBuilder expected = mock(ParamConfigBuilder.class);
        when(paramConfigBuilderFactory.newInstance(String.class, String.class)).thenReturn(expected);

        ParamConfigBuilder actual = toTest.newParamConfig(String.class);
        assertSame(expected, actual);
    }


    @Test
    public void newParamConfigShouldBuildANewParamConfigWithGivenArgs(){
        ParamConfigBuilder expected = mock(ParamConfigBuilder.class);
        when(paramConfigBuilderFactory.newInstance(String.class, Integer.class)).thenReturn(expected);

        ParamConfigBuilder actual = toTest.newParamConfig(String.class, Integer.class);
        assertSame(expected, actual);
    }
}
