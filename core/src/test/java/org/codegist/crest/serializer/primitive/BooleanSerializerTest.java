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
import org.codegist.crest.serializer.Serializer;
import org.codegist.crest.test.util.CRestConfigs;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * @author laurent.gilles@codegist.org
 */
public class BooleanSerializerTest {

    private static final Charset charset = Charset.defaultCharset();
    private final CRestConfig mockCRestConfig = CRestConfigs.mockDefaultBehavior();
    
    private final Serializer<Boolean> toTest = new BooleanSerializer(mockCRestConfig);

    @Test
    public void shouldSerializeFalseToFalseUsingDefaultBooleanFormat() throws Exception {
        OutputStream out = new ByteArrayOutputStream();
        toTest.serialize(false, charset, out);
        assertEquals("false", out.toString());
    }
    @Test
    public void shouldSerializeTrueToTrueUsingDefaultBooleanFormat() throws Exception {
        OutputStream out = new ByteArrayOutputStream();
        toTest.serialize(true, charset, out);
        assertEquals("true", out.toString());
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

    private Serializer<Boolean> newToTest(){
        when(mockCRestConfig.getBooleanTrue()).thenReturn("1");
        when(mockCRestConfig.getBooleanFalse()).thenReturn("0");
        return new BooleanSerializer(mockCRestConfig);
    }
}
