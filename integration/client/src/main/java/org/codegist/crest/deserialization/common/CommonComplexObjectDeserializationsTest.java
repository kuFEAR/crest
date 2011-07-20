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

import org.codegist.crest.BaseCRestTest;
import org.codegist.crest.util.model.SomeData;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * @author laurent.gilles@codegist.org
 */
public class CommonComplexObjectDeserializationsTest<T extends IComplexObjectDeserializations> extends BaseCRestTest<T> {

    public CommonComplexObjectDeserializationsTest(CRestHolder crest, Class<T> service) {
        super(crest, service);
    }

    @Test
    public void testSomeDataGuessed() throws IOException {
        SomeData actual = toTest.someDataGuessed(getEffectiveDateFormat(), getEffectiveBooleanTrue(), getEffectiveBooleanFalse());
        SomeData expected = expected();
        assertSomeDataGuessed(expected, actual);
    }

    public void assertSomeDataGuessed(SomeData expected, SomeData actual){
        assertEquals(expected, actual);
    }

    @Test
    public void testSomeDataForced() throws IOException {
        SomeData actual = toTest.someDataForced(getEffectiveDateFormat(), getEffectiveBooleanTrue(), getEffectiveBooleanFalse());
        SomeData expected = expected();

        assertSomeDataForced(expected, actual);
    }

    public void assertSomeDataForced(SomeData expected, SomeData actual){
        assertEquals(expected, actual);
    }

    @Test
    public void testSomeDatas() throws IOException {
        SomeData[] actual = toTest.someDatas(getEffectiveDateFormat(), getEffectiveBooleanTrue(), getEffectiveBooleanFalse());
        SomeData[] expected = {expected(),expected()};

        assertSomeDatas(expected, actual);
    }

    public void assertSomeDatas(SomeData[] expected, SomeData[] actual){
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testSomeDatas2() throws IOException {
        List<? extends SomeData> actual = toTest.someDatas2(getEffectiveDateFormat(), getEffectiveBooleanTrue(), getEffectiveBooleanFalse());
        List<? extends SomeData> expected = asList(expected(),expected());

        assertSomeDatas2(expected, actual);
    }

    public void assertSomeDatas2(List<? extends SomeData> expected, List<? extends SomeData> actual){
        assertEquals(expected, actual);
    }


    private static SomeData  expected(){
        SomeData expected = new SomeData() {
        };
        expected.setBool(true);
        expected.setDate(date("1926-02-20T12:32:01+00:00", "yyyy-MM-dd'T'HH:mm:ss+00:00"));
        expected.setNum(13);
        return expected;
    }

}
