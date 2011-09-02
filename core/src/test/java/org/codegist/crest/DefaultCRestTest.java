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

import org.codegist.common.lang.Disposables;
import org.codegist.common.reflect.InvocationHandler;
import org.codegist.common.reflect.ObjectMethodsAwareInvocationHandler;
import org.codegist.common.reflect.ProxyFactory;
import org.codegist.crest.config.InterfaceConfig;
import org.codegist.crest.config.InterfaceConfigFactory;
import org.codegist.crest.config.MethodConfig;
import org.codegist.crest.handler.ErrorHandler;
import org.codegist.crest.handler.ResponseHandler;
import org.codegist.crest.interceptor.RequestInterceptor;
import org.codegist.crest.io.Request;
import org.codegist.crest.io.RequestBuilderFactory;
import org.codegist.crest.io.RequestExecutor;
import org.codegist.crest.io.Response;
import org.codegist.crest.test.util.Classes;
import org.codegist.crest.util.Requests;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Method;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

/**
 * @author laurent.gilles@codegist.org
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Requests.class, Disposables.class, CRestException.class})
public class DefaultCRestTest {

    private final ProxyFactory proxyFactory = mock(ProxyFactory.class);
    private final RequestExecutor requestExecutor = mock(RequestExecutor.class);
    private final RequestBuilderFactory requestBuilderFactory = mock(RequestBuilderFactory.class);
    private final InterfaceConfigFactory configFactory = mock(InterfaceConfigFactory.class);
    private final InterfaceConfig config = mock(InterfaceConfig.class);

    private final DefaultCRest toTest = new DefaultCRest(proxyFactory, requestExecutor, requestBuilderFactory, configFactory);

    @Test
    public void buildShouldBuildAProxyWithCRestInvocationHandler() throws Exception, IllegalAccessException {
        TestInterface expected = mock(TestInterface.class);
        ArgumentCaptor<ObjectMethodsAwareInvocationHandler> objectMethodsAwareInvocationHandlerCaptor = ArgumentCaptor.forClass(ObjectMethodsAwareInvocationHandler.class);

        when(proxyFactory.createProxy(eq(TestInterface.class.getClassLoader()), objectMethodsAwareInvocationHandlerCaptor.capture(), eq(new Class[]{TestInterface.class}))).thenReturn(expected);
        when(configFactory.newConfig(TestInterface.class)).thenReturn(config);

        TestInterface actual = toTest.build(TestInterface.class);

        assertSame(expected,actual);
        assertEquals("CRestInvocationHandler", objectMethodsAwareInvocationHandlerCaptor.getValue().getClass().getSimpleName());
        assertEquals(config, Classes.getFieldValue(objectMethodsAwareInvocationHandlerCaptor.getValue(), "interfaceConfig"));
    }

    @Test
    public void buildShouldWrapInCRestExceptionWhenFailure() {
        RuntimeException e = new RuntimeException();
        RuntimeException expected = new RuntimeException();

        when(proxyFactory.createProxy(eq(TestInterface.class.getClassLoader()), isA(InvocationHandler.class), eq(new Class[]{TestInterface.class}))).thenThrow(e);
        mockStatic(CRestException.class);
        when(CRestException.handle(e)).thenReturn(expected);

        try {
            toTest.build(TestInterface.class);
            fail();
        } catch (Exception e1) {
            assertSame(expected, e1);
        }
    }



    @Test
    public void crestInvocationHandlerInvokeBuildARequestExecuteItAndHandleResponse() throws Throwable {
        Object expected = new Object();
        Object[] args = new Object[0];
        MethodConfig methodConfig = mock(MethodConfig.class);
        Request request = mock(Request.class);
        Response response = mock(Response.class);
        RequestInterceptor requestInterceptor = mock(RequestInterceptor.class);
        ResponseHandler responseHandler= mock(ResponseHandler.class);

        when(methodConfig.getRequestInterceptor()).thenReturn(requestInterceptor);
        when(methodConfig.getResponseHandler()).thenReturn(responseHandler);
        when(config.getMethodConfig(TestInterface.GET)).thenReturn(methodConfig);
        mockStatic(Requests.class);
        when(Requests.from(requestBuilderFactory, methodConfig, args)).thenReturn(request);
        when(requestExecutor.execute(request)).thenReturn(response);
        when(responseHandler.handle(response)).thenReturn(expected);

        DefaultCRest.CRestInvocationHandler toTest = new DefaultCRest(proxyFactory, requestExecutor, requestBuilderFactory, configFactory).new CRestInvocationHandler(config);

        Object actual = toTest.doInvoke(null, TestInterface.GET, args);
        assertSame(expected, actual);
        verify(requestInterceptor).beforeFire(request);
    }

    @Test
    public void crestInvocationHandlerInvokeBuildARequestExecuteItAndHandleError() throws Throwable {
        Object expected = new Object();
        Object[] args = new Object[0];
        MethodConfig methodConfig = mock(MethodConfig.class);
        Request request = mock(Request.class);
        RequestInterceptor requestInterceptor = mock(RequestInterceptor.class);
        ErrorHandler errorHandler = mock(ErrorHandler.class);
        Exception e = new Exception();

        when(methodConfig.getRequestInterceptor()).thenReturn(requestInterceptor);
        when(methodConfig.getErrorHandler()).thenReturn(errorHandler);
        when(config.getMethodConfig(TestInterface.GET)).thenReturn(methodConfig);
        mockStatic(Requests.class);
        when(Requests.from(requestBuilderFactory, methodConfig, args)).thenReturn(request);
        when(requestExecutor.execute(request)).thenThrow(e);
        when(errorHandler.handle(request, e)).thenReturn(expected);


        DefaultCRest.CRestInvocationHandler toTest = new DefaultCRest(proxyFactory, requestExecutor, requestBuilderFactory, configFactory).new CRestInvocationHandler(config);

        Object actual = toTest.doInvoke(null, TestInterface.GET, args);
        assertSame(expected, actual);
        verify(requestInterceptor).beforeFire(request);
        verifyStatic();
        Disposables.dispose(null, e);
    }

    public interface TestInterface {
        void get();
        Method GET = Classes.byName(TestInterface.class, "get");
    }
}
