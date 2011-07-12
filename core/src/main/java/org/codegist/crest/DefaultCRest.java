/*
 * Copyright 2010 CodeGist.org
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 * ===================================================================
 *
 * More information at http://www.codegist.org.
 */

package org.codegist.crest;

import org.codegist.common.lang.Disposable;
import org.codegist.common.lang.Disposables;
import org.codegist.common.reflect.ObjectMethodsAwareInvocationHandler;
import org.codegist.common.reflect.ProxyFactory;
import org.codegist.crest.config.InterfaceConfig;
import org.codegist.crest.config.InterfaceConfigFactory;
import org.codegist.crest.config.MethodConfig;
import org.codegist.crest.io.Request;
import org.codegist.crest.io.RequestExecutor;
import org.codegist.crest.io.Response;
import org.codegist.crest.serializer.DeserializationManager;

import java.lang.reflect.Method;

/**
 * <p>On top of the behavior described in {@link org.codegist.crest.CRest}, this implementation adds :
 * <p>- {@link org.codegist.crest.interceptor.RequestInterceptor} to intercept any requests before it gets fired.
 * <p>- {@link org.codegist.crest.serializer.Serializer} to customize the serialization process of any types.
 * <p>- {@link org.codegist.crest.handler.ResponseHandler} to customize response handling when interface method's response type is not one of raw types.
 * <p>- {@link org.codegist.crest.handler.ErrorHandler} to customize how the created interface behaves when any error occurs during the method call process.
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class DefaultCRest extends CRest implements Disposable {

    private final ProxyFactory proxyFactory;
    private final RequestExecutor requestExecutor;
    private final InterfaceConfigFactory configFactory;
    private final DeserializationManager deserializationManager;

    public DefaultCRest(ProxyFactory proxyFactory, RequestExecutor requestExecutor, InterfaceConfigFactory configFactory, DeserializationManager deserializationManager) {
        this.proxyFactory = proxyFactory;
        this.requestExecutor = requestExecutor;
        this.configFactory = configFactory;
        this.deserializationManager = deserializationManager;
    }

    /**
     * @inheritDoc
     */
    @SuppressWarnings("unchecked")
    public <T> T build(Class<T> interfaze) throws CRestException {
        try {
            return (T) proxyFactory.createProxy(interfaze.getClassLoader(), new CRestInvocationHandler(interfaze), new Class[]{interfaze});
        } catch (Exception e) {
            throw CRestException.handle(e);
        }
    }

    class CRestInvocationHandler<T> extends ObjectMethodsAwareInvocationHandler {

        private final InterfaceConfig interfaceConfig;

        private CRestInvocationHandler(Class<T> interfaze) {
            this.interfaceConfig = configFactory.newConfig(interfaze);
        }

        @Override
        protected Object doInvoke(Object proxy, Method method, Object[] args) throws Throwable {
            MethodConfig mc = interfaceConfig.getMethodConfig(method);
            Request request = new SimpleRequest(interfaceConfig, mc, args);
            Response response = null;
            try {
                mc.getRequestInterceptor().beforeFire(request);
                response = requestExecutor.execute(request);
                return mc.getResponseHandler().handle(response);
            }catch(Exception e){
                try {
                    return mc.getErrorHandler().handle(request, e);
                } finally {
                    Disposables.dispose(response, e);
                }
            }
        }
    }

    private static class SimpleRequest implements Request {

        private static final Object[] EMPTY = new Object[0];
        private final InterfaceConfig interfaceConfig;
        private final MethodConfig methodConfig;
        private final Object[] args;

        public SimpleRequest(InterfaceConfig interfaceConfig, MethodConfig methodConfig, Object[] args) {
            this.interfaceConfig = interfaceConfig;
            this.methodConfig = methodConfig;
            this.args = args != null ? args.clone() : EMPTY;
        }

        public InterfaceConfig getInterfaceConfig(){
            return interfaceConfig;
        }

        public MethodConfig getMethodConfig() {
            return methodConfig;
        }

        public Object[] getArgs() {
            return args != null ? args.clone() : EMPTY;
        }
    }

    public void dispose() {
        Disposables.dispose(proxyFactory, requestExecutor, configFactory, deserializationManager);
    }
}
