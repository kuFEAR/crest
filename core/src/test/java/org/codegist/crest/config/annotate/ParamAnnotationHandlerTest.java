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

import org.codegist.crest.CRestConfig;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

/**
 * @author laurent.gilles@codegist.org
 */
public class ParamAnnotationHandlerTest {

    private final CRestConfig mockCRestConfig = mock(CRestConfig.class);
    private final ParamAnnotationHandler toTest = new ParamAnnotationHandler(mockCRestConfig){};

    @Test
    public void nullIfUnsetShouldReturnNullIfEmpty(){
         assertNull(toTest.nullIfUnset(""));
    }
    @Test
    public void nullIfUnsetShouldReturnValueIfNotEmpty(){
         assertEquals("a", toTest.nullIfUnset("a"));
    }
}
