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

import org.codegist.common.lang.Objects;
import org.codegist.common.lang.Strings;
import org.codegist.crest.CRestProperty;
import org.codegist.crest.serializer.SerializerException;
import org.codegist.crest.serializer.StreamingSerializer;
import org.simpleframework.xml.stream.Format;
import org.simpleframework.xml.stream.NodeBuilder;
import org.simpleframework.xml.stream.OutputNode;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;

/**
 * @author laurent.gilles@codegist.org
 */
public class XmlEncodedFormSimpleXmlSerializer extends StreamingSerializer<Map<String, Object>> {

    public static final String USER_SERIALIZER_PROP =  SimpleXmlFactory.SERIALIZER_USER_SERIALIZER_PROP;
    public static final String DATE_FORMAT_PROP =  SimpleXmlFactory.SERIALIZER_DATE_FORMAT_PROP;
    public static final String BOOLEAN_FORMAT_PROP = SimpleXmlFactory.SERIALIZER_BOOLEAN_FORMAT_PROP;

    public static final String DEFAULT_WRAPPER_ELEMENT_NAME = "form-data";

    private final org.simpleframework.xml.Serializer serializer;
    private final String rootElement;

    public XmlEncodedFormSimpleXmlSerializer(Map<String,Object> cfg) {
        serializer = SimpleXmlFactory.createSerializer(cfg);
        this.rootElement = Strings.defaultIfBlank((String) cfg.get(CRestProperty.SERIALIZER_XML_WRAPPER_ELEMENT_NAME), DEFAULT_WRAPPER_ELEMENT_NAME);
    }

    public void serialize(Map<String, Object> map, Charset charset, OutputStream out) throws SerializerException {
        try {
            String prolog = "<?xml version=\"1.0\" encoding=\""+charset.toString()+"\" standalone=\"yes\"?>";
            OutputNode node = NodeBuilder.write(new OutputStreamWriter(out, charset), new Format(0, prolog));
            OutputNode root =  node.getChild(rootElement);

            for(Map.Entry<String,Object> entry : map.entrySet()){
                Object value = entry.getValue();
                Iterator<Object> iterator = Objects.iterate(value);
                while(iterator.hasNext()){
                    OutputNode n = root.getChild(entry.getKey());
                    Object val = iterator.next();
                    if(isPrimitive(val)) {
                        n.setValue(String.valueOf(val));
                    }else{
                        serializer.write(val, n);
                    }
                }
            }
            node.commit();
        } catch (Exception e) {
            throw new SerializerException(e);
        }
    }

    private boolean isPrimitive(Object value){
        return (value instanceof String || value instanceof Number || value instanceof Boolean || value instanceof Character);
    }
}
