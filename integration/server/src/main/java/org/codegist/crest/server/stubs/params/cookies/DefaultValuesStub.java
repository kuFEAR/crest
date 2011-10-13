/*
 * Copyright 2011 CodeGist.org
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
@Path("params/cookie/default-value")
public class DefaultValuesStub {

    @GET
    @Path("value")
    public String value(
            @HeaderParam("Cookie") List<String> cookies,
            @CookieParam("p1") String p1,
            @CookieParam("p2") Integer p2,
            @CookieParam("p01") String p01,
            @CookieParam("p02") String p02,
            @CookieParam("p03") String p03) {
        return String.format("value(%s) p1=%s p2=%s p01=%s p02=%s p03=%s", stringCookie(cookies, 5), p1, p2,  p01, p02, p03);
    }

    @GET
    @Path("param")
    public String param(
            @HeaderParam("Cookie") List<String> cookies,
            @CookieParam("p1") String p1,
            @CookieParam("p2") String p2,
            @CookieParam("p3") String p3,
            @CookieParam("p01") String p01,
            @CookieParam("p02") String p02,
            @CookieParam("p03") String p03) {
        return String.format("param(%s) p1=%s p2=%s p3=%s p01=%s p02=%s p03=%s", stringCookie(cookies, -1), p1, p2, p3,  p01, p02, p03);
    }
}
