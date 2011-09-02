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

package org.codegist.crest.entity;

import org.codegist.crest.CRestConfig;
import org.codegist.crest.serializer.Serializer;
import org.codegist.crest.util.ComponentRegistry;

/**
 * @author laurent.gilles@codegist.org
 */
public class JsonEntityWriter extends SerializingEntityWriter {

    public static final String MIME  = "application/form-jsonencoded";
    private static final String CONTENT_TYPE = "application/json";

    public JsonEntityWriter(CRestConfig crestConfig) {
        super(getSerializer(crestConfig), CONTENT_TYPE);
    }

    private static Serializer getSerializer(CRestConfig crestConfig){
        ComponentRegistry<String,Serializer> registryMime = crestConfig.get(ComponentRegistry.class.getName() + "#serializers-per-mime");
        return registryMime.get(MIME);
    }
}
