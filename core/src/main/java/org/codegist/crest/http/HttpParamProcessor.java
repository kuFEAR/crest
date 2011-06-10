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

package org.codegist.crest.http;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.codegist.common.lang.Strings.isNotBlank;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public abstract class HttpParamProcessor {

    protected abstract Collection<Pair> exec(HttpParam params, Charset charset, boolean preEncodeIfNeeded);

    public static Collection<Pair> process(HttpParam param, Charset charset){
        return process(param, charset, true);
    }

    public static Collection<Pair> process(HttpParam param, Charset charset, boolean preEncodeIfNeeded){
        if(isNotBlank(param.getConfig().getListSeparator())) {
            return COLLECTION_MERGING_PROCESSOR.exec(param, charset, preEncodeIfNeeded);
        }else{
            return DEFAULT_PROCESSOR.exec(param, charset, preEncodeIfNeeded);
        }
    }



    private static final HttpParamProcessor DEFAULT_PROCESSOR = new HttpParamProcessor(){
        @Override
        protected  Collection<Pair> exec(HttpParam param, Charset charset, boolean preEncodeIfNeeded) {
            Collection<Pair> pairs = new ArrayList<Pair>();
            boolean isEncoded = !preEncodeIfNeeded || param.getConfig().isEncoded();
            for(Object value : param.getValue()){
                if(value == null) continue;
                String serializedValue = param.getConfig().getSerializer().serialize(value, charset);
                pairs.add(new Pair(param.getConfig().getName(), serializedValue, charset, isEncoded));
            }

            return pairs;
        }
    };

    private static final HttpParamProcessor COLLECTION_MERGING_PROCESSOR = new HttpParamProcessor(){
        @Override
        protected  Collection<Pair> exec(HttpParam param, Charset charset, boolean preEncodeIfNeeded) {
            StringBuilder sb = new StringBuilder();
            String separator = param.getConfig().getListSeparator();
            boolean isEncoded = !preEncodeIfNeeded || param.getConfig().isEncoded();
            boolean first = true;
            for(Object value : param.getValue()){
                if(value == null) continue;
                String serializedValue = param.getConfig().getSerializer().serialize(value, charset);
                if(!first) {
                    sb.append(separator);
                }
                sb.append(serializedValue);
                first = false;
            }

            if(sb.length() == 0) {
                return emptyList();
            }else{
                return asList(new Pair(param.getConfig().getName(), sb.toString(), charset, isEncoded));
            }
        }
    };



}