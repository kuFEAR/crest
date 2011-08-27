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

package org.codegist.crest.config;

import org.codegist.crest.test.util.Classes;
import org.codegist.crest.test.util.TestInterface;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

/**
 * @author laurent.gilles@codegist.org
 */
public class DefaultInterfaceConfigTest {

    private final MethodConfig mockMethodConfig1 = mock(MethodConfig.class);
    private final MethodConfig mockMethodConfig2 = mock(MethodConfig.class);
    private final Class clazz = TestInterface.class;
    private final Map<Method, MethodConfig> cache = new HashMap<Method, MethodConfig>(){{
        put(TestInterface.M1, mockMethodConfig1);
        put(TestInterface.M2, mockMethodConfig2);
    }};
    private final DefaultInterfaceConfig toTest = new DefaultInterfaceConfig(clazz, cache);

    @Test
    public void getInterfaceShouldReturnInterface(){
        assertSame(clazz, toTest.getInterface());
    }

    @Test
    public void getMethodConfigShouldReturnConfig(){
        assertSame(mockMethodConfig1, toTest.getMethodConfig(TestInterface.M1));
        assertSame(mockMethodConfig2, toTest.getMethodConfig(TestInterface.M2));
        assertNull(toTest.getMethodConfig(Classes.byName(Object.class, "toString")));
    }
}
