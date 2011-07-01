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

package org.codegist.crest.param.paths.common;

import org.codegist.crest.annotate.*;
import org.junit.Test;

import java.util.List;
import java.util.Set;

/**
 * @author laurent.gilles@codegist.org
 */
public class ICollectionsTest<T extends ICollectionsTest.ICollections> extends org.codegist.crest.param.common.ICollectionsTest<T> {

    public ICollectionsTest(CRestHolder crest, Class<T> clazz) {
        super(crest, clazz);
    }

    @EndPoint("{crest.server.end-point}")
    @Path("params/path/collection")
    @GET
    public static interface Collections extends org.codegist.crest.param.common.ICollectionsTest.ICollections {


        @Path("merging/{p1}/{p2}/{p3}/{p4}")
        @ListSeparator("(def)")
        String merging(
                @PathParam("p1") String[] p1,
                @PathParam("p2") @ListSeparator("(p2)") boolean[] p2,
                @PathParam("p3") @ListSeparator("(p3)") List<Integer> p3,
                @PathParam("p4") @ListSeparator("(p4)") Set<Long> p4);

    }

    @Override
    @Test
    public void testDefaultLists() {
        // "N/A - Path params do not support list by default")
    }
}
