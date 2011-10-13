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

package org.codegist.crest;

import java.util.Map;

/**
 * <p>Config object that maintains the internal state of <b>CRest</b>.</p>
 * <p>Note: any custom object added through:</p>
 * <code><pre>
 * CRest.property(...)
 * CRestBuilder.property(...)
 * CRestBuilder.addProperties(...)
 * CRestBuilder.setProperties(...)
 * </pre></code>
 * <p>Will be available through it's access methods.</p>
 * <p>Custom user state can be passed through it to any user custom components as an instance of this object will be injected into any custom user components if the user implementation have a single argument constructor that takes a CRestConfig parameter.</p>
 * @see org.codegist.crest.CRest#property(String, Object)
 * @see org.codegist.crest.CRestBuilder#property(String, Object)
 * @see org.codegist.crest.CRestBuilder#addProperties(java.util.Map)
 * @see org.codegist.crest.CRestBuilder#setProperties(java.util.Map)
 * @see org.codegist.crest.annotate.CRestComponent
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public interface CRestConfig {

    /**
     * <p>Property name for setting <b>CRest</b>'s components Concurrency Level (default is 1)</p>
     * <p>Expects an Integer</p>
     */
    String CREST_CONCURRENCY_LEVEL = CRestConfig.class.getName() + "#concurrency-level";

    /**
     * <p>Property name for setting <b>CRest</b>'s date format for serialization (default is "yyyy-MM-dd'T'HH:mm:ssZ")</p>
     * <p>Expects a valid date format string</p>
     */
    String CREST_DATE_FORMAT = CRestConfig.class.getName() + "#date-format";

    /**
     * <p>Property name for setting <b>CRest</b>'s boolean format for TRUE for serialization (default is "true")</p>
     * <p>Expects a String</p>
     */
    String CREST_BOOLEAN_TRUE = CRestConfig.class.getName() + "#boolean-format.true";

    /**
     * <p>Property name for setting <b>CRest</b>'s boolean format for FALSE for serialization (default is "false")</p>
     * <p>Expects a String</p>
     */
    String CREST_BOOLEAN_FALSE = CRestConfig.class.getName() + "#boolean-format.false";

    /**
     * <p>Property name for setting <b>CRest</b>'s maximym retry attempts on request failure (default is 1, meaning no retry)</p>
     * <p>Expects an Integer</p>
     */
    String CREST_MAX_ATTEMPTS = CRestConfig.class.getName() + "#retry-attempts";

    /**
     * Merges the current config with the given properties (given properties have priority over config ones), returning a new config instance, leaving the current instance unchanged.
     * @param properties Map of properties to merge the current config with
     * @return a new CRestConfig, results of merging current config with given properties
     */
    CRestConfig merge(Map<String, Object> properties);

    /**
     * Lookup of a property with the given key.
     * @param key Key to lookup
     * @param <T> Type of looked-up property
     * @return The looked-up property if found, otherwise null
     */
    <T> T get(String key);

    /**
     * Lookup of a property with the given key.
     * @param key Key to lookup
     * @param defaultIfNotFound Default value of not found
     * @param <T> Type of looked-up property
     * @return The looked-up property if found, otherwise returns defaultIfNotFound
     */
    <T> T get(String key, T defaultIfNotFound);

    /**
     * Lookup of a property with the given class full qualified name.
     * @param key Class to get the full qualified name from
     * @param <T> Type of looked-up property
     * @return The looked-up property if found, otherwise null
     */
    <T> T get(Class<?> key);

    /**
     * Lookup of a property with the given class full qualified name.
     * @param key Class to get the full qualified name from
     * @param defaultIfNotFound Default value of not found
     * @param <T> Type of looked-up property
     * @return The looked-up property if found, otherwise defaultIfNotFound
     */
    <T> T get(Class<?> key, T defaultIfNotFound);

    /**
     * @return Configured max attempts on request failure
     */
    int getMaxAttempts();

    /**
     * @return Configured date format
     */
    String getDateFormat();

    /**
     * @return Configured boolean format for TRUE
     */
    String getBooleanTrue();

    /**
     * @return Configured boolean format for FALSE
     */
    String getBooleanFalse();

    /**
     * @return Configured concurrency level
     */
    int getConcurrencyLevel();
}
