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
import org.codegist.crest.entity.EntityWriter;
import org.codegist.crest.entity.UrlEncodedFormEntityWriter;
import org.codegist.crest.entity.multipart.MultiPartEntityWriter;
import org.codegist.crest.handler.*;
import org.codegist.crest.interceptor.NoOpRequestInterceptor;
import org.codegist.crest.interceptor.RequestInterceptor;
import org.codegist.crest.io.Request;
import org.codegist.crest.io.RequestException;
import org.codegist.crest.io.Response;
import org.codegist.crest.serializer.Deserializer;
import org.codegist.crest.serializer.Serializer;
import org.codegist.crest.test.util.CRestConfigs;
import org.codegist.crest.test.util.TestInterface;
import org.codegist.crest.test.util.Values;
import org.codegist.crest.util.ComponentFactory;
import org.codegist.crest.util.MultiParts;
import org.codegist.crest.util.Registry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * @author laurent.gilles@codegist.org
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Registry.class, DefaultMethodConfigBuilder.class, DefaultParamConfigBuilder.class, RegexPathTemplate.class, ComponentFactory.class})
public class DefaultMethodConfigBuilderTest {

    private final InterfaceConfigBuilder interfaceConfigBuilder = mock(InterfaceConfigBuilder.class);

    private final DefaultParamConfigBuilder mockParamConfigBuilder1 = mock(DefaultParamConfigBuilder.class);
    private final DefaultParamConfigBuilder mockParamConfigBuilder2 = mock(DefaultParamConfigBuilder.class);

    private final DefaultParamConfigBuilder mockExtraParamConfigBuilder1 = mock(DefaultParamConfigBuilder.class);
    private final DefaultParamConfigBuilder mockExtraParamConfigBuilder2 = mock(DefaultParamConfigBuilder.class);

    private final RegexPathTemplate mockRegexPathTemplate = mock(RegexPathTemplate.class);
    private final Method method = TestInterface.M1;
    private final CRestConfig mockCRestConfig = CRestConfigs.mockDefaultBehavior();
    private final Registry<String, Deserializer> mockMimeDeserializerRegistry = mock(Registry.class);
    private final Registry<Class<?>, Serializer> mockClassSerializerRegistry = mock(Registry.class);

    private final ParamConfig mockM1ParamConfig1 = mock(ParamConfig.class);
    private final ParamConfig mockM1ParamConfig2 = mock(ParamConfig.class);

    private final ParamConfig mockExtraParamConfig1 = mock(ParamConfig.class);
    private final ParamConfig mockExtraParamConfig2 = mock(ParamConfig.class);


    @Test(expected = IllegalStateException.class)
    public void shouldUseDefaultValueForEndpoint() throws Exception {
        DefaultMethodConfigBuilder toTest = newToTest();
        try {
            toTest.build();
        } catch (Exception e) {
            assertEquals("End-point is mandatory. This is probably due to a missing or empty @EndPoint annotation.\n" +
                    "Either provide an @EndPoint annotation or build a CRest instance as follow:\n" +
                    "\n" +
                    "   String defaultEndPoint = ...;\n" +
                    "   CRest crest = CRest.property(MethodConfig.METHOD_CONFIG_DEFAULT_ENDPOINT, defaultEndPoint).build();\n" +
                    "\n" +
                    "Location information:\n" +
                    "Method[method=public abstract void org.codegist.crest.test.util.TestInterface.m1(java.lang.String,int),interface=Mock for InterfaceConfigBuilder, hashCode: "+interfaceConfigBuilder.hashCode()+"]", e.getMessage());
            throw e;
        }
    }

    @Test
    public void shouldOverrideValueForEndpoint() throws Exception {
        mockEndpoint();
        DefaultMethodConfigBuilder toTest = newToTest();
        MethodConfig actual = toTest.build();
        assertCommons(actual);
        assertSame(mockRegexPathTemplate, actual.getPathTemplate());
    }

    @Test
    public void shouldUseGivenValueForEndpoint() throws Exception {
        mockEndpoint(Values.ENDPOINT + "/a", "");

        DefaultMethodConfigBuilder toTest = newToTest();
        MethodConfig actual = toTest.setEndPoint(Values.ENDPOINT + "/a").build();
        assertCommons(actual);
        assertSame(mockRegexPathTemplate, actual.getPathTemplate());
    }

    @Test
    public void shouldUseDefaultValueForProduces() throws Exception {
        mockEndpoint();
        DefaultMethodConfigBuilder toTest = newToTest();
        MethodConfig actual = toTest.build();
        assertCommons(actual);
        assertNull(actual.getProduces());
    }

    @Test
    public void shouldOverrideValueForProduces() throws Exception {
        mockEndpoint();
        mockOverride(MethodConfig.METHOD_CONFIG_DEFAULT_PRODUCES, "value");
        DefaultMethodConfigBuilder toTest = newToTest();
        MethodConfig actual = toTest.build();
        assertCommons(actual);
        assertEquals("value",actual.getProduces());
    }

    @Test
    public void shouldUseGivenValueForProduces() throws Exception {
        mockEndpoint();
        DefaultMethodConfigBuilder toTest = newToTest();
        MethodConfig actual = toTest.setProduces("value").build();
        assertCommons(actual);
        assertEquals("value",actual.getProduces());
    }

    @Test
    public void shouldUseDefaultValueForCharset() throws Exception {
        mockEndpoint();
        DefaultMethodConfigBuilder toTest = newToTest();
        MethodConfig actual = toTest.build();
        assertCommons(actual);
        assertEquals(Values.UTF8, actual.getCharset());
    }

    @Test
    public void shouldOverrideValueForCharset() throws Exception {
        mockEndpoint();
        mockOverride(MethodConfig.METHOD_CONFIG_DEFAULT_CHARSET, Values.ISO_8859_1);
        DefaultMethodConfigBuilder toTest = newToTest();
        MethodConfig actual = toTest.build();
        assertCommons(actual);
        assertEquals(Values.ISO_8859_1,actual.getCharset());
    }

    @Test
    public void shouldUseGivenValueForCharset() throws Exception {
        mockEndpoint();
        DefaultMethodConfigBuilder toTest = newToTest();
        MethodConfig actual = toTest.setCharset(Values.ISO_8859_1).build();
        assertCommons(actual);
        assertEquals(Values.ISO_8859_1,actual.getCharset());
    }

    @Test
    public void shouldUseDefaultValueForType() throws Exception {
        mockEndpoint();
        DefaultMethodConfigBuilder toTest = newToTest();
        MethodConfig actual = toTest.build();
        assertCommons(actual);
        assertEquals(MethodType.GET, actual.getType());
        assertNull(actual.getEntityWriter());
    }

    @Test
    public void shouldOverrideValueForType() throws Exception {
        mockEndpoint();
        mockOverride(MethodConfig.METHOD_CONFIG_DEFAULT_TYPE, MethodType.PUT);
        DefaultMethodConfigBuilder toTest = newToTest();
        MethodConfig actual = toTest.build();
        assertCommons(actual);
        assertEquals(MethodType.PUT,actual.getType());
        assertEquals(UrlEncodedFormEntityWriter.class, actual.getEntityWriter().getClass());
    }

    @Test
    public void shouldUseGivenValueForType() throws Exception {
        mockEndpoint();
        DefaultMethodConfigBuilder toTest = newToTest();
        MethodConfig actual = toTest.setType(MethodType.POST).build();
        assertCommons(actual);
        assertEquals(MethodType.POST,actual.getType());
        assertEquals(UrlEncodedFormEntityWriter.class, actual.getEntityWriter().getClass());
    }


    @Test
    public void shouldUseDefaultValueForSocketTimeout() throws Exception {
        mockEndpoint();
        DefaultMethodConfigBuilder toTest = newToTest();
        MethodConfig actual = toTest.build();
        assertCommons(actual);
        assertEquals(20000, actual.getSocketTimeout());
    }

    @Test
    public void shouldOverrideValueForSocketTimeout() throws Exception {
        mockEndpoint();
        mockOverride(MethodConfig.METHOD_CONFIG_DEFAULT_SO_TIMEOUT, 10);
        DefaultMethodConfigBuilder toTest = newToTest();
        MethodConfig actual = toTest.build();
        assertCommons(actual);
        assertEquals(10,actual.getSocketTimeout());
    }

    @Test
    public void shouldUseGivenValueForSocketTimeout() throws Exception {
        mockEndpoint();
        DefaultMethodConfigBuilder toTest = newToTest();
        MethodConfig actual = toTest.setSocketTimeout(10).build();
        assertCommons(actual);
        assertEquals(10,actual.getSocketTimeout());
    }


    @Test
    public void shouldUseDefaultValueForConnectionTimeout() throws Exception {
        mockEndpoint();
        DefaultMethodConfigBuilder toTest = newToTest();
        MethodConfig actual = toTest.build();
        assertCommons(actual);
        assertEquals(20000, actual.getConnectionTimeout());
    }

    @Test
    public void shouldOverrideValueForConnectionTimeout() throws Exception {
        mockEndpoint();
        mockOverride(MethodConfig.METHOD_CONFIG_DEFAULT_CO_TIMEOUT, 10);
        DefaultMethodConfigBuilder toTest = newToTest();
        MethodConfig actual = toTest.build();
        assertCommons(actual);
        assertEquals(10,actual.getConnectionTimeout());
    }

    @Test
    public void shouldUseGivenValueForConnectionTimeout() throws Exception {
        mockEndpoint();
        DefaultMethodConfigBuilder toTest = newToTest();
        MethodConfig actual = toTest.setConnectionTimeout(10).build();
        assertCommons(actual);
        assertEquals(10,actual.getConnectionTimeout());
    }




    @Test
    public void shouldUseDefaultValueForExtraParams() throws Exception {
        mockEndpoint();
        DefaultMethodConfigBuilder toTest = newToTest();
        MethodConfig actual = toTest.build();
        assertCommons(actual);
        assertEquals(0, actual.getExtraParams().length);
    }

    @Test
    public void shouldOverrideValueForExtraParams() throws Exception {
        mockEndpoint();
        ParamConfig[] extraParams = {mock(ParamConfig.class), mock(ParamConfig.class)};
        mockOverride(MethodConfig.METHOD_CONFIG_DEFAULT_EXTRA_PARAMS, extraParams);
        DefaultMethodConfigBuilder toTest = newToTest();
        MethodConfig actual = toTest.build();
        assertCommons(actual);
        assertArrayEquals(extraParams, actual.getExtraParams());
        assertNull(actual.getEntityWriter());
    }

    @Test
    public void shouldUseDefaultValuePlusGivenValueForExtraParams() throws Exception {
        mockEndpoint();
        ParamConfig[] extraParams = {mock(ParamConfig.class), mock(ParamConfig.class)};
        ParamConfig[] expected = {extraParams[0], extraParams[1], mockExtraParamConfig1, mockExtraParamConfig2};
        mockOverride(MethodConfig.METHOD_CONFIG_DEFAULT_EXTRA_PARAMS, extraParams);

        DefaultMethodConfigBuilder toTest = newToTest();
        toTest.startExtraParamConfig().build();
        toTest.startExtraParamConfig().build();
        MethodConfig actual = toTest.build();
        assertCommons(actual);
        assertArrayEquals(expected, actual.getExtraParams());
        assertNull(actual.getEntityWriter());
    }

    @Test
    public void shouldSwitchToMultiPartEntityWriterIfTypeIsPostAndAnyMethodParamHasMultiPartMetadatas() throws Exception {
        shouldSwitchToMultiPartEntityWriterIfAnyMethodParamHasMultiPartMetadatasForType(MethodType.POST);
    }

    @Test
    public void shouldSwitchToMultiPartEntityWriterIfTypeIsPutAndAnyMethodParamHasMultiPartMetadatas() throws Exception {
        shouldSwitchToMultiPartEntityWriterIfAnyMethodParamHasMultiPartMetadatasForType(MethodType.PUT);
    }

    private void shouldSwitchToMultiPartEntityWriterIfAnyMethodParamHasMultiPartMetadatasForType (MethodType type) throws Exception {
        mockEndpoint();
        when(mockM1ParamConfig2.getMetaDatas()).thenReturn(MultiParts.toMetaDatas("test","test"));

        MethodConfig actual = newToTest().setType(type).build();
        assertCommons(actual);
        assertEquals(2, actual.getParamCount());
        assertEquals(mockM1ParamConfig1, actual.getParamConfig(0));
        assertEquals(mockM1ParamConfig2, actual.getParamConfig(1));
        assertEquals(MultiPartEntityWriter.class, actual.getEntityWriter().getClass());
    }

    @Test
    public void shouldSwitchToMultiPartEntityWriterIfAnyDefaultExtraParamHasMultiPartMetadatas() throws Exception {
        mockEndpoint();
        ParamConfig[] extraParams = {mock(ParamConfig.class), mock(ParamConfig.class)};
        mockOverride(MethodConfig.METHOD_CONFIG_DEFAULT_EXTRA_PARAMS, extraParams);

        when(extraParams[1].getMetaDatas()).thenReturn(MultiParts.toMetaDatas("test","test"));

        MethodConfig actual = newToTest().setType(MethodType.POST).build();
        assertCommons(actual);
        assertArrayEquals(extraParams, actual.getExtraParams());
        assertEquals(MultiPartEntityWriter.class, actual.getEntityWriter().getClass());
    }

    @Test
    public void shouldSwitchToMultiPartEntityWriterIfAnyExtraParamHasMultiPartMetadatas() throws Exception {
        mockEndpoint();
        when(mockExtraParamConfig1.getMetaDatas()).thenReturn(MultiParts.toMetaDatas("test","test"));

        MethodConfigBuilder toTest = newToTest().setType(MethodType.POST);
        toTest.startExtraParamConfig().build();
        MethodConfig actual = toTest.build();
        assertCommons(actual);
        assertArrayEquals(new ParamConfig[]{mockExtraParamConfig1}, actual.getExtraParams());
        assertEquals(MultiPartEntityWriter.class, actual.getEntityWriter().getClass());
    }

    @Test
    public void startParamConfigShouldReturnPrecreatedMethodParamConfigBuilder() throws Exception {
        mockEndpoint();
        MethodConfigBuilder toTest = newToTest();
        assertSame(mockParamConfigBuilder1, toTest.startParamConfig(0));
        assertSame(mockParamConfigBuilder2, toTest.startParamConfig(1));
        try {
            toTest.startParamConfig(2);
            fail();
        } catch (IndexOutOfBoundsException e) {

        }
    }


    @Test
    public void shouldUseDefaultValueForRequestInterceptor() throws Exception {
        mockEndpoint();
        NoOpRequestInterceptor mockNoOpRequestInterceptor = mock(NoOpRequestInterceptor.class);
        mockStatic(ComponentFactory.class);
        when(ComponentFactory.instantiate(NoOpRequestInterceptor.class, mockCRestConfig)).thenReturn(mockNoOpRequestInterceptor);

        DefaultMethodConfigBuilder toTest = newToTest();
        MethodConfig actual = toTest.build();

        assertCommons(actual);
        assertSame(mockNoOpRequestInterceptor, actual.getRequestInterceptor());
    }

    @Test
    public void shouldOverrideValueForRequestInterceptor() throws Exception {
        mockEndpoint();
        TestRequestInterceptor mockTestRequestInterceptor = mock(TestRequestInterceptor.class);
        mockStatic(ComponentFactory.class);
        when(ComponentFactory.instantiate(TestRequestInterceptor.class, mockCRestConfig)).thenReturn(mockTestRequestInterceptor);

        mockOverride(MethodConfig.METHOD_CONFIG_DEFAULT_REQUEST_INTERCEPTOR, TestRequestInterceptor.class);
        DefaultMethodConfigBuilder toTest = newToTest();
        MethodConfig actual = toTest.build();
        assertCommons(actual);
        assertSame(mockTestRequestInterceptor, actual.getRequestInterceptor());
    }

    @Test
    public void shouldUseGivenValueForRequestInterceptor() throws Exception {
        mockEndpoint();
        TestRequestInterceptor mockTestRequestInterceptor = mock(TestRequestInterceptor.class);
        mockStatic(ComponentFactory.class);
        when(ComponentFactory.instantiate(TestRequestInterceptor.class, mockCRestConfig)).thenReturn(mockTestRequestInterceptor);

        DefaultMethodConfigBuilder toTest = newToTest();
        MethodConfig actual = toTest.setRequestInterceptor(TestRequestInterceptor.class).build();
        assertCommons(actual);
        assertSame(mockTestRequestInterceptor, actual.getRequestInterceptor());
    }

    @Test
    public void shouldUseDefaultValueForResponseHandler() throws Exception {
        mockEndpoint();
        DefaultResponseHandler mockDefaultResponseHandler = mock(DefaultResponseHandler.class);
        mockStatic(ComponentFactory.class);
        when(ComponentFactory.instantiate(DefaultResponseHandler.class, mockCRestConfig)).thenReturn(mockDefaultResponseHandler);

        DefaultMethodConfigBuilder toTest = newToTest();
        MethodConfig actual = toTest.build();

        assertCommons(actual);
        assertSame(mockDefaultResponseHandler, actual.getResponseHandler());
    }

    @Test
    public void shouldOverrideValueForResponseHandler() throws Exception {
        mockEndpoint();
        TestResponseHandler mockTestResponseHandler = mock(TestResponseHandler.class);
        mockStatic(ComponentFactory.class);
        when(ComponentFactory.instantiate(TestResponseHandler.class, mockCRestConfig)).thenReturn(mockTestResponseHandler);

        mockOverride(MethodConfig.METHOD_CONFIG_DEFAULT_RESPONSE_HANDLER, TestResponseHandler.class);
        DefaultMethodConfigBuilder toTest = newToTest();
        MethodConfig actual = toTest.build();
        assertCommons(actual);
        assertSame(mockTestResponseHandler, actual.getResponseHandler());
    }

    @Test
    public void shouldUseGivenValueForResponseHandler() throws Exception {
        mockEndpoint();
        TestResponseHandler mockTestResponseHandler = mock(TestResponseHandler.class);
        mockStatic(ComponentFactory.class);
        when(ComponentFactory.instantiate(TestResponseHandler.class, mockCRestConfig)).thenReturn(mockTestResponseHandler);

        DefaultMethodConfigBuilder toTest = newToTest();
        MethodConfig actual = toTest.setResponseHandler(TestResponseHandler.class).build();
        assertCommons(actual);
        assertSame(mockTestResponseHandler, actual.getResponseHandler());
    }

    @Test
    public void shouldUseDefaultValueForErrorHandler() throws Exception {
        mockEndpoint();
        ErrorDelegatorHandler mockErrorHandler = mock(ErrorDelegatorHandler.class);
        mockStatic(ComponentFactory.class);
        when(ComponentFactory.instantiate(ErrorDelegatorHandler.class, mockCRestConfig)).thenReturn(mockErrorHandler);

        DefaultMethodConfigBuilder toTest = newToTest();
        MethodConfig actual = toTest.build();

        assertCommons(actual);
        assertSame(mockErrorHandler, actual.getErrorHandler());
    }

    @Test
    public void shouldOverrideValueForErrorHandler() throws Exception {
        mockEndpoint();
        TestErrorHandler mockTestErrorHandler = mock(TestErrorHandler.class);
        mockStatic(ComponentFactory.class);
        when(ComponentFactory.instantiate(TestErrorHandler.class, mockCRestConfig)).thenReturn(mockTestErrorHandler);

        mockOverride(MethodConfig.METHOD_CONFIG_DEFAULT_ERROR_HANDLER, TestErrorHandler.class);
        DefaultMethodConfigBuilder toTest = newToTest();
        MethodConfig actual = toTest.build();
        assertCommons(actual);
        assertSame(mockTestErrorHandler, actual.getErrorHandler());
    }

    @Test
    public void shouldUseGivenValueForErrorHandler() throws Exception {
        mockEndpoint();
        TestErrorHandler mockTestErrorHandler = mock(TestErrorHandler.class);
        mockStatic(ComponentFactory.class);
        when(ComponentFactory.instantiate(TestErrorHandler.class, mockCRestConfig)).thenReturn(mockTestErrorHandler);

        DefaultMethodConfigBuilder toTest = newToTest();
        MethodConfig actual = toTest.setErrorHandler(TestErrorHandler.class).build();
        assertCommons(actual);
        assertSame(mockTestErrorHandler, actual.getErrorHandler());
    }

    @Test
    public void shouldUseDefaultValueForRetryHandler() throws Exception {
        mockEndpoint();
        MaxAttemptRetryHandler mockRetryHandler = mock(MaxAttemptRetryHandler.class);
        mockStatic(ComponentFactory.class);
        when(ComponentFactory.instantiate(MaxAttemptRetryHandler.class, mockCRestConfig)).thenReturn(mockRetryHandler);

        DefaultMethodConfigBuilder toTest = newToTest();
        MethodConfig actual = toTest.build();

        assertCommons(actual);
        assertSame(mockRetryHandler, actual.getRetryHandler());
    }

    @Test
    public void shouldOverrideValueForRetryHandler() throws Exception {
        mockEndpoint();
        TestRetryHandler mockTestRetryHandler = mock(TestRetryHandler.class);
        mockStatic(ComponentFactory.class);
        when(ComponentFactory.instantiate(TestRetryHandler.class, mockCRestConfig)).thenReturn(mockTestRetryHandler);

        mockOverride(MethodConfig.METHOD_CONFIG_DEFAULT_RETRY_HANDLER, TestRetryHandler.class);
        DefaultMethodConfigBuilder toTest = newToTest();
        MethodConfig actual = toTest.build();
        assertCommons(actual);
        assertSame(mockTestRetryHandler, actual.getRetryHandler());
    }

    @Test
    public void shouldUseGivenValueForRetryHandler() throws Exception {
        mockEndpoint();
        TestRetryHandler mockTestRetryHandler = mock(TestRetryHandler.class);
        mockStatic(ComponentFactory.class);
        when(ComponentFactory.instantiate(TestRetryHandler.class, mockCRestConfig)).thenReturn(mockTestRetryHandler);

        DefaultMethodConfigBuilder toTest = newToTest();
        MethodConfig actual = toTest.setRetryHandler(TestRetryHandler.class).build();
        assertCommons(actual);
        assertSame(mockTestRetryHandler, actual.getRetryHandler());
    }

    @Test
    public void shouldUseDefaultValueForEntityWriter() throws Exception {
        mockEndpoint();
        DefaultMethodConfigBuilder toTest = newToTest();
        MethodConfig actual = toTest.build();

        assertCommons(actual);
        assertNull(actual.getEntityWriter());
    }

    @Test
    public void shouldOverrideValueForEntityWriter() throws Exception {
        mockEndpoint();
        TestEntityWriter mockTestEntityWriter = mock(TestEntityWriter.class);
        mockStatic(ComponentFactory.class);
        when(ComponentFactory.instantiate(TestEntityWriter.class, mockCRestConfig)).thenReturn(mockTestEntityWriter);

        mockOverride(MethodConfig.METHOD_CONFIG_DEFAULT_ENTITY_WRITER, TestEntityWriter.class);
        DefaultMethodConfigBuilder toTest = newToTest();
        MethodConfig actual = toTest.build();
        assertCommons(actual);
        assertSame(mockTestEntityWriter, actual.getEntityWriter());
    }

    @Test
    public void shouldUseGivenValueForEntityWriter() throws Exception {
        mockEndpoint();
        TestEntityWriter mockTestEntityWriter = mock(TestEntityWriter.class);
        mockStatic(ComponentFactory.class);
        when(ComponentFactory.instantiate(TestEntityWriter.class, mockCRestConfig)).thenReturn(mockTestEntityWriter);

        DefaultMethodConfigBuilder toTest = newToTest();
        MethodConfig actual = toTest.setEntityWriter(TestEntityWriter.class).build();
        assertCommons(actual);
        assertSame(mockTestEntityWriter, actual.getEntityWriter());
    }

    @Test
    public void shouldUseDefaultValueForDeserializers() throws Exception {
        mockEndpoint();
        DefaultMethodConfigBuilder toTest = newToTest();
        MethodConfig actual = toTest.build();

        assertCommons(actual);
        assertEquals(0, actual.getDeserializers().length);
    }

    @Test
    public void shouldOverrideValueForDeserializers() throws Exception {
        mockEndpoint();
        TestDeserializer1 mockTestDeserializer1 = mock(TestDeserializer1.class);
        TestDeserializer2 mockTestDeserializer2 = mock(TestDeserializer2.class);
        mockStatic(ComponentFactory.class);
        when(ComponentFactory.instantiate(TestDeserializer1.class, mockCRestConfig)).thenReturn(mockTestDeserializer1);
        when(ComponentFactory.instantiate(TestDeserializer2.class, mockCRestConfig)).thenReturn(mockTestDeserializer2);

        mockOverride(MethodConfig.METHOD_CONFIG_DEFAULT_DESERIALIZERS, asList(TestDeserializer1.class, TestDeserializer2.class));
        DefaultMethodConfigBuilder toTest = newToTest();
        MethodConfig actual = toTest.build();
        assertCommons(actual);
        assertArrayEquals(new Deserializer[]{mockTestDeserializer1,mockTestDeserializer2}, actual.getDeserializers());
    }

    @Test
    public void shouldUseGivenValueForDeserializers() throws Exception {
        mockEndpoint();
        TestDeserializer1 mockTestDeserializer1 = mock(TestDeserializer1.class);
        TestDeserializer2 mockTestDeserializer2 = mock(TestDeserializer2.class);
        mockStatic(ComponentFactory.class);
        when(ComponentFactory.instantiate(TestDeserializer1.class, mockCRestConfig)).thenReturn(mockTestDeserializer1);
        when(ComponentFactory.instantiate(TestDeserializer2.class, mockCRestConfig)).thenReturn(mockTestDeserializer2);

        mockOverride(MethodConfig.METHOD_CONFIG_DEFAULT_DESERIALIZERS, asList(TestDeserializer1.class, TestDeserializer2.class));
        DefaultMethodConfigBuilder toTest = newToTest();
        toTest.setDeserializer(TestDeserializer2.class);
        MethodConfig actual = toTest.build();
        assertCommons(actual);
        assertArrayEquals(new Deserializer[]{mockTestDeserializer2}, actual.getDeserializers());
    }


    @Test
    public void shouldUseDefaultValueForConsumes() throws Exception {
        mockEndpoint();
        DefaultMethodConfigBuilder toTest = newToTest();
        MethodConfig actual = toTest.build();

        assertCommons(actual);
        assertArrayEquals(new String[]{"*/*"}, actual.getConsumes());
        assertEquals(0, actual.getDeserializers().length);
    }

    @Test
    public void shouldOverrideValueForConsumes() throws Exception {
        mockEndpoint();
        TestDeserializer1 mockTestDeserializer1 = mock(TestDeserializer1.class);
        TestDeserializer2 mockTestDeserializer2 = mock(TestDeserializer2.class);
        when(mockMimeDeserializerRegistry.get("mime1")).thenReturn(mockTestDeserializer1);
        when(mockMimeDeserializerRegistry.get("mime2")).thenReturn(mockTestDeserializer2);

        mockOverride(MethodConfig.METHOD_CONFIG_DEFAULT_CONSUMES, asList("mime1", "mime2"));
        DefaultMethodConfigBuilder toTest = newToTest();
        MethodConfig actual = toTest.build();
        assertCommons(actual);
        assertArrayEquals(new String[]{"mime1","mime2"}, actual.getConsumes());
        assertArrayEquals(new Deserializer[]{mockTestDeserializer1,mockTestDeserializer2}, actual.getDeserializers());
    }

    @Test
    public void shouldUseGivenValueForConsumes() throws Exception {
        mockEndpoint();
        TestDeserializer1 mockTestDeserializer1 = mock(TestDeserializer1.class);
        TestDeserializer2 mockTestDeserializer2 = mock(TestDeserializer2.class);
        when(mockMimeDeserializerRegistry.get("mime1")).thenReturn(mockTestDeserializer1);
        when(mockMimeDeserializerRegistry.get("mime2")).thenReturn(mockTestDeserializer2);

        mockOverride(MethodConfig.METHOD_CONFIG_DEFAULT_CONSUMES, asList("mime1", "mime2"));
        DefaultMethodConfigBuilder toTest = newToTest();
        toTest.setConsumes("mime2");
        MethodConfig actual = toTest.build();
        assertCommons(actual);
        assertArrayEquals(new Deserializer[]{mockTestDeserializer2}, actual.getDeserializers());
    }


    @Test
    public void shouldUseDefaultValueForPath() throws Exception {
        mockEndpoint();
        DefaultMethodConfigBuilder toTest = newToTest();
        MethodConfig actual = toTest.build();

        assertCommons(actual);
        assertSame(mockRegexPathTemplate, actual.getPathTemplate());
    }

    @Test
    public void shouldOverrideValueForPath() throws Exception {
        mockEndpoint("http://localhost:81", "/some/path");
        mockOverride(MethodConfig.METHOD_CONFIG_DEFAULT_PATH, asList("/some","path"));
        DefaultMethodConfigBuilder toTest = newToTest();
        MethodConfig actual = toTest.build();
        assertCommons(actual);
        assertSame(mockRegexPathTemplate, actual.getPathTemplate());
    }

    @Test
    public void shouldUseGivenValueForPath() throws Exception {
        mockEndpoint(Values.ENDPOINT, "/some/path/a/b");
        mockOverride(MethodConfig.METHOD_CONFIG_DEFAULT_PATH, asList("/some","path"));
        DefaultMethodConfigBuilder toTest = newToTest();
        toTest.appendPath("a").appendPath("/b");
        MethodConfig actual = toTest.build();
        assertCommons(actual);
        assertSame(mockRegexPathTemplate, actual.getPathTemplate());
    }

    @Test
    public void setParamsSerializerShouldSetSerializerOnAllParamConfigs() throws Exception {
        MethodConfigBuilder toTest = newToTest();
        assertSame(toTest, toTest.setParamsSerializer(TestSerializer.class));
        verify(mockParamConfigBuilder1).setSerializer(TestSerializer.class);
        verify(mockParamConfigBuilder2).setSerializer(TestSerializer.class);
    }


    @Test
    public void setParamsEncodedShouldSetEncodedOnAllParamConfigs() throws Exception {
        MethodConfigBuilder toTest = newToTest();
        assertSame(toTest, toTest.setParamsEncoded(true));
        verify(mockParamConfigBuilder1).setEncoded(true);
        verify(mockParamConfigBuilder2).setEncoded(true);
    }


    @Test
    public void setParamsListSeparatorShouldSetListSeparatorOnAllParamConfigs() throws Exception {
        MethodConfigBuilder toTest = newToTest();
        assertSame(toTest, toTest.setParamsListSeparator("-"));
        verify(mockParamConfigBuilder1).setListSeparator("-");
        verify(mockParamConfigBuilder2).setListSeparator("-");
    }

    private void assertCommons(MethodConfig actual){
        assertSame(method, actual.getMethod());
        assertEquals(DefaultMethodConfig.class, actual.getClass());
    }


    private void mockEndpoint(){
        mockEndpoint(Values.ENDPOINT, "");
    }
    private void mockEndpoint(String endpoint, String path){
        mockOverride(MethodConfig.METHOD_CONFIG_DEFAULT_ENDPOINT, endpoint);
        mockStatic(RegexPathTemplate.class);
        when(RegexPathTemplate.create(endpoint + path)).thenReturn(mockRegexPathTemplate);
    }

    private void mockOverride(String name, Object value){
        when(mockCRestConfig.<Object>get(eq(name), anyObject())).thenReturn(value);
    }


    public DefaultMethodConfigBuilder newToTest() throws Exception {
        whenNew(DefaultParamConfigBuilder.class)
                .withParameterTypes(MethodConfigBuilder.class, CRestConfig.class, Registry.class, Class.class, Type.class)
                .withArguments(isA(MethodConfigBuilder.class), eq(mockCRestConfig), eq(mockClassSerializerRegistry), eq(String.class), eq(String.class))
                .thenReturn(mockParamConfigBuilder1, mockExtraParamConfigBuilder1, mockExtraParamConfigBuilder2);
        whenNew(DefaultParamConfigBuilder.class)
                .withParameterTypes(MethodConfigBuilder.class, CRestConfig.class, Registry.class, Class.class, Type.class)
                .withArguments(isA(MethodConfigBuilder.class), eq(mockCRestConfig), eq(mockClassSerializerRegistry), eq(int.class), eq(int.class))
                .thenReturn(mockParamConfigBuilder2);

        DefaultMethodConfigBuilder ret = new DefaultMethodConfigBuilder(
                interfaceConfigBuilder,
                method,
                mockCRestConfig,
                mockMimeDeserializerRegistry,
                mockClassSerializerRegistry
        );
        verifyNew(DefaultParamConfigBuilder.class)
                .withArguments(ret, mockCRestConfig, mockClassSerializerRegistry, String.class, String.class);
        verifyNew(DefaultParamConfigBuilder.class)
                .withArguments(ret, mockCRestConfig, mockClassSerializerRegistry, int.class, int.class);

        when(mockParamConfigBuilder1.build()).thenReturn(mockM1ParamConfig1);
        when(mockParamConfigBuilder2.build()).thenReturn(mockM1ParamConfig2);

        when(mockExtraParamConfigBuilder1.build()).thenReturn(mockExtraParamConfig1);
        when(mockExtraParamConfigBuilder2.build()).thenReturn(mockExtraParamConfig2);


        return ret;
    }


    public static class TestErrorHandler implements ErrorHandler {
        public <T> T handle(Request request, Exception e) throws Exception {
            return null;
        }
    }
    public static class TestRetryHandler implements RetryHandler {
        public boolean retry(RequestException exception, int attemptNumber) throws Exception {
            return false;
        }
    }
    public static class TestRequestInterceptor implements RequestInterceptor {
        public void beforeFire(Request request) throws Exception {

        }
    }
    public static class TestResponseHandler implements ResponseHandler {
        public Object handle(Response response) throws Exception {
            return null;
        }
    }
    public static class TestSerializer implements Serializer {
        public void serialize(Object value, Charset charset, OutputStream out) throws Exception {

        }
    }
    public static class TestEntityWriter implements EntityWriter {
        public void writeTo(Request request, OutputStream outputStream) throws Exception {

        }

        public String getContentType(Request request) {
            return null;
        }

        public int getContentLength(Request request) {
            return 0;
        }
    }
    public static class TestDeserializer1 implements Deserializer {
        public <T> T deserialize(Class<T> type, Type genericType, InputStream stream, Charset charset) throws Exception {
            return null;
        }
    }
    public static class TestDeserializer2 implements Deserializer {
        public <T> T deserialize(Class<T> type, Type genericType, InputStream stream, Charset charset) throws Exception {
            return null;
        }
    }
}
