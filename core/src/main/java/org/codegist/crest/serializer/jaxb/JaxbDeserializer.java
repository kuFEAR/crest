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

package org.codegist.crest.serializer.jaxb;

import org.codegist.common.io.IOs;
import org.codegist.crest.CRestConfig;
import org.codegist.crest.serializer.Deserializer;

import javax.xml.bind.JAXBException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

/**
 * <p><a href="http://jaxb.java.net/">Jaxb</a> XML deserializer implementation</p>
 * <p>As Jaxb is not threadsafe, <b>CRest</b> will hold a pool of Jaxb if the {@link org.codegist.crest.CRestConfig#getConcurrencyLevel()} returns more than 1</p>
 * <p>The pool size will reflect the {@link org.codegist.crest.CRestConfig#getConcurrencyLevel()} value</p>
 * @author laurent.gilles@codegist.org
 */
public class JaxbDeserializer implements Deserializer {

    private static final String PREFIX = JaxbDeserializer.class.getName();

    /**
     * <p>Sets the max time in milliseconds to wait for a Jaxb-pooled instances to become available.</p>
     * <p>As Jaxb is not threadsafe, <b>CRest</b> will hold a pool of Jaxb sized in function of {@link org.codegist.crest.CRestConfig#getConcurrencyLevel()} value</p>
     * <p>If concurrency level is 1, this property is ignored.</p>
     * <p>Can be overridden by setting this property as follow:</p>
     * <code><pre>
     * long milleseconds = ...;
     * CRest crest = CRest.property(JaxbDeserializer.POOL_RETRIEVAL_MAX_WAIT, milleseconds).buid();
     * </pre></code>
     * <p>Default is 30,000 milliseconds</p>
     * <p>Expects a long value</p>
     */
    public static final String POOL_RETRIEVAL_MAX_WAIT_PROP = PREFIX + JaxbFactory.POOL_RETRIEVAL_MAX_WAIT;

    /**
     * <p>CRestConfig property to provide a custom pre-configured {@link org.codegist.crest.serializer.jaxb.Jaxb} instance.</p>
     * <p>Can be overridden by setting this property as follow:</p>
     * <code><pre>
     * Jaxb jaxb = ...;
     * CRest crest = CRest.property(JaxbDeserializer.JAXB_PROP, jaxb).buid();
     * </pre></code>
     * <p>Default is auto-instantiated</p>
     * <p>Expects a {@link org.codegist.crest.serializer.jaxb.Jaxb} value</p>
     */
    public static final String JAXB_PROP = PREFIX + JaxbFactory.JAXB;

    /**
     * <p>CRestConfig property to provide a custom jaxb context package (see {@link javax.xml.bind.JAXBContext#newInstance(String)}).</p>
     * <p>Can be overridden by setting this property as follow:</p>
     * <code><pre>
     * String contextPath = ...;
     * CRest crest = CRest.property(JaxbDeserializer.MODEL_PACKAGE_PROP, contextPath).buid();
     * </pre></code>
     * <p>Default behavior try to auto-detected classes involved if possible and creates a JAXBContext</p>
     * <p>Expects a String value as defined in {@link javax.xml.bind.JAXBContext#newInstance(String)}</p>
     */
    public static final String MODEL_PACKAGE_PROP = PREFIX + JaxbFactory.MODEL_PACKAGE;

    /**
     * <p>CRestConfig property to provide a custom jaxb object factory class (see {@link javax.xml.bind.JAXBContext#newInstance(Class[])}).</p>
     * <p>Can be overridden by setting this property as follow:</p>
     * <code><pre>
     * Class factory = ...;
     * CRest crest = CRest.property(JaxbDeserializer.MODEL_FACTORY_CLASS_PROP, factory).buid();
     * </pre></code>
     * <p>Default behavior try to auto-detected classes involved if possible and creates a JAXBContext</p>
     * <p>Expects a single Class value as defined in {@link javax.xml.bind.JAXBContext#newInstance(Class[])}</p>
     */
    public static final String MODEL_FACTORY_CLASS_PROP = PREFIX + JaxbFactory.MODEL_FACTORY_CLASS;

    private final Jaxb jaxb;

    /**
     * @param crestConfig CRest injected CRestConfig
     * @throws JAXBException For any jaxb creation failure
     */
    public JaxbDeserializer(CRestConfig crestConfig) throws JAXBException {
        this.jaxb = JaxbFactory.create(crestConfig, getClass());
    }

    /**
     * @inheritDoc
     */
    public <T> T deserialize(Class<T> type, Type genericType, InputStream stream, Charset charset) throws Exception {
        try {
            return jaxb.<T>unmarshal(type, genericType, new InputStreamReader(stream, charset));
        }finally{
            IOs.close(stream);
        }
    }

}
