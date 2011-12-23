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

import org.codegist.crest.CRestConfig;
import org.codegist.crest.CRestException;
import org.codegist.crest.io.Response;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class DefaultResponseHandler implements ResponseHandler {

    public static final String MIN_ERROR_STATUS_CODE_PROP = DefaultResponseHandler.class.getName() + "#min-error-status-code";
    private final int minErrorStatusCode;

    public DefaultResponseHandler(CRestConfig crestConfig) {
        this.minErrorStatusCode = crestConfig.get(MIN_ERROR_STATUS_CODE_PROP, Integer.MAX_VALUE);
    }

    /**
     * @inheritDoc
     */
    public Object handle(Response response) throws Exception {
        if(response.getStatusCode() >= minErrorStatusCode) {
            throw new CRestException("Response Status Code:" + response.getStatusCode() + "\nResponse: " + response.to(String.class));
        }
        return response.deserialize();
    }
}
