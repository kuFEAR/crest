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

package org.codegist.crest.param;

import org.codegist.crest.CRest;
import org.codegist.crest.param.common.CommonParamsTest;
import org.junit.Test;
import org.junit.runners.Parameterized;

import java.io.*;
import java.util.Collection;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.codegist.common.net.Urls.encode;
import static org.junit.Assert.assertEquals;

public class MultiPartsTest extends CommonParamsTest<MultiParts> {

    public MultiPartsTest(CRest crest) {
        super(crest, MultiParts.class);
    }

    @Parameterized.Parameters
    public static Collection<CRest[]> getData() {
        return crest(byRestServices());
    }



    @Test
    public void testMisc() throws IOException {
        InputStream is = new ByteArrayInputStream("hello".getBytes("UTF-8"));
        File file = new File("textFile.txt");
        file.deleteOnExit();

        FileWriter fw = new FileWriter(file);
        fw.write("that's my file");
        fw.close();

        String actual = toTest.misc(UTF8_VALUE, 1983, new float[]{1.2f,2.3f,3.4f}, is, file);
        assertEquals(
                "misc" +
                "\n1(name=p1, content-type=text/plain;charset=UTF-8, value=123@#?&£{}abc, filename=null)" +
                "\n2(name=p2, content-type=text/html;charset=UTF-8, value=1983, filename=my-file)" +
                "\n3(name=p3, content-type=text/plain;charset=UTF-8, value=1.2, filename=null)" +
                "\n4(name=p3, content-type=text/plain;charset=UTF-8, value=2.3, filename=null)" +
                "\n5(name=p3, content-type=text/plain;charset=UTF-8, value=3.4, filename=null)" +
                "\n6(name=p4, content-type=application/octet-stream, value=hello, filename=null)" +
                "\n7(name=p5, content-type=application/octet-stream, value=that's my file, filename=textFile.txt)"
                , actual);
    }

    // Multiparts writes data as is comes without further encoding
    public void assertPreEncoded(String p1, String p21, String p22, String actual) throws UnsupportedEncodingException {
        String expected = format("preEncoded() p1=%s p2=%s", encode(p1, "utf-8"), asList(encode(p21, "utf-8"), encode(p22, "utf-8")));
        assertEquals(expected, actual);
    }
}
