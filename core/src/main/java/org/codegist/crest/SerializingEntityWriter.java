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
import org.codegist.crest.serializer.Serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    public HttpParamMap getHeaders(HttpRequest request) {
        HttpParamMap headers = new HttpParamMap();
        headers.set(new HttpParam("Content-Type", contentType, true));
        return headers;
    }

    public void writeTo(HttpRequest request, OutputStream outputStream) throws IOException {
        Map<String,Object> params = new LinkedHashMap<String,Object>();
        for(Map.Entry<String, List<HttpParam>> entry : request.getFormParamMap().entrySet()){
            String httpParamName = entry.getKey();
            List<HttpParam> httpParams = entry.getValue();
            if(httpParams.size() > 1) {
                Object[] raws = new Object[httpParams.size()];
                for(int i = 0; i < raws.length; i++){
                    raws[i] = httpParams.get(i).getValue().getRaw();
                }
                params.put(httpParamName, raws);
            }else if(httpParams.size() == 1) {
                params.put(httpParamName, httpParams.get(0).getValue().getRaw());
            }
        }
        serializer.serialize(params, outputStream, request.getEncodingAsCharset());
    }
}
