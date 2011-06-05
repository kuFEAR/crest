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

package org.codegist.crest.resources;

import org.codegist.crest.CRest;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.Parameterized;
import org.xml.sax.SAXException;

import java.io.*;
import java.util.Collection;

import static org.custommonkey.xmlunit.XMLAssert.assertXMLEqual;
import static org.junit.Assert.assertEquals;

//@Ignore
public class EntityWritersTest extends BaseCRestTest {

    private final EntityWriters toTest = crest.build(EntityWriters.class);

    public EntityWritersTest(CRest crest) {
        super(crest);
    }
    
    @Parameterized.Parameters
    public static Collection<CRest[]> getData() {
        return data(bySerializers());
    }

    @Test
    public void testPostFormAsXml() throws IOException, SAXException {
        String actual = toTest.postFormAsXml(UTF8_VALUE, 1983, new float[]{1.2f,2.3f,3.4f});
        assertXMLEqual(
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<form-data><f1>123@#?&amp;£{}abc</f1><f2>1983</f2><f3>1.2</f3><f3>2.3</f3><f3>3.4</f3></form-data>",
                actual);

    }

    @Test
    public void testPostFormAsJson(){
        String actual = toTest.postFormAsJson(UTF8_VALUE, 1983, new float[]{1.2f,2.3f,3.4f});
        assertEquals("{\"f1\":\""+UTF8_VALUE+"\",\"f2\":1983,\"f3\":[1.2,2.3,3.4]}", actual);
    }

    @Test
    public void testMultipart() throws IOException {

        InputStream is = new ByteArrayInputStream("hello".getBytes("UTF-8"));
        File file = new File("textFile.txt");
        file.deleteOnExit();

        FileWriter fw = new FileWriter(file);
        fw.write("that's my file");
        fw.close();

        String actual = toTest.multipart(UTF8_VALUE, 1983, new float[]{1.2f,2.3f,3.4f}, is, file);
        assertEquals(
                "multipart" +
                "\n1(name=p1, content-type=text/plain;charset=UTF-8, value=123@#?&Â£{}abc, filename=null)" +  // TODO encoding fails, is that CXF fault ?
                "\n2(name=p2, content-type=text/html;charset=UTF-8, value=1983, filename=my-file)" +
                "\n3(name=p3, content-type=text/plain;charset=UTF-8, value=1.2, filename=null)" +
                "\n4(name=p3, content-type=text/plain;charset=UTF-8, value=2.3, filename=null)" +
                "\n5(name=p3, content-type=text/plain;charset=UTF-8, value=3.4, filename=null)" +
                "\n6(name=p4, content-type=application/octet-stream, value=hello, filename=null)" +
                "\n7(name=p5, content-type=application/octet-stream, value=that's my file, filename=textFile.txt)"
                , actual);
    }
}
