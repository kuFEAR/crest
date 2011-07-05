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

package org.codegist.crest.http;

import org.codegist.common.lang.EqualsBuilder;
import org.codegist.common.lang.HashCodeBuilder;
import org.codegist.common.lang.ToStringBuilder;
import org.codegist.crest.config.ParamConfig;
import org.codegist.crest.config.StringParamConfig;

import java.util.Collection;
import java.util.Collections;

public class HttpParam {

    private final Collection<Object> value;
    private final ParamConfig config;

    public HttpParam(String name, String value, String dest, boolean encoded) {
        this(new StringParamConfig(name, value, dest, encoded), Collections.<Object>singleton(value));
    }
    
    public HttpParam(ParamConfig config, Collection<Object> value) {
        this.config = config;
        this.value = value.isEmpty() ? Collections.<Object>singleton(config.getDefaultValue()) : value;
    }

    public Collection<Object> getValue() {
        return value;
    }

    public ParamConfig getConfig() {
        return config;
    }

    public int hashCode() {
        return new HashCodeBuilder()
                .append(this.value)
                .append(this.config)
                .hashCode();
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (null == o || this.getClass() != o.getClass()) return false;
        HttpParam that = (HttpParam) o;
        return new EqualsBuilder()
                .append(this.value, that.value)
                .append(this.config, that.config)
                .equals();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", config.getName())
                .append("value", value)
                .toString();
    }
}