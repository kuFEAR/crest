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

package org.codegist.crest.param.cookies.common;

import org.codegist.crest.BaseCRestTest;
import org.junit.runners.Parameterized;

import java.io.InputStream;
import java.io.Reader;
import java.util.Collection;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.codegist.common.collect.Collections.join;
import static org.junit.Assert.assertEquals;

/**
 * @author laurent.gilles@codegist.org
 */
public class ISpecialParamsTests<T extends ISpecialParamsTests.ISpecialParams> extends org.codegist.crest.param.common.ISpecialParamsTests<T> {

    public ISpecialParamsTests(CRestHolder crest, Class<T> clazz) {
        super(crest, clazz);
    }



    public void assertReader(String p1,String p21, String p22, String actual){
        assertEquals(format("reader(cookies(count:2):[p1=%s,p2=%s,p2=%s]) p1=%s p2=%s", p1, p21, p22, p1, p22), actual);
    }
    public void assertInputStream(String p1,String p21, String p22, String actual){
        assertEquals(format("inputStream(cookies(count:2):[p1=%s,p2=%s,p2=%s]) p1=%s p2=%s", p1, p21, p22, p1, p22), actual);
    }

}
