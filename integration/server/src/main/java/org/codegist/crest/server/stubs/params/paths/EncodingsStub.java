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
@Path("params/path/encoding")
public class EncodingsStub {

    @GET
    @Path("default/{p1}/{p2}")
    public String defaults(
            @PathParam("p1") String p1,
            @PathParam("p2") String p2) {
        return String.format("default() p1=%s p2=%s", p1, p2);
    }

    @GET
    @Path("encoded/{p1}/{p2}")
    public String encoded(
            @PathParam("p1") String p1,
            @PathParam("p2") String p2) {
        return String.format("encoded() p1=%s p2=%s", p1, p2);
    }

}
