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

import org.codegist.crest.NonInstanciableClassTest;
import org.junit.Test;

import java.util.ArrayList;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author laurent.gilles@codegist.org
 */
public class ParamsTest extends NonInstanciableClassTest {
    public ParamsTest() {
        super(Params.class);
    }

    @Test
    public void shouldReturnTrueIfObjectIsNull(){
        assertTrue(Params.isNull(null));
    }

    @Test
    public void shouldReturnTrueIfCollectionIsEmpty(){
        assertTrue(Params.isNull(new ArrayList()));
    }

    @Test
    public void shouldReturnTrueIfCollectionContainsOnlyNulls(){
        assertTrue(Params.isNull(asList(null,null)));
    }

    @Test
    public void shouldReturnFalseIfObjectIsNotNull(){
        assertFalse(Params.isNull(""));
    }

    @Test
    public void shouldReturnFalseIfCollectionContainsNonNulls(){
        assertFalse(Params.isNull(asList(null,"",null)));
    }
}
