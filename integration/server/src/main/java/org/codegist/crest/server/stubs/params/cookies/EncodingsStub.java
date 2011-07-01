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

package org.codegist.crest.server.stubs.params.cookies;

import javax.ws.rs.*;
import java.util.List;

import static org.codegist.crest.server.utils.ToStrings.stringCookie;


/**
 * @author laurent.gilles@codegist.org
 */
@Produces("text/html;charset=UTF-8")
@Path("params/cookie/encoding")
public class EncodingsStub {

    @GET
    @Path("default")
    public String defaults(
            @HeaderParam("Cookie") List<String> cookies,
            @CookieParam("p1") String p1,
            @CookieParam("p2") String p2) {
        return String.format("default(%s) p1=%s p2=%s", stringCookie(cookies, -1), p1, p2);
    }

    @GET
    @Path("encoded")
    public String encoded(
            @HeaderParam("Cookie") List<String> cookies,
            @CookieParam("p1") String p1,
            @CookieParam("p2") String p2) {
        return String.format("encoded(%s) p1=%s p2=%s", stringCookie(cookies, -1), p1, p2);
    }



}
