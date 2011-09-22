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

import org.codegist.common.collect.Maps;
import org.codegist.common.net.Urls;
import org.codegist.crest.annotate.JsonEntity;
import org.codegist.crest.annotate.XmlEntity;
import org.codegist.crest.config.annotate.JsonEntityAnnotationHandler;
import org.codegist.crest.config.annotate.XmlEntityAnnotationHandler;
import org.codegist.crest.entity.JsonEntityWriter;
import org.codegist.crest.entity.XmlEntityWriter;
import org.codegist.crest.io.http.HttpClientHttpChannelFactory;
import org.codegist.crest.serializer.Serializer;
import org.codegist.crest.serializer.jackson.JsonEncodedFormJacksonSerializer;
import org.codegist.crest.serializer.jaxb.XmlEncodedFormJaxbSerializer;
import org.codegist.crest.serializer.simplexml.SimpleXmlDeserializer;
import org.codegist.crest.serializer.simplexml.XmlEncodedFormSimpleXmlSerializer;
import org.codegist.crest.util.ComponentRegistry;
import org.codegist.crest.util.model.BunchOfData;
import org.codegist.crest.util.model.Data;
import org.codegist.crest.util.model.SerializerTypes;
import org.codehaus.jackson.map.ObjectMapper;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.Collections.singletonMap;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public abstract class BaseCRestTest<T> {
    public static final String DATE_FORMAT = "dd/MM/yyyy @ HH:mm:ssZ";
    public static final Charset ENCODING = Charset.forName("UTF-8");

    public static final String TEST_SERVER = System.getProperty("crest.server.end-point", "http://localhost:8080") + "/crest-server";
    private static final boolean TEST_JAXB = Boolean.valueOf(System.getProperty("crest.test.jaxb", "true"));
    private static final String TEST_TMP_DIR = System.getProperty("crest.test.tmp-dir", null);





    private static final Map<String,Object> CREST_PROPERTIES = new HashMap<String, Object>(){{
        put(CRestConfig.CREST_DATE_FORMAT, DATE_FORMAT);
        put(CRestConfig.CREST_BOOLEAN_FALSE, "myFalse");
        put(CRestConfig.CREST_BOOLEAN_TRUE, "myTrue");
        put("crest.encoding", ENCODING.displayName());
        put("encoding.header", ENCODING.displayName());
        put(Serializer.class.getName(), SerializerTypes.JACKSON);
    }};

    // TODO this is to handle the fact we CANNOT override the way jaxb serialize types!
    public static final Map<String,Object> JAXB_SPECIFIC_PROPERTIES = new HashMap<String, Object>(){{
        put(CRestConfig.CREST_DATE_FORMAT, "yyyy-MM-dd'T'HH:mm:ss+00:00");
        put(CRestConfig.CREST_BOOLEAN_TRUE, "true");
        put(CRestConfig.CREST_BOOLEAN_FALSE, "false");
        put(Serializer.class.getName(), SerializerTypes.JAXB);
    }};
    public static final Map<String,Object> SIMPLEXML_SPECIFIC_PROPERTIES = new HashMap<String, Object>(){{
        put(Serializer.class.getName(), SerializerTypes.SIMPLEXML);
    }};

    // TODO for some reason HttpUrlConnection header encoding always encode in ISO-8859-1
    private static final Map<String,Object> HTTP_URL_CONNECTION = new HashMap<String, Object>(){{
        put("encoding.header", System.getProperty("crest.test.http-url-connection.encoding.header", "ISO-8859-1"));
    }};

    protected final T toTest;
    private final CRestConfig crestConfig;



    public BaseCRestTest(CRestHolder holder, Class<T> service) {
        this.toTest = holder.crest.build(service);
        this.crestConfig = holder.crestConfig;
    }


    public String getEffectiveDateFormat(){
        return crestConfig.getDateFormat();
    }
    public String getEffectiveBooleanTrue(){
        return crestConfig.getBooleanTrue();
    }
    public String getEffectiveBooleanFalse(){
        return crestConfig.getBooleanFalse();
    }
    public File getTempDir(){
        return TEST_TMP_DIR != null ? new File(TEST_TMP_DIR) : null;
    }

    public String encodeHeader(String header) throws UnsupportedEncodingException {
        return new String(header.getBytes(ENCODING), (String) crestConfig.get("encoding.header"));
    }


    public <D> BunchOfData<D> newBunchOfData(Date val1, Boolean val2, D val3){
        return BunchOfData.create((SerializerTypes)crestConfig.get(Serializer.class), val1, val2, val3);
    }
    public Data newData(int val1, String val2){
        return Data.create((SerializerTypes)crestConfig.get(Serializer.class), val1, val2);
    }



    public String toString(Date date) {
        return new SimpleDateFormat(getEffectiveDateFormat()).format(date);
    }
    public String toString(boolean val){
        return val ? getEffectiveBooleanTrue() : getEffectiveBooleanFalse();
    }

    public static Date date(String date, String format) {
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

    public static void assertXmlEquals(String control, String actual) {
        try {
            XMLUnit.setIgnoreWhitespace(true);
            Diff diff = XMLUnit.compareXML(control, actual);
            assertTrue(diff.toString(), diff.similar());
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void assertMatches(String controlPattern, String actual) {
        assertTrue(actual.matches(controlPattern));
    }

    // this represent the real root builder shared by all tests
    protected static CRestBuilder baseBuilder() {
        CRestBuilder builder = CRest
                    .placeholder("crest.server.end-point", TEST_SERVER)
                    .setConcurrencyLevel(2)
                    .bindAnnotationHandler(JsonEntityAnnotationHandler.class, JsonEntity.class)
                    .bindAnnotationHandler(XmlEntityAnnotationHandler.class, XmlEntity.class)
                    .property(ComponentRegistry.class.getName() + "#serializers-per-mime", new ComponentRegistry.Builder<String, Serializer>().register(JsonEncodedFormJacksonSerializer.class, JsonEntityWriter.MIME).build(new DefaultCRestConfig(CREST_PROPERTIES)))
                    .addProperties(CREST_PROPERTIES);
        if(!TEST_JAXB) {
            builder.deserializeXmlWith(SimpleXmlDeserializer.class);
        }

        return builder;
    }

    private static Map<String,Object> getEntitySerializerProperties(boolean jaxb){
        return getEntitySerializerProperties(jaxb, false);
    }
    private static Map<String,Object> getEntitySerializerProperties(boolean jaxb, boolean json){
        ComponentRegistry.Builder<String, Serializer> registry = new ComponentRegistry.Builder<String, Serializer>();

        if(jaxb) {
            registry.register(XmlEncodedFormJaxbSerializer.class, XmlEntityWriter.MIME);
        }else{
            registry.register(XmlEncodedFormSimpleXmlSerializer.class, XmlEntityWriter.MIME);    
        }
        if(json) {
            registry.register(JsonEncodedFormJacksonSerializer.class, JsonEntityWriter.MIME);
        }

        return singletonMap(ComponentRegistry.class.getName() + "#serializers-per-mime", (Object) registry.build(new DefaultCRestConfig(CREST_PROPERTIES)));
    }

    // these represents the common permutations all test will pass
    private static CRestBuilder[] baseBuilders() {
        return new CRestBuilder[] {
                baseBuilder(),
                baseBuilder().oauth("ConsumerKey","ConsumerSecret","AccessToken","AccessTokenSecret"),
                baseBuilder().basicAuth("My UserName", "My password")
        };
    }
    public static List<CRestHolder> forEach(CRestBuilder[] builders, Builder builder){
        List<CRestHolder> holders = new ArrayList<CRestHolder>();
        for(CRestBuilder b : builders){
            holders.add(builder.build(b));
        }
        return holders;
    }
    public static List<CRestHolder> forEachBaseBuilder(Builder builder){
        return forEach(baseBuilders(), builder);
    }
    public static CRestHolder[] arrify(List<CRestHolder> list){
        return list.toArray(new CRestHolder[list.size()]);
    }

    public static interface Builder {
        CRestHolder build(CRestBuilder builder);
    }

    public static List<CRestHolder> byDefault() {
        return forEachBaseBuilder(new Builder() {
            public CRestHolder build(CRestBuilder builder) {
                return new CRestHolder(builder.build());
            }
        });
    }

    public static CRestHolder[] byRestServices() {
        List<CRestHolder> holders = new ArrayList<CRestHolder>();
        holders.addAll(byDefault());
        holders.addAll(forEachBaseBuilder(new Builder() {
            public CRestHolder build(CRestBuilder builder) {
                return new CRestHolder(builder.setHttpChannelFactory(HttpClientHttpChannelFactory.class).build());
            }
        }));
        return arrify(holders);
    }


    public static CRestHolder[] byRestServicesRetrying(final int maxAttempts) {
        List<CRestHolder> holders = new ArrayList<CRestHolder>();
        holders.addAll(forEachBaseBuilder(new Builder() {
            public CRestHolder build(CRestBuilder builder) {
                return new CRestHolder(builder
                        .property(CRestConfig.CREST_MAX_ATTEMPTS, maxAttempts)
                        .build());
            }
        }));
        holders.addAll(forEachBaseBuilder(new Builder() {
            public CRestHolder build(CRestBuilder builder) {
                return new CRestHolder(builder
                        .property(CRestConfig.CREST_MAX_ATTEMPTS, maxAttempts)
                        .setHttpChannelFactory(HttpClientHttpChannelFactory.class)
                        .build());
            }
        }));
        return arrify(holders);
    }


    public static CRestHolder[] byRestServicesForHeaders() {
        List<CRestHolder> holders = new ArrayList<CRestHolder>();
        holders.addAll(forEachBaseBuilder(new Builder() {
            public CRestHolder build(CRestBuilder builder) {
                return new CRestHolder(builder.build(), HTTP_URL_CONNECTION);
            }
        }));
        holders.addAll(forEachBaseBuilder(new Builder() {
            public CRestHolder build(CRestBuilder builder) {
                return new CRestHolder(builder
                        .setHttpChannelFactory(HttpClientHttpChannelFactory.class)
                        .build());
            }
        }));
        return arrify(holders);
    }

    public static CRestHolder[] byRestServicesAndCustomContentTypes() {
        List<CRestHolder> holders = new ArrayList<CRestHolder>();
        holders.addAll(forEachBaseBuilder(new Builder() {
            public CRestHolder build(CRestBuilder builder) {
                return new CRestHolder(builder
                        .addProperties(getEntitySerializerProperties(TEST_JAXB, true))
                        .bindPlainTextDeserializerWith("text/html", "application/custom", "application/custom1", "application/custom2")
                        .build());
            }
        }));
        holders.addAll(forEachBaseBuilder(new Builder() {
            public CRestHolder build(CRestBuilder builder) {
                return new CRestHolder(builder
                        .addProperties(getEntitySerializerProperties(TEST_JAXB, true))
                        .bindPlainTextDeserializerWith("text/html", "application/custom", "application/custom1", "application/custom2")
                        .setHttpChannelFactory(HttpClientHttpChannelFactory.class)
                        .build());
            }
        }));

        return arrify(holders);
    }

    public static CRestHolder[] byXmlSerializers() {
        List<CRestHolder> holders = new ArrayList<CRestHolder>();
        holders.addAll(forEachBaseBuilder(new Builder() {
            public CRestHolder build(CRestBuilder builder) {
                return new CRestHolder(builder
                        .addProperties(getEntitySerializerProperties(false))
                        .build(), SIMPLEXML_SPECIFIC_PROPERTIES);
            }
        }));
        if(TEST_JAXB) {
            holders.addAll(forEachBaseBuilder(new Builder() {
                public CRestHolder build(CRestBuilder builder) {
                    return new CRestHolder(builder
                            .addProperties(getEntitySerializerProperties(true))
                            .build(), JAXB_SPECIFIC_PROPERTIES);
                }
            }));
        }
        return arrify(holders);
    }


    public static CRestHolder[] byJsonSerializersAndRestServices() {
        return byRestServices();
    }

    public static CRestHolder[] byXmlSerializersAndRestServices() {
        List<CRestHolder> holders = new ArrayList<CRestHolder>();

        holders.addAll(forEachBaseBuilder(new Builder() {
            public CRestHolder build(CRestBuilder builder) {
                return new CRestHolder(builder
                        .addProperties(getEntitySerializerProperties(false))
                        .build(), SIMPLEXML_SPECIFIC_PROPERTIES);
            }
        }));
        holders.addAll(forEachBaseBuilder(new Builder() {
            public CRestHolder build(CRestBuilder builder) {
                return new CRestHolder(builder
                        .setHttpChannelFactory(HttpClientHttpChannelFactory.class)
                        .addProperties(getEntitySerializerProperties(false))
                        .build(), SIMPLEXML_SPECIFIC_PROPERTIES);
            }
        }));
        if(TEST_JAXB) {
            holders.addAll(forEachBaseBuilder(new Builder() {
                public CRestHolder build(CRestBuilder builder) {
                    return new CRestHolder(builder
                            .addProperties(getEntitySerializerProperties(true))
                            .build(), JAXB_SPECIFIC_PROPERTIES);
                }
            }));
            holders.addAll(forEachBaseBuilder(new Builder() {
                public CRestHolder build(CRestBuilder builder) {
                    return new CRestHolder(builder
                            .addProperties(getEntitySerializerProperties(true))
                            .setHttpChannelFactory(HttpClientHttpChannelFactory.class)
                            .build(), JAXB_SPECIFIC_PROPERTIES);
                }
            }));
        }
        return arrify(holders);
    }

    public static Collection<CRestHolder[]> crest(CRestHolder[]... holderss) {
        Collection<CRestHolder[]> data = new ArrayList<CRestHolder[]>();
        for (CRestHolder[] holders : holderss) {
            for (CRestHolder holder : holders) {
                data.add(new CRestHolder[]{holder});
            }
        }
        return data;
    }


    public static class CRestHolder {
        public final CRest crest;
        public final CRestConfig crestConfig;

        public CRestHolder(CRest crest) {
            this(crest, Collections.<String, Object>emptyMap());
        }
        public CRestHolder(CRest crest, Map<String, Object> properties) {
            this.crest = crest;
            this.crestConfig = new DefaultCRestConfig(Maps.merge(CREST_PROPERTIES, properties));
        }
    }
}
