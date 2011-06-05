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

import org.codegist.crest.serializer.Serializer;
import org.codegist.crest.serializer.UrlEncodedHttpParamSerializer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * @author laurent.gilles@codegist.org
 */
public class UrlEncodedFormEntityWriter implements EntityWriter {

    private final String multiValuedParamSeparator;

    public UrlEncodedFormEntityWriter(Map<String,Object> customProperties) {
        this.multiValuedParamSeparator = (String) customProperties.get(CRestProperty.PARAM_COLLECTION_SEPARATOR);

    }

    public HttpParamMap getHeaders(HttpRequest request) {
        HttpParamMap headers = new HttpParamMap();
        headers.put(new HttpParam("Content-Type", "application/x-www-form-urlencoded; charset=" + request.getEncoding(), true));
        return headers;
    }

    public void writeTo(HttpRequest request, OutputStream out) throws IOException {
        Serializer<Map<String, List<HttpParam>>> paramSerializer;

        if(multiValuedParamSeparator == null) {
            paramSerializer = UrlEncodedHttpParamSerializer.createDefaultForMap("&");
        }else{
            paramSerializer = UrlEncodedHttpParamSerializer.createCollectionMergingForMap("&", multiValuedParamSeparator);
        }

        paramSerializer.serialize(request.getFormParamMap(), out, request.getEncodingAsCharset());
    }

}
