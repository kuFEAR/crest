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

package org.codegist.crest.interceptor;

import org.codegist.crest.BaseCRestTest;
import org.codegist.crest.CRestBuilder;
import org.codegist.crest.CRestException;
import org.codegist.crest.annotate.EndPoint;
import org.codegist.crest.annotate.GET;
import org.codegist.crest.annotate.Path;
import org.codegist.crest.annotate.RequestInterceptor;
import org.codegist.crest.io.Request;
import org.junit.Test;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static org.junit.Assert.*;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class InterceptorsTest extends BaseCRestTest<InterceptorsTest.CustomInterceptors> {

    public InterceptorsTest(CRestHolder holder) {
        super(holder, CustomInterceptors.class);
    }

    @Parameterized.Parameters
    public static Collection<CRestHolder[]> getData() {
        return crest(arrify(forEachBaseBuilder(new Builder() {
            public CRestHolder build(CRestBuilder builder) {
                return new CRestHolder(builder.build());
            }
        })));
    }

    @Test
    public void testOverriden(){
        Interceptor1.reset();
        Interceptor2.reset();
        try {
            toTest.overriden();
            fail("should have failed, no server end point!");
        }catch(CRestException e){
            assertFalse(Interceptor1.hit);
            assertTrue(Interceptor2.hit);
        }
    }

    @Test
    public void testDefault(){
        Interceptor1.reset();
        Interceptor2.reset();
        try {
            toTest.def();
            fail("should have failed, no server end point!");
        }catch(CRestException e){
            assertTrue(Interceptor1.hit);
            assertFalse(Interceptor2.hit);
        }
    }


    @EndPoint("{crest.server.end-point}")
    @Path("interceptor/custom")
    @GET
    @RequestInterceptor(Interceptor1.class)
    public interface CustomInterceptors {

        @Path("overriden")
        @RequestInterceptor(Interceptor2.class)
        void overriden();

        @Path("default")
        void def();
    }

    public static class Interceptor1 implements org.codegist.crest.interceptor.RequestInterceptor {

        static boolean hit = false;

        public void beforeFire(Request request) throws Exception {
            hit = true;
        }
        static void reset(){
            hit = false;
        }
    }
    public static class Interceptor2 implements org.codegist.crest.interceptor.RequestInterceptor {

        static boolean hit = false;

        public void beforeFire(Request request) throws Exception {
            hit = true;
        }
        static void reset(){
            hit = false;
        }
    }
}
