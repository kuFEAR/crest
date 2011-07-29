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

package org.codegist.crest.serializer.simplexml;

import org.junit.Before;
import org.junit.Test;
import org.simpleframework.xml.transform.Transform;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class MatcherRegistryTest {

    private final Transform DATE_TX = mock(Transform.class);
    private final Transform BOOLEAN_TX = mock(Transform.class);
    private final Map<Class, Transform> map = new HashMap<Class, Transform>();
    private final MatcherRegistry toTest = new MatcherRegistry(map);

    @Before
    public void setupMap(){
        map.put(Date.class, DATE_TX);
        map.put(Boolean.class, BOOLEAN_TX);
        map.put(boolean.class, BOOLEAN_TX);
    }

    @Test
    public void shouldMatchMappedTx() throws Exception {
        assertEquals(DATE_TX, toTest.match(Date.class));
        assertEquals(BOOLEAN_TX, toTest.match(boolean.class));
        assertEquals(BOOLEAN_TX, toTest.match(Boolean.class));
    }
    @Test
    public void shouldntMatchUnmappedTx() throws Exception {
        assertNull(toTest.match(String.class));
    }
}
