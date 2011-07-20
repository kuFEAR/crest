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

package org.codegist.crest.util.model.simplexml;

import org.codegist.crest.util.model.BunchOfData;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.Date;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
@Root(name="bunchOfData")
public class SimpleXmlBunchOfData<T> extends BunchOfData<T> {

    public SimpleXmlBunchOfData(Date val1, Boolean val2, T val3) {
        super(val1,val2,val3);
    }

    public SimpleXmlBunchOfData() {
    }

    @Element
    public Date getVal1() {
        return super.getVal1();
    }

    @Element
    public void setVal1(Date val1) {
        super.setVal1(val1);
    }

    @Element
    public Boolean getVal2() {
        return super.getVal2();
    }

    @Element
    public void setVal2(Boolean val2) {
        super.setVal2(val2);
    }

    @Element
    public T getVal3() {
        return super.getVal3();
    }

    @Element
    public void setVal3(T val3) {
        super.setVal3(val3);
    }
}

