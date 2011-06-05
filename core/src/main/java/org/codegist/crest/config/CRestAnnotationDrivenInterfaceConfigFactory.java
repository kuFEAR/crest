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
import org.codegist.crest.CRestContext;
import org.codegist.crest.HttpRequest;
import org.codegist.crest.MultiParts;
import org.codegist.crest.annotate.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

/**
 * <p>Annotation based config factory of any possible interfaces given to the factory.
 * <p>The factory will lookup any annotation in package {@link org.codegist.crest.annotate} on to the given interface.
 * <p/>
 * <p>- Each config fallback from param to method to interface until one config is found, otherwise defaults to any respective default value ({@link org.codegist.crest.config.InterfaceConfig}, {@link MethodConfig}, {@link PropertiesDrivenInterfaceConfigFactory}).
 *
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 * @see org.codegist.crest.config.InterfaceConfig
 * @see org.codegist.crest.annotate
 */
public class CRestAnnotationDrivenInterfaceConfigFactory implements InterfaceConfigFactory {

    private final boolean buildTemplates;
    private final boolean modelPriority = false; // todo

    public CRestAnnotationDrivenInterfaceConfigFactory(boolean buildTemplates) {
        this.buildTemplates = buildTemplates;
    }

    public CRestAnnotationDrivenInterfaceConfigFactory() {
        this(false);
    }

    // TODO handle @MultiPartParam(s)
    public InterfaceConfig newConfig(Class<?> interfaze, CRestContext context) throws ConfigFactoryException {
        try {
            /* Interface specifics */
            EndPoint endPoint = interfaze.getAnnotation(EndPoint.class);
            Path path = interfaze.getAnnotation(Path.class);
            Encoding encoding = interfaze.getAnnotation(Encoding.class);
            GlobalInterceptor globalInterceptor = interfaze.getAnnotation(GlobalInterceptor.class);

            /* Methods defaults */
            SocketTimeout socketTimeout = interfaze.getAnnotation(SocketTimeout.class);
            ConnectionTimeout connectionTimeout = interfaze.getAnnotation(ConnectionTimeout.class);
            RequestInterceptor interceptor = interfaze.getAnnotation(RequestInterceptor.class);
            ResponseHandler responseHandler = interfaze.getAnnotation(ResponseHandler.class);
            ErrorHandler errorHandler = interfaze.getAnnotation(ErrorHandler.class);
            RetryHandler retryHandler = interfaze.getAnnotation(RetryHandler.class);
            Accepts accepts = interfaze.getAnnotation(Accepts.class);
            ContentType contentType = interfaze.getAnnotation(ContentType.class);
            HttpMethod httpMethod = getHttpMethod(interfaze.getAnnotations(), interfaze.getAnnotation(HttpMethod.class));
            EntityWriter entityWriter = interfaze.getAnnotation(EntityWriter.class);
            Set<ParamConfig> extraParams = getExtraParamConfigs(interfaze.getAnnotations());

            /* Params defaults */
            Serializer serializer = interfaze.getAnnotation(Serializer.class);
            Encoded encoded = interfaze.getAnnotation(Encoded.class);

            InterfaceConfigBuilder config = new InterfaceConfigBuilder(interfaze, context.getProperties());
            for (ParamConfig c : extraParams) {
                config.addMethodsExtraParam(c.getName(), c.getDefaultValue(), c.getDestination(), c.getMetaDatas());
            }

            if (endPoint != null) config.setMethodsEndPoint(endPoint.value());
            if (path != null) config.appendMethodsPath(path.value());
            if (encoding != null) config.setEncoding(encoding.value());
            if (globalInterceptor != null) config.setGlobalInterceptor(globalInterceptor.value());

            if (socketTimeout != null) config.setMethodsSocketTimeout(socketTimeout.value());
            if (connectionTimeout != null) config.setMethodsConnectionTimeout(connectionTimeout.value());
            if (interceptor != null) config.setMethodsRequestInterceptor(interceptor.value());
            if (responseHandler != null) config.setMethodsResponseHandler(responseHandler.value());
            if (errorHandler != null) config.setMethodsErrorHandler(errorHandler.value());
            if (retryHandler != null) config.setMethodsRetryHandler(retryHandler.value());
            if (accepts != null) config.setMethodsAccepts(accepts.value());
            if (contentType != null) config.setMethodsContentType(contentType.value());
            if (httpMethod != null) config.setMethodsHttpMethod(httpMethod.value());
            if (entityWriter != null) config.setMethodsEntityWriter(entityWriter.value());

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
                accepts = meth.getAnnotation(Accepts.class);
                contentType = meth.getAnnotation(ContentType.class);
                httpMethod = getHttpMethod(meth.getAnnotations(), meth.getAnnotation(HttpMethod.class));
                entityWriter = meth.getAnnotation(EntityWriter.class);

                /* Params defaults */
                serializer = meth.getAnnotation(Serializer.class);
                encoded = meth.getAnnotation(Encoded.class);

                MethodConfigBuilder methodConfigBuilder = config.startMethodConfig(meth);
                for (ParamConfig c : extraParams) {
                    methodConfigBuilder.addExtraParam(c.getName(), c.getDefaultValue(), c.getDestination(), c.getMetaDatas());
                }

                if (endPoint != null) methodConfigBuilder.setEndPoint(endPoint.value());
                if (path != null) methodConfigBuilder.appendPath(path.value());
                if (socketTimeout != null) methodConfigBuilder.setSocketTimeout(socketTimeout.value());
                if (connectionTimeout != null) methodConfigBuilder.setConnectionTimeout(connectionTimeout.value());
                if (interceptor != null) methodConfigBuilder.setRequestInterceptor(interceptor.value());
                if (responseHandler != null) methodConfigBuilder.setResponseHandler(responseHandler.value());
                if (errorHandler != null) methodConfigBuilder.setErrorHandler(errorHandler.value());
                if (retryHandler != null) methodConfigBuilder.setRetryHandler(retryHandler.value());
                if (accepts != null) methodConfigBuilder.setAccepts(accepts.value());
                if (contentType != null) methodConfigBuilder.setContentType(contentType.value());
                if (httpMethod != null) methodConfigBuilder.setHttpMethod(httpMethod.value());
                if (entityWriter != null) methodConfigBuilder.setEntityWriter(entityWriter.value());

                if (serializer != null) methodConfigBuilder.setParamsSerializer(serializer.value());
                methodConfigBuilder.setParamsEncoded(encoded != null);

                for (int i = 0, max = meth.getParameterTypes().length; i < max; i++) {
                    Class<?> pType = meth.getParameterTypes()[i];
                    Map<Class<? extends Annotation>, Annotation> paramAnnotations = Methods.getParamsAnnotation(meth, i);
                    MethodParamConfigBuilder methodParamConfigBuilder = methodConfigBuilder.startParamConfig(i);

                    /* Params specifics - Override user annotated config */
                    serializer = (Serializer) paramAnnotations.get(Serializer.class);
                    encoded = (Encoded) paramAnnotations.get(Encoded.class);

                    Serializer pSerializer = pType.getAnnotation(Serializer.class);
                    Encoded pEncoded = pType.getAnnotation(Encoded.class);
                    ParamConfig lowConfig = getFirstExtraParamConfig(modelPriority ? paramAnnotations.values().toArray(new Annotation[paramAnnotations.size()]) : pType.getAnnotations());
                    ParamConfig highConfig = getFirstExtraParamConfig(modelPriority ? pType.getAnnotations() : paramAnnotations.values().toArray(new Annotation[paramAnnotations.size()]));

                    Serializer lowPrioritySerializer = modelPriority ? serializer : pSerializer;
                    Serializer highPrioritySerializer = modelPriority ? pSerializer : serializer;
                    boolean lowPriorityEncoded = (modelPriority ? encoded : pEncoded) != null;
                    if(HttpRequest.DEST_HEADER.equals(lowConfig.getDestination()) || HttpRequest.DEST_COOKIE.equals(lowConfig.getDestination())) {
                        lowPriorityEncoded = true;
                    }
                    boolean highPriorityEncoded =   (modelPriority ? pEncoded : encoded) != null;
                    if(HttpRequest.DEST_HEADER.equals(highConfig.getDestination()) || HttpRequest.DEST_COOKIE.equals(highConfig.getDestination())) {
                        highPriorityEncoded = true;
                    }

                    methodParamConfigBuilder.setEncoded(lowPriorityEncoded);
                    if(lowConfig.getName() != null)  methodParamConfigBuilder.setName(lowConfig.getName());
                    if(lowConfig.getDefaultValue() != null)  methodParamConfigBuilder.setDefaultValue(lowConfig.getDefaultValue());
                    if(lowConfig.getDestination() != null)  methodParamConfigBuilder.setDestination(lowConfig.getDestination());
                    if(lowConfig.getMetaDatas() != null && !lowConfig.getMetaDatas().isEmpty())  methodParamConfigBuilder.setMetaDatas(lowConfig.getMetaDatas());
                    if (lowPrioritySerializer != null) methodParamConfigBuilder.setSerializer(lowPrioritySerializer.value());

                    methodParamConfigBuilder.setEncoded(highPriorityEncoded);
                    if(highConfig.getName() != null)  methodParamConfigBuilder.setName(highConfig.getName());
                    if(highConfig.getDefaultValue() != null)  methodParamConfigBuilder.setDefaultValue(highConfig.getDefaultValue());
                    if(highConfig.getDestination() != null)  methodParamConfigBuilder.setDestination(highConfig.getDestination());
                    if(highConfig.getMetaDatas() != null && !highConfig.getMetaDatas().isEmpty())  methodParamConfigBuilder.setMetaDatas(highConfig.getMetaDatas());
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


    private static ParamConfig getFirstExtraParamConfig(Annotation[] annotations) {
        Set<ParamConfig> config = getExtraParamConfigs(annotations);
        if (config.isEmpty()) return new DefaultParamConfig(null, null, null, null);
        return config.iterator().next();// get the first
    }

    private static Set<ParamConfig> getExtraParamConfigs(Annotation[] annotations) {
        Set<ParamConfig> params = new LinkedHashSet<ParamConfig>(getParam(annotations));

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

    private static List<ParamConfig> getParam(Annotation... annotations) {
        List<ParamConfig> params = new ArrayList<ParamConfig>();

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
                    MultiParts.putIfNotBlank(metadatas, p.contentType(), p.fileName());
                } else {
                    throw new IllegalArgumentException("Unsupported param annotation:" + a);
                }
                params.add(new DefaultParamConfig(name, defaultValue, dest, metadatas));
            }
        }
        return params;
    }

    private static HttpMethod getHttpMethod(Annotation[] annotations, HttpMethod def) {
        for (Annotation a : annotations) {
            HttpMethod meth = a.annotationType().getAnnotation(HttpMethod.class);
            if (meth != null) return meth;
        }
        return def;
    }
}
