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

package org.codegist.crest.server.stubs;

import org.apache.cxf.message.Message;

import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@Produces("text/html;charset=UTF-8")
@Path("resource/post")
public class PostsStub {

    @POST
    public String post(){
        return "post";
    }

    @POST
    @Path("form")
    public String postForm(
                @FormParam("f1") String f1,
                @FormParam("f2") String f2,
                @FormParam("f3") Float[] f3){
        return String.format("postForm f1=%s f2=%s f3=%s", f1, f2, Arrays.toString(f3));
    }

    @POST
    @Path("query")
    public String postQuery(
                @QueryParam("q1") String q1,
                @QueryParam("q2") String q2,
                @QueryParam("q3") Float[] q3){
        return String.format("postQuery q1=%s q2=%s q3=%s", q1, q2, Arrays.toString(q3));
    }

    @POST
    @Path("matrix")
    public String postMatrix(
                @MatrixParam("m1") String m1,
                @MatrixParam("m2") String m2,
                @MatrixParam("m3") Float[] m3){
        return String.format("postMatrix m1=%s m2=%s m3=%s", m1, m2, Arrays.toString(m3));
    }

    @POST
    @Path("path/{p1}/{p2:\\d{4}}/{p3}")
    public String postPath(
                @PathParam("p1") String p1,
                @PathParam("p2") String p2,
                @PathParam("p3") String p3){
        return String.format("postPath p1=%s p2=%s p3=%s", p1, p2, p3);
    }

    @POST
    @Path("header")
    public String postHeader(
                @HeaderParam("h1") String h1,
                @HeaderParam("h2") String h2,
                @HeaderParam("h3") Float[] h3){    
        return String.format("postHeader h1=%s h2=%s h3=%s", h1, h2, Arrays.toString(h3));
    }

    @POST
    @Path("cookie")
    public String postCookie(
                @HeaderParam("Cookie") List<Cookie> cookies,
                @CookieParam("c1") String c1,
                @CookieParam("c2") String c2,
                @CookieParam("c3") String c3) {
        return String.format("postCookie(header:%s) c1=%s c2=%s c3=%s" , cookies, c1, c2, c3);
    }



}
