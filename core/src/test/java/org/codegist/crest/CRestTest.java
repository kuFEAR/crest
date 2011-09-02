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

package org.codegist.crest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Map;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(CRest.class)
public class CRestTest {

    private final CRestBuilder builder = mock(CRestBuilder.class);
    private final CRest crest = mock(CRest.class);

    @Before
    public void setup() throws Exception {
        whenNew(CRestBuilder.class).withNoArguments().thenReturn(builder);
        when(builder.build()).thenReturn(crest);
    }

    @Test
    public void getInstanceShouldBuildACRestWithADefaultCRestBuilder(){
        assertSame(crest, CRest.getInstance());
        verify(builder).build();
        verifyNoMoreInteractions(builder);
    }

    @Test
    public void placeholderShouldReturnACRestBuilderWithPlaceholderSet(){
        CRestBuilder expected = mock(CRestBuilder.class);
        when(builder.placeholder("n","v")).thenReturn(expected);

        assertSame(expected, CRest.placeholder("n", "v"));

        verify(builder).placeholder("n", "v");
        verifyNoMoreInteractions(builder);
    }

    @Test
    public void placeholdersShouldReturnACRestBuilderWithPlaceholdersSet(){
        Map<String,String> ph = mock(Map.class);
        CRestBuilder expected = mock(CRestBuilder.class);
        when(builder.setPlaceholders(ph)).thenReturn(expected);

        assertSame(expected, CRest.placeholders(ph));

        verify(builder).setPlaceholders(ph);
        verifyNoMoreInteractions(builder);
    }

    @Test
    public void oauthShouldReturnAOAuthReadyCRestBuilder(){
        CRestBuilder expected = mock(CRestBuilder.class);
        when(builder.oauth("a","b","c","d")).thenReturn(expected);

        assertSame(expected, CRest.oauth("a","b","c","d"));

        verify(builder).oauth("a","b","c","d");
        verifyNoMoreInteractions(builder);
    }

    @Test
    public void oauthWithRefreshShouldReturnAOAuthReadyCRestBuilder(){
        CRestBuilder expected = mock(CRestBuilder.class);
        when(builder.oauth("a","b","c","d", "e", "f")).thenReturn(expected);

        assertSame(expected, CRest.oauth("a","b","c","d", "e", "f"));

        verify(builder).oauth("a","b","c","d", "e", "f");
        verifyNoMoreInteractions(builder);
    }


    @Test
    public void basicAuthShouldReturnABasicAuthReadyCRestBuilder(){
        CRestBuilder expected = mock(CRestBuilder.class);
        when(builder.basicAuth("a","b")).thenReturn(expected);

        assertSame(expected, CRest.basicAuth("a","b"));

        verify(builder).basicAuth("a","b");
        verifyNoMoreInteractions(builder);
    }







    @Test
    public void getInstanceWithPlaceholdersShouldReturnACRestWithPlaceholdersSet(){
        Map<String,String> ph = mock(Map.class);
        CRestBuilder expectedBuilder = mock(CRestBuilder.class);
        CRest expected = mock(CRest.class);
        when(builder.setPlaceholders(ph)).thenReturn(expectedBuilder);
        when(expectedBuilder.build()).thenReturn(expected);

        assertSame(expected, CRest.getInstance(ph));

        verify(builder).setPlaceholders(ph);
        verifyNoMoreInteractions(builder);
    }

    @Test
    public void getOAuthInstanceShouldReturnAOAuthReadyCRest(){
        CRestBuilder expectedBuilder = mock(CRestBuilder.class);
        CRest expected = mock(CRest.class);
        when(builder.oauth("a","b","c","d")).thenReturn(expectedBuilder);
        when(expectedBuilder.build()).thenReturn(expected);

        assertSame(expected, CRest.getOAuthInstance("a","b","c","d"));

        verify(builder).oauth("a","b","c","d");
        verifyNoMoreInteractions(builder);
    }

    @Test
    public void getOAuthInstanceWithRefreshShouldReturnAOAuthReadyCRest(){
        CRestBuilder expectedBuilder = mock(CRestBuilder.class);
        CRest expected = mock(CRest.class);
        when(builder.oauth("a","b","c","d", "e", "f")).thenReturn(expectedBuilder);
        when(expectedBuilder.build()).thenReturn(expected);

        assertSame(expected, CRest.getOAuthInstance("a","b","c","d", "e", "f"));

        verify(builder).oauth("a","b","c","d", "e", "f");
        verifyNoMoreInteractions(builder);
    }


    @Test
    public void getBasicAuthInstanceShouldReturnABasicAuthReadyCRest(){
        CRestBuilder expectedBuilder = mock(CRestBuilder.class);
        CRest expected = mock(CRest.class);
        when(builder.basicAuth("a","b")).thenReturn(expectedBuilder);
        when(expectedBuilder.build()).thenReturn(expected);

        assertSame(expected, CRest.getBasicAuthInstance("a","b"));

        verify(builder).basicAuth("a","b");
        verifyNoMoreInteractions(builder);
    }


}
