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
import org.codegist.crest.util.Registry;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;

import static java.lang.System.arraycopy;

/**
 * <p>Annotation based config factory of any possible interfaces given to the factory.
 * <p>The factory will lookup any annotation in package {@link org.codegist.crest.annotate} on to the given interface.
 * <p/>
 * <p>- Each config fallback from param to method to interface until one config is found, otherwise defaults to any respective default value ({@link InterfaceConfig}, {@link org.codegist.crest.config.MethodConfig},
 *
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 * @see InterfaceConfig
 * @see org.codegist.crest.annotate
 */
public class AnnotationDrivenInterfaceConfigFactory implements InterfaceConfigFactory {

    private final Map<String,Object> crestProperties;
    private final Registry<Class<? extends Annotation>,AnnotationHandler> handlersRegistry;
    private final boolean modelPriority;

    public AnnotationDrivenInterfaceConfigFactory(Map<String,Object> crestProperties, Registry<Class<? extends Annotation>,AnnotationHandler> handlersRegistry, boolean modelPriority) {
        this.handlersRegistry = handlersRegistry;
        this.modelPriority = modelPriority;
        this.crestProperties = crestProperties;
    }

    public InterfaceConfig newConfig(Class<?> interfaze) {
        InterfaceConfigBuilder config = new InterfaceConfigBuilder(interfaze, crestProperties);
        
        for(Annotation annotation : interfaze.getAnnotations()){
            handlersRegistry.get(annotation.annotationType()).handleInterfaceAnnotation(annotation, config);
        }

        for (Method meth : interfaze.getDeclaredMethods()) {
            MethodConfigBuilder methodConfigBuilder = config.startMethodConfig(meth);
            for(Annotation methAnnotation : meth.getAnnotations()){
                handlersRegistry.get(methAnnotation.annotationType()).handleMethodAnnotation(methAnnotation, methodConfigBuilder);
            }

            for (int i = 0, max = meth.getParameterTypes().length; i < max; i++) {
                Class<?> pClass = meth.getParameterTypes()[i];
                Type pType = meth.getGenericParameterTypes()[i];
                Class<?> realClass = Types.getComponentClass(pClass, pType);
                ParamConfigBuilder methodParamConfigBuilder = methodConfigBuilder.startParamConfig(i);

                Annotation[] typeAnnotations = realClass.getAnnotations();
                Annotation[] paramAnnotations = meth.getParameterAnnotations()[i];
                Annotation[] highAnnotations = modelPriority ?  typeAnnotations : paramAnnotations;
                Annotation[] lowAnnotations = modelPriority ?  paramAnnotations : typeAnnotations;
                Annotation[] annotations = new Annotation[lowAnnotations.length + highAnnotations.length];

                arraycopy(lowAnnotations, 0, annotations, 0, lowAnnotations.length);
                arraycopy(highAnnotations, 0, annotations, lowAnnotations.length, highAnnotations.length);

                for(Annotation paramAnnotation : annotations){
                    handlersRegistry.get(paramAnnotation.annotationType()).handleParameterAnnotation(paramAnnotation, methodParamConfigBuilder);
                }
            }
        }

        return config.build();
    }
}
