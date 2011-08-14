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

package org.codegist.crest.config.annotate.jaxrs;

import org.codegist.crest.config.annotate.AnnotationHandler;
import org.codegist.crest.config.annotate.DownToMethodAnnotationBaseTest;
import org.junit.Test;

import javax.ws.rs.Produces;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class ProducesAnnotationHandlerTest extends DownToMethodAnnotationBaseTest<Produces> {

    private final ProducesAnnotationHandler toTest = new ProducesAnnotationHandler();

    public ProducesAnnotationHandlerTest() {
        super(Produces.class);
    }


    @Test
    public void handleInterfaceAnnotationShouldSetMethodsConsumesWithGivenValue() throws Exception {
        when(mockAnnotation.value()).thenReturn(new String[]{"a","b"});
        toTest.handleInterfaceAnnotation(mockAnnotation,mockInterfaceConfigBuilder);
        verify(mockInterfaceConfigBuilder).setMethodsConsumes("a", "b");
        verify(mockAnnotation).value();
    }


    @Test
    public void handleMethodAnnotationShouldSetMethodsConsumesWithGivenValue() throws Exception {
        when(mockAnnotation.value()).thenReturn(new String[]{"a","b"});
        toTest.handleMethodAnnotation(mockAnnotation,mockMethodConfigBuilder);
        verify(mockMethodConfigBuilder).setConsumes("a", "b");
        verify(mockAnnotation).value();
    }

    @Override
    public AnnotationHandler<Produces> getToTest() {
        return toTest;
    }
}
