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

package org.codegist.crest.server.stubs;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;
import org.apache.cxf.message.Message;
import org.codegist.common.io.IOs;

import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@Produces("text/html;charset=UTF-8")
@Path("resource/post/entity-writer")
public class EntityWritersStub {

    @POST
    @Path("form/xml")
    @Consumes("application/xml")
    public String postFormAsXml(InputStream msg) throws IOException {
        return IOs.toString(msg);
    }

    @POST
    @Path("form/json")
    @Consumes("application/json")
    public String postFormAsJson(InputStream msg) throws IOException {
        return IOs.toString(msg);
    }


    @POST
    @Path("form/multipart")
    @Multipart
    public String multipart(MultipartBody msg) throws IOException {
        String s = "multipart";
        int i = 0, max =  msg.getAllAttachments().size() - 1;
        for(Attachment at : msg.getAllAttachments()){
            if(i == max) break;
            s+="\n"+ (++i) +"(name=" + at.getContentDisposition().getParameter("name") + ", content-type=" + at.getContentType() + ", value=" + at.getObject(String.class) + ", filename=" + at.getContentDisposition().getParameter("filename")+ ")";
        }
        return s;
    }

}
