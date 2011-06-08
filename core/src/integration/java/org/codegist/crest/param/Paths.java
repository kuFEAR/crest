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

package org.codegist.crest.param;

import org.codegist.crest.BaseCRestTest;
import org.codegist.crest.CRestSuite;
import org.codegist.crest.annotate.*;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
@EndPoint(BaseCRestTest.ADDRESS)
@Path("params/path")

@GET
public interface Paths extends Params {

    @Path("{p1}/{p2:\\d{4}}/{p3}")
    String send(
            @PathParam("p1") String p1,
            @PathParam("p2") int p2,
            @PathParam("p3") @ListSeparator("-") float[] p3);


    @Path("mergingLists/{p1}/{p2}/{p3}/{p4}")
    @ListSeparator("(def)")
    String mergingLists(
            @PathParam("p1") String[] p1,
            @PathParam("p2") @ListSeparator("(p2)") boolean[] p2,
            @PathParam("p3") @ListSeparator("(p3)") List<Integer> p3,
            @PathParam("p4") @ListSeparator("(p4)") Set<Long> p4);

    @Path("encodings/{p1}/{p2}")
    String encodings(
            @PathParam("p1") String p1,
            @PathParam("p2") @ListSeparator("(p2)") Collection<String> p2);

    @Path("preEncoded/{p1}/{p2}")
    @Encoded
    String preEncoded(
            @PathParam("p1") String p1,
            @PathParam("p2") @ListSeparator("(p2)") Collection<String> p2);
}
