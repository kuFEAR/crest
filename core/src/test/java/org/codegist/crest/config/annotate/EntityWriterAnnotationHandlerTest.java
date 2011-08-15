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

import org.codegist.crest.annotate.EntityWriter;
import org.codegist.crest.entity.UrlEncodedFormEntityWriter;
import org.junit.Test;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class EntityWriterAnnotationHandlerTest extends DownToMethodAnnotationBaseTest<EntityWriter> {

    private final EntityWriterAnnotationHandler toTest = new EntityWriterAnnotationHandler();

    public EntityWriterAnnotationHandlerTest() {
        super(EntityWriter.class);
    }

    @Test
    public void handleInterfaceAnnotationShouldSetMethodsEntityWriter() throws Exception {
        when(mockAnnotation.value()).thenReturn((Class) UrlEncodedFormEntityWriter.class);
        toTest.handleInterfaceAnnotation(mockAnnotation, mockInterfaceConfigBuilder);
        verify(mockAnnotation).value();
        verify(mockInterfaceConfigBuilder).setMethodsEntityWriter(UrlEncodedFormEntityWriter.class);
    }

    @Test
    public void handleMethodsAnnotationShouldSetEntityWriter() throws Exception {
        when(mockAnnotation.value()).thenReturn((Class)UrlEncodedFormEntityWriter.class);
        toTest.handleMethodAnnotation(mockAnnotation, mockMethodConfigBuilder);
        verify(mockAnnotation).value();
        verify(mockMethodConfigBuilder).setEntityWriter(UrlEncodedFormEntityWriter.class);
    }

    @Override
    public AnnotationHandler<EntityWriter> getToTest() {
        return toTest;
    }
}
