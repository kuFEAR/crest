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

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * @author laurent.gilles@codegist.org
 */
public class DefaultAnnotationHandlers implements AnnotationHandlers {

    public final Map<Class<? extends Annotation>, AnnotationHandler<?>> handlers;
    private static final AnnotationHandler NOOP = new NoOpAnnotationHandler();

    public DefaultAnnotationHandlers(Map<Class<? extends Annotation>, AnnotationHandler<?>> handlers) {
        this.handlers = handlers;
    }

    public <T extends Annotation> AnnotationHandler<T> handlerFor(T annotation) {
        AnnotationHandler handler = handlers.get(annotation.annotationType());
        return handler != null ? handler : NOOP;
    }
}
