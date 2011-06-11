/*
 * Copyright 2010 CodeGist.org
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 * ===================================================================
 *
 * More information at http://www.codegist.org.
 */

package org.codegist.crest.config;

import java.lang.reflect.Method;
import java.util.*;

import static org.codegist.common.lang.Objects.defaultIfNull;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public final class Configs {

    private Configs() {
        throw new IllegalStateException();
    }


    /**
     * Overrides a config (overrides) with another one (base).
     * <p>The override is a config template where nulls values are legals and will fallback to the base config. Base config must apply to the general contract of {@link org.codegist.crest.config.InterfaceConfig}.
     * <p>Any non-null values in override config will take priority over base config.
     * <p>RequestInterceptor are not overriding each other but are chaining, thus if either override and base configs declare a request interceptor, both of them will run, with the override's request interceptor running before the base one.
     * <p>If dynamic flag is true, the returned config is a dynamic view over the two given config, thus the two configs can change over time and the resulting config will reflect the changes.
     *
     * @param base      Normal full configured config, respect the general contract of InterfaceConfig object
     * @param overrides Config template, can hold null values, that plays as flag to indicate a fallback to the base config
     * @return A view that gives priority of "overrides" non-null values object upon "base" object. Any changes at runtime will be reflected.
     * @see org.codegist.crest.config.InterfaceConfig
     * @throws NullPointerException if any of the two configs are null
     */
    public static InterfaceConfig override(InterfaceConfig base, InterfaceConfig overrides) {
        if(overrides == null) return base;
        Map<Method, MethodConfig> cache = new HashMap<Method, MethodConfig>();
        for (Method method : defaultIfNull(overrides.getMethods(), base.getMethods())) {
            cache.put(method, override(base.getMethodConfig(method), overrides.getMethodConfig(method)));
        }
        return new DefaultInterfaceConfig(
                defaultIfNull(overrides.getInterface(), base.getInterface()),
                defaultIfNull(overrides.getEncoding(), base.getEncoding()),
                defaultIfNull(overrides.getGlobalInterceptor(), base.getGlobalInterceptor()),
                cache
        );
    }

    /**
     * Overrides a config (overrides) with another one (base).
     * <p>The override is a config template where nulls values are legals and will fallback to the base config. Base config must apply to the general contract of {@link org.codegist.crest.config.MethodConfig}.
     * <p>Any non-null values in override config will take priority over base config.
     * <p>RequestInterceptor are not overriding each other but are chaining, thus if either override and base configs declare a request interceptor, both of them will run, with the override's request interceptor running before the base one.
     * <p>If dynamic flag is true, the returned config is a dynamic view over the two given config, thus the two configs can change over time and the resulting config will reflect the changes.
     * <p>NB: Extra params will be merged, so is override contains extraparam definitions not contained in the base config, they will be on the final config returned, and thus, they must be legal configuration object with no nulls returned.
     *
     * @param base      Normal full configured config, respect the general contract of MethodConfig object
     * @param overrides Config template, can hold null values, that plays as flag to indicate a fallback to the base config
     * @return A view that gives priority of "overrides" non-null values object upon "base" object. Any changes at runtime will be reflected.
     * @see org.codegist.crest.config.MethodConfig
     * @throws NullPointerException if any of the two configs are null
     * @throws IllegalArgumentException if an extra param found in overrides but not in the base config hasn't a proper configuration
     */
    public static MethodConfig override(MethodConfig base, MethodConfig overrides) {
        if(overrides == null) return base;
        ParamConfig[] pl = new ParamConfig[defaultIfNull(overrides.getParamCount(), base.getParamCount())];
        for (int i = 0; i < pl.length; i++) {
            pl[i] = override(base.getParamConfig(i), overrides.getParamConfig(i));
        }
        Map<String, ParamConfig> baseExtraParams = toMap(base.getExtraParams());
        Map<String, ParamConfig> overridesExtraParams = toMap(overrides.getExtraParams());
        List<ParamConfig> overridden = new ArrayList<ParamConfig>();

        for(Map.Entry<String, ParamConfig> baseP : baseExtraParams.entrySet()){
            ParamConfig overP = overridesExtraParams.get(baseP.getKey());
            if(overP == null) {
                overridden.add(baseP.getValue());
            }else{
                overridden.add(override(baseP.getValue(), overP));
            }
        }
        for(Map.Entry<String, ParamConfig> overP : overridesExtraParams.entrySet()){
            if(!baseExtraParams.containsKey(overP.getKey())) {
                if(overP.getValue().getDefaultValue() == null || overP.getValue().getName() == null || overP.getValue().getDestination() == null) {
                    throw new IllegalArgumentException("an extra param found in overrides but not in the base config has an illegal configuration");
                }
                overridden.add(overP.getValue());
            }
        }

        ParamConfig[] extras = overridden.toArray(new ParamConfig[overridden.size()]);

        return new DefaultMethodConfig(
                defaultIfNull(overrides.getMethod(), base.getMethod()),
                defaultIfNull(overrides.getPathTemplate(), base.getPathTemplate()),
                defaultIfNull(overrides.getHttpMethod(), base.getHttpMethod()),
                defaultIfNull(overrides.getSocketTimeout(), base.getSocketTimeout()),
                defaultIfNull(overrides.getConnectionTimeout(), base.getConnectionTimeout()),
                defaultIfNull(overrides.getBodyWriter(), base.getBodyWriter()),
                defaultIfNull(overrides.getRequestInterceptor(), base.getRequestInterceptor()),
                defaultIfNull(overrides.getResponseHandler(), base.getResponseHandler()),
                defaultIfNull(overrides.getErrorHandler(), base.getErrorHandler()),
                defaultIfNull(overrides.getRetryHandler(), base.getRetryHandler()),
                defaultIfNull(overrides.getDeserializers(), base.getDeserializers()),
                pl,
                extras
        );
    }

    // TODO REMOVE THAT MUST BE A MULTIMAP
    public static Map<String, ParamConfig> toMap(ParamConfig[] params){
        Map<String, ParamConfig> map = new LinkedHashMap<String, ParamConfig>();
        if(params == null) return map;
        for(ParamConfig e : params){
            map.put(e.getName(), e);
        }
        return map;
    }

    /**
     * Overrides a config (overrides) with another one (base).
     * <p>The override is a config template where nulls values are legals and will fallback to the base config. Base config must apply to the general contract of {@link ParamConfig}.
     * <p>Any non-null values in override config will take priority over base config.
     * <p>If dynamic flag is true, the returned config is a dynamic view over the two given config, thus the two configs can change over time and the resulting config will reflect the changes.
     *
     * @param base      Normal full configured config, respect the general contract of ParamConfig object
     * @param overrides Config template, can hold null values, that plays as flag to indicate a fallback to the base config
     * @return A view that gives priority of "overrides" non-null values object upon "base" object. Any changes at runtime will be reflected.
     * @see ParamConfig
     * @throws NullPointerException if any of the two configs are null
     */
    public static ParamConfig override(ParamConfig base, ParamConfig overrides) {
        if(overrides == null) return base;
        return new DefaultParamConfig(
                defaultIfNull(overrides.getValueGenericType(), base.getValueGenericType()),
                defaultIfNull(overrides.getValueClass(), base.getValueClass()),
                defaultIfNull(overrides.getName(), base.getName()),
                defaultIfNull(overrides.getDefaultValue(), base.getDefaultValue()),
                defaultIfNull(overrides.getDestination(), base.getDestination()),
                defaultIfNull(overrides.getListSeparator(), base.getListSeparator()),
                defaultIfNull(overrides.getMetaDatas(), base.getMetaDatas()),
                defaultIfNull(overrides.getSerializer(), base.getSerializer()),
                defaultIfNull(overrides.isEncoded(), base.isEncoded()),
                defaultIfNull(overrides.getAnnotations(), base.getAnnotations())
        );
    }
}

