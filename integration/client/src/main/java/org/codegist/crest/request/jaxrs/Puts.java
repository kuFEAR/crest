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

package org.codegist.crest.request.jaxrs;

import org.codegist.crest.annotate.*;
import org.codegist.crest.annotate.MultiPartEntity;
import org.codegist.crest.annotate.MultiPartParam;
import org.codegist.crest.request.common.EntityRequests;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */

@EndPoint("{crest.server.end-point}")
@Path("io/put")
public interface Puts extends EntityRequests {

    @PUT
    String raw();

    @PUT
    @Path("accept")
    @Produces({"application/custom1", "application/custom2"})
    String accept();

    @PUT
    @Path("content-type")
    @Consumes("application/custom")
    String contentType();

    @PUT
    @Path("entity-writer/xml")
    @XmlEntity
    String xmlEntity();

    @PUT
    @Path("entity-writer/xml/produces")
    @Consumes("application/custom")
    @XmlEntity
    String xmlEntityWithProduces();

    @PUT
    @Path("entity-writer/json")
    @JsonEntity
    String jsonEntity();

    @PUT
    @Path("entity-writer/json/produces")
    @Consumes("application/custom")
    @JsonEntity
    String jsonEntityWithProduces();

    @PUT
    @Path("entity-writer/multipart")
    @MultiPartEntity
    @MultiPartParam(value="p",defaultValue = "some-val") // jersey doesn't like multipart entity with nothing in it..
    String multipartEntity();

    @PUT
    @Path("entity-writer/multipart/produces")
    @Consumes("application/custom")
    @MultiPartEntity
    @MultiPartParam(value="p",defaultValue = "some-val") // jersey doesn't like multipart entity with nothing in it..
    String multipartEntityWithProduces();
}
