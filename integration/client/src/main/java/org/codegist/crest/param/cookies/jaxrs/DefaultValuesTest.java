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

package org.codegist.crest.param.cookies.jaxrs;

import org.codegist.crest.annotate.EndPoint;
import org.codegist.crest.param.cookies.common.IDefaultValuesTest;

import javax.ws.rs.CookieParam;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * @author laurent.gilles@codegist.org
 */
public class DefaultValuesTest extends IDefaultValuesTest<DefaultValuesTest.DefaultValues> {

    public DefaultValuesTest(CRestHolder crest) {
        super(crest, DefaultValues.class);
    }

    @EndPoint("{crest.server.end-point}")
    @Path("params/cookie/default-value")
    public static interface DefaultValues extends IDefaultValuesTest.IDefaultValues {

        @GET
        @Path("value")
        String value(
                @CookieParam("p1") @DefaultValue("default-p1") String p1,
                @CookieParam("p2") @DefaultValue("123") Integer p2);

        @GET
        @org.codegist.crest.annotate.CookieParam(value = "p2", defaultValue = "p2-val")
        @org.codegist.crest.annotate.CookieParams({
                @org.codegist.crest.annotate.CookieParam(value = "p1", defaultValue = "p1-val"),
                @org.codegist.crest.annotate.CookieParam(value = "p3", defaultValue = "p3-val")
        })
        @Path("param")
        String param(@CookieParam("p1") String p1);

    }

}
