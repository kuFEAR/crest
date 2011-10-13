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

package org.codegist.crest.io.http;

import org.codegist.crest.config.MethodType;
import org.codegist.crest.security.Authorization;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Map;

import static org.codegist.crest.test.util.Values.UTF8;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({AuthorizationHttpChannelFactory.class, MethodType.class})
public class AuthorizationHttpChannelFactoryTest {

    private final HttpChannelFactory delegate = mock(HttpChannelFactory.class);
    private final Authorization authorization = mock(Authorization.class);
    private final Map<String, EntityParamExtractor> httpEntityParamExtractors = mock(Map.class);

    private final AuthorizationHttpChannelFactory toTest = new AuthorizationHttpChannelFactory(delegate,authorization,httpEntityParamExtractors);


    @Test
    public void shouldConstructAnAuthorizationHttpChannelWithGivenParams() throws Exception {
        MethodType type = mock(MethodType.class);
        HttpChannel channel = mock(HttpChannel.class);
        AuthorizationHttpChannel expected = mock(AuthorizationHttpChannel.class);

        when(delegate.open(type, "url", UTF8)).thenReturn(channel);
        whenNew(AuthorizationHttpChannel.class).withArguments(channel, authorization, type, "url", UTF8, httpEntityParamExtractors).thenReturn(expected);

        HttpChannel actual = toTest.open(type,"url", UTF8);
        assertSame(expected, actual);
    }

}
