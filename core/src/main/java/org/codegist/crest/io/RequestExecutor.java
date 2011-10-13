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

package org.codegist.crest.io;

/**
 * <b>CRest</b>'s {@link org.codegist.crest.io.Request} executor
 * @author laurent.gilles@codegist.org
 */
public interface RequestExecutor {

    /**
     * Executes the given request
     * @param request request to execute
     * @return response
     * @throws RequestException thrown when a request-related problem occured
     * @throws Exception Any exception thrown during pre-request execution or response wrapping 
     */
    Response execute(Request request) throws RequestException, Exception;
    
}
