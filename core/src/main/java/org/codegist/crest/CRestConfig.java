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

import java.util.Map;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public interface CRestConfig {
    
    String CREST_CONCURRENCY_LEVEL = CRestConfig.class.getName() + "#concurrency-level";
    String CREST_DATE_FORMAT = CRestConfig.class.getName() + "#date-format";
    String CREST_BOOLEAN_TRUE = CRestConfig.class.getName() + "#boolean-format.true";
    String CREST_BOOLEAN_FALSE = CRestConfig.class.getName() + "#boolean-format.false";
    String CREST_MAX_ATTEMPTS = CRestConfig.class.getName() + "#retry-attempts";

    CRestConfig merge(Map<String, Object> m);

    <T> T get(String key);

    <T> T get(String key, T defaultIfNotFound);

    <T> T get(Class<?> key);

    int getMaxAttempts();

    String getDateFormat();

    String getBooleanTrue();

    String getBooleanFalse();

    int getConcurrencyLevel();
}
