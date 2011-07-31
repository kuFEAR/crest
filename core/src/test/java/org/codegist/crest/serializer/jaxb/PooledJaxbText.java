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

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import sun.misc.ExtensionInstallationException;

import javax.print.attribute.standard.Chromaticity;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import java.io.OutputStream;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.verifyNew;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
@Ignore
@RunWith(PowerMockRunner.class)
@PrepareForTest({SimpleJaxb.class})
public class PooledJaxbText {

//    private final SimpleJaxb mockJaxb1 = mock(SimpleJaxb.class);
//    private final SimpleJaxb mockJaxb2 = mock(SimpleJaxb.class);
//    private final long maxwait = 20;
//    private final JAXBContext mockJaxbContext = mock(JAXBContext.class);
//
//    private final PooledJaxb toTest = newToTest();
//
//
//    @Test
//    public void shouldBorrowThenUnmarshallWithGivenArgsAndFinallyLendBack() throws Exception {
//        Class arg1 = String.class;
//        Type arg2 = Object.class;
//        Reader arg3 = mock(Reader.class);
//        Object expected = new Object();
//        Queue queue = getQueue();
//
//        when(mockJaxb1.unmarshal(arg1, arg2, arg3)).thenReturn(expected);
//
//        assertQueueState(queue);
//        Object actual = toTest.unmarshal(arg1, arg2, arg3);
//        assertQueueState(queue);
//        assertSame(expected, actual);
//    }
//
//    public void assertQueueState(Queue queue){
//        assertEquals(2, queue.size());
//        Iterator iter = queue.iterator();
//        assertSame(mockJaxb1, iter.next());
//        assertSame(mockJaxb2, iter.next());
//    }
//
//    private Queue getQueue(){
//        try {
//            Field field = toTest.getClass().getDeclaredField("pool");
//            field.setAccessible(true);
//            return (Queue) field.get(toTest);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private PooledJaxb newToTest(){
//        try {
//            whenNew(SimpleJaxb.class).withArguments(mockJaxbContext).thenReturn(mockJaxb1,mockJaxb2);
//            return new PooledJaxb(mockJaxbContext, 2, maxwait);
//        } catch (Exception e) {
//            throw new ExceptionInInitializerError(e);
//        }finally {
//            try {
//                verifyNew(SimpleJaxb.class).withArguments(mockJaxbContext);
//            } catch (Exception e) {
//                throw new ExceptionInInitializerError(e);
//            }
//        }
//    }
}
