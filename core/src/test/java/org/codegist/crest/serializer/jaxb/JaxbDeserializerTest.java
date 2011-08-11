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

import org.codegist.common.io.IOs;
import org.codegist.crest.CRestConfig;
import org.codegist.crest.serializer.BaseDeserializerTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.xml.bind.JAXBException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import static org.codegist.crest.test.util.Values.UTF8;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(JaxbFactory.class)
public class JaxbDeserializerTest extends BaseDeserializerTest {

    private final Jaxb mockJaxb = mock(Jaxb.class);
    private final CRestConfig crestConfig = mock(CRestConfig.class);
    private final JaxbDeserializer toTest = newToTest();

    private JaxbDeserializer newToTest(){
        mockStatic(JaxbFactory.class);
        try {
            when(JaxbFactory.create(crestConfig, JaxbDeserializer.class)).thenReturn(mockJaxb);
            return new JaxbDeserializer(crestConfig);
        } catch (JAXBException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    @Test
    public void shouldDeserializeInputStreamUsingGivenJaxbWithGivenArgs() throws Exception {
        Class arg1 = Object.class;
        Type arg2 = String.class;
        InputStream arg3 = toInputStream("hello");
        Charset arg4 = UTF8;
        Object expected = new Object();

        ArgumentCaptor<InputStreamReader> reader = ArgumentCaptor.forClass(InputStreamReader.class);
        when(mockJaxb.unmarshal(eq(arg1), eq(arg2), reader.capture())).thenReturn(expected);

        Object actual = toTest.deserialize(arg1, arg2, arg3, arg4);

        InputStreamReader actualInputStream = reader.getValue();
        assertEquals("UTF8", actualInputStream.getEncoding());
        assertEquals("hello", IOs.toString(actualInputStream));
        assertSame(expected, actual);

    }
}
