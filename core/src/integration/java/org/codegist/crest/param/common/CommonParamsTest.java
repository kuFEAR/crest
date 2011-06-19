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

package org.codegist.crest.param.common;

import org.codegist.crest.BaseCRestTest;
import org.codegist.crest.CRest;
import org.codegist.crest.model.BunchOfData;
import org.codegist.crest.model.Data;
import org.codegist.crest.serializer.AnotherBunchOfDataSerializer;
import org.codegist.crest.serializer.BunchOfDataSerializer;
import org.codegist.crest.serializer.DataSerializer;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.codegist.common.net.Urls.encode;
import static org.junit.Assert.assertEquals;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class CommonParamsTest<T extends Params> extends BaseCRestTest<T> {

    public CommonParamsTest(CRest crest, Class<T> service) {
        super(crest, service);
    }

    @Test
    public void testSend() {
        String p1 = "my-value";
        int p2 = 1983;

        String actual = toTest.send(p1, p2);
        assertSend(p1, p2, actual);
    }
    public void assertSend(String p1, int p2, String actual){
        String expected = format("receive() p1=%s p2=%s", p1, p2);
        assertEquals(expected, actual);
    }

    @Test
    public void testDates() throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy/MM/dd'T'HH:mm:ssZ");
        DateFormat expected = new SimpleDateFormat(DATE_FORMAT);
        Date p1 = format.parse("1969/07/20T02:56:00+0000");
        Date p21 = format.parse("1983/03/13T00:35:00+0100");
        Date p22 = format.parse("2010/10/31T13:56:21-0700");

        String actual = toTest.dates(p1, p21, p22);
        assertDates(expected.format(p1), expected.format(p21), expected.format(p22), actual);
    }
    public void assertDates(String p1, String p21, String p22, String actual){
        String expected = format("dates() p1=%s p2=%s", p1, asList(p21,p22));
        assertEquals(expected, actual);
    }

    @Test
    public void testNulls(){
        String actual = toTest.nulls(null, null, null);
        assertNulls(actual);
    }

    @Test
    public void testNullsInCollection(){
        String actual = toTest.nulls(null, asList((String)null,(String)null), null);
        assertNulls(actual);
    }

    @Test
    public void testNullsInArray(){
        String actual = toTest.nulls(null, null, new String[]{null,null});
        assertNulls(actual);
    }

    public void assertNulls(String actual){
        assertEquals("nulls() p1=null p2=null p3=null", actual);
    }


    @Test
    public void testNullsMerging(){
        String actual = toTest.nullsMerging(null, null, null);
        assertNullsMerging(actual);
    }

    @Test
    public void testNullsMergingInCollection(){
        String actual = toTest.nullsMerging(null, asList((String)null,(String)null), null);
        assertNullsMerging(actual);
    }

    @Test
    public void testNullsMergingInArray(){
        String actual = toTest.nullsMerging(null, null, new String[]{null,null});
        assertNullsMerging(actual);
    }

    public void assertNullsMerging(String actual){
        assertEquals("nullsMerging() p1=null p2=null p3=null", actual);
    }

    @Test
    public void testDefaultValue(){
        String actual = toTest.defaultValue(null, null);
        assertDefaultValue("default-p1", 123, actual);
    }
    public void assertDefaultValue(String defaultP1, int defaultP2, String actual){
        assertEquals(format("defaultValue() p1=%s p2=%s", defaultP1, defaultP2), actual);
    }

    @Test
    public void testDefaultParams(){
        String p1 = "p1";
        String actual = toTest.defaultParams(p1);
        assertParamsValue("p1-val", p1, "p2-val","p3-val", actual);
    }
    public void assertParamsValue(String p11, String p12, String p2, String p3, String actual){
        assertEquals(format("defaultParams() p1=[%s, %s] p2=%s p3=%s", p11, p12,p2,p3), actual);
    }

    @Test
    public void testDefaultLists() {
        String p11 = "p1-val1", p12 = "my-value";
        boolean p21 = true, p22 = false;
        Integer p31 = 31, p32 = 32;
        Long p41 = 41l, p42 = 42l;

        String[] p1 = {p11, p12};
        boolean[] p2 = {p21, p22};
        List<Integer> p3 = asList(p31, p32);
        Set<Long> p4 = new LinkedHashSet<Long>(asList(p41, p42));

        String actual = toTest.defaultLists(p1,p2,p3,p4);

        assertDefaultLists(p11, p12, p21, p22, p31, p32, p41, p42, actual);
    }
    public void assertDefaultLists(String p11, String p12, boolean p21, boolean p22, Integer p31, Integer p32, Long p41, Long p42, String actual){
        String expected = format("defaultLists() p1=%s p2=%s p3=%s p4=%s", asList(p11, p12),asList(p21, p22), asList(p31, p32), asList(p41, p42));
        assertEquals(expected, actual);
    }

    @Test
    public void testMergingLists() {
        String p1Sep = "(def)";
        String p2Sep = "(p2)";
        String p3Sep = "(p3)";
        String p4Sep = "(p4)";
        String p11 = "p1-val1", p12 = "my-value";
        boolean p21 = true, p22 = false;
        Integer p31 = 31, p32 = 32;
        Long p41 = 41l, p42 = 42l;

        String[] p1 = {p11, p12};
        boolean[] p2 = {p21, p22};
        List<Integer> p3 = asList(p31, p32);
        Set<Long> p4 = new LinkedHashSet<Long>(asList(p41, p42));

        String actual = toTest.mergingLists(p1,p2,p3,p4);

        assertMergingLists(p11,p1Sep,p12,
                            p21,p2Sep,p22,
                            p31,p3Sep,p32,
                            p41,p4Sep,p42, actual);
    }

    public void assertMergingLists(String p11,String p1Sep,String p12,
                                    boolean p21,String p2Sep,boolean p22,
                                    Integer p31,String p3Sep,Integer p32,
                                    Long p41,String p4Sep,Long p42, String actual){
        String expected = format("mergingLists() p1=%s%s%s p2=%s%s%s p3=%s%s%s p4=%s%s%s",
                p11,p1Sep,p12,
                p21,p2Sep,p22,
                p31,p3Sep,p32,
                p41,p4Sep,p42);
        assertEquals(expected, actual);
    }

    @Test
    public void testEncodings() {
        String p21 = "&";
        String p22 = "my-value";
        String p1 = "my-value";
        Collection<String> p2 = asList(p21, p22);

        String actual = toTest.encodings(p1, p2);
        assertEncodings(p1, p21, p22, actual);
    }

    public void assertEncodings(String p1, String p21, String p22, String actual){
        String expected = format("encodings() p1=%s p2=%s", p1, asList(p21, p22));
        assertEquals(expected, actual);
    }

    @Test
    public void testPreEncoded() throws UnsupportedEncodingException {
        String p21 = "&";
        String p22 = "my-value";
        String p1 = "my-value";
        Collection<String> p2 = asList(encode(p21,"utf-8"), encode(p22, "utf-8"));

        String actual = toTest.preEncoded(encode(p1,"utf-8"), p2);
        assertPreEncoded(p1, p21, p22, actual);
    }

    public void assertPreEncoded(String p1, String p21, String p22, String actual) throws UnsupportedEncodingException {
        String expected = format("preEncoded() p1=%s p2=%s", p1, asList(p21, p22));
        assertEquals(expected, actual);
    }




    @Test
    public void testDefaultSerialize(){
        Data bof = new Data(123, "val-456");
        BunchOfData<Data> bof21 = new BunchOfData<Data>(date("31/12/2010", "dd/MM/yyyy"), false, new Data(456, "val-789"));
        BunchOfData<Data> bof22 = new BunchOfData<Data>(date("20/01/2010", "dd/MM/yyyy"), false, new Data(789, "val-123"));
        BunchOfData<Data> bof31 = new BunchOfData<Data>(date("02/12/2010", "dd/MM/yyyy"), true, new Data(1456, "val-1789"));
        BunchOfData<Data> bof32 = new BunchOfData<Data>(date("23/03/2010", "dd/MM/yyyy"), null, new Data(1789, "val-1123"));

        String actual = toTest.defaultSerialize(bof, (Collection) asList(bof21, bof22), new BunchOfData[]{bof31, bof32});

        assertDefaultSerialize(bof,bof21,bof22,bof31,bof32,actual);
    }
    public void assertDefaultSerialize(
            Data bof,
            BunchOfData<Data> bof21,
            BunchOfData<Data> bof22,
            BunchOfData<Data> bof31,
            BunchOfData<Data> bof32,
            String actual
        ){
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
        ){
        String expected = format("defaults() p1=%s p2=%s p3=%s", expectSerializedBof, asList(expectSerializedBof21, expectSerializedBof22), asList(expectSerializedBof31, expectSerializedBof32));
        assertEquals(expected, actual);
    }

    @Test
    public void testConfiguredSerialize(){
        Data bof = new Data(123, "val-456");
        BunchOfData<Data> bof21 = new BunchOfData<Data>(date("31/12/2010", "dd/MM/yyyy"), false, new Data(456, "val-789"));
        BunchOfData<Data> bof22 = new BunchOfData<Data>(date("20/01/2010", "dd/MM/yyyy"), false, new Data(789, "val-123"));
        BunchOfData<Data> bof31 = new BunchOfData<Data>(date("02/12/2010", "dd/MM/yyyy"), true, new Data(1456, "val-1789"));
        BunchOfData<Data> bof32 = new BunchOfData<Data>(date("23/03/2010", "dd/MM/yyyy"), null, new Data(1789, "val-1123"));

        String actual = toTest.configuredSerialize(bof, (Collection) asList(bof21, bof22), new BunchOfData[]{bof31, bof32});

        assertConfiguredSerialize(bof,bof21,bof22,bof31,bof32,actual);
    }
    public void assertConfiguredSerialize(
            Data bof,
            BunchOfData<Data> bof21,
            BunchOfData<Data> bof22,
            BunchOfData<Data> bof31,
            BunchOfData<Data> bof32,
            String actual
        ){

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
        ){
        String expected = format("configured() p1=%s p2=%s p3=%s", expectSerializedBof, asList(expectSerializedBof21, expectSerializedBof22), asList(expectSerializedBof31, expectSerializedBof32));
        assertEquals(expected, actual);
    }

    @Test
    public void testSerializeNulls(){
        String actual = toTest.serializeNulls(null, null, null);
        assertSerializeNulls(null, null, null, actual);
    }

    public void assertSerializeNulls(String expectedSerializedBof, String expectedSerializedBof2, String expectedSerializedBof3, String actual){
        String expected = format("nulls() p1=%s p2=%s p3=%s", expectedSerializedBof, expectedSerializedBof2, expectedSerializedBof3);
        assertEquals(expected, actual);
    }
}
