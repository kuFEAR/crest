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

package org.codegist.crest.param.cookies.common;

import org.codegist.crest.annotate.*;
import org.codegist.crest.model.BunchOfData;
import org.codegist.crest.model.Data;
import org.codegist.crest.param.common.ISerializersTest;
import org.codegist.crest.serializer.BunchOfDataSerializer;
import org.codegist.crest.serializer.DataSerializer;

import java.util.Collection;

import static org.junit.Assert.assertEquals;

/**
 * @author laurent.gilles@codegist.org
 */
public class SerializersTest extends ISerializersTest<SerializersTest.Serializers> {

    public SerializersTest(CRestHolder crest) {
        super(crest, Serializers.class);
    }

    @EndPoint("{crest.server.end-point}")
    @Path("params/cookie/serializer")
    @GET
    public static interface Serializers extends ISerializersTest.ISerializers {

        @Path("default")
        String defaults(
                @CookieParam("p1") Data p1,
                @CookieParam("p2") Collection<BunchOfData<Data>> p2,
                @CookieParam("p3") BunchOfData<Data>[] p3);


        @Path("configured")
        String configured(
                @CookieParam("p1") @Serializer(DataSerializer.class) Data p1,
                @CookieParam("p2") @Serializer(BunchOfDataSerializer.class) Collection<BunchOfData<Data>> p2,
                @CookieParam("p3") @Serializer(BunchOfDataSerializer.class) BunchOfData<Data>[] p3);

        @Path("null")
        String nulls(
                @CookieParam("p1") @Serializer(DataSerializer.class) Data p1,
                @CookieParam("p2") @Serializer(BunchOfDataSerializer.class) Collection<BunchOfData<Data>> p2,
                @CookieParam("p3") @Serializer(BunchOfDataSerializer.class) BunchOfData<Data>[] p3);

    }

    @Override
    public void assertDefaultSerialize(String expectSerializedBof, String expectSerializedBof21, String expectSerializedBof22, String expectSerializedBof31, String expectSerializedBof32, String actual) {
        // these value doesn't mean anything, because the serialization generate cookies special characters (coma and semi-colon).
        // this test at least the the serialization process is executed for cookies as well
        assertEquals("default(cookies(count:18):[p1=Data{val1=123, val2='val-456'}, p2=AnotherBuchOfData(val1=31/12/2010 00:00:00, val2=false, val3=Data(val1=456, val2=val-789)), p2=AnotherBuchOfData(val1=20/01/2010 00:00:00, val2=false, val3=Data(val1=789, val2=val-123)), p3=AnotherBuchOfData(val1=02/12/2010 00:00:00, val2=true, val3=Data(val1=1456, val2=val-1789)), p3=AnotherBuchOfData(val1=23/03/2010 00:00:00, val2=true, val3=Data(val1=1789, val2=val-1123))]) p1=Data{val1=123 p2=AnotherBuchOfData(val1=20/01/2010 00:00:00 p3=AnotherBuchOfData(val1=23/03/2010 00:00:00", actual);
    }

    @Override
    public void assertConfiguredSerialize(String expectSerializedBof, String expectSerializedBof21, String expectSerializedBof22, String expectSerializedBof31, String expectSerializedBof32, String actual) {
        // these value doesn't mean anything, because the serialization generate cookies special characters (coma and semi-colon).
        // this test at least the the serialization process is executed for cookies as well
        assertEquals("configured(cookies(count:18):[p1=Data(val1=123, val2=val-456), p2=MyBuchOfData(val1=31/12/2010 00:00:00, val2=false, val3=Data(val1=456, val2=val-789)), p2=MyBuchOfData(val1=20/01/2010 00:00:00, val2=false, val3=Data(val1=789, val2=val-123)), p3=MyBuchOfData(val1=02/12/2010 00:00:00, val2=true, val3=Data(val1=1456, val2=val-1789)), p3=MyBuchOfData(val1=23/03/2010 00:00:00, val2=true, val3=Data(val1=1789, val2=val-1123))]) p1=Data(val1=123 p2=MyBuchOfData(val1=20/01/2010 00:00:00 p3=MyBuchOfData(val1=23/03/2010 00:00:00", actual);
    }
}
