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

package org.codegist.crest.server.stubs.params.forms;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;

import static org.codegist.crest.utils.ToStrings.string;

/**
 * @author laurent.gilles@codegist.org
 */
@Produces("text/html;charset=UTF-8")
@Path("params/form/encoding")
public class EncodingsStub {

    @POST
    @Path("default")
    public String defaults(
            @FormParam("p1") String p1,
            @FormParam("p2") List<String> p2){
        return String.format("default() p1=%s p2=%s" , p1, string(p2));
    }

    @POST
    @Path("encoded")
    public String encoded(
            @FormParam("p1") String p1,
            @FormParam("p2") List<String> p2){
        return String.format("encoded() p1=%s p2=%s" , p1, string(p2));
    }

}
