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

package org.codegist.crest.request.common;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public abstract class CommonEntityRequestsTest<T extends EntityRequests> extends CommonRequestsTest<T> {

    public CommonEntityRequestsTest(CRestHolder crest, Class<T> service) {
        super(crest, service);
    }


    public static CRestHolder[] byRestServicesAndCustomContentTypes() {
        return new CRestHolder[]{
                /* HttpURLConnection based CRest */
                new CRestHolder(baseBuilder()
                        .bindPlainTextDeserializerWith("text/html", "application/custom", "application/custom1", "application/custom2")
                        .build()),
                /* Apache HttpClient based CRest */
                new CRestHolder(baseBuilder()
                        .bindPlainTextDeserializerWith("text/html", "application/custom", "application/custom1", "application/custom2")
                        .useHttpClientRestService()
                        .build()),
        };
    }


    @Test
    public void testRaw() {
        String actual = toTest.raw().toString();
        assertEquals("raw() content-type-header=[application/x-www-form-urlencoded; charset=UTF-8] accepts-header=[*/*]", actual);
    }

    @Test
    public void testAccept() {
        String actual = toTest.accept().toString();
        assertEquals("accept() content-type-header=[application/x-www-form-urlencoded; charset=UTF-8] accepts-header=[application/custom1,application/custom2]", actual);
    }

    @Test
    public void testContentType() {
        String actual = toTest.contentType().toString();
        assertEquals("contentType() content-type-header=[application/custom] accepts-header=[*/*]", actual);
    }


    @Test
    public void testXmlEntityWriter() {
        String actual = toTest.xmlEntityWriter();
        assertEquals("xmlEntityWriter() content-type-header=[application/xml] accepts-header=[*/*]", actual);
    }

    @Test
    public void testXmlEntityWriterWithProduces() {
        String actual = toTest.xmlEntityWriterWithProduces();
        assertEquals("xmlEntityWriterWithProduces() content-type-header=[application/custom] accepts-header=[*/*]", actual);
    }

    @Test
    public void testJsonEntityWriter() {
        String actual = toTest.jsonEntityWriter();
        assertEquals("jsonEntityWriter() content-type-header=[application/json] accepts-header=[*/*]", actual);
    }

    @Test
    public void testJsonEntityWriterWithProduces() {
        String actual = toTest.jsonEntityWriterWithProduces();
        assertEquals("jsonEntityWriterWithProduces() content-type-header=[application/custom] accepts-header=[*/*]", actual);
    }

}
