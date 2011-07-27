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

package org.codegist.crest.util;

import org.codegist.crest.CRestConfig;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class CRestConfigs {

    public static CRestConfig mockDefaultBehavior(){
        return mockDefaultBehavior(mock(CRestConfig.class));
    }
    public static CRestConfig mockDefaultBehavior(CRestConfig mockCrestConfig){
        when(mockCrestConfig.getBooleanTrue()).thenReturn("true");
        when(mockCrestConfig.getBooleanFalse()).thenReturn("false");
        when(mockCrestConfig.getMaxAttempts()).thenReturn(1);
        when(mockCrestConfig.getDateFormat()).thenReturn("yyyy-MM-dd'T'HH:mm:ssZ");
        when(mockCrestConfig.getConcurrencyLevel()).thenReturn(1);
        when(mockCrestConfig.get(any(String.class), any(Object.class))).thenAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return invocation.getArguments()[1];
            }
        });
        return mockCrestConfig;
    }
}
