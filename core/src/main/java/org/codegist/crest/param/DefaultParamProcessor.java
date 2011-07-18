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
import org.codegist.crest.util.Serializers;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.codegist.crest.util.Pairs.toPair;
import static org.codegist.crest.util.Serializers.serialize;

/**
 * @author laurent.gilles@codegist.org
 */
class DefaultParamProcessor implements ParamProcessor {

    static final ParamProcessor INSTANCE = new DefaultParamProcessor();

    public List<EncodedPair> process(Param param, Charset charset, boolean encodeIfNeeded) throws Exception {
        List<EncodedPair> pairs = new ArrayList<EncodedPair>();
        Serializer serializer = param.getConfig().getSerializer();
        boolean isEncoded = !encodeIfNeeded || param.getConfig().isEncoded();
        for(Object value : param.getValue()){
            String serializedValue = Serializers.serialize(serializer, value, charset);
            pairs.add(toPair(param.getConfig().getName(), serializedValue, charset, isEncoded));
        }
        return pairs;
    }
}
