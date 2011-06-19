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

package org.codegist.crest.server.stubs.params.multiparts;

import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.io.UnsupportedEncodingException;

import static org.codegist.crest.server.stubs.params.multiparts.MultiParts.toResponseString;

/**
 * @author laurent.gilles@codegist.org
 */
@Produces("text/html;charset=UTF-8")
@Path("params/multipart/collection")
public class CollectionsStub {


    @POST
    @Path("default")
    public String defaults(MultipartBody msg)throws UnsupportedEncodingException {
        return toResponseString("default", msg);
    }

    @POST
    @Path("merging")
    public String merging(MultipartBody msg) throws UnsupportedEncodingException{
        return toResponseString("merging", msg);
    }
}
