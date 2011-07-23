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

package org.codegist.crest.param.forms.json.common;

import org.codegist.crest.annotate.*;
import org.codegist.crest.param.common.ICollectionsTest;
import org.junit.runners.Parameterized;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * @author laurent.gilles@codegist.org
 */
public class CollectionsTest extends ICollectionsTest<CollectionsTest.Collections> {

    public CollectionsTest(CRestHolder crest) {
        super(crest, Collections.class);
    }

    @Parameterized.Parameters
    public static Collection<CRestHolder[]> getData() {
        return crest(byJsonSerializersAndRestServices());
    }

    @EndPoint("{crest.server.end-point}")
    @Path("params/form/json")
    @POST
    @JsonEntity
    public static interface Collections extends ICollectionsTest.ICollections {


        String defaults(
                @FormParam("p1") String[] p1,
                @FormParam("p2") boolean[] p2,
                @FormParam("p3") List<Integer> p3,
                @FormParam("p4") Set<Long> p4);

        @ListSeparator("(def)")
        String merging(
                @FormParam("p1") String[] p1,
                @FormParam("p2") @ListSeparator("(p2)") boolean[] p2,
                @FormParam("p3") @ListSeparator("(p3)") List<Integer> p3,
                @FormParam("p4") @ListSeparator("(p4)") Set<Long> p4);

    }

    @Override
    public void assertDefaultLists(String p11, String p12, boolean p21, boolean p22, Integer p31, Integer p32, Long p41, Long p42, String actual) {
        StringBuilder expected = new StringBuilder();
        expected.append("{");
        expected.append("\"p1\":[\"").append(p11).append("\",\"").append(p12).append("\"],");
        expected.append("\"p2\":[").append(p21).append(",").append(p22).append("],");
        expected.append("\"p3\":[").append(p31).append(",").append(p32).append("],");
        expected.append("\"p4\":[").append(p41).append(",").append(p42).append("]");
        expected.append("}");
        assertEquals(expected.toString(), actual);
    }

    @Override
    public void assertMergingLists(String p11, String p1Sep, String p12, boolean p21, String p2Sep, boolean p22, Integer p31, String p3Sep, Integer p32, Long p41, String p4Sep, Long p42, String actual) {
        assertDefaultLists(p11, p12, p21, p22, p31, p32, p41, p42, actual);
    }
}
