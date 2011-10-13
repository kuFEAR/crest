/*
 * Copyright 2011 CodeGist.org
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

package org.codegist.crest.param.multiparts.common;

import org.codegist.crest.annotate.*;
import org.codegist.crest.param.common.ISerializersTest;
import org.codegist.crest.util.BunchOfDataSerializer;
import org.codegist.crest.util.DataSerializer;
import org.codegist.crest.util.model.BunchOfData;
import org.codegist.crest.util.model.Data;

import java.util.Collection;

/**
 * @author laurent.gilles@codegist.org
 */
public class SerializersTest extends ISerializersTest<SerializersTest.Serializers> {

    public SerializersTest(CRestHolder crest) {
        super(crest, Serializers.class);
    }

    @EndPoint("{crest.server.end-point}")
    @Path("params/multipart/serializer")
    @POST
    public static interface Serializers extends ISerializersTest.ISerializers {

        @Path("default")
        String defaults(
                @MultiPartParam("p1") Data p1,
                @MultiPartParam("p2") Collection<BunchOfData<Data>> p2,
                @MultiPartParam("p3") BunchOfData<Data>[] p3);


        @Path("configured")
        String configured(
                @MultiPartParam("p1") @Serializer(DataSerializer.class) Data p1,
                @MultiPartParam("p2") @Serializer(BunchOfDataSerializer.class) Collection<BunchOfData<Data>> p2,
                @MultiPartParam("p3") @Serializer(BunchOfDataSerializer.class) BunchOfData<Data>[] p3);

        @Path("null")
        @MultiPartParam(value="p0", defaultValue = "some val") // default value so that jersey accepts the requests
        String nulls(
                @MultiPartParam("p1") @Serializer(DataSerializer.class) Data p1,
                @MultiPartParam("p2") @Serializer(BunchOfDataSerializer.class) Collection<BunchOfData<Data>> p2,
                @MultiPartParam("p3") @Serializer(BunchOfDataSerializer.class) BunchOfData<Data>[] p3);

    }
}
