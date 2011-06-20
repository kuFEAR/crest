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

package org.codegist.crest.server.stubs.request;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;

import static java.lang.String.format;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */

@Produces("text/html;charset=UTF-8")
@Path("request/get")
public class GetsStub {

    @GET
    public String raw(
            @HeaderParam("Content-Type") List<String> contentTypes,
            @HeaderParam("Accept") List<String> accepts
    ) {
        return asResponse("raw", contentTypes, accepts);
    }

    @GET
    @Produces("application/custom1")
    @Path("accept")
    public String accept(
            @HeaderParam("Content-Type") List<String> contentTypes,
            @HeaderParam("Accept") List<String> accepts
    ) {
        return asResponse("accept", contentTypes, accepts);
    }

    @GET
    @Path("content-type")
    public String contentType(
            @HeaderParam("Content-Type") List<String> contentTypes,
            @HeaderParam("Accept") List<String> accepts
    ) {
        return asResponse("contentType", contentTypes, accepts);
    }

    private static String asResponse(String from, List<String> contentTypes, List<String> accepts) {
        return format("%s() content-type-header=%s accepts-header=%s", from, contentTypes, accepts);
    }
}
