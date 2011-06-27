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

package org.codegist.crest;

import org.codegist.common.lang.Validate;
import org.codegist.crest.http.HttpParam;
import org.codegist.crest.http.HttpRequest;
import org.codegist.crest.serializer.Serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @author laurent.gilles@codegist.org
 */
public class SerializingEntityWriter implements EntityWriter {

    private final Serializer<List<HttpParam>> serializer;
    private final String contentType;

    public SerializingEntityWriter(Serializer<List<HttpParam>> serializer, String contentType) {
        Validate.notNull(serializer, "Serializer can't be null");
        Validate.notNull(contentType, "ContentType can't be null");
        this.serializer = serializer;
        this.contentType = contentType;
    }

    public String getContentType(HttpRequest request) {
        return contentType;
    }

    public void writeTo(HttpRequest request, OutputStream outputStream) throws IOException {
        serializer.serialize(request.getFormParams(), request.getCharset(), outputStream);
    }
}
