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

package org.codegist.crest.security.oauth;

import org.codegist.common.io.IOs;
import org.codegist.crest.http.Pair;
import org.codegist.crest.http.Pairs;
import org.codegist.crest.security.http.HttpEntityParamsParser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.codegist.common.lang.Strings.isNotBlank;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class UrlEncodedFormEntityParamsParser implements HttpEntityParamsParser {
    public List<Pair> parse(String contentType, Charset charset, InputStream httpEntity) throws IOException {
        List<Pair> params = new ArrayList<Pair>();
        String formContent = IOs.toString(httpEntity, charset);
        if(isNotBlank(formContent)) {
            params.addAll(Pairs.parseUrlEncoded(formContent));
        }
        return params;
    }
}
