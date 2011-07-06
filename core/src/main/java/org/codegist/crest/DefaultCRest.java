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
import org.codegist.crest.config.ParamConfig;
import org.codegist.crest.handler.RetryHandler;
import org.codegist.crest.http.HttpException;
import org.codegist.crest.http.HttpRequest;
import org.codegist.crest.http.HttpRequestExecutor;
import org.codegist.crest.serializer.DeserializationManager;

import java.lang.reflect.Method;
import java.net.URISyntaxException;

import static org.codegist.crest.util.Params.isNull;

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
    private final HttpRequestExecutor httpRequestExecutor;
    private final InterfaceConfigFactory configFactory;
    private final DeserializationManager deserializationManager;

    public DefaultCRest(ProxyFactory proxyFactory, HttpRequestExecutor httpRequestExecutor, InterfaceConfigFactory configFactory, DeserializationManager deserializationManager) {
        this.proxyFactory = proxyFactory;
        this.httpRequestExecutor = httpRequestExecutor;
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
            try {
                return doInvoke(method, args);
            } catch (Throwable e) {
                return CRestException.handle(e);
            }
        }

        private Object doInvoke(Method method, Object[] args) throws Throwable {
            MethodConfig mc = interfaceConfig.getMethodConfig(method);
            RequestContext requestContext = new DefaultRequestContext(interfaceConfig, method, args);
            ResponseContext responseContext;

            try {
                responseContext = fire(requestContext);
            }catch(Exception e){
                try {
                    return mc.getErrorHandler().handle(requestContext, e);
                } finally {
                    Disposables.dispose(e);
                }
            }

            try {
                return mc.getResponseHandler().handle(responseContext);
            } catch (Exception e) {
                Disposables.dispose(responseContext.getResponse());
                throw CRestException.handle(e);
            }
        }

        private ResponseContext fire(RequestContext requestContext) throws Exception {
            RetryHandler retryHandler = requestContext.getMethodConfig().getRetryHandler();
            int attemptCount = 0;
            ResponseContext responseContext = null;
            Exception exception = null;
            do {
                if(exception != null && exception instanceof HttpException) {
                    ((HttpException) exception).close();
                }
                exception = null;
                // build the request, can throw exception but that should not be part of the retry policy
                HttpRequest request = buildRequest(requestContext);
                try {
                    // doInvoke the request
                    responseContext = new DefaultResponseContext(deserializationManager, requestContext, httpRequestExecutor.execute(request));
                } catch (Exception e) {
                    exception = e;
                }
                // loop until an exception has been thrown and the retry handle ask for retry
            }while(responseContext == null && retryHandler.retry(requestContext, exception, ++attemptCount));

            if (exception != null) {
                throw exception;
            } else {
                return responseContext;
            }
        }

        /**
         *
         * @param requestContext
         * @return
         * @throws URISyntaxException
         */
        private HttpRequest buildRequest(RequestContext requestContext) throws Exception {
            MethodConfig mc = requestContext.getMethodConfig();

            // Build base request
            HttpRequest.Builder builder = new HttpRequest.Builder(mc.getPathTemplate(), mc.getBodyWriter(), interfaceConfig.getEncoding())
                    .timeoutSocketAfter(mc.getSocketTimeout())
                    .timeoutConnectionAfter(mc.getConnectionTimeout())
                    .ofContentType(mc.getContentType())
                    .thatAccepts(mc.getAccept())
                    .withAction(mc.getHttpMethod())
                    .withParams(mc.getExtraParams());

            for (int i = 0; i < mc.getParamCount(); i++) {
                Object value = requestContext.getValue(i);
                ParamConfig pc = mc.getParamConfig(i);
                if(isNull(value) && pc.getDefaultValue() == null) {
                    continue;
                }
                builder.addParam(mc.getParamConfig(i), value);
            }

            // Notify interceptor
            mc.getRequestInterceptor().beforeFire(builder, requestContext);

            return builder.build();
        }
    }


    public void dispose() {
        Disposables.dispose(proxyFactory);
        Disposables.dispose(httpRequestExecutor);
        Disposables.dispose(configFactory);
        Disposables.dispose(deserializationManager);
    }
}
