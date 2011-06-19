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

package org.codegist.crest.model;

import org.codegist.crest.annotate.Serializer;
import org.codegist.crest.serializer.AnotherBunchOfDataSerializer;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
@Serializer(AnotherBunchOfDataSerializer.class)
@XmlRootElement
@Root
public class BunchOfData<T> {

    @Element
    private Date val1;

    @Attribute
    private Boolean val2;

    @Element   
    private T val3;

    public BunchOfData(Date val1, Boolean val2, T val3) {
        this.val1 = val1;
        this.val2 = val2;
        this.val3 = val3;
    }

    public BunchOfData() {
    }

    public Date getVal1() {
        return val1;
    }

    public void setVal1(Date val1) {
        this.val1 = val1;
    }

    @XmlAttribute
    public Boolean getVal2() {
        return val2;
    }

    public void setVal2(Boolean val2) {
        this.val2 = val2;
    }

    public T getVal3() {
        return val3;
    }

    public void setVal3(T val3) {
        this.val3 = val3;
    }

    @Override
    public String toString() {
        return "BunchOfData{" +
                "val1=" + val1 +
                ", val2=" + val2 +
                ", val3=" + val3 +
                '}';
    }
}

