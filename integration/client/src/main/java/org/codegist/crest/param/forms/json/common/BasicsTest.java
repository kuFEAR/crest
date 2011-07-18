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

package org.codegist.crest.param.forms.json.common;

import org.codegist.crest.annotate.*;
import org.codegist.crest.entity.JsonEntityWriter;
import org.codegist.crest.param.common.IBasicsTest;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static org.junit.Assert.assertEquals;

/**
 * @author laurent.gilles@codegist.org
 */
public class BasicsTest extends IBasicsTest<BasicsTest.Basics> {

    public BasicsTest(CRestHolder crest) {
        super(crest, Basics.class);
    }

    @Parameterized.Parameters
    public static Collection<CRestHolder[]> getData() {
        return crest(byJsonSerializersAndRestServices());
    }

    @EndPoint("{crest.server.end-point}")
    @Path("params/form/json")
    @POST
    @EntityWriter(JsonEntityWriter.class)
    public static interface Basics extends IBasicsTest.IBasics {

        String send();

        String send(
                @FormParam("p1") String p1,
                @FormParam("p2") int p2);

    }

    @Override
    public void assertSend(String actual) {
        assertEquals("{}", actual);
    }

    @Override
    public void assertSendWithParams(String p1, int p2, String actual) {
        StringBuilder expected = new StringBuilder();
        expected.append("{");
        expected.append("\"p1\":\"").append(p1).append("\",");
        expected.append("\"p2\":").append(p2);
        expected.append("}");
        assertEquals(expected.toString(), actual);
    }
}
