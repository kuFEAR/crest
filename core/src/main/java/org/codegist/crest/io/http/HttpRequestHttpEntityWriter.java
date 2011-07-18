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

package org.codegist.crest.io.http;

import org.codegist.common.log.Logger;
import org.codegist.common.log.LoggingOutputStream;
import org.codegist.crest.CRestException;
import org.codegist.crest.io.Request;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author laurent.gilles@codegist.org
 */
class RequestEntityWriter implements HttpEntityWriter {

    private final Request request;
    private final Logger logger;

    RequestEntityWriter(Request request, Logger logger) {
        this.request = request;
        this.logger = logger;
    }

    public void writeEntityTo(OutputStream out) throws IOException {
        OutputStream os = !logger.isTraceOn() ? out : new LoggingOutputStream(out, logger);
        try {
            request.getMethodConfig().getEntityWriter().writeTo(request, os);
        } catch (Exception e) {
            throw CRestException.handle(e);
        }
        os.flush();
    }

    public int getContentLength() {
        return request.getMethodConfig().getEntityWriter().getContentLength(request);
    }
}
