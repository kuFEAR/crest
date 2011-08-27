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

package org.codegist.crest.io.http;

import org.codegist.common.log.Logger;
import org.codegist.common.log.LoggingOutputStream;
import org.codegist.crest.CRestException;
import org.codegist.crest.entity.EntityWriter;
import org.codegist.crest.io.Request;
import org.codegist.crest.test.util.Requests;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * @author laurent.gilles@codegist.org
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({RequestEntityWriter.class,CRestException.class})
public class RequestEntityWriterTest {

    private final EntityWriter mockEntityWriter = mock(EntityWriter.class);
    private final Request mockRequest = Requests.mockWith(mockEntityWriter);
    private final RequestEntityWriter toTest = new RequestEntityWriter(mockRequest);

    @Test
    public void getContentLengthShouldReturnEntityWriterContentLength(){
        when(mockEntityWriter.getContentLength(mockRequest)).thenReturn(10);
        assertEquals(10, toTest.getContentLength());
    }

    @Test
    public void writeEntityToShouldWriteEntityToGivenOutputStream() throws Exception {
        org.apache.log4j.Logger.getLogger(Request.class).setLevel(org.apache.log4j.Level.INFO);
        OutputStream out = mock(OutputStream.class);
        toTest.writeEntityTo(out);
        verify(mockEntityWriter).writeTo(mockRequest, out);
        verify(out).flush();
    }

    @Test
    public void writeEntityToWrapAnyExceptionThrownByWriter() throws Exception {
        OutputStream out = mock(OutputStream.class);
        Exception thrown = new Exception();
        RuntimeException expected = new RuntimeException();
        doThrow(thrown).when(mockEntityWriter).writeTo(mockRequest, out);
        mockStatic(CRestException.class);
        when(CRestException.handle(thrown)).thenReturn(expected);
        try {
            toTest.writeEntityTo(out);
        } catch (Exception e) {
            assertSame(expected, e);
        }
    }

    @Test
    public void writeEntityToShouldWrapOutputStreamIntoLoggingIfTraceIsEnabled() throws Exception {
        OutputStream out = mock(OutputStream.class);
        LoggingOutputStream mockLoggingOutputStream = mock(LoggingOutputStream.class);

        org.apache.log4j.Logger.getLogger(Request.class).setLevel(org.apache.log4j.Level.TRACE);

        whenNew(LoggingOutputStream.class).withArguments(out, Logger.getLogger(Request.class)).thenReturn(mockLoggingOutputStream);

        toTest.writeEntityTo(out);

        verify(mockEntityWriter).writeTo(mockRequest, mockLoggingOutputStream);
        verify(mockLoggingOutputStream).flush();
        org.apache.log4j.Logger.getLogger(Request.class).setLevel(org.apache.log4j.Level.INFO);
    }

}
