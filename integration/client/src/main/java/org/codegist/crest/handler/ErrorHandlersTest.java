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
import org.codegist.crest.io.Request;
import org.codegist.crest.io.RequestException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author laurent.gilles@codegist.org
 */
public class ErrorHandlersTest extends BaseCRestTest<ErrorHandlersTest.ErrorHandlers> {

    public ErrorHandlersTest(CRestHolder holder) {
        super(holder, ErrorHandlers.class);
    }

    @Parameterized.Parameters
    public static Collection<CRestHolder[]> getData() {
        return crest(byRestServices());
    }

    @Test
    public void testHandleError(){
        assertEquals("Hello!", toTest.handleError());
    }

    @Test
    public void testHttpCode400() {
        try {
            toTest.httpCode(400);
            fail();
        }catch(CRestException e){
            e.printStackTrace();
        }
    }
    @Test
    public void testHttpCode500() {
        try {
            toTest.httpCode(500);
            fail();
        }catch(CRestException e){
            e.printStackTrace();
        }
    }

    @Test
    public void testHttpCode200() {
        assertEquals("ok", toTest.httpCode(200));
    }


    @EndPoint("{crest.server.end-point}")
    @Path("handler/error")
    @GET
    @ErrorHandler(ErrorDelegatorHandler.class) // just for coverage
    public static interface ErrorHandlers {

        @ErrorHandler(MyErrorHandler.class)
        @Path("error-handler")
        String handleError();

        @Path("http-code")
        String httpCode(@QueryParam("code") int httpCode);



    }

    public static class MyErrorHandler implements org.codegist.crest.handler.ErrorHandler {
        public <T> T handle(Request request, Exception e) throws Exception {
            return (T) "Hello!";
        }
    }
}
