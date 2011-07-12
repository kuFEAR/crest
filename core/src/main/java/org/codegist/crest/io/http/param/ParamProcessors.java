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

package org.codegist.crest.io.http.param;

import org.codegist.crest.io.http.HttpParam;
import org.codegist.crest.io.http.Pair;

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;

/**
 * @author laurent.gilles@codegist.org
 */
public final class ParamProcessors {

    private ParamProcessors(){
        throw new IllegalStateException();
    }

    public static ParamProcessor select(ParamType type, String listSeparator){
        switch(type){
            case COOKIE:
                return listSeparator != null ? new CollectionMergingCookieParamProcessor(listSeparator) : new DefaultCookieParamProcessor();
            default:
                return listSeparator != null ? new CollectionMergingParamProcessor(listSeparator) : new DefaultParamProcessor();
        }
    }

    public static Iterator<Pair> iterate(List<HttpParam> params, Charset charset){
        return iterate(params, charset, true);
    }

    public static Iterator<Pair> iterate(List<HttpParam> params, Charset charset, boolean encodeIfNeeded){
        return new ProcessIterator(params, charset, encodeIfNeeded);
    }

    private static final class ProcessIterator implements Iterator<Pair> {
        private final Iterator<HttpParam> params;
        private final Charset charset;
        private final boolean encodeIfNeeded;

        private Iterator<Pair> current;

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
            HttpParam currentParam = this.params.next();
            this.current = currentParam.getConfig().getParamProcessor().process(currentParam, charset, encodeIfNeeded).iterator();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
