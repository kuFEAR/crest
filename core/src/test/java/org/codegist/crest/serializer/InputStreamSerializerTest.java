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

import org.codegist.common.io.Files;
import org.codegist.crest.test.util.TestInputStream;
import org.codegist.crest.test.util.Values;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class InputStreamSerializerTest {

    private final InputStreamSerializer toTest = new InputStreamSerializer();

    @Test
    public void shouldCopyInputStreamContentToGivenOutput() throws IOException {
        byte[] inBytes = Files.toByteArray(Values.IMAGE1);
        TestInputStream in = new TestInputStream(new FileInputStream(Values.IMAGE1));

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        toTest.serialize(in, null, out);

        assertArrayEquals(inBytes, out.toByteArray());
        assertTrue(in.hasBeenClosed());        
    }

}
