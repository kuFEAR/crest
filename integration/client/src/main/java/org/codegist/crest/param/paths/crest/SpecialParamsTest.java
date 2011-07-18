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

package org.codegist.crest.param.paths.crest;

import org.codegist.crest.annotate.*;
import org.codegist.crest.annotate.GET;
import org.codegist.crest.annotate.PathParam;
import org.codegist.crest.param.paths.common.ISpecialParamsTests;

import java.io.InputStream;
import java.io.Reader;

/**
 * @author laurent.gilles@codegist.org
 */
public class SpecialParamsTest extends ISpecialParamsTests<SpecialParamsTest.SpecialParams> {

    public SpecialParamsTest(CRestHolder crest) {
        super(crest, SpecialParams.class);
    }

    @EndPoint("{crest.server.end-point}")
    @Path("params/path/special")
    @GET
    public static interface SpecialParams extends ISpecialParamsTests.ISpecialParams {

        @Path("inputStream/{p1}/{p2}")
        String inputStream(
                @PathParam("p1") InputStream p1,
                @PathParam("p2") @ListSeparator("-") InputStream[] p2);

        @Path("reader/{p1}/{p2}")
        String reader(
                @PathParam("p1") Reader p1,
                @PathParam("p2") @ListSeparator("-") Reader[] p2);
    }

}
