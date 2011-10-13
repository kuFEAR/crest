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

package org.codegist.crest.server.stubs.deserilizations;

import javax.ws.rs.*;
import java.util.List;

import static org.codegist.common.collect.Collections.join;

/**
 * @author laurent.gilles@codegist.org
 */
@Produces("text/html;charset=UTF-8")
@Path("deserialization")
public class DeserializationsStub {

    private String value;

    @GET
    @Path("reader")
    public String reader(@QueryParam("value") String value) {
        return value;
    }

    @GET
    @Path("inputstream")
    public String inputstream(@QueryParam("value") String value) {
        return value;
    }

    @GET
    @Path("ints")
    public String primitives(@QueryParam("value") List<Integer> values) {
        return join(",", values);
    }


    @GET
    @Path("get")
    public String get() {
        return value;
    }

    @HEAD
    @Path("void")
    public void nothing(@QueryParam("value") String value) {
        this.value = value;
    }


    @GET
    @Path("int")
    public String getInt(@QueryParam("value") String value) {
        return value;
    }

    @GET
    @Path("byte")
    public String getByte(@QueryParam("value") String value){
        return value;
    }

    @GET
    @Path("bytes")
    public String getBytes(@QueryParam("value") String value){
        return value;
    }

    @GET
    @Path("short")
    public String getShort(@QueryParam("value") String value){
        return value;
    }

    @GET
    @Path("long")
    public String getLong(@QueryParam("value") String value){
        return value;
    }

    @GET
    @Path("float")
    public String getFloat(@QueryParam("value") String value){
        return value;
    }

    @GET
    @Path("double")
    public String getDouble(@QueryParam("value") String value){
        return value;
    }

    @GET
    @Path("char")
    public String getChar(@QueryParam("value") String value){
        return value;
    }

    @GET
    @Path("boolean")
    public String getBoolean(@QueryParam("value") String value){
        return value;
    }

    

}
