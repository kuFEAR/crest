/*
 * Copyright 2011 CodeGist.org
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

import org.codegist.common.io.StringBuilderWriter;
import org.codegist.crest.param.EncodedPair;
import org.codegist.crest.param.SimpleEncodedPair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Pattern;

import static org.codegist.common.net.Urls.encode;

/**
 * Set of utility functions to deal with name/value pairs
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public final class Pairs {

    private static final Pattern AMP = Pattern.compile("&");
    private static final Pattern EQUAL = Pattern.compile("=");

    private Pairs(){
        throw new IllegalStateException();
    }

    /**
     * Returns a EncodedPair with the given name/value considered as pre-encoded
     * @param name pre-encoded name
     * @param value pre-encoded value
     * @return the encoded pair object
     */
    public static EncodedPair toPreEncodedPair(String name, String value) {
        return new SimpleEncodedPair(name, value);
    }

    /**
     * Returns a EncodedPair with the given name/value considered as not encoded 
     * @param nameToEncode name to encode
     * @param valueToEncode value to encode
     * @param charset charset to use while encoding the name/value
     * @return the encoded pair object 
     * @throws UnsupportedEncodingException When the given charset is not supported
     */
    public static EncodedPair toPair(String nameToEncode, String valueToEncode, Charset charset) throws UnsupportedEncodingException {
        return toPair(nameToEncode, valueToEncode, charset, false);
    }

    /**
     * <p>Returns a EncodedPair with the given name/value.</p>
     * <p>The encoded flag indicates if name/value need to be encoded with the given charset.</p>
     * @param name name to encode if encoded is set to true
     * @param value value to encode if encoded is set to true
     * @param charset charset to use while encoding the name/value if encoded is set to true
     * @param encoded if true, name/value will be encoded
     * @return the encoded pair object
     * @throws UnsupportedEncodingException When the given charset is not supported
     */
    public static EncodedPair toPair(String name, String value, Charset charset, boolean encoded) throws UnsupportedEncodingException {
        String nameEncoded;
        String valueEncoded;
        if(encoded){
            nameEncoded = name;
            valueEncoded = value;
        }else{
            nameEncoded = encode(name, charset);
            valueEncoded = encode(value, charset);
        }
        return new SimpleEncodedPair(nameEncoded, valueEncoded);
    }

    /**
     * <p>Parse and url-encoded string and returns the list of encoded pairs.</p>
     * @param urlEncoded the url to extract the encoded pairs from
     * @return the list of encoded pair objects
     */
    public static List<EncodedPair> fromUrlEncoded(String urlEncoded) {
        List<EncodedPair> pairs = new ArrayList<EncodedPair>();
        String[] split = AMP.split(urlEncoded);
        for(String param : split){
            String[] paramSplit = EQUAL.split(param);
            pairs.add(toPreEncodedPair(paramSplit[0], paramSplit[1]));
        }
        return pairs;
    }

    /**
     * <p>Sort the given list of encoded pairs by name and by values.</p>
     * <p>Does not alter the provided list.</p>
     * @param pairs list of encoded pair objects to order by
     * @return ordered list of encoded pair objects 
     */
    public static List<EncodedPair> sortByNameAndValues(List<? extends EncodedPair> pairs){
       List<EncodedPair> sorted = new ArrayList<EncodedPair>(pairs);
       Collections.sort(sorted, PAIR_NAME_VALUE_COMPARATOR);
       return sorted;
    }

    /**
     * Joins the given list of encoded pairs using "=" for name/value separator and pairSep for pair separator.
     * @param pairs list of encoded pair to join
     * @param pairSep character to use to join pairs
     * @return joined pairs
     */
    public static String join(List<? extends EncodedPair> pairs, char pairSep){
        return join(pairs, pairSep, '=', false, false);
    }

    /**
     * Joins the given list of encoded pairs using nameValSep for name/value separator and pairSep for pair separator.
     * @param pairs list of encoded pair to join
     * @param pairSep character to use to join pairs
     * @param nameValSep character to use to join name/value for each pair
     * @return joined pairs
     */        
    public static String join(List<? extends EncodedPair> pairs, char pairSep, char nameValSep){
        return join(pairs, pairSep, nameValSep, false, false);
    }

    /**
     * Joins the given list of encoded pairs using nameValSep for name/value separator and pairSep for pair separator.
     * @param pairs list of encoded pair to join
     * @param pairSep character to use to join pairs
     * @param nameValSep character to use to join name/value for each pair
     * @param quoteName if true, will double quote the name while joining 
     * @param quoteVal if true, will double quote the value while joining
     * @return joined pairs
     */
    public static String join(List<? extends EncodedPair> pairs, char pairSep, char nameValSep, boolean quoteName, boolean quoteVal){
        return join(pairs.iterator(), pairSep, nameValSep, quoteName,quoteVal);
    }

    /**
     * Joins the given iterator of encoded pairs using "=" for name/value separator and pairSep for pair separator.
     * @param pairs list of encoded pair to join
     * @param pairSep character to use to join pairs
     * @return joined pairs
     */
    public static String join(Iterator<? extends EncodedPair> pairs, char pairSep){
        return join(pairs, pairSep, '=', false, false);
    }

    /**
     * Joins the given list of encoded pairs using nameValSep for name/value separator and pairSep for pair separator.
     * @param pairs list of encoded pair to join
     * @param pairSep character to use to join pairs
     * @param nameValSep character to use to join name/value for each pair
     * @return joined pairs
     */
    public static String join(Iterator<? extends EncodedPair> pairs, char pairSep, char nameValSep){
        return join(pairs, pairSep, nameValSep, false, false);
    }

    /**
     * Joins the given iterator of encoded pairs using nameValSep for name/value separator and pairSep for pair separator.
     * @param pairs list of encoded pair to join
     * @param pairSep character to use to join pairs
     * @param nameValSep character to use to join name/value for each pair
     * @param quoteName if true, will double quote the name while joining 
     * @param quoteVal if true, will double quote the value while joining
     * @return joined pairs
     */
    public static String join(Iterator<? extends EncodedPair> pairs, char pairSep, char nameValSep, boolean quoteName, boolean quoteVal){
        Writer sw = new StringBuilderWriter();
        try {
            join(sw, pairs,pairSep, nameValSep,quoteName,quoteVal);
        } catch (IOException e) {
            //ignore
        }
        return sw.toString();
    }

    /**
     * Joins the given list of encoded pairs using "=" for name/value separator and pairSep for pair separator.
     * @param writer where the joined pairs are written 
     * @param pairs list of encoded pair to join
     * @param pairSep character to use to join pairs
     * @throws IOException If an exception occures while writing to the writer
     */
    public static void join(Writer writer, List<? extends EncodedPair> pairs, char pairSep) throws IOException {
        join(writer, pairs, pairSep, '=', false, false);
    }

    /**
     * Joins the given list of encoded pairs using nameValSep for name/value separator and pairSep for pair separator.
     * @param writer where the joined pairs are written
     * @param pairs list of encoded pair to join
     * @param pairSep character to use to join pairs
     * @param nameValSep character to use to join name/value for each pair
     * @throws IOException If an exception occures while writing to the writer
     */
    public static void join(Writer writer, List<? extends EncodedPair> pairs, char pairSep, char nameValSep) throws IOException {
        join(writer, pairs, pairSep, nameValSep, false, false);
    }

    /**
     * Joins the given list of encoded pairs using nameValSep for name/value separator and pairSep for pair separator.
     * @param writer where the joined pairs are written
     * @param pairs list of encoded pair to join
     * @param pairSep character to use to join pairs
     * @param nameValSep character to use to join name/value for each pair
     * @param quoteName if true, will double quote the name while joining 
     * @param quoteVal if true, will double quote the value while joining
     * @throws IOException If an exception occures while writing to the writer
     */
    public static void join(Writer writer, List<? extends EncodedPair> pairs, char pairSep, char nameValSep, boolean quoteName, boolean quoteVal) throws IOException {
        join(writer, pairs.iterator(), pairSep, nameValSep, quoteName,quoteVal);
    }

    /**
     * Joins the given iterator of encoded pairs using "=" for name/value separator and pairSep for pair separator.
     * @param writer where the joined pairs are written
     * @param pairs list of encoded pair to join
     * @param pairSep character to use to join pairs
     * @throws IOException If an exception occures while writing to the writer
     */
    public static void join(Writer writer, Iterator<? extends EncodedPair> pairs, char pairSep) throws IOException {
        join(writer, pairs, pairSep, '=', false, false);
    }

    /**
     Joins the given iterator of encoded pairs using nameValSep for name/value separator and pairSep for pair separator.
     * @param writer where the joined pairs are written
     * @param pairs list of encoded pair to join
     * @param pairSep character to use to join pairs
     * @param nameValSep character to use to join name/value for each pair
     * @throws IOException If an exception occures while writing to the writer
     */
    public static void join(Writer writer, Iterator<? extends EncodedPair> pairs, char pairSep, char nameValSep) throws IOException {
        join(writer, pairs, pairSep, nameValSep, false, false);
    }

    /**
     * Joins the given iterator of encoded pairs using nameValSep for name/value separator and pairSep for pair separator.
     * @param writer where the joined pairs are written
     * @param pairs list of encoded pair to join
     * @param pairSep character to use to join pairs
     * @param nameValSep character to use to join name/value for each pair
     * @param quoteName if true, will double quote the name while joining 
     * @param quoteVal if true, will double quote the value while joining
     * @throws IOException If an exception occures while writing to the writer
     */
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
