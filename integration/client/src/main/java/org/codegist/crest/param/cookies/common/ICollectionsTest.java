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

import static java.lang.String.format;
import static org.junit.Assert.assertEquals;

/**
 * @author laurent.gilles@codegist.org
 */
public class ICollectionsTest<T extends ICollectionsTest.ICollections>  extends org.codegist.crest.param.common.ICollectionsTest<T> {

    public ICollectionsTest(CRestHolder crest, Class<T> interfaze) {
        super(crest, interfaze);
    }

    @Override
    public void assertDefaultLists(String p11, String p12, boolean p21, boolean p22, Integer p31, Integer p32, Long p41, Long p42, String actual) {
        assertEquals(format("default(cookies(count:4):[p1=%s,p1=%s,p2=%s,p2=%s,p3=%s,p3=%s,p4=%s,p4=%s]) p1=%s p2=%s p3=%s p4=%s",
                p11, p12,
                toString(p21), toString(p22),
                p31, p32,
                p41, p42,
                p12,
                toString(p22), 
                p32, p42
        ), actual);
    }
}
