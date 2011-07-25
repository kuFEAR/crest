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

package org.codegist.crest.serializer.primitive;

import org.codegist.crest.CRestProperty;
import org.codegist.crest.serializer.BaseDeserializerTest;
import org.codegist.crest.serializer.TypeDeserializer;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.codegist.crest.CRestProperty.CREST_BOOLEAN_FALSE_DEFAULT;
import static org.codegist.crest.CRestProperty.CREST_BOOLEAN_TRUE_DEFAULT;
import static org.junit.Assert.*;

/**
 * @author laurent.gilles@codegist.org
 */
public class BooleanWrapperDeserializerTest extends BaseDeserializerTest {
    
    private final BooleanWrapperDeserializer toTest = new BooleanWrapperDeserializer(new HashMap<String, Object>());

    @Test
    public void shouldDeserializeFalseToFalseUsingDefaultBooleanFormat() throws Exception {
        assertFalse(deserialize(toTest, CREST_BOOLEAN_FALSE_DEFAULT));
    }
    @Test
    public void shouldDeserializeTrueToTrueUsingDefaultBooleanFormat() throws Exception {
        assertTrue(deserialize(toTest, CREST_BOOLEAN_TRUE_DEFAULT));
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
        assertFalse(deserialize(toTest, CREST_BOOLEAN_FALSE_DEFAULT));
    }
    @Test
    public void shouldDeserializeTrueToFalseUsingCustomBooleanFormat() throws Exception {
        TypeDeserializer<Boolean> toTest = newToTest();
        assertFalse(deserialize(toTest, CREST_BOOLEAN_TRUE_DEFAULT));
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
    
    private static TypeDeserializer<Boolean> newToTest(){
        Map<String, Object> crestProperties = new HashMap<String, Object>();
        crestProperties.put(CRestProperty.CREST_BOOLEAN_TRUE, "ffff");
        return new BooleanWrapperDeserializer(crestProperties);
    }
}
