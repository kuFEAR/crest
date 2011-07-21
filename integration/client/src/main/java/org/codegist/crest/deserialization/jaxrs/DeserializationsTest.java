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

package org.codegist.crest.deserialization.jaxrs;

import org.codegist.crest.annotate.EndPoint;
import org.codegist.crest.deserialization.common.CommonDeserializationsTest;
import org.codegist.crest.deserialization.common.IDeserializations;

import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.io.InputStream;
import java.io.Reader;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class DeserializationsTest extends CommonDeserializationsTest<org.codegist.crest.deserialization.crest.DeserializationsTest.Deserializations> {

    public DeserializationsTest(CRestHolder crest) {
        super(crest, org.codegist.crest.deserialization.crest.DeserializationsTest.Deserializations.class);
    }

    /**
     * @author laurent.gilles@codegist.org
     */
    @EndPoint("{crest.server.end-point}")
    @Path("deserialization")
    public static interface Deserializations extends IDeserializations {

        @GET
        @Path("reader")
        Reader reader(@QueryParam("value") String value);

        @GET
        @Path("inputstream")
        InputStream inputStream(@QueryParam("value") String value);

        @GET
        @Path("ints")
        int[] getInts(@QueryParam("value") int[] values);

        @GET
        @Path("ints")
        long[] getLongs(@QueryParam("value") long[] values);

        @GET
        @Path("get")
        String get();

        @HEAD
        @Path("void")
        void nothing(@QueryParam("value") String value);

        @HEAD
        @Path("void")
        Void nothing2(@QueryParam("value") String value);

        @GET
        @Path("int")
        int getInt(@QueryParam("value") String value);

        @GET
        @Path("byte")
        byte getByte(@QueryParam("value") String value);

        @GET
        @Path("bytes")
        byte[] getBytes(@QueryParam("value") String value);

        @GET
        @Path("short")
        short getShort(@QueryParam("value") String value);

        @GET
        @Path("long")
        long getLong(@QueryParam("value") String value);

        @GET
        @Path("float")
        float getFloat(@QueryParam("value") String value);

        @GET
        @Path("double")
        double getDouble(@QueryParam("value") String value);

        @GET
        @Path("char")
        char getChar(@QueryParam("value") String value);

        @GET
        @Path("boolean")
        boolean getBoolean(@QueryParam("value") String value);

        @GET
        @Path("byte")
        Byte getWrappedByte(@QueryParam("value") String value);

        @GET
        @Path("short")
        Short getWrappedShort(@QueryParam("value") String value);

        @GET
        @Path("int")
        Integer getWrappedInt(@QueryParam("value") String value);

        @GET
        @Path("long")
        Long getWrappedLong(@QueryParam("value") String value);

        @GET
        @Path("float")
        Float getWrappedFloat(@QueryParam("value") String value);

        @GET
        @Path("double")
        Double getWrappedDouble(@QueryParam("value") String value);

        @GET
        @Path("char")
        Character getWrappedChar(@QueryParam("value") String value);

        @GET
        @Path("boolean")
        Boolean getWrappedBoolean(@QueryParam("value") String value);
    }
}
