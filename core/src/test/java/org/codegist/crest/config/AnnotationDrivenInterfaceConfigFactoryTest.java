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

import org.codegist.crest.config.annotate.AnnotationHandler;
import org.codegist.crest.test.util.Classes;
import org.codegist.crest.util.Registry;
import org.junit.Before;
import org.junit.Test;

import java.lang.annotation.*;
import java.lang.reflect.Method;

import static org.codegist.crest.config.AnnotationDrivenInterfaceConfigFactoryTest.TestInterface.M1;
import static org.codegist.crest.config.AnnotationDrivenInterfaceConfigFactoryTest.TestInterface.M2;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

/**
 * @author laurent.gilles@codegist.org
 */
public class AnnotationDrivenInterfaceConfigFactoryTest {

    private final AnnotationHandler mockDefaultHandler = mock(AnnotationHandler.class);
    private final AnnotationHandler<Ann1> mockAnn1Handler = mock(AnnotationHandler.class);
    private final AnnotationHandler<Ann2> mockAnn2Handler = mock(AnnotationHandler.class);
    private final AnnotationHandler<Ann3> mockAnn3Handler = mock(AnnotationHandler.class);

    private final InterfaceConfigBuilderFactory mockInterfaceConfigBuilderFactory = mock(InterfaceConfigBuilderFactory.class);

    private final InterfaceConfigBuilder mockInterfaceConfigBuilder = mock(InterfaceConfigBuilder.class);
    private final MethodConfigBuilder mockMethodM1ConfigBuilder = mock(MethodConfigBuilder.class);
    private final MethodConfigBuilder mockMethodM2ConfigBuilder = mock(MethodConfigBuilder.class);
    private final ParamConfigBuilder mockParamM1P1ConfigBuilder = mock(ParamConfigBuilder.class);
    private final ParamConfigBuilder mockParamM1P2ConfigBuilder = mock(ParamConfigBuilder.class);


    private final Registry<Class<? extends Annotation>,AnnotationHandler> mockHandlersRegistry = new Registry.Builder<Class<? extends Annotation>, AnnotationHandler>()
            .defaultAs(mockDefaultHandler)
            .register(mockAnn1Handler, Ann1.class)
            .register(mockAnn2Handler, Ann2.class)
            .register(mockAnn3Handler, Ann3.class)
            .build(null);

    private final AnnotationDrivenInterfaceConfigFactory toTest = new AnnotationDrivenInterfaceConfigFactory(mockInterfaceConfigBuilderFactory, mockHandlersRegistry);

    @Before
    public void setupMocks() throws Exception {
        when(mockInterfaceConfigBuilderFactory.newInstance(TestInterface.class)).thenReturn(mockInterfaceConfigBuilder);
        when(mockInterfaceConfigBuilder.startMethodConfig(M1)).thenReturn(mockMethodM1ConfigBuilder);
        when(mockInterfaceConfigBuilder.startMethodConfig(M2)).thenReturn(mockMethodM2ConfigBuilder);
        when(mockMethodM1ConfigBuilder.startParamConfig(0)).thenReturn(mockParamM1P1ConfigBuilder);
        when(mockMethodM1ConfigBuilder.startParamConfig(1)).thenReturn(mockParamM1P2ConfigBuilder);
    }

    @Test
    public void shouldBuildAnInterfaceConfigByDelegatingToAnnotationHandlersForEachAnnotationsDetected() throws Exception {
        InterfaceConfig expected = mock(InterfaceConfig.class);
        when(mockInterfaceConfigBuilder.build()).thenReturn(expected);

        InterfaceConfig actual = toTest.newConfig(TestInterface.class);
        assertSame(expected, actual);

        verify(mockAnn2Handler).handleInterfaceAnnotation(TestInterface.class.getAnnotation(Ann2.class), mockInterfaceConfigBuilder);
        verify(mockAnn3Handler).handleInterfaceAnnotation(TestInterface.class.getAnnotation(Ann3.class), mockInterfaceConfigBuilder);

        verify(mockInterfaceConfigBuilder).startMethodConfig(M1);
        verify(mockAnn1Handler).handleMethodAnnotation(M1.getAnnotation(Ann1.class), mockMethodM1ConfigBuilder);
        verify(mockAnn2Handler).handleMethodAnnotation(M1.getAnnotation(Ann2.class), mockMethodM1ConfigBuilder);
        verify(mockDefaultHandler).handleMethodAnnotation(M1.getAnnotation(Ann4Unknown.class), mockMethodM1ConfigBuilder);

        verify(mockMethodM1ConfigBuilder).startParamConfig(0);
        verify(mockAnn1Handler).handleParameterAnnotation(Type.class.getAnnotation(Ann1.class), mockParamM1P1ConfigBuilder);
        verify(mockMethodM1ConfigBuilder).startParamConfig(1);
        verify(mockAnn1Handler).handleParameterAnnotation(Type.class.getAnnotation(Ann1.class), mockParamM1P2ConfigBuilder);
        verify(mockAnn1Handler).handleParameterAnnotation((Ann1)M1.getParameterAnnotations()[1][0], mockParamM1P2ConfigBuilder);
        verify(mockAnn2Handler).handleParameterAnnotation((Ann2)M1.getParameterAnnotations()[1][1], mockParamM1P2ConfigBuilder);

        verify(mockInterfaceConfigBuilder).startMethodConfig(M2);
        verify(mockAnn3Handler).handleMethodAnnotation(M2.getAnnotation(Ann3.class), mockMethodM2ConfigBuilder);
    }

    @Ann2("a")
    @Ann3("b")
    static interface TestInterface {

        @Ann1("c")
        @Ann2("d")
        @Ann4Unknown("e")
        void m1(Type p1, @Ann1("f") @Ann2("g") Type p2);

        @Ann3("h")
        void m2();
        
        Method M1 = Classes.byName(TestInterface.class, "m1");
        Method M2 = Classes.byName(TestInterface.class, "m2");
    }

    @Ann1("i")
    static class Type {

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE,ElementType.METHOD,ElementType.PARAMETER})
    @interface Ann1 {
        String value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE,ElementType.METHOD,ElementType.PARAMETER})
    @interface Ann2 {
        String value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE,ElementType.METHOD,ElementType.PARAMETER})
    @interface Ann3 {
        String value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE,ElementType.METHOD,ElementType.PARAMETER})
    @interface Ann4Unknown {
        String value();
    }
}
