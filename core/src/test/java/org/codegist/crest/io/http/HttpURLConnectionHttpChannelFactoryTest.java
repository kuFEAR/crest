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

import org.codegist.crest.config.MethodType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({HttpURLConnectionHttpChannelFactory.class,MethodType.class,URL.class, HttpURLConnectionHttpChannel.class})
public class HttpURLConnectionHttpChannelFactoryTest {

    private final HttpURLConnectionHttpChannelFactory toTest = new HttpURLConnectionHttpChannelFactory();

    @Test
    public void openShouldOpenAHttpURLConnectionAndInjectItIntoAHttpURLConnectionHttpChannel() throws Exception {
        URL url = PowerMockito.mock(URL.class);
        HttpURLConnection httpURLConnection = mock(HttpURLConnection.class);
        HttpURLConnectionHttpChannel expected = mock(HttpURLConnectionHttpChannel.class);
        MethodType methodType = MethodType.POST;

        whenNew(URL.class).withArguments("url").thenReturn(url);
        whenNew(HttpURLConnectionHttpChannel.class).withArguments(httpURLConnection, methodType).thenReturn(expected);
        when(url.openConnection()).thenReturn(httpURLConnection);

        HttpChannel actual = toTest.open(methodType, "url", null);
        assertSame(expected, actual);
        verify(httpURLConnection).setRequestMethod("POST");
    }


}
