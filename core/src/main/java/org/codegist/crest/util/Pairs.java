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

import org.codegist.crest.param.EncodedPair;
import org.codegist.crest.param.SimpleEncodedPair;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Pattern;

import static org.codegist.common.net.Urls.encode;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public final class Pairs {

    private static final Pattern AMP = Pattern.compile("&");
    private static final Pattern EQUAL = Pattern.compile("=");

    private Pairs(){
        throw new IllegalStateException();
    }
    
    public static EncodedPair toPreEncodedPair(String name, String value) throws UnsupportedEncodingException {
        return toPair(name, value, null, true);
    }
    
    public static EncodedPair toPair(String name, String value, Charset charset, boolean encoded) throws UnsupportedEncodingException {
        String nameEncoded = encoded ? name : encode(name, charset);
        String valueEncoded = encoded ? value : encode(value, charset);
        return new SimpleEncodedPair(nameEncoded, valueEncoded);
    }

    public static List<EncodedPair> fromUrlEncoded(String urlEncoded) throws UnsupportedEncodingException {
        List<EncodedPair> pairs = new ArrayList<EncodedPair>();

        String[] split = AMP.split(urlEncoded);
        for(String param : split){
            String[] paramSplit = EQUAL.split(param);
            EncodedPair pair = toPreEncodedPair(paramSplit[0], paramSplit[1]);
            pairs.add(pair);
        }
        return pairs;
    }

    public static List<EncodedPair> sortByNameAndValues(List<? extends EncodedPair> map){
       List<EncodedPair> sorted = new ArrayList<EncodedPair>(map);
       Collections.sort(sorted, PAIR_NAME_VALUE_COMPARATOR);
       return sorted;
    }

     public static String join(List<? extends EncodedPair> pairs, char pairSep){
        return join(pairs, pairSep, '=', false, false);
    }

    public static String join(List<? extends EncodedPair> pairs, char pairSep, char nameValSep){
        return join(pairs, pairSep, nameValSep, false, false);
    }

    public static String join(List<? extends EncodedPair> pairs, char pairSep, char nameValSep, boolean quoteName, boolean quoteVal){
        return join(pairs.iterator(), pairSep, nameValSep, quoteName,quoteVal);
    }

    public static String join(Iterator<? extends EncodedPair> pairs, char pairSep){
        return join(pairs, pairSep, '=', false, false);
    }

    public static String join(Iterator<? extends EncodedPair> pairs, char pairSep, char nameValSep){
        return join(pairs, pairSep, nameValSep, false, false);
    }

    public static String join(Iterator<? extends EncodedPair> pairs, char pairSep, char nameValSep, boolean quoteName, boolean quoteVal){
        StringWriter sw = new StringWriter();
        try {
            join(sw, pairs,pairSep, nameValSep,quoteName,quoteVal);
        } catch (IOException e) {
            //ignore
        }
        return sw.toString();
    }

    public static void join(Writer writer, List<? extends EncodedPair> pairs, char pairSep) throws IOException {
        join(writer, pairs, pairSep, '=', false, false);
    }

    public static void join(Writer writer, List<? extends EncodedPair> pairs, char pairSep, char nameValSep) throws IOException {
        join(writer, pairs, pairSep, nameValSep, false, false);
    }

    public static void join(Writer writer, List<? extends EncodedPair> pairs, char pairSep, char nameValSep, boolean quoteName, boolean quoteVal) throws IOException {
        join(writer, pairs.iterator(), pairSep, nameValSep, quoteName,quoteVal);
    }

    public static void join(Writer writer, Iterator<? extends EncodedPair> pairs, char pairSep) throws IOException {
        join(writer, pairs, pairSep, '=', false, false);
    }

    public static void join(Writer writer, Iterator<? extends EncodedPair> pairs, char pairSep, char nameValSep) throws IOException {
        join(writer, pairs, pairSep, nameValSep, false, false);
    }

    public static void join(Writer writer, Iterator<? extends EncodedPair> pairs, char pairSep, char nameValSep, boolean quoteName, boolean quoteVal) throws IOException {

        boolean first = true;

        while(pairs.hasNext()) {
            EncodedPair httpEncodedPair = pairs.next();
            if(!first) {
                writer.append(pairSep);
            }

            if(quoteName) {
                writer.append('\"').append(httpEncodedPair.getName()).append('\"');
            }else{
                writer.append(httpEncodedPair.getName());
            }

            writer.append(nameValSep);

            if(quoteVal) {
                writer.append('\"').append(httpEncodedPair.getValue()).append('\"');
            }else{
                writer.append(httpEncodedPair.getValue());
            }

            first = false;
        }
    }


    private static final Comparator<EncodedPair> PAIR_NAME_VALUE_COMPARATOR = new Comparator<EncodedPair>() {
        public int compare(EncodedPair o1, EncodedPair o2) {
            int i = o1.getName().compareTo(o2.getName());
            return i != 0 ? i : o1.getValue().compareTo(o2.getValue());
        }
    };
}
