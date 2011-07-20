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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.Collection;
import java.util.EnumSet;

import static java.lang.String.format;
import static org.codegist.crest.util.ToStrings.string;
import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeTrue;

/**
 * @author laurent.gilles@codegist.org
 */
public class ISpecialParamsTests<T extends ISpecialParamsTests.ISpecialParams> extends BaseCRestTest<T> {

    public ISpecialParamsTests(CRestHolder crest, Class<T> service) {
        super(crest, service);
    }

    @Parameterized.Parameters
    public static Collection<CRestHolder[]> getData() {
        return crest(byRestServices());
    }

    public enum Tests {
        InputStream,Reader
    }

    public EnumSet<Tests> ignores(){
        return EnumSet.noneOf(Tests.class);
    }

    public void assumeThatTestIsEnabled(Tests test){
        assumeTrue(!ignores().contains(test));
    }

    @Test
    public void testInputStream(){
        assumeThatTestIsEnabled(Tests.InputStream);
        String p1 = "p1-value";
        String p21 = "p21-value";
        String p22 = "p22-value";

        InputStream ip1 = new ByteArrayInputStream(p1.getBytes());
        InputStream ip21 = new ByteArrayInputStream(p21.getBytes());
        InputStream ip22 = new ByteArrayInputStream(p22.getBytes());

        String actual = toTest.inputStream(ip1, new InputStream[]{ip21,ip22});
        assertInputStream(p1, p21, p22, actual);
    }

    public void assertInputStream(String p1,String p21, String p22, String actual){
        assertEquals(format("inputStream() p1=%s p2=%s", p1, string(p21, p22)), actual);
    }


    @Test
    public void testReader(){
        assumeThatTestIsEnabled(Tests.Reader);
        String p1 = "p1-value";
        String p21 = "p21-value";
        String p22 = "p22-value";

        Reader ip1 = new StringReader(p1);
        Reader ip21 = new StringReader(p21);
        Reader ip22 = new StringReader(p22);

        String actual = toTest.reader(ip1, new Reader[]{ip21,ip22});
        assertReader(p1, p21, p22, actual);
    }

    public void assertReader(String p1,String p21, String p22, String actual){
        assertEquals(format("reader() p1=%s p2=%s", p1, string(p21, p22)), actual);
    }



    /**
     * @author laurent.gilles@codegist.org
     */
    public static interface ISpecialParams {

        String inputStream(InputStream p1, InputStream[] p2);

        String reader(Reader p1, Reader[] p2);

    }
}
