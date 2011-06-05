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
 *  ==================================================================
 *
 *  More information at http://www.codegist.org.
 */

package org.codegist.crest;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author laurent.gilles@codegist.org
 */
public class HttpParamMap extends LinkedHashMap<String, List<HttpParam>> {

    public HttpParamMap() {
        super();
    }

    public HttpParamMap(HttpParamMap paramMap) {
        super(paramMap);
    }

    public List<HttpParam> set(HttpParam value) {
        List<HttpParam> existing = get(value.getName());
        List<HttpParam> newList = new ArrayList<HttpParam>();
        newList.add(value);
        put(value.getName(), newList);
        return existing;
    }

    public void put(HttpParam value) {
        List<HttpParam> existing = get(value.getName());
        if(existing == null) {
            existing = new ArrayList<HttpParam>();
            put(value.getName(), existing);
        }
        existing.add(value);
    }

    public void setAll(Map<? extends String, ? extends List<HttpParam>> map) {
        super.putAll(map);
    }
    
    public void putAll(Map<? extends String, ? extends List<HttpParam>> map) {
        for(List<HttpParam> values : map.values()){
            for(HttpParam value : values) {
                put(value);
            }
        }
    }

    public List<HttpParam> allValues(){
        List<HttpParam> values = new ArrayList<HttpParam>();
        for(List<HttpParam> vals : values()){
            values.addAll(vals);
        }
        return values;
    }




    
}
