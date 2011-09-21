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

package org.codegist.crest.annotate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Binds a method parameter to be used as a request multipart parameter. Note that as soon as a MultiPartParam is detected in a method's parameters set, all other FormParam will be treated as MultiPartParam.</p>
 * <p>The type of the parameter must either:</p>
 * <ol>
 * <li>Be a primitive</li>
 * <li>Have a toString() method that returns the value to be used</li>
 * <li>Any type being handled by <b>CRest</b>, see available {@link org.codegist.crest.serializer.Serializer} implementations for a list of supported types.</li>
 * <li>Any user specific type given that a {@link org.codegist.crest.serializer.Serializer} has been provided for it.</li>
 * <li>Be a Collection&lt;T&gt;, or an array T[] where T satisfies 2, 3 or 4 above.</li>
 * </ol>
 * <p>Note that for array/Collection, the default behavior will be to create as many pair/value parameters as values in the array/collection. Values can be merged in one single parameter using the {@link org.codegist.crest.annotate.ListSeparator} annotation</p>
 * <p>Can contain placeholders, see {@link org.codegist.crest.CRestBuilder#placeholder(String, String)}.</p>
 * <p>When set at interface or method levels, it will add a multipart parameter with the given value for all method's to which it applies</p>
 * @see org.codegist.crest.annotate.Serializer
 * @see org.codegist.crest.annotate.ListSeparator
 * @author laurent.gilles@codegist.org
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD,ElementType.PARAMETER})
public @interface MultiPartParam {

    /**
     * Defines the name of the request multipart parameter that will hold the given value
     */
    String value();

    /**
     * Defines the default value of the request multipart parameter if the value being passed is null
     */
    String defaultValue() default "";

    /**
     * Defines the multipart Content-Type to use. Default is application/octet-stream for java.io.InputStream and java.io.File parameter types, and plain/text for everything else.
     */
    String contentType() default "";

    /**
     * Defines the multipart file name to use. Not defined by default, unless the parameter type is a java.io.File, in which case the file name will be used
     */
    String fileName() default "";
}
