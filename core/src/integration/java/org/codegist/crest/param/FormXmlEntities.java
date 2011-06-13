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
import org.codegist.crest.XmlEntityWriter;
import org.codegist.crest.annotate.*;
import org.codegist.crest.param.common.Params;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
@EndPoint(BaseCRestTest.ADDRESS)
@Path("entity/xml")
@POST
@EntityWriter(XmlEntityWriter.class)
public interface FormXmlEntities extends Params {

    String send(
            @FormParam("p1") String p1,
            @FormParam("p2") int p2);

    String dates(
            @FormParam("p1") Date p1,
            @FormParam("p2") Date... p2);

    String defaultValue(
            @FormParam(value="p1", defaultValue = "default-p1") String p1,
            @FormParam(value="p2", defaultValue = "123") Integer p2);

    @FormParam(value="p2", defaultValue = "p2-val")
    @FormParams({
            @FormParam(value="p1", defaultValue = "p1-val"),
            @FormParam(value="p3", defaultValue = "p3-val")
    })
    String defaultParams(@FormParam("p1") String p1);

    String defaultLists(
            @FormParam("p1") String[] p1,
            @FormParam("p2") boolean[] p2,
            @FormParam("p3") List<Integer> p3,
            @FormParam("p4") Set<Long> p4);

    String nulls(
            @FormParam("p1") String p1,
            @FormParam("p2") Collection<String> p2,
            @FormParam("p3") String[] p3);

    String nullsMerging(
            @FormParam("p1") String p1,
            @FormParam("p2") @ListSeparator("(p2)") Collection<String> p2,
            @FormParam("p3") @ListSeparator("(p3)") String[] p3);

    @ListSeparator("(def)")
    String mergingLists(
            @FormParam("p1") String[] p1,
            @FormParam("p2") @ListSeparator("(p2)") boolean[] p2,
            @FormParam("p3") @ListSeparator("(p3)") List<Integer> p3,
            @FormParam("p4") @ListSeparator("(p4)") Set<Long> p4);

    String encodings(
            @FormParam("p1") String p1,
            @FormParam("p2") Collection<String> p2);

    @Encoded
    String preEncoded(
            @FormParam("p1") String p1,
            @FormParam("p2") Collection<String> p2);

}
