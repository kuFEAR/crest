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

import org.codegist.crest.CRest;
import org.codegist.crest.JsonEntityWriter;
import org.codegist.crest.annotate.*;
import org.codegist.crest.model.BunchOfData;
import org.codegist.crest.model.Data;
import org.codegist.crest.param.common.ISerializersTest;
import org.codegist.crest.serializer.BunchOfDataSerializer;
import org.codegist.crest.serializer.DataSerializer;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static org.junit.Assert.assertEquals;

/**
 * @author laurent.gilles@codegist.org
 */
public class SerializersTest extends ISerializersTest<SerializersTest.Serializers> {

    public SerializersTest(CRest crest) {
        super(crest, Serializers.class);
    }

    @Parameterized.Parameters
    public static Collection<CRest[]> getData() {
        return crest(byJsonSerializersAndRestServices());
    }

    @EndPoint(ADDRESS)
    @Path("params/form/json")
    @POST
    @EntityWriter(JsonEntityWriter.class)
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
        expected.append("{");
        expected.append("\"p1\":{");
        expected.append("\"val1\":").append(bof.getVal1()).append(",");
        expected.append("\"val2\":\"").append(bof.getVal2()).append("\"");
        expected.append("},");
        expected.append("\"p2\":[");
        expected.append("{");
        expected.append("\"val1\":").append(bof21.getVal1().getTime()).append(",");
        expected.append("\"val2\":").append(bof21.getVal2()).append(",");
        expected.append("\"val3\":{");
        expected.append("\"val1\":").append(bof21.getVal3().getVal1()).append(",");
        expected.append("\"val2\":\"").append(bof21.getVal3().getVal2()).append("\"");
        expected.append("}");
        expected.append("},");
        expected.append("{");
        expected.append("\"val1\":").append(bof22.getVal1().getTime()).append(",");
        expected.append("\"val2\":").append(bof22.getVal2()).append(",");
        expected.append("\"val3\":{");
        expected.append("\"val1\":").append(bof22.getVal3().getVal1()).append(",");
        expected.append("\"val2\":\"").append(bof22.getVal3().getVal2()).append("\"");
        expected.append("}");
        expected.append("}");
        expected.append("],");
        expected.append("\"p3\":[");
        expected.append("{");
        expected.append("\"val1\":").append(bof31.getVal1().getTime()).append(",");
        expected.append("\"val2\":").append(bof31.getVal2()).append(",");
        expected.append("\"val3\":{");
        expected.append("\"val1\":").append(bof31.getVal3().getVal1()).append(",");
        expected.append("\"val2\":\"").append(bof31.getVal3().getVal2()).append("\"");
        expected.append("}");
        expected.append("},");
        expected.append("{");
        expected.append("\"val1\":").append(bof32.getVal1().getTime()).append(",");
        expected.append("\"val2\":").append(bof32.getVal2()).append(",");
        expected.append("\"val3\":{");
        expected.append("\"val1\":").append(bof32.getVal3().getVal1()).append(",");
        expected.append("\"val2\":\"").append(bof32.getVal3().getVal2()).append("\"");
        expected.append("}");
        expected.append("}");
        expected.append("]");
        expected.append("}");
        assertEquals(expected.toString(), actual);
    }

    @Override
    public void assertConfiguredSerialize(Data bof, BunchOfData<Data> bof21, BunchOfData<Data> bof22, BunchOfData<Data> bof31, BunchOfData<Data> bof32, String actual) {
        assertDefaultSerialize(bof,bof21,bof22,bof31,bof32,actual);
    }

    @Override
    public void assertSerializeNulls(String expectedSerializedBof, String expectedSerializedBof2, String expectedSerializedBof3, String actual) {
        assertEquals("{}", actual);
    }
}
