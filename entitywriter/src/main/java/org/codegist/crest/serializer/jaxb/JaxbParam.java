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

import org.codegist.common.reflect.Types;
import org.codegist.crest.param.Param;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.namespace.QName;
import java.util.*;

@XmlRootElement
class JaxbParam {

    @XmlAnyElement
    private List<JAXBElement<?>> params;

    @XmlTransient
    private Set<Class<?>> classes;

    public JaxbParam() {
    }

    public Set<Class<?>> getClasses() {
        return classes;
    }

    public void setParams(List<Param> params) {
        this.params = new ArrayList<JAXBElement<?>>();
        this.classes = new HashSet<Class<?>>();
        this.classes.add(getClass());
        for (Param entry: params) {
            Class<?> cls = entry.getConfig().getValueClass();

            for(Object value : entry.getValue()){
                if(value == null) continue;
                Object valueEl;
                if(isPrimitive(value)) {
                   valueEl = value;
                }else{
                    // Any other cleaner way to do that ??
                    XmlRootElement root = cls.getAnnotation(XmlRootElement.class);
                    String name;
                    String ns;
                    if(root != null) {
                        name = "##default".equals(root.name()) ? decapitalize(cls.getSimpleName()) : root.name();
                        ns = "##default".equals(root.namespace()) ? null : root.namespace();
                    }else{
                        name = decapitalize(cls.getSimpleName());
                        ns = null;
                    }
                    valueEl = new JAXBElement(new QName(ns, name), cls, value);
                }
                JAXBElement entryEl = new JAXBElement(new QName(entry.getConfig().getName()), valueEl.getClass(), valueEl);
               this.params.add(entryEl);
            }


            this.classes.addAll(Types.getActors(entry.getConfig().getValueGenericType()));
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
    private String decapitalize(String name) {
        if (name == null || name.length() == 0) {
            return name;
        }
        if (name.length() > 1 && Character.isUpperCase(name.charAt(1)) &&
                Character.isUpperCase(name.charAt(0))){
            return name;
        }
        char chars[] = name.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return new String(chars);
    }
}