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

import org.codegist.crest.serializer.DeserializerException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.OutputStream;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * @author laurent.gilles@codegist.org
 */
class SimpleJaxb implements Jaxb {

    private final Marshaller marshaller;
    private final Unmarshaller unmarshaller;

    public SimpleJaxb(Map<String,Object> customProperties, JAXBContext jaxbContext) {
        try {
            this.marshaller = jaxbContext.createMarshaller();
            this.unmarshaller = jaxbContext.createUnmarshaller();
        } catch (JAXBException e) {
            throw new DeserializerException(e);
        }
    }

    public <T> void marshal(T object, OutputStream out, Charset charset, Class<?>... types) {
        try {
            marshaller.setProperty(Marshaller.JAXB_ENCODING, charset.toString());
            marshaller.marshal(object, out);
        } catch (JAXBException e) {
            throw new DeserializerException(e);
        }
    }

    public <T> void marshal(T object, OutputStream out, Charset charset) {
        marshal(object, out, charset, null);
    }

    public <T> T unmarshal(Class<T> type, Type genericType, Reader reader) {
        try {
            return (T) unmarshaller.unmarshal(reader);
        } catch (JAXBException e) {
            throw new DeserializerException(e);
        }
    }

}
