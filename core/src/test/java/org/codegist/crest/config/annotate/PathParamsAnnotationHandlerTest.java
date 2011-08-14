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

import org.codegist.crest.annotate.CookieParam;
import org.codegist.crest.annotate.CookieParams;
import org.codegist.crest.annotate.PathParam;
import org.codegist.crest.annotate.PathParams;
import org.codegist.crest.config.InterfaceConfigBuilder;
import org.codegist.crest.config.MethodConfigBuilder;
import org.codegist.crest.config.ParamConfigBuilder;
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
@PrepareForTest({PathParamsAnnotationHandler.class})
public class PathParamsAnnotationHandlerTest extends DownToMethodAnnotationBaseTest<PathParams> {


    private final PathParamAnnotationHandler mockPathParamAnnotationHandler = mock(PublicParamAnnotationHandler.class);
    private final PathParamsAnnotationHandler toTest = new PathParamsAnnotationHandler(mockPathParamAnnotationHandler);
    private final PathParam[] mockAnnotations = { mock(PathParam.class), mock(PathParam.class)};

    public PathParamsAnnotationHandlerTest() {
        super(PathParams.class);
    }                   

    @Test
    public void defaultContructorShouldUseDefaultInnerHandler() throws Exception {
        whenNew(PathParamAnnotationHandler.class).withNoArguments().thenReturn(mockPathParamAnnotationHandler);
        new PathParamsAnnotationHandler();
        verifyNew(PathParamAnnotationHandler.class).withNoArguments();
    }

    @Test
    public void handleInterfaceAnnotationShouldDelegateToInnerHandlerForEachAnnotations() throws Exception {
        when(mockAnnotation.value()).thenReturn(mockAnnotations);

        toTest.handleInterfaceAnnotation(mockAnnotation, mockInterfaceConfigBuilder);

        verify(mockPathParamAnnotationHandler).handleInterfaceAnnotation(mockAnnotations[0], mockInterfaceConfigBuilder);
        verify(mockPathParamAnnotationHandler).handleInterfaceAnnotation(mockAnnotations[1], mockInterfaceConfigBuilder);
        verify(mockAnnotation).value();
    }


    @Test
    public void handleMethodAnnotationShouldDelegateToInnerHandlerForEachAnnotations() throws Exception {
        when(mockAnnotation.value()).thenReturn(mockAnnotations);

        toTest.handleMethodAnnotation(mockAnnotation, mockMethodConfigBuilder);

        verify(mockPathParamAnnotationHandler).handleMethodAnnotation(mockAnnotations[0], mockMethodConfigBuilder);
        verify(mockPathParamAnnotationHandler).handleMethodAnnotation(mockAnnotations[1], mockMethodConfigBuilder);
        verify(mockAnnotation).value();
    }


    @Override
    public AnnotationHandler<PathParams> getToTest() {
        return toTest;
    }

    public static class PublicParamAnnotationHandler extends PathParamAnnotationHandler{
        @Override
        public void handleInterfaceAnnotation(PathParam annotation, InterfaceConfigBuilder builder) {

        }

        @Override
        public void handleMethodAnnotation(PathParam annotation, MethodConfigBuilder builder) {

        }

        @Override
        public void handleParameterAnnotation(PathParam annotation, ParamConfigBuilder builder) {
            
        }
    }
}
