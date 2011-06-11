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
import org.custommonkey.xmlunit.XMLAssert;
import org.junit.runners.Parameterized;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collection;

import static org.codegist.common.net.Urls.encode;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class FormXmlEntitiesTest extends CommonParamsTest<FormXmlEntities> {

    public FormXmlEntitiesTest(CRest crest) {
        super(crest, FormXmlEntities.class);
    }

    @Parameterized.Parameters
    public static Collection<CRest[]> getData() {
        return crest(byXmlSerializers());
    }


    @Override
    public void assertSend(String p1, int p2, String actual) {
        StringBuilder expected = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
        expected.append("<form-data>");
        expected.append("<p1>").append(xml(p1)).append("</p1>");
        expected.append("<p2>").append(p2).append("</p2>");
        expected.append("</form-data>");
        assertXMLEqual(expected.toString(), actual);
    }

    @Override
    public void assertNulls(String actual) {
        assertXMLEqual("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><form-data/>", actual);
    }

    @Override
    public void assertNullsMerging(String actual) {
        assertXMLEqual("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><form-data/>", actual);
    }

    @Override
    public void assertDefaultValue(String defaultP1, int defaultP2, String actual) {
        StringBuilder expected = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
        expected.append("<form-data>");
        expected.append("<p1>").append(defaultP1).append("</p1>");
        expected.append("<p2>").append(defaultP2).append("</p2>");
        expected.append("</form-data>");
        assertXMLEqual(expected.toString(), actual);
    }

    @Override
    public void assertParamsValue(String p11, String p12, String p2, String p3, String actual) {
        StringBuilder expected = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
        expected.append("<form-data>");
        expected.append("<p2>").append(p2).append("</p2>");
        expected.append("<p3>").append(p3).append("</p3>");
        expected.append("<p1>").append(p11).append("</p1>");
        expected.append("<p1>").append(p12).append("</p1>");
        expected.append("</form-data>");
        assertXMLEqual(expected.toString(), actual);
    }


    @Override
    public void assertDefaultLists(String p11, String p12, boolean p21, boolean p22, Integer p31, Integer p32, Long p41, Long p42, String actual) {
        StringBuilder expected = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
        expected.append("<form-data>");
        expected.append("<p1>").append(p11).append("</p1>");
        expected.append("<p1>").append(xml(p12)).append("</p1>");
        expected.append("<p2>").append(p21).append("</p2>");
        expected.append("<p2>").append(p22).append("</p2>");
        expected.append("<p3>").append(p31).append("</p3>");
        expected.append("<p3>").append(p32).append("</p3>");
        expected.append("<p4>").append(p41).append("</p4>");
        expected.append("<p4>").append(p42).append("</p4>");
        expected.append("</form-data>");
        assertXMLEqual(expected.toString(), actual); 
    }

    @Override
    public void assertMergingLists(String p11, String p1Sep, String p12, boolean p21, String p2Sep, boolean p22, Integer p31, String p3Sep, Integer p32, Long p41, String p4Sep, Long p42, String actual) {
        StringBuilder expected = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
        expected.append("<form-data>");
        expected.append("<p1>").append(p11).append(p1Sep).append(xml(p12)).append("</p1>");
        expected.append("<p2>").append(p21).append(p2Sep).append(p22).append("</p2>");
        expected.append("<p3>").append(p31).append(p3Sep).append(p32).append("</p3>");
        expected.append("<p4>").append(p41).append(p4Sep).append(p42).append("</p4>");
        expected.append("</form-data>");
        assertXMLEqual(expected.toString(), actual);
    }

    @Override
    public void assertEncodings(String p1, String p21, String p22, String actual) {
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
    public void assertPreEncoded(String p1, String p21, String p22, String actual) throws UnsupportedEncodingException {
        StringBuilder expected = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
        expected.append("<form-data>");
        expected.append("<p1>").append(url(p1)).append("</p1>");
        expected.append("<p2>").append(url(p21)).append("</p2>");
        expected.append("<p2>").append(url(p22)).append("</p2>");
        expected.append("</form-data>");
        assertXMLEqual(expected.toString(), actual);
    }

    private static String xml(String s){
        return s.replace("&", "&amp;");
    }
    private static String url(String s) throws UnsupportedEncodingException {
        return encode(s, "utf-8");
    }
    private static void assertXMLEqual(String control, String actual){
        try {
            XMLAssert.assertXMLEqual(control, actual);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
