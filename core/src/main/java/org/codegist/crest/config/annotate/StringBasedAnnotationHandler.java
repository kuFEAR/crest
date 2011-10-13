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

package org.codegist.crest.config.annotate;

import org.codegist.crest.CRestConfig;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.regex.Pattern;

import static org.codegist.crest.util.Placeholders.merge;

/**
 * @author laurent.gilles@codegist.org
 */
public abstract class StringBasedAnnotationHandler<A extends Annotation> extends NoOpAnnotationHandler<A> {

    private final Map<Pattern, String> placeholders;

    /**
     * @param crestConfig the crest config
     */
    public StringBasedAnnotationHandler(CRestConfig crestConfig) {
        this.placeholders = crestConfig.get(CRestConfig.class.getName() + "#placeholders");
    }

    /**
     * replace any placeholder from the given strings
     * @param strs strings to replace the placeholders from
     * @return same string with the placeholders merged
     * @see org.codegist.crest.CRest#placeholder(String, String)
     */
    protected String[] ph(String... strs) {
        String[] res = new String[strs.length];
        for(int i = 0; i < res.length; i++){
            res[i] = ph(strs[i]);
        }
        return res;
    }

    /**
     * replace any placeholder from the given string
     * @param str string to replace the placeholders from
     * @return string with the placeholders merged
     * @see org.codegist.crest.CRest#placeholder(String, String)
     */
    protected String ph(String str) {
        return merge(placeholders, str);
    }
}
