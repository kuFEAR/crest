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

package org.codegist.crest.config;

import org.codegist.crest.CRestConfig;
import org.codegist.crest.param.EncodedPair;
import org.codegist.crest.param.Param;
import org.codegist.crest.param.ParamProcessor;
import org.codegist.crest.param.ParamProcessorFactory;
import org.codegist.crest.serializer.Serializer;
import org.codegist.crest.serializer.ToStringSerializer;
import org.codegist.crest.test.util.CRestConfigs;
import org.codegist.crest.test.util.Classes;
import org.codegist.crest.util.ComponentFactory;
import org.codegist.crest.util.Registry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.OutputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * @author laurent.gilles@codegist.org
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Registry.class, ParamProcessorFactory.class, ComponentFactory.class})
public class DefaultParamConfigBuilderTest {

    private static final Type LIST_GENERIC_TYPE = Classes.byName(TypeHolder.class, "getListType").getGenericReturnType();
    private static final Class<?> LIST_CLASS_TYPE = Classes.byName(TypeHolder.class, "getListType").getReturnType();

    private static final Type ARRAY_GENERIC_TYPE = Classes.byName(TypeHolder.class, "getArrayType").getGenericReturnType();
    private static final Class<?> ARRAY_CLASS_TYPE = Classes.byName(TypeHolder.class, "getArrayType").getReturnType();

    private static final Type SINGLE_GENERIC_TYPE = Classes.byName(TypeHolder.class, "getSingleType").getGenericReturnType();
    private static final Class<?> SINGLE_CLASS_TYPE = Classes.byName(TypeHolder.class, "getSingleType").getReturnType();

    private final CRestConfig mockCRestConfig = CRestConfigs.mockDefaultBehavior();
    private final MethodConfigBuilder methodConfigBuilder = mock(MethodConfigBuilder.class);
    private final Registry<Class<?>, Serializer> mockClassSerializerRegistry = mock(Registry.class);


    @Test(expected=IllegalStateException.class)
    public void shouldUseDefaultValueForName() throws Exception {
        DefaultParamConfigBuilder toTest = newToTest();
        try {
            toTest.build();
        } catch (Exception e) {
            assertEquals("Parameter name is mandatory. This is probably due to a missing or empty named param annotation (one of the following: @CookieParam, @FormParam, @HeaderParam, @MatrixParam, @MultiPartParam, @PathParam, @QueryParam).\n" +
                    "Location information:\n" +
                    "Param[class=interface java.lang.Comparable,type=QUERY,method="+methodConfigBuilder+"]", e.getMessage());
            throw e;
        }
    }
    @Test
    public void shouldOverrideValueForName() throws Exception {
        mockOverride(ParamConfig.PARAM_CONFIG_DEFAULT_NAME, "value");
        DefaultParamConfigBuilder toTest = newToTest();

        ParamConfig actual = toTest.build();
        assertCommons(actual);
        assertEquals("value", actual.getName());
    }
    @Test
    public void shouldUseGivenValueForName() throws Exception {
        DefaultParamConfigBuilder toTest = newToTest();

        ParamConfig actual = toTest.setName("value").build();
        assertCommons(actual);
        assertEquals("value", actual.getName());
    }

    @Test
    public void shouldUseDefaultValueForDefaultValue() throws Exception {
        DefaultParamConfigBuilder toTest = newToTest();
        ParamConfig actual = toTest.setName("bla").build();
        assertCommons(actual);
        assertNull(actual.getDefaultValue());
    }
    @Test
    public void shouldOverrideValueForDefaultValue() throws Exception {
        mockOverride(ParamConfig.PARAM_CONFIG_DEFAULT_VALUE, "value");
        DefaultParamConfigBuilder toTest = newToTest();

        ParamConfig actual = toTest.setName("bla").build();
        assertCommons(actual);
        assertEquals("value", actual.getDefaultValue());
    }
    @Test
    public void shouldUseGivenValueForDefaultValue() throws Exception {
        DefaultParamConfigBuilder toTest = newToTest();

        ParamConfig actual = toTest.setName("bla").setDefaultValue("value").build();
        assertCommons(actual);
        assertEquals("value", actual.getDefaultValue());
    }

    @Test
    public void shouldUseDefaultValueForEncoded() throws Exception {
        DefaultParamConfigBuilder toTest = newToTest();
        ParamConfig actual = toTest.setName("bla").build();
        assertCommons(actual);
        assertFalse(actual.isEncoded());
    }
    @Test
    public void shouldOverrideValueForEncoded() throws Exception {
        mockOverride(ParamConfig.PARAM_CONFIG_DEFAULT_ENCODED, true);
        DefaultParamConfigBuilder toTest = newToTest();

        ParamConfig actual = toTest.setName("bla").build();
        assertCommons(actual);
        assertTrue("value", actual.isEncoded());
    }
    @Test
    public void shouldUseGivenValueForEncoded() throws Exception {
        DefaultParamConfigBuilder toTest = newToTest();

        ParamConfig actual = toTest.setName("bla").setEncoded(true).build();
        assertCommons(actual);
        assertTrue("value", actual.isEncoded());
    }

    @Test
    public void shouldUseDefaultValueForListSeparator() throws Exception {
        ParamProcessor mockParamProcessor = mock(ParamProcessor.class);
        mockStatic(ParamProcessorFactory.class);
        when(ParamProcessorFactory.newInstance(ParamType.getDefault(), null)).thenReturn(mockParamProcessor);
        DefaultParamConfigBuilder toTest = newToTest();
        ParamConfig actual = toTest.setName("bla").build();
        assertCommons(actual);
        assertSame(mockParamProcessor, actual.getParamProcessor());
    }
    @Test
    public void shouldOverrideValueForListSeparator() throws Exception {
        ParamProcessor mockParamProcessor = mock(ParamProcessor.class);
        mockStatic(ParamProcessorFactory.class);
        when(ParamProcessorFactory.newInstance(ParamType.getDefault(), "value")).thenReturn(mockParamProcessor);
        mockOverride(ParamConfig.PARAM_CONFIG_DEFAULT_LIST_SEPARATOR, "value");
        DefaultParamConfigBuilder toTest = newToTest();

        ParamConfig actual = toTest.setName("bla").build();
        assertCommons(actual);
        assertSame(mockParamProcessor, actual.getParamProcessor());
    }
    @Test
    public void shouldUseGivenValueForListSeparator() throws Exception {
        ParamProcessor mockParamProcessor = mock(ParamProcessor.class);
        mockStatic(ParamProcessorFactory.class);
        when(ParamProcessorFactory.newInstance(ParamType.getDefault(), "value")).thenReturn(mockParamProcessor);

        DefaultParamConfigBuilder toTest = newToTest();

        ParamConfig actual = toTest.setName("bla").setListSeparator("value").build();
        assertCommons(actual);
        assertSame(mockParamProcessor, actual.getParamProcessor());
    }

    @Test
    public void shouldUseDefaultValueForType() throws Exception {
        DefaultParamConfigBuilder toTest = newToTest();
        ParamConfig actual = toTest.setName("bla").build();
        assertCommons(actual);
        assertEquals(ParamType.QUERY, actual.getType());
        assertFalse(actual.isEncoded());
    }
    @Test
    public void shouldOverrideValueForType() throws Exception {
        mockOverride(ParamConfig.PARAM_CONFIG_DEFAULT_TYPE, ParamType.MATRIX);
        DefaultParamConfigBuilder toTest = newToTest();

        ParamConfig actual = toTest.setName("bla").build();
        assertCommons(actual);
        assertEquals(ParamType.MATRIX, actual.getType());
        assertFalse(actual.isEncoded());
    }
    @Test
    public void shouldUseGivenValueForType() throws Exception {
        DefaultParamConfigBuilder toTest = newToTest();

        ParamConfig actual = toTest.setName("bla").setType(ParamType.MATRIX).build();
        assertCommons(actual);
        assertEquals(ParamType.MATRIX, actual.getType());
        assertFalse(actual.isEncoded());
    }
    @Test
    public void shouldSetEncodedToTrueForCookie() throws Exception {
        DefaultParamConfigBuilder toTest = newToTest();

        ParamConfig actual = toTest.setName("bla").setType(ParamType.COOKIE).build();
        assertCommons(actual);
        assertEquals(ParamType.COOKIE, actual.getType());
        assertTrue(actual.isEncoded());
    }
    @Test
    public void shouldSetEncodedToTrueForHeader() throws Exception {
        DefaultParamConfigBuilder toTest = newToTest();

        ParamConfig actual = toTest.setName("bla").setType(ParamType.HEADER).build();
        assertCommons(actual);
        assertEquals(ParamType.HEADER, actual.getType());
        assertTrue(actual.isEncoded());
    }


    @Test
    public void shouldUseDefaultValueForSerializer() throws Exception {
        ToStringSerializer mockToStringSerializer = mock(ToStringSerializer.class);
        when(mockClassSerializerRegistry.get(SINGLE_CLASS_TYPE)).thenReturn(mockToStringSerializer);

        DefaultParamConfigBuilder toTest = newToTest();
        ParamConfig actual = toTest.setName("bla").build();
        assertCommons(actual);
        assertSame(mockToStringSerializer, actual.getSerializer());
    }
    @Test
    public void shouldOverrideValueForSerializer() throws Exception {
        TestSerializer mockTestSerializer = mock(TestSerializer.class);
        mockStatic(ComponentFactory.class);
        when(ComponentFactory.instantiate(TestSerializer.class, mockCRestConfig)).thenReturn(mockTestSerializer);

        mockOverride(ParamConfig.PARAM_CONFIG_DEFAULT_SERIALIZER, TestSerializer.class);
        DefaultParamConfigBuilder toTest = newToTest();

        ParamConfig actual = toTest.setName("bla").build();
        assertCommons(actual);
        assertSame(mockTestSerializer, actual.getSerializer());
    }
    @Test
    public void shouldUseGivenValueForSerializer() throws Exception {
        TestSerializer mockTestSerializer = mock(TestSerializer.class);
        mockStatic(ComponentFactory.class);
        when(ComponentFactory.instantiate(TestSerializer.class, mockCRestConfig)).thenReturn(mockTestSerializer);

        DefaultParamConfigBuilder toTest = newToTest();

        ParamConfig actual = toTest.setName("bla").setSerializer(TestSerializer.class).build();
        assertCommons(actual);
        assertSame(mockTestSerializer, actual.getSerializer());
    }


    @Test
    public void shouldUseDefaultValueForParamProcessor() throws Exception {
        ParamProcessor mockParamProcessor = mock(ParamProcessor.class);
        mockStatic(ParamProcessorFactory.class);
        when(ParamProcessorFactory.newInstance(ParamType.getDefault(), null)).thenReturn(mockParamProcessor);

        DefaultParamConfigBuilder toTest = newToTest();
        ParamConfig actual = toTest.setName("bla").build();
        assertCommons(actual);
        assertSame(mockParamProcessor, actual.getParamProcessor());
    }
    @Test
    public void shouldOverrideValueForParamProcessor() throws Exception {
        TestParamProcessor mockParamProcessor = mock(TestParamProcessor.class);
        mockStatic(ComponentFactory.class);
        when(ComponentFactory.instantiate(TestParamProcessor.class, mockCRestConfig)).thenReturn(mockParamProcessor);

        mockOverride(ParamConfig.PARAM_CONFIG_DEFAULT_PROCESSOR, TestParamProcessor.class);
        DefaultParamConfigBuilder toTest = newToTest();

        ParamConfig actual = toTest.setName("bla").build();
        assertCommons(actual);
        assertSame(mockParamProcessor, actual.getParamProcessor());
    }

    @Test
    public void shouldUseDefaultValueForMetas() throws Exception {
        DefaultParamConfigBuilder toTest = newToTest();
        ParamConfig actual = toTest.setName("bla").build();
        assertCommons(actual);
        assertTrue(actual.getMetaDatas().isEmpty());
    }
    @Test
    public void shouldOverrideValueForMetas() throws Exception {
        mockOverride(ParamConfig.PARAM_CONFIG_DEFAULT_METAS, Collections.<String,Object>singletonMap("key","value"));
        DefaultParamConfigBuilder toTest = newToTest();

        ParamConfig actual = toTest.setName("bla").build();
        assertCommons(actual);
        assertEquals(Collections.<String,Object>singletonMap("key","value"), actual.getMetaDatas());
    }
    @Test
    public void shouldUseGivenValueForMetas() throws Exception {
        mockOverride(ParamConfig.PARAM_CONFIG_DEFAULT_METAS, Collections.<String,Object>singletonMap("key2","value2"));
        DefaultParamConfigBuilder toTest = newToTest();

        ParamConfig actual = toTest.setName("bla").setMetaDatas(Collections.<String,Object>singletonMap("key","value")).build();
        assertCommons(actual);
        assertEquals(Collections.<String,Object>singletonMap("key","value"), actual.getMetaDatas());
    }

    private void assertCommons(ParamConfig actual){
        assertEquals(SINGLE_GENERIC_TYPE, actual.getValueGenericType());
        assertEquals(SINGLE_CLASS_TYPE, actual.getValueClass());
        assertEquals(DefaultParamConfig.class, actual.getClass());
    }

    private void mockOverride(String name, Object value){
        when(mockCRestConfig.<Object>get(eq(name), anyObject())).thenReturn(value);
    }
    private DefaultParamConfigBuilder newToTest() throws Exception {
        return newToTest(SINGLE_GENERIC_TYPE, SINGLE_CLASS_TYPE);
    }
    private DefaultParamConfigBuilder newToTestForListType() throws Exception {
        return newToTest(LIST_GENERIC_TYPE, LIST_CLASS_TYPE);
    }
    private DefaultParamConfigBuilder newToTestForArrayType() throws Exception {
        return newToTest(ARRAY_GENERIC_TYPE, ARRAY_CLASS_TYPE);
    }
    private DefaultParamConfigBuilder newToTest(Type type, Class<?> clazz) throws Exception {
        return (DefaultParamConfigBuilder) new DefaultParamConfigBuilder(methodConfigBuilder, mockCRestConfig, mockClassSerializerRegistry, clazz, type);
    }

    public interface TypeHolder {

        List<Comparable<String>> getListType();

        Comparable<String>[] getArrayType();

        Comparable<String> getSingleType();


    }

    public static class TestSerializer implements Serializer {
        public void serialize(Object value, Charset charset, OutputStream out) throws Exception {

        }
    }

    public static class TestParamProcessor implements ParamProcessor {
        public List<EncodedPair> process(Param param, Charset charset, boolean encodeIfNeeded) throws Exception {
            return null;
        }
    }
}
