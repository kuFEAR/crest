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

package org.codegist.crest;

import org.codegist.common.collect.Maps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({DefaultCRestConfig.class, Maps.class})
public class DefaultCRestConfigTest {

    private Map<String,Object> config = mock(Map.class);
    private final DefaultCRestConfig toTest = new DefaultCRestConfig(config);

    @Test
    public void mergeShouldReturnNewInstanceWithMergedMap() throws Exception {
        Map map = mock(Map.class);
        Map mergedMap = mock(Map.class);
        DefaultCRestConfig expected = mock(DefaultCRestConfig.class);

        mockStatic(Maps.class);
        when(Maps.merge(config, map)).thenReturn(mergedMap);
        whenNew(DefaultCRestConfig.class).withArguments(mergedMap).thenReturn(expected);
        
        assertSame(expected, toTest.merge(map));
    }

    @Test
    public void getShouldDelegateToMap(){
        when(config.get("a")).thenReturn("b");
        assertEquals("b", toTest.get("a"));
    }

    @Test
    public void getWithDefaultShouldReturnMapValueWhenPresent(){
        when(config.containsKey("a")).thenReturn(true);
        when(config.get("a")).thenReturn("b");
        assertEquals("b", toTest.get("a", "c"));
    }

    @Test
    public void getWithDefaultShouldReturnDefaultWhenNotInMap(){
        assertEquals("c", toTest.get("b", "c"));
    }

    @Test
    public void getWithClassShouldLookForPropWithClassName(){
        when(config.containsKey(DefaultCRestConfigTest.class.getName())).thenReturn(true);
        when(config.get(DefaultCRestConfigTest.class.getName())).thenReturn("b");
        assertEquals("b", toTest.get(DefaultCRestConfigTest.class));
    }

    @Test
    public void getWithClassAndDefaultShouldLookForPropWithClassNameAndReturnIt(){
        when(config.containsKey(DefaultCRestConfigTest.class.getName())).thenReturn(true);
        when(config.get(DefaultCRestConfigTest.class.getName())).thenReturn("b");
        assertEquals("b", toTest.get(DefaultCRestConfigTest.class, "c"));
    }

    @Test
    public void getWithClassAndDefaultShouldLookForPropWithClassNameAndReturnDefault(){
        assertEquals("c", toTest.get(DefaultCRestConfigTest.class, "c"));
    }

    @Test
    public void getMaxAttemptsShouldTryToLookUpInMapAndReturnDefault(){
        assertEquals(1, toTest.getMaxAttempts());
        verify(config).containsKey(CRestConfig.CREST_MAX_ATTEMPTS);
    }

    @Test
    public void getDateFormatShouldTryToLookUpInMapAndReturnDefault(){
        assertEquals("yyyy-MM-dd'T'HH:mm:ssZ", toTest.getDateFormat());
        verify(config).containsKey(CRestConfig.CREST_DATE_FORMAT);
    }

    @Test
    public void getBooleanTrueShouldTryToLookUpInMapAndReturnDefault(){
        assertEquals("true", toTest.getBooleanTrue());
        verify(config).containsKey(CRestConfig.CREST_BOOLEAN_TRUE);
    }

    @Test
    public void getBooleanFalseShouldTryToLookUpInMapAndReturnDefault(){
        assertEquals("false", toTest.getBooleanFalse());
        verify(config).containsKey(CRestConfig.CREST_BOOLEAN_FALSE);
    }

    @Test
    public void getConcurrencyLevelShouldTryToLookUpInMapAndReturnDefault(){
        assertEquals(1, toTest.getConcurrencyLevel());
        verify(config).containsKey(CRestConfig.CREST_CONCURRENCY_LEVEL);
    }
}
