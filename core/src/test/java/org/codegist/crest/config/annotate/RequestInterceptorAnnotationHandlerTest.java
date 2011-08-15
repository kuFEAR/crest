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

package org.codegist.crest.config.annotate;

import org.codegist.crest.annotate.RequestInterceptor;
import org.codegist.crest.interceptor.NoOpRequestInterceptor;
import org.junit.Test;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class RequestInterceptorAnnotationHandlerTest extends DownToMethodAnnotationBaseTest<RequestInterceptor> {

    private final RequestInterceptorAnnotationHandler toTest = new RequestInterceptorAnnotationHandler();

    public RequestInterceptorAnnotationHandlerTest() {
        super(RequestInterceptor.class);
    }

    @Test
    public void handleInterfaceAnnotationShouldSetMethodsRequestInterceptor() throws Exception {
        when(mockAnnotation.value()).thenReturn((Class) NoOpRequestInterceptor.class);
        toTest.handleInterfaceAnnotation(mockAnnotation, mockInterfaceConfigBuilder);
        verify(mockAnnotation).value();
        verify(mockInterfaceConfigBuilder).setMethodsRequestInterceptor(NoOpRequestInterceptor.class);
    }

    @Test
    public void handleMethodsAnnotationShouldSetRequestInterceptor() throws Exception {
        when(mockAnnotation.value()).thenReturn((Class)NoOpRequestInterceptor.class);
        toTest.handleMethodAnnotation(mockAnnotation, mockMethodConfigBuilder);
        verify(mockAnnotation).value();
        verify(mockMethodConfigBuilder).setRequestInterceptor(NoOpRequestInterceptor.class);
    }

    @Override
    public AnnotationHandler<RequestInterceptor> getToTest() {
        return toTest;
    }
}
