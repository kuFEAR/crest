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

package org.codegist.crest.config;

import org.codegist.crest.serializer.Serializer;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author laurent.gilles@codegist.org
 */
public class CompositeParamConfigBuilderTest {

    private final ParamConfigBuilder mockParamConfigBuilder1 = mock(ParamConfigBuilder.class);
    private final ParamConfigBuilder mockParamConfigBuilder2 = mock(ParamConfigBuilder.class);

    private final DefaultInterfaceConfigBuilder.CompositeParamConfigBuilder toTest = new DefaultInterfaceConfigBuilder.CompositeParamConfigBuilder(new ParamConfigBuilder[]{mockParamConfigBuilder1, mockParamConfigBuilder2});

    @Test(expected = UnsupportedOperationException.class)
    public void buildShouldThrowUnsupportedOperationException() throws Exception {
        toTest.build();
    }

    @Test
    public void setNameShouldSetNameOverAllNestedParamConfigBuilders() throws Exception {
        assertSame(toTest, toTest.setName("a"));
        verify(mockParamConfigBuilder1).setName("a");
        verify(mockParamConfigBuilder2).setName("a");
    }

    @Test
    public void setDefaultValueShouldSetDefaultValueOverAllNestedParamConfigBuilders() throws Exception {
        assertSame(toTest, toTest.setDefaultValue("a"));
        verify(mockParamConfigBuilder1).setDefaultValue("a");
        verify(mockParamConfigBuilder2).setDefaultValue("a");
    }

    @Test
    public void setListSeparatorShouldSetListSeparatorOverAllNestedParamConfigBuilders() throws Exception {
        assertSame(toTest, toTest.setListSeparator("a"));
        verify(mockParamConfigBuilder1).setListSeparator("a");
        verify(mockParamConfigBuilder2).setListSeparator("a");
    }

    @Test
    public void setEncodedShouldSetEncodedOverAllNestedParamConfigBuilders() throws Exception {
        assertSame(toTest, toTest.setEncoded(true));
        verify(mockParamConfigBuilder1).setEncoded(true);
        verify(mockParamConfigBuilder2).setEncoded(true);
    }

    @Test
    public void setMetaDatasShouldSetMetaDatasOverAllNestedParamConfigBuilders() throws Exception {
        assertSame(toTest, toTest.setMetaDatas(Collections.<String, Object>emptyMap()));
        verify(mockParamConfigBuilder1).setMetaDatas(Collections.<String, Object>emptyMap());
        verify(mockParamConfigBuilder2).setMetaDatas(Collections.<String, Object>emptyMap());
    }

    @Test
    public void setSerializerShouldSetSerializerOverAllNestedParamConfigBuilders() throws Exception {
        assertSame(toTest, toTest.setSerializer(Serializer.class));
        verify(mockParamConfigBuilder1).setSerializer(Serializer.class);
        verify(mockParamConfigBuilder2).setSerializer(Serializer.class);
    }


    @Test
    public void setTypeShouldSetTypeOverAllNestedParamConfigBuilders() throws Exception {
        assertSame(toTest, toTest.setType(ParamType.getDefault()));
        verify(mockParamConfigBuilder1).setType(ParamType.getDefault());
        verify(mockParamConfigBuilder2).setType(ParamType.getDefault());
    }

}
