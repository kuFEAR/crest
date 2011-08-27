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

package org.codegist.crest.test.util;

import org.codegist.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.codegist.crest.test.util.Resources.getFile;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class Values {
    public static final String ISO_8859_1_STR = "iso-8859-1";
    public static final Charset ISO_8859_1= Charset.forName(ISO_8859_1_STR);
    public static final Charset UTF8 = Charset.forName("utf-8");
    public static final String FORMAT = "dd/MM/yyyy HH:mm:ss";
    public static final String DATE_STR = "13/03/1983 00:35:10";
    public static final String DATE_STR_DEFAULT_FORMAT;
    public static final Date DATE;
    public static final File FILE1 = getFile("org/codegist/crest/File1.txt");
    public static final String FILE1_CONTENT;
    public static final String ENDPOINT = "http://localhost";

    public static final String SOME_STRING = "hello £ world";
    public static final byte[] SOME_STRING_UTF8_BYTES = SOME_STRING.getBytes(UTF8);
    public static final File IMAGE1 = getFile("org/codegist/crest/image1.gif");

    public static final String URL_ENCODED_FORM = "p1=v%201&p1=v%202&p3=v3";

    static {
        Calendar CAL = GregorianCalendar.getInstance();
        CAL.set(1983, 2, 13, 0,35,10);
        CAL.set(Calendar.MILLISECOND, 0);
        DATE = CAL.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        DATE_STR_DEFAULT_FORMAT = sdf.format(DATE);

        try {
            FILE1_CONTENT = Files.toString(FILE1);
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }
}
