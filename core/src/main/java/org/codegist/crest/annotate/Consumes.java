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

package org.codegist.crest.annotate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Defines the media types that the method will handle. Also used to set the request's Accept header.</p>
 * <p>Annotating a method with it will make it ignore the server's response Content-Type during the deserialization process, and force it to use a deserializer that can handle the given media types.</p>
 * <p>During the deserialization process, each entry will be tried until one succeed, the value must be known to <b>CRest</b>,see {@link org.codegist.crest.CRestBuilder#bindDeserializer(Class, String...)}</p>
 * <p>Can contain placeholders, see {@link org.codegist.crest.CRestBuilder#placeholder(String, String)}.</p>
 * <p>When set at interface level, it will applies to all methods where it is not already specified</p>
 * @author laurent.gilles@codegist.org
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD})
public @interface Consumes {

    /**
     * A list of media types.
     */
    String[] value();

}
