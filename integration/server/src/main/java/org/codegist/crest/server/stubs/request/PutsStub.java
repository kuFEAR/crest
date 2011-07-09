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

import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;

import static java.lang.String.format;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */

@Produces("text/html;charset=UTF-8")
@Path("io/put")
public class PutsStub {

    @PUT
    public String raw(
            @HeaderParam("Content-Type") List<String> contentTypes,
            @HeaderParam("Accept") List<String> accepts
    ) {
        return asResponse("raw", contentTypes, accepts);
    }

    @PUT
    @Produces("application/custom1")
    @Path("accept")
    public String accept(
            @HeaderParam("Content-Type") List<String> contentTypes,
            @HeaderParam("Accept") List<String> accepts
    ) {
        return asResponse("accept", contentTypes, accepts);
    }

    @PUT
    @Path("content-type")
    public String contentType(
            @HeaderParam("Content-Type") List<String> contentTypes,
            @HeaderParam("Accept") List<String> accepts
    ) {
        return asResponse("contentType", contentTypes, accepts);
    }


    @PUT
    @Path("entity-writer/xml")
    public String xmlEntity(
            @HeaderParam("Content-Type") List<String> contentTypes,
            @HeaderParam("Accept") List<String> accepts
    ) {
        return asResponse("xmlEntity", contentTypes, accepts);
    }

    @PUT
    @Path("entity-writer/xml/produces")
    @Produces("application/custom")
    public String xmlEntityWithProduces(
            @HeaderParam("Content-Type") List<String> contentTypes,
            @HeaderParam("Accept") List<String> accepts
    ) {
        return asResponse("xmlEntityWithProduces", contentTypes, accepts);
    }

    @PUT
    @Path("entity-writer/json")
    public String jsonEntity(
            @HeaderParam("Content-Type") List<String> contentTypes,
            @HeaderParam("Accept") List<String> accepts
    ) {
        return asResponse("jsonEntity", contentTypes, accepts);
    }

    @PUT
    @Path("entity-writer/json/produces")
    @Produces("application/custom")
    public String jsonEntityWithProduces(
            @HeaderParam("Content-Type") List<String> contentTypes,
            @HeaderParam("Accept") List<String> accepts
    ) {
        return asResponse("jsonEntityWithProduces", contentTypes, accepts);
    }

    @PUT
    @Path("entity-writer/multipart")
    public String multipartEntity(
            @HeaderParam("Content-Type") List<String> contentTypes,
            @HeaderParam("Accept") List<String> accepts
    ) {
        return asResponse("multipartEntity", contentTypes, accepts);
    }

    @PUT
    @Path("entity-writer/multipart/produces")
    @Produces("application/custom")
    public String multipartEntityWithProduces(
            @HeaderParam("Content-Type") List<String> contentTypes,
            @HeaderParam("Accept") List<String> accepts
    ) {
        return asResponse("multipartEntityWithProduces", contentTypes, accepts);
    }

    private static String asResponse(String from, List<String> contentTypes, List<String> accepts) {
        return format("%s() content-type-header=%s accepts-header=%s", from, contentTypes, accepts);
    }
}
