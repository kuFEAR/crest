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
import org.codegist.crest.serializer.CommaSeparatedIntDeserializer;
import org.codegist.crest.serializer.IntDeserializer;
import org.junit.Test;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Collection;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

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
                        .bindDeserializer(new IntDeserializer(), "text/int")
                        .bindDeserializer(new CommaSeparatedIntDeserializer(), int[].class)
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
    public void testPrimitive() throws IOException {
        int data = 12;
        int actual = toTest.primitive(data);
        assertEquals(actual, actual);
    }

    @Test
    public void testPrimitives() throws IOException {
        int[] data = {-56,0,10,22,34};
        int[] actual = toTest.primitives(data);
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

}
