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

package org.codegist.crest.param;

import org.codegist.crest.serializer.Serializer;

import java.nio.charset.Charset;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.codegist.crest.util.Pairs.toPair;
import static org.codegist.crest.util.Serializers.serialize;

/**
 * @author laurent.gilles@codegist.org
 */
class CollectionMergingParamProcessor implements ParamProcessor {

    private final String listSeparator;

    public CollectionMergingParamProcessor(String listSeparator) {
        this.listSeparator = listSeparator;
    }

    public List<EncodedPair> process(Param param, Charset charset, boolean encodeIfNeeded) throws Exception {
        StringBuilder sb = new StringBuilder();
        Serializer serializer = param.getParamConfig().getSerializer();
        boolean isEncoded = !encodeIfNeeded || param.getParamConfig().isEncoded();
        boolean first = true;
        for(Object value : param.getValue()){
            String serializedValue = serialize(serializer, value, charset);
            if(!first) {
                sb.append(listSeparator);
            }
            sb.append(serializedValue);
            first = false;
        }

        if(sb.length() == 0) {
            return emptyList();
        }else{
            return singletonList(toPair(param.getParamConfig().getName(), sb.toString(), charset, isEncoded));
        }
    }

}
