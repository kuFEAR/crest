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

package org.codegist.crest.server.stubs.collection;

import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
import java.util.List;
import java.util.Set;

@Produces("text/html;charset=UTF-8")
@Path("collection/merging")
public class CollectionsMergingStub {


    @GET
    @Path("query")
    public String query(
            @QueryParam("q1") String q1,
            @QueryParam("q2") String q2,
            @QueryParam("q3") String q3,
            @QueryParam("q4") String q4) {
        return String.format("query q1=%s q2=%s q3=%s q4=%s", q1, q2, q3, q4) ;
    }

    @GET
    @Path("matrix")
    public String matrix(
            @MatrixParam("m1") String m1,
            @MatrixParam("m2") String m2,
            @MatrixParam("m3") String m3,
            @MatrixParam("m4") String m4) {
        return String.format("matrix m1=%s m2=%s m3=%s m4=%s", m1, m2, m3, m4) ;
    }

    @POST
    @Path("form")
    public String form(
            @FormParam("f1") String f1,
            @FormParam("f2") String f2,
            @FormParam("f3") String f3,
            @FormParam("f4") String f4) {
        return String.format("form f1=%s f2=%s f3=%s f4=%s", f1, f2, f3, f4) ;
    }

    @GET
    @Path("path/{p1}/{p2}/{p3}/{p4}")
    public String path(
            @PathParam("p1") String p1,
            @PathParam("p2") String p2,
            @PathParam("p3") String p3,
            @PathParam("p4") String p4) {
        return String.format("path p1=%s p2=%s p3=%s p4=%s", p1, p2, p3, p4) ;
    }

    @GET
    @Path("header")
    public String header(
            @HeaderParam("h1") String h1,
            @HeaderParam("h2") String h2,
            @HeaderParam("h3") String h3,
            @HeaderParam("h4") String h4){
        return String.format("header h1=%s h2=%s h3=%s h4=%s", h1, h2, h3, h4) ;
    }

    @GET
    @Path("cookie")
    public String cookie(
            @HeaderParam("Cookie") List<Cookie> cookies,
            @CookieParam("c1") String c1,
            @CookieParam("c2") String c2,
            @CookieParam("c3") String c3,
            @CookieParam("c4") String c4){
        return String.format("cookie(header:%s) c1=%s c2=%s c3=%s c4=%s", cookies, c1, c2, c3, c4) ;
    }

}
