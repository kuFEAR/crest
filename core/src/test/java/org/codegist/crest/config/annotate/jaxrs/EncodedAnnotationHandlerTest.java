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

package org.codegist.crest.config.annotate.jaxrs;

import org.codegist.crest.config.annotate.AnnotationBaseTest;
import org.codegist.crest.config.annotate.AnnotationHandler;
import org.junit.Test;

import javax.ws.rs.Encoded;

import static org.mockito.Mockito.verify;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class EncodedAnnotationHandlerTest extends AnnotationBaseTest<Encoded> {

    private final EncodedAnnotationHandler toTest = new EncodedAnnotationHandler();

    public EncodedAnnotationHandlerTest() {
        super(Encoded.class);
    }


    @Test
    public void handleInterfaceAnnotationShouldSetParamsEncoded() throws Exception {
        toTest.handleInterfaceAnnotation(mockAnnotation, mockInterfaceConfigBuilder);
        verify(mockInterfaceConfigBuilder).setParamsEncoded(true);
    }


    @Test
    public void handleMethodAnnotationShouldSetParamsEncoded() throws Exception {
        toTest.handleMethodAnnotation(mockAnnotation, mockMethodConfigBuilder);
        verify(mockMethodConfigBuilder).setParamsEncoded(true);
    }


    @Test
    public void handleParamsAnnotationShouldSetParamsEncoded() throws Exception {
        toTest.handleParameterAnnotation(mockAnnotation, mockParamConfigBuilder);
        verify(mockParamConfigBuilder).setEncoded(true);
    }

    @Override
    public AnnotationHandler<Encoded> getToTest() {
        return toTest;
    }
}
