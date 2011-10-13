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

package org.codegist.crest.entity.multipart;

import org.codegist.crest.config.ParamConfig;
import org.codegist.crest.param.Param;
import org.codegist.crest.serializer.Serializer;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.unmodifiableMap;

/**
 * @author laurent.gilles@codegist.org
 */
final class MultiPartBinarySerializer implements Serializer<MultiPart<Param>> {

    static final MultiPartBinarySerializer INSTANCE = new MultiPartBinarySerializer();

    private static final Map<Class, Serializer> BINARY_SERIALIZERS;
    static {    
        Map<Class, Serializer> map = new HashMap<Class, Serializer>();
        map.put(File.class, MultiPartFileSerializer.INSTANCE);
        map.put(InputStream.class, MultiPartInputStreamSerializer.INSTANCE);
        BINARY_SERIALIZERS = unmodifiableMap(map);
    }

    public void serialize(MultiPart<Param> multipart, Charset charset, OutputStream out) throws Exception {
        Serializer<MultiPart<?>> serializer = getSerializer(multipart.getParamConfig().getValueClass());
        serialize(multipart, charset, out, serializer);
    }

    static Serializer<MultiPart<?>> getSerializer(Class<?> clazz){
        return BINARY_SERIALIZERS.get(clazz);
    }

    static void serialize(MultiPart<Param> multipart, Charset charset, OutputStream out, Serializer<MultiPart<?>> serializer) throws Exception {
        ParamConfig pc = multipart.getParamConfig();
        String boundary = multipart.getBoundary();
        for(Object value : multipart.getValue().getValue()){
            MultiPart<?> valueMultiPart = new MultiPart<Object>(pc, value, boundary);
            serializer.serialize(valueMultiPart, charset, out);
        }
    }

    static boolean isBinary(Class<?> paramClass){
        return BINARY_SERIALIZERS.containsKey(paramClass);
    }
}
