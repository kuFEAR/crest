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

package org.codegist.crest.util;

import org.codegist.crest.CRestConfig;
import org.codegist.crest.NonInstanciableClassTest;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

/**
 * @author laurent.gilles@codegist.org
 */
public class ComponentFactoryTest extends NonInstanciableClassTest {

    private final CRestConfig mockCRestConfig = mock(CRestConfig.class);

    public ComponentFactoryTest() {
        super(ComponentFactory.class);
    }

    @Test
    public void shouldInstantiateAccessibleWithCRestConfig() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        AccessiblePublicClassWithCRestConfig actual = ComponentFactory.instantiate(AccessiblePublicClassWithCRestConfig.class, mockCRestConfig);
        assertNotNull(actual);
        assertSame(mockCRestConfig, actual.crestConfig);
    }

    @Test
    public void shouldInstantiateAccessibleWithoutCRestConfig() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        AccessiblePublicClassNoCRestConfig actual = ComponentFactory.instantiate(AccessiblePublicClassNoCRestConfig.class, mockCRestConfig);
        assertNotNull(actual);
    }

    @Test(expected=NoSuchMethodException.class)
    public void shouldFailInstantiateClassWithUnknownConstructors() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ComponentFactory.instantiate(AccessiblePublicClassWithUninstantiable.class, mockCRestConfig);
    }

    @Test
    public void shouldInstantiateNotAccessibleWithCRestConfig() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        NotAccessiblePublicClassWithCRestConfig actual = ComponentFactory.instantiate(NotAccessiblePublicClassWithCRestConfig.class, mockCRestConfig);
        assertNotNull(actual);
        assertSame(mockCRestConfig, actual.crestConfig);
    }

    @Test
    public void shouldInstantiateNotAccessibleWithoutCRestConfig() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        NotAccessiblePublicClassNoCRestConfig actual = ComponentFactory.instantiate(NotAccessiblePublicClassNoCRestConfig.class, mockCRestConfig);
        assertNotNull(actual);
    }

    @Test(expected=NoSuchMethodException.class)
    public void shouldFailInstantiateNotAccessibleClassWithUnknownConstructors() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ComponentFactory.instantiate(NotAccessiblePublicClassWithUninstantiable.class, mockCRestConfig);
    }



    public static class AccessiblePublicClassWithCRestConfig {
        final CRestConfig crestConfig;
        public AccessiblePublicClassWithCRestConfig(CRestConfig crestConfig){
            this.crestConfig = crestConfig;
        }
    }

    public static class AccessiblePublicClassNoCRestConfig {
        public AccessiblePublicClassNoCRestConfig(){
        }
    }

    public static class AccessiblePublicClassWithUninstantiable {
        public AccessiblePublicClassWithUninstantiable(String s){
        }
    }



    private static class NotAccessiblePublicClassWithCRestConfig {
        final CRestConfig crestConfig;
        private NotAccessiblePublicClassWithCRestConfig(CRestConfig crestConfig){
            this.crestConfig = crestConfig;
        }
    }

    private static class NotAccessiblePublicClassNoCRestConfig {
        private NotAccessiblePublicClassNoCRestConfig(){
        }
    }

    private static class NotAccessiblePublicClassWithUninstantiable {
        private NotAccessiblePublicClassWithUninstantiable(String s){
        }
    }

}
