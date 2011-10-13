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

package org.codegist.crest.param.forms.crest;

import org.codegist.crest.annotate.*;
import org.codegist.crest.param.forms.common.IDefaultValuesTest;

/**
 * @author laurent.gilles@codegist.org
 */
public class DefaultValuesTest extends IDefaultValuesTest<DefaultValuesTest.DefaultValues> {

    public DefaultValuesTest(CRestHolder crest) {
        super(crest, DefaultValues.class);
    }

    @EndPoint("{crest.server.end-point}")
    @Path("params/form/default-value")
    @POST
    @FormParam(value = "p02", defaultValue = "p02-val")
    @FormParams({
            @FormParam(value = "p01", defaultValue = "p01-val"),
            @FormParam(value = "p03", defaultValue = "p03-val")
    })
    public static interface DefaultValues extends IDefaultValuesTest.IDefaultValues {

        @Path("value")
        String value(
                @FormParam(value = "p1", defaultValue = "default-p1") String p1,
                @FormParam(value = "p2", defaultValue = "123") Integer p2);

        @FormParam(value = "p2", defaultValue = "p2-val")
        @FormParams({
                @FormParam(value = "p1", defaultValue = "p1-val"),
                @FormParam(value = "p3", defaultValue = "p3-val")
        })
        @Path("param")
        String param(@FormParam("p1") String p1);

    }
}
