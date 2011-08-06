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

import org.codegist.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import static org.codegist.crest.util.Resources.getFile;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class Values {
    public static final Charset UTF8 = Charset.forName("utf-8");
    public static final String FORMAT = "dd/MM/yyyy HH:mm:ss";
    public static final String DATE_STR = "13/03/1983 00:35:10";
    public static final String DATE_STR_DEFAULT_FORMAT = "1983-03-13T00:35:10+0000";
    public static final Date DATE;
    public static final File FILE1 = getFile("org/codegist/crest/File1.txt");
    public static final String FILE1_CONTENT;

    public static final String SOME_STRING = "hello £ world";
    public static final byte[] SOME_STRING_UTF8_BYTES = SOME_STRING.getBytes(UTF8);
    public static final File IMAGE1 = getFile("org/codegist/crest/image1.gif");
    

    static {
        Calendar CAL = GregorianCalendar.getInstance(Locale.ITALY);
        CAL.set(1983, 2, 13, 0,35,10);
        CAL.set(Calendar.MILLISECOND, 0);
        DATE = CAL.getTime();
        try {
            FILE1_CONTENT = Files.toString(FILE1);
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }
}
