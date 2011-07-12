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

package org.codegist.crest.deserialization.crest;

import org.codegist.crest.annotate.*;
import org.codegist.crest.deserialization.common.CommonDeserializationsTest;
import org.codegist.crest.deserialization.common.IDeserializations;

import java.io.InputStream;
import java.io.Reader;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class DeserializationsTest extends CommonDeserializationsTest<DeserializationsTest.Deserializations> {

    public DeserializationsTest(CRestHolder crest) {
        super(crest, Deserializations.class);
    }

    /**
     * @author laurent.gilles@codegist.org
     */
    @EndPoint("{crest.server.end-point}")
    @Path("deserialization")
    @GET
    public static interface Deserializations extends IDeserializations {

        @Path("reader")
        Reader reader(@QueryParam("value") String value);

        @Path("inputstream")
        InputStream inputStream(@QueryParam("value") String value);

        @Path("ints")
        int[] getInts(@QueryParam("value") int[] values);

        @Path("get")
        String get();

        @HEAD
        @Path("void")
        void nothing(@QueryParam("value") String value);

        @HEAD
        @Path("void")
        Void nothing2(@QueryParam("value") String value);



        @Path("int")
        int getInt(@QueryParam("value") String value);

        @Path("byte")
        byte getByte(@QueryParam("value") String value);

        @Path("bytes")
        byte[] getBytes(@QueryParam("value") String value);

        @Path("short")
        short getShort(@QueryParam("value") String value);

        @Path("long")
        long getLong(@QueryParam("value") String value);

        @Path("float")
        float getFloat(@QueryParam("value") String value);

        @Path("double")
        double getDouble(@QueryParam("value") String value);

        @Path("char")
        char getChar(@QueryParam("value") String value);

        @Path("boolean")
        boolean getBoolean(@QueryParam("value") String value);

        @Path("byte")
        Byte getWrappedByte(@QueryParam("value") String value);

        @Path("short")
        Short getWrappedShort(@QueryParam("value") String value);

        @Path("int")
        Integer getWrappedInt(@QueryParam("value") String value);

        @Path("long")
        Long getWrappedLong(@QueryParam("value") String value);

        @Path("float")
        Float getWrappedFloat(@QueryParam("value") String value);

        @Path("double")
        Double getWrappedDouble(@QueryParam("value") String value);

        @Path("char")
        Character getWrappedChar(@QueryParam("value") String value);

        @Path("boolean")
        Boolean getWrappedBoolean(@QueryParam("value") String value);
    }
}
