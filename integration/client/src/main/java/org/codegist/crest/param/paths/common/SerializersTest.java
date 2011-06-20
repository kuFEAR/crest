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

package org.codegist.crest.param.paths.common;

import org.codegist.crest.CRest;
import org.codegist.crest.annotate.*;
import org.codegist.crest.model.BunchOfData;
import org.codegist.crest.model.Data;
import org.codegist.crest.param.common.ISerializersTest;
import org.codegist.crest.serializer.BunchOfDataSerializer;
import org.codegist.crest.serializer.DataSerializer;
import org.junit.Test;

import java.util.Collection;

import static java.lang.String.format;
import static org.junit.Assert.assertEquals;

/**
 * @author laurent.gilles@codegist.org
 */
public class SerializersTest extends ISerializersTest<SerializersTest.Serializers> {

    public SerializersTest(CRest crest) {
        super(crest, Serializers.class);
    }

    @EndPoint("{crest.server.end-point}")
    @Path("params/path/serializer")
    @GET
    public static interface Serializers extends ISerializersTest.ISerializers {

        @Path("default/{p1}/{p2}/{p3}")
        String defaults(
                @PathParam("p1") Data p1,
                @PathParam("p2") @ListSeparator("(p2)") Collection<BunchOfData<Data>> p2,
                @PathParam("p3") @ListSeparator("(p3)") BunchOfData<Data>[] p3);


        @Path("configured/{p1}/{p2}/{p3}")
        String configured(
                @PathParam("p1") @Serializer(DataSerializer.class) Data p1,
                @PathParam("p2") @ListSeparator("(p2)") @Serializer(BunchOfDataSerializer.class) Collection<BunchOfData<Data>> p2,
                @PathParam("p3") @ListSeparator("(p3)") @Serializer(BunchOfDataSerializer.class) BunchOfData<Data>[] p3);

    }

    @Override
    public void assertDefaultSerialize(String expectSerializedBof, String expectSerializedBof21, String expectSerializedBof22, String expectSerializedBof31, String expectSerializedBof32, String actual) {
        assertEquals(format("default() p1=%s p2=%s(p2)%s p3=%s(p3)%s",
                expectSerializedBof,
                expectSerializedBof21,
                expectSerializedBof22,
                expectSerializedBof31,
                expectSerializedBof32), actual);
    }

    @Override
    public void assertConfiguredSerialize(String expectSerializedBof, String expectSerializedBof21, String expectSerializedBof22, String expectSerializedBof31, String expectSerializedBof32, String actual) {
        assertEquals(format("configured() p1=%s p2=%s(p2)%s p3=%s(p3)%s",
                expectSerializedBof,
                expectSerializedBof21,
                expectSerializedBof22,
                expectSerializedBof31,
                expectSerializedBof32), actual);
    }


    @Override
    @Test
    public void testSerializeNulls() {
        // N/A - Path params do not support nulls
    }
}
