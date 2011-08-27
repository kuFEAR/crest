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

package org.codegist.crest.config;

import org.codegist.crest.CRestConfig;
import org.codegist.crest.serializer.Deserializer;
import org.codegist.crest.serializer.Serializer;
import org.codegist.crest.test.util.TestInterface;
import org.codegist.crest.util.Registry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * @author laurent.gilles@codegist.org
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({DefaultInterfaceConfigBuilderFactory.class, Registry.class})
public class DefaultInterfaceConfigBuilderFactoryTest {

    private final Registry<String,Deserializer> mockMimeDeserializerRegistry = mock(Registry.class);
    private final Registry<Class<?>, Serializer> mockClassSerializerRegistry = mock(Registry.class);
    private final CRestConfig mockCrestConfig = mock(CRestConfig.class);

    private final DefaultInterfaceConfigBuilderFactory toTest = new DefaultInterfaceConfigBuilderFactory(
            mockCrestConfig, mockMimeDeserializerRegistry, mockClassSerializerRegistry
    );

    @Test
    public void shouldBuildAnInstanceOfDefaultInterfaceConfigBuilderWithGivenParams() throws Exception {
        DefaultInterfaceConfigBuilder expected = mock(DefaultInterfaceConfigBuilder.class);
        whenNew(DefaultInterfaceConfigBuilder.class)
                .withArguments(TestInterface.class, mockCrestConfig, mockMimeDeserializerRegistry, mockClassSerializerRegistry)
                .thenReturn(expected);

        InterfaceConfigBuilder actual = toTest.newInstance(TestInterface.class);
        assertSame(expected, actual);
    }
    
}
