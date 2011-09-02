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

package org.codegist.crest.serializer;

import org.codegist.crest.CRestConfig;
import org.codegist.crest.io.Response;
import org.codegist.crest.util.Registry;
import org.junit.Test;

import java.io.InputStream;

import static org.codegist.crest.test.util.Values.UTF8;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class ResponseDeserializerByMimeTypeTest {

    private final Deserializer mockDeserializerMime1 = mock(Deserializer.class);
    private final Deserializer mockDeserializerMime2 = mock(Deserializer.class);
    private final CRestConfig crestConfig = mock(CRestConfig.class);
    private final Response mockResponse = mock(Response.class);
    private final Registry<String, Deserializer> registry = new Registry.Builder<String, Deserializer>()
            .register(mockDeserializerMime1, "mime1")
            .register(mockDeserializerMime2, "mime2")
            .build(crestConfig);
    private final ResponseDeserializerByMimeType toTest = new ResponseDeserializerByMimeType(registry);


    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenResponseContentTypeIsNotRecognized() throws Exception {
        when(mockResponse.getContentType()).thenReturn("mime3");
        try {
            toTest.deserialize(mockResponse);
        } catch (Exception e) {
            assertEquals("Cannot deserialize response to response's mimeType 'mime3', cancelling deserialization.\n" +
                    "CRest has a predefined list of 'known' mime-type for common data type (ei:xml, json, plaintext). If the server response if effectively one of these common types, but not part of CRest's default mime type lists, then you can build a CRest instance of follow:\n" +
                    "\n" +
                    "  CRest crest = new CRestBuilder().bindJsonDeserializerWith(\"server-given-mime-type\").build();\n" +
                    "or\n" +
                    "  CRest crest = new CRestBuilder().bindXmlDeserializerWith(\"server-given-mime-type\").build();\n" +
                    "or\n" +
                    "  CRest crest = new CRestBuilder().bindPlainTextDeserializerWith(\"server-given-mime-type\").build();\n" +
                    "\n" +
                    "This will add \"server-given-mime-type\" mime type to the prefedined list of common mime for the respective deserializer.\n" +
                    "Otherwise, if the mime type represent a custom type, then you can write your own deserializer and bind it as follow:\n" +
                    "\n" +
                    "  CRest crest = new CRestBuilder().bindDeserializer(MyOwnTypeDeserializer.class, \"server-given-mime-type\").build();", e.getMessage());
            throw e;
        }
    }

    @Test
    public void shouldDeserializeWithAppropriateDeserializer() throws Exception {
        InputStream stream = mock(InputStream.class);
        when(mockResponse.getContentType()).thenReturn("mime2");
        when(mockResponse.getExpectedType()).thenReturn((Class)int.class);
        when(mockResponse.getExpectedGenericType()).thenReturn(int.class);
        when(mockResponse.getCharset()).thenReturn(UTF8);
        when(mockResponse.asStream()).thenReturn(stream);
        when(mockDeserializerMime2.deserialize(int.class, int.class, stream, UTF8)).thenReturn(123);

        int actual = toTest.<Integer>deserialize(mockResponse);

        assertEquals(123, actual);
    }

}
