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

import org.codegist.crest.CRestException;
import org.codegist.crest.util.ComponentFactory;

import java.lang.reflect.Array;
import java.util.Map;
import java.util.regex.Pattern;

import static org.codegist.crest.CRestProperty.get;
import static org.codegist.crest.CRestProperty.getPlaceholders;
import static org.codegist.crest.util.PlaceHolders.compile;
import static org.codegist.crest.util.PlaceHolders.merge;

/**
 * Handy builders for {@link DefaultInterfaceConfig}.
 * <p>Support auto empty/null ignore and defaults methods and params values at respectively interface and method levels.
 *
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 * @see DefaultInterfaceConfig
 * @see DefaultMethodConfig
 */
abstract class ConfigBuilder {

    private final Map<String, Object> crestProperties;
    private final Map<Pattern, String> placeholders;

    ConfigBuilder(Map<String, Object> crestProperties) {
        this.crestProperties = crestProperties;
        this.placeholders = compile(getPlaceholders(crestProperties));
    }

    public Map<String, Object> getCRestProperties() {
        return crestProperties;
    }

    protected String ph(String str) {
        return merge(placeholders, str);
    }

    protected <T> T[] defaultIfUndefined(T[] value, String defProp, Class<? extends T>[] def) {
        if(value != null) {
            return value;
        }else{
            T[] prop = get(crestProperties, defProp);
            return (prop != null) ? prop : newInstance(def);
        }
    }

    protected <T> T defaultIfUndefined(T value, String defProp, Class<? extends T> def)  {
        if(value != null) {
            return value;
        }else{
            Object prop = get(crestProperties, defProp);
            if(prop != null) {
                return (prop instanceof Class) ? newInstance((Class<T>)prop) : (T) prop;
            }else{
                return newInstance(def);
            }
        }
    }

    protected <T> T[] defaultIfUndefined(T[] value, String defProp, T[] def) {
        return (value != null && value.length > 0) ? value : get(crestProperties, defProp, def);
    }

    @SuppressWarnings("unchecked")
    protected <T> T defaultIfUndefined(T value, String defProp, T def) {
        return value != null ? value : get(crestProperties, defProp, def);
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