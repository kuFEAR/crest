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
import org.simpleframework.xml.stream.NodeBuilder;
import org.simpleframework.xml.stream.OutputNode;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Map;

public class SimpleXmlSerializer extends StreamingSerializer<Object> {

    public static final String USER_SERIALIZER_PROP =  SimpleXmlFactory.SERIALIZER_USER_SERIALIZER_PROP;
    public static final String DATE_FORMAT_PROP =  SimpleXmlFactory.SERIALIZER_DATE_FORMAT_PROP;
    public static final String BOOLEAN_FORMAT_PROP = SimpleXmlFactory.SERIALIZER_BOOLEAN_FORMAT_PROP;

    private final org.simpleframework.xml.Serializer serializer;

    public SimpleXmlSerializer(Map<String,Object> cfg) {
        serializer = SimpleXmlFactory.createSerializer(cfg);
    }

    public void serialize(Object value, OutputStream out, Charset charset) throws SerializerException {
        try {
            serializer.write(value, out);
        } catch (Exception e) {
            throw new SerializerException(e);
        }
    }

}
