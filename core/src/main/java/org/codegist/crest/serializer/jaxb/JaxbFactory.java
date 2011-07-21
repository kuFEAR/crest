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

import org.codegist.crest.CRestProperty;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.util.Map;

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

    static Jaxb create(Map<String,Object> crestProperties, Class<?> source) throws JAXBException {
        String prefix = source.getName();
        Jaxb jaxb = CRestProperty.get(crestProperties, prefix + JAXB);
        if(jaxb != null) {
            return jaxb;
        }

        String modelPackage = CRestProperty.get(crestProperties, prefix + MODEL_PACKAGE);
        Class<?> modelFactory = CRestProperty.get(crestProperties, prefix + MODEL_FACTORY_CLASS);

        if(modelPackage != null) {
            return create(crestProperties, source, modelPackage);
        }else if(modelFactory != null)  {
            return create(crestProperties, source, modelFactory);
        }else{
            return new TypeCachingJaxb(crestProperties, source);
        }
    }
    static Jaxb create(Map<String,Object> crestProperties, Class<?> source, String context) throws JAXBException {
        return create(crestProperties, source, JAXBContext.newInstance(context));
    }
    static Jaxb create(Map<String,Object> crestProperties, Class<?> source, Class<?>... classToBeBound) throws JAXBException {
        return create(crestProperties, source, JAXBContext.newInstance(classToBeBound));
    }
    static Jaxb create(Map<String,Object> crestProperties, Class<?> source, JAXBContext jaxb) throws JAXBException {
        String prefix = source.getName();
        int poolSize = CRestProperty.getConcurrencyLevel(crestProperties);
        long maxWait = CRestProperty.get(crestProperties, prefix + POOL_RETRIEVAL_MAX_WAIT, DEFAULT_MAX_WAIT);

        if (poolSize == 1) {
            return new SimpleJaxb(jaxb);
        } else {
            return new PooledJaxb(jaxb, poolSize, maxWait);
        }
    }
}
