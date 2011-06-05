/*
 * Copyright 2010 CodeGist.org
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 * ===================================================================
 *
 * More information at http://www.codegist.org.
 */

package org.codegist.crest.config;

import org.codegist.common.collect.Maps;
import org.codegist.common.lang.EqualsBuilder;
import org.codegist.common.lang.HashCodeBuilder;
import org.codegist.common.lang.ToStringBuilder;
import org.codegist.crest.serializer.Serializer;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Map;

/**
 * Default immutable in-memory implementation of {@link MethodParamConfig}
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
class DefaultMethodParamConfig extends DefaultParamConfig implements MethodParamConfig {

    private final Serializer serializer;
    private final boolean encoded;
    private final Map<Class<? extends Annotation>, Annotation> annotations;

    DefaultMethodParamConfig(ParamConfig base, Serializer serializer, boolean encoded, Map<Class<? extends Annotation>, Annotation> annotations) {
        this(base.getName(), base.getDefaultValue(), base.getDestination(), base.getMetaDatas(), serializer, encoded, annotations);
    }
    
    DefaultMethodParamConfig(String name, String defaultValue, String destination, Map<String,Object> metadatas, Serializer serializer, boolean encoded, Map<Class<? extends Annotation>, Annotation> annotations) {
        super(name, defaultValue, destination, metadatas);
        this.annotations = Maps.unmodifiable(annotations, false);
        this.serializer = serializer;
        this.encoded = encoded;
    }

    public Serializer getSerializer() {
        return serializer;
    }

    public boolean isEncoded() {
        return encoded;
    }

    public Map<Class<? extends Annotation>, Annotation> getAnnotations() {
        return annotations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultMethodParamConfig that = (DefaultMethodParamConfig) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(serializer, that.serializer)
                .append(encoded, that.encoded)
                .append(annotations, that.annotations)
                .equals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(serializer)
                .append(encoded)
                .append(annotations)
                .hashCode();
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append("name", getName())
                .append("defaultValue", getDefaultValue())
                .append("destination", getDestination())
                .append("serializer", serializer)
                .append("encoded", encoded)
                .append("annotations", annotations)
                .toString();
    }
}
