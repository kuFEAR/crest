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

import org.codegist.common.lang.Strings;
import org.codegist.crest.config.ParamConfig;

import java.util.Map;

/**
 * @author laurent.gilles@codegist.org
 */
public final class MultiParts {

    public static final String CONTENT_TYPE = "multipart.content-type";
    public static final String FILENAME = "multipart.filename";

    private MultiParts(){
        throw new IllegalStateException();
    }

    public static String getContentType(ParamConfig paramConfig){
        return (String) (paramConfig.getMetaDatas() == null ? null : paramConfig.getMetaDatas().get(CONTENT_TYPE));
    }

    public static String getFileName(ParamConfig paramConfig){
        return (String) (paramConfig.getMetaDatas() == null ? null : paramConfig.getMetaDatas().get(FILENAME));
    }

    public static String getContentType(Value value){
        return (String) value.getMetaData(CONTENT_TYPE);
    }

    public static String getFileName(Value value){
        return (String) value.getMetaData(FILENAME);
    }

    public static void putIfNotBlank(Map<String, Object> metadatas, String contentType, String fileName){
        if(!Strings.isBlank(CONTENT_TYPE)) {
            metadatas.put(CONTENT_TYPE, contentType);
        }
        if(!Strings.isBlank(FILENAME)) {
            metadatas.put(FILENAME, fileName);
        }
    }
}
