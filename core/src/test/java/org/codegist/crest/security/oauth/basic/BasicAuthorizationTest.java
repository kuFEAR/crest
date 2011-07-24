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

package org.codegist.crest.security.oauth.basic;

import org.codegist.crest.security.AuthorizationToken;
import org.codegist.crest.security.basic.BasicAuthorization;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class BasicAuthorizationTest {

    private static final String NAME = "NAME";
    private static final String PASSWORD = "PASSWORD";
    private final BasicAuthorization toTest = new BasicAuthorization(NAME, PASSWORD);

    @Test
    public void shouldGetTheSameBase64EncodedNamePwdBasicAuthentificationTokenIndifferentlyFromGivenParams(){
        AuthorizationToken actual = toTest.authorize(null,null);
        AuthorizationToken actual2 = toTest.authorize(null,null);
        assertSame(actual, actual2);
        assertEquals("Basic", actual.getName());
        assertEquals("TkFNRTpQQVNTV09SRA==", actual.getValue());
    }

    @Test(expected=UnsupportedOperationException.class)
    public void shouldThrowUnsupportedOperationExceptionWhenTryingToRefresh(){
        toTest.refresh();
    }
}
