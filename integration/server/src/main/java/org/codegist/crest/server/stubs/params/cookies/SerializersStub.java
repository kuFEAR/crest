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
import javax.ws.rs.core.Cookie;
import java.util.List;

import static java.lang.String.format;
import static org.codegist.crest.server.utils.ToStrings.string;

/**
 * @author laurent.gilles@codegist.org
 */
@Produces("text/html;charset=UTF-8")
@Path("params/cookie/serializer")
public class SerializersStub {


    @GET
    @Path("default")
    public String defaults(@HeaderParam("Cookie") List<Cookie> cookies) {
        return format("default(%s) %s=%s %s=%s %s=%s", string(cookies, 3),
                cookies.get(0).getName(),
                cookies.get(0).getValue(),
                cookies.get(1).getName(),
                cookies.get(1).getValue(),
                cookies.get(2).getName(),
                cookies.get(2).getValue()
        );
    }

    @GET
    @Path("configured")
    public String configured(@HeaderParam("Cookie") List<Cookie> cookies) {
        return format("configured(%s) %s=%s %s=%s %s=%s", string(cookies, 3),
                cookies.get(0).getName(),
                cookies.get(0).getValue(),
                cookies.get(1).getName(),
                cookies.get(1).getValue(),
                cookies.get(2).getName(),
                cookies.get(2).getValue()
        );
    }

    @GET
    @Path("null")
    public String nulls(
            @HeaderParam("Cookie") List<Cookie> cookies,
            @CookieParam("p1") Cookie p1,
            @CookieParam("p2") Cookie p2,
            @CookieParam("p3") Cookie p3) {
        return format("null(%s) p1=%s p2=%s p3=%s", string(cookies, 0), p1,p2,p3);
    }
}
