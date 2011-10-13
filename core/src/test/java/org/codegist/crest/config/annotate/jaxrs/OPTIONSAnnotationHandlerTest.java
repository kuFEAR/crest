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

import org.codegist.crest.config.MethodType;
import org.codegist.crest.config.annotate.AnnotationHandler;
import org.codegist.crest.config.annotate.MethodOnlyAnnotationBaseTest;
import org.junit.Test;

import javax.ws.rs.OPTIONS;

import static org.mockito.Mockito.verify;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class OPTIONSAnnotationHandlerTest extends MethodOnlyAnnotationBaseTest<OPTIONS> {

    private final OPTIONSAnnotationHandler toTest = new OPTIONSAnnotationHandler();

    public OPTIONSAnnotationHandlerTest() {
        super(OPTIONS.class);
    }

    @Test
    public void handleMethodAnnotationShouldSetMethodTypeToOPTIONS() throws Exception {
        toTest.handleMethodAnnotation(mockAnnotation, mockMethodConfigBuilder);
        verify(mockMethodConfigBuilder).setType(MethodType.OPTIONS);
    }

    @Override
    public AnnotationHandler<OPTIONS> getToTest() {
        return toTest;
    }
}
