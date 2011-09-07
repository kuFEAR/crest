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

package org.codegist.crest.handler;

import org.codegist.crest.annotate.CRestComponent;
import org.codegist.crest.io.Request;

/**
 * <p>ErrorHandlers are used called when any error occures during the request execution, after the all retry attempts have been consumed.</p>
 * <p>ErrorHandlers are CRest Components.</p>
 * @author laurent.gilles@codegist.org
 * @see org.codegist.crest.annotate.CRestComponent
 */
@CRestComponent
public interface ErrorHandler {

    /**
     * <p>Handles the exception</p>
     * @param e Exception occured
     * @param <T> Expected return type
     * @return any method compatible return type value if the exception can be ignored
     * @throws Exception Any thrown exception to throw back to the caller
     * @see ErrorHandler
     */
    <T> T handle(Request request, Exception e) throws Exception;

}
