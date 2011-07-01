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
import org.codegist.crest.param.cookies.common.IEncodingsTest;

import javax.ws.rs.CookieParam;
import javax.ws.rs.Encoded;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.Collection;

/**
 * @author laurent.gilles@codegist.org
 */
public class EncodingsTest extends IEncodingsTest<EncodingsTest.Encodings> {

    public EncodingsTest(CRestHolder crest) {
        super(crest, Encodings.class);
    }
    
    @EndPoint("{crest.server.end-point}")
    @Path("params/cookie/encoding")
    public static interface Encodings extends IEncodingsTest.IEncodings {

        @GET
        @Path("default")
        String defaults(
                @CookieParam("p1") String p1,
                @CookieParam("p2") Collection<String> p2);

        @GET
        @Path("encoded")
        @Encoded
        String encoded(
                @CookieParam("p1") String p1,
                @CookieParam("p2") Collection<String> p2);

    }

}
