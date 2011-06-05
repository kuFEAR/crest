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

package org.codegist.crest.resources;

import org.codegist.crest.CRest;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static org.junit.Assert.assertEquals;

//@Ignore
public class PutsTest extends BaseCRestTest {
                                      
    private final Puts toTest = crest.build(Puts.class);

    public PutsTest(CRest crest) {
        super(crest);
    }

    @Parameterized.Parameters
    public static Collection<CRest[]> getData() {
        return data(byRestServices());
    }

    @Test
    public void testPut(){
        String actual = toTest.put();
        assertEquals("put", actual);
    }

    @Test
    public void testPutForm(){
        String actual = toTest.putForm(UTF8_VALUE, 1983, new float[]{1.2f,2.3f,3.4f});
        assertEquals("putForm f1=" + UTF8_VALUE + " f2=1983 f3=[1.2, 2.3, 3.4]", actual);
    }

    @Test
    public void testPutQuery(){
        String actual = toTest.putQuery(UTF8_VALUE, 1983, new float[]{1.2f,2.3f,3.4f});
        assertEquals("putQuery q1=" + UTF8_VALUE + " q2=1983 q3=[1.2, 2.3, 3.4]", actual);
    }

    @Test
    public void testPutMatrix(){
        String actual = toTest.putMatrix(UTF8_VALUE, 1983, new float[]{1.2f,2.3f,3.4f});
        assertEquals("putMatrix m1=" + UTF8_VALUE + " m2=1983 m3=[1.2, 2.3, 3.4]", actual);
    }

    @Test
    public void testPutPath(){
        String actual = toTest.putPath(UTF8_VALUE, 1983, 1.2f);
        assertEquals("putPath p1=" + UTF8_VALUE + " p2=1983 p3=1.2", actual);
    }

    @Test
    public void testPutHeader(){
        String actual = toTest.putHeader(ISO_VALUE, 1983, new float[]{1.2f,2.3f,3.4f});
        assertEquals("putHeader h1=" + ISO_VALUE + " h2=1983 h3=[1.2, 2.3, 3.4]", actual);
    }

    @Test
    public void testPutCookie(){
        String actual = toTest.putCookie(ISO_VALUE, 1983, new float[]{1.2f,2.3f,3.4f});
        assertEquals("putCookie(header:[c1="+ISO_VALUE+", c2=1983, c3=1.2, c3=2.3, c3=3.4]) c1="+ISO_VALUE+" c2=1983 c3=3.4", actual);
    }

}
