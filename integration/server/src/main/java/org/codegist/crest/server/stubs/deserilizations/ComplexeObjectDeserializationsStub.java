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

package org.codegist.crest.server.stubs.deserilizations;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author laurent.gilles@codegist.org
 */
@Path("deserialization/complexobject")
public class ComplexeObjectDeserializationsStub {

    private static final Date DATE;
    static{
        try {
            DATE = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+00:00").parse("1926-02-20T12:32:01+00:00");
        } catch (ParseException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    @GET
    @Path("xml")
    @Produces("application/xml")
    public String xml(
            @QueryParam("date-format") String dateFormat,
            @QueryParam("boolean-true") String booleanTrue,
            @QueryParam("boolean-false") String booleanFalse) {
        return getXml(dateFormat, booleanTrue, booleanFalse);
    }
    @GET
    @Path("xmls")
    @Produces("application/xml")
    public String xmls(
            @QueryParam("date-format") String dateFormat,
            @QueryParam("boolean-true") String booleanTrue,
            @QueryParam("boolean-false") String booleanFalse) {
        return "<someDatas>" + getXml(dateFormat, booleanTrue, booleanFalse) + getXml(dateFormat, booleanTrue, booleanFalse) + "</someDatas>";
    }
    @GET
    @Path("json")
    @Produces("application/json")
    public String json() {
        return getJson();
    }

    @GET
    @Path("jsons")
    @Produces("application/json")
    public String jsons() {
        return "[" + getJson() + "," + getJson() + "]";
    }     

    @GET
    @Produces("application/xml")
    public String forcedXml(
            @QueryParam("date-format") String dateFormat,
            @QueryParam("boolean-true") String booleanTrue,
            @QueryParam("boolean-false") String booleanFalse) {
        return getXml(dateFormat, booleanTrue, booleanFalse);
    }

    @GET
    @Produces("application/json")
    public String forcedJson() {
        return getJson();
    }





    private String getXml(String df, String booleanTrue, String booleanFalse){
        String d = new SimpleDateFormat(df).format(DATE);
        return "<someData>\n" +
            "\t<num>13</num>\n" +
            "\t<date>"+d+"</date>\n" +
            "\t<bool>"+booleanTrue+"</bool>\n" +
            "</someData>";
    }

    private String getJson(){
        return "{\n" +
            "\t\"num\":13,\n" +
            "\t\"date\":"+DATE.getTime()+",\n" +
            "\t\"bool\":true\n" +
            "}";
    }
}
