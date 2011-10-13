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

import org.codegist.crest.CRestConfig;
import org.codegist.crest.config.annotate.AnnotationHandler;
import org.codegist.crest.test.util.Classes;
import org.codegist.crest.util.ComponentFactory;
import org.codegist.crest.util.ComponentRegistry;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.annotation.*;
import java.lang.reflect.Method;

import static org.codegist.crest.config.AnnotationDrivenInterfaceConfigFactoryTest.TestInterface.M1;
import static org.codegist.crest.config.AnnotationDrivenInterfaceConfigFactoryTest.TestInterface.M2;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
* @author laurent.gilles@codegist.org
*/
@RunWith(PowerMockRunner.class)
@PrepareForTest(ComponentFactory.class)
public class AnnotationDrivenInterfaceConfigFactoryTest {

    private final DefAnnotationHandler mockDefaultHandler = mock(DefAnnotationHandler.class);
    private final Ann1AnnotationHandler mockAnn1Handler = mock(Ann1AnnotationHandler.class);
    private final Ann2AnnotationHandler mockAnn2Handler = mock(Ann2AnnotationHandler.class);
    private final Ann3AnnotationHandler mockAnn3Handler = mock(Ann3AnnotationHandler.class);

    private final InterfaceConfigBuilderFactory mockInterfaceConfigBuilderFactory = mock(InterfaceConfigBuilderFactory.class);

    private final InterfaceConfigBuilder mockInterfaceConfigBuilder = mock(InterfaceConfigBuilder.class);
    private final MethodConfigBuilder mockMethodM1ConfigBuilder = mock(MethodConfigBuilder.class);
    private final MethodConfigBuilder mockMethodM2ConfigBuilder = mock(MethodConfigBuilder.class);
    private final ParamConfigBuilder mockParamM1P1ConfigBuilder = mock(ParamConfigBuilder.class);
    private final ParamConfigBuilder mockParamM1P2ConfigBuilder = mock(ParamConfigBuilder.class);

    {
        mockStatic(ComponentFactory.class);
        try {
            when(ComponentFactory.instantiate(eq(DefAnnotationHandler.class), any(CRestConfig.class))).thenReturn(mockDefaultHandler);
            when(ComponentFactory.instantiate(eq(Ann1AnnotationHandler.class), any(CRestConfig.class))).thenReturn(mockAnn1Handler);
            when(ComponentFactory.instantiate(eq(Ann2AnnotationHandler.class), any(CRestConfig.class))).thenReturn(mockAnn2Handler);
            when(ComponentFactory.instantiate(eq(Ann3AnnotationHandler.class), any(CRestConfig.class))).thenReturn(mockAnn3Handler);
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private final ComponentRegistry<Class<? extends Annotation>,AnnotationHandler> mockHandlersRegistry = new ComponentRegistry.Builder<Class<? extends Annotation>, AnnotationHandler>()
            .defaultAs(DefAnnotationHandler.class)
            .register(Ann1AnnotationHandler.class, Ann1.class)
            .register(Ann2AnnotationHandler.class, Ann2.class)
            .register(Ann3AnnotationHandler.class, Ann3.class)
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


    static class DefAnnotationHandler implements AnnotationHandler {
        public void handleInterfaceAnnotation(Annotation annotation, InterfaceConfigBuilder builder) throws Exception {

        }

        public void handleMethodAnnotation(Annotation annotation, MethodConfigBuilder builder) throws Exception {

        }

        public void handleParameterAnnotation(Annotation annotation, ParamConfigBuilder builder) throws Exception {

        }
    }

    static class Ann1AnnotationHandler implements AnnotationHandler<Ann1> {
        public void handleInterfaceAnnotation(Ann1 annotation, InterfaceConfigBuilder builder) throws Exception {

        }

        public void handleMethodAnnotation(Ann1 annotation, MethodConfigBuilder builder) throws Exception {

        }

        public void handleParameterAnnotation(Ann1 annotation, ParamConfigBuilder builder) throws Exception {

        }
    }

    static class Ann2AnnotationHandler implements AnnotationHandler<Ann2> {
        public void handleInterfaceAnnotation(Ann2 annotation, InterfaceConfigBuilder builder) throws Exception {

        }

        public void handleMethodAnnotation(Ann2 annotation, MethodConfigBuilder builder) throws Exception {

        }

        public void handleParameterAnnotation(Ann2 annotation, ParamConfigBuilder builder) throws Exception {

        }
    }

    static class Ann3AnnotationHandler implements AnnotationHandler<Ann3> {
        public void handleInterfaceAnnotation(Ann3 annotation, InterfaceConfigBuilder builder) throws Exception {

        }

        public void handleMethodAnnotation(Ann3 annotation, MethodConfigBuilder builder) throws Exception {

        }

        public void handleParameterAnnotation(Ann3 annotation, ParamConfigBuilder builder) throws Exception {

        }
    }
}
