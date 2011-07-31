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

package org.codegist.crest;

import org.codegist.common.collect.Maps;

import java.util.Collections;
import java.util.Map;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
final class DefaultCRestConfig implements CRestConfig {

    private final Map<String,Object> config;

    public DefaultCRestConfig() {
        this(Collections.<String, Object>emptyMap());
    }
    public DefaultCRestConfig(Map<String, Object> m) {
        this.config = m;
    }

    public CRestConfig merge(Map<String, Object> m){
        return new DefaultCRestConfig(Maps.merge(this.config, m));
    }

    public <T> T get(String key) {
        return (T) config.get(key);
    }

    public <T> T get(String propName, T defaultIfNotFound){
        return config.containsKey(propName) ? this.<T>get(propName) : defaultIfNotFound;
    }

    public <T> T get(Class<?> key){
        return this.<T>get(key, (T) null);
    }

    public <T> T get(Class<?> key, T defaultIfNotFound) {
        return this.<T>get(key.getName(), defaultIfNotFound);
    }

    public int getMaxAttempts(){
        return get(CREST_MAX_ATTEMPTS, 1);
    }

    public String getDateFormat(){
        return get(CREST_DATE_FORMAT, "yyyy-MM-dd'T'HH:mm:ssZ");
    }

    public String getBooleanTrue(){
        return get(CREST_BOOLEAN_TRUE, "true");
    }

    public String getBooleanFalse(){
        return get(CREST_BOOLEAN_FALSE, "false");
    }

    public int getConcurrencyLevel(){
        return get(CREST_CONCURRENCY_LEVEL, 1);
    }


    

}
