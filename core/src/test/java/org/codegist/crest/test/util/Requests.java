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

package org.codegist.crest.test.util;

import org.codegist.crest.config.MethodConfig;
import org.codegist.crest.entity.EntityWriter;
import org.codegist.crest.io.Request;

import java.lang.reflect.Method;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author laurent.gilles@codegist.org
 */
public class Requests {

    public static Request mockWith(Method method){
        Request mockRequest = mock(Request.class);
        MethodConfig mockMethodConfig = mock(MethodConfig.class);

        when(mockRequest.getMethodConfig()).thenReturn(mockMethodConfig);
        when(mockMethodConfig.getMethod()).thenReturn(method);

        return mockRequest;
    }
    public static Request mockWith(EntityWriter mockWriter){
        Request mockRequest = mock(Request.class);
        MethodConfig mockMethodConfig = mock(MethodConfig.class);

        when(mockRequest.getMethodConfig()).thenReturn(mockMethodConfig);
        when(mockMethodConfig.getEntityWriter()).thenReturn(mockWriter);

        return mockRequest;
    }
}
