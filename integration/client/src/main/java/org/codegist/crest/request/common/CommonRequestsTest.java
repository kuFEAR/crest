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

package org.codegist.crest.request.common;

import org.codegist.crest.BaseCRestTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public abstract class CommonRequestsTest<T extends Requests> extends BaseCRestTest<T> {

    public CommonRequestsTest(CRestHolder crest, Class<T> service) {
        super(crest, service);
    }


    @Test
    public void testRaw() {
        String actual = toTest.raw().toString();
        assertRaw(actual);
    }

    public static void assertRaw(String actual) {
        assertEquals("raw() content-type-header=[] accepts-header=[*/*]", actual);
    }

    @Test
    public void testAccept() {
        String actual = toTest.accept().toString();
        assertAccept(actual);
    }

    public static void assertAccept(String actual) {
        assertEquals("accept() content-type-header=[] accepts-header=[application/custom1,application/custom2]", actual);
    }

    @Test
    public void testContentType() {
        String actual = toTest.contentType().toString();
        assertContentType(actual);
    }

    public static void assertContentType(String actual) {
        assertEquals("contentType() content-type-header=[application/custom] accepts-header=[*/*]", actual);
    }
}
