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

package org.codegist.crest.serializer;

import org.codegist.crest.CRestException;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author laurent.gilles@codegist.org
 */
abstract class Registry<T> {

    private final Class<T> clazz;
    private final Map<String, Object> mimeTypeRegistry;
    private final Map<String, T> cache = new HashMap<String, T>();
    protected final Map<String,Object> customProperties;

    protected Registry(Class<T> clazz, Map<String, Object> mimeTypeRegistry, Map<String, Object> customProperties) {
        this.clazz = clazz;
        this.mimeTypeRegistry = mimeTypeRegistry;
        this.customProperties = customProperties;
    }

    public T getForMimeType(String mimeType) {
        T iten = cache.get(mimeType);
        if (iten == null) {
            synchronized (cache) {
                iten = cache.get(mimeType);
                if(iten == null) {
                    iten = build(mimeType);
                    cache(mimeType, iten);
                }
            }
        }
        return iten;
    }

    private void cache(String mimeType, T item) {
        cache.put(mimeType, item);
        for(Map.Entry<String,Object> entry : mimeTypeRegistry.entrySet()){
            if(entry.getValue() instanceof Class && entry.getValue().equals(entry.getClass())) {
                cache.put(entry.getKey(), item);
            }
        }
    }

    private T build(String mimeType) {
        Object item = mimeTypeRegistry.get(mimeType);
        if (item == null) throw new CRestException("No item bound to mime type: " + mimeType);
        if (clazz.isAssignableFrom(item.getClass())) {
            return (T) item;
        } else if (item instanceof ItemDescriptor) {
            return ((ItemDescriptor<T>) item).instanciate();
        } else {
            throw new IllegalStateException("Shouldn't be here");
        }
    }


    static class ItemDescriptor<T> {
        final Class<? extends T> clazz;
        final Map<String, Object> config;

        ItemDescriptor(Class<? extends T> clazz, Map<String, Object> config) {
            this.clazz = clazz;
            this.config = config;
        }

        public T instanciate() {
            try {
                return clazz.getConstructor(Map.class).newInstance(config);
            } catch (InvocationTargetException e) {
                throw new SerializerException(e.getMessage(), e.getCause());
            } catch (NoSuchMethodException e) {
                try {
                    return clazz.getConstructor().newInstance();
                } catch (InvocationTargetException e1) {
                    throw new SerializerException(e1.getMessage(), e1.getCause());
                } catch (Exception e1) {
                    throw new SerializerException("Class " + clazz + " doesn't have neither default contructor or a Map argument constructor!", e1);
                }
            } catch (Exception e) {
                throw new SerializerException(e);
            }
        }
    }

    public static abstract class Builder<T> {

        protected final Map<String, Object> mimeTypeRegistry = new HashMap<String, Object>();

        public abstract Registry<T> build(Map<String,Object> customProperties);

        public Builder<T> register(Class<? extends T> item, String... mimeTypes) {
            return register(item, mimeTypes, Collections.<String, Object>emptyMap());
        }
        public Builder<T> register(Class<? extends T> item, String[] mimeTypes, Map<String, Object> itemConfig) {
            for (String mt : mimeTypes) {
                mimeTypeRegistry.put(mt, new ItemDescriptor<T>(item, itemConfig));
            }
            return this;
        }

        public Builder<T> register(T item, String... mimeTypes) {
            for (String mt : mimeTypes) {
                mimeTypeRegistry.put(mt, item);
            }
            return this;
        }

    }
}

