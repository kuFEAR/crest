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

package org.codegist.crest.config.annotate.jaxrs;

import org.codegist.crest.NonInstanciableClassTest;
import org.junit.Test;

import javax.ws.rs.*;

import static org.junit.Assert.assertEquals;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class JaxRsAnnotationsTest extends NonInstanciableClassTest {
    public JaxRsAnnotationsTest() {
        super(JaxRsAnnotations.class);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void mappingShouldBeUnmodifiable(){
        JaxRsAnnotations.MAPPING.clear();
    }

    @Test
    public void mappingShouldContainsAllSupportedJaxRsAnnotations(){
        assertEquals(17, JaxRsAnnotations.MAPPING.size());
        assertEquals(ConsumesAnnotationHandler.class, JaxRsAnnotations.MAPPING.get(Consumes.class));
        assertEquals(CookieParamAnnotationHandler.class, JaxRsAnnotations.MAPPING.get(CookieParam.class));
        assertEquals(DELETEAnnotationHandler.class, JaxRsAnnotations.MAPPING.get(DELETE.class));
        assertEquals(EncodedAnnotationHandler.class, JaxRsAnnotations.MAPPING.get(Encoded.class));
        assertEquals(FormParamAnnotationHandler.class, JaxRsAnnotations.MAPPING.get(FormParam.class));
        assertEquals(GETAnnotationHandler.class, JaxRsAnnotations.MAPPING.get(GET.class));
        assertEquals(HEADAnnotationHandler.class, JaxRsAnnotations.MAPPING.get(HEAD.class));
        assertEquals(HeaderParamAnnotationHandler.class, JaxRsAnnotations.MAPPING.get(HeaderParam.class));
        assertEquals(MatrixParamAnnotationHandler.class, JaxRsAnnotations.MAPPING.get(MatrixParam.class));
        assertEquals(OPTIONSAnnotationHandler.class, JaxRsAnnotations.MAPPING.get(OPTIONS.class));
        assertEquals(PathAnnotationHandler.class, JaxRsAnnotations.MAPPING.get(Path.class));
        assertEquals(PathParamAnnotationHandler.class, JaxRsAnnotations.MAPPING.get(PathParam.class));
        assertEquals(POSTAnnotationHandler.class, JaxRsAnnotations.MAPPING.get(POST.class));
        assertEquals(ProducesAnnotationHandler.class, JaxRsAnnotations.MAPPING.get(Produces.class));
        assertEquals(PUTAnnotationHandler.class, JaxRsAnnotations.MAPPING.get(PUT.class));
        assertEquals(QueryParamAnnotationHandler.class, JaxRsAnnotations.MAPPING.get(QueryParam.class));
        assertEquals(DefaultValueAnnotationHandler.class, JaxRsAnnotations.MAPPING.get(DefaultValue.class));
    }


}
