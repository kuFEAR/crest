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

import org.codegist.crest.serializer.SerializerException;

import javax.xml.bind.JAXBContext;
import java.io.OutputStream;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;


class PooledJaxb implements Jaxb {

    private final BlockingQueue<Jaxb> pool;
    private final long maxWait;

    public PooledJaxb(JAXBContext jaxbContext, int poolSize, long maxWait) {
        this.maxWait = maxWait;
        this.pool = new ArrayBlockingQueue<Jaxb>(poolSize);
        for (int i = 0; i < poolSize; i++) {
            this.pool.add(new SimpleJaxb(jaxbContext));
        }
    }

    public <T> void marshal(T object, OutputStream out, Charset charset) {
        marshal(object, out, charset, null);
    }

    public <T> void marshal(T object, OutputStream out, Charset charset, Class<?>[] types) {
        Jaxb jaxb = get();
        try {
            jaxb.marshal(object, out, charset);
        } finally {
            put(jaxb);
        }
    }

    public <T> T unmarshal(Class<T> type, Type genericType, Reader reader) {
        Jaxb jaxb = get();
        try {
            return jaxb.<T>unmarshal(type, genericType, reader);
        } finally {
            put(jaxb);
        }
    }

    private void put(Jaxb jaxb) {
        pool.offer(jaxb);
    }

    private Jaxb get() {
        Jaxb jaxb;
        try {
            jaxb = pool.poll(maxWait, TimeUnit.MILLISECONDS);
            if (jaxb == null)
                throw new SerializerException("No jaxb could have been retrieved in the allowed time window");
        } catch (InterruptedException e) {
            throw new SerializerException(e);
        }
        return jaxb;
    }
}