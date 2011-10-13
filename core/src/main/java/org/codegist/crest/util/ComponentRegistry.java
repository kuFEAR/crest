/*
 * Copyright 2011 CodeGist.org
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
import java.util.concurrent.atomic.AtomicReference;

/**
 * <p>Lazy CRest Component registry</p>
 * <p>Contains a map of CRest Component classes. CRest Components are instantiated the first time they are requested and internally cached to be re-used on following requests.</p>
 * @see org.codegist.crest.annotate.CRestComponent
 * @see org.codegist.crest.util.ComponentFactory
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

    /**
     * Checks if the registry contains the given key
     * @param key key to look-up
     * @return true if exists
     */
    public boolean contains(K key) {
        return mapping.containsKey(key);
    }

    /**
     * <p>Return the CRest Component associated with the key</p>
     * @param key CRest Component's key
     * @return the CRest Component
     */
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
            defaultIfNotFound = defaultIfNotFoundDescriptor.instanciate(crestConfig);
            value = defaultIfNotFound;
        }else{      
            throw new CRestException("No item bound to key: " + key);
        }
        cache.put(key, value);
        return value;
    }

    static final class ItemDescriptor<T> {
        private final Class<? extends T> clazz;
        private final Map<String, Object> config;
        private final AtomicReference<T> instanceRef = new AtomicReference<T>();

        ItemDescriptor(Class<? extends T> clazz, Map<String, Object> config) {
            this.clazz = clazz;
            this.config = config;
        }

        T instanciate(CRestConfig crestConfig) {
            T instance = instanceRef.get();
            if(instance != null) {
                return instance;
            }
            try {
                CRestConfig merged = config.isEmpty() ? crestConfig : crestConfig.merge(config) ;
                instance = ComponentFactory.instantiate(clazz, merged);
                instanceRef.set(instance);
                return instance;
            } catch (Exception e) {
                throw CRestException.handle(e);
            }
        }
    }

    public static final class Builder<K,T> {

        private final Map<K, ItemDescriptor<T>> mapping = new HashMap<K, ItemDescriptor<T>>();
        private ItemDescriptor<T> defaultIfNotFoundDescriptor;

        /**
         * <p>Builds the ComponentRegistry with the given CRestConfig.</p>
         * <p>If a component has been registred with its own config map, the given CRestConfig will be merged with the given map and passed to the CRestConfig aware component when it will be instantiated.</p>
         * @param crestConfig CRestConfig to pass the any CRestConfig aware component
         * @return the component registry
         * @see org.codegist.crest.util.ComponentFactory
         */
        public ComponentRegistry<K,T> build(CRestConfig crestConfig) {
            return new ComponentRegistry<K,T>(mapping, crestConfig, defaultIfNotFoundDescriptor);
        }

        /**
         * Registers a component class for the given key list
         * @param item component class, can be any class as long as it follow the rules of {@link org.codegist.crest.annotate.CRestComponent}.
         * @param keys key to map the crest component to
         * @return current builder
         * @see org.codegist.crest.util.ComponentFactory
         */
        public Builder<K,T> register(Class<? extends T> item, K... keys) {
            return register(item, keys, Collections.<String, Object>emptyMap());
        }

        /**
         * Registers a component class for the given key list
         * @param item component class, can be any class as long as it follow the rules of {@link org.codegist.crest.annotate.CRestComponent}.
         * @param keys key to map the crest component to
         * @param itemConfig the component config/state. Will be merged with the CRestConfig and passed into the CRestConfig aware constructor of the component
         * @return current builder
         * @see org.codegist.crest.util.ComponentFactory
         */
        public Builder<K,T> register(Class<? extends T> item, K[] keys, Map<String, Object> itemConfig) {
            ItemDescriptor descriptor = new ItemDescriptor<T>(item, itemConfig);
            for (K mt : keys) {
                mapping.put(mt, descriptor);
            }
            return this;
        }

        /**
         * Registers a map of Keys/Components classes
         * @param mapping map of Keys/Components classes
         * @return current builder
         * @see org.codegist.crest.util.ComponentFactory
         */
        public Builder<K,T> register(Map<K, Class<? extends T>> mapping) {
            for (Map.Entry<K, Class<? extends T>> e : mapping.entrySet()) {
                register(e.getValue(), e.getKey());
            }
            return this;
        }

        /**
         * Sets the default component class when the component registry is being asked for a unknown key.
         * @param item default component
         * @return current builder
         * @see org.codegist.crest.util.ComponentFactory
         */
        public Builder<K,T> defaultAs(Class<? extends T> item) {
            return defaultAs(item, Collections.<String, Object>emptyMap());
        }

        /**
         * Sets the default component class when the component registry is being asked for a unknown key.
         * @param item default component
         * @param itemConfig the component config/state. Will be merged with the CRestConfig and passed into the CRestConfig aware constructor of the component
         * @return current builder
         * @see org.codegist.crest.util.ComponentFactory
         */
        public Builder<K,T> defaultAs(Class<? extends T> item, Map<String, Object> itemConfig) {
            this.defaultIfNotFoundDescriptor = new ItemDescriptor<T>(item, itemConfig);
            return this;
        }
    }
}

