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

import org.codegist.common.reflect.InvocationHandler;
import org.codegist.common.reflect.JdkProxyFactory;
import org.codegist.common.reflect.ProxyFactory;
import org.codegist.crest.config.*;
import org.codegist.crest.config.annotate.AnnotationHandler;
import org.codegist.crest.config.annotate.CRestAnnotations;
import org.codegist.crest.config.annotate.jaxrs.JaxRsAnnotations;
import org.codegist.crest.io.RequestExecutor;
import org.codegist.crest.io.RetryingRequestExecutor;
import org.codegist.crest.io.http.HttpChannel;
import org.codegist.crest.io.http.HttpChannelFactory;
import org.codegist.crest.io.http.HttpRequestExecutor;
import org.codegist.crest.io.http.platform.HttpURLConnectionHttpChannelFactory;
import org.codegist.crest.serializer.Deserializer;
import org.codegist.crest.test.util.Classes;
import org.codegist.crest.util.ComponentFactory;
import org.codegist.crest.util.ComponentRegistry;
import org.codegist.crest.util.Placeholders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static org.codegist.crest.CRestConfig.*;
import static org.codegist.crest.test.util.Classes.getFieldValue;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * @author laurent.gilles@codegist.org
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({CRestBuilder.class, DefaultCRestConfig.class, HttpURLConnectionHttpChannelFactory.class, ComponentFactory.class})
public class CRestBuilderTest {

    private final CRestBuilder toTest = new CRestBuilder();

    @Test
    public void buildShouldUseJdkProxyFactory() throws NoSuchFieldException, IllegalAccessException {
        CRest actual = toTest.build();
        assertEquals(JdkProxyFactory.class, getFieldValue(actual, "proxyFactory").getClass());
    }

    @Test
    public void setProxyFactoryClassShouldOverrideDefault() throws NoSuchFieldException, IllegalAccessException {
        CRest actual = toTest.setProxyFactory(TestProxyFactory.class).build();
        assertEquals(TestProxyFactory.class, getFieldValue(actual, "proxyFactory").getClass());
    }

    @Test
    public void setProxyFactoryShouldOverrideDefault() throws NoSuchFieldException, IllegalAccessException {
        TestProxyFactory expected = new TestProxyFactory();
        CRest actual = toTest.setProxyFactory(expected).build();
        assertSame(expected, getFieldValue(actual, "proxyFactory"));
    }

    @Test
    public void buildShouldUseHttpURLConnectionHttpChannelFactory() throws Exception{
        CRestConfig config = mockCRestConfig();
        HttpURLConnectionHttpChannelFactory expected = mock(HttpURLConnectionHttpChannelFactory.class);

        mockStatic(ComponentFactory.class);
        when(ComponentFactory.instantiate(HttpURLConnectionHttpChannelFactory.class, config)).thenReturn(expected);

        CRest actual = toTest.build();

        RequestExecutor retryingRequestExecutor = getFieldValue(actual, "requestExecutor");
        assertEquals(RetryingRequestExecutor.class, retryingRequestExecutor.getClass());

        RequestExecutor httpRequestExecutor = getFieldValue(retryingRequestExecutor, "delegate");
        assertSame(HttpRequestExecutor.class, httpRequestExecutor.getClass());

        HttpChannelFactory channelFactory = getFieldValue(httpRequestExecutor, "channelFactory");
        assertSame(expected, channelFactory);
        verifyNew(DefaultCRestConfig.class).withArguments(baseCRestProperties());
    }

    @Test
    public void setHttpChannelFactoryClassShouldOverrideDefault() throws Exception{
        CRestConfig config = mockCRestConfig();
        TestHttpChannelFactory expected = mock(TestHttpChannelFactory.class);

        mockStatic(ComponentFactory.class);
        when(ComponentFactory.instantiate(TestHttpChannelFactory.class, config)).thenReturn(expected);

        CRest actual = toTest.setHttpChannelFactory(TestHttpChannelFactory.class).build();

        RequestExecutor retryingRequestExecutor = getFieldValue(actual, "requestExecutor");
        assertEquals(RetryingRequestExecutor.class, retryingRequestExecutor.getClass());

        RequestExecutor httpRequestExecutor = getFieldValue(retryingRequestExecutor, "delegate");
        assertSame(HttpRequestExecutor.class, httpRequestExecutor.getClass());

        HttpChannelFactory channelFactory = getFieldValue(httpRequestExecutor, "channelFactory");
        assertSame(expected, channelFactory);
        verifyNew(DefaultCRestConfig.class).withArguments(baseCRestProperties());
    }

    @Test
    public void setHttpChannelFactoryShouldOverrideDefault() throws Exception{
        TestHttpChannelFactory expected = mock(TestHttpChannelFactory.class);

        CRest actual = toTest.setHttpChannelFactory(expected).build();

        RequestExecutor retryingRequestExecutor = getFieldValue(actual, "requestExecutor");
        assertEquals(RetryingRequestExecutor.class, retryingRequestExecutor.getClass());

        RequestExecutor httpRequestExecutor = getFieldValue(retryingRequestExecutor, "delegate");
        assertSame(HttpRequestExecutor.class, httpRequestExecutor.getClass());

        HttpChannelFactory channelFactory = getFieldValue(httpRequestExecutor, "channelFactory");
        assertSame(expected, channelFactory);
    }

    @Test
    public void defaultBuildShouldUseDefaultCRestConfig() throws Exception{
        mockCRestConfig();
        toTest.build();
        verifyNew(DefaultCRestConfig.class).withArguments(baseCRestProperties()); // will use DefaultCRestConfig defaults
    }

    @Test
    public void setConcurrencyLevelShouldOverrideDefault() throws Exception {
        Map<String,Object> props = baseCRestProperties();
        props.put(CREST_CONCURRENCY_LEVEL, 2);
        mockCRestConfig(props);
        toTest.setConcurrencyLevel(2).build();
        verifyNew(DefaultCRestConfig.class).withArguments(props);
    }

    @Test
    public void dateFormatShouldOverrideDefault() throws Exception {
        Map<String,Object> props = baseCRestProperties();
        props.put(CREST_DATE_FORMAT, "yyyy");
        mockCRestConfig(props);
        toTest.dateFormat("yyyy").build();
        verifyNew(DefaultCRestConfig.class).withArguments(props);
    }

    @Test
    public void booleanFormatShouldOverrideDefault() throws Exception {
        Map<String,Object> props = baseCRestProperties();
        props.put(CREST_BOOLEAN_TRUE, "yes");
        props.put(CREST_BOOLEAN_FALSE, "no");
        mockCRestConfig(props);
        toTest.booleanFormat("yes", "no").build();
        verifyNew(DefaultCRestConfig.class).withArguments(props);
    }

    @Test
    public void buildShouldUseDefaultAnnotationHandlers() throws Exception {
        CRest actual = toTest.build();
        InterfaceConfigFactory configFactory = Classes.getFieldValue(actual, "configFactory");
        assertEquals(AnnotationDrivenInterfaceConfigFactory.class, configFactory.getClass());

        ComponentRegistry annotationHandlerRegistry = Classes.getFieldValue(configFactory, "handlersRegistry");
        Map mapping = Classes.getFieldValue(annotationHandlerRegistry, "mapping");
        assertEquals(CRestAnnotations.getMapping().size() + JaxRsAnnotations.getMapping().size(), mapping.size());
        assertTrue(mapping.keySet().containsAll(CRestAnnotations.getMapping().keySet()));
        assertTrue(mapping.keySet().containsAll(JaxRsAnnotations.getMapping().keySet()));
    }
    
    @Test
    public void bindAnnotationHandlerShouldBindAnnotationWithGivenHandlerAndEmptyConfig() throws Exception {
        CRest actual = toTest.bindAnnotationHandler(TestAnnotationHandler.class, SuppressWarnings.class).build();
        InterfaceConfigFactory configFactory = Classes.getFieldValue(actual, "configFactory");
        assertEquals(AnnotationDrivenInterfaceConfigFactory.class, configFactory.getClass());

        ComponentRegistry annotationHandlerRegistry = Classes.getFieldValue(configFactory, "handlersRegistry");
        assertEquals(TestAnnotationHandler.class, annotationHandlerRegistry.get(SuppressWarnings.class).getClass());
        Map mapping = Classes.getFieldValue(annotationHandlerRegistry, "mapping");
        assertEquals(CRestAnnotations.getMapping().size() + JaxRsAnnotations.getMapping().size() + 1, mapping.size());
        assertTrue(mapping.keySet().containsAll(CRestAnnotations.getMapping().keySet()));
        assertTrue(mapping.keySet().containsAll(JaxRsAnnotations.getMapping().keySet()));
    }

    @Test
    public void bindAnnotationHandlerWithConfigShouldBindAnnotationWithGivenHandlerAndEmptyConfig() throws Exception {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("some-prop", new Object());
        CRestConfig merged = mock(CRestConfig.class);
        CRestConfig config = mockCRestConfig();
        when(config.merge(map)).thenReturn(merged);

        CRest actualCRest = toTest.bindAnnotationHandler(TestAnnotationHandlerWithConfig.class, SuppressWarnings.class, map).build();
        InterfaceConfigFactory configFactory = Classes.getFieldValue(actualCRest, "configFactory");
        assertEquals(AnnotationDrivenInterfaceConfigFactory.class, configFactory.getClass());

        ComponentRegistry annotationHandlerRegistry = Classes.getFieldValue(configFactory, "handlersRegistry");
        AnnotationHandler actual = (AnnotationHandler) annotationHandlerRegistry.get(SuppressWarnings.class);
        assertEquals(TestAnnotationHandlerWithConfig.class, actual.getClass());
        assertSame(merged, ((TestAnnotationHandlerWithConfig)actual).config);

        Map mapping = Classes.getFieldValue(annotationHandlerRegistry, "mapping");
        assertEquals(CRestAnnotations.getMapping().size() + JaxRsAnnotations.getMapping().size() + 1, mapping.size());
        assertTrue(mapping.keySet().containsAll(CRestAnnotations.getMapping().keySet()));
        assertTrue(mapping.keySet().containsAll(JaxRsAnnotations.getMapping().keySet()));
    }

    @Test
    public void addPropertiesShouldAddAllPropertiesToCRestConfig() throws Exception {
        Map<String,Object> props = baseCRestProperties();
        props.put("hello", "world");
        props.put(CRestConfig.class.getName() + "#placeholders", new Object());

        Map<String,Object> expected = baseCRestProperties();
        expected.putAll(props);

        mockCRestConfig(props);
        toTest.addProperties(props).build();
        
        verifyNew(DefaultCRestConfig.class).withArguments(expected);
    }

    @Test
    public void setPropertyShouldSetAPropertiesToCRestConfig() throws Exception {
        Object o = new Object();
        Map<String,Object> expected = new HashMap();
        expected.put(CRestConfig.class.getName() + "#placeholders", o);

        mockCRestConfig(expected);
        toTest.property(CRestConfig.class.getName() + "#placeholders", o).build();

        verifyNew(DefaultCRestConfig.class).withArguments(expected);
    }

    @Test
    public void setPropertiesShouldResetAndAddAllPropertiesToCRestConfig() throws Exception {
        Map<String,Object> props = baseCRestProperties();
        props.put("hello", "world");
        props.put(CRestConfig.class.getName() + "#placeholders", new Object());

        Map<String,Object> props2 = baseCRestProperties();
        props2.put("hello2", "world2");
        props2.put(CRestConfig.class.getName() + "#placeholders", new Object());


        Map<String,Object> expected = baseCRestProperties();
        expected.putAll(props2);

        mockCRestConfig(props);
        toTest.addProperties(props).setProperties(props2).build();

        verifyNew(DefaultCRestConfig.class).withArguments(expected);
    }

    @Test
    public void addPlaceholdersShouldAddGivenPlaceholders() throws Exception {
        Map<String,String> phs = new HashMap<String,String>();
        phs.put("a", "b");
        phs.put("c", "d");

        Map<String,Object> expected = new HashMap();
        expected.put(CRestConfig.class.getName() + "#placeholders", Placeholders.compile(phs));

        mockCRestConfig(expected);
        toTest.addPlaceholders(phs).build();

        ArgumentCaptor<Map> placeholders = ArgumentCaptor.forClass(Map.class);
        verifyNew(DefaultCRestConfig.class).withArguments(placeholders .capture());

        Map<Pattern,String> placeholdersCompiled =  Placeholders.compile(phs);
        Map<Pattern,String> actuals =  (Map<Pattern,String>) placeholders.getValue().get(CRestConfig.class.getName() + "#placeholders");

        assertEquals(placeholdersCompiled.values().size(), actuals.values().size());
        assertTrue(placeholdersCompiled.values().containsAll(actuals.values()));
        assertEquals(placeholdersCompiled.keySet().size(), actuals.keySet().size());
    }


    private static Map<String,Object> baseCRestProperties(){
        Map<String,Object> map = new HashMap<String, Object>();
        map.put(CRestConfig.class.getName() + "#placeholders", new HashMap<String,String>());
        return map;
    }
    private static CRestConfig mockCRestConfig() throws Exception {
        return mockCRestConfig(baseCRestProperties());
    }
    private static CRestConfig mockCRestConfig(Map<String,Object> properties) throws Exception {
        DefaultCRestConfig config = mock(DefaultCRestConfig.class);
        whenNew(DefaultCRestConfig.class)
                .withParameterTypes(Map.class)
                .withArguments(properties).thenReturn(config);
        return config;
    }

    public static class TestHttpChannelFactory implements HttpChannelFactory {
        public HttpChannel open(MethodType methodType, String url, Charset charset) throws IOException {
            return null;
        }
    }
    public static class TestProxyFactory implements ProxyFactory {
        public <T> T createProxy(ClassLoader classLoader, InvocationHandler handler, Class<?>[] interfaces) {
            return null;
        }

        public <T> T createProxy(ClassLoader classLoader, InvocationHandler handler, Class<?>[] interfaces, Object target) {
            return null;
        }
    }
    public static class TestAnnotationHandler implements AnnotationHandler<SuppressWarnings> {

        public void handleInterfaceAnnotation(SuppressWarnings annotation, InterfaceConfigBuilder builder) throws Exception {

        }

        public void handleMethodAnnotation(SuppressWarnings annotation, MethodConfigBuilder builder) throws Exception {

        }

        public void handleParameterAnnotation(SuppressWarnings annotation, ParamConfigBuilder builder) throws Exception {

        }
    }

    public static class TestAnnotationHandlerWithConfig implements AnnotationHandler<SuppressWarnings> {

        final CRestConfig config;

        public TestAnnotationHandlerWithConfig(CRestConfig config) {
            this.config = config;
        }

        public void handleInterfaceAnnotation(SuppressWarnings annotation, InterfaceConfigBuilder builder) throws Exception {

        }

        public void handleMethodAnnotation(SuppressWarnings annotation, MethodConfigBuilder builder) throws Exception {

        }

        public void handleParameterAnnotation(SuppressWarnings annotation, ParamConfigBuilder builder) throws Exception {

        }
    }

    public static class TestDeserializer implements Deserializer  {
        public <T> T deserialize(Class<T> type, Type genericType, InputStream stream, Charset charset) throws Exception {
            return null;
        }
    }

    public static class TestDeserializerWithConfig implements Deserializer  {
        final CRestConfig config;

        public TestDeserializerWithConfig(CRestConfig config) {
            this.config = config;
        }

        public <T> T deserialize(Class<T> type, Type genericType, InputStream stream, Charset charset) throws Exception {
            return null;
        }
    }
}
