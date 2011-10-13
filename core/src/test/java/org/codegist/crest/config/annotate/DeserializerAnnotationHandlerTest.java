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

package org.codegist.crest.config.annotate;

import org.codegist.crest.annotate.Deserializer;
import org.codegist.crest.serializer.StringDeserializer;
import org.junit.Test;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class DeserializerAnnotationHandlerTest extends DownToMethodAnnotationBaseTest<Deserializer> {

    private final DeserializerAnnotationHandler toTest = new DeserializerAnnotationHandler();

    public DeserializerAnnotationHandlerTest() {
        super(Deserializer.class);
    }

    @Test
    public void handleInterfaceAnnotationShouldSetMethodsDeserializer() throws Exception {
        when(mockAnnotation.value()).thenReturn((Class)StringDeserializer.class);
        toTest.handleInterfaceAnnotation(mockAnnotation, mockInterfaceConfigBuilder);
        verify(mockAnnotation).value();
        verify(mockInterfaceConfigBuilder).setMethodsDeserializer(StringDeserializer.class);
    }

    @Test
    public void handleMethodsAnnotationShouldSetDeserializer() throws Exception {
        when(mockAnnotation.value()).thenReturn((Class)StringDeserializer.class);
        toTest.handleMethodAnnotation(mockAnnotation, mockMethodConfigBuilder);
        verify(mockAnnotation).value();
        verify(mockMethodConfigBuilder).setDeserializer(StringDeserializer.class);
    }

    @Override
    public AnnotationHandler<Deserializer> getToTest() {
        return toTest;
    }
}
