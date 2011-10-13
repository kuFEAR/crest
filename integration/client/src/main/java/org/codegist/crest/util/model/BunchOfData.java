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

package org.codegist.crest.util.model;

import org.codegist.crest.annotate.Serializer;
import org.codegist.crest.util.AnotherBunchOfDataSerializer;

import java.lang.reflect.Constructor;
import java.util.Date;

/**
 * abstract so that implementation-specific annotation are on subtypes in order to run the tests in environment where some of the declared annotation are not in the classpath
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
@Serializer(AnotherBunchOfDataSerializer.class)
public abstract class BunchOfData<T> {

    private Date val1;

    private Boolean val2;

    private T val3;

    public BunchOfData(Date val1, Boolean val2, T val3) {
        this.val1 = val1;
        this.val2 = val2;
        this.val3 = val3;
    }

    public BunchOfData() {
    }

    public static <T> BunchOfData<T> create(SerializerTypes serializerTypes, Date val1, Boolean val2, T val3){
        String klass;
        switch(serializerTypes){
            case JACKSON:
                klass = "org.codegist.crest.util.model.jackson.JacksonBunchOfData";
                break;
            case JAXB:
                klass = "org.codegist.crest.util.model.jaxb.JaxbBunchOfData";
                break;
            case SIMPLEXML:
                klass = "org.codegist.crest.util.model.simplexml.SimpleXmlBunchOfData";
                break;
            default:
                throw new IllegalStateException();
        }
        try {
            Constructor constructor = Class.forName(klass).getConstructor(Date.class, Boolean.class, Object.class);
            return (BunchOfData<T>) constructor.newInstance(val1, val2, val3);
        }catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Date getVal1() {
        return val1;
    }

    public void setVal1(Date val1) {
        this.val1 = val1;
    }

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

