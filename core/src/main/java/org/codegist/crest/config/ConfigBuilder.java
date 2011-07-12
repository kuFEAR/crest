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
import org.codegist.common.lang.Strings;
import org.codegist.crest.CRestException;
import org.codegist.crest.CRestProperty;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static org.codegist.common.lang.Objects.defaultIfNull;
import static org.codegist.common.lang.Strings.defaultIfEmpty;

/**
 * Handy builders for {@link org.codegist.crest.config.DefaultInterfaceConfig}.
 * <p>Support auto empty/null ignore and defaults methods and params values at respectively interface and method levels.
 *
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 * @see org.codegist.crest.config.DefaultInterfaceConfig
 * @see org.codegist.crest.config.DefaultMethodConfig
 */
abstract class ConfigBuilder<T> {

    private final Map<String, Object> crestProperties;
    private final Map<Pattern, String> placeholders;

    ConfigBuilder(Map<String, Object> crestProperties) {
        this.crestProperties = crestProperties;

        Map<String, String> pPlaceholders = Maps.defaultsIfNull((Map<String, String>) crestProperties.get(CRestProperty.CONFIG_PLACEHOLDERS_MAP));
        this.placeholders = new HashMap<Pattern, String>();
        for (Map.Entry<String, String> entry : pPlaceholders.entrySet()) {
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
    public abstract T build();

    public Map<String, Object> getCRestProperties() {
        return crestProperties;
    }

    protected String replacePlaceholders(String str) {
        if (Strings.isBlank(str)) {
            return str;
        }
        String replaced = str;
        for (Map.Entry<Pattern, String> entry : placeholders.entrySet()) {
            Pattern placeholder = entry.getKey();
            String value = entry.getValue();
            replaced = placeholder.matcher(replaced).replaceAll(value);
        }
        return replaced.replaceAll("\\\\\\{", "{").replaceAll("\\\\\\}", "}"); // replace escaped with non escaped
    }

    protected <T> T getProperty(String key) {
        return (T) crestProperties.get(key);
    }

    protected <T> T defaultIfUndefined(T value, String defProp, T def) {
        if (value instanceof String || def instanceof String) {
            String defs = defaultIfEmpty((String) crestProperties.get(defProp), (String) def);
            return (T) defaultIfEmpty((String) value, defs);
        } else {
            return defaultIfNull(value, defaultIfNull((T) crestProperties.get(defProp), def));
        }
    }

    protected <T> T[] newInstance(Class<T>[] classes) {
        if (classes == null) {
            return null;
        }
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

    protected <T> T newInstance(Class<T> clazz) {
        if (clazz == null) {
            return null;
        }
        try {
            return newInstance(clazz.getConstructor(Map.class), crestProperties);
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

}