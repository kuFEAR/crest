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

package org.codegist.crest.entity;

import org.codegist.crest.BaseCRestTest;
import org.codegist.crest.CRest;
import org.junit.Test;
import org.junit.runners.Parameterized;
import org.xml.sax.SAXException;

import java.io.*;
import java.util.Collection;

import static org.custommonkey.xmlunit.XMLAssert.assertXMLEqual;
import static org.junit.Assert.assertEquals;

//@Ignore
public class EntityWritersTest extends BaseCRestTest<EntityWriters> {

    public EntityWritersTest(CRest crest) {
        super(crest, EntityWriters.class);
    }
    
    @Parameterized.Parameters
    public static Collection<CRest[]> getData() {
        return crest(bySerializers());
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
}
