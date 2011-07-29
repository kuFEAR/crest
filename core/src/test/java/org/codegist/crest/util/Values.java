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

package org.codegist.crest.util;

import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class Values {
    public static final Charset UTF8 = Charset.forName("utf-8");
    public static final String FORMAT = "dd/MM/yyyy HH:mm:ss";
    public static final String DATE_STR = "13/03/1983 00:35:10";
    public static final Date DATE;

    static {
        Calendar CAL = GregorianCalendar.getInstance(Locale.ITALY);
        CAL.set(1983, 2, 13, 0,35,10);
        DATE = CAL.getTime();
    }
}
