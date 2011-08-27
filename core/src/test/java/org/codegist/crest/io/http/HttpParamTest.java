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

import org.codegist.crest.config.ParamConfig;
import org.junit.Test;

import java.util.Collection;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author laurent.gilles@codegist.org
 */
public class HttpParamTest {

    private final ParamConfig paramConfig = mock(ParamConfig.class);
    private final Collection<Object> value = (Collection<Object>)(Collection)asList("a","b");
    private final HttpParam toTest = new HttpParam(paramConfig,value);

    @Test
    public void getValueShouldReturnIt(){
        assertEquals(value, toTest.getValue());
    }

    @Test
    public void getParamConfigReturnIt(){
        assertSame(paramConfig, toTest.getParamConfig());
    }

    @Test
    public void toStringShouldReturnSomethingUsefull(){
        when(paramConfig.getName()).thenReturn("name");
        assertEquals("HttpParam[name=name,value=[a, b]]", toTest.toString());
    }
}
