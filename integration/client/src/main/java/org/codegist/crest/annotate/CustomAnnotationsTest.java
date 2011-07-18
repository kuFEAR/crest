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

package org.codegist.crest.annotate;

import org.codegist.crest.BaseCRestTest;
import org.codegist.crest.CRestBuilder;
import org.codegist.crest.serializer.CommaSeparatedIntDeserializer;
import org.junit.Test;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static org.junit.Assert.assertEquals;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class CustomAnnotationsTest extends BaseCRestTest<CustomAnnotationsTest.CustomAnnotations> {

    public CustomAnnotationsTest(CRestHolder holder) {
        super(holder, CustomAnnotations.class);
    }

    @Parameterized.Parameters
    public static Collection<CRestHolder[]> getData() {
        return crest(arrify(forEachBaseBuilder(new Builder() {
            public CRestHolder build(CRestBuilder builder) {
                return new CRestHolder(builder
                        .bind(CustomAnnotationHandler.class, CustomAnnotation.class)
                        .build());
            }
        })));
    }

    @Test
    public void testUnknown(){
        assertEquals("unknown() p0=my-value", toTest.unknown("my-value"));
    }

    @Test
    public void testCustom(){
        assertEquals("custom() p0=p3-val p1=p1-val p2=p2-val p3=null", toTest.custom(null));
    }

    @EndPoint("{crest.server.end-point}")
    @Path("annotate/custom")
    @GET
    @UnmappedAnnotation
    @CustomAnnotation(name="p1",value="p1-val")
    public interface CustomAnnotations {

        @Path("unknown")
        @UnmappedAnnotation
        String unknown(@UnmappedAnnotation @QueryParam("p0") String p1);   // todo why "p" do not work ? problem in oauth sorting ?

        @Path("custom")
        @CustomAnnotation(name="p2",value="p2-val")
        String custom(
        @QueryParam("p0") @CustomAnnotation(name="p3",value="p3-val") String p1);
    }
}
