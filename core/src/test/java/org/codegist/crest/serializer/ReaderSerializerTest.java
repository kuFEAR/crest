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

import org.codegist.crest.test.util.TestReader;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;

import static org.codegist.crest.test.util.Values.SOME_STRING;
import static org.codegist.crest.test.util.Values.UTF8;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class ReaderSerializerTest {

    private final ReaderSerializer toTest = new ReaderSerializer();

    @Test
    public void shouldDumpReaderContentIntoOutputStream() throws IOException {
        TestReader in = new TestReader(new StringReader(SOME_STRING));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        toTest.serialize(in, UTF8, out);
        assertEquals(SOME_STRING, new String(out.toByteArray(), UTF8));
        assertTrue(in.hasBeenClosed());
    }

}
