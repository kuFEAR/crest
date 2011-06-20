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

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.codegist.crest.server.utils.ToStrings.string;

/**
 * @author laurent.gilles@codegist.org
 */
final class MultiParts {
    private MultiParts() {

    }

    public static String toResponseString(String meth, MultipartBody msg, String... expecteds) throws UnsupportedEncodingException {
        Map<String, List<String>> values = new TreeMap<String, List<String>>();

        int i = 0, max = msg.getAllAttachments().size() - 1;
        for (Attachment at : msg.getAllAttachments()) {
            if (i == max) break;

            String name = at.getContentDisposition().getParameter("name");
            String value = new String(at.getObject(byte[].class), "utf-8");

            List<String> v = values.get(name);
            if (v == null) {
                v = new ArrayList<String>();
                values.put(name, v);
            }
            v.add(value);
            i++;
        }

        String s = meth + "() ";
        i = 0;
        max = values.size();
        if (values.isEmpty()) {

            for (String expected : expecteds) {
                s += expected + "=null";
                if (++i != expecteds.length) {
                    s += " ";
                }
            }
        } else {
            for (Map.Entry<String, List<String>> entry : values.entrySet()) {
                if (entry.getValue().size() == 1) {
                    s += entry.getKey() + "=" + entry.getValue().get(0);
                } else {
                    s += entry.getKey() + "=" + string(entry.getValue());
                }

                if (++i != max) {
                    s += " ";
                }
            }
        }


        return s;
    }
}
