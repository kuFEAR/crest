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

package org.codegist.crest.server;

import org.codegist.crest.CRestAllSuite;
import org.codegist.crest.deserialization.DeserializationsCRestSuite;
import org.codegist.crest.param.forms.FormsCRestSuite;
import org.codegist.crest.param.forms.FormsJaxRsSuite;
import org.codegist.crest.param.headers.crest.SpecialParamsTest;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.io.IOException;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
@RunWith(Suite.class)
@Suite.SuiteClasses(CRestAllSuite.class)
public class CRestSuiteTest {
    private static int PORT = 8080;
//    static {  for some reason this works when ran within an IDE, but not within maven...
//        try {
//            PORT = Sockets.getFreePort();
//            System.setProperty("crest.server.end-point", "http://localhost:" + PORT);
//        } catch (IOException e) {
//            throw new ExceptionInInitializerError(e);
//        }
//    }

    private static CRestServer SERVER;

    @BeforeClass
    public synchronized static void setUp() throws IOException, InstantiationException, IllegalAccessException {
        if(SERVER != null) return;
        SERVER = new CRestServer("http://localhost:" + PORT);
        SERVER.stopOnExit();
    }

}
