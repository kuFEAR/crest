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

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public abstract class INullsTest<T extends INullsTest.INulls> extends BaseCRestTest<T> {

    public INullsTest(CRestHolder crest, Class<T> service) {
        super(crest, service);
    }

    @Parameterized.Parameters
    public static Collection<CRestHolder[]> getData() {
        return crest(byRestServices());
    }

    @Test
    public void testNulls() {
        String actual = toTest.nulls(null, null, null);
        assertNulls(actual);
    }

    @Test
    public void testNullsInCollection() {
        String actual = toTest.nulls(null, asList((String) null, (String) null), null);
        assertNulls(actual);
    }

    @Test
    public void testNullsInArray() {
        String actual = toTest.nulls(null, null, new String[]{null, null});
        assertNulls(actual);
    }

    public void assertNulls(String actual) {
        assertEquals("null() p1=null p2=null p3=null", actual);
    }


    @Test
    public void testNullsMerging() {
        String actual = toTest.merging(null, null, null);
        assertNullsMerging(actual);
    }

    @Test
    public void testNullsMergingInCollection() {
        String actual = toTest.merging(null, asList((String) null, (String) null), null);
        assertNullsMerging(actual);
    }

    @Test
    public void testNullsMergingInArray() {
        String actual = toTest.merging(null, null, new String[]{null, null});
        assertNullsMerging(actual);
    }

    public void assertNullsMerging(String actual) {
        assertEquals("merging() p1=null p2=null p3=null", actual);
    }

    /**
     * @author laurent.gilles@codegist.org
     */
    public static interface INulls {

        String nulls(String p1, Collection<String> p2, String[] p3);

        String merging(String p1, Collection<String> p2, String[] p3);

    }
}
