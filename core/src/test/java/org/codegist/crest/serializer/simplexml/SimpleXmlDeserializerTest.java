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

package org.codegist.crest.serializer.simplexml;

import org.codegist.crest.CRestConfig;
import org.codegist.crest.util.CRestConfigs;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.simpleframework.xml.Serializer;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import static org.codegist.crest.util.Values.UTF8;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(SimpleXmlFactory.class)
public class SimpleXmlDeserializerTest {

    private final CRestConfig config = CRestConfigs.mockDefaultBehavior();

    @Test
    public void shouldUseFactoryWithGivenConfigAndDeserializeAccordingly() throws Exception {
        Serializer mock = mock(Serializer.class);
        String str = "hello";

        mockStatic(SimpleXmlFactory.class);
        when(SimpleXmlFactory.createSerializer(any(CRestConfig.class), any(Class.class))).thenReturn(mock);
        when(mock.read(any(Class.class),any(Reader.class),anyBoolean())).thenReturn(str);

        SimpleXmlDeserializer toTest = new SimpleXmlDeserializer(config);

        verify(config).get(SimpleXmlDeserializer.STRICT_PROP, true);
        verifyStatic();
        SimpleXmlFactory.createSerializer(config, SimpleXmlDeserializer.class);


        assertEquals(str, toTest.deserialize(String.class, String.class, mock(InputStream.class), UTF8));

        ArgumentCaptor<Reader> reader = ArgumentCaptor.forClass(Reader.class);
        verify(mock).read(eq(String.class), reader.capture(), eq(true));
        assertEquals(InputStreamReader.class, reader.getValue().getClass());
        assertEquals("UTF8", InputStreamReader.class.cast(reader.getValue()).getEncoding());
    }
}
