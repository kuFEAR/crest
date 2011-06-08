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

import org.codegist.common.net.Urls;
import org.codegist.crest.CRestException;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public final class Encoders {
    private Encoders(){
        throw new IllegalStateException();
    }


    public static String encode(String value, Charset charset){
        return encode(value, charset, false);
    }
    public static String encode(String value, Charset charset, boolean quote){
        try {
            String val =  Urls.encode(value, charset.toString());
            if(quote) {
                val = "\"" + val + "\"";
            }
            return val;
        } catch (UnsupportedEncodingException e) {
            throw new CRestException(e);
        }
    }
}
