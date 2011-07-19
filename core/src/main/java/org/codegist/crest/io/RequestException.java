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

package org.codegist.crest.io;

import org.codegist.common.lang.Disposable;
import org.codegist.common.lang.Disposables;

/**
 * @author laurent.gilles@codegist.org
 */
public class RequestException extends Exception implements Disposable {

    private final Response response;

    public RequestException(Throwable e) {
        super(e);
        this.response = null;
    }
    public RequestException(String message, Response response) {
        super(message);
        this.response = response;
    }

    public boolean hasResponse() {
        return response != null;
    }

    public Response getResponse() {
        return response;
    }

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
