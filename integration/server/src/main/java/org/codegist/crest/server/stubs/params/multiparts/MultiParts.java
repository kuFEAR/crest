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

package org.codegist.crest.server.stubs.params.multiparts;

import com.sun.jersey.multipart.FormDataBodyPart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.codegist.common.lang.Strings.isNotBlank;

/**
 * @author laurent.gilles@codegist.org
 */
final class MultiParts {
    private MultiParts() {

    }

    public static String toResponseString(String name, List<FormDataBodyPart> parts, String... expected){
        StringBuilder sb = new StringBuilder(name).append("() ");
        Map<String,String> expecteds = new HashMap<String,String>();
        for(String e : expected) {
            expecteds.put(e,e);
        }
        boolean first = true;
        for(FormDataBodyPart part : parts){
            if(!first) {
                sb.append(" ");
            }
            first = false;
            sb.append(part.getName()).append("=");
            if(isNotBlank(part.getContentDisposition().getFileName())) {
                sb.append("filename=").append(part.getContentDisposition().getFileName());
            }

            sb.append(part.getValueAs(String.class));
            expecteds.remove(part.getName());
        }
        for(String e : expecteds.values()){
            sb.append(e).append("=null");
        }
        return sb.toString();
    }

}
