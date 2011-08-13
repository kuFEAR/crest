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

import org.codegist.crest.CRestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * @author laurent.gilles@codegist.org
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Registry.Builder.class, Registry.class, Registry.ItemDescriptor.class})
public class RegistryBuilderTest {
    private final CRestConfig mockCRestConfig = mock(CRestConfig.class);
    private final Registry mockRegistry = mock(Registry.class);
    private final Registry.Builder<String, Number> toTest = new Registry.Builder<String, Number>();

    @Test
    public void buildShouldCreateEmptyRegistryWithNullDefaultValue() throws Exception {
        whenNew(Registry.class).withArguments(new HashMap(), mockCRestConfig, null).thenReturn(mockRegistry);
        Registry actual = toTest.build(mockCRestConfig);
        assertSame(mockRegistry, actual);
    }

    @Test
    public void registerClassWithMultipleKeysAndConfigShouldCreateARegistryWithItemDescriptorsForEachKeys() throws Exception {
        HashMap<String,Object> config = new HashMap<String,Object>();
        Registry.ItemDescriptor mockItemDescriptor = mock(Registry.ItemDescriptor.class);
        ArgumentCaptor<Map> map = ArgumentCaptor.forClass(Map.class);
        whenNew(Registry.class).withArguments(map.capture(), eq(mockCRestConfig), isNull()).thenReturn(mockRegistry);
        whenNew(Registry.ItemDescriptor.class).withArguments(Long.class, config).thenReturn(mockItemDescriptor);

        Registry actual = toTest.register(Long.class, new String[]{"1", "2"}, config).build(mockCRestConfig);
        assertSame(mockRegistry, actual);
        assertEquals(2, map.getValue().size());
        assertSame(mockItemDescriptor, map.getValue().get("1"));
        assertSame(mockItemDescriptor, map.getValue().get("2"));
    }

    @Test
    public void registerClassWithMultipleKeysShouldCreateARegistryWithItemDescriptorsForEachKeys() throws Exception {
        Registry.ItemDescriptor mockItemDescriptor = mock(Registry.ItemDescriptor.class);
        ArgumentCaptor<Map> map = ArgumentCaptor.forClass(Map.class);
        whenNew(Registry.class).withArguments(map.capture(), eq(mockCRestConfig), isNull()).thenReturn(mockRegistry);
        whenNew(Registry.ItemDescriptor.class).withArguments(Long.class, Collections.<String, Object>emptyMap()).thenReturn(mockItemDescriptor);

        Registry actual = toTest.register(Long.class, "1", "2").build(mockCRestConfig);
        assertSame(mockRegistry, actual);
        assertEquals(2, map.getValue().size());
        assertSame(mockItemDescriptor, map.getValue().get("1"));
        assertSame(mockItemDescriptor, map.getValue().get("2"));
    }

    @Test
    public void registerReferenceWithMultipleKeysShouldCreateARegistryWithItemDescriptorsForEachKeys() throws Exception {
        ArgumentCaptor<Map> map = ArgumentCaptor.forClass(Map.class);
        whenNew(Registry.class).withArguments(map.capture(), eq(mockCRestConfig), isNull()).thenReturn(mockRegistry);

        Registry actual = toTest.register(10l, "1", "2").build(mockCRestConfig);
        assertSame(mockRegistry, actual);
        assertEquals(2, map.getValue().size());
        assertSame(10l, map.getValue().get("1"));
        assertSame(10l, map.getValue().get("2"));
    }

    @Test
    public void registerClassMappingShouldCopyOverGivenMapping() throws Exception {
        Map<String, Class<? extends Number>> mapping = new HashMap<String, Class<? extends Number>>();
        mapping.put("1", Long.class);
        mapping.put("2", Integer.class);

        Registry.ItemDescriptor mockItemDescriptor1 = mock(Registry.ItemDescriptor.class);
        Registry.ItemDescriptor mockItemDescriptor2 = mock(Registry.ItemDescriptor.class);
        ArgumentCaptor<Map> map = ArgumentCaptor.forClass(Map.class);
        whenNew(Registry.class).withArguments(map.capture(), eq(mockCRestConfig), isNull()).thenReturn(mockRegistry);
        whenNew(Registry.ItemDescriptor.class).withArguments(Long.class, Collections.<String, Object>emptyMap()).thenReturn(mockItemDescriptor1);
        whenNew(Registry.ItemDescriptor.class).withArguments(Integer.class, Collections.<String, Object>emptyMap()).thenReturn(mockItemDescriptor2);

        Registry actual = toTest.register(mapping).build(mockCRestConfig);
        assertSame(mockRegistry, actual);
        assertEquals(2, map.getValue().size());
        assertSame(mockItemDescriptor1, map.getValue().get("1"));
        assertSame(mockItemDescriptor2, map.getValue().get("2"));
    }
}
