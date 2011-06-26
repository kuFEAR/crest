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
import static org.codegist.crest.utils.ToStrings.string;
import static org.junit.Assert.assertEquals;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public abstract class IDefaultValuesTest<T extends IDefaultValuesTest.IDefaultValues> extends BaseCRestTest<T> {

    public IDefaultValuesTest(CRestHolder crest, Class<T> service) {
        super(crest, service);
    }

    @Parameterized.Parameters
    public static Collection<CRestHolder[]> getData() {
        return crest(byRestServices());
    }

    @Test
    public void testDefaultValue() {
        String actual = toTest.value(null, null);
        assertDefaultValue("default-p1", 123, actual);
    }

    public void assertDefaultValue(String defaultP1, int defaultP2, String actual) {
        assertEquals(format("value() p1=%s p2=%s", defaultP1, defaultP2), actual);
    }

    @Test
    public void testDefaultParams() {
        String p1 = "p1";
        String actual = toTest.param(p1);
        assertParamsValue("p1-val", p1, "p2-val", "p3-val", actual);
    }

    public void assertParamsValue(String p11, String p12, String p2, String p3, String actual) {
        assertEquals(format("param() p1=%s p2=%s p3=%s", string(p11, p12), p2, p3), actual);
    }

    /**
     * @author laurent.gilles@codegist.org
     */
    public static interface IDefaultValues {

        String value(String p1, Integer p2);

        String param(String p1);

    }
}
