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

import org.codegist.crest.RequestContext;

/**
 * Error handler gets invoked when an exception occurs during the request firing.
 * <p>If implementor declares a constructor with a Map argument, it will be called with the user custom properties.
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public interface ErrorHandler {

    /**
     * @param e       Exception occured
     * @param <T>     Expected return type
     * @return any value of the expected error type when exception is ignored
     * @throws Exception Any thrown exception while be delegated to the client using the relative rest interface.
     * @see ErrorHandler
     */
    <T> T handle(RequestContext requestContext, Exception e) throws Exception;

}
