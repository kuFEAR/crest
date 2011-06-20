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

package org.codegist.crest.server.stubs.params.matrixes;

import javax.ws.rs.*;
import java.util.ArrayList;
import java.util.List;

import static org.codegist.common.net.Urls.decode;
import static org.codegist.crest.server.utils.ToStrings.string;

/**
 * @author laurent.gilles@codegist.org
 */
@Produces("text/html;charset=UTF-8")
@Path("params/matrix/encoding")
public class EncodingsStub {

    @GET
    @Path("default")
    public String defaults(
            @MatrixParam("p1") @Encoded String p1,
            @MatrixParam("p2") @Encoded List<String> p2) {
        List<String> p2Decoded = new ArrayList<String>();
        for (String p2p : p2) {
            p2Decoded.add(decode(p2p, "utf-8"));
        }
        return String.format("default() p1=%s p2=%s", decode(p1, "utf-8"), string(p2Decoded));
    }

    @GET
    @Path("encoded")
    public String preEncoded(
            @MatrixParam("p1") @Encoded String p1,
            @MatrixParam("p2") @Encoded List<String> p2) {
        List<String> p2Decoded = new ArrayList<String>();
        for (String p2p : p2) {
            p2Decoded.add(decode(p2p, "utf-8"));
        }
        return String.format("encoded() p1=%s p2=%s", decode(p1, "utf-8"), string(p2Decoded));
    }

}
