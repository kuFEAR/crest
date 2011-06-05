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

package org.codegist.crest.collection;

import org.codegist.crest.CRestSuite;
import org.codegist.crest.annotate.*;

import java.util.List;
import java.util.Set;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */

@EndPoint(CRestSuite.ADDRESS)
@Path("collection/{service.path}")
public interface Collections {

    @GET
    @Path("query")
    String query(
            @QueryParam("q1") String[] q1,
            @QueryParam("q2") boolean[] q2,
            @QueryParam("q3") List<Integer> q3,
            @QueryParam("q4") Set<Long> q4);

    @GET
    @Path("matrix")
    String matrix(
            @MatrixParam("m1") String[] m1,
            @MatrixParam("m2") boolean[] m2,
            @MatrixParam("m3") List<Integer> m3,
            @MatrixParam("m4") Set<Long> m4);

    @POST
    @Path("form")
    String form(
            @FormParam("f1") String[] f1,
            @FormParam("f2") boolean[] f2,
            @FormParam("f3") List<Integer> f3,
            @FormParam("f4") Set<Long> f4);

    @GET
    @Path("path/{p1}/{p2}/{p3}/{p4}")
    String path(
            @PathParam("p1") String[] p1,
            @PathParam("p2") boolean[] p2,
            @PathParam("p3") List<Integer> p3,
            @PathParam("p4") Set<Long> p4);

    @GET
    @Path("header")
    String header(
            @HeaderParam("h1") String[] h1,
            @HeaderParam("h2") boolean[] h2,
            @HeaderParam("h3") List<Integer> h3,
            @HeaderParam("h4") Set<Long> h4);

    @GET
    @Path("cookie")
    String cookie(
            @CookieParam("c1") String[] c1,
            @CookieParam("c2") boolean[] c2,
            @CookieParam("c3") List<Integer> c3,
            @CookieParam("c4") Set<Long> c4);

}
