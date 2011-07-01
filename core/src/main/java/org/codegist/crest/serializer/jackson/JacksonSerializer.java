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

package org.codegist.crest.serializer.jackson;

import org.codegist.crest.CRestException;
import org.codegist.crest.serializer.StreamingSerializer;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Map;


public class JacksonSerializer<T> extends StreamingSerializer<T> {

    private final ObjectMapper jackson;

    public JacksonSerializer(Map<String, Object> config) {
        this.jackson = JacksonProvider.createSerializer(config);
    }

    public void serialize(Object value, Charset charset, OutputStream out) {
        try {
            jackson.writeValue(out, value);
        } catch (IOException e) {
            throw CRestException.handle(e);
        }
    }
}
