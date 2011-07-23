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

package org.codegist.crest.security.oauth.v1;

import org.junit.Test;

import java.util.Random;

import static java.lang.Long.valueOf;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class DefaultVariantProviderTest {

    private final Random mockRandom = mock(Random.class);
    private final DefaultVariantProvider toTest = new DefaultVariantProvider(mockRandom);

    @Test
    public void timestampShouldGetCurrentTimeAsSeconds(){
        long currentSeconds = System.currentTimeMillis() / 1000;
        long actual = valueOf(toTest.timestamp());
        assertTrue(currentSeconds == actual || (currentSeconds + 1) == actual);
    }

    @Test
    public void nonceShouldGetARandomNumber(){
        long rdm = 10;
        when(mockRandom.nextLong()).thenReturn(rdm);

        long currentSeconds = System.currentTimeMillis()  + rdm;
        long actual = valueOf(toTest.nonce());
        assertTrue(currentSeconds == actual || (currentSeconds + 1) == actual);

        verify(mockRandom).nextLong();
    }

}
