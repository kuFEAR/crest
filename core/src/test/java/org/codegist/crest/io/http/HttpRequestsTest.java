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

import org.codegist.crest.NonInstanciableClassTest;
import org.codegist.crest.config.MethodConfig;
import org.codegist.crest.config.PathBuilder;
import org.codegist.crest.config.PathTemplate;
import org.codegist.crest.io.Request;
import org.codegist.crest.param.EncodedPair;
import org.codegist.crest.util.Pairs;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Iterator;

import static java.util.Arrays.asList;
import static org.codegist.crest.config.ParamType.*;
import static org.codegist.crest.test.util.Values.UTF8;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * @author laurent.gilles@codegist.org
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Pairs.class})
public class HttpRequestsTest extends NonInstanciableClassTest {

    public HttpRequestsTest() {
        super(HttpRequests.class);
    }

    @Test
    public void toUrlShouldTransformGivenRequestToUrl() throws Exception {
        Request request = mock(Request.class);
        MethodConfig methodConfig = mock(MethodConfig.class);
        PathTemplate pathTemplate = mock(PathTemplate.class);
        PathBuilder pathBuilder = mock(PathBuilder.class);
        Iterator<EncodedPair> pathPairs = asList(
                Pairs.toPreEncodedPair("p1","v1"),
                Pairs.toPreEncodedPair("p2","v2")
        ).iterator();
        Iterator<EncodedPair> queryPairs = mock(Iterator.class);
        Iterator<EncodedPair> matrixPairs = mock(Iterator.class);

        when(request.getEncodedParamsIterator(PATH)).thenReturn(pathPairs);
        when(request.getEncodedParamsIterator(QUERY)).thenReturn(queryPairs);
        when(request.getEncodedParamsIterator(MATRIX)).thenReturn(matrixPairs);
        when(pathTemplate.getBuilder(UTF8)).thenReturn(pathBuilder);
        when(methodConfig.getCharset()).thenReturn(UTF8);
        when(methodConfig.getPathTemplate()).thenReturn(pathTemplate);
        when(request.getMethodConfig()).thenReturn(methodConfig);
        when(pathBuilder.build()).thenReturn("path");
        mockStatic(Pairs.class);
        when(Pairs.join(queryPairs, '&')).thenReturn("query");
        when(Pairs.join(matrixPairs, ';')).thenReturn("matrix");

        assertEquals("path;matrix?query", HttpRequests.toUrl(request));

        verify(pathBuilder).merge("p1", "v1", true);
        verify(pathBuilder).merge("p2", "v2", true);
    }

    @Test
    public void toUrlShouldTransformGivenRequestToUrlWhenQueryAndMatrixAreEmpty() throws Exception {
        Request request = mock(Request.class);
        MethodConfig methodConfig = mock(MethodConfig.class);
        PathTemplate pathTemplate = mock(PathTemplate.class);
        PathBuilder pathBuilder = mock(PathBuilder.class);
        Iterator<EncodedPair> pathPairs = asList(
                Pairs.toPreEncodedPair("p1","v1"),
                Pairs.toPreEncodedPair("p2","v2")
        ).iterator();
        Iterator<EncodedPair> queryPairs = mock(Iterator.class);
        Iterator<EncodedPair> matrixPairs = mock(Iterator.class);

        when(request.getEncodedParamsIterator(PATH)).thenReturn(pathPairs);
        when(request.getEncodedParamsIterator(QUERY)).thenReturn(queryPairs);
        when(request.getEncodedParamsIterator(MATRIX)).thenReturn(matrixPairs);
        when(pathTemplate.getBuilder(UTF8)).thenReturn(pathBuilder);
        when(methodConfig.getCharset()).thenReturn(UTF8);
        when(methodConfig.getPathTemplate()).thenReturn(pathTemplate);
        when(request.getMethodConfig()).thenReturn(methodConfig);
        when(pathBuilder.build()).thenReturn("path");
        mockStatic(Pairs.class);
        when(Pairs.join(queryPairs, '&')).thenReturn("");
        when(Pairs.join(matrixPairs, ';')).thenReturn("");

        assertEquals("path", HttpRequests.toUrl(request));

        verify(pathBuilder).merge("p1", "v1", true);
        verify(pathBuilder).merge("p2", "v2", true);
    }
}
