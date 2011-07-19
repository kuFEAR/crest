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

package org.codegist.crest.server.utils;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;


public class AuthException extends WebApplicationException {
    public final Response.Status status;
    public final String wwwAuthHeader;

    public AuthException(Response.Status status, String wwwAuthHeader) {
        super(toResponse(status, wwwAuthHeader));
        this.status = status;
        this.wwwAuthHeader = wwwAuthHeader;
    }

    /** Maps this exception to a response object.
     *
     * @return Response this exception maps to.
     */
    public static Response toResponse(Response.Status status, String wwwAuthHeader) {
        Response.ResponseBuilder rb = Response.status(status);
        if (wwwAuthHeader != null) {
            rb.header("WWW-Authenticate", wwwAuthHeader);
        }
        return rb.build();
    }
}