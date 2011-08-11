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

package org.codegist.crest.serializer.jaxb;

import org.codegist.crest.CRestException;
import org.codegist.crest.test.util.Values;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.OutputStream;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Queue;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.verifyNew;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({PooledJaxb.class})
public class PooledJaxbText {
    
    private final ToMockSimpleJaxb mockJaxb1 = mock(ToMockSimpleJaxb.class);
    private final ToMockSimpleJaxb mockJaxb2 = mock(ToMockSimpleJaxb.class);
    private final long maxwait = 20;
    private final JAXBContext mockJaxbContext = mock(JAXBContext.class);

    private final PooledJaxb toTest = newToTest();


    @Test(expected= CRestException.class)
    public void shouldTryToBorrowForUnmarshallAndFailWithTimeout() throws Exception {
        Queue<Jaxb> queue = getQueue();
        assertQueueState(queue);
        queue.poll();
        queue.poll();
        toTest.unmarshal(null, null, null);
    }
    @Test(expected= CRestException.class)
    public void shouldTryToBorrowForMarshallAndFailWithTimeout() throws Exception {
        Queue<Jaxb> queue = getQueue();
        assertQueueState(queue);
        queue.poll();
        queue.poll();
        toTest.marshal(null, null, null);
    }
    @Test
    public void shouldBorrowThenUnmarshallWithGivenArgsAndFinallyLendBack() throws Exception {
        Class arg1 = String.class;
        Type arg2 = Object.class;
        Reader arg3 = mock(Reader.class);
        Object expected = new Object();
        Queue<Jaxb> queue = getQueue();

        when(mockJaxb2.unmarshal(arg1, arg2, arg3)).thenReturn(expected);

        assertQueueState(queue);
        Jaxb jaxb = queue.poll();
        Object actual = toTest.unmarshal(arg1, arg2, arg3);
        assertSame(mockJaxb1, jaxb);
        queue.offer(jaxb);
        assertQueueState(queue);
        assertSame(expected, actual);
    }

    @Test
    public void shouldBorrowThenMarshallWithGivenArgsAndFinallyLendBack() throws Exception {
        Class arg1 = String.class;
        OutputStream arg2 = mock(OutputStream.class);
        Charset arg3 = Values.UTF8;
        Queue<Jaxb> queue = getQueue();

        assertQueueState(queue);
        Jaxb jaxb = queue.poll();
        toTest.marshal(arg1, arg2, arg3);
        queue.offer(jaxb);
        assertSame(mockJaxb1, jaxb);
        assertQueueState(queue);

        verify(mockJaxb2).marshal(arg1, arg2, arg3);
    }

    public void assertQueueState(Queue<Jaxb> queue){
        assertEquals(2, queue.size());
        assertTrue(queue.containsAll(asList(mockJaxb1, mockJaxb2)));
    }

    private Queue<Jaxb> getQueue(){
        try {
            Field field = toTest.getClass().getDeclaredField("pool");
            field.setAccessible(true);
            return (Queue<Jaxb>) field.get(toTest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private PooledJaxb newToTest(){
        try {
            whenNew(SimpleJaxb.class).withArguments(mockJaxbContext).thenReturn(mockJaxb1,mockJaxb2);
            PooledJaxb toTest = new PooledJaxb(mockJaxbContext, 2, maxwait);
            verifyNew(SimpleJaxb.class, times(2)).withArguments(mockJaxbContext);
            return toTest;
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    // hack since mockito can't mock package private classes...
    public static class ToMockSimpleJaxb extends SimpleJaxb {
        public ToMockSimpleJaxb(JAXBContext jaxbContext) throws JAXBException {
            super(jaxbContext);
        }

        @Override
        public <T> void marshal(T object, OutputStream out, Charset charset) throws JAXBException {
            
        }

        @Override
        public <T> T unmarshal(Class<T> type, Type genericType, Reader reader) throws JAXBException {
            return null;
        }
    }
}
