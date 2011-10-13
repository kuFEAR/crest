/*
 * Copyright 2011 CodeGist.org
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

import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;

import static java.lang.String.format;
import static org.codegist.crest.param.common.IDatesTest.Tests.Dates;
import static org.codegist.crest.util.ToStrings.string;
import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeTrue;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public abstract class IDatesTest<T extends IDatesTest.IDates> extends BaseCRestTest<T> {

    public IDatesTest(CRestHolder crest, Class<T> service) {
        super(crest, service);
    }

    @Parameterized.Parameters
    public static Collection<CRestHolder[]> getData() {
        return crest(byRestServices());
    }

    public enum Tests {
        Dates
    }

    public EnumSet<Tests> ignores(){
        return EnumSet.noneOf(Tests.class);
    }

    public void assumeThatTestIsEnabled(Tests test){
        assumeTrue(!ignores().contains(test));
    }

    @Test
    public void testDates() throws ParseException {
        assumeThatTestIsEnabled(Dates);
        Date p1 = date("1969/07/20T02:56:00+0000", "yyyy/MM/dd'T'HH:mm:ssZ");
        Date p21 = date("1983/03/13T00:35:00+0100", "yyyy/MM/dd'T'HH:mm:ssZ");
        Date p22 = date("2010/10/31T13:56:21-0700", "yyyy/MM/dd'T'HH:mm:ssZ");

        String actual = toTest.date(p1, p21, p22);
        assertDates(toString(p1), toString(p21), toString(p22), actual);
    }

    public void assertDates(String p1, String p21, String p22, String actual) {
        String expected = format("date() p1=%s p2=%s", p1, string(p21, p22));
        assertEquals(expected, actual);
    }

    /**
     * @author laurent.gilles@codegist.org
     */
    public static interface IDates {

        String date(Date p1, Date... p2);

    }
}
