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

package org.codegist.crest.collection;

import org.codegist.crest.BaseCRestTest;
import org.codegist.crest.CRest;
import org.codegist.crest.annotate.*;
import org.junit.Test;
import org.junit.runners.Parameterized;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static java.util.Arrays.*;
import static org.junit.Assert.assertEquals;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class CollectionsDefaultTest extends BaseCRestTest<Collections> {

    public CollectionsDefaultTest(CRest crest) {
        super(crest, Collections.class);
    }

    @Parameterized.Parameters
    public static Collection<CRest[]> getData() {
        return crest(byCollectionDefault("default"));
    }

    private static final String[] Q1 = {"q1-val1","q1-val2"};
    private static final boolean[] Q2 =  {false,true};
    private static final List<Integer> Q3 = asList(23,-45, 23);
    private static final Set<Long> Q4 = new HashSet<Long>(asList(23l,-45l, 23l));

    @Test
    public void testQuery(){
        String actual = toTest.query(Q1, Q2, Q3, Q4);
        assertEquals("query q1=[q1-val1, q1-val2] q2=[false, true] q3=[23, -45, 23] q4=[23, -45]", actual);
    }

    @Test
    public void testMatrix(){
        String actual = toTest.matrix(Q1, Q2, Q3, Q4);
        assertEquals("matrix m1=[q1-val1, q1-val2] m2=[false, true] m3=[23, -45, 23] m4=[23, -45]", actual);
    }

    @Test
    public void testForm(){
        String actual = toTest.form(Q1, Q2, Q3, Q4);
        assertEquals("form f1=[q1-val1, q1-val2] f2=[false, true] f3=[23, -45, 23] f4=[23, -45]", actual);
    }

    @Test(expected = IllegalArgumentException.class) // TODO Provide default collection merging for path ??
    public void testPath(){
        toTest.path(Q1, Q2, Q3, Q4);
    }

    @Test
    public void testHeader(){
        String actual = toTest.header(Q1, Q2, Q3, Q4);
        assertEquals("header h1=[q1-val1, q1-val2] h2=[false, true] h3=[23, -45, 23] h4=[23, -45]", actual);
    }

    @Test
    public void testCookie(){
        String actual = toTest.cookie(Q1, Q2, Q3, Q4);
        assertEquals("cookie(header:[c1=q1-val1, c1=q1-val2, c2=false, c2=true, c3=23, c3=-45, c3=23, c4=23, c4=-45]) c1=q1-val2 c2=true c3=23 c4=-45", actual);
    }

}
