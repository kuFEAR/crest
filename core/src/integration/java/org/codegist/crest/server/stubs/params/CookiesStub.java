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
import java.util.List;

@Produces("text/html;charset=UTF-8")
@Path("params/cookie")
public class CookiesStub {

    @GET
    public String receive(
                @HeaderParam("Cookie") List<Cookie> cookies,
                @CookieParam("p1") String p1,
                @CookieParam("p2") String p2) {
        return String.format("receive(header:%s) p1=%s p2=%s" , cookies, p1, p2);
    }

    @GET
    @Path("defaultValue")
    public String defaultValue(
                @CookieParam("p1") String p1,
                @CookieParam("p2") Integer p2) {
        return String.format("defaultValue() p1=%s p2=%s", p1, p2);
    }

    @GET
    @Path("defaultParams")
    public String defaultParams(
                @HeaderParam("Cookie") List<Cookie> cookies,
                @CookieParam("p1") String p1,
                @CookieParam("p2") String p2,
                @CookieParam("p3") String p3){
        return String.format("defaultParams(header:%s) p1=%s p2=%s p3=%s", cookies, p1, p2, p3);
    }

    @GET
    @Path("defaultLists")
    public String defaultLists(
            @HeaderParam("Cookie") List<Cookie> cookies,
            @CookieParam("p1") String p1,
            @CookieParam("p2") Boolean p2,
            @CookieParam("p3") Integer p3,
            @CookieParam("p4") Long p4){
        return String.format("defaultLists(header:%s) p1=%s p2=%s p3=%s p4=%s" , cookies, p1, p2, p3, p4);
    }

    @GET
    @Path("mergingLists")
    public String mergingLists(
            @HeaderParam("Cookie") List<Cookie> cookies,
            @CookieParam("p1") String p1,
            @CookieParam("p2") String p2,
            @CookieParam("p3") String p3,
            @CookieParam("p4") String p4){
        return String.format("mergingLists(header:%s) p1=%s p2=%s p3=%s p4=%s" , cookies, p1, p2, p3, p4);
    }

    @GET
    @Path("encodings")
    public String encodings(
            @HeaderParam("Cookie") List<Cookie> cookies,
            @CookieParam("p1") String p1,
            @CookieParam("p2") String p2){
        return String.format("encodings(header:%s) p1=%s p2=%s" , cookies, p1, p2);
    }

    @GET
    @Path("preEncoded")
    public String preEncoded(
            @HeaderParam("Cookie") List<Cookie> cookies,
            @CookieParam("p1") String p1,
            @CookieParam("p2") String p2){
        return String.format("preEncoded(header:%s) p1=%s p2=%s" , cookies, p1, p2);
    }

}
