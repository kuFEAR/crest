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

package org.codegist.crest.server.stubs.security;

import com.sun.jersey.api.core.HttpRequestContext;
import org.codegist.crest.server.utils.OAuths;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;

import static java.lang.String.format;

/**
 * @author laurent.gilles@codegist.org
 */
@Produces("text/html;charset=UTF-8")
@Path("security/oauth")
public class OAuthsStub {

    private boolean refreshCalled = false;

    @GET
    @Path("authenticated")
    public String authenticated(@QueryParam("p1") String p1){
        boolean refreshCalled = this.refreshCalled;
        this.refreshCalled = false;
        return format("authenticated() p1=%s refreshCalled=%s", p1, refreshCalled);
    }


    @POST
    @Path("refresh")
    public String refresh(@Context Request request){
        refreshCalled = true;
        OAuths.validateRefresh((HttpRequestContext) request);
        return format("oauth_token=%s&oauth_token_secret=%s&oauth_session_handle=%s",
                OAuths.OAUTH_ACCESS_TOKEN,
                OAuths.OAUTH_ACCESS_SECRET,
                OAuths.OAUTH_SESSION_HANDLE);
    }
}
