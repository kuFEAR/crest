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
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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
        String p1 = UTF8_VALUE;
        int p2 = 1983;

        String actual = toTest.send(p1, p2);
        assertSend(p1, p2, actual);
    }
    public void assertSend(String p1, int p2, String actual){
        String expected = format("receive() p1=%s p2=%s", p1, p2);
        assertEquals(expected, actual);
    }

    @Test
    public void testDefaultValue(){
        String actual = toTest.defaultValue(null, null);
        assertEquals("defaultValue() p1=default-p1 p2=123", actual);
    }

    @Test
    public void testDefaultParams(){
        String actual = toTest.defaultParams("p1");
        assertEquals("defaultParams() p1=[p1-val, p1] p2=p2-val p3=p3-val", actual);
    }

    @Test
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
        String p11 = "p1-val1", p12 = UTF8_VALUE;
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
        String p22 = UTF8_VALUE;
        String p1 = UTF8_VALUE;
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
        String p22 = UTF8_VALUE;
        String p1 = UTF8_VALUE;
        Collection<String> p2 = asList(encode(p21,"utf-8"), encode(p22, "utf-8"));

        String actual = toTest.preEncoded(encode(p1,"utf-8"), p2);
        assertPreEncoded(p1, p21, p22, actual);
    }

    public void assertPreEncoded(String p1, String p21, String p22, String actual) throws UnsupportedEncodingException {
        String expected = format("preEncoded() p1=%s p2=%s", p1, asList(p21, p22));
        assertEquals(expected, actual);
    }

}
