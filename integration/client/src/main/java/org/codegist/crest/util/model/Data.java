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

package org.codegist.crest.util.model;

import java.lang.reflect.Constructor;

/**
 * abstract so that implementation-specific annotation are on subtypes in order to run the tests in environment where some of the declared annotation are not in the classpath
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public abstract class Data {

    private int val1;

    private String val2;

    public Data() {

    }

    public Data(int val1, String val2) {
        this.val1 = val1;
        this.val2 = val2;
    }

    public static Data create(SerializerTypes serializerTypes, int val1, String val2){
        String klass;
        switch(serializerTypes){
            case JACKSON:
                klass = "org.codegist.crest.util.model.jackson.JacksonData";
                break;
            case JAXB:
                klass = "org.codegist.crest.util.model.jaxb.JaxbData";
                break;
            case SIMPLEXML:
                klass = "org.codegist.crest.util.model.simplexml.SimpleXmlData";
                break;
            default:
                throw new IllegalStateException();
        }
        try {
            Constructor constructor = Class.forName(klass).getConstructor(int.class, String.class);
            return (Data) constructor.newInstance(val1, val2);
        }catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int getVal1() {
        return val1;
    }

    public void setVal1(int val1) {
        this.val1 = val1;
    }

    public String getVal2() {
        return val2;
    }

    public void setVal2(String val2) {
        this.val2 = val2;
    }

    @Override
    public String toString() {
        return "Data{" +
                "val1=" + val1 +
                ", val2='" + val2 + '\'' +
                '}';
    }
}
