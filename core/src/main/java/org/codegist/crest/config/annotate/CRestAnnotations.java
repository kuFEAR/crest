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

package org.codegist.crest.config.annotate;

import org.codegist.crest.annotate.*;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author laurent.gilles@codegist.org
 */
public final class CRestAnnotations {

    private CRestAnnotations(){
        throw new IllegalStateException();
    }

    public static Map<Class<? extends Annotation>, Class<? extends AnnotationHandler>> getMapping(){
        Map<Class<? extends Annotation>, Class<? extends AnnotationHandler>> handlers = new HashMap<Class<? extends Annotation>, Class<? extends AnnotationHandler>>();
        handlers.put(ConnectionTimeout.class, ConnectionTimeoutAnnotationHandler.class);
        handlers.put(Consumes.class, ConsumesAnnotationHandler.class);
        handlers.put(CookieParam.class, CookieParamAnnotationHandler.class);
        handlers.put(CookieParams.class, CookieParamsAnnotationHandler.class);
        handlers.put(DELETE.class, DELETEAnnotationHandler.class);
        handlers.put(Encoded.class, EncodedAnnotationHandler.class);
        handlers.put(Encoding.class, EncodingAnnotationHandler.class);
        handlers.put(EndPoint.class, EndPointAnnotationHandler.class);
        handlers.put(EntityWriter.class, EntityWriterAnnotationHandler.class);
        handlers.put(ErrorHandler.class, ErrorHandlerAnnotationHandler.class);
        handlers.put(FormParam.class, FormParamAnnotationHandler.class);
        handlers.put(FormParams.class, FormParamsAnnotationHandler.class);
        handlers.put(GET.class, GETAnnotationHandler.class);
        handlers.put(HEAD.class, HEADAnnotationHandler.class);
        handlers.put(HeaderParam.class, HeaderParamAnnotationHandler.class);
        handlers.put(HeaderParams.class, HeaderParamsAnnotationHandler.class);
        handlers.put(ListSeparator.class, ListSeparatorAnnotationHandler.class);
        handlers.put(MatrixParam.class, MatrixParamAnnotationHandler.class);
        handlers.put(MatrixParams.class, MatrixParamsAnnotationHandler.class);
        handlers.put(MultiPartParam.class, MultiPartParamAnnotationHandler.class);
        handlers.put(MultiPartParams.class, MultiPartParamsAnnotationHandler.class);
        handlers.put(OPTIONS.class, OPTIONSAnnotationHandler.class);
        handlers.put(Path.class, PathAnnotationHandler.class);
        handlers.put(PathParam.class, PathParamAnnotationHandler.class);
        handlers.put(PathParams.class, PathParamsAnnotationHandler.class);
        handlers.put(POST.class, POSTAnnotationHandler.class);
        handlers.put(Produces.class, ProducesAnnotationHandler.class);
        handlers.put(PUT.class, PUTAnnotationHandler.class);
        handlers.put(QueryParam.class, QueryParamAnnotationHandler.class);
        handlers.put(QueryParams.class, QueryParamsAnnotationHandler.class);
        handlers.put(RequestInterceptor.class, RequestInterceptorAnnotationHandler.class);
        handlers.put(ResponseHandler.class, ResponseHandlerAnnotationHandler.class);
        handlers.put(RetryHandler.class, RetryHandlerAnnotationHandler.class);
        handlers.put(Serializer.class, SerializerAnnotationHandler.class);
        handlers.put(SocketTimeout.class, SocketTimeoutAnnotationHandler.class);
        handlers.put(Deserializer.class, DeserializerAnnotationHandler.class);
        return Collections.unmodifiableMap(handlers);
    }
}
