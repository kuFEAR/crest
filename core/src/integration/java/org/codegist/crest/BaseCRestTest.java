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

import org.codegist.common.net.Urls;
import org.codegist.crest.server.Server;
import org.codegist.crest.server.stubs.request.*;
import org.codehaus.jackson.map.ObjectMapper;
import org.custommonkey.xmlunit.XMLAssert;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@RunWith(Parameterized.class)
public class BaseCRestTest<T> {

    public static final String DATE_FORMAT = "dd/MM/yyyy @ HH:mm:ssZ";
    public static final String ENCODING = "UTF-8";

    protected final T toTest;

    public BaseCRestTest(CRest crest, Class<T> service) {
        this.toTest = crest.build(service);
    }

    public static String formatDate(Date date){
        return new SimpleDateFormat(DATE_FORMAT).format(date);
    }
    public static Date date(String date, String format){
        try {
            return new SimpleDateFormat(format).parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    public static String url(String value) throws UnsupportedEncodingException {
        return Urls.encode(value, ENCODING);
    }
    public static String json(String value) {

        try {
            return new ObjectMapper().writeValueAsString(value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String xml(String value) {
        return value.replace("&", "&amp;");
    }

    public static void assertXMLEqual(String control, String actual){
        try {
            XMLAssert.assertXMLEqual(control, actual);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static final String ADDRESS = "http://localhost:8080";

    private static final Server server = Server.create(ADDRESS,
            new org.codegist.crest.server.stubs.params.queries.BasicsStub(),
            new org.codegist.crest.server.stubs.params.queries.CollectionsStub(),
            new org.codegist.crest.server.stubs.params.queries.DatesStub(),
            new org.codegist.crest.server.stubs.params.queries.DefaultValuesStub(),
            new org.codegist.crest.server.stubs.params.queries.EncodingsStub(),
            new org.codegist.crest.server.stubs.params.queries.NullsStub(),
            new org.codegist.crest.server.stubs.params.queries.SerializersStub(),

            new org.codegist.crest.server.stubs.params.matrixes.BasicsStub(),
            new org.codegist.crest.server.stubs.params.matrixes.CollectionsStub(),
            new org.codegist.crest.server.stubs.params.matrixes.DatesStub(),
            new org.codegist.crest.server.stubs.params.matrixes.DefaultValuesStub(),
            new org.codegist.crest.server.stubs.params.matrixes.EncodingsStub(),
            new org.codegist.crest.server.stubs.params.matrixes.NullsStub(),
            new org.codegist.crest.server.stubs.params.matrixes.SerializersStub(),

            new org.codegist.crest.server.stubs.params.forms.BasicsStub(),
            new org.codegist.crest.server.stubs.params.forms.CollectionsStub(),
            new org.codegist.crest.server.stubs.params.forms.DatesStub(),
            new org.codegist.crest.server.stubs.params.forms.DefaultValuesStub(),
            new org.codegist.crest.server.stubs.params.forms.EncodingsStub(),
            new org.codegist.crest.server.stubs.params.forms.NullsStub(),
            new org.codegist.crest.server.stubs.params.forms.SerializersStub(),

            new org.codegist.crest.server.stubs.params.headers.BasicsStub(),
            new org.codegist.crest.server.stubs.params.headers.CollectionsStub(),
            new org.codegist.crest.server.stubs.params.headers.DatesStub(),
            new org.codegist.crest.server.stubs.params.headers.DefaultValuesStub(),
            new org.codegist.crest.server.stubs.params.headers.EncodingsStub(),
            new org.codegist.crest.server.stubs.params.headers.NullsStub(),
            new org.codegist.crest.server.stubs.params.headers.SerializersStub(),

            new org.codegist.crest.server.stubs.params.multiparts.BasicsStub(),
            new org.codegist.crest.server.stubs.params.multiparts.CollectionsStub(),
            new org.codegist.crest.server.stubs.params.multiparts.DatesStub(),
            new org.codegist.crest.server.stubs.params.multiparts.DefaultValuesStub(),
            new org.codegist.crest.server.stubs.params.multiparts.EncodingsStub(),
            new org.codegist.crest.server.stubs.params.multiparts.NullsStub(),
            new org.codegist.crest.server.stubs.params.multiparts.SerializersStub(),
            new org.codegist.crest.server.stubs.params.multiparts.MiscsStub(),

            new org.codegist.crest.server.stubs.params.forms.json.FormJsonEntitiesStub(),
            new org.codegist.crest.server.stubs.params.forms.xml.FormXmlEntitiesStub(),

            new org.codegist.crest.server.stubs.params.paths.BasicsStub(),
            new org.codegist.crest.server.stubs.params.paths.CollectionsStub(),
            new org.codegist.crest.server.stubs.params.paths.DatesStub(),
            new org.codegist.crest.server.stubs.params.paths.DefaultValuesStub(),
            new org.codegist.crest.server.stubs.params.paths.EncodingsStub(),
            new org.codegist.crest.server.stubs.params.paths.SerializersStub(),

            new org.codegist.crest.server.stubs.params.cookies.BasicsStub(),
            new org.codegist.crest.server.stubs.params.cookies.CollectionsStub(),
            new org.codegist.crest.server.stubs.params.cookies.DatesStub(),
            new org.codegist.crest.server.stubs.params.cookies.DefaultValuesStub(),
            new org.codegist.crest.server.stubs.params.cookies.EncodingsStub(),
            new org.codegist.crest.server.stubs.params.cookies.NullsStub(),
            new org.codegist.crest.server.stubs.params.cookies.SerializersStub(),

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
            .setDateFormat(DATE_FORMAT)
            .setBoolean("myTrue", "myFalse")
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

    public static CRest[] byJsonSerializersAndRestServices(){
        return byRestServices();
    }
    public static CRest[] byXmlSerializersAndRestServices(){
        return new CRest[]{
           /* Jaxb Serialization based CRest */
            baseBuilder()
                    .serializeXmlWithJaxb()
            .build(),
            /* SimpleXml Serialization based CRest */
            baseBuilder()
                    .serializeXmlWithSimpleXml()
            .build(),
           /* Jaxb Serialization based CRest */
            baseBuilder()
                    .useHttpClientRestService()
                    .serializeXmlWithJaxb()
            .build(),
            /* SimpleXml Serialization based CRest */
            baseBuilder()
                    .useHttpClientRestService()
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
