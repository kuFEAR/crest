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
import org.codegist.crest.CRestException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
* @author laurent.gilles@codegist.org
*/
@RunWith(PowerMockRunner.class)
@PrepareForTest({ComponentRegistry.class, ComponentFactory.class})
public class RegistryTest {

    private static final CRestConfig mockCRestConfig = mock(CRestConfig.class);


    private final Map<String,Object> someConfig = new HashMap<String, Object>();
    private final ComponentRegistry.Builder<String,SomeClass> builder = new ComponentRegistry.Builder<String,SomeClass>()
            .register(SomeClass.class, "1")
            .register(SomeClass.class, new String[]{"2"}, someConfig);

    private ComponentRegistry<String,SomeClass> toTest(){
        return toTest(null);
    }
    private ComponentRegistry<String,SomeClass> toTest(Class<? extends SomeClass> defautlValue){
        return builder.defaultAs(defautlValue).build(mockCRestConfig);
    }

    @Test
    public void containsShouldReturnTrueIfKnown(){
        ComponentRegistry<String,SomeClass> toTest = toTest();
        assertTrue(toTest.contains("1"));
        assertTrue(toTest.contains("2"));
        assertFalse(toTest.contains("3"));
    }



    @Test
    public void getUnknownItemShouldReturnDefaultValueIfSet(){

        ComponentRegistry<String,SomeClass> toTest = toTest(SomeClass2.class);
        assertEquals(SomeClass2.class, toTest.get("3").getClass());
        assertSame(toTest.get("3"), toTest.get("3"));
    }

    @Test(expected = CRestException.class)
    public void getUnknownItemShouldFailIfNoDefaultValueIsSet(){
        ComponentRegistry<String,SomeClass> toTest = toTest();
        toTest.get("3");
    }

    @Test
    public void getAClassShouldBuildItFirstTimePassingMergedConfigToComponentFactoryAndReuseItAfterward() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ComponentRegistry<String,SomeClass> toTest = toTest();
        someConfig.put("some", "value");

        SomeClass someInstance2 = mock(SomeClass.class);
        CRestConfig mergedCRestConfig = mock(CRestConfig.class);
        when(mockCRestConfig.merge(someConfig)).thenReturn(mergedCRestConfig);

        mockStatic(ComponentFactory.class);
        when(ComponentFactory.instantiate(SomeClass.class, mergedCRestConfig)).thenReturn(someInstance2);

        SomeClass ref = toTest.get("2");
        SomeClass ref2 = toTest.get("2");
        assertSame(someInstance2, ref);
        assertSame(ref, ref2);
    }

    @Test
    public void getAClassShouldBuildItFirstTimePassingNonMergedConfigToComponentFactoryAndReuseItAfterward() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ComponentRegistry<String,SomeClass> toTest = toTest();
        SomeClass someInstance2 = mock(SomeClass.class);
        mockStatic(ComponentFactory.class);
        when(ComponentFactory.instantiate(SomeClass.class, mockCRestConfig)).thenReturn(someInstance2);

        SomeClass ref = toTest.get("2");
        SomeClass ref2 = toTest.get("2");
        assertSame(someInstance2, ref);
        assertSame(ref, ref2);
    }

    @Test(expected = CRestException.class)
    public void getAClassShouldThrowCRestExceptionIfBuildingItFails() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ComponentRegistry<String,SomeClass> toTest = toTest();
        mockStatic(ComponentFactory.class);
        when(ComponentFactory.instantiate(SomeClass.class, mockCRestConfig)).thenThrow(new NoSuchMethodException());
        toTest.get("2");
    }


    private static class SomeClass {}
    private static class SomeClass2 extends SomeClass {}
}
