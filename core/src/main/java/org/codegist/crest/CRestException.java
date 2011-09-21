/*
 * Copyright 2010 CodeGist.org
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 * ===================================================================
 *
 * More information at http://www.codegist.org.
 */

package org.codegist.crest;

import org.codegist.crest.io.RequestException;

import java.lang.reflect.InvocationTargetException;

/**
 * <b>CRest</b>'s exception wrapper
 * <p>Any exception that occures while using a REST interface build by <b>CRest</b>, except for IllegalArgumentException or IllegalStateException, are wrapped into a CRestException and delegated back to the user</p>
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class CRestException extends RuntimeException {
    public CRestException() {
        super();
    }

    public CRestException(String message) {
        super(message);
    }

    public CRestException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Handles any kind of exception, wrapping it into a CRestException.
     * <p>NB: IllegalArgumentException and IllegalStateException are not wrapped.</p>
     * @param e
     * @return a wrapped CRestException or the original IllegalArgumentException or IllegalStateException
     */
    public static RuntimeException handle(Throwable e) {
        if (e instanceof CRestException) {
            return handle((CRestException) e);
        }else if (e instanceof RequestException) {
            return handle((RequestException) e);
        } else if (e instanceof IllegalArgumentException) {
            return handle((IllegalArgumentException) e);
        } else if (e instanceof IllegalStateException) {
            return handle((IllegalStateException) e);
        } else if (e instanceof InvocationTargetException) {
            return handle((InvocationTargetException) e);
        } else {
            return new CRestException(e.getMessage(), e);
        }
    }

    public static RuntimeException handle(CRestException e) {
        return e;
    }

    public static RuntimeException handle(RequestException e) {
        return e.getCause() != null ? handle(e.getCause()) : new CRestException(e.getMessage(), e);
    }

    public static RuntimeException handle(IllegalArgumentException e) {
        return e;
    }

    public static RuntimeException handle(IllegalStateException e) {
        return e;
    }

    public static RuntimeException handle(InvocationTargetException e) {
        return handle(e.getCause());
    }
}
