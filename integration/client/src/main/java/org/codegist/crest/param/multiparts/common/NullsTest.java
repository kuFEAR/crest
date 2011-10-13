/*
 * Copyright 2011 CodeGist.org
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

package org.codegist.crest.param.multiparts.common;

import org.codegist.crest.annotate.*;
import org.codegist.crest.param.common.INullsTest;

import java.util.Collection;

/**
 * @author laurent.gilles@codegist.org
 */
public class NullsTest extends INullsTest<NullsTest.Nulls> {

    public NullsTest(CRestHolder crest) {
        super(crest, Nulls.class);
    }

    @EndPoint("{crest.server.end-point}")
    @Path("params/multipart/null")
    @POST
    public static interface Nulls extends INullsTest.INulls {

        @MultiPartParam(value="p0", defaultValue = "some val") // default value so that jersey accepts the requests
        String nulls(
                @MultiPartParam("p1") String p1,
                @MultiPartParam("p2") Collection<String> p2,
                @MultiPartParam("p3") String[] p3);

        @Path("merging")
        @MultiPartParam(value="p0", defaultValue = "some val") // default value so that jersey accepts the requests
        String merging(
                @MultiPartParam("p1") String p1,
                @MultiPartParam("p2") @ListSeparator("(p2)") Collection<String> p2,
                @MultiPartParam("p3") @ListSeparator("(p3)") String[] p3);

    }
}
