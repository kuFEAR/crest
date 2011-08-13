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

import org.codegist.crest.config.annotate.ParamOnlyAnnotationBaseTest;
import org.junit.Test;

import javax.ws.rs.CookieParam;

import static org.codegist.crest.config.ParamType.COOKIE;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class CookieParamAnnotationHandlerTest extends ParamOnlyAnnotationBaseTest<CookieParam> {
    
    public CookieParamAnnotationHandlerTest() {
        super(CookieParam.class, new CookieParamAnnotationHandler());
    }

    @Test
    public void handleParameterAnnotationShouldFillNameWithAnnotationValue() throws Exception {
        when(mockAnnotation.value()).thenReturn("a");
        when(mockParamConfigBuilder.setType(COOKIE)).thenReturn(mockParamConfigBuilder);
        when(mockParamConfigBuilder.setName("a")).thenReturn(mockParamConfigBuilder);
        toTest.handleParameterAnnotation(mockAnnotation, mockParamConfigBuilder);
        verify(mockAnnotation).value();
        verify(mockParamConfigBuilder).setType(COOKIE);
        verify(mockParamConfigBuilder).setName("a");
    }

}
