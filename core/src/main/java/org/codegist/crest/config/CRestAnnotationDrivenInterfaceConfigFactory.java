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
 *  ==================================================================
 *
 *  More information at http://www.codegist.org.
 */

package org.codegist.crest.config;

import org.codegist.common.reflect.Methods;
import org.codegist.common.reflect.Types;
import org.codegist.crest.MultiParts;
import org.codegist.crest.annotate.*;
import org.codegist.crest.http.HttpRequest;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

/**
 * <p>Annotation based config factory of any possible interfaces given to the factory.
 * <p>The factory will lookup any annotation in package {@link org.codegist.crest.annotate} on to the given interface.
 * <p/>
 * <p>- Each config fallback from param to method to interface until one config is found, otherwise defaults to any respective default value ({@link org.codegist.crest.config.InterfaceConfig}, {@link MethodConfig},
 *
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 * @see org.codegist.crest.config.InterfaceConfig
 * @see org.codegist.crest.annotate
 */
public class CRestAnnotationDrivenInterfaceConfigFactory implements InterfaceConfigFactory {

    public static final String PROP_BUILD_TEMPLATE = CRestAnnotationDrivenInterfaceConfigFactory.class + "#build-template";
    public static final String PROP_MODEL_PRIORITY = CRestAnnotationDrivenInterfaceConfigFactory.class + "#model-priority";

    private final Map<String,Object> customProperties;
    private final boolean buildTemplates;
    private final boolean modelPriority;

    public CRestAnnotationDrivenInterfaceConfigFactory(Map<String,Object> customProperties) {
        this.customProperties = customProperties;
        this.buildTemplates = Boolean.valueOf((String) customProperties.get(PROP_BUILD_TEMPLATE));
        this.modelPriority = Boolean.valueOf((String) customProperties.get(PROP_MODEL_PRIORITY));
    }

    // TODO handle @MultiPartParam(s)
    public InterfaceConfig newConfig(Class<?> interfaze) throws ConfigFactoryException {
        try {
            /* Interface specifics */
            EndPoint endPoint = interfaze.getAnnotation(EndPoint.class);
            Path path = interfaze.getAnnotation(Path.class);
            Encoding encoding = interfaze.getAnnotation(Encoding.class);

            /* Methods defaults */
            SocketTimeout socketTimeout = interfaze.getAnnotation(SocketTimeout.class);
            ConnectionTimeout connectionTimeout = interfaze.getAnnotation(ConnectionTimeout.class);
            RequestInterceptor interceptor = interfaze.getAnnotation(RequestInterceptor.class);
            ResponseHandler responseHandler = interfaze.getAnnotation(ResponseHandler.class);
            ErrorHandler errorHandler = interfaze.getAnnotation(ErrorHandler.class);
            RetryHandler retryHandler = interfaze.getAnnotation(RetryHandler.class);
            Consumes consumes = interfaze.getAnnotation(Consumes.class);
            Produces produces = interfaze.getAnnotation(Produces.class);
            HttpMethod httpMethod = getHttpMethod(interfaze.getAnnotations(), interfaze.getAnnotation(HttpMethod.class));
            EntityWriter entityWriter = interfaze.getAnnotation(EntityWriter.class);
            Set<ParamConfigHolder> extraParams = getExtraParamConfigs(interfaze.getAnnotations());

            /* Params defaults */
            ListSeparator listSeparator = interfaze.getAnnotation(ListSeparator.class);
            Serializer serializer = interfaze.getAnnotation(Serializer.class);
            Encoded encoded = interfaze.getAnnotation(Encoded.class);

            InterfaceConfigBuilder config = new InterfaceConfigBuilder(interfaze, customProperties);
            for (ParamConfigHolder c : extraParams) {
                config.addMethodsExtraParam(c.name, c.defaultValue, c.destination, c.metadatas);
            }

            if (endPoint != null) config.setMethodsEndPoint(endPoint.value());
            if (path != null) config.appendMethodsPath(path.value());
            if (encoding != null) config.setEncoding(encoding.value());

            if (socketTimeout != null) config.setMethodsSocketTimeout(socketTimeout.value());
            if (connectionTimeout != null) config.setMethodsConnectionTimeout(connectionTimeout.value());
            if (interceptor != null) config.setMethodsRequestInterceptor(interceptor.value());
            if (responseHandler != null) config.setMethodsResponseHandler(responseHandler.value());
            if (errorHandler != null) config.setMethodsErrorHandler(errorHandler.value());
            if (retryHandler != null) config.setMethodsRetryHandler(retryHandler.value());
            if (consumes != null) config.setMethodsConsumes(consumes.value());
            if (produces != null) config.setMethodsProduces(produces.value());
            if (httpMethod != null) config.setMethodsHttpMethod(httpMethod.value());
            if (entityWriter != null) config.setMethodsEntityWriter(entityWriter.value());

            if (listSeparator != null) config.setParamsListSeparator(listSeparator.value());
            if (serializer != null) config.setParamsSerializer(serializer.value());
            config.setParamsEncoded(encoded != null);


            for (Method meth : interfaze.getDeclaredMethods()) {
                /* Methods specifics */
                endPoint = meth.getAnnotation(EndPoint.class);
                path = meth.getAnnotation(Path.class);
                extraParams = getExtraParamConfigs(meth.getAnnotations());
                socketTimeout = meth.getAnnotation(SocketTimeout.class);
                connectionTimeout = meth.getAnnotation(ConnectionTimeout.class);
                interceptor = meth.getAnnotation(RequestInterceptor.class);
                responseHandler = meth.getAnnotation(ResponseHandler.class);
                errorHandler = meth.getAnnotation(ErrorHandler.class);
                retryHandler = meth.getAnnotation(RetryHandler.class);
                consumes = meth.getAnnotation(Consumes.class);
                produces = meth.getAnnotation(Produces.class);
                httpMethod = getHttpMethod(meth.getAnnotations(), meth.getAnnotation(HttpMethod.class));
                entityWriter = meth.getAnnotation(EntityWriter.class);

                /* Params defaults */
                listSeparator = meth.getAnnotation(ListSeparator.class);
                serializer = meth.getAnnotation(Serializer.class);
                encoded = meth.getAnnotation(Encoded.class);

                MethodConfigBuilder methodConfigBuilder = config.startMethodConfig(meth);
                for (ParamConfigHolder c : extraParams) {
                    methodConfigBuilder.addExtraParam(c.name, c.defaultValue, c.destination, c.metadatas);
                }

                if (endPoint != null) methodConfigBuilder.setEndPoint(endPoint.value());
                if (path != null) methodConfigBuilder.appendPath(path.value());
                if (socketTimeout != null) methodConfigBuilder.setSocketTimeout(socketTimeout.value());
                if (connectionTimeout != null) methodConfigBuilder.setConnectionTimeout(connectionTimeout.value());
                if (interceptor != null) methodConfigBuilder.setRequestInterceptor(interceptor.value());
                if (responseHandler != null) methodConfigBuilder.setResponseHandler(responseHandler.value());
                if (errorHandler != null) methodConfigBuilder.setErrorHandler(errorHandler.value());
                if (retryHandler != null) methodConfigBuilder.setRetryHandler(retryHandler.value());
                if (consumes != null) methodConfigBuilder.setConsumes(consumes.value());
                if (produces != null) methodConfigBuilder.setProduces(produces.value());
                if (httpMethod != null) methodConfigBuilder.setHttpMethod(httpMethod.value());
                if (entityWriter != null) methodConfigBuilder.setEntityWriter(entityWriter.value());
                if (entityWriter != null) methodConfigBuilder.setEntityWriter(entityWriter.value());

                if (serializer != null) methodConfigBuilder.setParamsSerializer(serializer.value());
                if(listSeparator !=null) methodConfigBuilder.setParamsListSeparator(listSeparator.value());
                methodConfigBuilder.setParamsEncoded(encoded != null);

                for (int i = 0, max = meth.getParameterTypes().length; i < max; i++) {
                    Class<?> pClass = meth.getParameterTypes()[i];
                    Type pType = meth.getGenericParameterTypes()[i];
                    Class<?> realClass = Types.getComponentClass(pClass, pType);

                    Map<Class<? extends Annotation>, Annotation> paramAnnotations = Methods.getParamsAnnotation(meth, i);
                    ParamConfigBuilder methodParamConfigBuilder = methodConfigBuilder.startParamConfig(i);

                    /* Params specifics - Override user annotated config */
                    serializer = (Serializer) paramAnnotations.get(Serializer.class);
                    encoded = (Encoded) paramAnnotations.get(Encoded.class);
                    listSeparator = (ListSeparator) paramAnnotations.get(ListSeparator.class);

                    Serializer pSerializer = realClass.getAnnotation(Serializer.class);
                    Encoded pEncoded = realClass.getAnnotation(Encoded.class);
                    ListSeparator pListSeparator = realClass.getAnnotation(ListSeparator.class);

                    ParamConfigHolder lowConfig = getFirstExtraParamConfig(modelPriority ? paramAnnotations.values().toArray(new Annotation[paramAnnotations.size()]) : realClass.getAnnotations());
                    ParamConfigHolder highConfig = getFirstExtraParamConfig(modelPriority ? realClass.getAnnotations() : paramAnnotations.values().toArray(new Annotation[paramAnnotations.size()]));


                    Serializer lowPrioritySerializer = modelPriority ? serializer : pSerializer;
                    Serializer highPrioritySerializer = modelPriority ? pSerializer : serializer;

                    ListSeparator lowListSeparator = modelPriority ? listSeparator : pListSeparator;
                    ListSeparator highListSeparator = modelPriority ? pListSeparator : listSeparator;

                    Boolean lowPriorityEncoded;

                    if(HttpRequest.DEST_HEADER.equals(lowConfig.destination) || HttpRequest.DEST_COOKIE.equals(lowConfig.destination)) {
                        lowPriorityEncoded = true;
                    }else{
                        lowPriorityEncoded = (modelPriority ? encoded : pEncoded) != null ? true : null;
                    }

                    Boolean highPriorityEncoded;
                    if(HttpRequest.DEST_HEADER.equals(highConfig.destination) || HttpRequest.DEST_COOKIE.equals(highConfig.destination)) {
                        highPriorityEncoded = true;
                    }else{
                        highPriorityEncoded = (modelPriority ? pEncoded : encoded) != null ? true : null;
                    }

                    if(lowPriorityEncoded != null) methodParamConfigBuilder.setEncoded(lowPriorityEncoded);
                    if(lowConfig.name != null)  methodParamConfigBuilder.setName(lowConfig.name);
                    if(lowConfig.defaultValue != null)  methodParamConfigBuilder.setDefaultValue(lowConfig.defaultValue);
                    if(lowConfig.destination != null)  methodParamConfigBuilder.setDestination(lowConfig.destination);
                    if(lowConfig.metadatas != null && !lowConfig.metadatas.isEmpty())  methodParamConfigBuilder.setMetaDatas(lowConfig.metadatas);
                    if(lowListSeparator != null)  methodParamConfigBuilder.setListSeparator(lowListSeparator.value());
                    if (lowPrioritySerializer != null) methodParamConfigBuilder.setSerializer(lowPrioritySerializer.value());

                    if(highPriorityEncoded != null) methodParamConfigBuilder.setEncoded(highPriorityEncoded);
                    if(highConfig.name != null)  methodParamConfigBuilder.setName(highConfig.name);
                    if(highConfig.defaultValue != null)  methodParamConfigBuilder.setDefaultValue(highConfig.defaultValue);
                    if(highConfig.destination != null)  methodParamConfigBuilder.setDestination(highConfig.destination);
                    if(highConfig.metadatas != null && !highConfig.metadatas.isEmpty())  methodParamConfigBuilder.setMetaDatas(highConfig.metadatas);
                    if(highListSeparator != null)  methodParamConfigBuilder.setListSeparator(highListSeparator.value());
                    if (highPrioritySerializer != null) methodParamConfigBuilder.setSerializer(highPrioritySerializer.value());

                    methodParamConfigBuilder.endParamConfig();
                }

                methodConfigBuilder.endMethodConfig();
            }

            return config.build(true, buildTemplates);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new ConfigFactoryException(e);
        }
    }


    private static ParamConfigHolder getFirstExtraParamConfig(Annotation[] annotations) {
        Set<ParamConfigHolder> config = getExtraParamConfigs(annotations);
        if (config.isEmpty()) return new ParamConfigHolder(null, null, null, null);
        return config.iterator().next();// get the first
    }

    private static Set<ParamConfigHolder> getExtraParamConfigs(Annotation[] annotations) {
        Set<ParamConfigHolder> params = new LinkedHashSet<ParamConfigHolder>(getParam(annotations));

        for (Annotation a : annotations) {
            if (a instanceof FormParams) {
                params.addAll(getParam(((FormParams) a).value()));
            } else if (a instanceof PathParams) {
                params.addAll(getParam(((PathParams) a).value()));
            } else if (a instanceof QueryParams) {
                params.addAll(getParam(((QueryParams) a).value()));
            } else if (a instanceof HeaderParams) {
                params.addAll(getParam(((HeaderParams) a).value()));
            } else if (a instanceof CookieParams) {
                params.addAll(getParam(((CookieParams) a).value()));
            } else if (a instanceof MatrixParams) {
                params.addAll(getParam(((MatrixParams) a).value()));
            }else if (a instanceof MultiPartParams) {
                params.addAll(getParam(((MultiPartParams) a).value()));
            }
        }
        return params;
    }

    private static List<ParamConfigHolder> getParam(Annotation... annotations) {
        List<ParamConfigHolder> params = new ArrayList<ParamConfigHolder>();

        for (Annotation a : annotations) {
            Param param = a.annotationType().getAnnotation(Param.class);
            if (param != null) {
                String dest = param.value(), name, defaultValue;
                Map<String,Object> metadatas = new HashMap<String,Object>();
                if (a instanceof QueryParam) {
                    name = ((QueryParam) a).value();
                    defaultValue = ((QueryParam) a).defaultValue();
                } else if (a instanceof PathParam) {
                    name = ((PathParam) a).value();
                    defaultValue = ((PathParam) a).defaultValue();
                } else if (a instanceof FormParam) {
                    name = ((FormParam) a).value();
                    defaultValue = ((FormParam) a).defaultValue();
                } else if (a instanceof HeaderParam) {
                    name = ((HeaderParam) a).value();
                    defaultValue = ((HeaderParam) a).defaultValue();
                } else if (a instanceof MatrixParam) {
                    name = ((MatrixParam) a).value();
                    defaultValue = ((MatrixParam) a).defaultValue();
                } else if (a instanceof CookieParam) {
                    name = ((CookieParam) a).value();
                    defaultValue = ((CookieParam) a).defaultValue();
                } else if (a instanceof MultiPartParam) {
                    MultiPartParam p = (MultiPartParam) a;
                    name = p.value();
                    defaultValue = p.defaultValue();
                    MultiParts.putMetaDatas(metadatas, p.contentType(), p.fileName());
                } else {
                    throw new IllegalArgumentException("Unsupported param annotation:" + a);
                }
                params.add(new ParamConfigHolder(name, defaultValue, dest, metadatas));
            }
        }
        return params;
    }

    private static class ParamConfigHolder {
        private final String name;
        private final String defaultValue;
        private final String destination;
        private final Map<String,Object> metadatas;

        private ParamConfigHolder(String name, String defaultValue, String destination, Map<String, Object> metadatas) {
            this.name = name;
            this.defaultValue = defaultValue;
            this.destination = destination;
            this.metadatas = metadatas;
        }
    }

    private static HttpMethod getHttpMethod(Annotation[] annotations, HttpMethod def) {
        for (Annotation a : annotations) {
            HttpMethod meth = a.annotationType().getAnnotation(HttpMethod.class);
            if (meth != null) return meth;
        }
        return def;
    }
}
