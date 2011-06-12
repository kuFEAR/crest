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

package org.codegist.crest;

import org.codegist.crest.http.HttpParam;
import org.codegist.crest.http.HttpRequest;
import org.codegist.crest.http.Pair;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

import static java.util.Arrays.asList;
import static org.codegist.crest.http.HttpParamProcessor.process;

/**
 * @author laurent.gilles@codegist.org
 */
public class UrlEncodedFormEntityWriter extends AbstractEntityWriter {

    public String getContentType(HttpRequest request) {
        return "application/x-www-form-urlencoded; charset=" + request.getEncoding();
    }

    public void writeTo(HttpRequest request, OutputStream out) throws IOException {
        PrintStream print = new PrintStream(out);
        
        boolean first = true;
        for(HttpParam param : request.getFormParam()){
            for(Pair encoded : process(param, request.getCharset())){
                if(!first) {
                    print.append("&");
                }
                print.append(encoded.getName()).append("=").append(encoded.getValue());
                first = false;
            }
        }
    }

}
