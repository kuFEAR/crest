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

package org.codegist.crest.param.multiparts;

import org.codegist.crest.BaseCRestTest;
import org.codegist.crest.CRest;
import org.codegist.crest.annotate.EndPoint;
import org.codegist.crest.annotate.MultiPartParam;
import org.codegist.crest.annotate.POST;
import org.codegist.crest.annotate.Path;
import org.codegist.crest.param.common.IEncodingsTest;
import org.junit.Test;
import org.junit.runners.Parameterized;

import java.io.*;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

/**
 * @author laurent.gilles@codegist.org
 */
public class MiscsTest extends BaseCRestTest<MiscsTest.Miscs> {

    public MiscsTest(CRest crest) {
        super(crest, Miscs.class);
    }

    @Parameterized.Parameters
    public static Collection<CRest[]> getData() {
        return crest(byRestServices());
    }

    @EndPoint(ADDRESS)
    @Path("params/multipart/misc")
    @POST
    public static interface Miscs extends IEncodingsTest.IEncodings {

        String misc(
                    @MultiPartParam("p1") String q1,
                    @MultiPartParam(value="p2", contentType = "text/html", fileName = "my-file") int q2,
                    @MultiPartParam("p3") float[] q3,
                    @MultiPartParam("p4") InputStream in,
                    @MultiPartParam("p5") File file);

    }


    @Test
    public void testMisc() throws IOException {
        InputStream is = new ByteArrayInputStream("hello".getBytes("UTF-8"));
        File file = new File("textFile.txt");
        file.deleteOnExit();

        FileWriter fw = new FileWriter(file);
        fw.write("that's my file");
        fw.close();

        String actual = toTest.misc("val-p1", 1983, new float[]{1.2f,2.3f,3.4f}, is, file);
        assertEquals(
                "misc" +
                "\n1(name=p1, content-type=text/plain;charset=UTF-8, value=val-p1, filename=null)" +
                "\n2(name=p2, content-type=text/html;charset=UTF-8, value=1983, filename=my-file)" +
                "\n3(name=p3, content-type=text/plain;charset=UTF-8, value=1.2, filename=null)" +
                "\n4(name=p3, content-type=text/plain;charset=UTF-8, value=2.3, filename=null)" +
                "\n5(name=p3, content-type=text/plain;charset=UTF-8, value=3.4, filename=null)" +
                "\n6(name=p4, content-type=application/octet-stream, value=hello, filename=null)" +
                "\n7(name=p5, content-type=application/octet-stream, value=that's my file, filename=textFile.txt)"
                , actual);
    }
}

