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

import static java.util.regex.Pattern.quote;
import static org.junit.Assert.assertEquals;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public abstract class CommonEntityRequestsTest<T extends EntityRequests> extends CommonRequestsTest<T> {

    public CommonEntityRequestsTest(CRestHolder crest, Class<T> service) {
        super(crest, service);
    }

    @Test
    public void testRaw() {
        String actual = toTest.raw();
        assertEquals("raw() content-type-header=[application/x-www-form-urlencoded; charset=UTF-8] accepts-header=[*/*]", actual);
    }

    @Test
    public void testAccept() {
        String actual = toTest.accept();
        assertEquals("accept() content-type-header=[application/x-www-form-urlencoded; charset=UTF-8] accepts-header=[application/custom1,application/custom2]", actual);
    }

    @Test
    public void testContentType() {
        String actual = toTest.contentType();
        assertEquals("contentType() content-type-header=[application/custom] accepts-header=[*/*]", actual);
    }


    @Test
    public void testXmlEntity() {
        String actual = toTest.xmlEntity();
        assertEquals("xmlEntity() content-type-header=[application/xml] accepts-header=[*/*]", actual);
    }

    @Test
    public void testXmlEntityWithProduces() {
        String actual = toTest.xmlEntityWithProduces();
        assertEquals("xmlEntityWithProduces() content-type-header=[application/custom] accepts-header=[*/*]", actual);
    }

    @Test
    public void testJsonEntity() {
        String actual = toTest.jsonEntity();
        assertEquals("jsonEntity() content-type-header=[application/json] accepts-header=[*/*]", actual);
    }

    @Test
    public void testJsonEntityWithProduces() {
        String actual = toTest.jsonEntityWithProduces();
        assertEquals("jsonEntityWithProduces() content-type-header=[application/custom] accepts-header=[*/*]", actual);
    }

    @Test
    public void testMultipartEntity() {
        String actual = toTest.multipartEntity();
        assertMatches(quote("multipartEntity() content-type-header=[multipart/form-data; boundary=") + "\\w+" + quote("] accepts-header=[*/*]"), actual);
    }

    @Test
    public void testMultipartEntityWithProduces() {
        String actual = toTest.multipartEntityWithProduces();
        assertEquals("multipartEntityWithProduces() content-type-header=[application/custom] accepts-header=[*/*]", actual);
    }

}
