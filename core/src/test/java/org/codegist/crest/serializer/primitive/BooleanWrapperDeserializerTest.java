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

package org.codegist.crest.serializer.primitive;

import org.codegist.crest.CRestConfig;
import org.codegist.crest.serializer.BaseDeserializerTest;
import org.codegist.crest.serializer.TypeDeserializer;
import org.codegist.crest.test.util.CRestConfigs;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * @author laurent.gilles@codegist.org
 */
public class BooleanWrapperDeserializerTest extends BaseDeserializerTest {
    
    private final CRestConfig mockCRestConfig = CRestConfigs.mockDefaultBehavior();
    private final BooleanWrapperDeserializer toTest = new BooleanWrapperDeserializer(mockCRestConfig);

    @Test
    public void shouldDeserializeFalseToFalseUsingDefaultBooleanFormat() throws Exception {
        assertFalse(deserialize(toTest, "false"));
    }
    @Test
    public void shouldDeserializeTrueToTrueUsingDefaultBooleanFormat() throws Exception {
        assertTrue(deserialize(toTest, "true"));
    }
    @Test
    public void shouldDeserializeAnythingToFalse() throws Exception {
        assertFalse(deserialize(toTest, "dffsf"));
    }
    @Test
    public void shouldDeserializeNullToNull() throws Exception {
        assertNull(deserialize(toTest, null));
    }

    @Test
    public void shouldDeserializeFalseToFalseUsingCustomBooleanFormat() throws Exception {
        TypeDeserializer<Boolean> toTest = newToTest();
        assertFalse(deserialize(toTest, "false"));
    }
    @Test
    public void shouldDeserializeTrueToFalseUsingCustomBooleanFormat() throws Exception {
        TypeDeserializer<Boolean> toTest = newToTest();
        assertFalse(deserialize(toTest, "true"));
    }
    @Test
    public void shouldDeserializeCustomTrueToTrueUsingCustomBooleanFormat() throws Exception {
        TypeDeserializer<Boolean> toTest = newToTest();
        assertTrue(deserialize(toTest, "ffff"));
    }
    @Test
    public void shouldDeserializeAnythingToFalseWithCustomBooleanFormat() throws Exception {
        TypeDeserializer<Boolean> toTest = newToTest();
        assertFalse(deserialize(toTest, "dffsf"));
    }
    @Test
    public void shouldDeserializeNullToNullWithCustomBooleanFormat() throws Exception {
        TypeDeserializer<Boolean> toTest = newToTest();
        assertNull(deserialize(toTest, null));
    }
    
    private TypeDeserializer<Boolean> newToTest(){
        when(mockCRestConfig.getBooleanTrue()).thenReturn("ffff");
        return new BooleanWrapperDeserializer(mockCRestConfig);
    }
}
