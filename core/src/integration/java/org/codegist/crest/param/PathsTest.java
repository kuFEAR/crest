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

import java.util.Collection;

import static org.junit.Assert.assertEquals;

public class PathsTest extends CommonParamsTest<Paths> {

    public PathsTest(CRest crest) {
        super(crest, Paths.class);
    }

    @Parameterized.Parameters
    public static Collection<CRest[]> getData() {
        return crest(byRestServices(baseBuilder()));
    }

    @Test
    public void testPattern(){
        String p1 = "val-1983-ab";
        String actual = toTest.pattern(p1);
        assertEquals("pattern() p1=" + p1, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPatternInvalidValue(){
        toTest.pattern("val-198-ab");
    }

    @Test
    public void testDefaultParams(){
        String actual = toTest.defaultParams("p4-val");
        assertEquals("defaultParams() p1=p1-val p2=p2-val p3=p3-val p4=p4-val", actual);
    }

    // Path interface has the list separator set
    public void assertEncodings(String p1, String p21, String p22, String actual){
        String expected = String.format("encodings() p1=%s p2=%s(p2)%s", p1, p21, p22);
        assertEquals(expected, actual);
    }
    // Path interface has the list separator set
    public void assertPreEncoded(String p1, String p21, String p22, String actual){
        String expected = String.format("preEncoded() p1=%s p2=%s(p2)%s", p1, p21, p22);
        assertEquals(expected, actual);
    }

    @Override
    @Ignore("Path doesn't have default list behavior")
    public void testDefaultLists() {

    }
}

