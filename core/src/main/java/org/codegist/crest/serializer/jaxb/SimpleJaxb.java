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

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.OutputStream;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

/**
 * @author laurent.gilles@codegist.org
 */
class SimpleJaxb implements Jaxb {

    private final Marshaller marshaller;
    private final Unmarshaller unmarshaller;

    public SimpleJaxb(JAXBContext jaxbContext) throws JAXBException {
        this.marshaller = jaxbContext.createMarshaller();
        this.unmarshaller = jaxbContext.createUnmarshaller();
    }

    public <T> void marshal(T object, OutputStream out, Charset charset) throws JAXBException {
        marshaller.setProperty(Marshaller.JAXB_ENCODING, charset.displayName());
        marshaller.marshal(object, out);
    }

    public <T> T unmarshal(Class<T> type, Type genericType, Reader reader) throws JAXBException {
        return (T) unmarshaller.unmarshal(reader);
    }

}
