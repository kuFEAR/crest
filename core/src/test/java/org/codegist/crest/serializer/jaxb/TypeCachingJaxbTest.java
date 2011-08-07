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

package org.codegist.crest.serializer.jaxb;

import org.codegist.common.reflect.Types;
import org.codegist.crest.CRestConfig;
import org.codegist.crest.util.CRestConfigs;
import org.codegist.crest.util.Values;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.OutputStream;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.codegist.common.collect.Collections.asSet;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * @author laurent.gilles@codegist.org
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(JaxbFactory.class)
public class TypeCachingJaxbTest {

    private final CRestConfig mockCRestConfig = CRestConfigs.mockDefaultBehavior();
    private final TypeCachingJaxb toTest = new TypeCachingJaxb(mockCRestConfig, getClass());



    @Test
    public void shouldCreateJaxbInstanceForFirstCallFromClassAndTypeAndReuseItForUnmarshallCallWithSameObjectType() throws Exception {
        Type type1 = Types.newType(List.class, Types.newType(Set.class, int[].class)); // List<Set<int[]>>
        Class clazz1 = List.class;
        Type type2 = String.class;
        Class clazz2 = String.class;
        Reader reader = mock(Reader.class);
        Jaxb mockJaxb1 = mock(Jaxb.class);
        Jaxb mockJaxb2 = mock(Jaxb.class);

        Class[] expected = asSet(List.class, Set.class, int[].class).toArray(new Class[3]);
        mockStatic(JaxbFactory.class);
        when(JaxbFactory.create(mockCRestConfig, getClass(), expected)).thenReturn(mockJaxb1);
        when(JaxbFactory.create(mockCRestConfig, getClass(), new Class<?>[]{String.class})).thenReturn(mockJaxb2);

        toTest.unmarshal(clazz1, type1, reader);
        toTest.unmarshal(clazz1, type1, reader);
        toTest.unmarshal(clazz2, type2, reader);
        toTest.unmarshal(clazz2, type2, reader);

        verify(mockJaxb1, times(2)).unmarshal(clazz1, type1, reader);
        verify(mockJaxb2, times(2)).unmarshal(clazz2, type2, reader);
    }

    @Test
    public void shouldCreateJaxbInstanceForFirstCallFromSimpleObjectAndReuseItForMarshallCallWithSameObjectType() throws Exception {
        Object toMarshal = new Object();
        Object toMarshal2 = new Object();
        String toMarshal3 = new String();
        OutputStream outputStream = mock(OutputStream.class);
        Charset charset = Values.UTF8;
        Jaxb mockJaxb = mock(Jaxb.class);
        Jaxb mockJaxb2 = mock(Jaxb.class);

        mockStatic(JaxbFactory.class);
        when(JaxbFactory.create(mockCRestConfig, getClass(), new Class<?>[]{Object.class})).thenReturn(mockJaxb);
        when(JaxbFactory.create(mockCRestConfig, getClass(), new Class<?>[]{String.class})).thenReturn(mockJaxb2);

        toTest.marshal(toMarshal,outputStream,charset);
        toTest.marshal(toMarshal2,outputStream,charset);
        toTest.marshal(toMarshal3,outputStream,charset);

        verify(mockJaxb).marshal(toMarshal,outputStream,charset);
        verify(mockJaxb).marshal(toMarshal2,outputStream,charset);
        verify(mockJaxb2).marshal(toMarshal3,outputStream,charset);
    }

    @Test
    public void shouldCreateJaxbInstanceForFirstCallFromInstanceOfClassesAndReuseItForMarshallCallWithSameObjectType() throws Exception {
        Object toMarshal1 = new Object();
        Object toMarshal12 = new Object();
        MyObject toMarshal2 = new MyObject();
        MyObject toMarshal22 = new MyObject();
        OutputStream outputStream = mock(OutputStream.class);
        Charset charset = Values.UTF8;
        final Jaxb mockJaxb2 = mock(Jaxb.class);
        Jaxb mockJaxb1 = mock(Jaxb.class);
        Class[] expected = MyObject.withItSelf();

        mockStatic(JaxbFactory.class);
        when(JaxbFactory.create(mockCRestConfig, getClass(), new Class<?>[]{Object.class})).thenReturn(mockJaxb1);
        when(JaxbFactory.create(mockCRestConfig, getClass(),expected)).thenReturn(mockJaxb2);


        toTest.marshal(toMarshal1,outputStream,charset);
        toTest.marshal(toMarshal2,outputStream,charset);
        toTest.marshal(toMarshal12,outputStream,charset);
        toTest.marshal(toMarshal22,outputStream,charset);

        verify(mockJaxb1).marshal(toMarshal1,outputStream,charset);
        verify(mockJaxb2).marshal(toMarshal2,outputStream,charset);
        verify(mockJaxb1).marshal(toMarshal12,outputStream,charset);
        verify(mockJaxb2).marshal(toMarshal22,outputStream,charset);
    }

    public static class MyObject implements Classes {
        public Set<Class<?>> getClasses() {
            return classes();
        }
        public static Set<Class<?>> classes() {
            return asSet(String.class, Integer.class, int[].class, List.class);
        }
        public static Class[] withItSelf() {
            Set<Class<?>>  set = new HashSet<Class<?>>(classes());
            set.add(MyObject.class);
            return set.toArray(new Class[set.size()]);
        }
    }




}
