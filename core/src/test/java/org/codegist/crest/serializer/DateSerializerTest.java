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

package org.codegist.crest.serializer;

import org.codegist.crest.CRestConfig;
import org.codegist.crest.test.util.CRestConfigs;
import org.codegist.crest.test.util.Values;
import org.junit.After;
import org.junit.Test;

import java.util.Date;

import static org.codegist.crest.test.util.Values.DATE;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class DateSerializerTest extends BaseSerializerTest {

    private final CRestConfig mockCRestConfig = CRestConfigs.mockDefaultBehavior();
    private final Serializer<Date> toTest = new DateSerializer(mockCRestConfig);

    @Test
    public void shouldSerializeDateToDateStringUsingDefaultDateFormat() throws Exception {
        assertEquals(Values.DATE_STR_DEFAULT_FORMAT, serializeToString(toTest, DATE));
    }
    @Test(expected= Exception.class)
    public void shouldSerializeAnyThrowException() throws Exception {
        serializeToString(toTest, "abdfg");
    }
    @Test(expected= Exception.class)
    public void shouldSerializeNullThrowException() throws Exception {
        serializeToString(toTest, null);
    }

    @After
    public void verifyCRestConfig(){
        verify(mockCRestConfig).getDateFormat();
    }
}
