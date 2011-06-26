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

package org.codegist.crest.param.common;

import org.codegist.crest.BaseCRestTest;
import org.junit.Test;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static java.lang.String.format;
import static org.junit.Assert.assertEquals;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public abstract class IBasicsTest<T extends IBasicsTest.IBasics> extends BaseCRestTest<T> {

    public IBasicsTest(CRestHolder crest, Class<T> service) {
        super(crest, service);
    }

    @Parameterized.Parameters
    public static Collection<CRestHolder[]> getData() {
        return crest(byRestServices());
    }

    @Test
    public void testSend() {
        String actual = toTest.send();
        assertSend(actual);
    }

    public void assertSend(String actual) {
        String expected = format("send() p1=null p2=null");
        assertEquals(expected, actual);
    }

    @Test
    public void testSendWithParams() {
        String p1 = "my-value";
        int p2 = 1983;

        String actual = toTest.send(p1, p2);
        assertSendWithParams(p1, p2, actual);
    }

    public void assertSendWithParams(String p1, int p2, String actual) {
        String expected = format("send() p1=%s p2=%s", p1, p2);
        assertEquals(expected, actual);
    }

    /**
     * @author laurent.gilles@codegist.org
     */
    public static interface IBasics {

        String send();

        String send(String p1, int p2);

    }
}
