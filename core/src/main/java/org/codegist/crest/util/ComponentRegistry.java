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

package org.codegist.crest.util;

import org.codegist.crest.CRestConfig;
import org.codegist.crest.CRestException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author laurent.gilles@codegist.org
 */
public final class ComponentRegistry<K,T> {

    private final Map<K, ItemDescriptor<T>> mapping;
    private final Map<K, T> cache = new HashMap<K, T>();
    private final ItemDescriptor<T> defaultIfNotFoundDescriptor;
    private final CRestConfig crestConfig;
    private T defaultIfNotFound;

    ComponentRegistry(Map<K, ItemDescriptor<T>> mapping, CRestConfig crestConfig, ItemDescriptor<T> defaultIfNotFoundDescriptor) {
        this.defaultIfNotFoundDescriptor = defaultIfNotFoundDescriptor;
        this.mapping = mapping;
        this.crestConfig = crestConfig;
    }

    public boolean contains(K key) {
        return mapping.containsKey(key);
    }

    public T get(K key) {
        T item = cache.get(key);
        if (item == null) {
            item = buildAndCache(key, crestConfig);
        }
        return item;
    }

    private synchronized T buildAndCache(K key, CRestConfig crestConfig) {
        T value;
        ItemDescriptor<T> item = mapping.get(key);
        if (item != null) {
            value = item.instanciate(crestConfig);
        }else if(defaultIfNotFound != null) {
            value = defaultIfNotFound;
        }else if(defaultIfNotFoundDescriptor != null) {
            value = defaultIfNotFound = defaultIfNotFoundDescriptor.instanciate(crestConfig);
        }else{      
            throw new CRestException("No item bound to key: " + key);
        }
        cache.put(key, value);
        return value;
    }

    static final class ItemDescriptor<T> {
        private final Class<? extends T> clazz;
        private final Map<String, Object> config;

        ItemDescriptor(Class<? extends T> clazz, Map<String, Object> config) {
            this.clazz = clazz;
            this.config = config;
        }

        T instanciate(CRestConfig crestConfig) {
            try {
                CRestConfig merged = config.isEmpty() ? crestConfig : crestConfig.merge(config) ;
                return ComponentFactory.instantiate(clazz, merged);
            } catch (Exception e) {
                throw CRestException.handle(e);
            }
        }
    }


    public static final class Builder<K,T> {

        private final Map<K, ItemDescriptor<T>> mapping = new HashMap<K, ItemDescriptor<T>>();
        private ItemDescriptor<T> defaultIfNotFoundDescriptor;

        public ComponentRegistry<K,T> build(CRestConfig crestConfig) {
            return new ComponentRegistry<K,T>(mapping, crestConfig, defaultIfNotFoundDescriptor);
        }

        public Builder<K,T> register(Class<? extends T> item, K... keys) {
            return register(item, keys, Collections.<String, Object>emptyMap());
        }

        public Builder<K,T> register(Class<? extends T> item, K[] keys, Map<String, Object> itemConfig) {
            ItemDescriptor descriptor = new ItemDescriptor<T>(item, itemConfig);
            for (K mt : keys) {
                mapping.put(mt, descriptor);
            }
            return this;
        }

        public Builder<K,T> register(Map<K, Class<? extends T>> mapping) {
            for (Map.Entry<K, Class<? extends T>> e : mapping.entrySet()) {
                register(e.getValue(), e.getKey());
            }
            return this;
        }

        public Builder<K,T> defaultAs(Class<? extends T> item) {
            return defaultAs(item, Collections.<String, Object>emptyMap());
        }

        public Builder<K,T> defaultAs(Class<? extends T> item, Map<String, Object> itemConfig) {
            this.defaultIfNotFoundDescriptor = new ItemDescriptor<T>(item, itemConfig);
            return this;
        }
    }
}

