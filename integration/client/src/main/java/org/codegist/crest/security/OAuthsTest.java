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

package org.codegist.crest.security;

import org.codegist.crest.BaseCRestTest;
import org.codegist.crest.CRestException;
import org.codegist.crest.annotate.*;
import org.codegist.crest.handler.MaxAttemptRetryHandler;
import org.codegist.crest.io.RequestException;
import org.junit.Test;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static org.junit.Assert.*;

/**
 * @author laurent.gilles@codegist.org
 */
public class OAuthsTest extends BaseCRestTest<OAuthsTest.OAuths> {

    public OAuthsTest(CRestHolder holder) {
        super(holder, OAuths.class);
    }


    @Parameterized.Parameters
    public static Collection<CRestHolder[]> getData() {
        return crest(new CRestHolder[]{
                new CRestHolder(baseBuilder()
                .oauth("ConsumerKey",
                       "ConsumerSecret",
                       "ExpiredAccessToken",
                       "ExpiredAccessTokenSecret",
                       "SessionHandle",
                        TEST_SERVER + "/security/oauth/refresh")
                .build()
                )}
        );
    }

    @Test
    public void testAuthenticatedCallFailBecauseOfExpiredToken() throws Exception {
        try {
            toTest.authenticatedFail("p1-val");
            fail("should have failed");
        }catch(CRestException e){
            assertTrue(e.getCause() instanceof RequestException);
            assertEquals(401, RequestException.class.cast(e.getCause()).getResponse().getStatusCode());
        }
    }
    @Test
    public void testAuthenticatedCallSuccessAfterExpiredTokenRefresh(){
        assertEquals("authenticated() p1=p1-val refreshCalled=true", toTest.authenticatedSuccess("p1-val"));
    }

    @EndPoint("{crest.server.end-point}")
    @Path("security/oauth")
    @PUT
    public interface OAuths {

        @Path("authenticated")
        @RetryHandler(MaxAttemptRetryHandler.class) // overrides the RefreshAuthorizationRetryHandler defined by oauth.
        @GET
        String authenticatedFail(@QueryParam("p1") String p1);

        @Path("authenticated")
        @GET
        String authenticatedSuccess(@QueryParam("p1") String p1);

    }

}
