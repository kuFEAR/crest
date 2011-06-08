/*
 * Copyright 2010 CodeGist.org
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 * ===================================================================
 *
 * More information at http://www.codegist.org.
 */

package org.codegist.crest.http;

import java.io.IOException;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class HttpException extends RuntimeException {

    private final HttpResponse response;
    private final String responseString;

    public HttpException(Throwable cause) throws IOException {
        this(null,null,cause);
    }
    public HttpException(String message, HttpResponse response) throws IOException {
        super(message);
        this.response = response;
        this.responseString = response.asString();
    }

    public HttpException(Throwable cause, HttpResponse response) throws IOException {
        super(cause);
        this.response = response;
        this.responseString = response.asString();
    }

    public HttpException(String message, HttpResponse response, Throwable cause) throws IOException {
        super(message, cause);
        this.response = response;
        this.responseString = response.asString();
    }

    public HttpResponse getResponse() {
        return response;
    }

    public String getResponseString() {
        return responseString;
    }
}
