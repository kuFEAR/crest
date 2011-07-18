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
import org.codegist.crest.handler.JaxbSomeDatasResponseHandler;
import org.codegist.crest.annotate.GET;
import org.codegist.crest.annotate.QueryParam;
import org.codegist.crest.model.jaxb.JaxbSomeData;
import org.junit.runners.Parameterized;

import java.util.Collection;
import java.util.List;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class ComplexObjectDeserializationsWithJaxbTest extends CommonComplexObjectDeserializationsTest<ComplexObjectDeserializationsWithJaxbTest.ComplexObjectDeserializationsWithJaxb> {

    public ComplexObjectDeserializationsWithJaxbTest(CRestHolder crest) {
        super(crest, ComplexObjectDeserializationsWithJaxb.class);
    }

    @Parameterized.Parameters
    public static Collection<CRestHolder[]> getData() {
        return crest(arrify(forEachBaseBuilder(new Builder() {
            public CRestHolder build(CRestBuilder builder) {
                return new CRestHolder(builder.deserializeXmlWithJaxb().build(), JAXB_SPECIFIC_PROPERTIES);
            }
        })));
    }


    /**
     * @author laurent.gilles@codegist.org
     */
    @EndPoint("{crest.server.end-point}")
    @Path("deserialization/complexobject")
    @GET
    public static interface ComplexObjectDeserializationsWithJaxb extends IComplexObjectDeserializations {


        @Path("xml")
        JaxbSomeData someDataGuessed(
                @QueryParam("date-format") String dateFormat,
                @QueryParam("boolean-true") String booleanTrue,
                @QueryParam("boolean-false") String booleanFalse);

        @Consumes("application/xml")
        JaxbSomeData someDataForced(
                @QueryParam("date-format") String dateFormat,
                @QueryParam("boolean-true") String booleanTrue,
                @QueryParam("boolean-false") String booleanFalse);

        @Path("xmls")
        @ResponseHandler(JaxbSomeDatasResponseHandler.class)
        JaxbSomeData[] someDatas(
                @QueryParam("date-format") String dateFormat,
                @QueryParam("boolean-true") String booleanTrue,
                @QueryParam("boolean-false") String booleanFalse);

        @Path("xmls")
        @ResponseHandler(JaxbSomeDatasResponseHandler.class)
        List<JaxbSomeData> someDatas2(
                @QueryParam("date-format") String dateFormat,
                @QueryParam("boolean-true") String booleanTrue,
                @QueryParam("boolean-false") String booleanFalse);

    }
}
