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

package org.codegist.crest.param.paths.jaxrs;

import org.codegist.crest.annotate.EndPoint;
import org.codegist.crest.annotate.PathParams;
import org.codegist.crest.param.paths.common.IDefaultValuesTest;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 * @author laurent.gilles@codegist.org
 */
public class DefaultValuesTest extends IDefaultValuesTest<DefaultValuesTest.DefaultValues> {

    public DefaultValuesTest(CRestHolder crest) {
        super(crest, DefaultValues.class);
    }

    @EndPoint("{crest.server.end-point}")
    @Path("params/path/default-value")
    public static interface DefaultValues extends IDefaultValuesTest.IDefaultValues {

        @GET
        @Path("value/{p1}/{p2}")
        String value(
                @PathParam(value = "p1") @DefaultValue("default-p1") String p1,
                @PathParam(value = "p2") @DefaultValue("123") Integer p2);

        @GET
        @org.codegist.crest.annotate.PathParam(value = "p2", defaultValue = "p2-val")
        @PathParams({
                @org.codegist.crest.annotate.PathParam(value = "p3", defaultValue = "p3-val")
        })
        @Path("param/{p1}/{p2}/{p3}")
        String param(@PathParam("p1") String p1);

    }
}
