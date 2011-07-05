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

import org.codegist.crest.serializer.Registry;
import org.codegist.crest.serializer.Serializer;

import java.util.Map;

/**
 * @author laurent.gilles@codegist.org
 */
public class JsonEntityWriter extends SerializingEntityWriter {

    private static final String MIME  = "application/form-jsonencoded";
    private static final String CONTENT_TYPE = "application/json";

    public JsonEntityWriter(Map<String,Object> customProperties) {
        super(getSerializer(customProperties), CONTENT_TYPE);
    }

    private static Serializer getSerializer(Map<String,Object> customProperties){
        Registry<String,Serializer> registryMime = (Registry<String,Serializer>) customProperties.get(Registry.class.getName() + "#serializers-per-mime");
        return registryMime.getFor(MIME);
    }
}
