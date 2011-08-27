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

import org.codegist.crest.annotate.Encoding;
import org.codegist.crest.test.util.Values;
import org.junit.Test;

import java.util.regex.Pattern;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class EncodingAnnotationHandlerTest extends DownToMethodAnnotationBaseTest<Encoding> {

    private final EncodingAnnotationHandler toTest = new EncodingAnnotationHandler(crestConfig);

    public EncodingAnnotationHandlerTest() {
        super(Encoding.class);
    }

    @Test
    public void handleInterfaceAnnotationShouldSetMethodsCharset() throws Exception {
        when(mockAnnotation.value()).thenReturn(Values.ISO_8859_1_STR);
        toTest.handleInterfaceAnnotation(mockAnnotation, mockInterfaceConfigBuilder);
        verify(mockAnnotation).value();
        verify(mockInterfaceConfigBuilder).setMethodsCharset(Values.ISO_8859_1);
    }

    @Test
    public void handleInterfaceAnnotationShouldMergePlaceholdersAndSetMethodsCharset() throws Exception {
        placeholders.put(Pattern.compile("\\{" + Pattern.quote("ph.3") + "\\}"), Values.ISO_8859_1_STR);
        when(mockAnnotation.value()).thenReturn("{ph.3}");
        toTest.handleInterfaceAnnotation(mockAnnotation, mockInterfaceConfigBuilder);
        verify(mockAnnotation).value();
        verify(mockInterfaceConfigBuilder).setMethodsCharset(Values.ISO_8859_1);
    }

    @Test
    public void handleMethodsAnnotationShouldSetCharset() throws Exception {
        when(mockAnnotation.value()).thenReturn(Values.ISO_8859_1_STR);
        toTest.handleMethodAnnotation(mockAnnotation, mockMethodConfigBuilder);
        verify(mockAnnotation).value();
        verify(mockMethodConfigBuilder).setCharset(Values.ISO_8859_1);
    }

    @Test
    public void handleMethodsAnnotationShouldMergePlaceholdersAndSetCharset() throws Exception {
        placeholders.put(Pattern.compile("\\{" + Pattern.quote("ph.3") + "\\}"), Values.ISO_8859_1_STR);
        when(mockAnnotation.value()).thenReturn("{ph.3}");
        toTest.handleMethodAnnotation(mockAnnotation, mockMethodConfigBuilder);
        verify(mockAnnotation).value();
        verify(mockMethodConfigBuilder).setCharset(Values.ISO_8859_1);
    }

    @Override
    public AnnotationHandler<Encoding> getToTest() {
        return toTest;
    }
}
