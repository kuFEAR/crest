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

package org.codegist.crest;

import org.codegist.common.lang.EqualsBuilder;
import org.codegist.common.lang.HashCodeBuilder;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Map;

public class StringValue extends AbstractValue {

    private final String value;


    public StringValue(String value) {
        this(value, null);
    }
    public StringValue(String value, Map<String,Object> metadatas) {
        super(metadatas);
        this.value = value;
    }

    public Object getRaw() {
        return value;
    }

    public String asString() {
        return value;
    }

    public void writeTo(OutputStream out, Charset charset) throws IOException {
        out.write(value.getBytes(charset));
    }

    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(value)
                .hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (null == o || this.getClass() != o.getClass()) return false;
        StringValue that = (StringValue) o;
        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(value, that.value)
                .equals() ;
    }

    @Override
    public String toString() {
        return value;
    }
}