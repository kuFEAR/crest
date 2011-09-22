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

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

/**
 * HTTP abstraction layer
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public interface HttpChannel {

    /**
     * Sets the socket timeout
     * @param timeout the socket timeout
     * @throws IOException
     */
    void setSocketTimeout(int timeout) throws IOException;

    /**
     * Sets the connection timeout
     * @param timeout the connection timeout
     * @throws IOException
     */
    void setConnectionTimeout(int timeout) throws IOException;

    /**
     * Adds a new HTTP header
     * @param name the HTTP header name
     * @param value the HTTP header value
     * @throws IOException
     */
    void addHeader(String name, String value) throws IOException;

    /**
     * Sets a HTTP header, overriding any existing one
     * @param name the HTTP header name
     * @param value the HTTP header value
     * @throws IOException
     */
    void setHeader(String name, String value) throws IOException;

    /**
     * Sets the HTTP Content-Type header
     * @param value the HTTP Content-Type header value
     * @throws IOException
     */
    void setContentType(String value) throws IOException;

    /**
     * Sets the HTTP Accept header
     * @param value the HTTP Accept header value
     * @throws IOException
     */
    void setAccept(String value) throws IOException;

    /**
     * Sets the HTTP entity writer to use
     * @param httpEntityWriter the HTTP entity writer to use
     * @throws IOException
     */
    void writeEntityWith(HttpEntityWriter httpEntityWriter) throws IOException;

    /**
     * Sends the HTTP request. Once called, no other calls can be made to the HTTP channel instance
     * @return the HTTP response
     * @throws IOException
     */
    Response send() throws IOException;

    /**
     * HTTP Chanel's response
     */
    interface Response extends Closeable {

        /**
         * Returns the HTTP response's status code
         * @return the HTTP response's status code
         * @throws IOException
         */
        int getStatusCode() throws IOException;

        /**
         * Returns the HTTP response's status message
         * @return the HTTP response's status message
         * @throws IOException
         */
        String getStatusMessage() throws IOException;

        /**
         * Returns the HTTP response's entity stream
         * @return the HTTP response's entity stream
         * @throws IOException
         */
        InputStream getEntity() throws IOException;

        /**
         * Returns the HTTP response's Content-Type
         * @return the HTTP response's Content-Type
         * @throws IOException
         */
        String getContentType() throws IOException;

        /**
         * Returns the HTTP response's Content-Encoding
         * @return the HTTP response's Content-Encoding
         * @throws IOException
         */
        String getContentEncoding() throws IOException;

    }
}
