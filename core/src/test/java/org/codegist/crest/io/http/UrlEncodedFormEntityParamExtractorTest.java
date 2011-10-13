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

package org.codegist.crest.io.http;

import org.codegist.crest.param.EncodedPair;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.codegist.crest.test.util.Values.URL_ENCODED_FORM;
import static org.codegist.crest.test.util.Values.UTF8;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author laurent.gilles@codegist.org
 */
public class UrlEncodedFormEntityParamExtractorTest {

    private final UrlEncodedFormEntityParamExtractor toTest = new UrlEncodedFormEntityParamExtractor();

    @Test
    public void shouldReturnParsedPairsOutOfUrlEncodedEntity() throws IOException {
        InputStream stream = new ByteArrayInputStream(URL_ENCODED_FORM.getBytes());
        List<EncodedPair> actual = toTest.extract(null, UTF8, stream);

        assertEquals(3, actual.size());
        assertEquals("p1", actual.get(0).getName());
        assertEquals("v%201", actual.get(0).getValue());
        assertEquals("p1", actual.get(1).getName());
        assertEquals("v%202", actual.get(1).getValue());
        assertEquals("p3", actual.get(2).getName());
        assertEquals("v3", actual.get(2).getValue());
    }

    @Test
    public void shouldReturnEmptyListForEmptyEntity() throws IOException {
        InputStream stream = new ByteArrayInputStream("".getBytes());
        assertTrue(toTest.extract(null, UTF8, stream).isEmpty());
    }

}
