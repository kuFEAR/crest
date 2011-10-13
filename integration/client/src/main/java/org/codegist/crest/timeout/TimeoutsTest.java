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

package org.codegist.crest.timeout;

import org.codegist.crest.BaseCRestTest;
import org.codegist.crest.CRestException;
import org.codegist.crest.annotate.*;
import org.junit.Test;
import org.junit.runners.Parameterized;

import java.net.SocketTimeoutException;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

/**
 * @author laurent.gilles@codegist.org
 */
public class TimeoutsTest extends BaseCRestTest<TimeoutsTest.Timeouts> {

    public TimeoutsTest(CRestHolder holder) {
        super(holder, Timeouts.class);
    }

    @Parameterized.Parameters
    public static Collection<CRestHolder[]> getData() {
        return crest(byRestServices());
    }

    @Test(expected= SocketTimeoutException.class)
    public void testSocketTimeout_Timeout() throws Throwable {
        try {
            toTest.socketTimeout(300);
        }catch(CRestException e){
            Thread.sleep(110); // sleep a bit so that the server stop to sleep and is available again
            throw e.getCause();
        }
    }

    @Test
    public void testSocketTimeout_NoTimeout() {
        assertEquals("ok", toTest.socketTimeout(0));
    }

    @Test
    public void testOverridenSocketTimeout_Timeout() throws Throwable {
        assertEquals("ok", toTest.overridenSocketTimeout(300));
    }

//    @Test(expected= IOException.class)
//    public void testConnectionTimeout_Timeout() throws Throwable {
//        new Thread(){
//            @Override
//            public void run() {
//                System.out.println("BUSY");
//                toTest.busy(50000);
//            }
//        }.start();
//        Thread.sleep(10);
//        try {
//            System.out.println("connectionTimeout");
//            toTest.connectionTimeout(0);
//        }catch(CRestException e){
//            System.out.println(e.getCause());
//            Thread.sleep(200);// sleep a bit so that the server stop to sleep and is available again
//            throw e.getCause();
//        }
//    }

    @Test
    public void testOverridenConnectionTimeout_Timeout() throws Throwable {
        assertEquals("ok", toTest.overridenConnectionTimeout(150));
    }

    @Test
    public void testConnetionTimeout_NoTimeout() {
        assertEquals("ok", toTest.connectionTimeout(0));
    }

    @EndPoint("{crest.server.end-point}")
    @Path("timeout")
    @ConnectionTimeout(100)
    @SocketTimeout(200)
    public interface Timeouts{

        @ConnectionTimeout(5000)
        @SocketTimeout(5000)
        @Path("make-busy")
        String busy(@QueryParam("millis") int millis);

        @ConnectionTimeout(5000)
        @Path("connection-timeout")
        String overridenConnectionTimeout(@QueryParam("timeout") int timeout);

        @SocketTimeout(5000)
        @Path("socket-timeout")
        String overridenSocketTimeout(@QueryParam("timeout") int timeout);

        @SocketTimeout(5000)
        @Path("connection-timeout")
        String connectionTimeout(@QueryParam("timeout") int timeout);

        @Path("socket-timeout")
        String socketTimeout(@QueryParam("timeout") int timeout);
    }
}
