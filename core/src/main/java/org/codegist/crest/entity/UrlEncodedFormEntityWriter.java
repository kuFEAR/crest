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

package org.codegist.crest.entity;

import org.codegist.crest.io.Request;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

import static org.codegist.crest.config.ParamType.FORM;
import static org.codegist.crest.util.Pairs.join;

/**
 * @author laurent.gilles@codegist.org
 */
public class UrlEncodedFormEntityWriter implements EntityWriter {

    /**
     * @inheritDoc
     */
    public String getContentType(Request request) {
        return "application/x-www-form-urlencoded; charset=" + request.getMethodConfig().getCharset().displayName();
    }

    /**
     * @inheritDoc
     */
    public int getContentLength(Request request) {
        return -1;
    }

    /**
     * @inheritDoc
     */
    public void writeTo(Request request, OutputStream out) throws IOException {
        Charset charset = request.getMethodConfig().getCharset();
        Writer writer = new OutputStreamWriter(out, charset);
        join(writer, request.getEncodedParamsIterator(FORM), '&');
        writer.flush();
    }

}
