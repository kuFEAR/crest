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

package org.codegist.crest.io.http;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public abstract class HttpParamProcessor {

    protected abstract Collection<Pair> exec(HttpParam params, Charset charset, boolean preEncodeIfNeeded);

    public static Iterator<Pair> iterateProcess(List<HttpParam> params, Charset charset){
        return iterateProcess(params, charset, true);
    }
    public static Iterator<Pair> iterateProcess(List<HttpParam> params, Charset charset, boolean encodeIfNeeded){
        return new ProcessIterator(params, charset, encodeIfNeeded);
    }

    public static Collection<Pair> process(HttpParam param, Charset charset){
        return process(param, charset, true);
    }
    public static Collection<Pair> process(HttpParam param, Charset charset, boolean encodeIfNeeded){
        if(param.getConfig().getListSeparator() != null) {
            return COLLECTION_MERGING_PROCESSOR.exec(param, charset, encodeIfNeeded);
        }else{
            return DEFAULT_PROCESSOR.exec(param, charset, encodeIfNeeded);
        }
    }



    private static final HttpParamProcessor DEFAULT_PROCESSOR = new HttpParamProcessor(){
        @Override
        protected  Collection<Pair> exec(HttpParam param, Charset charset, boolean encodeIfNeeded) {
            Collection<Pair> pairs = new ArrayList<Pair>();
            boolean isEncoded = !encodeIfNeeded || param.getConfig().isEncoded();
            for(Object value : param.getValue()){
                String serializedValue = param.getConfig().getSerializer().serialize(value, charset);
                pairs.add(new Pair(param.getConfig().getName(), serializedValue, charset, isEncoded));
            }

            return pairs;
        }
    };

    private static final HttpParamProcessor COLLECTION_MERGING_PROCESSOR = new HttpParamProcessor(){
        @Override
        protected  Collection<Pair> exec(HttpParam param, Charset charset, boolean encodeIfNeeded) {
            StringBuilder sb = new StringBuilder();
            String separator = param.getConfig().getListSeparator();
            boolean isEncoded = !encodeIfNeeded || param.getConfig().isEncoded();
            boolean first = true;
            for(Object value : param.getValue()){
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
                return singletonList(new Pair(param.getConfig().getName(), sb.toString(), charset, isEncoded));
            }
        }
    };


    private static class ProcessIterator implements Iterator<Pair> {
        final Iterator<HttpParam> params;
        final Charset charset;
        final boolean encodeIfNeeded;

        Iterator<Pair> current;

        private ProcessIterator(List<HttpParam> params, Charset charset, boolean encodeIfNeeded) {
            this.params = params.iterator();
            this.charset = charset;
            this.encodeIfNeeded = encodeIfNeeded;
        }

        public boolean hasNext() {
            if(current != null && current.hasNext()) {
                return true;
            }else if(params.hasNext()){
                doNext();
                return hasNext();
            }else{
                return false;
            }
        }

        public Pair next() {
            return current.next();
        }

        private void doNext() {
            this.current = process(this.params.next(), charset, encodeIfNeeded).iterator();
        }
        public void remove() {

        }
    }

}