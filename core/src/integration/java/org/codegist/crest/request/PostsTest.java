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

package org.codegist.crest.request;

import org.codegist.crest.CRest;
import org.codegist.crest.JsonEntityWriter;
import org.codegist.crest.XmlEntityWriter;
import org.codegist.crest.annotate.EntityWriter;
import org.codegist.crest.annotate.Path;
import org.codegist.crest.annotate.Produces;
import org.codegist.crest.request.common.CommonRequestsTest;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static org.junit.Assert.assertEquals;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
//@Ignore
public class PostsTest extends CommonRequestsTest<Posts> {

    public PostsTest(CRest crest) {
        super(crest, Posts.class);
    }

    @Parameterized.Parameters
    public static Collection<CRest[]> getData() {
        return crest(byRestServicesAndCustomContentTypes());
    }


    @Test
    public void testRaw(){
        String actual = toTest.raw();
        assertEquals("raw() content-type-header=[application/x-www-form-urlencoded; charset=UTF-8] accepts-header=[*/*]", actual);
    }

    @Test
    public void testAccept(){
        String actual = toTest.accept();
        assertEquals("accept() content-type-header=[application/x-www-form-urlencoded; charset=UTF-8] accepts-header=[application/custom1, application/custom2]", actual);
    }

    @Test
    public void testContentType(){
        String actual = toTest.contentType();
        assertEquals("contentType() content-type-header=[application/custom] accepts-header=[*/*]", actual);
    }


    @Test
    public void testXmlEntityWriter(){
        String actual = toTest.xmlEntityWriter();
        assertEquals("xmlEntityWriter() content-type-header=[application/xml] accepts-header=[*/*]", actual);
    }

    @Test
    public void testXmlEntityWriterWithProduces(){
        String actual = toTest.xmlEntityWriterWithProduces();
        assertEquals("xmlEntityWriterWithProduces() content-type-header=[application/custom] accepts-header=[*/*]", actual);
    }

    @Test
    public void testJsonEntityWriter(){
        String actual = toTest.jsonEntityWriter();
        assertEquals("jsonEntityWriter() content-type-header=[application/json] accepts-header=[*/*]", actual);
    }

    @Test
    public void testJsonEntityWriterWithProduces(){
        String actual = toTest.jsonEntityWriterWithProduces();
        assertEquals("jsonEntityWriterWithProduces() content-type-header=[application/custom] accepts-header=[*/*]", actual);
    }

}
