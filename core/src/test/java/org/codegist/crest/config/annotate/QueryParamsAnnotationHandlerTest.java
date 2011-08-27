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

import org.codegist.crest.annotate.QueryParam;
import org.codegist.crest.annotate.QueryParams;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.verifyNew;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({QueryParamsAnnotationHandler.class, QueryParamAnnotationHandler.class})
public class QueryParamsAnnotationHandlerTest extends DownToMethodAnnotationBaseTest<QueryParams> {


    private final QueryParamAnnotationHandler mockQueryParamAnnotationHandler = mock(QueryParamAnnotationHandler.class);
    private final QueryParamsAnnotationHandler toTest = new QueryParamsAnnotationHandler(mockQueryParamAnnotationHandler);
    private final QueryParam[] mockAnnotations = { mock(QueryParam.class), mock(QueryParam.class)};

    public QueryParamsAnnotationHandlerTest() {
        super(QueryParams.class);
    }   

    @Test
    public void defaultContructorShouldUseDefaultInnerHandler() throws Exception {
        whenNew(QueryParamAnnotationHandler.class).withArguments(crestConfig).thenReturn(mockQueryParamAnnotationHandler);
        new QueryParamsAnnotationHandler(crestConfig);
        verifyNew(QueryParamAnnotationHandler.class).withArguments(crestConfig);
    }

    @Test
    public void handleInterfaceAnnotationShouldDelegateToInnerHandlerForEachAnnotations() throws Exception {
        when(mockAnnotation.value()).thenReturn(mockAnnotations);

        toTest.handleInterfaceAnnotation(mockAnnotation, mockInterfaceConfigBuilder);

        verify(mockQueryParamAnnotationHandler).handleInterfaceAnnotation(mockAnnotations[0], mockInterfaceConfigBuilder);
        verify(mockQueryParamAnnotationHandler).handleInterfaceAnnotation(mockAnnotations[1], mockInterfaceConfigBuilder);
        verify(mockAnnotation).value();
    }


    @Test
    public void handleMethodAnnotationShouldDelegateToInnerHandlerForEachAnnotations() throws Exception {
        when(mockAnnotation.value()).thenReturn(mockAnnotations);

        toTest.handleMethodAnnotation(mockAnnotation, mockMethodConfigBuilder);

        verify(mockQueryParamAnnotationHandler).handleMethodAnnotation(mockAnnotations[0], mockMethodConfigBuilder);
        verify(mockQueryParamAnnotationHandler).handleMethodAnnotation(mockAnnotations[1], mockMethodConfigBuilder);
        verify(mockAnnotation).value();
    }


    @Override
    public AnnotationHandler<QueryParams> getToTest() {
        return toTest;
    }
}
