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

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <p>Disables automatic encoding of parameter values bound using {@link QueryParam}, {@link PathParam}, {@link FormParam} or {@link MatrixParam}.</p>
 * <p>Using this annotation on a method will disable decoding for all parameters. Using this annotation on a class will disable decoding for all parameters of all methods.</p>
 * @author laurent.gilles@codegist.org
 */
@Target(value={PARAMETER,METHOD,FIELD,CONSTRUCTOR,TYPE})
@Retention(value=RUNTIME)
public @interface Encoded {

}
