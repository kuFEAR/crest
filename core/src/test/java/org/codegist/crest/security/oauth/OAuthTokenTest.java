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

package org.codegist.crest.security.oauth;

import org.codegist.crest.param.EncodedPair;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class OAuthTokenTest {

    private static final String TOKEN = "TOKEN";
    private static final String SECRET = "SECRET";
    private final Map<String,String> attributes = new LinkedHashMap<String, String>(){{
        put("attr1", "val%201");
        put("attr2", "val%202");
    }};
    private final OAuthToken toTest = new OAuthToken(TOKEN, SECRET, attributes);

    @Test(expected=UnsupportedOperationException.class)
    public void shouldBeImmutable() {
        //should that also be the case, or left to the responsability of the caller?
        //attributes.put("attr3", "attr3");
        //assertNull(toTest.getAttribute("attr3"));
        
        toTest.getAttributes().put("a", "b");
    }

    @Test
    public void shouldReturnNullPairIfAttributeNotFound() {   
        assertNull(toTest.getAttribute("attr3"));
    }

    @Test
    public void shouldReturnPairIfAttributeFound() {
        EncodedPair actual = toTest.getAttribute("attr2");
        assertNotNull(actual);
        assertEquals("attr2", actual.getName());
        assertEquals("val%202", actual.getValue());
    }

    @Test
    public void shouldReturnAInformativeToString() {
        String actual = toTest.toString();
        assertNotNull(actual);
        assertEquals("OAuthToken[token=TOKEN,secret=SECRET,attributes={attr1=val%201, attr2=val%202}]", actual);
    }
}
