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

import org.codegist.crest.config.ParamConfig;
import org.codegist.crest.param.Param;

import java.util.HashMap;
import java.util.Map;

import static org.codegist.common.lang.Strings.isNotBlank;

/**
 * @author laurent.gilles@codegist.org
 */
public final class MultiParts {

    public static final String CONTENT_TYPE = "multipart.content-type";
    public static final String FILENAME = "multipart.filename";
    public static final String MULTIPART_FLAG = "multipart.flag";

    private MultiParts(){
        throw new IllegalStateException();
    }

    public static String getContentType(ParamConfig paramConfig){
        return (String) paramConfig.getMetaDatas().get(CONTENT_TYPE);
    }

    public static String getFileName(ParamConfig paramConfig){
        return (String) paramConfig.getMetaDatas().get(FILENAME);
    }

    public static String getContentType(Param param){
        return getContentType(param.getParamConfig());
    }

    public static String getFileName(Param param){
        return getFileName(param.getParamConfig());
    }

    public static boolean hasMultiPart(Map<String,Object> metadatas){
        return metadatas.containsKey(MULTIPART_FLAG);
    }
    
    public static boolean hasMultiPart(Param param){
        return hasMultiPart(param.getParamConfig().getMetaDatas());
    }

    public static void putMetaDatas(Map<String, Object> metadatas, String contentType, String fileName){
        metadatas.put(MULTIPART_FLAG, true);
        if(isNotBlank(contentType)) {
            metadatas.put(CONTENT_TYPE, contentType);
        }
        if(isNotBlank(fileName)) {
            metadatas.put(FILENAME, fileName);
        }
    }

    public static Map<String,Object> toMetaDatas(String contentType, String fileName){
        Map<String,Object> metadatas = new HashMap<String, Object>();
        putMetaDatas(metadatas, contentType, fileName);
        return metadatas;
    }
}
