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

import org.codegist.crest.test.util.TestInputStream;
import org.codegist.crest.test.util.Values;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.codegist.crest.test.util.Values.SOME_STRING_UTF8_BYTES;
import static org.codegist.crest.test.util.Values.UTF8;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class StringDeserializerTest {

    private final StringDeserializer toTest = new StringDeserializer();

    @Test
    public void shouldDumpGivenInputStreamContentIntoString() throws IOException {
        TestInputStream in = new TestInputStream(new ByteArrayInputStream(SOME_STRING_UTF8_BYTES));
        String actual = toTest.deserialize(in, UTF8);
        assertEquals(Values.SOME_STRING, actual);
        assertTrue(in.hasBeenClosed());
    }
}
