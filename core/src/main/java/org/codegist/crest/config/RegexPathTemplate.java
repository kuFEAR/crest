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

import org.codegist.common.lang.State;
import org.codegist.common.lang.Validate;
import org.codegist.common.net.Urls;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static org.codegist.common.lang.Validate.isTrue;

/**
 * @author laurent.gilles@codegist.org
 */
public final class RegexPathTemplate implements PathTemplate {

    private static final Pattern TEMPLATE_NAME_PATTERN = Pattern.compile("^\\w[-\\w\\.]+$");
    private static final Pattern DEFAULT_VALIDATION_PATTERN = Pattern.compile("^[^/]+$");

    private final String urlTemplate;
    private final Map<String, PathTemplate> templates;

    private RegexPathTemplate(String urlTemplate, Map<String, PathTemplate> templates) {
        this.urlTemplate = urlTemplate;
        this.templates = templates;
    }

    public PathBuilder getBuilder(Charset charset) {
        return new DefaultPathBuilder(charset);
    }

    private final class DefaultPathBuilder implements PathBuilder {

        private final Map<String, PathTemplate> remainingTemplates = new HashMap<String, PathTemplate>(templates);
        private final StringBuilder url = new StringBuilder(urlTemplate);
        private final Charset charset;

        private DefaultPathBuilder(Charset charset) {
            this.charset = charset;
        }

        public PathBuilder merge(String templateName, String templateValue, boolean encoded) throws UnsupportedEncodingException {
            Validate.isTrue(remainingTemplates.containsKey(templateName), "Path parameters is unknown or has already been provided for base uri '%s' (template:%s)! Param: %s", url, urlTemplate, templateName);
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
            return encoded ? value : Urls.encode(value, charset);
        }

        public String build() {
            State.isTrue(remainingTemplates.isEmpty(), "Not all path templates have been merged! (url=%s)", url);
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

        public void validate(String value) {
            Validate.isTrue(validator.matcher(value).matches(), "Path param %s=%s don't matches expected format %s" , name,value,validator);
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

                int index = tok.indexOf(':'); // first index of : as it can't appears in the name
                String name;
                Pattern validationPattern;
                if(index > -1) {
                    name = tok.substring(0, index);
                    validationPattern = Pattern.compile("^" + tok.substring(index + 1) + "$");
                }else{
                    name = tok;
                    validationPattern = DEFAULT_VALIDATION_PATTERN;
                }
                Validate.isTrue(TEMPLATE_NAME_PATTERN.matcher(name).matches(), "Template name '%s' doesn't match the expected format: %s", name, TEMPLATE_NAME_PATTERN);
                Validate.isFalse(templates.containsKey(name), "Template name '%s' is already defined!", name);
                templates.put(name, new PathTemplate(name, validationPattern));
                baseUrl.append("{").append(name).append("}");
            } else {
                baseUrl.append(tok);
            }
        }
        String url = baseUrl.toString();
        isTrue(!Urls.hasQueryString(url), "Given url contains a query string: %s", url);
        return new RegexPathTemplate(url, templates);
    }



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
         * Strips token from enclosed curly braces. 
         *
         * @param token text to verify
         * @return text stripped from curly brace begin-end pair.
         */
        public static String stripBraces(String token) {
            return token.substring(1, token.length() - 1);
        }

        public boolean hasNext() {
            return tokens.size() > tokenIdx;
        }

        public String next() {
            return tokens.get(tokenIdx++);
        }
    }

}
