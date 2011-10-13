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

package org.codegist.crest.server.stubs.params.multiparts;

import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataParam;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author laurent.gilles@codegist.org
 */
@Produces("text/html;charset=UTF-8")
@Consumes("multipart/form-data")
@Path("params/multipart/misc")
public class MiscsStub {

    @POST
    public String misc(
            @FormDataParam("p1") FormDataBodyPart p1,
            @FormDataParam("p2") FormDataBodyPart p2,
            @FormDataParam("p3") List<FormDataBodyPart> p3,
            @FormDataParam("p4") FormDataBodyPart p4,
            @FormDataParam("p5") FormDataBodyPart p5
    ) throws IOException {
        List<FormDataBodyPart> ps = new ArrayList<FormDataBodyPart>();
        ps.add(p1);
        ps.add(p2);
        ps.addAll(p3);
        ps.add(p4);
        ps.add(p5);
        String s = "misc";
        int i = 0;
        for (FormDataBodyPart part : ps) {
            s += "\n" + (++i) + "(name=" + part.getName() + ", content-type=" + part.getHeaders().getFirst("Content-Type") + ", value=" + new String(part.getValueAs(byte[].class), "utf-8") + ", filename=" + part.getContentDisposition().getFileName() + ")";
        }
        return s;
    }

}
