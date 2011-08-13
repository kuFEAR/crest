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

package org.codegist.crest.param;

import org.codegist.crest.config.ParamType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ParamProcessorFactory.class})
public class ParamProcessorFactoryTest  {

    @Test
    public void shouldReturnAnInstanceOfCollectionMergingCookieProcessor() throws Exception {
        CollectionMergingCookieParamProcessor expected = mock(CollectionMergingCookieParamProcessor.class);
        whenNew(CollectionMergingCookieParamProcessor.class).withArguments("-").thenReturn(expected);
        ParamProcessor actual = ParamProcessorFactory.newInstance(ParamType.COOKIE, "-");
        assertSame(expected, actual);
    }

    @Test
    public void shouldReturnAnInstanceOfDefaultCookieProcessor() throws Exception {
        ParamProcessor actual = ParamProcessorFactory.newInstance(ParamType.COOKIE, null);
        assertSame(DefaultCookieParamProcessor.INSTANCE, actual);
    }

    @Test
    public void shouldReturnAnInstanceOfCollectionMergingDefaultProcessorForQuery() throws Exception {
        CollectionMergingParamProcessor expected = mock(CollectionMergingParamProcessor.class);
        whenNew(CollectionMergingParamProcessor.class).withArguments("-").thenReturn(expected);
        ParamProcessor actual = ParamProcessorFactory.newInstance(ParamType.QUERY, "-");
        assertSame(expected, actual);
    }

    @Test
    public void shouldReturnAnInstanceOfDefaultProcessorForQuery() throws Exception {
        ParamProcessor actual = ParamProcessorFactory.newInstance(ParamType.QUERY, null);
        assertSame(DefaultParamProcessor.INSTANCE, actual);
    }

    @Test
    public void shouldReturnAnInstanceOfCollectionMergingDefaultProcessorForForm() throws Exception {
        CollectionMergingParamProcessor expected = mock(CollectionMergingParamProcessor.class);
        whenNew(CollectionMergingParamProcessor.class).withArguments("-").thenReturn(expected);
        ParamProcessor actual = ParamProcessorFactory.newInstance(ParamType.FORM, "-");
        assertSame(expected, actual);
    }

    @Test
    public void shouldReturnAnInstanceOfDefaultProcessorForForm() throws Exception {
        ParamProcessor actual = ParamProcessorFactory.newInstance(ParamType.FORM, null);
        assertSame(DefaultParamProcessor.INSTANCE, actual);
    }

    @Test
    public void shouldReturnAnInstanceOfCollectionMergingDefaultProcessorForHeader() throws Exception {
        CollectionMergingParamProcessor expected = mock(CollectionMergingParamProcessor.class);
        whenNew(CollectionMergingParamProcessor.class).withArguments("-").thenReturn(expected);
        ParamProcessor actual = ParamProcessorFactory.newInstance(ParamType.HEADER, "-");
        assertSame(expected, actual);
    }

    @Test
    public void shouldReturnAnInstanceOfDefaultProcessorForHeader() throws Exception {
        ParamProcessor actual = ParamProcessorFactory.newInstance(ParamType.HEADER, null);
        assertSame(DefaultParamProcessor.INSTANCE, actual);
    }

    @Test
    public void shouldReturnAnInstanceOfCollectionMergingDefaultProcessorForMatrix() throws Exception {
        CollectionMergingParamProcessor expected = mock(CollectionMergingParamProcessor.class);
        whenNew(CollectionMergingParamProcessor.class).withArguments("-").thenReturn(expected);
        ParamProcessor actual = ParamProcessorFactory.newInstance(ParamType.MATRIX, "-");
        assertSame(expected, actual);
    }

    @Test
    public void shouldReturnAnInstanceOfDefaultProcessorForMatrix() throws Exception {
        ParamProcessor actual = ParamProcessorFactory.newInstance(ParamType.MATRIX, null);
        assertSame(DefaultParamProcessor.INSTANCE, actual);
    }

    @Test
    public void shouldReturnAnInstanceOfCollectionMergingDefaultProcessorForPath() throws Exception {
        CollectionMergingParamProcessor expected = mock(CollectionMergingParamProcessor.class);
        whenNew(CollectionMergingParamProcessor.class).withArguments("-").thenReturn(expected);
        ParamProcessor actual = ParamProcessorFactory.newInstance(ParamType.PATH, "-");
        assertSame(expected, actual);
    }

    @Test
    public void shouldReturnAnInstanceOfDefaultProcessorForPath() throws Exception {
        ParamProcessor actual = ParamProcessorFactory.newInstance(ParamType.PATH, null);
        assertSame(DefaultParamProcessor.INSTANCE, actual);
    }
}
