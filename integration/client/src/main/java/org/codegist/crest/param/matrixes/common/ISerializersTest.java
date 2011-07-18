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

package org.codegist.crest.param.matrixes.common;

import org.codegist.crest.annotate.*;
import org.codegist.crest.model.BunchOfData;
import org.codegist.crest.model.Data;
import org.codegist.crest.serializer.BunchOfDataSerializer;
import org.codegist.crest.serializer.DataSerializer;

import java.util.Collection;

/**
 * @author laurent.gilles@codegist.org
 */
public class ISerializersTest<T extends ISerializersTest.ISerializers> extends org.codegist.crest.param.common.ISerializersTest<T> {

    public ISerializersTest(CRestHolder crest, Class<T> clazz) {
        super(crest, clazz);
    }

    @EndPoint("{crest.server.end-point}")
    @Path("params/matrix/serializer")
    @GET
    public static interface Serializers extends org.codegist.crest.param.common.ISerializersTest.ISerializers {

        @Path("default")
        String defaults(
                @MatrixParam("p1") Data p1,
                @MatrixParam("p2") Collection<BunchOfData<Data>> p2,
                @MatrixParam("p3") BunchOfData<Data>[] p3);


        @Path("configured")
        String configured(
                @MatrixParam("p1") @Serializer(DataSerializer.class) Data p1,
                @MatrixParam("p2") @Serializer(BunchOfDataSerializer.class) Collection<BunchOfData<Data>> p2,
                @MatrixParam("p3") @Serializer(BunchOfDataSerializer.class) BunchOfData<Data>[] p3);

        @Path("null")
        String nulls(
                @MatrixParam("p1") @Serializer(DataSerializer.class) Data p1,
                @MatrixParam("p2") @Serializer(BunchOfDataSerializer.class) Collection<BunchOfData<Data>> p2,
                @MatrixParam("p3") @Serializer(BunchOfDataSerializer.class) BunchOfData<Data>[] p3);

    }
}
