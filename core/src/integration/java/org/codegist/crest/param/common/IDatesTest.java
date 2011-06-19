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
import org.codegist.crest.CRest;
import org.junit.Test;
import org.junit.runners.Parameterized;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import static java.lang.String.format;
import static org.codegist.crest.utils.ToStrings.string;
import static org.junit.Assert.assertEquals;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public abstract class IDatesTest<T extends IDatesTest.IDates> extends BaseCRestTest<T> {

    public IDatesTest(CRest crest, Class<T> service) {
        super(crest, service);
    }

    @Parameterized.Parameters
    public static Collection<CRest[]> getData() {
        return crest(byRestServices());
    }

    @Test
    public void testDates() throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy/MM/dd'T'HH:mm:ssZ");
        DateFormat expected = new SimpleDateFormat(DATE_FORMAT);
        Date p1 = format.parse("1969/07/20T02:56:00+0000");
        Date p21 = format.parse("1983/03/13T00:35:00+0100");
        Date p22 = format.parse("2010/10/31T13:56:21-0700");

        String actual = toTest.date(p1, p21, p22);
        assertDates(expected.format(p1), expected.format(p21), expected.format(p22), actual);
    }
    
    public void assertDates(String p1, String p21, String p22, String actual){
        String expected = format("date() p1=%s p2=%s", p1, string(p21,p22));
        assertEquals(expected, actual);
    }

    /**
     * @author laurent.gilles@codegist.org
     */
    public static interface IDates {

        String date(Date p1, Date... p2);

    }
}
