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

package org.codegist.crest.handler;

import org.codegist.crest.BaseCRestTest;
import org.codegist.crest.CRestException;
import org.codegist.crest.annotate.*;
import org.codegist.crest.annotate.ErrorHandler;
import org.codegist.crest.annotate.RetryHandler;
import org.codegist.crest.io.RequestException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author laurent.gilles@codegist.org
 */
public class RetryHandlersTest extends BaseCRestTest<RetryHandlersTest.RetryHandlers>{

    public RetryHandlersTest(CRestHolder holder) {
        super(holder, RetryHandlers.class);
    }

    @Parameterized.Parameters
    public static Collection<CRestHolder[]> getData() {
        return crest(byRestServicesRetrying(3));
    }

    @Before
    public void setup(){
        toTest.reset();
        MyRetryHandler.hit = false;
    }

    @Test
    public void testFailAndRetryAndSuccess() {
        toTest.failFor(2);
        String value = toTest.retry("value");

        assertEquals("value", value);
        assertEquals(2, toTest.callCount());
    }

    @Test(expected = CRestException.class)
    public void testFailAndRetryAndFail() throws Exception {
        toTest.failFor(4);
        try {
            toTest.retry("value");
        }catch(Exception e){
            e.printStackTrace();
            assertEquals(3, toTest.callCount());
            throw e;
        }
    }

    @Test(expected = CRestException.class)
    public void testRetryCustom() throws Exception {
        try {
            toTest.retryCustom();
        }catch(Exception e){
            assertEquals(1, toTest.callCount());
            assertTrue(MyRetryHandler.hit);
            throw e;
        }
    }

    @EndPoint("{crest.server.end-point}")
    @Path("handler/retry")
    @GET
    @org.codegist.crest.annotate.RetryHandler(MaxAttemptRetryHandler.class) // just for coverage
    public interface RetryHandlers{

        @Path("call-count")
        int callCount();

        @Path("reset")
        void reset();

        @Path("fail-for")
        void failFor(@QueryParam("time") int times);

        @Path("retry")
        String retry(@QueryParam("value") String value);

        @RetryHandler(MyRetryHandler.class)
        @Path("retry-custom")
        String retryCustom();
    }


    public static class MyRetryHandler implements org.codegist.crest.handler.RetryHandler {
        static boolean hit = false;
        public boolean retry(RequestException exception, int retryNumber) {
            hit = true;
            return false;
        }
    }
}
