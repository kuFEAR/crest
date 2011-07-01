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

package org.codegist.crest.serializer.jackson;

import org.codegist.crest.CRestException;
import org.codegist.crest.http.HttpParam;
import org.codegist.crest.serializer.StreamingSerializer;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class JsonEncodedFormJacksonSerializer extends StreamingSerializer<List<HttpParam>> {

    private final ObjectMapper jackson;

    public JsonEncodedFormJacksonSerializer(Map<String, Object> config) {
        this.jackson = JacksonProvider.createSerializer(config);
    }

    // todo clean that....
    public void serialize(List<HttpParam> value, Charset charset, OutputStream out) {
        try {
            Map<String, Object> map = new LinkedHashMap<String, Object>();
            for(HttpParam p : value){
                if(areAllNull(p.getValue())) continue;

                String key = p.getConfig().getName();
                Object existing = map.get(key);
                if(existing == null) {
                    map.put(p.getConfig().getName(), p.getValue().size() == 1 ? p.getValue().iterator().next() : p.getValue());
                }else{
                    if(existing instanceof Collection) {
                        if(p.getValue().size() == 1) {
                            ((Collection)existing).add(p.getValue().iterator().next());
                        }   else{
                            ((Collection)existing).addAll(p.getValue());
                        }
                    }else{
                        Object saved = existing;
                        ArrayList<Object> list = new ArrayList<Object>();
                        list.add(saved);
                        if(p.getValue().size() == 1) {
                            list.add(p.getValue().iterator().next());
                        }   else{
                            list.addAll(p.getValue());
                        }
                        map.put(key, list);
                    }
                }
            }
            jackson.writeValue(out, map);
        } catch (IOException e) {
            throw CRestException.handle(e);
        }
    }
    private boolean areAllNull(Collection<Object> objects){
        for(Object o : objects){
            if(o != null) return false;
        }
        return true;
    }
}
