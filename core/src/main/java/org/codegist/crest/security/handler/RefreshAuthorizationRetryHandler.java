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

package org.codegist.crest.security.handler;

import org.codegist.common.log.Logger;
import org.codegist.crest.CRestProperty;
import org.codegist.crest.handler.RetryHandler;
import org.codegist.crest.io.RequestException;
import org.codegist.crest.security.Authorization;

import java.util.Map;

import static org.codegist.common.lang.Validate.notNull;
import static org.codegist.crest.CRestProperty.get;
import static org.codegist.crest.CRestProperty.getRetryAttempts;

/**
 * Authentification retry handler that refresh the authentification if the retry cause is a 401 problem.
 * <p>Requires an {@link org.codegist.crest.security.Authorization} instance to be present in the custom properties.
 */
public class RefreshAuthorizationRetryHandler implements RetryHandler {

    private static final Logger LOGGER = Logger.getLogger(RefreshAuthorizationRetryHandler.class);

    private final Authorization authorization;
    private final int max;
    private final int unauthorizedStatusCode;

    public RefreshAuthorizationRetryHandler(Map<String, Object> crestProperties) {
        this.max = getRetryAttempts(crestProperties) + 1;
        this.unauthorizedStatusCode = CRestProperty.<Integer>get(crestProperties, CRestProperty.CREST_UNAUTHORIZED_STATUS_CODE);
        authorization = get(crestProperties, Authorization.class);
        notNull(this.authorization, "No authentification manager found, please pass it in the properties (key=%s)", Authorization.class);
    }

    public boolean retry(RequestException exception, int retryNumber) throws Exception {
        if (retryNumber > max
                || !exception.hasResponse()
                || exception.getResponse().getStatusCode() != unauthorizedStatusCode) {
            LOGGER.debug("Not retrying, maximum failure reached or catched exception does not have a response or with a status code different than unauthorized.");
            return false;
        }
        LOGGER.debug("Unauthorized status code has been detected, refreshing authentification and retry.");
        authorization.refresh();
        return true;
    }

}
