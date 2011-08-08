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

package org.codegist.crest.serializer.jaxb;

import org.codegist.crest.CRestConfig;
import org.codegist.crest.param.Param;
import org.codegist.crest.serializer.Serializer;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;


/**
 * @author laurent.gilles@codegist.org
 */
public class XmlEncodedFormJaxbSerializer implements Serializer<List<Param>> {

    private static final String PREFIX = XmlEncodedFormJaxbSerializer.class.getName();
    public static final String POOL_RETRIEVAL_MAX_WAIT_PROP = PREFIX + JaxbFactory.POOL_RETRIEVAL_MAX_WAIT;
    public static final String JAXB_PROP = PREFIX + JaxbFactory.JAXB;
    public static final String MODEL_PACKAGE_PROP = PREFIX + JaxbFactory.MODEL_PACKAGE;
    public static final String MODEL_FACTORY_CLASS_PROP = PREFIX + JaxbFactory.MODEL_FACTORY_CLASS;
    public static final String WRAPPER_ELEMENT_NAME_PROP = PREFIX + "#wrapper.element-name";

    public static final String DEFAULT_WRAPPER_ELEMENT_NAME = "form-data";

    private final Jaxb jaxb;
    private final QName wrapperElementName;

    public XmlEncodedFormJaxbSerializer(CRestConfig crestConfig) throws JAXBException {
        this.jaxb = JaxbFactory.create(crestConfig, getClass());
        this.wrapperElementName = new QName(crestConfig.get(WRAPPER_ELEMENT_NAME_PROP, DEFAULT_WRAPPER_ELEMENT_NAME));
    }

    public void serialize(List<Param> value, Charset charset, OutputStream out) throws Exception {
        JAXBElement<JaxbParam> object = JaxbParamJAXBElement.create(wrapperElementName, value);
        jaxb.marshal(object, out, charset);
    }
}