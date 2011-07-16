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

package org.codegist.crest.util;

import org.codegist.crest.serializer.Serializer;
import org.codegist.crest.serializer.StringSerializer;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public final class Serializers {

    private Serializers(){
        throw new IllegalStateException();
    }

    public static <T> String serialize(Serializer<T> serializer, T value, Charset charset) throws Exception {
        if(serializer instanceof StringSerializer) {
            return ((StringSerializer)serializer).serialize(value, charset);
        }else{
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            serializer.serialize(value, charset, out);
            return new String(out.toByteArray(), charset);
        }
    }
}
