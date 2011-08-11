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

import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.OutputStream;
import java.io.Reader;
import java.lang.reflect.Type;

import static org.codegist.crest.test.util.Values.UTF8;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class SimpleJaxbTest {

    private final Marshaller mockMarshaller = mock(Marshaller.class);
    private final Unmarshaller mockUnmarshaller = mock(Unmarshaller.class);
    private final SimpleJaxb toTest = newToTest();

    @Test
    public void shouldMarshalByDelegatingToJaxbMarshallerWithGivenArgument() throws JAXBException {
        Object arg = new Object();
        OutputStream outputStream = mock(OutputStream.class);
        toTest.marshal(arg, outputStream, UTF8);
        verify(mockMarshaller).setProperty(Marshaller.JAXB_ENCODING, UTF8.displayName());
        verify(mockMarshaller).marshal(arg, outputStream);
    }
    @Test
    public void shouldUnmarshalByDelegatingToJaxbUnmarshallerWithGivenArgument() throws JAXBException {
        Object expected = new Object();
        Class arg = Object.class;
        Type arg2 = String.class;
        Reader reader = mock(Reader .class);
        when(mockUnmarshaller.unmarshal(reader)).thenReturn(expected);
        Object actual = toTest.unmarshal(arg, arg2, reader);
        verify(mockUnmarshaller).unmarshal(reader);
        assertSame(expected, actual);
    }

    private SimpleJaxb newToTest(){
        JAXBContext mockJaxbContext = mock(JAXBContext.class);
        try {
            when(mockJaxbContext.createMarshaller()).thenReturn(mockMarshaller);
            when(mockJaxbContext.createUnmarshaller()).thenReturn(mockUnmarshaller);
            return new SimpleJaxb(mockJaxbContext);
        } catch (JAXBException e) {
            throw new ExceptionInInitializerError(e);
        }
    }


}
