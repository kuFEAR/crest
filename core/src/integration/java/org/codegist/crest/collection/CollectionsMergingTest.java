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
import org.junit.Test;
import org.junit.runners.Parameterized;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class CollectionsMergingTest extends BaseCRestTest<Collections> {

    private static final String MATRIX_SEP = "(m)";
    private static final String PATH_SEP = "(p)";
    private static final String QUERY_SEP= "(q)";
    private static final String FORM_SEP = "(f)";
    private static final String COOKIE_SEP = "(c)";
    private static final String HEADER_SEP = "(h)";
    
    public CollectionsMergingTest(CRest crest) {
        super(crest, Collections.class);
    }

    @Parameterized.Parameters
    public static Collection<CRest[]> getData() {
        return crest(byCollectionMerging("merging",
                MATRIX_SEP,
                PATH_SEP,
                QUERY_SEP,
                FORM_SEP,
                COOKIE_SEP,
                HEADER_SEP));
    }

    private static final String[] Q1 = {"q1-val1","q1-val2"};
    private static final boolean[] Q2 =  {false,true};
    private static final List<Integer> Q3 = asList(23,-45, 23);
    private static final Set<Long> Q4 = new HashSet<Long>(asList(23l,-45l, 23l));

    @Test
    public void testQuery(){
        String actual = toTest.query(Q1, Q2, Q3, Q4);
        assertEquals("query q1=q1-val1"+QUERY_SEP+"q1-val2 q2=false"+QUERY_SEP+"true q3=23"+QUERY_SEP+"-45"+QUERY_SEP+"23 q4=23"+QUERY_SEP+"-45", actual);
    }

    @Test
    public void testMatrix(){
        String actual = toTest.matrix(Q1, Q2, Q3, Q4);
        assertEquals("matrix m1=q1-val1"+MATRIX_SEP+"q1-val2 m2=false"+MATRIX_SEP+"true m3=23"+MATRIX_SEP+"-45"+MATRIX_SEP+"23 m4=23"+MATRIX_SEP+"-45", actual);
    }

    @Test
    public void testForm(){
        String actual = toTest.form(Q1, Q2, Q3, Q4);
        assertEquals("form f1=q1-val1"+FORM_SEP+"q1-val2 f2=false"+FORM_SEP+"true f3=23"+FORM_SEP+"-45"+FORM_SEP+"23 f4=23"+FORM_SEP+"-45", actual);
    }

    @Test
    public void testPath(){
        String actual = toTest.path(Q1, Q2, Q3, Q4);
        assertEquals("path p1=q1-val1"+PATH_SEP+"q1-val2 p2=false"+PATH_SEP+"true p3=23"+PATH_SEP+"-45"+PATH_SEP+"23 p4=23"+PATH_SEP+"-45", actual);
    }

    @Test
    public void testHeader(){
        String actual = toTest.header(Q1, Q2, Q3, Q4);
        assertEquals("header h1=q1-val1"+HEADER_SEP+"q1-val2 h2=false"+HEADER_SEP+"true h3=23"+HEADER_SEP+"-45"+HEADER_SEP+"23 h4=23"+HEADER_SEP+"-45", actual);
    }

    @Test
    public void testCookie(){
        String actual = toTest.cookie(Q1, Q2, Q3, Q4);
        assertEquals("cookie(header:[c1=q1-val1"+COOKIE_SEP+"q1-val2, c2=false"+COOKIE_SEP+"true, c3=23"+COOKIE_SEP+"-45"+COOKIE_SEP+"23, c4=23"+COOKIE_SEP+"-45]) c1=q1-val1"+COOKIE_SEP+"q1-val2 c2=false"+COOKIE_SEP+"true c3=23"+COOKIE_SEP+"-45"+COOKIE_SEP+"23 c4=23"+COOKIE_SEP+"-45", actual);
    }

}
