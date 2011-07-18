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

package org.codegist.crest.io.http;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public interface HttpConstants {

    // --- 1xx Informational ---
    int HTTP_CONTINUE = 100;
    int HTTP_SWITCHING_PROTOCOLS = 101;
    int HTTP_PROCESSING = 102;

    // --- 2xx Success ---
    int HTTP_OK = 200;
    int HTTP_CREATED = 201;
    int HTTP_ACCEPTED = 202;
    int HTTP_NON_AUTHORITATIVE_INFORMATION = 203;
    int HTTP_NO_CONTENT = 204;
    int HTTP_RESET_CONTENT = 205;
    int HTTP_PARTIAL_CONTENT = 206;
    int HTTP_MULTI_STATUS = 207;

    // --- 3xx Redirection ---
    int HTTP_MULTIPLE_CHOICES = 300;
    int HTTP_MOVED_PERMANENTLY = 301;
    int HTTP_MOVED_TEMPORARILY = 302;
    int HTTP_SEE_OTHER = 303;
    int HTTP_NOT_MODIFIED = 304;
    int HTTP_USE_PROXY = 305;
    int HTTP_TEMPORARY_REDIRECT = 307;

    // --- 4xx Client Error ---
    int HTTP_BAD_REQUEST = 400;
    int HTTP_UNAUTHORIZED = 401;
    int HTTP_PAYMENT_REQUIRED = 402;
    int HTTP_FORBIDDEN = 403;
    int HTTP_NOT_FOUND = 404;
    int HTTP_METHOD_NOT_ALLOWED = 405;
    int HTTP_NOT_ACCEPTABLE = 406;
    int HTTP_PROXY_AUTHENTICATION_REQUIRED = 407;
    int HTTP_REQUEST_TIMEOUT = 408;
    int HTTP_CONFLICT = 409;
    int HTTP_GONE = 410;
    int HTTP_LENGTH_REQUIRED = 411;
    int HTTP_PRECONDITION_FAILED = 412;
    int HTTP_REQUEST_TOO_LONG = 413;
    int HTTP_REQUEST_URI_TOO_LONG = 414;
    int HTTP_UNSUPPORTED_MEDIA_TYPE = 415;
    int HTTP_REQUESTED_RANGE_NOT_SATISFIABLE = 416;
    int HTTP_EXPECTATION_FAILED = 417;
    int HTTP_IM_A_TEA_POT_ENTITY = 418;
    int HTTP_INSUFFICIENT_SPACE_ON_RESOURCE = 419;
    int HTTP_METHOD_FAILURE = 420;
    int HTTP_UNPROCESSABLE_ENTITY = 422;
    int HTTP_LOCKED = 423;
    int HTTP_FAILED_DEPENDENCY = 424;

    // --- 5xx Server Error ---
    int HTTP_INTERNAL_SERVER_ERROR = 500;
    int HTTP_NOT_IMPLEMENTED = 501;
    int HTTP_BAD_GATEWAY = 502;
    int HTTP_SERVICE_UNAVAILABLE = 503;
    int HTTP_GATEWAY_TIMEOUT = 504;
    int HTTP_HTTP_VERSION_NOT_SUPPORTED = 505;
    int HTTP_INSUFFICIENT_STORAGE = 507;
}
