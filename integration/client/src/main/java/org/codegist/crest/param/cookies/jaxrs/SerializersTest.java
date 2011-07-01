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

package org.codegist.crest.param.cookies.jaxrs;

import org.codegist.crest.annotate.EndPoint;
import org.codegist.crest.annotate.Serializer;
import org.codegist.crest.model.BunchOfData;
import org.codegist.crest.model.Data;
import org.codegist.crest.param.cookies.common.ISerializersTest;
import org.codegist.crest.serializer.BunchOfDataSerializer;
import org.codegist.crest.serializer.DataSerializer;

import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.Collection;

/**
 * @author laurent.gilles@codegist.org
 */
public class SerializersTest extends ISerializersTest<SerializersTest.Serializers> {

    public SerializersTest(CRestHolder crest) {
        super(crest, Serializers.class);
    }

    @EndPoint("{crest.server.end-point}")
    @Path("params/cookie/serializer")
    public static interface Serializers extends ISerializersTest.ISerializers {

        @GET
        @Path("default")
        String defaults(
                @CookieParam("p1") Data p1,
                @CookieParam("p2") Collection<BunchOfData<Data>> p2,
                @CookieParam("p3") BunchOfData<Data>[] p3);


        @GET
        @Path("configured")
        String configured(
                @CookieParam("p1") @Serializer(DataSerializer.class) Data p1,
                @CookieParam("p2") @Serializer(BunchOfDataSerializer.class) Collection<BunchOfData<Data>> p2,
                @CookieParam("p3") @Serializer(BunchOfDataSerializer.class) BunchOfData<Data>[] p3);

        @GET
        @Path("null")
        String nulls(
                @CookieParam("p1") @Serializer(DataSerializer.class) Data p1,
                @CookieParam("p2") @Serializer(BunchOfDataSerializer.class) Collection<BunchOfData<Data>> p2,
                @CookieParam("p3") @Serializer(BunchOfDataSerializer.class) BunchOfData<Data>[] p3);

    }

}
