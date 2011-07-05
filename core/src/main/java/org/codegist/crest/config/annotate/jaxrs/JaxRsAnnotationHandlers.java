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

import org.codegist.crest.config.annotate.AnnotationHandler;

import javax.ws.rs.*;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * @author laurent.gilles@codegist.org
 */
public final class JaxRsAnnotationHandlers {

    private JaxRsAnnotationHandlers(){
        throw new IllegalStateException();
    }

    public static Map<Class<? extends Annotation>, AnnotationHandler<?>> getHandlersMap(){
        Map<Class<? extends Annotation>, AnnotationHandler<?>> handlers = new HashMap<Class<? extends Annotation>, AnnotationHandler<?>>();
        handlers.put(Consumes.class, new ConsumesAnnotationHandler());
        handlers.put(CookieParam.class, new CookieParamAnnotationHandler());
        handlers.put(DELETE.class, new DELETEAnnotationHandler());
        handlers.put(Encoded.class, new EncodedAnnotationHandler());
        handlers.put(FormParam.class, new FormParamAnnotationHandler());
        handlers.put(GET.class, new GETAnnotationHandler());
        handlers.put(HEAD.class, new HEADAnnotationHandler());
        handlers.put(HeaderParam.class, new HeaderParamAnnotationHandler());
        handlers.put(MatrixParam.class, new MatrixParamAnnotationHandler());
        handlers.put(OPTIONS.class, new OPTIONSAnnotationHandler());
        handlers.put(Path.class, new PathAnnotationHandler());
        handlers.put(PathParam.class, new PathParamAnnotationHandler());
        handlers.put(POST.class, new POSTAnnotationHandler());
        handlers.put(Produces.class, new ProducesAnnotationHandler());
        handlers.put(PUT.class, new PUTAnnotationHandler());
        handlers.put(QueryParam.class, new QueryParamAnnotationHandler());
        handlers.put(DefaultValue.class, new DefaultValueAnnotationHandler());
        return handlers;
    }
}
