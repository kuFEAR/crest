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

import org.codegist.crest.config.MethodConfig;
import org.codegist.crest.config.PathBuilder;
import org.codegist.crest.io.Request;
import org.codegist.crest.param.EncodedPair;

import java.nio.charset.Charset;
import java.util.Iterator;

import static org.codegist.common.lang.Strings.isNotBlank;
import static org.codegist.crest.config.ParamType.*;
import static org.codegist.crest.util.Pairs.join;

/**
 * @author laurent.gilles@codegist.org
 */
public final class HttpRequests {

    private HttpRequests() {
        throw new IllegalStateException();
    }

    public static String toUrl(Request request) throws Exception {
        MethodConfig mc = request.getMethodConfig();
        Charset charset = mc.getCharset();
        PathBuilder pathBuilder = mc.getPathTemplate().getBuilder(charset);

        Iterator<EncodedPair> pathParamsIterator = request.getEncodedParamsIterator(PATH);
        while(pathParamsIterator.hasNext()){
            EncodedPair encoded = pathParamsIterator.next();
            pathBuilder.merge(encoded.getName(), encoded.getValue(), true);
        }

        String query = join(request.getEncodedParamsIterator(QUERY), '&');
        if(isNotBlank(query)) {
            query = "?" + query;
        }

        String matrix = join(request.getEncodedParamsIterator(MATRIX), ';');
        if(isNotBlank(matrix)) {
            matrix = ";" + matrix;
        }

        return pathBuilder.build() + matrix + query;
    }
}
