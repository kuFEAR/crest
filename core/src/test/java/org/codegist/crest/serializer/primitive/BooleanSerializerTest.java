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
import org.codegist.crest.serializer.Serializer;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import static org.codegist.crest.CRestProperty.CREST_BOOLEAN_FALSE_DEFAULT;
import static org.codegist.crest.CRestProperty.CREST_BOOLEAN_TRUE_DEFAULT;
import static org.junit.Assert.assertEquals;

/**
 * @author laurent.gilles@codegist.org
 */
public class BooleanSerializerTest {

    private static final Charset charset = Charset.defaultCharset();
    private final Serializer<Boolean> toTest = new BooleanSerializer(new HashMap<String, Object>());

    @Test
    public void shouldSerializeFalseToFalseUsingDefaultBooleanFormat() throws Exception {
        OutputStream out = new ByteArrayOutputStream();
        toTest.serialize(false, charset, out);
        assertEquals(CREST_BOOLEAN_FALSE_DEFAULT, out.toString());
    }
    @Test
    public void shouldSerializeTrueToTrueUsingDefaultBooleanFormat() throws Exception {
        OutputStream out = new ByteArrayOutputStream();
        toTest.serialize(true, charset, out);
        assertEquals(CREST_BOOLEAN_TRUE_DEFAULT, out.toString());
    }
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerWhenSerializingNull() throws Exception {
        toTest.serialize(null, charset, new ByteArrayOutputStream());
    }

    @Test
    public void shouldSerializeFalseToCustomFalseUsingCustomBooleanFormat() throws Exception {
        Serializer toTest = newToTest();
        OutputStream out = new ByteArrayOutputStream();
        toTest.serialize(false, charset, out);
        assertEquals("0", out.toString());
    }
    @Test
    public void shouldSerializeTrueToCustomTrueUsingCustomBooleanFormat() throws Exception {
        Serializer toTest = newToTest();
        OutputStream out = new ByteArrayOutputStream();
        toTest.serialize(true, charset, out);
        assertEquals("1", out.toString());
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerWhenSerializingNullUsingCustomBooleanFormat() throws Exception {
        newToTest().serialize(null, charset, new ByteArrayOutputStream());
    }

    private static Serializer<Boolean> newToTest(){
        Map<String, Object> crestProperties = new HashMap<String, Object>();
        crestProperties.put(CRestProperty.CREST_BOOLEAN_TRUE, "1");
        crestProperties.put(CRestProperty.CREST_BOOLEAN_FALSE, "0");
        return new BooleanSerializer(crestProperties);
    }
}
