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

package org.codegist.crest.resources;

import org.codegist.crest.CRest;
import org.codegist.crest.CRestBuilder;
import org.codegist.crest.CRestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class BaseCRestTest {

    protected static final String UTF8_VALUE = "123@#?&£{}abc";
    protected static final String ISO_VALUE = "123@#?&{}abc";

    protected final CRest crest;

    public BaseCRestTest(CRest crest) {
        this.crest = crest;
    }

    public static CRestBuilder baseBuilder(){
        return new CRestBuilder()
            .bindPlainTextDeserializerWith("text/html");
    }

    public static CRest[] byRestServices(){
        return  new CRest[]{
            /* HttpURLConnection based CRest */
            baseBuilder().build(),
            /* Apache HttpClient based CRest */
            baseBuilder()
                    .useHttpClientRestService()
            .build(),
        };
    }

    public static CRest[] bySerializers(){
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

    public static Collection<CRest[]> data(CRest[]... crests) {
        Collection<CRest[]> data = new ArrayList<CRest[]>();
        for(CRest[] ccrests : crests){
            for(CRest crest : ccrests){
                data.add(new CRest[]{crest});
            }
        }
        return data;
    }
}
