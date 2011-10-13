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

package org.codegist.crest.entity;

import org.codegist.crest.config.MethodConfig;
import org.codegist.crest.config.ParamType;
import org.codegist.crest.io.Request;
import org.codegist.crest.param.EncodedPair;
import org.codegist.crest.test.util.Values;
import org.codegist.crest.util.Pairs;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;

import static java.util.Arrays.asList;
import static org.codegist.crest.test.util.Values.UTF8;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author laurent.gilles@codegist.org
 */
public class UrlEncodedFormEntityWriterTest {

    private final UrlEncodedFormEntityWriter toTest = new UrlEncodedFormEntityWriter();

    @Test
    public void getContentLengthShouldReturnMinusOne(){
        assertEquals(-1, toTest.getContentLength(null));
    }
    @Test
    public void getContentTypeShouldReturnWWWFormEncodedWithRequestsCharset(){
        assertEquals("application/x-www-form-urlencoded; charset=UTF-8", toTest.getContentType(mockRequest()));
    }
    @Test
    public void writeToShouldUseRequestFormParamsWithGivenCharset() throws IOException {
        Iterator<EncodedPair> pairs = asList(
                Pairs.toPair("p1", "v%201", Values.UTF8, true),
                Pairs.toPair("p2", "v£ 1", Values.UTF8, false)
        ).iterator();
        Request mockRequest = mockRequest();
        when(mockRequest.getEncodedParamsIterator(ParamType.FORM)).thenReturn(pairs);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        toTest.writeTo(mockRequest, out);
        assertEquals("p1=v%201&p2=v%C2%A3%201", out.toString("utf-8"));
    }



    private Request mockRequest(){
        MethodConfig mockMethodConfig = mock(MethodConfig.class);
        when(mockMethodConfig.getCharset()).thenReturn(UTF8);
        Request mockRequest = mock(Request.class);
        when(mockRequest.getMethodConfig()).thenReturn(mockMethodConfig);
        return mockRequest;
    }

}
