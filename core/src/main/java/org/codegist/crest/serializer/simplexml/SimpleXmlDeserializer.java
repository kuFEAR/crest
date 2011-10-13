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

package org.codegist.crest.serializer.simplexml;

import org.codegist.common.io.IOs;
import org.codegist.crest.CRestConfig;
import org.codegist.crest.serializer.Deserializer;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

/**
 * <p><a href="http://simple.sourceforge.net/">SimpleXml</a> XML deserializer implementation</p>
 * @author laurent.gilles@codegist.org
 */
public class SimpleXmlDeserializer implements Deserializer {

    private static final String PREFIX = SimpleXmlDeserializer.class.getName();
    private static final boolean DEFAULT_STRICT = true;

    /**
     * <p>CRestConfig property to use strict mode for deserializing xml</p>
     * <p>Can be overridden by setting this property as follow:</p>
     * <code><pre>
     * CRest crest = CRest.property(SimpleXmlDeserializer.STRICT_PROP, false|true).buid();
     * </pre></code>
     * <p>Default is TRUE</p>
     * <p>Expects a Boolean value</p>
     * @see  org.simpleframework.xml.Serializer#read(Class, java.io.Reader, boolean)
     */
    public static final String STRICT_PROP =  PREFIX + "#strict";

    /**
     * <p>CRestConfig property to provide a custom pre-configured SimpleXml {@link org.simpleframework.xml.Serializer} instance.</p>
     * <p>Can be overridden by setting this property as follow:</p>
     * <code><pre>
     * org.simpleframework.xml.Serializer serializer = ...;
     * CRest crest = CRest.property(SimpleXmlDeserializer.SERIALIZER, serializer).buid();
     * </pre></code>
     * <p>Default is auto-instantiated</p>
     * <p>Expects a {@link org.simpleframework.xml.Serializer} value</p>
     * @see  org.simpleframework.xml.Serializer
     */
    public static final String SERIALIZER_PROP =  PREFIX + SimpleXmlFactory.SERIALIZER;

    private final boolean strict;
    private final org.simpleframework.xml.Serializer serializer;

    /**
     * @param crestConfig CRest injected CRestConfig
     */
    public SimpleXmlDeserializer(CRestConfig crestConfig) {
        serializer = SimpleXmlFactory.createSerializer(crestConfig, getClass());
        strict = crestConfig.get(STRICT_PROP, DEFAULT_STRICT);
    }

    /**
     * @inheritDoc
     */
    public <T> T deserialize(Class<T> type, Type genericType, InputStream stream, Charset charset) throws Exception {
        try {
            
            return serializer.read(type, new InputStreamReader(stream, charset), strict);
        } finally {
            IOs.close(stream);
        }
    }

}
