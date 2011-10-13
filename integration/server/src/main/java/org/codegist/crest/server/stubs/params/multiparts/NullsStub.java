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
import org.codegist.crest.server.utils.ToStrings;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.io.UnsupportedEncodingException;
import java.util.List;

import static java.lang.String.format;
import static org.codegist.crest.server.utils.ToStrings.string;

/**
 * @author laurent.gilles@codegist.org
 */
@Produces("text/html;charset=UTF-8")
@Consumes("multipart/form-data")
@Path("params/multipart/null")
public class NullsStub {

    @POST
    public String nulls(
            @FormDataParam("p1") FormDataBodyPart p1,
            @FormDataParam("p2") List<FormDataBodyPart> p2,
            @FormDataParam("p3") List<FormDataBodyPart> p3) throws UnsupportedEncodingException {
        return format("null() p1=%s p2=%s p3=%s", string(p1), ToStrings.stringMulti(p2), ToStrings.stringMulti(p3));
    }

    @POST
    @Path("merging")
    public String merging(
            @FormDataParam("p1") FormDataBodyPart p1,
            @FormDataParam("p2") FormDataBodyPart p2,
            @FormDataParam("p3") FormDataBodyPart p3
    ) throws UnsupportedEncodingException {
        return format("merging() p1=%s p2=%s p3=%s", string(p1), string(p2), string(p3));
    }
}
