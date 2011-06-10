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
import org.codegist.crest.annotate.*;
import org.codegist.crest.param.common.Params;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
@EndPoint(BaseCRestTest.ADDRESS)
@Path("params/header")
@GET
public interface Headers extends Params {

    String send(
            @HeaderParam("p1") String p1,
            @HeaderParam("p2") int p2);

    @Path("defaultValue")
    String defaultValue(
            @HeaderParam(value="p1", defaultValue = "default-p1") String p1,
            @HeaderParam(value="p2", defaultValue = "123") Integer p2);   

    @HeaderParam(value="p2", defaultValue = "p2-val")
    @HeaderParams({
            @HeaderParam(value="p1", defaultValue = "p1-val"),
            @HeaderParam(value="p3", defaultValue = "p3-val")
    })
    @Path("defaultParams")
    String defaultParams(@HeaderParam("p1") String p1);

    @Path("defaultLists")
    String defaultLists(
            @HeaderParam("p1") String[] p1,
            @HeaderParam("p2") boolean[] p2,
            @HeaderParam("p3") List<Integer> p3,
            @HeaderParam("p4") Set<Long> p4);       

    @Path("nulls")
    String nulls(
            @HeaderParam("p1") String p1,
            @HeaderParam("p2") Collection<String> p2,
            @HeaderParam("p3") String[] p3);

    @Path("nullsMerging")
    String nullsMerging(
            @HeaderParam("p1") String p1,
            @HeaderParam("p2") @ListSeparator("(p2)") Collection<String> p2,
            @HeaderParam("p3") @ListSeparator("(p3)") String[] p3);

    @Path("mergingLists")
    @ListSeparator("(def)")
    String mergingLists(
            @HeaderParam("p1") String[] p1,
            @HeaderParam("p2") @ListSeparator("(p2)") boolean[] p2,
            @HeaderParam("p3") @ListSeparator("(p3)") List<Integer> p3,
            @HeaderParam("p4") @ListSeparator("(p4)") Set<Long> p4);

    @Path("encodings")
    String encodings(
            @HeaderParam("p1") String p1,
            @HeaderParam("p2") Collection<String> p2);

    @Path("preEncoded")
    @Encoded 
    String preEncoded(
            @HeaderParam("p1") String p1,
            @HeaderParam("p2") Collection<String> p2);

}
