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

package org.codegist.crest.serializer.jaxb;

import org.codegist.crest.CRestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.xml.bind.JAXBContext;

import static org.codegist.crest.test.util.CRestConfigs.mockDefaultBehavior;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({JaxbFactory.class, JAXBContext.class, SimpleJaxb.class, PooledJaxb.class, TypeCachingJaxb.class})
public class JaxbFactoryTest {

    private final CRestConfig mockCRestConfig = mockDefaultBehavior();
    private final JAXBContext mockJaxbContext = mock(JAXBContext.class);

    @Test
    public void shouldUseGivenCRestConfigJaxb() throws Exception {
        Jaxb predefined = mock(Jaxb.class);
        when(mockCRestConfig.get(getClass().getName() + JaxbFactory.JAXB)).thenReturn(predefined);

        Jaxb actual = JaxbFactory.create(mockCRestConfig, getClass());
        assertSame(predefined,actual);
    }

    @Test
    public void shouldCreateATypeCachingJaxb() throws Exception {
        TypeCachingJaxb mockTypeCachingJaxb = mock(TypeCachingJaxb.class);
        whenNew(TypeCachingJaxb.class).withArguments(any(CRestConfig.class), any(Class.class)).thenReturn(mockTypeCachingJaxb);

        Jaxb actual = JaxbFactory.create(mockCRestConfig, getClass());
        assertTrue(actual instanceof TypeCachingJaxb);
        verifyNew(TypeCachingJaxb.class).withArguments(mockCRestConfig, getClass());
    }

    @Test
    public void shouldDelegateToCreateWithGivenPackageContext() throws Exception {
        String packageContext = "packageContext";
        when(mockCRestConfig.get(getClass().getName() + JaxbFactory.MODEL_PACKAGE)).thenReturn(packageContext);
        mockStatic(JAXBContext.class);
        when(JAXBContext.newInstance(packageContext)).thenReturn(mockJaxbContext);

        Jaxb actual = JaxbFactory.create(mockCRestConfig, getClass());
        assertTrue(actual instanceof SimpleJaxb);
    }

    @Test
    public void shouldDelegateToCreateWithGivenModelFactory() throws Exception {
        Class<?> modelFactory = JaxbFactoryTest.class;
        when(mockCRestConfig.get(getClass().getName() + JaxbFactory.MODEL_FACTORY_CLASS)).thenReturn(modelFactory);
        mockStatic(JAXBContext.class);
        when(JAXBContext.newInstance(modelFactory)).thenReturn(mockJaxbContext);

        Jaxb actual = JaxbFactory.create(mockCRestConfig, getClass());
        assertTrue(actual instanceof SimpleJaxb);
    }


    @Test
    public void shouldCreateASimpleJaxbWithJaxContextCreateWithGivenClassToBeBound() throws Exception {
        Class<?>[] classesToBeBound = new Class<?>[0];
        SimpleJaxb mockSimpleJaxb = mock(SimpleJaxb.class);

        mockStatic(JAXBContext.class);
        when(JAXBContext.newInstance(classesToBeBound)).thenReturn(mockJaxbContext);
        whenNew(SimpleJaxb.class).withArguments(any(JAXBContext.class)).thenReturn(mockSimpleJaxb);

        Jaxb actual = JaxbFactory.create(mockCRestConfig, getClass(), classesToBeBound);
        assertTrue(actual instanceof SimpleJaxb);
        verifyNew(SimpleJaxb.class).withArguments(mockJaxbContext);
    }
    @Test
    public void shouldCreateASimpleJaxbWithJaxContextCreateWithGivenPackageContext() throws Exception {
        String packageContext = "packageContext";
        SimpleJaxb mockSimpleJaxb = mock(SimpleJaxb.class);

        mockStatic(JAXBContext.class);
        when(JAXBContext.newInstance(packageContext)).thenReturn(mockJaxbContext);
        whenNew(SimpleJaxb.class).withArguments(any(JAXBContext.class)).thenReturn(mockSimpleJaxb);

        Jaxb actual = JaxbFactory.create(mockCRestConfig, getClass(), packageContext);
        assertTrue(actual instanceof SimpleJaxb);
        verifyNew(SimpleJaxb.class).withArguments(mockJaxbContext);
    }

    @Test
    public void shouldCreateAPooledJaxbWithJaxContextCreateWithGivenClassToBeBound() throws Exception {
        Class<?>[] classesToBeBound = new Class<?>[0];
        PooledJaxb mockPooledJaxb = mock(PooledJaxb.class);

        when(mockCRestConfig.getConcurrencyLevel()).thenReturn(2);
        when(mockCRestConfig.get(getClass().getName() + JaxbFactory.POOL_RETRIEVAL_MAX_WAIT, 30000l)).thenReturn(123l);
        mockStatic(JAXBContext.class);
        when(JAXBContext.newInstance(classesToBeBound)).thenReturn(mockJaxbContext);
        whenNew(PooledJaxb.class).withArguments(any(JAXBContext.class), anyInt(), anyLong()).thenReturn(mockPooledJaxb);

        Jaxb actual = JaxbFactory.create(mockCRestConfig, getClass(), classesToBeBound);
        assertTrue(actual instanceof PooledJaxb);
        verifyNew(PooledJaxb.class).withArguments(mockJaxbContext, 2, 123l);
    }

    @Test
    public void shouldCreateAPooledJaxbWithJaxContextCreateWithGivenPackageContext() throws Exception {
        String packageContext = "packageContext";
        PooledJaxb mockPooledJaxb = mock(PooledJaxb.class);

        when(mockCRestConfig.getConcurrencyLevel()).thenReturn(2);
        when(mockCRestConfig.get(getClass().getName() + JaxbFactory.POOL_RETRIEVAL_MAX_WAIT, 30000l)).thenReturn(123l);
        mockStatic(JAXBContext.class);
        when(JAXBContext.newInstance(packageContext)).thenReturn(mockJaxbContext);
        whenNew(PooledJaxb.class).withArguments(any(JAXBContext.class), anyInt(), anyLong()).thenReturn(mockPooledJaxb);

        Jaxb actual = JaxbFactory.create(mockCRestConfig, getClass(), packageContext);
        assertTrue(actual instanceof PooledJaxb);
        verifyNew(PooledJaxb.class).withArguments(mockJaxbContext, 2, 123l);
    }



}
