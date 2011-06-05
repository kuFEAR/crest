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

package org.codegist.crest.method;

import org.codegist.crest.BaseCRestTest;
import org.codegist.crest.CRest;
import org.junit.Test;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static org.junit.Assert.assertEquals;

//@Ignore
public class PostsTest extends BaseCRestTest<Posts> {
                                      
    public PostsTest(CRest crest) {
        super(crest, Posts.class);
    }

    @Parameterized.Parameters
    public static Collection<CRest[]> getData() {
        return crest(byRestServices());
    }

    @Test
    public void testPost(){
        String actual = toTest.post();
        assertEquals("post", actual);
    }

    @Test
    public void testPostForm(){
        String actual = toTest.postForm(UTF8_VALUE, 1983, new float[]{1.2f,2.3f,3.4f});
        assertEquals("postForm f1=" + UTF8_VALUE + " f2=1983 f3=[1.2, 2.3, 3.4]", actual);
    }

    @Test
    public void testPostQuery(){
        String actual = toTest.postQuery(UTF8_VALUE, 1983, new float[]{1.2f,2.3f,3.4f});
        assertEquals("postQuery q1=" + UTF8_VALUE + " q2=1983 q3=[1.2, 2.3, 3.4]", actual);
    }

    @Test
    public void testPostMatrix(){
        String actual = toTest.postMatrix(UTF8_VALUE, 1983, new float[]{1.2f,2.3f,3.4f});
        assertEquals("postMatrix m1=" + UTF8_VALUE + " m2=1983 m3=[1.2, 2.3, 3.4]", actual);
    }

    @Test
    public void testPostPath(){
        String actual = toTest.postPath(UTF8_VALUE, 1983, 1.2f);
        assertEquals("postPath p1=" + UTF8_VALUE + " p2=1983 p3=1.2", actual);
    }

    @Test
    public void testPostHeader(){
        String actual = toTest.postHeader(ISO_VALUE, 1983, new float[]{1.2f,2.3f,3.4f});
        assertEquals("postHeader h1=" + ISO_VALUE + " h2=1983 h3=[1.2, 2.3, 3.4]", actual);
    }

    @Test
    public void testPostCookie(){
        String actual = toTest.postCookie(ISO_VALUE, 1983, new float[]{1.2f,2.3f,3.4f});
        assertEquals("postCookie(header:[c1="+ISO_VALUE+", c2=1983, c3=1.2, c3=2.3, c3=3.4]) c1="+ISO_VALUE+" c2=1983 c3=3.4", actual);
    }

}
