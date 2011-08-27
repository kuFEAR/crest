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

import java.io.File;

import static org.mockito.Mockito.mock;

/**
 * @author laurent.gilles@codegist.org
 */
public class MultiPartFileSerializerTest {
    
    private final MultiPartFileSerializer toTest = new MultiPartFileSerializer();

    @Test
    public void shouldSerializeParameterWithNoContentTypeAndNoFileNameButFile() throws Exception {
        File file = new File("some file");
        MultiPartOctetStreamSerializerTest.shouldSerializeParameterWith(toTest, file, "application/octet-stream", "some file");
    }
    
    @Test
    public void shouldSerializeParameterWithGivenContentTypeAndFileName() throws Exception {
        MultiPartOctetStreamSerializerTest.shouldSerializeParameterWith(toTest, mock(File.class), MultiParts.toMetaDatas("some-content-type", "some-file-name"), "some-content-type", "some-file-name");
    }


}
