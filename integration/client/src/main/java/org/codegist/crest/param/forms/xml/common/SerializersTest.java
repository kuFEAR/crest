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

import org.codegist.crest.annotate.*;
import org.codegist.crest.entity.XmlEntityWriter;
import org.codegist.crest.util.model.BunchOfData;
import org.codegist.crest.util.model.Data;
import org.codegist.crest.param.common.ISerializersTest;
import org.codegist.crest.util.BunchOfDataSerializer;
import org.codegist.crest.util.DataSerializer;
import org.junit.runners.Parameterized;

import java.util.Collection;
import java.util.regex.Pattern;

/**
 * @author laurent.gilles@codegist.org
 */
public class SerializersTest extends ISerializersTest<SerializersTest.Serializers> {

    public SerializersTest(CRestHolder crest) {
        super(crest, Serializers.class);
    }

    @Parameterized.Parameters
    public static Collection<CRestHolder[]> getData() {
        return crest(byXmlSerializersAndRestServices());
    }

    @EndPoint("{crest.server.end-point}")
    @Path("params/form/xml")
    @POST
    @EntityWriter(XmlEntityWriter.class)
    public static interface Serializers extends ISerializersTest.ISerializers {

        String defaults(
                @FormParam("p1") Data p1,
                @FormParam("p2") Collection<BunchOfData<Data>> p2,
                @FormParam("p3") BunchOfData<Data>[] p3);


        String configured(
                @FormParam("p1") @Serializer(DataSerializer.class) Data p1,
                @FormParam("p2") @Serializer(BunchOfDataSerializer.class) Collection<BunchOfData<Data>> p2,
                @FormParam("p3") @Serializer(BunchOfDataSerializer.class) BunchOfData<Data>[] p3);

        String nulls(
                @FormParam("p1") @Serializer(DataSerializer.class) Data p1,
                @FormParam("p2") @Serializer(BunchOfDataSerializer.class) Collection<BunchOfData<Data>> p2,
                @FormParam("p3") @Serializer(BunchOfDataSerializer.class) BunchOfData<Data>[] p3);

    }


    @Override
    public void assertDefaultSerialize(Data bof, BunchOfData<Data> bof21, BunchOfData<Data> bof22, BunchOfData<Data> bof31, BunchOfData<Data> bof32, String actual) {
        StringBuilder expected = new StringBuilder();
        expected.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
        expected.append("<form-data>");
        expected.append("    <p1>".trim());
        expected.append("        <data>".trim());
        expected.append("            <val1>".trim()).append(bof.getVal1()).append("</val1>");
        expected.append("            <val2>".trim()).append(bof.getVal2()).append("</val2>");
        expected.append("        </data>".trim());
        expected.append("    </p1>".trim());
        expected.append("    <p2>".trim());
        expected.append("        <bunchOfData>".trim());
        expected.append("            <val1>".trim()).append(toString(bof21.getVal1())).append("</val1>");
        expected.append("            <val2>".trim()).append(toString(bof21.getVal2())).append("</val2>");
        expected.append("            <val3>".trim());
        expected.append("                <val1>".trim()).append(bof21.getVal3().getVal1()).append("</val1>");
        expected.append("                <val2>".trim()).append(bof21.getVal3().getVal2()).append("</val2>");
        expected.append("            </val3>".trim());
        expected.append("        </bunchOfData>".trim());
        expected.append("    </p2>".trim());
        expected.append("    <p2>".trim());
        expected.append("        <bunchOfData>".trim());
        expected.append("            <val1>".trim()).append(toString(bof22.getVal1())).append("</val1>");
        expected.append("            <val2>".trim()).append(toString(bof22.getVal2())).append("</val2>");
        expected.append("            <val3>".trim());
        expected.append("                <val1>".trim()).append(bof22.getVal3().getVal1()).append("</val1>");
        expected.append("                <val2>".trim()).append(bof22.getVal3().getVal2()).append("</val2>");
        expected.append("            </val3>".trim());
        expected.append("        </bunchOfData>".trim());
        expected.append("    </p2>".trim());
        expected.append("    <p3>".trim());
        expected.append("        <bunchOfData>".trim());
        expected.append("            <val1>".trim()).append(toString(bof31.getVal1())).append("</val1>");
        expected.append("            <val2>".trim()).append(toString(bof31.getVal2())).append("</val2>");
        expected.append("            <val3>".trim());
        expected.append("                <val1>".trim()).append(bof31.getVal3().getVal1()).append("</val1>");
        expected.append("                <val2>".trim()).append(bof31.getVal3().getVal2()).append("</val2>");
        expected.append("            </val3>".trim());
        expected.append("        </bunchOfData>".trim());
        expected.append("    </p3>".trim());
        expected.append("    <p3>".trim());
        expected.append("        <bunchOfData>".trim());
        expected.append("            <val1>".trim()).append(toString(bof32.getVal1())).append("</val1>");
        expected.append("            <val2>".trim()).append(toString(bof32.getVal2())).append("</val2>");
        expected.append("            <val3>".trim());
        expected.append("                <val1>".trim()).append(bof32.getVal3().getVal1()).append("</val1>");
        expected.append("                <val2>".trim()).append(bof32.getVal3().getVal2()).append("</val2>");
        expected.append("            </val3>".trim());
        expected.append("        </bunchOfData>".trim());
        expected.append("    </p3>".trim());
        expected.append("</form-data>");
        // todo Do something about it
        actual = actual.replaceAll(Pattern.quote("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"data\""), "");
        actual = actual.replaceAll("class=\"org\\.codegist\\.crest\\.util\\.model\\.\\w+.\\w+\"", "");
        assertXmlEquals(expected.toString(), actual);
    }

    @Override
    public void assertConfiguredSerialize(Data bof, BunchOfData<Data> bof21, BunchOfData<Data> bof22, BunchOfData<Data> bof31, BunchOfData<Data> bof32, String actual) {
        assertDefaultSerialize(bof, bof21, bof22, bof31, bof32, actual);
    }

    @Override
    public void assertSerializeNulls(String expectedSerializedBof, String expectedSerializedBof2, String expectedSerializedBof3, String actual) {
        assertXmlEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><form-data/>", actual);
    }
}
