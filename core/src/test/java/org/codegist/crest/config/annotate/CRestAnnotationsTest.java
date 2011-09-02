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

package org.codegist.crest.config.annotate;

import org.codegist.crest.NonInstanciableClassTest;
import org.codegist.crest.annotate.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class CRestAnnotationsTest extends NonInstanciableClassTest {
    
    public CRestAnnotationsTest() {
        super(CRestAnnotations.class);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void mappingShouldBeUnmodifiable(){
        CRestAnnotations.getMapping().clear();
    }

    @Test
    public void mappingShouldContainsAllSupportedJaxRsAnnotations(){
        
        assertEquals(37, CRestAnnotations.getMapping().size());
        assertEquals(ConnectionTimeoutAnnotationHandler.class, CRestAnnotations.getMapping().get(ConnectionTimeout.class));
        assertEquals(ConsumesAnnotationHandler.class, CRestAnnotations.getMapping().get(Consumes.class));
        assertEquals(CookieParamAnnotationHandler.class, CRestAnnotations.getMapping().get(CookieParam.class));
        assertEquals(CookieParamsAnnotationHandler.class, CRestAnnotations.getMapping().get(CookieParams.class));
        assertEquals(DELETEAnnotationHandler.class, CRestAnnotations.getMapping().get(DELETE.class));
        assertEquals(EncodedAnnotationHandler.class, CRestAnnotations.getMapping().get(Encoded.class));
        assertEquals(EncodingAnnotationHandler.class, CRestAnnotations.getMapping().get(Encoding.class));
        assertEquals(EndPointAnnotationHandler.class, CRestAnnotations.getMapping().get(EndPoint.class));
        assertEquals(EntityWriterAnnotationHandler.class, CRestAnnotations.getMapping().get(EntityWriter.class));
        assertEquals(ErrorHandlerAnnotationHandler.class, CRestAnnotations.getMapping().get(ErrorHandler.class));
        assertEquals(FormParamAnnotationHandler.class, CRestAnnotations.getMapping().get(FormParam.class));
        assertEquals(FormParamsAnnotationHandler.class, CRestAnnotations.getMapping().get(FormParams.class));
        assertEquals(GETAnnotationHandler.class, CRestAnnotations.getMapping().get(GET.class));
        assertEquals(HEADAnnotationHandler.class, CRestAnnotations.getMapping().get(HEAD.class));
        assertEquals(HeaderParamAnnotationHandler.class, CRestAnnotations.getMapping().get(HeaderParam.class));
        assertEquals(HeaderParamsAnnotationHandler.class, CRestAnnotations.getMapping().get(HeaderParams.class));
        assertEquals(ListSeparatorAnnotationHandler.class, CRestAnnotations.getMapping().get(ListSeparator.class));
        assertEquals(MatrixParamAnnotationHandler.class, CRestAnnotations.getMapping().get(MatrixParam.class));
        assertEquals(MatrixParamsAnnotationHandler.class, CRestAnnotations.getMapping().get(MatrixParams.class));
        assertEquals(MultiPartParamAnnotationHandler.class, CRestAnnotations.getMapping().get(MultiPartParam.class));
        assertEquals(MultiPartParamsAnnotationHandler.class, CRestAnnotations.getMapping().get(MultiPartParams.class));
        assertEquals(OPTIONSAnnotationHandler.class, CRestAnnotations.getMapping().get(OPTIONS.class));
        assertEquals(PathAnnotationHandler.class, CRestAnnotations.getMapping().get(Path.class));
        assertEquals(PathParamAnnotationHandler.class, CRestAnnotations.getMapping().get(PathParam.class));
        assertEquals(PathParamsAnnotationHandler.class, CRestAnnotations.getMapping().get(PathParams.class));
        assertEquals(POSTAnnotationHandler.class, CRestAnnotations.getMapping().get(POST.class));
        assertEquals(ProducesAnnotationHandler.class, CRestAnnotations.getMapping().get(Produces.class));
        assertEquals(PUTAnnotationHandler.class, CRestAnnotations.getMapping().get(PUT.class));
        assertEquals(QueryParamAnnotationHandler.class, CRestAnnotations.getMapping().get(QueryParam.class));
        assertEquals(QueryParamsAnnotationHandler.class, CRestAnnotations.getMapping().get(QueryParams.class));
        assertEquals(RequestInterceptorAnnotationHandler.class, CRestAnnotations.getMapping().get(RequestInterceptor.class));
        assertEquals(ResponseHandlerAnnotationHandler.class, CRestAnnotations.getMapping().get(ResponseHandler.class));
        assertEquals(RetryHandlerAnnotationHandler.class, CRestAnnotations.getMapping().get(RetryHandler.class));
        assertEquals(SerializerAnnotationHandler.class, CRestAnnotations.getMapping().get(Serializer.class));
        assertEquals(SocketTimeoutAnnotationHandler.class, CRestAnnotations.getMapping().get(SocketTimeout.class));
        assertEquals(MultiPartEntityAnnotationHandler.class, CRestAnnotations.getMapping().get(MultiPartEntity.class));
        assertEquals(DeserializerAnnotationHandler.class, CRestAnnotations.getMapping().get(Deserializer.class));
    }


}
