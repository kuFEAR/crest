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

package org.codegist.crest.entity;

import org.codegist.crest.*;
import org.codegist.crest.annotate.*;
import org.codegist.crest.annotate.EntityWriter;

import java.io.File;
import java.io.InputStream;


@EndPoint(BaseCRestTest.ADDRESS)
@Path("entity")
@POST
public interface EntityWriters {

    @Path("xml")
    @EntityWriter(XmlEntityWriter.class)
    String postFormAsXml(
                @FormParam("f1") String q1,
                @FormParam("f2") int q2,
                @FormParam("f3") float[] q3);

    @Path("json")
    @EntityWriter(JsonEntityWriter.class)
    String postFormAsJson(
                @FormParam("f1") String q1,
                @FormParam("f2") int q2,
                @FormParam("f3") float[] q3);

    @Path("multipart")
    @EntityWriter(MultiPartEntityWriter.class) // TODO make it an annotation (@MultiPartEntity @JsonEntity @XmlEntity)
    String multipart(
                @MultiPartParam("p1") String q1,
                @MultiPartParam(value="p2", contentType = "text/html", fileName = "my-file") int q2,
                @MultiPartParam("p3") float[] q3,
                @MultiPartParam("p4") InputStream in,
                @MultiPartParam("p5") File file);

}
