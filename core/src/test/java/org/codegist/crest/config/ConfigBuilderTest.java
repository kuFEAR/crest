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

package org.codegist.crest.config;

import org.codegist.crest.CRestConfig;
import org.codegist.crest.CRestException;
import org.codegist.crest.util.ComponentFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * @author laurent.gilles@codegist.org
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ComponentFactory.class, CRestException.class})
public class ConfigBuilderTest {

    private final CRestConfig mockCRestConfig = mock(CRestConfig.class);
    private final ConfigBuilder toTest = new ConfigBuilder(mockCRestConfig){};

    @Test
    public void getCRestConfigShouldReturnIt(){
        assertSame(mockCRestConfig, toTest.getCRestConfig());
    }

    @Test
    public void overrideShouldDelegetToCRestConfig(){
        toTest.override("a", "b");
        verify(mockCRestConfig).get("a", "b");
    }
    @Test
    public void instanciateShouldReturnNullIfGivenNull() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        assertNull(toTest.instantiate(null));
    }
    @Test
    public void instanciateShouldDelegateToComponentFactory() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        mockStatic(ComponentFactory.class);
        when(ComponentFactory.instantiate(String.class, mockCRestConfig)).thenReturn("a");
        String actual = toTest.instantiate(String.class);
        assertEquals("a", actual);
    }

    @Test
    public void instanciateShouldDelegateAnyExceptionToCRestHandle() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        RuntimeException expected = mock(RuntimeException.class);
        InvocationTargetException e = mock(InvocationTargetException.class);

        mockStatic(ComponentFactory.class);
        when(ComponentFactory.instantiate(String.class, mockCRestConfig)).thenThrow(e);
        
        mockStatic(CRestException.class);
        when(CRestException.handle((Exception)e)).thenReturn(expected);
        try {
            toTest.instantiate(String.class);
            fail();
        } catch (Exception e1) {
            assertSame(expected, e1);
        }
    }
}
