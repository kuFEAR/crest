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

package org.codegist.crest.interceptor;

import org.codegist.crest.io.Request;

/**
 * Interceptors are notified before and after the parameters have been added to the io.
 * <p>They can be used to cancel a io from being fired by returning false, or arbitrary modify the io.
 * <p>If implementor declares a constructor with a Map argument, it will be called with the user custom properties.
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public interface RequestInterceptor {

    /**
     * Called after general parameter have been added to the io, but before parameters are injected into it.
     *
     * @param builder The current http io being build
     * @param context The current io context
     */
    void beforeFire(Request request) throws Exception;

}
