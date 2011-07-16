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

package org.codegist.crest.impl.config.annotate.jaxrs;

import org.codegist.crest.impl.config.annotate.AnnotationHandler;

import javax.ws.rs.*;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author laurent.gilles@codegist.org
 */
public final class JaxRsAnnotations {

    private JaxRsAnnotations(){
        throw new IllegalStateException();
    }
    public static final  Map<Class<? extends Annotation>, Class<? extends AnnotationHandler>> MAPPING;

    static {
        Map<Class<? extends Annotation>, Class<? extends AnnotationHandler>> handlers = new HashMap<Class<? extends Annotation>, Class<? extends AnnotationHandler>>();
        handlers.put(Consumes.class, ConsumesAnnotationHandler.class);
        handlers.put(CookieParam.class, CookieParamAnnotationHandler.class);
        handlers.put(DELETE.class, DELETEAnnotationHandler.class);
        handlers.put(Encoded.class, EncodedAnnotationHandler.class);
        handlers.put(FormParam.class, FormParamAnnotationHandler.class);
        handlers.put(GET.class, GETAnnotationHandler.class);
        handlers.put(HEAD.class, HEADAnnotationHandler.class);
        handlers.put(HeaderParam.class, HeaderParamAnnotationHandler.class);
        handlers.put(MatrixParam.class, MatrixParamAnnotationHandler.class);
        handlers.put(OPTIONS.class, OPTIONSAnnotationHandler.class);
        handlers.put(Path.class, PathAnnotationHandler.class);
        handlers.put(PathParam.class, PathParamAnnotationHandler.class);
        handlers.put(POST.class, POSTAnnotationHandler.class);
        handlers.put(Produces.class, ProducesAnnotationHandler.class);
        handlers.put(PUT.class, PUTAnnotationHandler.class);
        handlers.put(QueryParam.class, QueryParamAnnotationHandler.class);
        handlers.put(DefaultValue.class, DefaultValueAnnotationHandler.class);
        MAPPING = Collections.unmodifiableMap(handlers);
    }
}
