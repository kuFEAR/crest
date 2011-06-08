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

import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
import java.util.Arrays;
import java.util.List;

@Produces("text/html;charset=UTF-8")
@Path("params/query")
public class QueriesStub {

    @GET
    public String receive(
                @QueryParam("p1") String p1,
                @QueryParam("p2") String p2,
                @QueryParam("p3") Float[] p3){
        return String.format("receive() p1=%s p2=%s p3=%s", p1, p2, Arrays.toString(p3));
    }        

    @GET
    @Path("defaultLists")
    public String defaultLists(
            @QueryParam("p1") List<String> p1,
            @QueryParam("p2") List<Boolean> p2,
            @QueryParam("p3") List<Integer> p3,
            @QueryParam("p4") List<Long> p4){
        return String.format("defaultLists() p1=%s p2=%s p3=%s p4=%s" , p1, p2, p3, p4);
    }

    @GET
    @Path("mergingLists")
    public String mergingLists(
            @QueryParam("p1") String p1,
            @QueryParam("p2") String p2,
            @QueryParam("p3") String p3,
            @QueryParam("p4") String p4){
        return String.format("mergingLists() p1=%s p2=%s p3=%s p4=%s" , p1, p2, p3, p4);
    }

    @GET
    @Path("encodings")
    public String encodings(
            @QueryParam("p1") String p1,
            @QueryParam("p2") List<String> p2){
        return String.format("encodings() p1=%s p2=%s" , p1, p2);
    }

    @GET
    @Path("preEncoded")
    public String preEncoded(
            @QueryParam("p1") @Encoded String p1,
            @QueryParam("p2") @Encoded List<String> p2){
        return String.format("preEncoded() p1=%s p2=%s" , p1, p2);
    }

}
