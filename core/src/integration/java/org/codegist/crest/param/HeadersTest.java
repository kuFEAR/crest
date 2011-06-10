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

package org.codegist.crest.param;

import org.codegist.crest.CRest;
import org.codegist.crest.param.common.CommonParamsTest;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.Parameterized;

import java.io.UnsupportedEncodingException;
import java.util.Collection;

import static java.util.Arrays.asList;
import static org.codegist.common.net.Urls.encode;
import static org.junit.Assert.assertEquals;

public class HeadersTest extends CommonParamsTest<Headers> {

    public HeadersTest(CRest crest) {
        super(crest, Headers.class);
    }

    @Parameterized.Parameters
    public static Collection<CRest[]> getData() {
        return crest(byRestServices());
    }

    public void assertPreEncoded(String p1, String p21, String p22, String actual) throws UnsupportedEncodingException {
        String expected = String.format("preEncoded() p1=%s p2=%s", encode(p1, "utf-8"), asList(encode(p21,"utf-8"), encode(p22,"utf-8")));
        assertEquals(expected, actual);
    }

    @Test
    @Ignore("HttpURLConnection.setRequestProperty don't encode properly UTF-8. Works with HttpClient")
    public void testSend() {
    }

    @Test
    @Ignore("HttpURLConnection.setRequestProperty don't encode properly UTF-8. Works with HttpClient")
    public void testDefaultLists() {
    }

    @Test
    @Ignore("HttpURLConnection.setRequestProperty don't encode properly UTF-8. Works with HttpClient")
    public void testMergingLists() {
    }

    @Test
    @Ignore("HttpURLConnection.setRequestProperty don't encode properly UTF-8. Works with HttpClient")
    public void testEncodings() {
    }


}
