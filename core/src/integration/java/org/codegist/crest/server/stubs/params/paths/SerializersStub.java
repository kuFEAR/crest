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

import static java.lang.String.format;

/**
 * @author laurent.gilles@codegist.org
 */
@Produces("text/html;charset=UTF-8")
@Path("params/path/serializer")
public class SerializersStub {


    @GET
    @Path("default/{p1}/{p2}/{p3}")
    public String defaults(
            @PathParam("p1") String p1,
            @PathParam("p2") String p2,
            @PathParam("p3") String p3){
        return format("default() p1=%s p2=%s p3=%s" , p1, p2, p3);
    }

    @GET
    @Path("configured/{p1}/{p2}/{p3}")
    public String configured(
            @PathParam("p1") String p1,
            @PathParam("p2") String p2,
            @PathParam("p3") String p3) {
        return format("configured() p1=%s p2=%s p3=%s" , p1, p2, p3);
    }

    @GET
    @Path("null/{p1}/{p2}/{p3}")
    public String nulls(
            @PathParam("p1") String p1,
            @PathParam("p2") String p2,
            @PathParam("p3") String p3) {
        return format("null() p1=%s p2=%s p3=%s" ,  p1, p2, p3);
    }
}
