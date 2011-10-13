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

package org.codegist.crest.util;

import org.codegist.crest.serializer.Serializer;
import org.codegist.crest.serializer.StringSerializer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayOutputStream;

import static org.codegist.crest.test.util.Values.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Serializers.class)
public class SerializersTest {
    @Test
    public void shouldSerializeToStringUsingStringSerializerSpecificMethodIfSerializerIsOfSpecificInstance() throws Exception {
        StringSerializer serializer = mock(StringSerializer.class);
        when(serializer.serialize("arg", UTF8)).thenReturn("serialized");

        String actual = Serializers.serialize(serializer, "arg", UTF8);
        assertEquals("serialized", actual);
    }

    @Test
    public void shouldSerializeToStringUsingInterfaceDefaultMethodIfNotStringSerializer() throws Exception {
        Serializer serializer = mock(Serializer.class);
        ByteArrayOutputStream mockByteArrayOutputStream = mock(ByteArrayOutputStream.class);

        whenNew(ByteArrayOutputStream.class).withNoArguments().thenReturn(mockByteArrayOutputStream);
        when(mockByteArrayOutputStream.toByteArray()).thenReturn(SOME_STRING_UTF8_BYTES);

        String actual = Serializers.serialize(serializer, "arg", UTF8);
        assertEquals(SOME_STRING, actual);

        verify(serializer).serialize("arg", UTF8, mockByteArrayOutputStream);
    }
}
