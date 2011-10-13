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

import org.codegist.common.reflect.Classes;
import org.codegist.crest.config.annotate.AnnotationHandler;

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
    
    public static boolean isJaxRsAware(){
        return Classes.isClassKnown("javax.ws.rs.GET", JaxRsAnnotations.class.getClassLoader());
    }
    
    public static Map<Class<? extends Annotation>, Class<? extends AnnotationHandler>> getMapping() {
        Map<Class<? extends Annotation>, Class<? extends AnnotationHandler>> handlers = new HashMap<Class<? extends Annotation>, Class<? extends AnnotationHandler>>();
        handlers.put(javax.ws.rs.Consumes.class, ConsumesAnnotationHandler.class);
        handlers.put(javax.ws.rs.CookieParam.class, CookieParamAnnotationHandler.class);
        handlers.put(javax.ws.rs.DELETE.class, DELETEAnnotationHandler.class);
        handlers.put(javax.ws.rs.Encoded.class, EncodedAnnotationHandler.class);
        handlers.put(javax.ws.rs.FormParam.class, FormParamAnnotationHandler.class);
        handlers.put(javax.ws.rs.GET.class, GETAnnotationHandler.class);
        handlers.put(javax.ws.rs.HEAD.class, HEADAnnotationHandler.class);
        handlers.put(javax.ws.rs.HeaderParam.class, HeaderParamAnnotationHandler.class);
        handlers.put(javax.ws.rs.MatrixParam.class, MatrixParamAnnotationHandler.class);
        handlers.put(javax.ws.rs.OPTIONS.class, OPTIONSAnnotationHandler.class);
        handlers.put(javax.ws.rs.Path.class, PathAnnotationHandler.class);
        handlers.put(javax.ws.rs.PathParam.class, PathParamAnnotationHandler.class);
        handlers.put(javax.ws.rs.POST.class, POSTAnnotationHandler.class);
        handlers.put(javax.ws.rs.Produces.class, ProducesAnnotationHandler.class);
        handlers.put(javax.ws.rs.PUT.class, PUTAnnotationHandler.class);
        handlers.put(javax.ws.rs.QueryParam.class, QueryParamAnnotationHandler.class);
        handlers.put(javax.ws.rs.DefaultValue.class, DefaultValueAnnotationHandler.class);
        return Collections.unmodifiableMap(handlers);
    }
}
