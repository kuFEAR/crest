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

package org.codegist.crest.impl.io.platform;

import org.codegist.crest.impl.io.HttpChannel;
import org.codegist.crest.impl.io.HttpChannelInitiator;
import org.codegist.crest.impl.io.HttpMethod;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

/**
* @author Laurent Gilles (laurent.gilles@codegist.org)
*/
public final class HttpURLConnectionHttpChannelInitiator implements HttpChannelInitiator {

    public HttpChannel initiate(HttpMethod method, String url, Charset charset) throws IOException {
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        con.setRequestMethod(method.name());
        return new HttpURLConnectionHttpChannel(con, method);
    }

}
