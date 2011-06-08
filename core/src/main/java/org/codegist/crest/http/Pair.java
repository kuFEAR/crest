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

import java.nio.charset.Charset;

import static org.codegist.crest.http.Encoders.encode;

/**
 * @author laurent.gilles@codegist.org
 */
public class Pair {
    private final String name;
    private final String value;
    private final Charset charset;

    public Pair(String name, String value, Charset charset, boolean encoded) {
        this.name = encoded ? name : encode(name, charset);
        this.value = encoded ? value : encode(value, charset);
        this.charset = charset;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public Charset getCharset() {
        return charset;
    }
}
