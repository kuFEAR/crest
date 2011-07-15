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
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Map;

/**
 * @author laurent.gilles@codegist.org
 */
public final class ComponentFactory {

    private ComponentFactory(){
        throw new IllegalStateException();
    }

    public static <T> T instantiate(Class<T> clazz, Map<String,Object> crestProperties) throws InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {
        try {
            return accessible(clazz.getDeclaredConstructor(Map.class)).newInstance(crestProperties);
        } catch (NoSuchMethodException e) {
            return accessible(clazz.getDeclaredConstructor()).newInstance();
        }
    }

    private static <T> Constructor<? extends T> accessible(final Constructor<? extends T> constructor){
        if(!isPublic(constructor.getModifiers()) || !isPublic(constructor.getDeclaringClass().getModifiers())) {
             AccessController.doPrivileged(new MakeAccessible(constructor));
        }
        return constructor;
    }

    private static boolean isPublic(int modifiers){
        return (modifiers & 0x00000001) != 0;
    }


    private static final class MakeAccessible implements PrivilegedAction<Constructor> {
        private final Constructor constructor;

        private MakeAccessible(Constructor constructor) {
            this.constructor = constructor;
        }

        public Constructor run() {
            constructor.setAccessible(true);
            return constructor;
        }
    }
}
