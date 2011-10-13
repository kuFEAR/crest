/*
 * Copyright 2011 CodeGist.org
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
import org.codegist.common.reflect.ObjectMethodsAwareInvocationHandler;
import org.codegist.common.reflect.ProxyFactory;
import org.codegist.crest.config.InterfaceConfig;
import org.codegist.crest.config.InterfaceConfigFactory;
import org.codegist.crest.config.MethodConfig;
import org.codegist.crest.io.Request;
import org.codegist.crest.io.RequestBuilderFactory;
import org.codegist.crest.io.RequestExecutor;
import org.codegist.crest.io.Response;
import org.codegist.crest.util.Requests;

import java.lang.reflect.Method;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
class DefaultCRest extends CRest {

    private final ProxyFactory proxyFactory;
    private final RequestExecutor requestExecutor;
    private final RequestBuilderFactory requestBuilderFactory;
    private final InterfaceConfigFactory configFactory;

    public DefaultCRest(ProxyFactory proxyFactory, RequestExecutor requestExecutor, RequestBuilderFactory requestBuilderFactory, InterfaceConfigFactory configFactory) {
        this.proxyFactory = proxyFactory;
        this.requestExecutor = requestExecutor;
        this.requestBuilderFactory = requestBuilderFactory;
        this.configFactory = configFactory;
    }

    /**
     * @inheritDoc
     */
    @SuppressWarnings("unchecked")
    public <T> T build(Class<T> interfaze) throws CRestException {
        try {
            InterfaceConfig interfaceConfig = configFactory.newConfig(interfaze);
            return (T) proxyFactory.createProxy(interfaze.getClassLoader(), new CRestInvocationHandler(interfaceConfig), new Class[]{interfaze});
        } catch (Exception e) {
            throw CRestException.handle(e);
        }
    }

    final class CRestInvocationHandler<T> extends ObjectMethodsAwareInvocationHandler {

        private final InterfaceConfig interfaceConfig;

        CRestInvocationHandler(InterfaceConfig interfaceConfig) throws Exception {
            this.interfaceConfig = interfaceConfig;
        }

        @Override
        protected Object doInvoke(Object proxy, Method method, Object[] args) throws Throwable {
            MethodConfig mc = interfaceConfig.getMethodConfig(method);
            Request request = Requests.from(requestBuilderFactory, mc, args);
            Response response = null;
            try {
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
}
