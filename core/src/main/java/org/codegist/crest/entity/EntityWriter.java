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

package org.codegist.crest.entity;

import org.codegist.crest.annotate.CRestComponent;
import org.codegist.crest.io.Request;

import java.io.OutputStream;

/**
 * <p>EntityWriters are used to write the content of the request entity</p>
 * <p>EntityWriters are CRest Components.</p>
 * @author laurent.gilles@codegist.org
 * @see org.codegist.crest.annotate.CRestComponent
 */
@CRestComponent
public interface EntityWriter {

    /**
     * <p>Writes the request entity into the given outputstream</p>
     * @param request request to get the entity related data from
     * @param outputStream entity outputstream
     * @throws Exception Any exception thrown while writing the entity
     */
    void writeTo(Request request, OutputStream outputStream) throws Exception;

    /**
     * <p>Should return the request content-type</p>
     * @param request current request
     * @return request's Content-Type
     */
    String getContentType(Request request);

    /**
     * <p>Should return the entity content length if known, or -1</p>
     * @param request current request
     * @return request's content length if known, or -1
     */
    int getContentLength(Request request);
}
