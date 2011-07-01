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

package org.codegist.crest.serializer.jaxb;

import org.codegist.crest.serializer.StreamingSerializer;

import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Map;

public class JaxbSerializer<T> extends StreamingSerializer<T> {

    public static final String POOL_RETRIEVAL_MAX_WAIT_PROP = JaxbFactory.POOL_RETRIEVAL_MAX_WAIT_PROP;
    public static final String CUSTOM_JAXB = JaxbFactory.CUSTOM_JAXB;
    public static final String MODEL_PACKAGE = JaxbFactory.MODEL_PACKAGE;
    public static final String MODEL_FACTORY_CLASS = JaxbFactory.MODEL_FACTORY_CLASS;

    private final Jaxb jaxb;

    public JaxbSerializer(Map<String, Object> config) {
        this.jaxb = JaxbFactory.create(config);
    }

    public void serialize(T value, Charset charset, OutputStream out) {
        jaxb.marshal(value, out, charset);
    }
}
