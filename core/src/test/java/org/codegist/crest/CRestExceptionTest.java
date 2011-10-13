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

import org.codegist.crest.io.RequestException;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * @author laurent.gilles@codegist.org
 */
public class CRestExceptionTest {


    @Test
    public void handleAnyExceptionShouldWrapItInCRestException(){
        Throwable e = new Throwable("msg");
        RuntimeException actual = CRestException.handle(e);
        assertEquals(CRestException.class, actual.getClass());
        assertSame(e, actual.getCause());
        assertSame("msg", actual.getMessage());
    }

    @Test
    public void handleCRestExceptionShouldReturnIt(){
        CRestException e = new CRestException();
        assertSame(e, CRestException.handle(e));
    }

    @Test
    public void handleCRestExceptionThrowableShouldReturnIt(){
        Throwable e = new CRestException();
        assertSame(e, CRestException.handle(e));
    }

    @Test
    public void handleRequestExceptionWithCauseShouldHandleIt(){
        CRestException cause = new CRestException();
        RequestException e = new RequestException(cause);
        assertSame(cause, CRestException.handle(e));
    }

    @Test
    public void handleRequestExceptionThrowableWithCauseShouldHandleIt(){
        CRestException cause = new CRestException();
        Throwable e = new RequestException(cause);
        assertSame(cause, CRestException.handle(e));
    }

    @Test
    public void handleRequestExceptionWithoutCauseShouldReturnIt(){
        RequestException e = new RequestException("msg", null);
        RuntimeException actual = CRestException.handle(e);
        assertEquals(CRestException.class, actual.getClass());
        assertSame(e, actual.getCause());
        assertSame("msg", actual.getMessage());
    }

    @Test
    public void handleRequestExceptionThrowableWithoutCauseShouldReturnIt(){
        Throwable e = new RequestException("msg", null);
        RuntimeException actual = CRestException.handle(e);
        assertEquals(CRestException.class, actual.getClass());
        assertSame(e, actual.getCause());
        assertSame("msg", actual.getMessage());
    }

    @Test
    public void handleIllegalArgumentExceptionShouldReturnIt(){
        IllegalArgumentException e = new IllegalArgumentException();
        assertSame(e, CRestException.handle(e));
    }

    @Test
    public void handleIllegalArgumentExceptionThrowableShouldReturnIt(){
        Throwable e = new IllegalArgumentException();
        assertSame(e, CRestException.handle(e));
    }

    @Test
    public void handleIllegalStateExceptionShouldReturnIt(){
        IllegalStateException e = new IllegalStateException();
        assertSame(e, CRestException.handle(e));
    }

    @Test
    public void handleIllegalStateExceptionThrowableShouldReturnIt(){
        Throwable e = new IllegalStateException();
        assertSame(e, CRestException.handle(e));
    }

    @Test
    public void handleInvocationTargetExceptionShouldHandleCause(){
        CRestException expected = new CRestException();
        RequestException cause = new RequestException(expected);
        InvocationTargetException e = new InvocationTargetException(cause);
        assertSame(expected, CRestException.handle(e));
    }

    @Test
    public void handleInvocationTargetExceptionThrowableShouldHandleCause(){
        CRestException expected = new CRestException();
        RequestException cause = new RequestException(expected);
        Throwable e = new InvocationTargetException(cause);
        assertSame(expected, CRestException.handle(e));
    }
}
