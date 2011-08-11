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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author laurent.gilles@codegist.org
 */
public class PlaceholdersTest extends NonInstanciableClassTest {
    public PlaceholdersTest() {
        super(Placeholders.class);
    }

    @Test
    public void compileShouldReturnACompiledVersionOfGivenPlaceholdeMap(){
        Map<String,String> placeholders = Collections.singletonMap("te.st", "someval");
        Map<Pattern, String> actual = Placeholders.compile(placeholders);
        assertNotNull(actual);
        assertEquals(1, actual.size());
        Map.Entry<Pattern,String> entry = actual.entrySet().iterator().next();
        assertEquals("someval", entry.getValue());
        assertEquals("\\{\\Qte.st\\E\\}", entry.getKey().pattern());
    }

    @Test
    public void mergeShouldMergePlaceholderInStringWithValues(){
        Map<String,String> placeholders = new HashMap<String, String>();
        placeholders.put("te.st", "someval");
        placeholders.put("p1", "someval2");
        Map<Pattern, String> compiled = Placeholders.compile(placeholders);

        String actual = Placeholders.merge(compiled, "some{te.st}string{p1}with{p1}placeholders{p2}-{tesst}");
        assertEquals("somesomevalstringsomeval2withsomeval2placeholders{p2}-{tesst}", actual);
    }


    @Test
    public void mergeShouldReturnSameIfEmpty(){
        assertEquals("", Placeholders.merge(null, ""));
    }

}
