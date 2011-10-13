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

package org.codegist.crest.param.common;

import org.codegist.crest.BaseCRestTest;
import org.codegist.crest.util.AnotherBunchOfDataSerializer;
import org.codegist.crest.util.BunchOfDataSerializer;
import org.codegist.crest.util.DataSerializer;
import org.codegist.crest.util.model.BunchOfData;
import org.codegist.crest.util.model.Data;
import org.junit.Test;
import org.junit.runners.Parameterized;

import java.util.Collection;
import java.util.EnumSet;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.codegist.crest.util.ToStrings.string;
import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeTrue;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public abstract class ISerializersTest<T extends ISerializersTest.ISerializers> extends BaseCRestTest<T> {

    public ISerializersTest(CRestHolder crest, Class<T> service) {
        super(crest, service);
    }

    @Parameterized.Parameters
    public static Collection<CRestHolder[]> getData() {
        return crest(byRestServices());
    }                      

    public enum Tests {
        DefaultSerialize,ConfiguredSerialize,SerializeNulls
    }

    public EnumSet<Tests> ignores(){
        return EnumSet.noneOf(Tests.class);
    }

    public void assumeThatTestIsEnabled(Tests test){
        assumeTrue(!ignores().contains(test));
    }

    @Test
    public void testDefaultSerialize() {
        assumeThatTestIsEnabled(Tests.DefaultSerialize);
        Data bof = newData(123, "val-456");
        BunchOfData<Data> bof21 = newBunchOfData(date("31/12/2010", "dd/MM/yyyy"), false, newData(456, "val-789"));
        BunchOfData<Data> bof22 = newBunchOfData(date("20/01/2010", "dd/MM/yyyy"), false, newData(789, "val-123"));
        BunchOfData<Data> bof31 = newBunchOfData(date("02/12/2010", "dd/MM/yyyy"), true, newData(1456, "val-1789"));
        BunchOfData<Data> bof32 = newBunchOfData(date("23/03/2010", "dd/MM/yyyy"), true, newData(1789, "val-1123"));

        String actual = toTest.defaults(bof, (Collection) asList(bof21, bof22), new BunchOfData[]{bof31, bof32});

        assertDefaultSerialize(bof, bof21, bof22, bof31, bof32, actual);
    }

    public void assertDefaultSerialize(
            Data bof,
            BunchOfData<Data> bof21,
            BunchOfData<Data> bof22,
            BunchOfData<Data> bof31,
            BunchOfData<Data> bof32,
            String actual
    ) {
        String expectSerializedBof21 = new AnotherBunchOfDataSerializer().serialize(bof21, null);
        String expectSerializedBof22 = new AnotherBunchOfDataSerializer().serialize(bof22, null);
        String expectSerializedBof31 = new AnotherBunchOfDataSerializer().serialize(bof31, null);
        String expectSerializedBof32 = new AnotherBunchOfDataSerializer().serialize(bof32, null);
        String expectSerializedBof = bof.toString();
        assertDefaultSerialize(expectSerializedBof,
                expectSerializedBof21,
                expectSerializedBof22,
                expectSerializedBof31,
                expectSerializedBof32,
                actual);
    }

    public void assertDefaultSerialize(
            String expectSerializedBof,
            String expectSerializedBof21,
            String expectSerializedBof22,
            String expectSerializedBof31,
            String expectSerializedBof32,
            String actual
    ) {
        String expected = format("default() p1=%s p2=%s p3=%s", expectSerializedBof, string(expectSerializedBof21, expectSerializedBof22), string(expectSerializedBof31, expectSerializedBof32));
        assertEquals(expected, actual);
    }


    @Test
    public void testConfiguredSerialize() {
        assumeThatTestIsEnabled(Tests.ConfiguredSerialize);
        Data bof = newData(123, "val-456");
        BunchOfData<Data> bof21 = newBunchOfData(date("31/12/2010", "dd/MM/yyyy"), false, newData(456, "val-789"));
        BunchOfData<Data> bof22 = newBunchOfData(date("20/01/2010", "dd/MM/yyyy"), false, newData(789, "val-123"));
        BunchOfData<Data> bof31 = newBunchOfData(date("02/12/2010", "dd/MM/yyyy"), true, newData(1456, "val-1789"));
        BunchOfData<Data> bof32 = newBunchOfData(date("23/03/2010", "dd/MM/yyyy"), true, newData(1789, "val-1123"));

        String actual = toTest.configured(bof, (Collection) asList(bof21, bof22), new BunchOfData[]{bof31, bof32});

        assertConfiguredSerialize(bof, bof21, bof22, bof31, bof32, actual);
    }

    public void assertConfiguredSerialize(
            Data bof,
            BunchOfData<Data> bof21,
            BunchOfData<Data> bof22,
            BunchOfData<Data> bof31,
            BunchOfData<Data> bof32,
            String actual
    ) {

        String expectSerializedBof21 = new BunchOfDataSerializer().serialize(bof21, null);
        String expectSerializedBof22 = new BunchOfDataSerializer().serialize(bof22, null);
        String expectSerializedBof31 = new BunchOfDataSerializer().serialize(bof31, null);
        String expectSerializedBof32 = new BunchOfDataSerializer().serialize(bof32, null);
        String expectSerializedBof = new DataSerializer().serialize(bof, null);
        assertConfiguredSerialize(expectSerializedBof,
                expectSerializedBof21,
                expectSerializedBof22,
                expectSerializedBof31,
                expectSerializedBof32,
                actual);
    }

    public void assertConfiguredSerialize(
            String expectSerializedBof,
            String expectSerializedBof21,
            String expectSerializedBof22,
            String expectSerializedBof31,
            String expectSerializedBof32,
            String actual
    ) {
        String expected = format("configured() p1=%s p2=%s p3=%s", expectSerializedBof, string(expectSerializedBof21, expectSerializedBof22), string(expectSerializedBof31, expectSerializedBof32));
        assertEquals(expected, actual);
    }


    @Test
    public void testSerializeNulls() {
        assumeThatTestIsEnabled(Tests.SerializeNulls);
        String actual = toTest.nulls(null, null, null);
        assertSerializeNulls(null, null, null, actual);
    }

    public void assertSerializeNulls(String expectedSerializedBof, String expectedSerializedBof2, String expectedSerializedBof3, String actual) {
        String expected = format("null() p1=%s p2=%s p3=%s", expectedSerializedBof, expectedSerializedBof2, expectedSerializedBof3);
        assertEquals(expected, actual);
    }

    /**
     * @author laurent.gilles@codegist.org
     */
    public static interface ISerializers {

        String defaults(Data p1, Collection<BunchOfData<Data>> p2, BunchOfData<Data>[] p3);

        String configured(Data p1, Collection<BunchOfData<Data>> p2, BunchOfData<Data>[] p3);

        String nulls(Data p1, Collection<BunchOfData<Data>> p2, BunchOfData<Data>[] p3);

    }
}
