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

package org.codegist.crest.serializer.primitive;

import java.util.Map;

import static org.codegist.common.lang.Strings.defaultIfBlank;
import static org.codegist.common.lang.Strings.isBlank;
import static org.codegist.crest.CRestProperty.CREST_BOOLEAN_TRUE;

/**
 * @author laurent.gilles@codegist.org
 */
public class BooleanWrapperDeserializer extends PrimitiveDeserializer<Boolean> {
    public static final String DEFAULT_TRUE = "true";
    private final String trueString;

    public BooleanWrapperDeserializer() {
        this(DEFAULT_TRUE);
    }
    public BooleanWrapperDeserializer(Map<String,Object> crestProperties) {
        this(defaultIfBlank((String) crestProperties.get(CREST_BOOLEAN_TRUE), DEFAULT_TRUE));
    }
    public BooleanWrapperDeserializer(String trueString) {
        this.trueString = trueString;
    }

    public Boolean deserialize(String value) {
        return isBlank(value) ? null : trueString.equals(value);
    }

}
