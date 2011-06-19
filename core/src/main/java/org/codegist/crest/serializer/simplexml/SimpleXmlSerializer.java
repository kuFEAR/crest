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

package org.codegist.crest.serializer.simplexml;

import org.codegist.crest.serializer.SerializerException;
import org.codegist.crest.serializer.StreamingSerializer;

import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Map;

public class SimpleXmlSerializer extends StreamingSerializer<Object> {

    public static final String USER_SERIALIZER_PROP =  SimpleXmlFactory.USER_SERIALIZER_PROP;

    private final org.simpleframework.xml.Serializer serializer;

    public SimpleXmlSerializer(Map<String,Object> cfg) {
        serializer = SimpleXmlFactory.createSerializer(cfg);
    }

    public void serialize(Object value, Charset charset, OutputStream out) throws SerializerException {
        try {
            serializer.write(value, out);
        } catch (Exception e) {
            throw new SerializerException(e);
        }
    }

}
