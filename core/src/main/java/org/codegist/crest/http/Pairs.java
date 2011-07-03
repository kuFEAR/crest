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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public final class Pairs {
    private Pairs(){
        throw new IllegalStateException();
    }

    public static List<Pair> parseUrlEncoded(String urlEncoded){
        List<Pair> pairs = new ArrayList<Pair>();
        String[] split = urlEncoded.split("&");
        for(String param : split){
            String[] paramSplit = param.split("=");
            pairs.add(new Pair(paramSplit[0], paramSplit[1]));
        }
        return pairs;
    }

    public static List<Pair> sortByNameAndValues(List<Pair> map){
       List<Pair> sorted = new ArrayList<Pair>(map);
       Collections.sort(sorted, HTTP_PAIR__NAME_VALUE_COMPARATOR);
       return sorted;
    }


    private static final Comparator<Pair> HTTP_PAIR__NAME_VALUE_COMPARATOR = new Comparator<Pair>() {
        public int compare(Pair o1, Pair o2) {
            int i = o1.getName().compareTo(o2.getName());
            return i != 0 ? i : o1.getValue().compareTo(o2.getValue());
        }
    };
}
