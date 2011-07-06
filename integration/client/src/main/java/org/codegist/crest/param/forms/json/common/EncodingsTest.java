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

package org.codegist.crest.param.forms.json.common;

import org.codegist.crest.annotate.*;
import org.codegist.crest.entity.JsonEntityWriter;
import org.codegist.crest.param.common.IEncodingsTest;
import org.junit.runners.Parameterized;

import java.io.UnsupportedEncodingException;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

/**
 * @author laurent.gilles@codegist.org
 */
public class EncodingsTest extends IEncodingsTest<EncodingsTest.Encodings> {

    public EncodingsTest(CRestHolder crest) {
        super(crest, Encodings.class);
    }

    @Parameterized.Parameters
    public static Collection<CRestHolder[]> getData() {
        return crest(byJsonSerializersAndRestServices());
    }

    @EndPoint("{crest.server.end-point}")
    @Path("params/form/json")
    @POST
    @EntityWriter(JsonEntityWriter.class)
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
        StringBuilder expected = new StringBuilder();
        expected.append("{");
        expected.append("\"p1\":").append(json(p1)).append(",");
        expected.append("\"p2\":[").append(json(p21)).append(",").append(json(p22)).append("]");
        expected.append("}");
        assertEquals(expected.toString(), actual);
    }

    // todo PreEncoded for xml should consider it as JSON preencoded!

    @Override
    public void assertEncoded(String p1, String p21, String p22, String actual) throws UnsupportedEncodingException {
        StringBuilder expected = new StringBuilder();
        expected.append("{");
        expected.append("\"p1\":\"").append(url(p1)).append("\",");
        expected.append("\"p2\":[\"").append(url(p21)).append("\",\"").append(url(p22)).append("\"]");
        expected.append("}");
        assertEquals(expected.toString(), actual);
    }
}
