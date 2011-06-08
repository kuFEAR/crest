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

package org.codegist.crest.server.stubs.params;

import org.codegist.crest.annotate.*;

import javax.ws.rs.*;
import javax.ws.rs.Encoded;
import javax.ws.rs.GET;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.Arrays;
import java.util.List;

@Produces("text/html;charset=UTF-8")
@Path("params/path")
public class PathsStub {

    @GET
    @Path("{p1}/{p2:\\d{4}}/{p3}")
    public String receive(
                @PathParam("p1") String p1,
                @PathParam("p2") Long p2,
                @PathParam("p3") String p3){
        return String.format("receive() p1=%s p2=%s p3=%s", p1, p2, p3);
    }
    

    @GET
    @Path("mergingLists/{p1}/{p2}/{p3}/{p4}")
    public String mergingLists(
            @PathParam("p1") String p1,
            @PathParam("p2") String p2,
            @PathParam("p3") String p3,
            @PathParam("p4") String p4){
        return String.format("mergingLists() p1=%s p2=%s p3=%s p4=%s" , p1, p2, p3, p4);
    }

    @GET
    @Path("encodings/{p1}/{p2}")
    public String encodings(
            @PathParam("p1") String p1,
            @PathParam("p2") String p2){
        return String.format("encodings() p1=%s p2=%s" , p1, p2);
    }

    @GET
    @Path("preEncoded/{p1}/{p2}")
    public String preEncoded(
            @PathParam("p1") @Encoded String p1,
            @PathParam("p2") @Encoded String  p2){
        return String.format("preEncoded() p1=%s p2=%s" , p1, p2);
    }
    

}
