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

package org.codegist.crest.param;

import org.codegist.crest.param.cookies.CookiesCRestSuite;
import org.codegist.crest.param.cookies.CookiesJaxRsSuite;
import org.codegist.crest.param.forms.FormsEntitySuite;
import org.codegist.crest.param.headers.HeadersCRestSuite;
import org.codegist.crest.param.headers.HeadersJaxRsSuite;
import org.codegist.crest.param.matrixes.MatrixesCRestSuite;
import org.codegist.crest.param.matrixes.MatrixesJaxRsSuite;
import org.codegist.crest.param.multiparts.MultiPartsSuite;
import org.codegist.crest.param.paths.PathsCRestSuite;
import org.codegist.crest.param.paths.PathsJaxRsSuite;
import org.codegist.crest.param.queries.QueriesCRestSuite;
import org.codegist.crest.param.queries.QueriesJaxRsSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author laurent.gilles@codegist.org
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CookiesCRestSuite.class,
        HeadersCRestSuite.class,
        MatrixesCRestSuite.class,
        PathsCRestSuite.class,
        QueriesCRestSuite.class
})
public class ParamsCRestSuite {
}
