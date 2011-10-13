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

package org.codegist.crest.serializer.simplexml;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class BooleanMatcherTest {

    private final BooleanMatcher toTest = new BooleanMatcher("myTrue", "myFalse");

    @Test
    public void shouldReadTrue(){
        assertTrue(toTest.read("myTrue"));
    }
    @Test
    public void shouldReadFalse(){
        assertFalse(toTest.read("myFalse"));
    }
    @Test
    public void shouldReadAnyAsFalse(){
        assertFalse(toTest.read("any"));
    }
    @Test
    public void shouldReadNullAsFalse(){
        assertFalse(toTest.read(null));
    }
    @Test
    public void shouldWriteTrueAsMyTrue(){
        assertEquals("myTrue", toTest.write(true));
    }
    @Test
    public void shouldWriteFalseAsMyFalse(){
        assertEquals("myFalse", toTest.write(false));
    }
    @Test
    public void shouldWriteNullAsMyFalse(){
        assertEquals("myFalse", toTest.write(null));
    }
}
