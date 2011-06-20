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

package org.codegist.crest.server.stubs.params.matrixes;

import javax.ws.rs.GET;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * @author laurent.gilles@codegist.org
 */
@Produces("text/html;charset=UTF-8")
@Path("params/matrix/null")
public class NullsStub {

    @GET
    public String nulls(
            @MatrixParam("p1") String p1,
            @MatrixParam("p2") String p2,
            @MatrixParam("p3") String p3) {
        return String.format("null() p1=%s p2=%s p3=%s", p1, p2, p3);
    }

    @GET
    @Path("merging")
    public String merging(
            @MatrixParam("p1") String p1,
            @MatrixParam("p2") String p2,
            @MatrixParam("p3") String p3) {
        return String.format("merging() p1=%s p2=%s p3=%s", p1, p2, p3);
    }
}
