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

import org.codegist.common.lang.Disposables;
import org.codegist.crest.handler.RetryHandler;

/**
 * <p>RequestExecutor implementation that used the request method config's retry handler to decides whether a failed request should be retried or not.</p>
 * @author laurent.gilles@codegist.org
 * @see org.codegist.crest.config.MethodConfig#getRetryHandler() 
 */
public class RetryingRequestExecutor implements RequestExecutor {

    private final RequestExecutor delegate;

    /**
     * @param delegate request executor to delegate the requests execution to
     */
    public RetryingRequestExecutor(RequestExecutor delegate) {
        this.delegate = delegate;
    }

    /**
     * @inheritDoc
     */
    public Response execute(Request request) throws Exception {
        RetryHandler retryHandler = request.getMethodConfig().getRetryHandler();
        RequestException exception = null;
        int attemptCount = 1;
        do {
            Disposables.dispose(exception);
            try {
                return delegate.execute(request);
            } catch (RequestException e) {
                exception = e;
            }
        }while(retryHandler.retry(exception, ++attemptCount));

        throw exception;
    }
}
