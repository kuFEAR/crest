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

package org.codegist.crest.param.cookies.common;

import org.codegist.crest.CRest;
import org.codegist.crest.annotate.*;
import org.codegist.crest.param.common.ICollectionsTest;

import java.util.List;
import java.util.Set;

import static java.lang.String.format;
import static org.junit.Assert.assertEquals;

/**
 * @author laurent.gilles@codegist.org
 */
public class CollectionsTest extends ICollectionsTest<CollectionsTest.Collections> {

    public CollectionsTest(CRest crest) {
        super(crest, Collections.class);
    }

    @EndPoint(ADDRESS)
    @Path("params/cookie/collection")
    @GET
    public static interface Collections extends ICollectionsTest.ICollections {


        @Path("default")
        String defaults(
                @CookieParam("p1") String[] p1,
                @CookieParam("p2") boolean[] p2,
                @CookieParam("p3") List<Integer> p3,
                @CookieParam("p4") Set<Long> p4);

        @Path("merging")
        @ListSeparator("(def)")
        String merging(
                @CookieParam("p1") String[] p1,
                @CookieParam("p2") @ListSeparator("(p2)") boolean[] p2,
                @CookieParam("p3") @ListSeparator("(p3)") List<Integer> p3,
                @CookieParam("p4") @ListSeparator("(p4)") Set<Long> p4);

    }

    @Override
    public void assertDefaultLists(String p11, String p12, boolean p21, boolean p22, Integer p31, Integer p32, Long p41, Long p42, String actual) {
        assertEquals(format("default(cookies(count:8):[p1=%s, p1=%s, p2=%s, p2=%s, p3=%s, p3=%s, p4=%s, p4=%s]) p1=%s p2=%s p3=%s p4=%s",
                p11,p12,
                p21?"myTrue":"myFalse",p22?"myTrue":"myFalse",
                p31,p32,
                p41,p42,
                p12,p22?"myTrue":"myFalse",p32,p42
        ), actual);
    }
}
