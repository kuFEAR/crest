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

package org.codegist.crest.param.cookies.crest;

import org.codegist.crest.annotate.CookieParam;
import org.codegist.crest.annotate.EndPoint;
import org.codegist.crest.annotate.GET;
import org.codegist.crest.annotate.Path;
import org.codegist.crest.param.cookies.common.IBasicsTest;

/**
 * @author laurent.gilles@codegist.org
 */
public class BasicsTest extends IBasicsTest<BasicsTest.Basics> {

    public BasicsTest(CRestHolder crest) {
        super(crest, Basics.class);
    }

    @EndPoint("{crest.server.end-point}")
    @Path("params/cookie/basic")
    @GET
    public static interface Basics extends IBasicsTest.IBasics {

        String send();

        String send(
                @CookieParam("p1") String p1,
                @CookieParam("p2") int p2);

    }

}


