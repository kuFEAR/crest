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

import org.codegist.crest.util.MultiParts;
import org.junit.Test;

import java.io.InputStream;

import static org.mockito.Mockito.mock;

/**
 * @author laurent.gilles@codegist.org
 */
public class MultiPartInputStreamSerializerTest {
    
    private final MultiPartInputStreamSerializer toTest = new MultiPartInputStreamSerializer();

    @Test
    public void shouldSerializeParameterWithNoContentTypeAndNoFileName() throws Exception {
        MultiPartOctetStreamSerializerTest.shouldSerializeParameterWith(toTest, mock(InputStream.class));
    }
    @Test
    public void shouldSerializeParameterWithGivenContentTypeAndFileName() throws Exception {
        MultiPartOctetStreamSerializerTest.shouldSerializeParameterWith(toTest, mock(InputStream.class), MultiParts.toMetaDatas("some-content-type", "some-file-name"), "some-content-type", "some-file-name");
    }


}
