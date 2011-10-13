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

package org.codegist.crest;

import org.codegist.crest.annotate.AnnotatesSuite;
import org.codegist.crest.deserialization.DeserializationsCRestSuite;
import org.codegist.crest.deserialization.DeserializationsJaxRsSuite;
import org.codegist.crest.handler.HandlersSuite;
import org.codegist.crest.interceptor.InterceptorsSuite;
import org.codegist.crest.param.ParamsBaseSuite;
import org.codegist.crest.param.ParamsCRestSuite;
import org.codegist.crest.param.ParamsJaxRsSuite;
import org.codegist.crest.request.RequestsCRestSuite;
import org.codegist.crest.request.RequestsJaxRsSuite;
import org.codegist.crest.security.SecuritySuite;
import org.codegist.crest.timeout.TimeoutsSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({       
        AnnotatesSuite.class,
        DeserializationsCRestSuite.class,
        DeserializationsJaxRsSuite.class,
        HandlersSuite.class,
        InterceptorsSuite.class,
        ParamsBaseSuite.class,
        ParamsCRestSuite.class,
        ParamsJaxRsSuite.class,
        RequestsCRestSuite.class,
        RequestsJaxRsSuite.class,
        SecuritySuite.class,
        TimeoutsSuite.class
})
public class CRestAllSuite {
}
