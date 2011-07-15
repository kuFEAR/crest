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

public final class PairIterator {
    private final Iterator<HttpParam> params;
    private final Charset charset;
    private final boolean encodeIfNeeded;

    private Iterator<Pair> current;

    PairIterator(List<HttpParam> params, Charset charset, boolean encodeIfNeeded) {
        this.params = params.iterator();
        this.charset = charset;
        this.encodeIfNeeded = encodeIfNeeded;
    }

    public boolean hasNext() throws Exception {
        if(current != null && current.hasNext()) {
            return true;
        }else if(params.hasNext()){
            doNext();
            return hasNext();
        }else{
            return false;
        }
    }

    public Pair next() throws Exception {
        return current.next();
    }

    private void doNext() throws Exception {
        HttpParam currentParam = this.params.next();
        this.current = currentParam.getConfig().getParamProcessor().process(currentParam, charset, encodeIfNeeded).iterator();
    }
}