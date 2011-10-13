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
 * <p>Defines the media type that the method will produce, sets the request's Content-Type header, overriding the default behavior. Default behavior will set it to application/x-www-form-urlencoded for POST/PUT request, and to multipart/form-data for multipart requests</p>
 * <p>Can contain placeholders, see {@link org.codegist.crest.CRestBuilder#placeholder(String, String)}.</p>
 * <p>When set at interface level, it will applies to all methods where it is not already specified</p>
 * @author laurent.gilles@codegist.org
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Produces {

    /**
     * Media type to set the request's Content-Type to
     */
    String value();

}