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

package org.codegist.crest;

import org.codegist.crest.resources.*;
import org.codegist.crest.server.Server;
import org.codegist.crest.server.stubs.*;
import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses( {
        GetsTest.class ,
        PostsTest.class,
        PutsTest.class,
        DeletesTest.class,
        OptionsTest.class,
        HeadsTest.class,
        EntityWritersTest.class })
public class CRestSuite {

    public static final String ADDRESS = "http://localhost:8080";

    private static final Server server = Server.create(ADDRESS,
            new GetsStub(),
            new PostsStub(),
            new PutsStub(),
            new DeletesStub(),
            new OptionsStub(),
            new HeadsStub(),
            new EntityWritersStub()
    );


    @AfterClass
    public static void setup(){
        server.stop();
    }


   // public static void main(String[] args) { }

    

}
