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

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.List;

@Produces("text/html;charset=UTF-8")
@Path("params/path")
public class PathsStub {

    @GET
    @Path("pattern/{p1:val-\\d{4}-[a-z]+}")
    public String pattern(@PathParam("p1") String p1){
        return String.format("pattern() p1=%s", p1);
    }

    @GET
    @Path("{p1}/{p2}")
    public String receive(
                @PathParam("p1") String p1,
                @PathParam("p2") Long p2){
        return String.format("receive() p1=%s p2=%s", p1, p2);
    }

    @GET
    @Path("dates/{p1}/{p2}")
    public String dates(
                @PathParam("p1") String p1,
                @PathParam("p2") List<String> p2){
        return String.format("dates() p1=%s p2=%s", p1, p2);
    }


    @GET
    @Path("nullsMerging")
    public String nullsMerging(
                @PathParam("p1") String p1,
                @PathParam("p2") String p2,
                @PathParam("p3") String p3){
        return String.format("nullsMerging() p1=%s p2=%s p3=%s", p1, p2, p3);
    }

    @GET
    @Path("defaultValue/{p1}/{p2}")
    public String defaultValue(
            @PathParam("p1") String p1,
            @PathParam("p2") Integer p2) {
        return String.format("defaultValue() p1=%s p2=%s", p1, p2);
    }

    @GET
    @Path("defaultParams/{p1}/{p2}/{p3}/{p4}")
    public String defaultParams(
                @PathParam("p1") String p1,
                @PathParam("p2") String p2,
                @PathParam("p3") String p3,
                @PathParam("p4") String p4){
        return String.format("defaultParams() p1=%s p2=%s p3=%s p4=%s", p1, p2, p3, p4);
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
            @PathParam("p1") String p1,
            @PathParam("p2") String  p2){
        return String.format("preEncoded() p1=%s p2=%s" , p1, p2);
    }
    

}
