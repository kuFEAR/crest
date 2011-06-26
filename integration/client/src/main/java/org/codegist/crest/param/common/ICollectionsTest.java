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
import org.junit.Test;
import org.junit.runners.Parameterized;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.codegist.crest.utils.ToStrings.string;
import static org.junit.Assert.assertEquals;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public abstract class ICollectionsTest<T extends ICollectionsTest.ICollections> extends BaseCRestTest<T> {

    public ICollectionsTest(CRestHolder crest, Class<T> service) {
        super(crest, service);
    }

    @Parameterized.Parameters
    public static Collection<CRestHolder[]> getData() {
        return crest(byRestServices());
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

        String actual = toTest.defaults(p1, p2, p3, p4);

        assertDefaultLists(p11, p12, p21, p22, p31, p32, p41, p42, actual);
    }

    public void assertDefaultLists(String p11, String p12, boolean p21, boolean p22, Integer p31, Integer p32, Long p41, Long p42, String actual) {
        String expected = format("default() p1=%s p2=%s p3=%s p4=%s", string(p11, p12), string(toString(p21), toString(p22)), string(p31, p32), string(p41, p42));
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

        String actual = toTest.merging(p1, p2, p3, p4);

        assertMergingLists(p11, p1Sep, p12,
                p21, p2Sep, p22,
                p31, p3Sep, p32,
                p41, p4Sep, p42, actual);
    }

    public void assertMergingLists(String p11, String p1Sep, String p12,
                                   boolean p21, String p2Sep, boolean p22,
                                   Integer p31, String p3Sep, Integer p32,
                                   Long p41, String p4Sep, Long p42, String actual) {
        String expected = format("merging() p1=%s%s%s p2=%s%s%s p3=%s%s%s p4=%s%s%s",
                p11, p1Sep, p12,
                toString(p21), p2Sep, toString(p22),
                p31, p3Sep, p32,
                p41, p4Sep, p42);
        assertEquals(expected, actual);
    }

    /**
     * @author laurent.gilles@codegist.org
     */
    public static interface ICollections {

        String defaults(String[] p1, boolean[] p2, List<Integer> p3, Set<Long> p4);

        String merging(String[] p1, boolean[] p2, List<Integer> p3, Set<Long> p4);

    }
}
