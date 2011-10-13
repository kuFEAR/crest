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

package org.codegist.crest.serializer;

import org.codegist.crest.CRestConfig;
import org.codegist.crest.test.util.CRestConfigs;
import org.codegist.crest.test.util.Values;
import org.junit.After;
import org.junit.Test;

import java.text.ParseException;
import java.util.Date;

import static org.codegist.crest.test.util.Values.DATE_STR_DEFAULT_FORMAT;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class DateDeserializerTest extends BaseDeserializerTest {

    private final CRestConfig mockCRestConfig = CRestConfigs.mockDefaultBehavior();
    private final TypeDeserializer<Date> toTest = new DateDeserializer(mockCRestConfig);

    @Test
    public void shouldDeserializeDateStringToDateUsingDefaultDateFormat() throws Exception {
        assertEquals(Values.DATE, deserialize(toTest, DATE_STR_DEFAULT_FORMAT));
    }
    @Test(expected= ParseException.class)
    public void shouldDeserializeAnyThrowException() throws Exception {
        deserialize(toTest,"nusdfsdfl");
    }
    @Test(expected= NullPointerException.class)
    public void shouldDeserializeNullThrowException() throws Exception {
        deserialize(toTest, null);
    }

    @After
    public void verifyCRestConfig(){
        verify(mockCRestConfig).getDateFormat();
    }
}
