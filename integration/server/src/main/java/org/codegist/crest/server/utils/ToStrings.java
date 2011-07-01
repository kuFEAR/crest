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

package org.codegist.crest.server.utils;

import com.sun.jersey.multipart.FormDataBodyPart;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.codegist.common.collect.Collections.join;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class ToStrings {
    public static String stringCookie(Collection<String> cookies, int expected) {
        String headers;
        if (cookies.isEmpty() || cookies.size() == expected) {
            headers = "";
        } else {
//            List<String> cookieStrs = new ArrayList<String>();
//            for(String c : cookies){
//                cookieStrs.add(c.getName()+"="+c.getValue());
//            }
            headers = format("cookies(count:%d):[%s]", cookies.size(), join(",", cookies));
        }
        return headers;
    }


    public static String string(Object... value) {
        return string(asList(value));
    }
    public static String string(FormDataBodyPart part) throws UnsupportedEncodingException {
        return part != null ? new String(part.getValueAs(byte[].class), "utf-8") : "null";
    }
    public static String stringMulti(List<FormDataBodyPart> parts) throws UnsupportedEncodingException {
        if(parts == null) {
            return "null";
        }
        List<String> values = new ArrayList<String>();
        for(FormDataBodyPart part : parts){
            values.add(string(part));
        }
        return string(values);
    }
    public static String string(List<String> values) {
        return format("list(size:%d):[%s]", values.size(), join(",", values));
    }
    public static String string(Collection values) {
        return format("list(size:%d):[%s]", values.size(), join(",", values));
    }
}
