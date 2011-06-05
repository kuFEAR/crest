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
 *  ==================================================================
 *
 *  More information at http://www.codegist.org.
 */

package org.codegist.crest;

import org.codegist.common.lang.EqualsBuilder;
import org.codegist.common.lang.HashCodeBuilder;
import org.codegist.common.lang.ToStringBuilder;

public class HttpParam {
    private final String name;
    private final Value value;
    private final boolean encoded;

    private final int hashCode;

    public HttpParam(HttpParam param) {
        this(param.name, param.value, param.encoded);
    }
    public HttpParam(String name, String value, boolean encoded) {
        this(name, new StringValue(value), encoded);
    }
    public HttpParam(String name, Value value, boolean encoded) {
        this.name = name;
        this.value = value;
        this.encoded = encoded;
        this.hashCode = new HashCodeBuilder()
                .append(name)
                .append(value)
                .append(encoded)
                .hashCode();
    }

    public String getName() {
        return name;
    }

    public Value getValue() {
        return value;
    }

    public boolean isEncoded() {
        return encoded;
    }

    public int hashCode() {
        return this.hashCode;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (null == o || this.getClass() != o.getClass()) return false;
        HttpParam that = (HttpParam) o;
        return new EqualsBuilder()
                .append(this.name, that.name)
                .append(this.value, that.value)
                .append(this.encoded, that.encoded)
                .equals();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .append("value", value)
                .append("encoded", encoded)
                .toString();
    }
}