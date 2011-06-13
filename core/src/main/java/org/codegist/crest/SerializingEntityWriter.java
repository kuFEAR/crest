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

import org.codegist.common.lang.Validate;
import org.codegist.crest.http.HttpParam;
import org.codegist.crest.http.HttpRequest;
import org.codegist.crest.http.Pair;
import org.codegist.crest.serializer.Serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.*;

import static org.codegist.crest.http.HttpParamProcessor.process;

/**
 * @author laurent.gilles@codegist.org
 */
public class SerializingEntityWriter implements EntityWriter {

    private final Serializer<Map<String,Object>> serializer;
    private final String contentType;

    public SerializingEntityWriter(Serializer<Map<String,Object>> serializer, String contentType) {
        Validate.notNull(serializer, "Serializer can't be null");
        Validate.notNull(contentType, "ContentType can't be null");
        this.serializer = serializer;
        this.contentType = contentType;
    }

    public String getContentType(HttpRequest request) {
        return contentType;
    }

    private void addTo(Map<String,Object> params, String name, Object singleValue){
        Object existing = params.get(name);
        if(existing == null) {
            params.put(name, singleValue);
        }else if(existing instanceof Collection) {
            ((Collection) existing).add(singleValue);
        }else {
            params.remove(name);
            List<Object> newCollection = new ArrayList<Object>(2);
            newCollection.add(existing);
            newCollection.add(singleValue);
            params.put(name, newCollection);
        }
    }

    public void writeTo(HttpRequest request, OutputStream outputStream) throws IOException {

        Charset charset = request.getCharset();
        Map<String,Object> params = new LinkedHashMap<String,Object>();
        for(HttpParam param : request.getFormParam()){
            Class<?> clazz = param.getConfig().getValueClass();
             String name = param.getConfig().getName();
            if(clazz.isPrimitive()
                    || Number.class.isAssignableFrom(clazz)
                    || Boolean.class.isAssignableFrom(clazz)
                    || Character.class.isAssignableFrom(clazz)
                    || String.class.isAssignableFrom(clazz)
                    || Date.class.isAssignableFrom(clazz)  ) {
                for(Pair pair : process(param, charset, false)){
                    addTo(params, pair.getName(), pair.getValue());
                }
            }else{
                for(Object value : param.getValue()){
                    if(value == null) continue;
                    addTo(params, name, value);
                }
            }
        }

        serializer.serialize(params, request.getCharset(), outputStream);
    }
}
