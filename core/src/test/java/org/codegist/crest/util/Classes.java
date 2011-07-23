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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.junit.Assert.*;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class Classes {

    public static Method byName(Class<?> klass, String name){
        for(Method m : klass.getDeclaredMethods()){
            if(m.getName().equals(name)) {
                return m;
            }
        }
        throw new IllegalArgumentException("Method " + name + " not found in " + klass);
    }

    public static void assertNotInstanciable(Class<?> klass) throws NoSuchMethodException {

        assertEquals(1, klass.getDeclaredConstructors().length);
        Constructor defaultConstructor = klass.getDeclaredConstructor();
        assertTrue((defaultConstructor.getModifiers() | Modifier.PRIVATE) == Modifier.PRIVATE);

        // make it accessible
        defaultConstructor.setAccessible(true);

        // try to instanciate
        try {
            defaultConstructor.newInstance();
            fail("should have failed!");
        } catch (InvocationTargetException e) {
            assertTrue(e.getCause() instanceof IllegalStateException);
        } catch (Exception e) {
            fail("unexpected exception " + e);
        }

    }

}
