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

package org.codegist.crest.param.paths.common;

import static java.lang.String.format;
import static org.junit.Assert.assertEquals;

/**
 * @author laurent.gilles@codegist.org
 */
public class IDefaultValuesTest<T extends IDefaultValuesTest.IDefaultValues> extends org.codegist.crest.param.common.IDefaultValuesTest<T> {

    public IDefaultValuesTest(CRestHolder crest, Class<T> clazz) {
        super(crest, clazz);
    }

    public void assertParamsValue(String p11, String p12, String p2, String p3, String defaultP01, String defaultP02, String defaultP03, String actual) {
        assertEquals(format("param() p1=%s p2=%s p3=%s p01=%s p02=%s p03=%s", p12, p2, p3, defaultP01, defaultP02, defaultP03), actual);
    }

}
