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

import org.codegist.common.lang.Disposable;
import org.codegist.common.lang.Disposables;

/**
 * Exception thrown during a request I/O
 * @author laurent.gilles@codegist.org
 */
public class RequestException extends Exception implements Disposable {

    private final Response response;

    /**
     * @param e original error
     */
    public RequestException(Throwable e) {
        super(e);
        this.response = null;
    }

    /**
     * @param message exception message
     * @param response underlying response, can be null
     */
    public RequestException(String message, Response response) {
        super(message);
        this.response = response;
    }

    /**
     * @return whether the given exception has an underlying response
     */
    public boolean hasResponse() {
        return response != null;
    }

    /**
     *
     * @return underlying response, can be null
     * @see RequestException#hasResponse()
     */
    public Response getResponse() {
        return response;
    }

    /**
     * Dispose underlying response if applicable
     */
    public void dispose() {
        if(hasResponse()) {
            Disposables.dispose(response);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            dispose();
        }finally{
            super.finalize();
        }
    }

}
