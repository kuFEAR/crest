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
import org.codegist.crest.test.util.Classes;
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
@PrepareForTest({ComponentRegistry.Builder.class, ComponentRegistry.class, ComponentRegistry.ItemDescriptor.class})
public class RegistryBuilderTest {
    private final CRestConfig mockCRestConfig = mock(CRestConfig.class);
    private final ComponentRegistry mockRegistry = mock(ComponentRegistry.class);
    private final ComponentRegistry.Builder<String, Component> toTest = new ComponentRegistry.Builder<String, Component>();

    @Test
    public void buildShouldCreateEmptyRegistryWithNullDefaultValue() throws Exception {
        whenNew(ComponentRegistry.class).withArguments(new HashMap(), mockCRestConfig, null).thenReturn(mockRegistry);
        ComponentRegistry actual = toTest.build(mockCRestConfig);
        assertSame(mockRegistry, actual);
    }

    @Test
    public void registerClassWithMultipleKeysAndConfigShouldCreateARegistryWithItemDescriptorsForEachKeys() throws Exception {
        HashMap<String,Object> config = new HashMap<String,Object>();
        ComponentRegistry.ItemDescriptor mockItemDescriptor = mock(ComponentRegistry.ItemDescriptor.class);
        ArgumentCaptor<Map> map = ArgumentCaptor.forClass(Map.class);
        whenNew(ComponentRegistry.class).withArguments(map.capture(), eq(mockCRestConfig), isNull()).thenReturn(mockRegistry);
        whenNew(ComponentRegistry.ItemDescriptor.class).withArguments(Component.class, config).thenReturn(mockItemDescriptor);

        ComponentRegistry actual = toTest.register(Component.class, new String[]{"1", "2"}, config).build(mockCRestConfig);
        assertSame(mockRegistry, actual);
        assertEquals(2, map.getValue().size());
        assertSame(mockItemDescriptor, map.getValue().get("1"));
        assertSame(mockItemDescriptor, map.getValue().get("2"));
    }

    @Test
    public void registerClassWithMultipleKeysShouldCreateARegistryWithItemDescriptorsForEachKeys() throws Exception {
        ComponentRegistry.ItemDescriptor mockItemDescriptor = mock(ComponentRegistry.ItemDescriptor.class);
        ArgumentCaptor<Map> map = ArgumentCaptor.forClass(Map.class);
        whenNew(ComponentRegistry.class).withArguments(map.capture(), eq(mockCRestConfig), isNull()).thenReturn(mockRegistry);
        whenNew(ComponentRegistry.ItemDescriptor.class).withArguments(Component.class, Collections.<String, Object>emptyMap()).thenReturn(mockItemDescriptor);

        ComponentRegistry actual = toTest.register(Component.class, "1", "2").build(mockCRestConfig);
        assertSame(mockRegistry, actual);
        assertEquals(2, map.getValue().size());
        assertSame(mockItemDescriptor, map.getValue().get("1"));
        assertSame(mockItemDescriptor, map.getValue().get("2"));
    }

    @Test
    public void registerReferenceWithMultipleKeysShouldCreateARegistryWithItemDescriptorsForEachKeys() throws Exception {
        ArgumentCaptor<Map<String, ComponentRegistry.ItemDescriptor<Component>>> map = ArgumentCaptor.<Map<String, ComponentRegistry.ItemDescriptor<Component>>>forClass((Class)Map.class);
        whenNew(ComponentRegistry.class).withArguments(map.capture(), eq(mockCRestConfig), isNull()).thenReturn(mockRegistry);

        ComponentRegistry actual = toTest.register(Component.class, "1", "2").build(mockCRestConfig);
        assertSame(mockRegistry, actual);
        assertEquals(2, map.getValue().size());
        assertEquals(Component.class, Classes.<String>getFieldValue(map.getValue().get("1"), "clazz"));
        assertEquals(Component.class, Classes.<String>getFieldValue(map.getValue().get("2"), "clazz"));
    }

    @Test
    public void registerClassMappingShouldCopyOverGivenMapping() throws Exception {
        Map<String, Class<? extends Component>> mapping = new HashMap<String, Class<? extends Component>>();
        mapping.put("1", Component.class);
        mapping.put("2", Component2.class);

        ComponentRegistry.ItemDescriptor mockItemDescriptor1 = mock(ComponentRegistry.ItemDescriptor.class);
        ComponentRegistry.ItemDescriptor mockItemDescriptor2 = mock(ComponentRegistry.ItemDescriptor.class);
        ArgumentCaptor<Map> map = ArgumentCaptor.forClass(Map.class);
        whenNew(ComponentRegistry.class).withArguments(map.capture(), eq(mockCRestConfig), isNull()).thenReturn(mockRegistry);
        whenNew(ComponentRegistry.ItemDescriptor.class).withArguments(Component.class, Collections.<String, Object>emptyMap()).thenReturn(mockItemDescriptor1);
        whenNew(ComponentRegistry.ItemDescriptor.class).withArguments(Component2.class, Collections.<String, Object>emptyMap()).thenReturn(mockItemDescriptor2);

        ComponentRegistry actual = toTest.register(mapping).build(mockCRestConfig);
        assertSame(mockRegistry, actual);
        assertEquals(2, map.getValue().size());
        assertSame(mockItemDescriptor1, map.getValue().get("1"));
        assertSame(mockItemDescriptor2, map.getValue().get("2"));
    }

    static class Component {

    }

    static class Component2 extends Component {

    }
}
