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

package org.codegist.crest.deserialization.common;

import org.codegist.common.io.IOs;
import org.codegist.crest.BaseCRestTest;
import org.codegist.crest.CRestBuilder;
import org.codegist.crest.util.CommaSeparatedIntDeserializer;
import org.junit.Test;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Collection;

import static java.lang.String.valueOf;
import static org.junit.Assert.*;

/**
 * @author laurent.gilles@codegist.org
 */
public class CommonDeserializationsTest<T extends IDeserializations> extends BaseCRestTest<T> {

    public CommonDeserializationsTest(CRestHolder holder, Class<T> service) {
        super(holder, service);
    }

    @Parameterized.Parameters
    public static Collection<CRestHolder[]> getData() {
        return crest(arrify(forEachBaseBuilder(new Builder() {
            public CRestHolder build(CRestBuilder builder) {
                return new CRestHolder(builder
                        .bind(new CommaSeparatedIntDeserializer(), int[].class)
                        .build());
            }
        })));
    }

    private static final String DATA = "some \n da £ ta";

    @Test
    public void testReader() throws IOException {
        Reader actual = toTest.reader(DATA);
        String val = IOs.toString(actual, true);
        assertEquals(DATA, val);
    }

    @Test
    public void testInputStream() throws IOException {
        InputStream actual = toTest.inputStream(DATA);
        String val = IOs.toString(actual, true);
        assertEquals(DATA, val);
    }

    @Test
    public void testGetInts() throws IOException {
        int[] data = {-56,0,10,22,34};
        int[] actual = toTest.getInts(data);
        assertArrayEquals(data, actual);
    }

    @Test
    public void testNothing() throws IOException {
        String data = "data-nothing";
        toTest.nothing(data);
        assertEquals(data, toTest.get());
    }

    @Test
    public void testNothing2() throws IOException {
        String data = "data-nothing2";
        toTest.nothing2(data);
        assertEquals(data, toTest.get());
    }



    @Test
    public void testGetByte() throws IOException {
        byte value = Byte.MIN_VALUE;
        assertEquals(value, toTest.getByte(valueOf(value)));
    }
    @Test
    public void testGetBytes() throws IOException {
        String value = "hello";
        assertArrayEquals(value.getBytes(), toTest.getBytes(value));
    }
    @Test
    public void testGetShort() throws IOException {
        short value = Short.MIN_VALUE;
        assertEquals(value, toTest.getShort(valueOf(value)));
    }
    @Test
    public void testGetInt() throws IOException {
        int value = Integer.MIN_VALUE;
        assertEquals(value, toTest.getInt(valueOf(value)));
    }
    @Test
    public void testGetLong() throws IOException {
        long value = Long.MIN_VALUE;
        assertEquals(value, toTest.getLong(valueOf(value)));
    }
    @Test
    public void testGetDouble() throws IOException {
        double value = Double.MIN_VALUE;
        assertEquals(valueOf(value), valueOf(toTest.getDouble(valueOf(value))));
    }
    @Test
    public void testGetFloat() throws IOException {
        float value = Float.MIN_VALUE;
        assertEquals(valueOf(value), valueOf(toTest.getFloat(valueOf(value))));
    }
    @Test
    public void testGetBoolean() throws IOException {
        String value = getEffectiveBooleanTrue();
        assertEquals(true, toTest.getBoolean(value));
    }
    @Test
    public void testGetChar() throws IOException {
        char value = Character.MAX_VALUE;
        assertEquals(value, toTest.getChar(valueOf(value)));
    }





    @Test
    public void testGetByteWithNull() throws IOException {
        assertEquals((byte)0, toTest.getByte(null));
    }
    @Test
    public void testGetShortWithNull() throws IOException {
        assertEquals((short)0, toTest.getShort(null));
    }
    @Test
    public void testGetIntWithNull() throws IOException {
        assertEquals(0, toTest.getInt(null));
    }
    @Test
    public void testGetLongWithNull() throws IOException {
        assertEquals(0l, toTest.getLong(null));
    }
    @Test
    public void testGetDoubleWithNull() throws IOException {
        assertEquals(valueOf(0.0d), valueOf(toTest.getDouble(null)));
    }
    @Test
    public void testGetFloatWithNull() throws IOException {
        assertEquals(valueOf(0.0f), valueOf(toTest.getFloat(null)));
    }
    @Test
    public void testGetBooleanWithNull() throws IOException {
        assertEquals(false, toTest.getBoolean(null));
    }
    @Test
    public void testGetCharWithNull() throws IOException {
        assertEquals((char)0, toTest.getChar(null));
    }





    @Test
    public void testGetWrappedByte() throws IOException {
        Byte value = Byte.MIN_VALUE;
        assertEquals(value, toTest.getWrappedByte(valueOf(value)));
    }
    @Test
    public void testGetWrappedShort() throws IOException {
        Short value = Short.MIN_VALUE;
        assertEquals(value, toTest.getWrappedShort(valueOf(value)));
    }
    @Test
    public void testGetWrappedInt() throws IOException {
        Integer value = Integer.MIN_VALUE;
        assertEquals(value, toTest.getWrappedInt(valueOf(value)));
    }
    @Test
    public void testGetWrappedLong() throws IOException {
        Long value = Long.MIN_VALUE;
        assertEquals(value, toTest.getWrappedLong(valueOf(value)));
    }
    @Test
    public void testGetWrappedDouble() throws IOException {
        Double value = Double.MIN_VALUE;
        assertEquals(value, toTest.getWrappedDouble(valueOf(value)));
    }
    @Test
    public void testGetWrappedFloat() throws IOException {
        Float value = Float.MIN_VALUE;
        assertEquals(value, toTest.getWrappedFloat(valueOf(value)));
    }
    @Test
    public void testGetWrappedBoolean() throws IOException {
        String value = getEffectiveBooleanTrue();
        assertEquals(Boolean.TRUE, toTest.getWrappedBoolean(value));
    }

    @Test
    public void testGetWrappedByteWithNull() throws IOException {
        assertNull(toTest.getWrappedByte(null));
    }
    @Test
    public void testGetWrappedShortWithNull() throws IOException {
        assertNull(toTest.getWrappedShort(null));
    }
    @Test
    public void testGetWrappedIntWithNull() throws IOException {
        assertNull(toTest.getWrappedInt(null));
    }
    @Test
    public void testGetWrappedLongWithNull() throws IOException {
        assertNull(toTest.getWrappedLong(null));
    }
    @Test
    public void testGetWrappedDoubleWithNull() throws IOException {
        assertNull(toTest.getWrappedDouble(null));
    }
    @Test
    public void testGetWrappedFloatWithNull() throws IOException {
        assertNull(toTest.getWrappedFloat(null));
    }
    @Test
    public void testGetWrappedBooleanWithNull() throws IOException {
        assertNull(toTest.getWrappedBoolean(null));
    }
    @Test
    public void testGetWrappedCharWithNull() throws IOException {
        assertNull(toTest.getWrappedChar(null));
    }
}
