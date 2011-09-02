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
import org.codegist.crest.handler.ErrorHandler;
import org.codegist.crest.handler.ResponseHandler;
import org.codegist.crest.handler.RetryHandler;
import org.codegist.crest.interceptor.RequestInterceptor;
import org.codegist.crest.serializer.Deserializer;
import org.codegist.crest.serializer.Serializer;
import org.codegist.crest.test.util.Classes;
import org.codegist.crest.test.util.TestInterface;
import org.codegist.crest.test.util.Values;
import org.codegist.crest.util.Registry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static org.codegist.crest.test.util.TestInterface.M1;
import static org.codegist.crest.test.util.TestInterface.M2;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.verifyNew;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * @author laurent.gilles@codegist.org
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Registry.class, DefaultMethodConfigBuilder.class, DefaultInterfaceConfig.class, DefaultInterfaceConfigBuilder.class})
public class DefaultInterfaceConfigBuilderTest {

    private final DefaultMethodConfigBuilder mockM1MethodConfigBuilder = mock(DefaultMethodConfigBuilder.class);
    private final DefaultMethodConfigBuilder mockM2MethodConfigBuilder = mock(DefaultMethodConfigBuilder.class);

    private final Class interfaze = TestInterface.class;
    private final CRestConfig mockCRestConfig = mock(CRestConfig.class);
    private final Registry<String, Deserializer> mockMimeDeserializerRegistry = mock(Registry.class);
    private final Registry<Class<?>, Serializer> mockClassSerializerRegistry = mock(Registry.class);

    private final DefaultInterfaceConfigBuilder toTest;


    @Test
    public void buildShouldBuildEveryMethodConfigAndReturnAnInterfaceConfigWithThem() throws Exception {
        DefaultInterfaceConfig expected = mock(DefaultInterfaceConfig.class);
        MethodConfig mockMethodConfigM1 = mock(MethodConfig.class);
        MethodConfig mockMethodConfigM2 = mock(MethodConfig.class);
        Map<Method, MethodConfig> expectedMethodConfigs = new HashMap<Method, MethodConfig>();
        expectedMethodConfigs.put(M1, mockMethodConfigM1);
        expectedMethodConfigs.put(M2, mockMethodConfigM2);

        when(mockM1MethodConfigBuilder.build()).thenReturn(mockMethodConfigM1);
        when(mockM2MethodConfigBuilder.build()).thenReturn(mockMethodConfigM2);
        whenNew(DefaultInterfaceConfig.class).withArguments( interfaze, expectedMethodConfigs).thenReturn(expected);

        InterfaceConfig actual = toTest.build();
        assertSame(expected, actual);
    }

    @Test
    public void startMethodConfigShouldGetTheConfigBuilderForTheGivenMethod(){
        assertSame(mockM1MethodConfigBuilder, toTest.startMethodConfig(M1));
        assertSame(mockM2MethodConfigBuilder, toTest.startMethodConfig(M2));
        assertNull(toTest.startMethodConfig(Classes.byName(Object.class, "toString")));
    }

    @Test
    public void setMethodsCharsetShouldSetCharsetOnAllMethodConfigs(){
        assertSame(toTest, toTest.setMethodsCharset(Values.ISO_8859_1));
        verify(mockM1MethodConfigBuilder).setCharset(Values.ISO_8859_1);
        verify(mockM2MethodConfigBuilder).setCharset(Values.ISO_8859_1);
    }

    @Test
    public void setMethodsSocketTimeoutShouldSetSocketTimeoutOnAllMethodConfigs(){
        assertSame(toTest, toTest.setMethodsSocketTimeout(10));
        verify(mockM1MethodConfigBuilder).setSocketTimeout(10);
        verify(mockM2MethodConfigBuilder).setSocketTimeout(10);
    }

    @Test
    public void setMethodsConnectionTimeoutShouldSetConnectionTimeoutOnAllMethodConfigs(){
        assertSame(toTest, toTest.setMethodsConnectionTimeout(10));
        verify(mockM1MethodConfigBuilder).setConnectionTimeout(10);
        verify(mockM2MethodConfigBuilder).setConnectionTimeout(10);
    }

    @Test
    public void setMethodsRequestInterceptorShouldSetRequestInterceptorOnAllMethodConfigs(){
        assertSame(toTest, toTest.setMethodsRequestInterceptor(RequestInterceptor.class));
        verify(mockM1MethodConfigBuilder).setRequestInterceptor(RequestInterceptor.class);
        verify(mockM2MethodConfigBuilder).setRequestInterceptor(RequestInterceptor.class);
    }

    @Test
    public void setMethodsResponseHandlerShouldSetResponseHandlerOnAllMethodConfigs(){
        assertSame(toTest, toTest.setMethodsResponseHandler(ResponseHandler.class));
        verify(mockM1MethodConfigBuilder).setResponseHandler(ResponseHandler.class);
        verify(mockM2MethodConfigBuilder).setResponseHandler(ResponseHandler.class);
    }

    @Test
    public void setMethodsErrorHandlerShouldSetErrorHandlerOnAllMethodConfigs(){
        assertSame(toTest, toTest.setMethodsErrorHandler(ErrorHandler.class));
        verify(mockM1MethodConfigBuilder).setErrorHandler(ErrorHandler.class);
        verify(mockM2MethodConfigBuilder).setErrorHandler(ErrorHandler.class);
    }

    @Test
    public void setMethodsRetryHandlerShouldSetRetryHandlerOnAllMethodConfigs(){
        assertSame(toTest, toTest.setMethodsRetryHandler(RetryHandler.class));
        verify(mockM1MethodConfigBuilder).setRetryHandler(RetryHandler.class);
        verify(mockM2MethodConfigBuilder).setRetryHandler(RetryHandler.class);
    }

    @Test
    public void setMethodsEntityWriterShouldSetEntityWriterOnAllMethodConfigs(){
        assertSame(toTest, toTest.setMethodsEntityWriter(EntityWriter.class));
        verify(mockM1MethodConfigBuilder).setEntityWriter(EntityWriter.class);
        verify(mockM2MethodConfigBuilder).setEntityWriter(EntityWriter.class);
    }


    @Test
    public void setMethodsDeserializerShouldSetDeserializerOnAllMethodConfigs(){
        assertSame(toTest, toTest.setMethodsDeserializer(Deserializer.class));
        verify(mockM1MethodConfigBuilder).setDeserializer(Deserializer.class);
        verify(mockM2MethodConfigBuilder).setDeserializer(Deserializer.class);
    }

    @Test
    public void setMethodsConsumesShouldSetConsumesOnAllMethodConfigs(){
        assertSame(toTest, toTest.setMethodsConsumes("a", "b"));
        verify(mockM1MethodConfigBuilder).setConsumes("a", "b");
        verify(mockM2MethodConfigBuilder).setConsumes("a", "b");
    }

    @Test
    public void setMethodsProducesShouldSetProducesOnAllMethodConfigs(){
        assertSame(toTest, toTest.setMethodsProduces("a"));
        verify(mockM1MethodConfigBuilder).setProduces("a");
        verify(mockM2MethodConfigBuilder).setProduces("a");
    }

    @Test
    public void setMethodsTypeShouldSetTypeOnAllMethodConfigs(){
        assertSame(toTest, toTest.setMethodsType(MethodType.getDefault()));
        verify(mockM1MethodConfigBuilder).setType(MethodType.getDefault());
        verify(mockM2MethodConfigBuilder).setType(MethodType.getDefault());
    }

    @Test
    public void appendMethodsPathShouldAppendPathOnAllMethodConfigs(){
        assertSame(toTest, toTest.appendMethodsPath("a"));
        verify(mockM1MethodConfigBuilder).appendPath("a");
        verify(mockM2MethodConfigBuilder).appendPath("a");
    }

    @Test
    public void setMethodsEndPointShouldSetEndPointOnAllMethodConfigs(){
        assertSame(toTest, toTest.setMethodsEndPoint("a"));
        verify(mockM1MethodConfigBuilder).setEndPoint("a");
        verify(mockM2MethodConfigBuilder).setEndPoint("a");
    }

    @Test
    public void setParamsSerializerShouldSetParamsSerializerOnAllMethodConfigs(){
        assertSame(toTest, toTest.setParamsSerializer(Serializer.class));
        verify(mockM1MethodConfigBuilder).setParamsSerializer(Serializer.class);
        verify(mockM2MethodConfigBuilder).setParamsSerializer(Serializer.class);
    }

    @Test
    public void setParamsEncodedShouldSetParamsEncodedOnAllMethodConfigs(){
        assertSame(toTest, toTest.setParamsEncoded(true));
        verify(mockM1MethodConfigBuilder).setParamsEncoded(true);
        verify(mockM2MethodConfigBuilder).setParamsEncoded(true);
    }

    @Test
    public void setParamsListSeparatorShouldSetParamsListSeparatorOnAllMethodConfigs(){
        assertSame(toTest, toTest.setParamsListSeparator("a"));
        verify(mockM1MethodConfigBuilder).setParamsListSeparator("a");
        verify(mockM2MethodConfigBuilder).setParamsListSeparator("a");
    }

    @Test
    public void startMethodsExtraParamConfigShouldReturnACompositeParamConfigBuilderOverAllMethodConfigExtraParamConfigBuilders() throws Exception {

        ParamConfigBuilder mockParamConfigBuilderM1 = mock(ParamConfigBuilder.class);
        ParamConfigBuilder mockParamConfigBuilderM2 = mock(ParamConfigBuilder.class);
        ParamConfigBuilder[] expectedBuilders = {mockParamConfigBuilderM1,mockParamConfigBuilderM2};

        when(mockM1MethodConfigBuilder.startExtraParamConfig()).thenReturn(mockParamConfigBuilderM1);
        when(mockM2MethodConfigBuilder.startExtraParamConfig()).thenReturn(mockParamConfigBuilderM2);

        ParamConfigBuilder actual = toTest.startMethodsExtraParamConfig();

        assertEquals(DefaultInterfaceConfigBuilder.CompositeParamConfigBuilder.class, actual.getClass());
        assertArrayEquals(expectedBuilders, Classes.<ParamConfigBuilder[]>getFieldValue(actual, "builders"));
    }

    {
        try {
            whenNew(DefaultMethodConfigBuilder.class)
                    .withArguments(isA(InterfaceConfigBuilder.class), eq(M1), eq(mockCRestConfig), eq(mockMimeDeserializerRegistry), eq(mockClassSerializerRegistry))
                    .thenReturn(mockM1MethodConfigBuilder);
            whenNew(DefaultMethodConfigBuilder.class)
                    .withArguments(isA(InterfaceConfigBuilder.class), eq(M2), eq(mockCRestConfig), eq(mockMimeDeserializerRegistry), eq(mockClassSerializerRegistry))
                    .thenReturn(mockM2MethodConfigBuilder);
            toTest = new DefaultInterfaceConfigBuilder(
                    interfaze,
                    mockCRestConfig,
                    mockMimeDeserializerRegistry,
                    mockClassSerializerRegistry
            );
            verifyNew(DefaultMethodConfigBuilder.class)
                    .withArguments(toTest, M1, mockCRestConfig, mockMimeDeserializerRegistry, mockClassSerializerRegistry);
            verifyNew(DefaultMethodConfigBuilder.class)
                    .withArguments(toTest, M2, mockCRestConfig, mockMimeDeserializerRegistry, mockClassSerializerRegistry);
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }

    }


}
