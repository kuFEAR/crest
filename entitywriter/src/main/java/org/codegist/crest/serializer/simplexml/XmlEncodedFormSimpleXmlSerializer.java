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

import org.codegist.crest.CRestConfig;
import org.codegist.crest.param.Param;
import org.codegist.crest.serializer.DateSerializer;
import org.codegist.crest.serializer.Serializer;
import org.codegist.crest.serializer.primitive.BooleanSerializer;
import org.simpleframework.xml.stream.Format;
import org.simpleframework.xml.stream.NodeBuilder;
import org.simpleframework.xml.stream.OutputNode;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;


/**
 * @author laurent.gilles@codegist.org
 */
public class XmlEncodedFormSimpleXmlSerializer implements Serializer<List<Param>> {

    private static final String PREFIX = XmlEncodedFormSimpleXmlSerializer.class.getName();
    public static final String SERIALIZER_PROP =  PREFIX + SimpleXmlFactory.SERIALIZER;
    public static final String WRAPPER_ELEMENT_NAME_PROP = PREFIX + "#wrapper.element-name";
    public static final String DEFAULT_WRAPPER_ELEMENT_NAME = "form-data";

    private final org.simpleframework.xml.Serializer serializer;
    private final String rootElement;

    private final DateSerializer dateSerializer;
    private final BooleanSerializer booleanSerializer;

    public XmlEncodedFormSimpleXmlSerializer(CRestConfig crestConfig) {
        serializer = SimpleXmlFactory.createSerializer(crestConfig, getClass());
        dateSerializer = new DateSerializer(crestConfig);
        booleanSerializer = new BooleanSerializer(crestConfig);
        this.rootElement = crestConfig.get(WRAPPER_ELEMENT_NAME_PROP, DEFAULT_WRAPPER_ELEMENT_NAME);
    }

    public void serialize(List<Param> params, Charset charset, OutputStream out) throws Exception {
        String prolog = "<?xml version=\"1.0\" encoding=\""+charset.displayName()+"\" standalone=\"yes\"?>";
        Writer writer = new OutputStreamWriter(out, charset);
        OutputNode node = NodeBuilder.write(writer, new Format(0, prolog));
        OutputNode root =  node.getChild(rootElement);

        for(Param param : params){
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
        writer.flush();
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
