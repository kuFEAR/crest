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

package org.codegist.crest.request.crest;

import org.codegist.crest.annotate.*;
import org.codegist.crest.request.common.EntityRequests;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */

@EndPoint("{crest.server.end-point}")
@Path("request/post")
@POST             
public interface Posts extends EntityRequests {

    @POST
    String raw();

    @Path("accept")
    @Consumes({"application/custom1", "application/custom2"})
    String accept();

    @Path("content-type")
    @Produces("application/custom")
    String contentType();

    @Path("entity-writer/xml")
    @XmlEntity
    String xmlEntity();

    @Path("entity-writer/xml/produces")
    @Produces("application/custom")
    @XmlEntity
    String xmlEntityWithProduces();

    @Path("entity-writer/json")
    @JsonEntity
    String jsonEntity();

    @Path("entity-writer/json/produces")
    @Produces("application/custom")
    @JsonEntity
    String jsonEntityWithProduces();

    @Path("entity-writer/multipart")
    @MultiPartParam(value="p",defaultValue = "some-val") // jersey doesn't like multipart entity with nothing in it..
    String multipartEntity();

    @Path("entity-writer/multipart/produces")
    @Produces("application/custom")
    @MultiPartParam(value="p",defaultValue = "some-val") // jersey doesn't like multipart entity with nothing in it..
    String multipartEntityWithProduces();
}
