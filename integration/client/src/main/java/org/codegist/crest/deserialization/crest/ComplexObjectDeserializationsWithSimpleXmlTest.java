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

import org.codegist.crest.CRestBuilder;
import org.codegist.crest.annotate.*;
import org.codegist.crest.deserialization.common.CommonComplexObjectDeserializationsTest;
import org.codegist.crest.deserialization.common.IComplexObjectDeserializations;
import org.codegist.crest.serializer.simplexml.SimpleXmlDeserializer;
import org.codegist.crest.util.SimpleXmlSomeDatasResponseHandler;
import org.codegist.crest.util.model.simplexml.SimpleXmlSomeData;
import org.junit.runners.Parameterized;

import java.util.Collection;
import java.util.List;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class ComplexObjectDeserializationsWithSimpleXmlTest extends CommonComplexObjectDeserializationsTest<ComplexObjectDeserializationsWithSimpleXmlTest.ComplexObjectDeserializationsWithSimpleXml> {

    public ComplexObjectDeserializationsWithSimpleXmlTest(CRestHolder crest) {
        super(crest, ComplexObjectDeserializationsWithSimpleXml.class);
    }

    @Parameterized.Parameters
    public static Collection<CRestHolder[]> getData() {
        return crest(arrify(forEachBaseBuilder(new Builder() {
            public CRestHolder build(CRestBuilder builder) {
                return new CRestHolder(builder.deserializeXmlWith(SimpleXmlDeserializer.class).build(), SIMPLEXML_SPECIFIC_PROPERTIES);
            }
        })));
    }

    /**
     * @author laurent.gilles@codegist.org
     */
    @EndPoint("{crest.server.end-point}")
    @Path("deserialization/complexobject")
    @GET
    public static interface ComplexObjectDeserializationsWithSimpleXml extends IComplexObjectDeserializations {


        @Path("xml")
        SimpleXmlSomeData someDataGuessed(
                @QueryParam("date-format") String dateFormat,
                @QueryParam("boolean-true") String booleanTrue,
                @QueryParam("boolean-false") String booleanFalse);

        @Consumes("application/xml")
        SimpleXmlSomeData someDataForced(
                @QueryParam("date-format") String dateFormat,
                @QueryParam("boolean-true") String booleanTrue,
                @QueryParam("boolean-false") String booleanFalse);

        @Path("xmls")
        @ResponseHandler(SimpleXmlSomeDatasResponseHandler.class)
        SimpleXmlSomeData[] someDatas(
                @QueryParam("date-format") String dateFormat,
                @QueryParam("boolean-true") String booleanTrue,
                @QueryParam("boolean-false") String booleanFalse);

        @Path("xmls")
        @ResponseHandler(SimpleXmlSomeDatasResponseHandler.class)
        List<SimpleXmlSomeData> someDatas2(
                @QueryParam("date-format") String dateFormat,
                @QueryParam("boolean-true") String booleanTrue,
                @QueryParam("boolean-false") String booleanFalse);

    }
}
