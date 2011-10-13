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

import java.lang.annotation.*;

/**
 * <p>Purely documentation annotation, not look-up by <b>CRest</b>.</p>
 * <p>Any inteface in the <b>CRest</b> API annotated with it means its implementations will be auto-instantiated on demand by <b>CRest</b></p>
 * <p><b>CRest</b>'s state is passed through {@link org.codegist.crest.CRestConfig} and component can be CRest state aware by defining a one-arg constructor that takes a {@link org.codegist.crest.CRestConfig} instance.</p>
 * <p>When <b>CRest</b> will instantiate the component, the {@link org.codegist.crest.CRestConfig} will be passed to it.</p>
 * @see org.codegist.crest.util.ComponentFactory
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE})
public @interface CRestComponent {
}
