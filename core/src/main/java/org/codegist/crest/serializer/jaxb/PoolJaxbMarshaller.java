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

package org.codegist.crest.serializer.jaxb;

import org.codegist.crest.CRestException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
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

    public PooledJaxb(JAXBContext jaxbContext, int poolSize, long maxWait) throws JAXBException {
        this.maxWait = maxWait;
        this.pool = new ArrayBlockingQueue<Jaxb>(poolSize);
        for (int i = 0; i < poolSize; i++) {
            SimpleJaxb jaxb = new SimpleJaxb(jaxbContext);
            this.pool.add(jaxb);
        }
    }

    public <T> void marshal(T object, OutputStream out, Charset charset) throws Exception {
        Jaxb jaxb = borrow();
        try {
            jaxb.marshal(object, out, charset);
        } finally {
            lend(jaxb);
        }
    }

    public <T> T unmarshal(Class<T> type, Type genericType, Reader reader) throws Exception {
        Jaxb jaxb = borrow();
        try {
            return jaxb.<T>unmarshal(type, genericType, reader);
        } finally {
            lend(jaxb);
        }
    }

    private void lend(Jaxb jaxb) {
        pool.offer(jaxb);
    }

    private Jaxb borrow() throws InterruptedException {
        Jaxb jaxb = pool.poll(maxWait, TimeUnit.MILLISECONDS);
        if (jaxb == null) {
            throw new CRestException("No jaxb could have been retrieved in the allowed time window");
        }
        return jaxb;
    }
}