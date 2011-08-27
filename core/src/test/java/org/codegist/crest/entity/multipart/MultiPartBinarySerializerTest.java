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

package org.codegist.crest.entity.multipart;

import org.codegist.crest.config.ParamConfig;
import org.codegist.crest.param.Param;
import org.codegist.crest.serializer.Serializer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import static org.codegist.crest.test.util.Values.UTF8;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

/**
 * @author laurent.gilles@codegist.org
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({MultiPartBinarySerializer.class})
public class MultiPartBinarySerializerTest {

    private final MultiPartBinarySerializer toTest = MultiPartBinarySerializer.INSTANCE;

    @Test
    public void serializeShouldUseGetSerializerAndDelegateToSerialize() throws Exception {
        OutputStream out = mock(OutputStream.class);
        Param param = mock(Param.class);
        ParamConfig paramConfig = mock(ParamConfig.class);
        when(paramConfig.getValueClass()).thenReturn((Class)String.class);
        MultiPart<Param> multiPart = new MultiPart<Param>(paramConfig,param, "boundary");
        Serializer serializer = mock(Serializer.class);
        mockStatic(MultiPartBinarySerializer.class);
        when(MultiPartBinarySerializer.getSerializer(String.class)).thenReturn(serializer);

        toTest.serialize(multiPart, UTF8, out);

        verifyStatic();
        MultiPartBinarySerializer.serialize(multiPart, UTF8, out, serializer);
    }


    @Test
    public void isBinaryShouldReturnTrueForFile(){
        assertTrue(MultiPartBinarySerializer.isBinary(File.class));
    }
    
    @Test
    public void isBinaryShouldReturnTrueForInputStream(){
        assertTrue(MultiPartBinarySerializer.isBinary(InputStream.class));
    }

    @Test
    public void getSerializerShouldReturnFileSerializerForFile(){
        assertSame(MultiPartFileSerializer.INSTANCE, MultiPartBinarySerializer.getSerializer(File.class));
    }

    @Test
    public void getSerializerShouldReturnInputStreamSerializerForInputStream(){
        assertSame(MultiPartInputStreamSerializer.INSTANCE, MultiPartBinarySerializer.getSerializer(InputStream.class));
    }

    @Test
    public void serializeShouldInterateOverAllParamValuesAndSerializeThem() throws Exception {
        Serializer serializer = mock(Serializer.class);
        OutputStream out = mock(OutputStream.class);
        Param param = mock(Param.class);
        ParamConfig paramConfig = mock(ParamConfig.class);
        when(param.getValue()).thenReturn(Arrays.<Object>asList("a", "b"));
        MultiPart<Param> multiPart = new MultiPart<Param>(paramConfig,param, "boundary");


        MultiPartBinarySerializer.serialize(multiPart, UTF8, out, serializer);
        ArgumentCaptor<MultiPart> mp = ArgumentCaptor.forClass((Class)MultiPart.class);
        verify(serializer, times(2)).serialize(mp.capture(), eq(UTF8), eq(out));

        assertEquals("boundary", mp.getAllValues().get(0).getBoundary());
        assertSame(paramConfig, mp.getAllValues().get(0).getParamConfig());
        assertSame("a", mp.getAllValues().get(0).getValue());

        assertEquals("boundary", mp.getAllValues().get(1).getBoundary());
        assertSame(paramConfig, mp.getAllValues().get(1).getParamConfig());
        assertSame("b", mp.getAllValues().get(1).getValue());
    }

}
