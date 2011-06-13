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

import org.codegist.crest.server.Server;
import org.codegist.crest.server.stubs.params.*;
import org.codegist.crest.server.stubs.request.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Collection;

@RunWith(Parameterized.class)
public class BaseCRestTest<T> {

    protected static final String UTF8_VALUE = "123@#?&£{}abc";

    protected final T toTest;

    public BaseCRestTest(CRest crest, Class<T> service) {
        this.toTest = crest.build(service);
    }


    public static final String ADDRESS = "http://localhost:8080";

    private static final Server server = Server.create(ADDRESS,
            new FormsStub(),
            new PathsStub(),
            new QueriesStub(),
            new CookiesStub(),
            new MatrixesStub(),
            new HeadersStub(),
            new MultiPartsStub(),
            new FormXmlEntitiesStub(),
            new FormJsonEntitiesStub(),
            new GetsStub(),
            new PostsStub(),
            new PutsStub(),
            new DeletesStub(),
            new HeadsStub(),
            new OptionsStub()
    );

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                server.stop();
            }
        });
    }












    public static CRestBuilder baseBuilder(){
        return new CRestBuilder()
            .bindPlainTextDeserializerWith("text/html");
    }

    public static CRest[] byDefault(){
        return  new CRest[]{
                baseBuilder().build()
        };
    }

    public static CRest[] byRestServices(){
        return byRestServices(baseBuilder());
    }
    public static CRest[] byRestServices(CRestBuilder base){
        return  new CRest[]{
            /* HttpURLConnection based CRest */
            base
            .build(),
            /* Apache HttpClient based CRest */
            base
                    .useHttpClientRestService()
            .build(),
        };
    }

    public static CRest[] byXmlSerializers(){
        return new CRest[]{
           /* Jaxb Serialization based CRest */
            baseBuilder()
                    .serializeXmlWithJaxb()
            .build(),
            /* SimpleXml Serialization based CRest */
            baseBuilder()
                    .serializeXmlWithSimpleXml()
            .build()
        };
    }


    public static CRest[] byJsonSerializers(){
        return new CRest[]{baseBuilder().build()};
    }

    public static Collection<CRest[]> crest(CRest[]... crests) {
        Collection<CRest[]> data = new ArrayList<CRest[]>();
        for(CRest[] ccrests : crests){
            for(CRest crest : ccrests){
                data.add(new CRest[]{crest});
            }
        }
        return data;
    }
}
