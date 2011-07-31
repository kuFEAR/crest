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

import org.codegist.crest.CRestConfig;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

/**
 * @author laurent.gilles@codegist.org
 */
final class JaxbFactory {

    private static final Long DEFAULT_MAX_WAIT = 30000l;
    static final String POOL_RETRIEVAL_MAX_WAIT = "#pool.retrieval.max-wait";
    static final String JAXB = "#jaxb";
    static final String MODEL_PACKAGE = "#model.package";
    static final String MODEL_FACTORY_CLASS = "#model.factory-class";

    private JaxbFactory(){
        throw new IllegalStateException();
    }

    static Jaxb create(CRestConfig crestConfig, Class<?> source) throws JAXBException {
        String prefix = source.getName();

        Jaxb jaxb = crestConfig.get(prefix + JAXB);
        if(jaxb != null) {
            return jaxb;
        }

        String modelPackage = crestConfig.get(prefix + MODEL_PACKAGE);
        if(modelPackage != null) {
            return create(crestConfig, source, modelPackage);
        }

        Class<?> modelFactory = crestConfig.get(prefix + MODEL_FACTORY_CLASS);
        if(modelFactory != null)  {
            return create(crestConfig, source, modelFactory);
        }

        return new TypeCachingJaxb(crestConfig, source);
    }

    static Jaxb create(CRestConfig crestConfig, Class<?> source, String context) throws JAXBException {
        return create(crestConfig, source, JAXBContext.newInstance(context));
    }

    static Jaxb create(CRestConfig crestConfig, Class<?> source, Class<?>... classToBeBound) throws JAXBException {
        return create(crestConfig, source, JAXBContext.newInstance(classToBeBound));
    }

    private static Jaxb create(CRestConfig crestConfig, Class<?> source, JAXBContext jaxb) throws JAXBException {
        int poolSize = crestConfig.getConcurrencyLevel();
        if (poolSize == 1) {
            return new SimpleJaxb(jaxb);
        } else {
            String prefix = source.getName();
            long maxWait = crestConfig.get(prefix + POOL_RETRIEVAL_MAX_WAIT, DEFAULT_MAX_WAIT);
            return new PooledJaxb(jaxb, poolSize, maxWait);
        }
    }
}
