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

import org.codegist.crest.CRestException;
import org.codegist.crest.config.ParamType;

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;

/**
 * <p>Set of utility functions for dealing with {@link org.codegist.crest.param.ParamProcessor} types.</p>
 * @author laurent.gilles@codegist.org
 * @see org.codegist.crest.param.ParamProcessor
 */
public final class ParamProcessors {
    
    private ParamProcessors(){
        throw new IllegalStateException();
    }


    /**
     * Returns an instance of a param processor.
     * @param type parameter type
     * @param listSeparator list separator if applicable, otherwise null
     * @return instance of param processor
     */
    public static ParamProcessor newInstance(ParamType type, String listSeparator){
        switch(type){
            case COOKIE:
                return listSeparator != null ? new CollectionMergingCookieParamProcessor(listSeparator) : DefaultCookieParamProcessor.INSTANCE;
            default:
                return listSeparator != null ? new CollectionMergingParamProcessor(listSeparator) : DefaultParamProcessor.INSTANCE;
        }
    }

    /**
     * Returns an iterator over each param's ParamProcessor results
     * @param params parameters to iterates param processors results from
     * @param charset charset to pass to the param processor
     * @return an iterator over each param's ParamProcessor results
     * @see org.codegist.crest.config.ParamConfig#getParamProcessor()
     */
    public static Iterator<EncodedPair> iterate(List<Param> params, Charset charset){
        return iterate(params, charset, true);
    }

    /**
     * Returns an iterator over each param's ParamProcessor results
     * @param params parameters to iterates param processors results from
     * @param charset charset to pass to the param processor
     * @param encodeIfNeeded if set to false, will turn off auto-encoding for parameter that needs it
     * @return an iterator over each param's ParamProcessor results
     * @see org.codegist.crest.config.ParamConfig#getParamProcessor()
     */
    public static Iterator<EncodedPair> iterate(List<Param> params, Charset charset, boolean encodeIfNeeded){
        return new ProcessIterator(params, charset, encodeIfNeeded);
    }

    private static final class ProcessIterator implements Iterator<EncodedPair> {
        private final Iterator<Param> params;
        private final Charset charset;
        private final boolean encodeIfNeeded;

        private Iterator<EncodedPair> current;

        private ProcessIterator(List<Param> params, Charset charset, boolean encodeIfNeeded) {
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

        public EncodedPair next() {
            return current.next();
        }

        private void doNext() {
            Param currentParam = this.params.next();
            try {
                this.current = currentParam.getParamConfig().getParamProcessor().process(currentParam, charset, encodeIfNeeded).iterator();
            } catch (Exception e) {
                throw CRestException.handle(e);
            }
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
