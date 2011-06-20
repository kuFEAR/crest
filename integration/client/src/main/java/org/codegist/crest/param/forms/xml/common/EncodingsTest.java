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

package org.codegist.crest.param.forms.xml.common;

import org.codegist.crest.CRest;
import org.codegist.crest.XmlEntityWriter;
import org.codegist.crest.annotate.*;
import org.codegist.crest.param.common.IEncodingsTest;
import org.junit.runners.Parameterized;

import java.io.UnsupportedEncodingException;
import java.util.Collection;

/**
 * @author laurent.gilles@codegist.org
 */
public class EncodingsTest extends IEncodingsTest<EncodingsTest.Encodings> {

    public EncodingsTest(CRest crest) {
        super(crest, Encodings.class);
    }

    @Parameterized.Parameters
    public static Collection<CRest[]> getData() {
        return crest(byXmlSerializersAndRestServices());
    }

    @EndPoint("{crest.server.end-point}")
    @Path("params/form/xml")
    @POST
    @EntityWriter(XmlEntityWriter.class)
    public static interface Encodings extends IEncodingsTest.IEncodings {

        String defaults(
                @FormParam("p1") String p1,
                @FormParam("p2") Collection<String> p2);

        @Encoded
        String encoded(
                @FormParam("p1") String p1,
                @FormParam("p2") Collection<String> p2);

    }


    @Override
    public void assertDefault(String p1, String p21, String p22, String actual) {
        StringBuilder expected = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
        expected.append("<form-data>");
        expected.append("<p1>").append(xml(p1)).append("</p1>");
        expected.append("<p2>").append(xml(p21)).append("</p2>");
        expected.append("<p2>").append(xml(p22)).append("</p2>");
        expected.append("</form-data>");
        assertXMLEqual(expected.toString(), actual);
    }

    // todo PreEncoded for xml should consider it as XML preencoded!

    @Override
    public void assertEncoded(String p1, String p21, String p22, String actual) throws UnsupportedEncodingException {
        StringBuilder expected = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
        expected.append("<form-data>");
        expected.append("<p1>").append(url(p1)).append("</p1>");
        expected.append("<p2>").append(url(p21)).append("</p2>");
        expected.append("<p2>").append(url(p22)).append("</p2>");
        expected.append("</form-data>");
        assertXMLEqual(expected.toString(), actual);
    }
}
