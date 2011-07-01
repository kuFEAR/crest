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

package org.codegist.crest.param.matrixes.common;

import org.codegist.crest.annotate.*;

import java.util.Collection;

/**
 * @author laurent.gilles@codegist.org
 */
public class INullsTest<T extends INullsTest.INulls> extends org.codegist.crest.param.common.INullsTest<T> {

    public INullsTest(CRestHolder crest, Class<T> clazz) {
        super(crest, clazz);
    }

    @EndPoint("{crest.server.end-point}")
    @Path("params/matrix/null")
    @GET
    public static interface Nulls extends org.codegist.crest.param.common.INullsTest.INulls {

        String nulls(
                @MatrixParam("p1") String p1,
                @MatrixParam("p2") Collection<String> p2,
                @MatrixParam("p3") String[] p3);

        @Path("merging")
        String merging(
                @MatrixParam("p1") String p1,
                @MatrixParam("p2") @ListSeparator("(p2)") Collection<String> p2,
                @MatrixParam("p3") @ListSeparator("(p3)") String[] p3);


    }
}
