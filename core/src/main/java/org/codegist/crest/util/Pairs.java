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

package org.codegist.crest.util;

import org.codegist.crest.io.http.Pair;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.*;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public final class Pairs {
    private Pairs(){
        throw new IllegalStateException();
    }

    public static List<Pair> fromUrlEncoded(String urlEncoded) throws UnsupportedEncodingException {
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
       Collections.sort(sorted, HTTP_PAIR_NAME_VALUE_COMPARATOR);
       return sorted;
    }

     public static String join(Collection<Pair> pairs, char pairSep){
        return join(pairs, pairSep, '=', false, false);
    }

    public static String join(Collection<Pair> pairs, char pairSep, char nameValSep){
        return join(pairs, pairSep, nameValSep, false, false);
    }

    public static String join(Collection<Pair> pairs, char pairSep, char nameValSep, boolean quoteName, boolean quoteVal){
        return join(pairs.iterator(), pairSep, nameValSep, quoteName,quoteVal);
    }

    public static String join(Iterator<Pair> pairs, char pairSep){
        return join(pairs, pairSep, '=', false, false);
    }

    public static String join(Iterator<Pair> pairs, char pairSep, char nameValSep){
        return join(pairs, pairSep, nameValSep, false, false);
    }

    public static String join(Iterator<Pair> pairs, char pairSep, char nameValSep, boolean quoteName, boolean quoteVal){
        StringWriter sw = new StringWriter();
        try {
            join(sw, pairs,pairSep, nameValSep,quoteName,quoteVal);
        } catch (IOException e) {
            //ignore
        }
        return sw.toString();
    }

    public static void join(Writer writer, Collection<Pair> pairs, char pairSep) throws IOException {
        join(writer, pairs, pairSep, '=', false, false);
    }

    public static void join(Writer writer, Collection<Pair> pairs, char pairSep, char nameValSep) throws IOException {
        join(writer, pairs, pairSep, nameValSep, false, false);
    }

    public static void join(Writer writer, Collection<Pair> pairs, char pairSep, char nameValSep, boolean quoteName, boolean quoteVal) throws IOException {
        join(writer, pairs.iterator(), pairSep, nameValSep, quoteName,quoteVal);
    }

    public static void join(Writer writer, Iterator<Pair> pairs, char pairSep) throws IOException {
        join(writer, pairs, pairSep, '=', false, false);
    }

    public static void join(Writer writer, Iterator<Pair> pairs, char pairSep, char nameValSep) throws IOException {
        join(writer, pairs, pairSep, nameValSep, false, false);
    }

    public static void join(Writer writer, Iterator<Pair> pairs, char pairSep, char nameValSep, boolean quoteName, boolean quoteVal) throws IOException {

        boolean first = true;

        while(pairs.hasNext()) {
            Pair pair = pairs.next();
            if(!first) {
                writer.append(pairSep);
            }

            if(quoteName) {
                writer.append('\"').append(pair.getName()).append('\"');
            }else{
                writer.append(pair.getName());
            }

            writer.append(nameValSep);

            if(quoteVal) {
                writer.append('\"').append(pair.getValue()).append('\"');
            }else{
                writer.append(pair.getValue());
            }

            first = false;
        }
    }


    private static final Comparator<Pair> HTTP_PAIR_NAME_VALUE_COMPARATOR = new Comparator<Pair>() {
        public int compare(Pair o1, Pair o2) {
            int i = o1.getName().compareTo(o2.getName());
            return i != 0 ? i : o1.getValue().compareTo(o2.getValue());
        }
    };
}
