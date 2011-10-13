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

package org.codegist.crest.server.stubs.annotate;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import static java.lang.String.format;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
@Path("annotate/custom")
@Produces("text/plain") // just for coverage
public class CustomAnnotationsStub {

    @GET
    @Path("unknown")
    public String unknown(@QueryParam("p0") String p) {
        return format("unknown() p0=%s", p);
    }

    @GET
    @Path("custom")
    public String custom(
            @QueryParam("p0") String p,
            @QueryParam("p1") String p1,
            @QueryParam("p2") String p2,
            @QueryParam("p3") String p3) {
        return format("custom() p0=%s p1=%s p2=%s p3=%s", p, p1, p2, p3);
    }
}
