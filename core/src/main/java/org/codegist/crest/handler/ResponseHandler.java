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

package org.codegist.crest.handler;

import org.codegist.crest.annotate.CRestComponent;
import org.codegist.crest.io.Response;

/**
 * <p>ResponseHandlers are responsible for handling the server response and returning the deserialized response back to the caller.</p>
 * <p>ResponseHandlers are CRest Components.</p>
 * @author laurent.gilles@codegist.org
 * @see org.codegist.crest.annotate.CRestComponent
 */
@CRestComponent
public interface ResponseHandler {

    /**
     * Server response to handle
     * @param response response to handle
     * @return deserialized response, must be compatible with the method return type
     * @throws Exception Any exception thrown during the response handling
     */
    Object handle(Response response) throws Exception;

}
