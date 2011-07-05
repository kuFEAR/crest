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

import org.codegist.crest.XmlEntityWriter;
import org.codegist.crest.annotate.*;
import org.codegist.crest.param.common.IDefaultValuesTest;
import org.junit.runners.Parameterized;

import java.util.Collection;

/**
 * @author laurent.gilles@codegist.org
 */
public class DefaultValuesTest extends IDefaultValuesTest<DefaultValuesTest.DefaultValues> {

    public DefaultValuesTest(CRestHolder crest) {
        super(crest, DefaultValues.class);
    }

    @Parameterized.Parameters
    public static Collection<CRestHolder[]> getData() {
        return crest(byXmlSerializersAndRestServices());
    }

    @EndPoint("{crest.server.end-point}")
    @Path("params/form/xml")
    @POST
    @EntityWriter(XmlEntityWriter.class)
    public static interface DefaultValues extends IDefaultValuesTest.IDefaultValues {

        String value(
                @FormParam(value = "p1", defaultValue = "default-p1") String p1,
                @FormParam(value = "p2", defaultValue = "123") Integer p2);

        @FormParam(value = "p2", defaultValue = "p2-val")
        @FormParams({
                @FormParam(value = "p1", defaultValue = "p1-val"),
                @FormParam(value = "p3", defaultValue = "p3-val")
        })
        String param(@FormParam("p1") String p1);

    }


    @Override
    public void assertDefaultValue(String defaultP1, int defaultP2, String actual) {
        StringBuilder expected = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
        expected.append("<form-data>");
        expected.append("<p1>").append(defaultP1).append("</p1>");
        expected.append("<p2>").append(defaultP2).append("</p2>");
        expected.append("</form-data>");
        assertXmlEquals(expected.toString(), actual);
    }

    @Override
    public void assertParamsValue(String p11, String p12, String p2, String p3, String actual) {
        StringBuilder expected = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
        expected.append("<form-data>");
        expected.append("<p2>").append(p2).append("</p2>");
        expected.append("<p1>").append(p11).append("</p1>");
        expected.append("<p1>").append(p12).append("</p1>");
        expected.append("<p3>").append(p3).append("</p3>");
        expected.append("</form-data>");
        assertXmlEquals(expected.toString(), actual);
    }

}
