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

package org.codegist.crest.resources;

import org.codegist.crest.CRestSuite;
import org.codegist.crest.annotate.*;

@EndPoint(CRestSuite.ADDRESS)
@Path("resource/option")
@OPTIONS
public interface Options {

    String option();

    @Path("query")
    String optionQuery(
                @QueryParam("q1") String q1,
                @QueryParam("q2") int q2,
                @QueryParam("q3") float[] q3);

    @Path("matrix")
    String optionMatrix(
                @MatrixParam("m1") String m1,
                @MatrixParam("m2") int m2,
                @MatrixParam("m3") float[] m3);

    @Path("path/{p1}/{p2:\\d{4}}/{p3}")
    String optionPath(
                @PathParam("p1") String p1,
                @PathParam("p2") int p2,
                @PathParam("p3") float p3);

    @Path("header")
    String optionHeader(
                @HeaderParam("h1") String h1,
                @HeaderParam("h2") int h2,
                @HeaderParam("h3") float[] h3);

    @Path("cookie")
    String optionCookie(
                @CookieParam("c1") String c1,
                @CookieParam("c2") int c2,
                @CookieParam("c3") float[] c3);

}
