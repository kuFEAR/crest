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

package org.codegist.crest.request;

import org.codegist.crest.JsonEntityWriter;
import org.codegist.crest.XmlEntityWriter;
import org.codegist.crest.annotate.*;
import org.codegist.crest.request.common.EntityRequests;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */

@EndPoint("{crest.server.end-point}")
@Path("request/post")
@POST
public interface Posts extends EntityRequests {

    String raw();

    @Path("accept")
    @Consumes({"application/custom1", "application/custom2"})
    String accept();

    @Path("content-type")
    @Produces("application/custom")
    String contentType();

    @Path("entity-writer/xml")
    @EntityWriter(XmlEntityWriter.class)
    String xmlEntityWriter();

    @Path("entity-writer/xml/produces")
    @Produces("application/custom")
    @EntityWriter(XmlEntityWriter.class)
    String xmlEntityWriterWithProduces();

    @Path("entity-writer/json")
    @EntityWriter(JsonEntityWriter.class)
    String jsonEntityWriter();

    @Path("entity-writer/json/produces")
    @Produces("application/custom")
    @EntityWriter(JsonEntityWriter.class)
    String jsonEntityWriterWithProduces();

}
