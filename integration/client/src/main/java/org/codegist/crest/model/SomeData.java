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

import java.util.Date;

/**
 * @author laurent.gilles@codegist.org
 */
public abstract class SomeData {

    protected int num;
    protected Date date;
    protected Boolean bool;

    public SomeData(int num, Date date, Boolean bool) {
        this.num = num;
        this.date = date;
        this.bool = bool;
    }

    public SomeData() {
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Boolean getBool() {
        return bool;
    }

    public void setBool(Boolean bool) {
        this.bool = bool;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof SomeData)) return false;

        SomeData someData = (SomeData) o;

        if (num != someData.num) return false;
        if (bool != null ? !bool.equals(someData.bool) : someData.bool != null) return false;
        if (date != null ? !date.equals(someData.date) : someData.date != null) return false;

        return true;
    }

    @Override
    public String toString() {
        return "SomeData{" +
                "num=" + num +
                ", date=" + date +
                ", bool=" + bool +
                '}';
    }
}
