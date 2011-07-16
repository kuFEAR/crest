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

package org.codegist.crest.impl.param;

import org.codegist.crest.param.CollectionMergingParamProcessor;
import org.codegist.crest.param.EncodedPair;
import org.codegist.crest.param.Param;

import java.nio.charset.Charset;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.codegist.crest.util.Pairs.join;
import static org.codegist.crest.util.Pairs.toPreEncodedPair;

/**
 * @author laurent.gilles@codegist.org
 */
public class CollectionMergingCookieParamProcessor extends CollectionMergingParamProcessor {

    public CollectionMergingCookieParamProcessor(String listSeparator) {
        super(listSeparator);
    }

    @Override
    public List<EncodedPair> process(Param param, Charset charset, boolean encodeIfNeeded) throws Exception {
        List<EncodedPair> pairs = super.process(param, charset, encodeIfNeeded);
        String cookie = join(pairs, ',');
        return singletonList(toPreEncodedPair("Cookie", cookie));
    }
}
