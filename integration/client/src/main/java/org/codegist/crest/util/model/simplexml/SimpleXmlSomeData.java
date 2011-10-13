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

package org.codegist.crest.util.model.simplexml;

import org.codegist.crest.util.model.SomeData;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.Date;

/**
 * @author laurent.gilles@codegist.org
 */
@Root(name = "someData")
public class SimpleXmlSomeData extends SomeData {


    @Override
    @Element(name="num")
    public int getNum() {
        return super.getNum();
    }

    @Override
    @Element(name="date")
    public Date getDate() {
        return super.getDate();
    }

    @Override
    @Element(name="bool")
    public Boolean getBool() {
        return super.getBool();
    }

    @Override
    @Element(name="bool")
    public void setBool(Boolean bool) {
        super.setBool(bool);
    }

    @Override
    @Element(name="date")
    public void setDate(Date date) {
        super.setDate(date);
    }

    @Override
    @Element(name="num")
    public void setNum(int num) {
        super.setNum(num);
    }
}
