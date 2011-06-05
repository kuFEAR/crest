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

package org.codegist.crest.serializer;

import org.codegist.common.collect.Maps;
import org.codegist.crest.CRestProperty;
import org.codegist.crest.HttpParamMap;
import org.codegist.crest.serializer.jaxb.JaxbSerializer;
import org.codegist.crest.serializer.jaxb.XmlEncodedFormJaxbSerializer;
import org.codegist.crest.serializer.simplexml.SimpleXmlFactory;
import org.codegist.crest.serializer.simplexml.SimpleXmlSerializer;
import org.codegist.crest.serializer.simplexml.XmlEncodedFormSimpleXmlSerializer;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.stream.NodeBuilder;
import org.simpleframework.xml.stream.OutputNode;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @author laurent.gilles@codegist.org
 */
public class Test {


    public static void main(String[] args) throws Exception {

        Map<String,Object> o = new HashMap<String,Object>();
        o.put("a","b");
        o.put("c",new A("aaa", new B("bbb", new AA("a1"))));
        o.put("d",new A("bbb", new B("ccc", new AA("b1"))));
        o.put("e","f");

        Map<String,Object> o2 = new HashMap<String,Object>();
        o2.put("a","b");
        o2.put("c",new A("aaa", new B("bbb", new AA("a1"))));
        o2.put("e","f");

        Map<String,Object> props = new Hashtable<String, Object>();
        props.put(CRestProperty.CREST_CONCURRENCY_LEVEL, 16);
        props.put(CRestProperty.SERIALIZER_XML_WRAPPER_ELEMENT_NAME, "my-root");

        Serializer<Map<String,Object>> simpleXml = new XmlEncodedFormSimpleXmlSerializer(props);
        Serializer<Map<String,Object>> jaxb = new XmlEncodedFormJaxbSerializer(props);


//
        System.out.println("JAXB");
        jaxb.serialize(o, System.out, Charset.defaultCharset());
        jaxb.serialize(o2, System.out, Charset.defaultCharset());

        System.out.println("\n----");
        System.out.println("SIMPLEXML");
        simpleXml.serialize(o, System.out, Charset.defaultCharset());
        simpleXml.serialize(o2, System.out, Charset.defaultCharset());

        System.out.println("");

    }

    
    @XmlRootElement(name="A")
    @Root(name="A")
    static class A {
        @XmlAttribute(name="valA")
        @Attribute(name="valA")
        String value;

        @XmlElement(name="BB")
        @Element(name="BB")
        B b;

        A() { }
        A(String value, B b) {
            this.value = value;
            this.b = b;
        }
    }

    @XmlRootElement(name="B")
    @Root(name="B")
    static class B {
        @XmlAttribute(name="valB")
        @Attribute(name="valB")
        String value;

        //@XmlAnyElement
        @XmlElement(name="AAA")
        @Element(name="AAA")
        AA aa;

        B() { }
        B(String value, AA aa) {
            this.value = value;
            this.aa = aa;
        }
    }

    @XmlRootElement(name="AA")
    @Root(name="AA")
    static class AA {
        @XmlAttribute(name="val")
        @Attribute(name="val")
        String val;

        AA() {

        }

        AA(String val) {
            this.val = val;
        }
    }
    @XmlRootElement(name="AA1")
    @Root(name="AA1")
    static class AA1 extends AA {
        @XmlAttribute(name="val1")
        @Attribute(name="val1")
        String val1;

        AA1() {

        }

        AA1(String val, String val1) {
            super(val);
            this.val1 = val1;
        }
    }
    @XmlRootElement(name="AA2")
    @Root(name="AA2")
    static class AA2 extends AA {
        @XmlAttribute(name="val2")
        @Attribute(name="val2")
        String val2;

        AA2() {

        }

        AA2(String val, String val2) {
            super(val);
            this.val2 = val2;
        }
    }


//    @org.junit.Test
//    public void test(){
//        Map<String,String> o = new HashMap<String,String>();
//        o.put("a","b");
//        o.put("c","d");
//        o.put("e","f");
//        Serializer serializer = ser();
//        String s = serializer.serialize(o, Charset.forName("utf-8"));
//        System.out.println("s="+s);
//    }
//
////    static Serializer ser(){
////      return new JacksonSerializer(new HashMap());
////    }
//
//    static Serializer ser(){
//        Map<String,Object> cfg = new HashMap<String, Object>();
//        cfg.put(JaxbSerializer.MODEL_CLASSES_BOUND_PROP, new Class[]{HashMap.class});
//        return new JaxbSerializer(cfg);
//    }
}


