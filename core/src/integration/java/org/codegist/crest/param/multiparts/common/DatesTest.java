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

package org.codegist.crest.param.multiparts.common;

import org.codegist.crest.CRest;
import org.codegist.crest.annotate.EndPoint;
import org.codegist.crest.annotate.MultiPartParam;
import org.codegist.crest.annotate.POST;
import org.codegist.crest.annotate.Path;
import org.codegist.crest.param.common.IDatesTest;

import java.util.Date;

/**
 * @author laurent.gilles@codegist.org
 */
public class DatesTest extends IDatesTest<DatesTest.Dates> {

    public DatesTest(CRest crest) {
        super(crest, Dates.class);
    }

    @EndPoint(ADDRESS)
    @Path("params/multipart/date")
    @POST
    public static interface Dates extends IDatesTest.IDates {

        String date(
                @MultiPartParam("p1") Date p1,
                @MultiPartParam("p2") Date... p2);

    }
}
