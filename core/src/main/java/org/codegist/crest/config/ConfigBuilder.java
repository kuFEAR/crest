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

import org.codegist.common.lang.Strings;
import org.codegist.crest.CRestException;
import org.codegist.crest.CRestProperty;
import org.codegist.crest.util.ComponentFactory;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static org.codegist.common.lang.Objects.defaultIfNull;
import static org.codegist.common.lang.Strings.defaultIfEmpty;
import static org.codegist.crest.CRestProperty.get;
import static org.codegist.crest.CRestProperty.getPlaceholders;

/**
 * // TODO things are a bit messy here, refactor it!
 * Handy builders for {@link DefaultInterfaceConfig}.
 * <p>Support auto empty/null ignore and defaults methods and params values at respectively interface and method levels.
 *
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 * @see DefaultInterfaceConfig
 * @see DefaultMethodConfig
 */
abstract class ConfigBuilder {

    private final Map<String, Object> crestProperties;
    private final Map<Pattern, String> placeholders = new HashMap<Pattern, String>();

    ConfigBuilder(Map<String, Object> crestProperties) {
        this.crestProperties = crestProperties;
        for (Map.Entry<String, String> entry : getPlaceholders(crestProperties).entrySet()) {
            String placeholder = entry.getKey();
            String value = entry.getValue().replaceAll("\\$", "\\\\\\$");
            this.placeholders.put(Pattern.compile("\\{" + Pattern.quote(placeholder) + "\\}"), value);
        }
    }

    public Map<String, Object> getCRestProperties() {
        return crestProperties;
    }

    protected String ph(String str) {
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

    protected <T> T[] defaultIfUndefined(T[] value, String defProp, Class<? extends T>[] def) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if(value == null) {
            T[] prop = CRestProperty.<T[]>get(crestProperties, defProp);
            if(prop != null) {
                return prop;
            }else{
                return newInstance(def);
            }
        }else{
            return value;
        }
    }
    protected <T> T defaultIfUndefined(T value, String defProp, Class<? extends T> def)  {
        if(value == null) {
            Object prop = CRestProperty.get(crestProperties, defProp);
            if(prop != null) {
                if(prop instanceof Class) {
                    return newInstance((Class<T>)prop);
                }else{
                    return (T) prop;
                }
            }else{
                return newInstance(def);
            }
        }else{
            return value;
        }
    }

    protected <T> T[] defaultIfUndefined(T[] value, String defProp, T[] def) {
        if(value == null || value.length == 0) {
            T[] prop = CRestProperty.<T[]>get(crestProperties, defProp);
            if(prop != null) {
                return prop;
            }else{
                return def;
            }
        }else{
            return value;
        }
    }

    @SuppressWarnings("unchecked")
    protected <T> T defaultIfUndefined(T value, String defProp, T def) {
        if (value instanceof String || def instanceof String) {
            String defs = get(crestProperties, defProp, (String) def);
            return (T) defaultIfEmpty((String) value, defs);
        } else {
            return defaultIfNull(value, get(crestProperties, defProp, def));
        }
    }

    @SuppressWarnings("unchecked")
    protected <T> T[] newInstance(Class<T>[] classes)  {
        if (classes == null) {
            return null;
        }
        T[] instances = (T[]) Array.newInstance(classes.getClass().getComponentType(), classes.length);
        for (int i = 0; i < instances.length; i++) {
            instances[i] = newInstance(classes[i]);
        }
        return instances;
    }

    protected <T> T newInstance(Class<T> clazz) {
        if (clazz == null) {
            return null;
        }else{
            try {
                return ComponentFactory.instantiate(clazz, crestProperties);
            } catch (Exception e) {
                throw CRestException.handle(e);
            }
        }
    }
    
}