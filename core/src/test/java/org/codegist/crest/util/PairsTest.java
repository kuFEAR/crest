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

package org.codegist.crest.util;

import org.codegist.crest.NonInstanciableClassTest;
import org.codegist.crest.param.EncodedPair;
import org.codegist.crest.param.SimpleEncodedPair;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.codegist.crest.test.util.Values.UTF8;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author laurent.gilles@codegist.org
 */
public class PairsTest extends NonInstanciableClassTest {

    private final static List<? extends EncodedPair> PAIRS = asList(
            new SimpleEncodedPair("p1", "v1"),
            new SimpleEncodedPair("p2", "v2")
    );

    public PairsTest() {
        super(Pairs.class);
    }

    @Test
    public void toPreEncodedPairShouldBuildASimpleEncodedPairWithGivenParams() {
        EncodedPair actual = Pairs.toPreEncodedPair("some name", "some value");
        assertNotNull(actual);
        assertEquals(SimpleEncodedPair.class, actual.getClass());
        assertEquals("some name", actual.getName());
        assertEquals("some value", actual.getValue());
    }

    @Test
    public void toPairDefaultToEncodedShouldEncodedAndBuildASimpleEncodedPairWithGivenParams() throws UnsupportedEncodingException {
        EncodedPair actual = Pairs.toPair("some name", "some value", UTF8);
        assertNotNull(actual);
        assertEquals(SimpleEncodedPair.class, actual.getClass());
        assertEquals("some%20name", actual.getName());
        assertEquals("some%20value", actual.getValue());
    }

    @Test
    public void toPairShouldEncodedAndBuildASimpleEncodedPairWithGivenParams() throws UnsupportedEncodingException {
        EncodedPair actual = Pairs.toPair("some name", "some value", UTF8, false);
        assertNotNull(actual);
        assertEquals(SimpleEncodedPair.class, actual.getClass());
        assertEquals("some%20name", actual.getName());
        assertEquals("some%20value", actual.getValue());
    }

    @Test
    public void toPairShouldNotEncodedAndBuildASimpleEncodedPairWithGivenParams() throws UnsupportedEncodingException {
        EncodedPair actual = Pairs.toPair("some name", "some value", UTF8, true);
        assertNotNull(actual);
        assertEquals(SimpleEncodedPair.class, actual.getClass());
        assertEquals("some name", actual.getName());
        assertEquals("some value", actual.getValue());
    }

    @Test
    public void fromUrlEncodedShouldParseQueryStringAndReturnEncodedParams(){
        List<EncodedPair> actual = Pairs.fromUrlEncoded("p1=v%201&p1=v%202&p2=v%203");
        assertNotNull(actual);
        assertEquals(3, actual.size());
        assertEquals("p1", actual.get(0).getName());
        assertEquals("v%201", actual.get(0).getValue());
        assertEquals("p1", actual.get(1).getName());
        assertEquals("v%202", actual.get(1).getValue());
        assertEquals("p2", actual.get(2).getName());
        assertEquals("v%203", actual.get(2).getValue());
    }


    @Test
    public void sortByNameAndValuesShouldSort(){
        List<? extends EncodedPair> expected = asList(
                new SimpleEncodedPair("a", "aa"),
                new SimpleEncodedPair("a", "ab"),
                new SimpleEncodedPair("aa", "aa"),
                new SimpleEncodedPair("aa", "ab"),
                new SimpleEncodedPair("ab", ""),
                new SimpleEncodedPair("ab", "00"),
                new SimpleEncodedPair("ab", "01")
        );
        List<? extends EncodedPair> toSort = new ArrayList<EncodedPair>(expected);
        Collections.shuffle(toSort);
        List<? extends EncodedPair> toSortOriginal = new ArrayList<EncodedPair>(toSort);
        List<EncodedPair> actual = Pairs.sortByNameAndValues(toSort);
        assertEquals(toSortOriginal, toSort);
        assertEquals(expected, actual);
    }

    @Test
    public void joinWithWriterIteratorPairNameSepsQuoteNameValuesShouldJoinParamsAccordingly() throws IOException {
        StringWriter actual = new StringWriter();
        Pairs.join(actual, PAIRS.iterator(), 's', 'e', true, true);
        assertEquals("\"p1\"e\"v1\"s\"p2\"e\"v2\"", actual.toString());

        actual = new StringWriter();
        Pairs.join(actual, PAIRS.iterator(), 's', 'e', false, true);
        assertEquals("p1e\"v1\"sp2e\"v2\"", actual.toString());

        actual = new StringWriter();
        Pairs.join(actual, PAIRS.iterator(), 's', 'e', true, false);
        assertEquals("\"p1\"ev1s\"p2\"ev2", actual.toString());

        actual = new StringWriter();
        Pairs.join(actual, PAIRS.iterator(), 's', 'e', false, false);
        assertEquals("p1ev1sp2ev2", actual.toString());
    }

    @Test
    public void joinWithWriterIteratorPairNameSepsShouldJoinParamsAccordingly() throws IOException {
        StringWriter actual = new StringWriter();
        Pairs.join(actual, PAIRS.iterator(), 's', 'e');
        assertEquals("p1ev1sp2ev2", actual.toString());
    }

    @Test
    public void joinWithWriterIteratorPairSepsShouldJoinParamsAccordingly() throws IOException {
        StringWriter actual = new StringWriter();
        Pairs.join(actual, PAIRS.iterator(), 's');
        assertEquals("p1=v1sp2=v2", actual.toString());
    }




    @Test
    public void joinWithWriterListPairNameSepsQuoteNameValuesShouldJoinParamsAccordingly() throws IOException {
        StringWriter actual = new StringWriter();
        Pairs.join(actual, PAIRS, 's', 'e', true, true);
        assertEquals("\"p1\"e\"v1\"s\"p2\"e\"v2\"", actual.toString());

        actual = new StringWriter();
        Pairs.join(actual, PAIRS, 's', 'e', false, true);
        assertEquals("p1e\"v1\"sp2e\"v2\"", actual.toString());

        actual = new StringWriter();
        Pairs.join(actual, PAIRS, 's', 'e', true, false);
        assertEquals("\"p1\"ev1s\"p2\"ev2", actual.toString());

        actual = new StringWriter();
        Pairs.join(actual, PAIRS, 's', 'e', false, false);
        assertEquals("p1ev1sp2ev2", actual.toString());
    }

    @Test
    public void joinWithWriterListPairNameSepsShouldJoinParamsAccordingly() throws IOException {
        StringWriter actual = new StringWriter();
        Pairs.join(actual, PAIRS, 's', 'e');
        assertEquals("p1ev1sp2ev2", actual.toString());
    }

    @Test
    public void joinWithWriterListPairSepsShouldJoinParamsAccordingly() throws IOException {
        StringWriter actual = new StringWriter();
        Pairs.join(actual, PAIRS, 's');
        assertEquals("p1=v1sp2=v2", actual.toString());
    }


    @Test
    public void joinWithIteratorPairNameSepsQuoteNameValuesShouldJoinParamsAccordingly() throws IOException {
        assertEquals("\"p1\"e\"v1\"s\"p2\"e\"v2\"", Pairs.join(PAIRS.iterator(), 's', 'e', true, true));
        assertEquals("p1e\"v1\"sp2e\"v2\"", Pairs.join(PAIRS.iterator(), 's', 'e', false, true));
        assertEquals("\"p1\"ev1s\"p2\"ev2", Pairs.join(PAIRS.iterator(), 's', 'e', true, false));
        assertEquals("p1ev1sp2ev2", Pairs.join(PAIRS.iterator(), 's', 'e', false, false));
    }

    @Test
    public void joinWithIteratorPairNameSepsShouldJoinParamsAccordingly() throws IOException {
        assertEquals("p1ev1sp2ev2", Pairs.join(PAIRS.iterator(), 's', 'e'));
    }

    @Test
    public void joinWithIteratorPairSepsShouldJoinParamsAccordingly() throws IOException {
        assertEquals("p1=v1sp2=v2", Pairs.join(PAIRS.iterator(), 's'));
    }




    @Test
    public void joinWithListPairNameSepsQuoteNameValuesShouldJoinParamsAccordingly() throws IOException {
        assertEquals("\"p1\"e\"v1\"s\"p2\"e\"v2\"", Pairs.join(PAIRS, 's', 'e', true, true));
        assertEquals("p1e\"v1\"sp2e\"v2\"", Pairs.join(PAIRS, 's', 'e', false, true));
        assertEquals("\"p1\"ev1s\"p2\"ev2", Pairs.join(PAIRS, 's', 'e', true, false));
        assertEquals("p1ev1sp2ev2", Pairs.join(PAIRS, 's', 'e', false, false));
    }

    @Test
    public void joinWithListPairNameSepsShouldJoinParamsAccordingly() throws IOException {
        assertEquals("p1ev1sp2ev2", Pairs.join(PAIRS, 's', 'e'));
    }

    @Test
    public void joinWithListPairSepsShouldJoinParamsAccordingly() throws IOException {
        assertEquals("p1=v1sp2=v2", Pairs.join(PAIRS, 's'));
    }

}
