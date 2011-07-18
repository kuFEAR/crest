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

package org.codegist.crest.server.stubs.params.paths;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 * @author laurent.gilles@codegist.org
 */
@Produces("text/html;charset=UTF-8")
@Path("params/path/default-value")
public class DefaultValuesStub {

    @GET
    @Path("value/{p1}/{p2}/{p01}/{p02}/{p03}")
    public String value(
            @PathParam("p1") String p1,
            @PathParam("p2") String p2,
            @PathParam("p01") String p01,
            @PathParam("p02") String p02,
            @PathParam("p03") String p03) {
        return String.format("value() p1=%s p2=%s p01=%s p02=%s p03=%s", p1, p2, p01, p02, p03);
    }

    @GET
    @Path("param/{p1}/{p2}/{p3}/{p01}/{p02}/{p03}")
    public String param(
            @PathParam("p1") String p1,
            @PathParam("p2") String p2,
            @PathParam("p3") String p3,
            @PathParam("p01") String p01,
            @PathParam("p02") String p02,
            @PathParam("p03") String p03) {
        return String.format("param() p1=%s p2=%s p3=%s p01=%s p02=%s p03=%s", p1, p2, p3, p01, p02, p03);
    }
}
