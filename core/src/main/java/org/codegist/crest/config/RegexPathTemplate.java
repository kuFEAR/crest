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

package org.codegist.crest.config;

import org.codegist.common.net.Urls;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.codegist.common.lang.Validate.isTrue;

/**
 * @author laurent.gilles@codegist.org
 */
public final class RegexPathTemplate implements PathTemplate {

    private static final Pattern TEMPLATE_PATTERN = Pattern.compile("(\\w[-\\w\\.]*)(?::(.+))?");
    private static final Pattern DEFAULT_PATTERN = Pattern.compile("([^/]+?)");

    private final String urlTemplate;
    private final Map<String, PathTemplate> templates;

    private RegexPathTemplate(String urlTemplate, Map<String, PathTemplate> templates) {
        this.urlTemplate = urlTemplate;
        this.templates = templates;
    }

    public PathBuilder getBuilder(String encoding) {
        return new DefaultPathBuilder(encoding);
    }

    public String getUrlTemplate() {
        return urlTemplate;
    }

    private final class DefaultPathBuilder implements PathBuilder {

        private final Map<String, PathTemplate> remainingTemplates = new HashMap<String, PathTemplate>(templates);
        private final StringBuilder url = new StringBuilder(urlTemplate);
        private final String encoding;

        private DefaultPathBuilder(String encoding) {
            this.encoding = encoding;
        }

        public PathBuilder merge(String templateName, String templateValue, boolean encoded) throws UnsupportedEncodingException {
            if (!remainingTemplates.containsKey(templateName)) {
                throw new IllegalArgumentException("Path parameters is unknown or has already been provided for base uri '" + urlTemplate + "'! Param: " + templateName);
            }
            PathTemplate template = remainingTemplates.remove(templateName);
            template.validate(templateValue);
            String tmpl = "{" + templateName + "}";

            int start;
            while((start = url.indexOf(tmpl)) != -1) {
                url.replace(start, start + tmpl.length(), encode(templateValue, encoded));
            }
            return this;
        }

        private String encode(String value, boolean encoded) throws UnsupportedEncodingException {
            return encoded ? value : Urls.encode(value, encoding);
        }

        public String build() {
            if (!remainingTemplates.isEmpty()) {
                throw new IllegalStateException("Not all path templates have been merged! (url=" + url + ")");
            }
            return url.toString();
        }
    }

    private static final class PathTemplate {
        private final String name;
        private final Pattern validator;

        private PathTemplate(String name, Pattern validator) {
            this.name = name;
            this.validator = validator;
        }

        public String getName() {
            return name;
        }

        public void validate(String value) {
            if (!validator.matcher(value).matches()) {
                throw new IllegalArgumentException("Path param " + name + "=" + value + " don't matches expected format " + validator);
            }
        }
    }

    public static RegexPathTemplate create(String urlTemplate) {
        StringBuffer baseUrl = new StringBuffer();
        Map<String, PathTemplate> templates = new HashMap<String, PathTemplate>();
        CurlyBraceTokenizer t = new CurlyBraceTokenizer(urlTemplate);
        while (t.hasNext()) {
            String tok = t.next();
            if (CurlyBraceTokenizer.insideBraces(tok)) {
                tok = CurlyBraceTokenizer.stripBraces(tok);
                Matcher m = TEMPLATE_PATTERN.matcher(tok);
                String name;
                Pattern regex;
                if (m.find()) {
                    name = m.group(1);
                    regex = m.group(2) != null ? Pattern.compile(m.group(2)) : DEFAULT_PATTERN;
                } else {
                    throw new IllegalArgumentException("Template doesn't match the expected format: " + TEMPLATE_PATTERN);
                }
                if (templates.containsKey(name)) {
                    throw new IllegalArgumentException("Template with name=" + name + " already defined!");
                }
                templates.put(name, new PathTemplate(name, regex));
                baseUrl.append("{").append(name).append("}");
            } else {
                baseUrl.append(tok);
            }
        }
        String url = baseUrl.toString();
        isTrue(!Urls.hasQueryString(url), "Given url contains a query string: %s", url);
        return new RegexPathTemplate(url, templates);
    }

//
//    public static void main(String[] args) {
//        RegexPathTemplate.create("http://localhost/fgfg{aaa:\\d+}/{bbb:.*}/{ccc:\\d{5}}/{ddd}/df").buildFor(
//                new HashMap<String, String>() {{
//                    put("aaa", "123");
//                    put("bbb", "ss");
//                    put("ccc", "12345");
//                    put("ddd", "dd");
//                }},
//                null, null
//        );
//    }


    /**
     * Splits string into parts inside and outside curly braces. Nested curly braces are ignored and treated as
     * part inside top-level curly braces. Example: string "foo{bar{baz}}blah" is split into three tokens, "foo",
     * "{bar{baz}}" and "blah". When closed bracket is missing, whole unclosed part is returned as one token,
     * e.g.: "foo{bar" is split into "foo" and "{bar". When opening bracket is missing, closing bracked is ignored
     * and taken as part of current token e.g.: "foo{bar}baz}blah" is split into "foo", "{bar}" and "baz}blah".
     * <p/>
     * This is helper class that enables recurring literals appearing next to regular
     * expressions e.g. "/foo/{zipcode:[0-9]{5}}/". Nested expressions with closed sections, like open-closed
     * brackets causes expression to be out of regular grammar (is context-free grammar) which are not supported
     * by Java regexp version.
     *
     * @author amichalec
     * @version $Rev:  $
     */
    private static final class CurlyBraceTokenizer {

        private List<String> tokens = new ArrayList<String>();
        private int tokenIdx;

        public CurlyBraceTokenizer(String string) {
            boolean outside = true;
            int level = 0;
            int lastIdx = 0;
            int idx;
            for (idx = 0; idx < string.length(); idx++) {
                if (string.charAt(idx) == '{') {
                    if (outside) {
                        if (lastIdx < idx) {
                            tokens.add(string.substring(lastIdx, idx));
                        }
                        lastIdx = idx;
                        outside = false;
                    } else {
                        level++;
                    }
                } else if (string.charAt(idx) == '}' && !outside) {
                    if (level > 0) {
                        level--;
                    } else {
                        if (lastIdx < idx) {
                            tokens.add(string.substring(lastIdx, idx + 1));
                        }
                        lastIdx = idx + 1;
                        outside = true;
                    }
                }
            }
            if (lastIdx < idx) {
                tokens.add(string.substring(lastIdx, idx));
            }
        }

        /**
         * Token is enclosed by curly braces.
         *
         * @param token text to verify
         * @return true if enclosed, false otherwise.
         */
        public static boolean insideBraces(String token) {
            return token.charAt(0) == '{' && token.charAt(token.length() - 1) == '}';
        }

        /**
         * Strips token from enclosed curly braces. If token is not enclosed method
         * has no side effect.
         *
         * @param token text to verify
         * @return text stripped from curly brace begin-end pair.
         */
        public static String stripBraces(String token) {
            if (insideBraces(token)) {
                return token.substring(1, token.length() - 1);
            } else {
                return token;
            }
        }

        public boolean hasNext() {
            return tokens.size() > tokenIdx;
        }

        public String next() {
            if (hasNext()) {
                return tokens.get(tokenIdx++);
            } else {
                throw new IllegalStateException("no more elements");
            }
        }
    }

}
