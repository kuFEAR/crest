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

package org.codegist.crest.serializer;

import org.codegist.common.net.Urls;
import org.codegist.crest.CRestException;
import org.codegist.crest.HttpParam;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

public abstract class UrlEncodedHttpParamSerializer<T> extends StreamingSerializer<T> {



    public static UrlEncodedHttpParamSerializer<Map<String, List<HttpParam>>> createDefaultForMap(String paramSeparator) {
        return createDefaultForMap(paramSeparator, false, false);
    }
    public static UrlEncodedHttpParamSerializer<Map<String, List<HttpParam>>> createDefaultForMap(String paramSeparator, boolean quoteName, boolean quoteValue) {
        return new DefaultUrlEncodedHttpParamMapSerializer(paramSeparator,quoteName,quoteValue);
    }

    public static UrlEncodedHttpParamSerializer<Map<String, List<HttpParam>>> createCollectionMergingForMap(String paramSeparator, String collectionSeparator) {
        return createCollectionMergingForMap(paramSeparator, collectionSeparator, false, false);
    }
    public static UrlEncodedHttpParamSerializer<Map<String, List<HttpParam>>> createCollectionMergingForMap(String paramSeparator, String collectionSeparator, boolean quoteName, boolean quoteValue)  {
        return new CollectionMergingUrlEncodedHttpParamMapSerializer(paramSeparator,quoteName,quoteValue,collectionSeparator);
    }

    public static UrlEncodedHttpParamSerializer<List<HttpParam>> createParamValuesSerializer(String paramSeparator)  {
        return new UrlEncodedHttpParamsSerializer(paramSeparator);
    }

    public static UrlEncodedHttpParamSerializer<HttpParam> createSingleParamSerializer()  {
        return createSingleParamSerializer(false,false);
    }
    public static UrlEncodedHttpParamSerializer<HttpParam> createSingleParamSerializer(boolean quoteName, boolean quoteValue)  {
        return new UrlEncodedSingleHttpParamSerializer(quoteName, quoteValue);
    }


    protected String valueAsString(HttpParam param){
        return param.getValue() != null ? param.getValue().asString() : "";
    }

    protected String encode(String value, Charset charset, boolean encoded, boolean quote){
        try {
            String val =  encoded ? value : Urls.encode(value, charset.toString());
            if(quote) {
                val = "\"" + val + "\"";
            }
            return val;
        } catch (UnsupportedEncodingException e) {
            throw new CRestException(e);
        }
    }

    protected String encodeValue(HttpParam param, Charset charset, boolean quote) {
        String valueStr = valueAsString(param);
        return encode(valueStr, charset, param.isEncoded(), quote);
    }

    protected String encodeName(HttpParam param, Charset charset, boolean quote) {
        String valueStr = param.getName() != null ? param.getName() : "";
        return encode(valueStr, charset, param.isEncoded(), quote);
    }


    private static abstract class UrlEncodedHttpParamMapSerializer extends UrlEncodedHttpParamSerializer<Map<String, List<HttpParam>>> {

        protected final boolean quoteName;
        protected final boolean quoteValue;
        protected final String paramSeparator;

        protected UrlEncodedHttpParamMapSerializer(boolean quoteName, boolean quoteValue, String paramSeparator) {
            this.paramSeparator= paramSeparator;
            this.quoteName = quoteName;
            this.quoteValue = quoteValue;
        }

        protected abstract void writeTo(List<HttpParam> params, PrintStream outputStream, Charset charset);

        protected String encodeValue(HttpParam param, Charset charset) {
            String valueStr = valueAsString(param);
            return encode(valueStr, charset, param.isEncoded(), quoteValue);
        }

        protected String encodeName(HttpParam param, Charset charset) {
            String valueStr = param.getName() != null ? param.getName() : "";
            return encode(valueStr, charset, param.isEncoded(), quoteName);
        }

        public void serialize(Map<String,List<HttpParam>> paramMap, OutputStream outputStream, Charset charset) {
            PrintStream out = new PrintStream(outputStream);
            int i = 0, max = paramMap.size();
            for (List<HttpParam> params : paramMap.values()) {
                writeTo(params, out, charset);
                if (++i < max) {
                    out.append(paramSeparator);
                }
            }
        }


    }


    private static class CollectionMergingUrlEncodedHttpParamMapSerializer extends UrlEncodedHttpParamMapSerializer {

        private final String collectionSeparator;

        private CollectionMergingUrlEncodedHttpParamMapSerializer(String paramSeparator, boolean quoteName, boolean quoteValue, String collectionSeparator) {
            super(quoteName, quoteValue, paramSeparator);
            this.collectionSeparator = collectionSeparator;
        }

        @Override
        protected void writeTo(List<HttpParam> params, PrintStream out, Charset charset) {
            int i = 0, len = params.size();
            if (len == 0) return;
            out.append(encodeName(params.get(0), charset));
            out.append("=");
            for (HttpParam param : params) {
                out.append(encodeValue(param, charset));
                if (++i < len) {
                    out.append(collectionSeparator);
                }
            }
        }

    }

    private static class DefaultUrlEncodedHttpParamMapSerializer extends UrlEncodedHttpParamMapSerializer {

        private DefaultUrlEncodedHttpParamMapSerializer(String paramSeparator, boolean quoteName, boolean quoteValue) {
            super(quoteName, quoteValue, paramSeparator);
        }

        @Override
        protected void writeTo(List<HttpParam> params, PrintStream out, Charset charset) {
            int i = 0, len = params.size();
            for (HttpParam param : params) {
                out.append(encodeName(param, charset));
                out.append("=");
                out.append(encodeValue(param, charset));
                if (++i < len) {
                    out.append(paramSeparator);
                }
            }
        }
    }

    private static class UrlEncodedHttpParamsSerializer extends UrlEncodedHttpParamSerializer<List<HttpParam>> {

        private final String paramSeparator;

        private UrlEncodedHttpParamsSerializer(String paramSeparator) {
            this.paramSeparator = paramSeparator;
        }

        public void serialize(List<HttpParam> params, OutputStream outputStream, Charset charset) throws SerializerException {
            int i = 0, len = params.size();
            if (len == 0) return;
            PrintStream out = new PrintStream(outputStream);
            for (HttpParam param : params) {
                String valueStr = valueAsString(param);
                out.append(encode(valueStr, charset, param.isEncoded(), false));
                if (++i < len) {
                    out.append(paramSeparator);
                }
            }
        }
    }

    private static class UrlEncodedSingleHttpParamSerializer extends UrlEncodedHttpParamSerializer<HttpParam> {

        private final boolean quoteName;
        private final boolean quoteValue;

        private UrlEncodedSingleHttpParamSerializer(boolean quoteName, boolean quoteValue) {
            this.quoteName = quoteName;
            this.quoteValue = quoteValue;
        }

        public void serialize(HttpParam param, OutputStream outputStream, Charset charset) throws SerializerException {
            String valueStr = valueAsString(param);
            PrintStream out = new PrintStream(outputStream);
            out.append(encodeName(param, charset, quoteName));
            out.append("=");
            out.append(encodeValue(param, charset, quoteValue));
        }
    }
}