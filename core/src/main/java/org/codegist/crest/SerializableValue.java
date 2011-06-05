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

package org.codegist.crest;

import org.codegist.common.lang.EqualsBuilder;
import org.codegist.common.lang.HashCodeBuilder;
import org.codegist.crest.serializer.Serializer;
import org.codegist.crest.serializer.SerializerException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * Default internal immutable implementation of ParamContext
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
class SerializableValue extends AbstractValue {

    private final Object value;
    private final String defaultValue;
    private final Serializer serializer;
    private final String encoding;
    private final Charset charset;

    SerializableValue(Object value, String defaultValue, Serializer serializer, String encoding) {
        this(value, defaultValue, serializer, encoding, null);
    }
    SerializableValue(Object value, String defaultValue, Serializer serializer, String encoding, Map<String,Object> metadatas) {
        super(metadatas);
        this.value = value;
        this.defaultValue = defaultValue;
        this.serializer = serializer;
        this.encoding = encoding;
        this.charset = Charset.forName(encoding);
    }

    public Object getRaw() {
        if(value != null) {
            return value;
        }else{
            return defaultValue;
        }
    }

    public String asString(){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            writeTo(out, charset);
            return out.toString(encoding);
        } catch (IOException e) {
            throw new SerializerException(e);
        }
    }

    public void writeTo(OutputStream out, Charset charset) throws IOException {
        if(value == null){
            out.write(defaultValue.getBytes(charset));
        }else{
            serializer.serialize(value, out, charset);
        }
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(value)
                .append(defaultValue)
                .append(serializer)
                .append(encoding)
                .hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (null == o || this.getClass() != o.getClass()) return false;
        SerializableValue that = (SerializableValue) o;
        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(this.value, that.value)
                .append(this.defaultValue, that.defaultValue)
                .append(this.serializer, that.serializer)
                .append(this.encoding, that.encoding)
                .equals();
    }

}
