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

package org.codegist.crest.entity.multipart;

import org.codegist.crest.config.ParamConfig;
import org.codegist.crest.serializer.Serializer;
import org.codegist.crest.util.MultiParts;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static org.codegist.crest.test.util.Values.UTF8;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * @author laurent.gilles@codegist.org
 */
public class MultiPartOctetStreamSerializerTest {

    private final AtomicReference<String> filename = new AtomicReference<String>();
    private final MultiPartOctetStreamSerializer<String> toTest = new MultiPartOctetStreamSerializer<String>(){
        @Override
        String getFileName(MultiPart multipart) {
            return filename.get();
        }
    };

    @Test
    public void shouldSerializeParameterWithNoContentTypeAndNoFileName() throws Exception {
        shouldSerializeParameterWith(toTest, "some value");
    }
    @Test
    public void shouldSerializeParameterWithGivenContentTypeAndFileName() throws Exception {
        filename.set("some-file-name");
        shouldSerializeParameterWith(toTest, "some value", MultiParts.toMetaDatas("some-content-type", "some-file-name"), "some-content-type", "some-file-name");
    }

    static <T> void shouldSerializeParameterWith(MultiPartOctetStreamSerializer<T> toTest, T valueToSerialize) throws Exception {
        shouldSerializeParameterWith(toTest, valueToSerialize, new HashMap<String, Object>(), "application/octet-stream", null);
    }
    static <T> void shouldSerializeParameterWith(MultiPartOctetStreamSerializer<T> toTest, T valueToSerialize, String expectedContentType, String expectedFileName) throws Exception {
        shouldSerializeParameterWith(toTest, valueToSerialize,  new HashMap<String, Object>(), expectedContentType, expectedFileName);
    }
    static <T> void shouldSerializeParameterWith(MultiPartOctetStreamSerializer<T> toTest, T valueToSerialize, Map<String,Object> metadatas, String expectedContentType, String expectedFileName) throws Exception {
        Serializer mockSerializer = mock(Serializer.class);
        doAnswer(new Answer() {
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((DataOutputStream)invocation.getArguments()[2]).writeBytes("some serialized value");
                return null;
            }
        }).when(mockSerializer).serialize(eq(valueToSerialize), eq(UTF8), any(OutputStream.class));
        ParamConfig mockParamConfig = mock(ParamConfig.class);
        when(mockParamConfig.getMetaDatas()).thenReturn(metadatas);
        when(mockParamConfig.getName()).thenReturn("p1");
        when(mockParamConfig.getSerializer()).thenReturn(mockSerializer);


        MultiPart<T> multiPart = new MultiPart<T>(mockParamConfig, valueToSerialize, "boundary");
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        toTest.serialize(multiPart, UTF8, out);
        String expected = "--boundary\r\n" +
        "Content-Disposition: form-data; name=\"p1\"" + (expectedFileName != null ? "; filename=\""+expectedFileName+"\"" : "") + "\r\n" +
        "Content-Type: "+expectedContentType+"\r\n" +
        "\r\n" +
        "some serialized value\r\n";

        assertEquals(expected, out.toString());
    }

}
