/*
 * Copyright 2011 CodeGist.org
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

import org.codegist.crest.param.Param;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import java.util.List;
import java.util.Set;

class JaxbParamJAXBElement extends JAXBElement<JaxbParam> implements Classes {

    public JaxbParamJAXBElement(QName name, JaxbParam value) {
        super(name, JaxbParam.class, value);
    }

    public static JAXBElement<JaxbParam> create(QName wrapperElementName, List<Param> value){
        JaxbParam params = new JaxbParam();
        params.setParams(value);
        return new JaxbParamJAXBElement(wrapperElementName, params);
    }

    public Set<Class<?>> getClasses() {
        return getValue().getClasses();
    }

}