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

package org.codegist.crest.param;

import org.codegist.crest.CRest;
import org.codegist.crest.param.common.CommonParamsTest;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.Parameterized;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.codegist.common.net.Urls.encode;
import static org.junit.Assert.assertEquals;

public class CookiesTest extends CommonParamsTest<Cookies> {

    public CookiesTest(CRest crest) {
        super(crest, Cookies.class);
    }

    @Parameterized.Parameters
    public static Collection<CRest[]> getData() {
        return crest(byRestServices());
    }

    public void assertDates(String p1, String p21, String p22, String actual){
        String expected = format("dates(header:[p1=%s, p2=%s, p2=%s]) p1=%s p2=%s", p1, p21,p22, p1, p22);
        assertEquals(expected, actual);
    }

    @Test
    public void testDefaultParams(){
        String actual = toTest.defaultParams("p1");
        assertEquals("defaultParams(header:[p2=p2-val, p1=p1-val, p3=p3-val, p1=p1]) p1=p1 p2=p2-val p3=p3-val", actual);
    }

    @Test
    public void testNulls(){
        String actual = toTest.nulls(null, null, null);
        assertEquals("nulls(header:[]) p1=null p2=null p3=null", actual);
    }

    @Test
    public void testNullsInCollection(){
        String actual = toTest.nulls(null, asList((String)null,(String)null), null);
        assertEquals("nulls(header:[]) p1=null p2=null p3=null", actual);
    }

    @Test
    public void testNullsInArray(){
        String actual = toTest.nulls(null, null, new String[]{null,null});
        assertEquals("nulls(header:[]) p1=null p2=null p3=null", actual);
    }

    @Test
    public void testNullsMerging(){
        String actual = toTest.nullsMerging(null, null, null);
        assertEquals("nullsMerging(header:[]) p1=null p2=null p3=null", actual);
    }

    @Test
    public void testNullsMergingInCollection(){
        String actual = toTest.nullsMerging(null, asList((String)null,(String)null), null);
        assertEquals("nullsMerging(header:[]) p1=null p2=null p3=null", actual);
    }

    @Test
    public void testNullsMergingInArray(){
        String actual = toTest.nullsMerging(null, null, new String[]{null,null});
        assertEquals("nullsMerging(header:[]) p1=null p2=null p3=null", actual);
    }

    @Test
    @Ignore("HttpURLConnection.setRequestProperty don't encode properly UTF-8. Works with HttpClient")
    public void testSend() {
        String p1 = UTF8_VALUE;
        int p2 = 1983;

        String actual = toTest.send(p1,p2);

        String expected = String.format("receive(header:[p1=%s, p2=%s]) p1=%s p2=%s", p1, p2, p1, p2);
        assertEquals(expected, actual);
    }

    @Test
    @Ignore("HttpURLConnection.setRequestProperty don't encode properly UTF-8. Works with HttpClient")
    public void testDefaultLists() {
        String p11 = "p1-val1", p12 = UTF8_VALUE;
        boolean p21 = true, p22 = false;
        Integer p31 = 31, p32 = 32;
        Long p41 = 41l, p42 = 42l;

        String[] p1 = {p11, p12};
        boolean[] p2 = {p21, p22};
        List<Integer> p3 = asList(p31, p32);
        Set<Long> p4 = new LinkedHashSet<Long>(asList(p41, p42));

        String actual = toTest.defaultLists(p1,p2,p3,p4);

        String expected = String.format("defaultLists(header:[p1=%s, p1=%s, p2=%s, p2=%s, p3=%s, p3=%s, p4=%s, p4=%s]) p1=%s p2=%s p3=%s p4=%s", p11,p12,p21,p22,p31,p32,p41,p42,p12,p22,p32,p42);
        assertEquals(expected, actual);
    }

    @Test
    @Ignore("HttpURLConnection.setRequestProperty don't encode properly UTF-8. Works with HttpClient")
    public void testMergingLists() {
        String p1Sep = "(def)";
        String p2Sep = "(p2)";
        String p3Sep = "(p3)";
        String p4Sep = "(p4)";
        String p11 = "p1-val1", p12 = UTF8_VALUE;
        boolean p21 = true, p22 = false;
        Integer p31 = 31, p32 = 32;
        Long p41 = 41l, p42 = 42l;

        String[] p1 = {p11, p12};
        boolean[] p2 = {p21, p22};
        List<Integer> p3 = asList(p31, p32);
        Set<Long> p4 = new LinkedHashSet<Long>(asList(p41, p42));

        String actual = toTest.mergingLists(p1,p2,p3,p4);

        String expected = String.format("mergingLists(header:[p1=%s%s%s, p2=%s%s%s, p3=%s%s%s, p4=%s%s%s]) p1=%s%s%s p2=%s%s%s p3=%s%s%s p4=%s%s%s",
                p11,p1Sep,p12,
                p21,p2Sep,p22,
                p31,p3Sep,p32,
                p41,p4Sep,p42,
                p11,p1Sep,p12,
                p21,p2Sep,p22,
                p31,p3Sep,p32,
                p41,p4Sep,p42);
        assertEquals(expected, actual);
    }

    @Test
    @Ignore("HttpURLConnection.setRequestProperty don't encode properly UTF-8. Works with HttpClient")
    public void testEncodings() {
        String p21 = "&";
        String p22 = UTF8_VALUE;
        String p1 = UTF8_VALUE;
        Collection<String> p2 = asList(p21, p22);
        String actual = toTest.encodings(p1, p2);

        String expected = String.format("encodings(header:[p1=%s, p2=%s, p2=%s]) p1=%s p2=%s", p1, p21, p22, p1, p22);
        assertEquals(expected, actual);
    }

    @Test
    public void testPreEncoded() throws UnsupportedEncodingException {
        String p21 = encode("&","utf-8");
        String p22 = encode(UTF8_VALUE,"utf-8");
        String p1 = encode(UTF8_VALUE,"utf-8");
        Collection<String> p2 = asList(p21, p22);
        String actual = toTest.preEncoded(p1, p2);

        String expected = String.format("preEncoded(header:[p1=%s, p2=%s, p2=%s]) p1=%s p2=%s", p1, p21, p22, p1, p22);
        assertEquals(expected, actual);
    }

}
