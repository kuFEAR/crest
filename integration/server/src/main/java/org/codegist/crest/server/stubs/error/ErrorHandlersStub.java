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

package org.codegist.crest.server.stubs.error;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import static java.lang.String.valueOf;

/**
 * @author laurent.gilles@codegist.org
 */

@Path("error")
public class ErrorHandlersStub {

    private int failFor = 0;
    private int count = 0;

    @GET
    @Path("call-count")
    public String callCount() {
        return valueOf(count);
    }

    @GET
    @Path("reset")
    public String reset() {
        count = 0;
        return "ok";
    }

    @GET
    @Path("fail-for")
    public String failFor(@QueryParam("time") int times) {
        this.failFor = times;
        return "ok";
    }

    @GET
    @Path("retry")
    public Response retry(@QueryParam("value") String value) {
        if(++count != failFor) {
            return Response.status(418).build();
        }else{
            return Response.ok(value).build();
        }
    }

    @GET
    @Path("error-handler")
    public Response handleError() {
        return Response.status(418).build();
    }

    @GET
    @Path("retry-custom")
    public Response retryCustom() {
        count++;
        return Response.status(418).build();
    }

    @GET
    @Path("http-code")
    public Response httpCode(@QueryParam("code") int httpCode) {
        return Response.status(httpCode).entity("ok").build();
    }

}
