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

import org.codegist.common.lang.Objects;
import org.codegist.crest.CRestException;
import org.codegist.crest.CRestProperty;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.util.Map;

/**
 * @author laurent.gilles@codegist.org
 */
final class JaxbFactory {

    static final Long DEFAULT_MAX_WAIT = 30000l;
    static final Integer DEFAULT_POOL_SIZE = 1;
    static final String POOL_RETRIEVAL_MAX_WAIT_PROP = "jaxb.pool.retrieval-max-wait";
    static final String CUSTOM_JAXB = "jaxb.custom";
    static final String MODEL_PACKAGE = "jaxb.model.package";
    static final String MODEL_FACTORY_CLASS = "jaxb.model.factory-class";

    private JaxbFactory(){
        throw new IllegalStateException();
    }

    static Jaxb create(Map<String,Object> crestProperties) {
        Jaxb jaxb = (Jaxb) crestProperties.get(CUSTOM_JAXB);
        if(jaxb != null) {
            return jaxb;
        }

        String modelPackage = (String) crestProperties.get(MODEL_PACKAGE);
        Class<?> modelFactory = (Class<?>) crestProperties.get(MODEL_FACTORY_CLASS);

        if(modelPackage != null) {
            return create(crestProperties, modelPackage);
        }else if(modelFactory != null)  {
            return create(crestProperties, modelFactory);
        }else{
            return new TypeCachingJaxb(crestProperties);
        }
    }
    static Jaxb create(Map<String,Object> crestProperties, String context) {
        try {
            return create(JAXBContext.newInstance(context), crestProperties);
        } catch (JAXBException e) {
            throw CRestException.handle(e);
        }
    }
    static Jaxb create(Map<String,Object> crestProperties, Class<?>... classToBeBound) {
        try {
            return create(JAXBContext.newInstance(classToBeBound), crestProperties);
        } catch (JAXBException e) {
            throw CRestException.handle(e);
        }
    }
    static Jaxb create(JAXBContext jaxb, Map<String,Object> crestProperties) {
        int poolSize = Objects.defaultIfNull((Integer) crestProperties.get(CRestProperty.CREST_CONCURRENCY_LEVEL), DEFAULT_POOL_SIZE);
        long maxWait = Objects.defaultIfNull((Long) crestProperties.get(POOL_RETRIEVAL_MAX_WAIT_PROP), DEFAULT_MAX_WAIT);

        if (poolSize == 1) {
            return new SimpleJaxb(jaxb);
        } else {
            return new PooledJaxb(jaxb, poolSize, maxWait);
        }
    }
}
