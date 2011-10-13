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

package org.codegist.crest.serializer;

import org.codegist.crest.CRestConfig;
import org.codegist.crest.io.Response;
import org.codegist.crest.util.ComponentFactory;
import org.codegist.crest.util.ComponentRegistry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import static org.codegist.crest.test.util.Values.UTF8;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
* @author Laurent Gilles (laurent.gilles@codegist.org)
*/
@RunWith(PowerMockRunner.class)
@PrepareForTest(ComponentFactory.class)
public class ResponseDeserializerByClassTest {

    private final Deserializer2 mockDeserializerInt = mock(Deserializer2.class);
    private final CRestConfig crestConfig = mock(CRestConfig.class);
    private final Response mockResponse = mock(Response.class);
    {
        mockStatic(ComponentFactory.class);
        try {
            when(ComponentFactory.instantiate(eq(Deserializer2.class), any(CRestConfig.class))).thenReturn(mockDeserializerInt);
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }
    private final ComponentRegistry<Class<?>, Deserializer> registry = new ComponentRegistry.Builder<Class<?>,Deserializer>()
            .register(Deserializer1.class, Integer.class)
            .register(Deserializer2.class, int.class)
            .build(crestConfig);
    private final ResponseDeserializerByClass toTest = new ResponseDeserializerByClass(registry);


    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenResponseExpectedTypeIsNotRecognized() throws Exception {
        when(mockResponse.getExpectedType()).thenReturn((Class)String.class);
        try {
            toTest.deserialize(mockResponse);
        } catch (Exception e) {
            assertEquals("Cannot deserialize response to class 'class java.lang.String', cancelling deserialization.\n" +
                    "This happens after response's Content-Type based deserialization have failed deserializing the response because of an unknown or not present response Content-Type.\n" +
                    "CRest has a predefined list of known classes for common data type (ei:primitives, InputStream, Reader etc...). These deserializers are used when CRest cannot deserialize the response based on the server response's content-type.\n" +
                    "The method return type does not fall in the predefined list of known types. You can write your own deserializer and bind it as follow:\n" +
                    "\n" +
                    "  CRest crest = new CRestBuilder().bindDeserializer(MyOwnTypeDeserializer.class, MyOwnType.class).build();\n" +
                    "or, if the server can provide a Content-Type:\n" +
                    "  CRest crest = new CRestBuilder().bindDeserializer(MyOwnTypeDeserializer.class, \"the-content-type\").build();", e.getMessage());
            throw e;
        }
    }

    @Test
    public void shouldDeserializeWithAppropriateDeserializer() throws Exception {
        InputStream stream = mock(InputStream.class);
        when(mockResponse.getExpectedType()).thenReturn((Class)int.class);
        when(mockResponse.getExpectedGenericType()).thenReturn(int.class);
        when(mockResponse.getCharset()).thenReturn(UTF8);
        when(mockResponse.asStream()).thenReturn(stream);
        when(mockDeserializerInt.deserialize(int.class, int.class, stream, UTF8)).thenReturn(123);

        int actual = toTest.<Integer>deserialize(mockResponse);

        assertEquals(123, actual);
    }
    static class Deserializer1 implements Deserializer {
        public <T> T deserialize(Class<T> type, Type genericType, InputStream stream, Charset charset) throws Exception {
            return null;
        }
    }
    static class Deserializer2 implements Deserializer {
        public <T> T deserialize(Class<T> type, Type genericType, InputStream stream, Charset charset) throws Exception {
            return null;
        }
    }

}
