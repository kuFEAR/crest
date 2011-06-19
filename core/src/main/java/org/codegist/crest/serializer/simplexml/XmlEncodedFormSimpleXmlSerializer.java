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

import org.codegist.common.lang.Strings;
import org.codegist.crest.CRestProperty;
import org.codegist.crest.http.HttpParam;
import org.codegist.crest.serializer.BooleanSerializer;
import org.codegist.crest.serializer.DateSerializer;
import org.codegist.crest.serializer.SerializerException;
import org.codegist.crest.serializer.StreamingSerializer;
import org.simpleframework.xml.stream.Format;
import org.simpleframework.xml.stream.NodeBuilder;
import org.simpleframework.xml.stream.OutputNode;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author laurent.gilles@codegist.org
 */
public class XmlEncodedFormSimpleXmlSerializer extends StreamingSerializer<List<HttpParam>> {

    public static final String USER_SERIALIZER_PROP =  SimpleXmlFactory.USER_SERIALIZER_PROP;
    public static final String DEFAULT_WRAPPER_ELEMENT_NAME = "form-data";

    private final org.simpleframework.xml.Serializer serializer;
    private final String rootElement;

    private final DateSerializer dateSerializer;
    private final BooleanSerializer booleanSerializer;

    public XmlEncodedFormSimpleXmlSerializer(Map<String,Object> cfg) {
        serializer = SimpleXmlFactory.createSerializer(cfg);
        dateSerializer = new DateSerializer(cfg);
        booleanSerializer = new BooleanSerializer(cfg);
        this.rootElement = Strings.defaultIfBlank((String) cfg.get(CRestProperty.SERIALIZER_XML_WRAPPER_ELEMENT_NAME), DEFAULT_WRAPPER_ELEMENT_NAME);
    }

    public void serialize(List<HttpParam> params, Charset charset, OutputStream out) throws SerializerException {
        try {
            String prolog = "<?xml version=\"1.0\" encoding=\""+charset.toString()+"\" standalone=\"yes\"?>";
            OutputNode node = NodeBuilder.write(new OutputStreamWriter(out, charset), new Format(0, prolog));
            OutputNode root =  node.getChild(rootElement);

            for(HttpParam param : params){
                for(Object value : param.getValue()){
                    if(value == null) continue;
                    OutputNode n = root.getChild(param.getConfig().getName());
                    if(isPrimitive(value)) {
                        // nasty.....
                        if(value instanceof Date) {
                            n.setValue(dateSerializer.serialize((Date)value, charset));
                        }else if(value instanceof Boolean) {
                            n.setValue(booleanSerializer.serialize((Boolean) value, charset));
                        }else{
                            n.setValue(value.toString());
                        }
                    }else{
                        serializer.write(value, n);
                    }
                }
            }
            node.commit();
        } catch (Exception e) {
            throw new SerializerException(e);
        }
    }

    private boolean isPrimitive(Object value){
            return value.getClass().isPrimitive()
                    || value instanceof Number
                    || value instanceof Boolean
                    || value instanceof Character
                    || value instanceof String
                    || value instanceof Date;
        }
}
