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

import org.codegist.crest.config.InterfaceConfigBuilder;
import org.codegist.crest.config.MethodConfigBuilder;
import org.codegist.crest.config.ParamConfigBuilder;

import java.lang.annotation.Annotation;

/**
 * <b>CRest</b>'s Annotation Handler interface.
 * <p>All <b>CRest</b>'s specific annotation in {@link org.codegist.crest.annotate} and Jax-rs's annotation in {@link javax.ws.rs} are handled via implementations of this interface</p>
 * <p>Note that annotation handling is a process that occures only once per REST interface, during build time (see {@link org.codegist.crest.CRest#build(Class)}). The handlers won't be invoked at run-time during REST proxies calls.</p>
 * @author laurent.gilles@codegist.org
 * @see org.codegist.crest.CRestBuilder#bindAnnotationHandler(Class, Class)
 */
public interface AnnotationHandler<A extends Annotation> {

    void handleInterfaceAnnotation(A annotation, InterfaceConfigBuilder builder) throws Exception;

    void handleMethodAnnotation(A annotation, MethodConfigBuilder builder) throws Exception;

    void handleParameterAnnotation(A annotation, ParamConfigBuilder builder) throws Exception;

}
