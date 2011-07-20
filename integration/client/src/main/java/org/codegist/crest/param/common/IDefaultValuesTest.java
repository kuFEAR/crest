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
import java.util.EnumSet;

import static java.lang.String.format;
import static org.codegist.crest.param.common.IDefaultValuesTest.Tests.DefaultParams;
import static org.codegist.crest.param.common.IDefaultValuesTest.Tests.DefaultValue;
import static org.codegist.crest.util.ToStrings.string;
import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeTrue;

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

    public enum Tests {
        DefaultValue,DefaultParams
    }

    public EnumSet<Tests> ignores(){
        return EnumSet.noneOf(Tests.class);
    }

    public void assumeThatTestIsEnabled(Tests test){
        assumeTrue(!ignores().contains(test));
    }

    @Test
    public void testDefaultValue() {
        assumeThatTestIsEnabled(DefaultValue);
        String actual = toTest.value(null, null);
        assertDefaultValue("default-p1", 123, "p01-val", "p02-val", "p03-val", actual);
    }

    public void assertDefaultValue(String defaultP1, int defaultP2, String defaultP01, String defaultP02, String defaultP03, String actual) {
        assertEquals(format("value() p1=%s p2=%s p01=%s p02=%s p03=%s", defaultP1, defaultP2, defaultP01, defaultP02, defaultP03), actual);
    }

    @Test
    public void testDefaultParams() {
        assumeThatTestIsEnabled(DefaultParams);
        String p1 = "p1";
        String actual = toTest.param(p1);
        assertParamsValue("p1-val", p1, "p2-val", "p3-val",  "p01-val", "p02-val", "p03-val", actual);
    }

    public void assertParamsValue(String p11, String p12, String p2, String p3, String defaultP01, String defaultP02, String defaultP03, String actual) {
        assertEquals(format("param() p1=%s p2=%s p3=%s p01=%s p02=%s p03=%s", string(p11, p12), p2, p3, defaultP01, defaultP02, defaultP03), actual);
    }

    /**
     * @author laurent.gilles@codegist.org
     */
    public static interface IDefaultValues {

        String value(String p1, Integer p2);

        String param(String p1);

    }
}
