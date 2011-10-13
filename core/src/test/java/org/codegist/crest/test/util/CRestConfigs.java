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

import org.codegist.crest.CRestConfig;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Collections;
import java.util.Map;
import java.util.regex.Pattern;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class CRestConfigs {

    private static final String BOOL_TRUE = "true";
    private static final String BOOL_FALSE = "false";
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

    public static CRestConfig mockBehavior(String booleanTrue, String booleanFalse, String dateFormat){
       return mockBehavior(booleanTrue, booleanFalse, dateFormat, Collections.<Pattern, String>emptyMap(), mock(CRestConfig.class));
    }

    public static CRestConfig mockDefaultBehavior(){
        return mockBehavior(BOOL_TRUE, BOOL_FALSE, DATE_FORMAT, Collections.<Pattern, String>emptyMap(), mock(CRestConfig.class));
    }

    public static CRestConfig mockBehavior(String booleanTrue, String booleanFalse, String dateFormat, Map<Pattern,String> placeholders, CRestConfig mockCrestConfig){
        when(mockCrestConfig.getBooleanTrue()).thenReturn(booleanTrue);
        when(mockCrestConfig.getBooleanFalse()).thenReturn(booleanFalse);
        when(mockCrestConfig.getDateFormat()).thenReturn(dateFormat);
        when(mockCrestConfig.getMaxAttempts()).thenReturn(1);
        when(mockCrestConfig.getConcurrencyLevel()).thenReturn(1);
        when(mockCrestConfig.get(any(String.class), any(Object.class))).thenAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return invocation.getArguments()[1];
            }
        });
        when(mockCrestConfig.get(CRestConfig.class.getName() + "#placeholders")).thenReturn(placeholders);
        return mockCrestConfig;
    }

    public static CRestConfig mockDefaultBehavior(Map<Pattern,String> placeholders){
        return mockBehavior(BOOL_TRUE, BOOL_FALSE, DATE_FORMAT, placeholders, mock(CRestConfig.class));
    }

}
