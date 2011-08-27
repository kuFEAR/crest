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
import org.codegist.crest.util.Placeholders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Map;
import java.util.regex.Pattern;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * @author laurent.gilles@codegist.org
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Placeholders.class})
public class StringBasedAnnotationHandlerTest {

    private final Map<Pattern,String> placeholders = mock(Map.class);
    private final CRestConfig mockCRestConfig = mock(CRestConfig.class);
    {
        when(mockCRestConfig.get(CRestConfig.class.getName() + "#placeholders")).thenReturn(placeholders);
    }
    private final StringBasedAnnotationHandler toTest = new StringBasedAnnotationHandler(mockCRestConfig){};


    @Test
    public void phsShouldUsePlaceholderMap(){
        mockStatic(Placeholders.class);
        when(Placeholders.merge(placeholders, "a")).thenReturn("aa");
        when(Placeholders.merge(placeholders, "b")).thenReturn("bb");
        String[] actual = toTest.ph("a", "b");
        assertArrayEquals(new String[]{"aa", "bb"}, actual);
    }

    @Test
    public void phShouldUsePlaceholderMap(){
        mockStatic(Placeholders.class);
        when(Placeholders.merge(placeholders, "a")).thenReturn("aa");
        String actual = toTest.ph("a");
        assertEquals("aa", actual);
    }
}
