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

import java.util.Arrays;

import static java.util.Arrays.toString;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public final class BaseParams {
    private BaseParams() {
        throw new IllegalStateException();
    }

    public static void assertSendsEquals(Params toTest, String p1, int p2, float[] p3) {
        assertSendsEquals(toTest, p1, p2, p3, "");
    }

    public static void assertSendsEquals(Params toTest, String p1, int p2, float[] p3, String extra) {
        assertSendsEquals(toTest, p1, p2, p3, extra, Arrays.toString(p3));
    }
    public static void assertSendsEquals(Params toTest, String p1, int p2, float[] p3, String expectedExtra, String expectedP3) {


        String expected = String.format("receive(%s) p1=%s p2=%s p3=%s", expectedExtra, p1, p2, expectedP3);

        String actual = toTest.send(p1, p2, p3);
        org.junit.Assert.assertEquals(expected, actual);
    }

}
