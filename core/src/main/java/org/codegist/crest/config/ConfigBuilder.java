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

import org.codegist.common.collect.Maps;
import org.codegist.common.lang.Objects;
import org.codegist.common.lang.Strings;
import org.codegist.crest.CRestException;
import org.codegist.crest.CRestProperty;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Handy builders for {@link org.codegist.crest.config.DefaultInterfaceConfig}.
 * <p>Support auto empty/null ignore and defaults methods and params values at respectively interface and method levels.
 *
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 * @see org.codegist.crest.config.DefaultInterfaceConfig
 * @see org.codegist.crest.config.DefaultMethodConfig
 * @see DefaultMethodParamConfig
 */
abstract class ConfigBuilder<T> {

    protected final Map<String, Object> customProperties;
    protected final Map<Pattern, String> placeholders;
    private boolean ignoreNullOrEmptyValues;

    ConfigBuilder(Map<String, Object> customProperties) {
        this.customProperties = Maps.unmodifiable(customProperties);

        Map<String, String> placeholders = Maps.defaultsIfNull((Map<String, String>) this.customProperties.get(CRestProperty.CONFIG_PLACEHOLDERS_MAP));
        this.placeholders = new HashMap<Pattern, String>();
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            String placeholder = entry.getKey();
            String value = entry.getValue().replaceAll("\\$", "\\\\\\$");
            this.placeholders.put(Pattern.compile("\\{" + Pattern.quote(placeholder) + "\\}"), value);
        }
    }

    /**
     * Validate and build a normal config with defaulted values if necessary
     *
     * @return config
     */
    public T build() {
        return build(true, false);
    }

    /**
     * Build a template config. No default values used. To be used to override another config
     *
     * @return config template
     */
    public T buildTemplate() {
        return build(false, true);
    }

    /**
     * Build a normal config with defaulted values if necessary. Validate the config if specified
     *
     * @param validateConfig flag that indicates if the config should be validated
     * @return config
     */
    public T build(boolean validateConfig) {
        return build(validateConfig, false);
    }

    /**
     * Build a normal config with defaulted values if necessary. No validation occurs
     *
     * @return config
     */
    public T buildUnvalidatedConfig() {
        return build(false);
    }

    /**
     * Build the config.
     * <p>If isTemplate is true, the returned config won't have any default value, should be used to override another config.
     * <p>If validate config is true (and isTemplate is false), the config is validated to ensure required information have been given
     *
     * @param validateConfig if true, config is validated
     * @param isTemplate     if true, return a config template
     * @return config
     */
    public abstract T build(boolean validateConfig, boolean isTemplate);


    public ConfigBuilder setIgnoreNullOrEmptyValues(boolean ignoreNullOrEmptyValues) {
        this.ignoreNullOrEmptyValues = ignoreNullOrEmptyValues;
        return this;
    }

    protected String replacePlaceholders(String str) {
        if (Strings.isBlank(str)) return str;
        for (Map.Entry<Pattern, String> entry : placeholders.entrySet()) {
            Pattern placeholder = entry.getKey();
            String value = entry.getValue();
            str = placeholder.matcher(str).replaceAll(value);
        }
        str = str.replaceAll("\\\\\\{", "{").replaceAll("\\\\\\}", "}"); // replace escaped with non escaped
        return str;
    }

    protected <T> T getProperty(String key) {
        return (T) customProperties.get(key);
    }

    protected <T> T defaultIfUndefined(T value, String defProp, T def) {
        if (value instanceof String || def instanceof String) {
            String defs = Strings.defaultIfBlank((String) customProperties.get(defProp), (String) def);
            return (T) Strings.defaultIfBlank((String) value, defs);
        } else {
            def = Objects.defaultIfNull((T) customProperties.get(defProp), def);
            return Objects.defaultIfNull(value, def);
        }
    }

    protected <K, V> Map<K, V> newInstance(Map<K, Class<? extends V>> model) {
        Map<K, V> map = new HashMap<K, V>();
        for (Map.Entry<K, Class<? extends V>> entry : model.entrySet()) {
            map.put(entry.getKey(), newInstance(entry.getValue()));
        }
        return map;
    }

    protected <T> T[] newInstance(Class<T>[] classes) {
        if (classes == null) return null;
        try {
            T[] instances = (T[]) java.lang.reflect.Array.newInstance(classes.getClass().getComponentType(), classes.length);
            for (int i = 0; i < instances.length; i++) {
                instances[i] = newInstance(classes[i]);
            }
            return instances;
        } catch (Exception e) {
            throw CRestException.handle(e);
        }
    }

    protected <T> Class<? extends T> forName(String clazz){
        try {
            return (Class<T>) Class.forName(replacePlaceholders(clazz));
        } catch (ClassNotFoundException e) {
            throw CRestException.handle(e);
        }
    }

    protected <T> T newInstance(Class<T> clazz) {
        if (clazz == null) return null;
        try {
            return newInstance(clazz.getConstructor(Map.class), customProperties);
        } catch (CRestException e) {
            throw e;
        } catch (Exception e) {
            try {
                return newInstance(clazz.getConstructor());
            } catch (Exception e1) {
                throw CRestException.handle(e1);
            }
        }
    }

    private <T> T newInstance(Constructor<T> constructor, Object... args) throws Exception {
        try {
            return constructor.newInstance(args);
        } catch (InvocationTargetException e) {
            throw CRestException.handle(e);
        }
    }

    protected boolean ignore(Object value) {
        if (!ignoreNullOrEmptyValues) return false;
        return (value == null || (value instanceof String && Strings.isBlank((String) value)));
    }

}