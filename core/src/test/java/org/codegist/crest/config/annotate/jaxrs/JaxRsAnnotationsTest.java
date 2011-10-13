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

package org.codegist.crest.config.annotate.jaxrs;

import org.codegist.crest.NonInstanciableClassTest;
import org.junit.Test;

import javax.ws.rs.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class JaxRsAnnotationsTest extends NonInstanciableClassTest {
    public JaxRsAnnotationsTest() {
        super(JaxRsAnnotations.class);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void mappingShouldBeUnmodifiable(){
        JaxRsAnnotations.getMapping().clear();
    }

    @Test
    public void isJaxRsAwareShouldReturnTrueAsAvailableInTestClassPath(){
        assertTrue(JaxRsAnnotations.isJaxRsAware());
    }

    @Test
    public void mappingShouldContainsAllSupportedJaxRsAnnotations(){
        assertEquals(17, JaxRsAnnotations.getMapping().size());
        assertEquals(ConsumesAnnotationHandler.class, JaxRsAnnotations.getMapping().get(Consumes.class));
        assertEquals(CookieParamAnnotationHandler.class, JaxRsAnnotations.getMapping().get(CookieParam.class));
        assertEquals(DELETEAnnotationHandler.class, JaxRsAnnotations.getMapping().get(DELETE.class));
        assertEquals(EncodedAnnotationHandler.class, JaxRsAnnotations.getMapping().get(Encoded.class));
        assertEquals(FormParamAnnotationHandler.class, JaxRsAnnotations.getMapping().get(FormParam.class));
        assertEquals(GETAnnotationHandler.class, JaxRsAnnotations.getMapping().get(GET.class));
        assertEquals(HEADAnnotationHandler.class, JaxRsAnnotations.getMapping().get(HEAD.class));
        assertEquals(HeaderParamAnnotationHandler.class, JaxRsAnnotations.getMapping().get(HeaderParam.class));
        assertEquals(MatrixParamAnnotationHandler.class, JaxRsAnnotations.getMapping().get(MatrixParam.class));
        assertEquals(OPTIONSAnnotationHandler.class, JaxRsAnnotations.getMapping().get(OPTIONS.class));
        assertEquals(PathAnnotationHandler.class, JaxRsAnnotations.getMapping().get(Path.class));
        assertEquals(PathParamAnnotationHandler.class, JaxRsAnnotations.getMapping().get(PathParam.class));
        assertEquals(POSTAnnotationHandler.class, JaxRsAnnotations.getMapping().get(POST.class));
        assertEquals(ProducesAnnotationHandler.class, JaxRsAnnotations.getMapping().get(Produces.class));
        assertEquals(PUTAnnotationHandler.class, JaxRsAnnotations.getMapping().get(PUT.class));
        assertEquals(QueryParamAnnotationHandler.class, JaxRsAnnotations.getMapping().get(QueryParam.class));
        assertEquals(DefaultValueAnnotationHandler.class, JaxRsAnnotations.getMapping().get(DefaultValue.class));
    }


}
