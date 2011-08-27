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

package org.codegist.crest.entity.multipart;

import org.codegist.crest.config.ParamConfig;
import org.codegist.crest.param.EncodedPair;
import org.codegist.crest.param.Param;
import org.codegist.crest.param.ParamProcessor;
import org.codegist.crest.util.MultiParts;
import org.codegist.crest.util.Pairs;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.util.List;

import static java.util.Arrays.asList;
import static org.codegist.crest.test.util.Values.UTF8;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author laurent.gilles@codegist.org
 */
public class MultiPartTextSerializerTest {

    private final MultiPartTextSerializer toTest = MultiPartTextSerializer.INSTANCE;

    @Test
    public void shouldSerializeParameterWithNoContentTypeAndNoFileName() throws Exception {
        shouldSerializeParameterWith(null, null);
    }
    @Test
    public void shouldSerializeParameterWithGivenContentTypeAndFileName() throws Exception {
        shouldSerializeParameterWith("some-content-type", "some-file-name");
    }
    private void shouldSerializeParameterWith(String contentType, String fileName) throws Exception {
        List<EncodedPair> pairs = asList(
                Pairs.toPreEncodedPair("aa", "v1"),// name is irrelevant here
                Pairs.toPreEncodedPair("bb", "v2") // name is irrelevant here
        );
        Param mockParam = mock(Param.class);

        ParamProcessor mockParamProcessor = mock(ParamProcessor.class);
        when(mockParamProcessor.process(mockParam, UTF8, false)).thenReturn(pairs);

        ParamConfig mockParamConfig = mock(ParamConfig.class);
        when(mockParamConfig.getMetaDatas()).thenReturn(MultiParts.toMetaDatas(contentType, fileName));
        when(mockParamConfig.getParamProcessor()).thenReturn(mockParamProcessor);
        when(mockParamConfig.getName()).thenReturn("p1");


        MultiPart<Param> multiPart = new MultiPart<Param>(mockParamConfig, mockParam, "boundary");
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        toTest.serialize(multiPart, UTF8, out);
        String expected = "--boundary\r\n" +
        "Content-Disposition: form-data; name=\"p1\"" + (fileName != null ? "; filename=\""+fileName+"\"" : "") + "\r\n" +
        "Content-Type: "+(contentType == null ? "text/plain" : contentType)+"; charset=UTF-8\r\n" +
        "\r\n" +
        "v1\r\n" +
        "--boundary\r\n" +
        "Content-Disposition: form-data; name=\"p1\"" + (fileName != null ? "; filename=\""+fileName+"\"" : "") + "\r\n" +
        "Content-Type: "+(contentType == null ? "text/plain" : contentType)+"; charset=UTF-8\r\n" +
        "\r\n" +
        "v2\r\n";

        assertEquals(expected, out.toString());
    }
    
}
