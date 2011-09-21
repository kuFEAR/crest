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

import org.codegist.common.reflect.Types;
import org.codegist.crest.config.annotate.AnnotationHandler;
import org.codegist.crest.util.ComponentRegistry;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import static org.codegist.common.collect.Arrays.merge;

/**
 * <p>Annotation based config factory.</p>
 * <p>The factory will lookup any annotation in the packages {@link org.codegist.crest.annotate} and {@link javax.ws.rs} on the given interface.</p>
 * <p/>
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class AnnotationDrivenInterfaceConfigFactory implements InterfaceConfigFactory {

    private final InterfaceConfigBuilderFactory icbf;
    private final ComponentRegistry<Class<? extends Annotation>, AnnotationHandler> handlersRegistry;

    /**
     * @param icbf the interface config builder factory to get InterfaceConfigBuilder instances from
     * @param handlersRegistry Annotation handlers registry
     */
    public AnnotationDrivenInterfaceConfigFactory(InterfaceConfigBuilderFactory icbf, ComponentRegistry<Class<? extends Annotation>,AnnotationHandler> handlersRegistry) {
        this.handlersRegistry = handlersRegistry;
        this.icbf = icbf;
    }

    @SuppressWarnings("unchecked")
    public InterfaceConfig newConfig(Class<?> interfaze) throws Exception {
        InterfaceConfigBuilder config = icbf.newInstance(interfaze);
        
        for(Annotation annotation : interfaze.getAnnotations()){
            handlersRegistry.get(annotation.annotationType()).handleInterfaceAnnotation(annotation, config);
        }

        for (Method meth : interfaze.getDeclaredMethods()) {
            MethodConfigBuilder methodConfigBuilder = config.startMethodConfig(meth);
            for(Annotation methAnnotation : meth.getAnnotations()){
                handlersRegistry.get(methAnnotation.annotationType()).handleMethodAnnotation(methAnnotation, methodConfigBuilder);
            }

            Class<?>[] paramTypes = meth.getParameterTypes();
            Type[] genParamTypes = meth.getGenericParameterTypes();
            Annotation[][] paramAnnotations = meth.getParameterAnnotations();
            for (int i = 0, max = paramTypes.length; i < max; i++) {
                Type pType = genParamTypes[i];
                Class<?> pClass = Types.getComponentClass(paramTypes[i], pType);
                Annotation[] annotations = merge(Annotation.class, pClass.getAnnotations(), paramAnnotations[i]);

                ParamConfigBuilder methodParamConfigBuilder = methodConfigBuilder.startParamConfig(i);
                for(Annotation paramAnnotation : annotations){
                    handlersRegistry.get(paramAnnotation.annotationType()).handleParameterAnnotation(paramAnnotation, methodParamConfigBuilder);
                }
            }
        }

        return config.build();
    }
}
