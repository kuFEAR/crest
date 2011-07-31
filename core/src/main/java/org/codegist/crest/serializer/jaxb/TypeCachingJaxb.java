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

import org.codegist.common.reflect.Types;
import org.codegist.crest.CRestConfig;

import javax.xml.bind.JAXBException;
import java.io.OutputStream;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author laurent.gilles@codegist.org
 */
class TypeCachingJaxb implements Jaxb {

    private final CRestConfig crestConfig;
    private final ConcurrentMap<Set<Class<?>>, Jaxb> cache = new ConcurrentHashMap<Set<Class<?>>, Jaxb>();
    private final Class<?> source;

    TypeCachingJaxb(CRestConfig crestConfig, Class<?> source) {
        this.crestConfig = crestConfig;
        this.source = source;
    }

    public <T> void marshal(T object, OutputStream out, Charset charset) throws Exception {
        Jaxb jaxb;
        if(object instanceof Classes) {
            jaxb = get(((Classes) object).getClasses());
        }else{
            jaxb = get(Collections.<Class<?>>singleton(object.getClass()));
        }
        jaxb.<T>marshal(object, out, charset);
    }

    public <T> T unmarshal(Class<T> type, Type genericType, Reader reader) throws Exception {
        Set<Class<?>> classes = Types.getActors(genericType);
        classes.add(type);

        Jaxb jaxb = get(classes);
        return jaxb.<T>unmarshal(type, genericType, reader);
    }

    private Jaxb get(Set<Class<?>> key) throws JAXBException {
        Jaxb jaxb = cache.get(key);
        if(jaxb == null) {
            jaxb = JaxbFactory.create(crestConfig, source, key.toArray(new Class<?>[key.size()]));
            Jaxb previousJaxb = cache.putIfAbsent(key, jaxb);
            jaxb = previousJaxb != null ? previousJaxb : jaxb;
        }
        return jaxb;
    }

    

}
