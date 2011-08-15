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
        CRestAnnotations.MAPPING.clear();
    }

    @Test
    public void mappingShouldContainsAllSupportedJaxRsAnnotations(){
        
        assertEquals(37, CRestAnnotations.MAPPING.size());
        assertEquals(ConnectionTimeoutAnnotationHandler.class, CRestAnnotations.MAPPING.get(ConnectionTimeout.class));
        assertEquals(ConsumesAnnotationHandler.class, CRestAnnotations.MAPPING.get(Consumes.class));
        assertEquals(CookieParamAnnotationHandler.class, CRestAnnotations.MAPPING.get(CookieParam.class));
        assertEquals(CookieParamsAnnotationHandler.class, CRestAnnotations.MAPPING.get(CookieParams.class));
        assertEquals(DELETEAnnotationHandler.class, CRestAnnotations.MAPPING.get(DELETE.class));
        assertEquals(EncodedAnnotationHandler.class, CRestAnnotations.MAPPING.get(Encoded.class));
        assertEquals(EncodingAnnotationHandler.class, CRestAnnotations.MAPPING.get(Encoding.class));
        assertEquals(EndPointAnnotationHandler.class, CRestAnnotations.MAPPING.get(EndPoint.class));
        assertEquals(EntityWriterAnnotationHandler.class, CRestAnnotations.MAPPING.get(EntityWriter.class));
        assertEquals(ErrorHandlerAnnotationHandler.class, CRestAnnotations.MAPPING.get(ErrorHandler.class));
        assertEquals(FormParamAnnotationHandler.class, CRestAnnotations.MAPPING.get(FormParam.class));
        assertEquals(FormParamsAnnotationHandler.class, CRestAnnotations.MAPPING.get(FormParams.class));
        assertEquals(GETAnnotationHandler.class, CRestAnnotations.MAPPING.get(GET.class));
        assertEquals(HEADAnnotationHandler.class, CRestAnnotations.MAPPING.get(HEAD.class));
        assertEquals(HeaderParamAnnotationHandler.class, CRestAnnotations.MAPPING.get(HeaderParam.class));
        assertEquals(HeaderParamsAnnotationHandler.class, CRestAnnotations.MAPPING.get(HeaderParams.class));
        assertEquals(ListSeparatorAnnotationHandler.class, CRestAnnotations.MAPPING.get(ListSeparator.class));
        assertEquals(MatrixParamAnnotationHandler.class, CRestAnnotations.MAPPING.get(MatrixParam.class));
        assertEquals(MatrixParamsAnnotationHandler.class, CRestAnnotations.MAPPING.get(MatrixParams.class));
        assertEquals(MultiPartParamAnnotationHandler.class, CRestAnnotations.MAPPING.get(MultiPartParam.class));
        assertEquals(MultiPartParamsAnnotationHandler.class, CRestAnnotations.MAPPING.get(MultiPartParams.class));
        assertEquals(OPTIONSAnnotationHandler.class, CRestAnnotations.MAPPING.get(OPTIONS.class));
        assertEquals(PathAnnotationHandler.class, CRestAnnotations.MAPPING.get(Path.class));
        assertEquals(PathParamAnnotationHandler.class, CRestAnnotations.MAPPING.get(PathParam.class));
        assertEquals(PathParamsAnnotationHandler.class, CRestAnnotations.MAPPING.get(PathParams.class));
        assertEquals(POSTAnnotationHandler.class, CRestAnnotations.MAPPING.get(POST.class));
        assertEquals(ProducesAnnotationHandler.class, CRestAnnotations.MAPPING.get(Produces.class));
        assertEquals(PUTAnnotationHandler.class, CRestAnnotations.MAPPING.get(PUT.class));
        assertEquals(QueryParamAnnotationHandler.class, CRestAnnotations.MAPPING.get(QueryParam.class));
        assertEquals(QueryParamsAnnotationHandler.class, CRestAnnotations.MAPPING.get(QueryParams.class));
        assertEquals(RequestInterceptorAnnotationHandler.class, CRestAnnotations.MAPPING.get(RequestInterceptor.class));
        assertEquals(ResponseHandlerAnnotationHandler.class, CRestAnnotations.MAPPING.get(ResponseHandler.class));
        assertEquals(RetryHandlerAnnotationHandler.class, CRestAnnotations.MAPPING.get(RetryHandler.class));
        assertEquals(SerializerAnnotationHandler.class, CRestAnnotations.MAPPING.get(Serializer.class));
        assertEquals(SocketTimeoutAnnotationHandler.class, CRestAnnotations.MAPPING.get(SocketTimeout.class));
        assertEquals(MultiPartEntityAnnotationHandler.class, CRestAnnotations.MAPPING.get(MultiPartEntity.class));
        assertEquals(DeserializerAnnotationHandler.class, CRestAnnotations.MAPPING.get(Deserializer.class));
    }


}
