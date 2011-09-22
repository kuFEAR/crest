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

import org.codegist.common.io.IOs;
import org.codegist.crest.param.EncodedPair;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.codegist.common.lang.Strings.isNotBlank;
import static org.codegist.crest.util.Pairs.fromUrlEncoded;

/**
 * EntityParamExtractor implementation that extract parameters from an URL-encoded entity
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class UrlEncodedFormEntityParamExtractor implements EntityParamExtractor {

    /**
     * @inheritDoc
     */
    public List<EncodedPair> extract(String contentType, Charset charset, InputStream entity) throws IOException {
        String formContent = IOs.toString(entity, charset);
        if(isNotBlank(formContent)) {
            return fromUrlEncoded(formContent);
        }else{
            return emptyList();
        }
    }

}
