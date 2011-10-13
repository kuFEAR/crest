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

package org.codegist.crest.entity.multipart;

import org.codegist.crest.config.MethodConfig;
import org.codegist.crest.config.ParamConfig;
import org.codegist.crest.config.ParamType;
import org.codegist.crest.io.Request;
import org.codegist.crest.param.Param;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.Arrays.asList;
import static org.codegist.crest.test.util.Values.UTF8;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author laurent.gilles@codegist.org
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({MultiPartBinarySerializer.class, MultiPartTextSerializer.class})
public class MultiPartEntityWriterTest {

    private final MultiPartBinarySerializer mockMultiPartBinarySerializer = mock(MultiPartBinarySerializer.class);
    private final MultiPartTextSerializer mockMultiPartTextSerializer = mock(MultiPartTextSerializer.class);
    private final MultiPartEntityWriter toTest = new MultiPartEntityWriter(mockMultiPartBinarySerializer, mockMultiPartTextSerializer);

    @Test
    public void getContentTypeShouldReturnMultiPartWithBoundary(){
        String actual = toTest.getContentType(null);
        assertTrue(actual.matches("multipart/form-data; boundary=[a-zA-Z0-9]{24}"));
    }

    @Test
    public void getContentLengthShouldReturnMiunsOne(){
        assertEquals(-1, toTest.getContentLength(null));
    }

    @Test
    public void writeToShouldSerializeRequestsFormParamBinariesValuesAndTextValuesWithAppropriateSerializer() throws Exception {
        Param p1 = mock(Param.class);
        Param p2 = mock(Param.class);
        Param p3 = mock(Param.class);
        ParamConfig paramConfig1 = mock(ParamConfig.class);
        ParamConfig paramConfig2 = mock(ParamConfig.class);
        ParamConfig paramConfig3 = mock(ParamConfig.class);
        List<Param> params = asList(p1,p2,p3);
        Request request = mock(Request.class);
        MethodConfig methodConfig = mock(MethodConfig.class);

        when(request.getParams(ParamType.FORM)).thenReturn(params);
        when(request.getMethodConfig()).thenReturn(methodConfig);
        when(methodConfig.getCharset()).thenReturn(UTF8);
        when(p1.getParamConfig()).thenReturn(paramConfig1);
        when(p2.getParamConfig()).thenReturn(paramConfig2);
        when(p3.getParamConfig()).thenReturn(paramConfig3);
        when(paramConfig1.getValueClass()).thenReturn((Class)String.class);
        when(paramConfig2.getValueClass()).thenReturn((Class) InputStream.class);
        when(paramConfig3.getValueClass()).thenReturn((Class) File.class);

        final AtomicReference<MultiPart<Param>> mp1 = new AtomicReference<MultiPart<Param>>();
        final AtomicReference<MultiPart<Param>> mp2 = new AtomicReference<MultiPart<Param>>();
        final AtomicReference<MultiPart<Param>> mp3 = new AtomicReference<MultiPart<Param>>();

        ByteArrayOutputStream out = new ByteArrayOutputStream();


        doAnswer(new Answer() {
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((DataOutputStream)invocation.getArguments()[2]).writeBytes("t1\r\n");
                mp1.set((MultiPart)invocation.getArguments()[0]);
                return null;
            }
        }).when(mockMultiPartTextSerializer).serialize(isA(MultiPart.class), eq(UTF8), isA(DataOutputStream.class));
        doAnswer(new Answer() {
            int i = 0;
            public Object answer(InvocationOnMock invocation) throws Throwable {
                if(i++ == 0) {
                    ((DataOutputStream)invocation.getArguments()[2]).writeBytes("b1\r\n");
                    mp2.set((MultiPart)invocation.getArguments()[0]);
                }else{
                    ((DataOutputStream)invocation.getArguments()[2]).writeBytes("b2\r\n");
                    mp3.set((MultiPart)invocation.getArguments()[0]);
                }
                return null;
            }
        }).when(mockMultiPartBinarySerializer).serialize(isA(MultiPart.class), eq(UTF8), isA(DataOutputStream.class));


        toTest.writeTo(request, out);
        assertEquals("t1\r\n" +
                "b1\r\n" +
                "b2\r\n" +
                "--"+mp2.get().getBoundary()+"--\r\n\r\n", out.toString());

        assertEquals(mp1.get().getBoundary(), mp2.get().getBoundary());
        assertEquals(mp1.get().getBoundary(), mp3.get().getBoundary());
        assertTrue(mp1.get().getBoundary().matches("[a-zA-Z0-9]{24}"));

        assertSame(paramConfig1, mp1.get().getParamConfig());
        assertSame(p1, mp1.get().getValue());

        assertSame(paramConfig2, mp2.get().getParamConfig());
        assertSame(p2, mp2.get().getValue());

        assertSame(paramConfig3, mp3.get().getParamConfig());
        assertSame(p3, mp3.get().getValue());
    }
}
