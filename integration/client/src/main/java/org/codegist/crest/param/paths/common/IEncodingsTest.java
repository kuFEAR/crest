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

package org.codegist.crest.param.paths.common;

import java.io.UnsupportedEncodingException;

import static java.lang.String.format;
import static org.junit.Assert.assertEquals;

/**
 * @author laurent.gilles@codegist.org
 */
public class IEncodingsTest<T extends IEncodingsTest.IEncodings> extends org.codegist.crest.param.common.IEncodingsTest<T> {

    public IEncodingsTest(CRestHolder crest, Class<T> clazz) {
        super(crest, clazz);
    }

    @Override
    public void assertDefault(String p1, String p21, String p22, String actual) {
        assertEquals(format("default() p1=%s p2=%s(p2)%s", p1, p21, p22), actual);
    }

    @Override
    public void assertEncoded(String p1, String p21, String p22, String actual) throws UnsupportedEncodingException {
        assertEquals(format("encoded() p1=%s p2=%s(p2)%s", p1, p21, p22), actual);
    }
}
