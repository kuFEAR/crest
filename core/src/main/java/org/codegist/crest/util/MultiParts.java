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
 * Set of utility function to deal with {@link org.codegist.crest.config.ParamConfigBuilder#setMetaDatas(java.util.Map)} and {@link org.codegist.crest.config.ParamConfig#getMetaDatas()} for multipart.
 * @author laurent.gilles@codegist.org
 */
public final class MultiParts {

    private static final String CONTENT_TYPE = "multipart.content-type";
    private static final String FILENAME = "multipart.filename";
    private static final String MULTIPART_FLAG = "multipart.flag";

    private MultiParts(){
        throw new IllegalStateException();
    }

    /**
     * Extracts the multipart content-type meta data
     * @param paramConfig config to extract it from
     * @return the content-type or null
     */
    public static String getContentType(ParamConfig paramConfig){
        return (String) paramConfig.getMetaDatas().get(CONTENT_TYPE);
    }

    /**
     * Extracts the multipart file name meta data
     * @param paramConfig config to extract it from
     * @return the file name or null
     */
    public static String getFileName(ParamConfig paramConfig){
        return (String) paramConfig.getMetaDatas().get(FILENAME);
    }

    /**
     * Extracts the multipart content-type meta data
     * @param param param to extract it from
     * @return the content-type or null
     */
    public static String getContentType(Param param){
        return getContentType(param.getParamConfig());
    }

    /**
     * Extracts the multipart file name meta data
     * @param param param to extract it from
     * @return the file name or null
     */
    public static String getFileName(Param param){
        return getFileName(param.getParamConfig());
    }

    /**
     * Checks if the given param config array contains a param with multipart meta datas.
     * @param paramConfigs param config array to look for multipart meta datas
     * @return true if multipart metadata found
     */
    public static boolean hasMultiPart(ParamConfig[] paramConfigs){
        for(ParamConfig cfg : paramConfigs){
            if(hasMultiPart(cfg.getMetaDatas())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the given metadata maps has multipart metadatas.
     * @param metadatas map to look for multipart metadata
     * @return true if multipart metadata found
     */
    public static boolean hasMultiPart(Map<String,Object> metadatas){
        return metadatas.containsKey(MULTIPART_FLAG);
    }

    /**
     * Checks if the given param has multipart metadatas.
     * @param param param to look for multipart metadata
     * @return true if multipart metadata found
     */
    public static boolean hasMultiPart(Param param){
        return hasMultiPart(param.getParamConfig().getMetaDatas());
    }

    /**
     * Puts multipart metadata informations into the given metadata map
     * @param metadatas map to put the metadata into
     * @param contentType multipart content-type. can be null or empty
     * @param fileName multipart file name. can be null or empty
     */
    public static void putMetaDatas(Map<String, Object> metadatas, String contentType, String fileName){
        metadatas.put(MULTIPART_FLAG, true);
        if(isNotBlank(contentType)) {
            metadatas.put(CONTENT_TYPE, contentType);
        }
        if(isNotBlank(fileName)) {
            metadatas.put(FILENAME, fileName);
        }
    }

    /**
     * Returns a multipart metadata map
     * @param contentType multipart content-type. can be null or empty
     * @param fileName multipart file name. can be null or empty
     */
    public static Map<String,Object> toMetaDatas(String contentType, String fileName){
        Map<String,Object> metadatas = new HashMap<String, Object>();
        putMetaDatas(metadatas, contentType, fileName);
        return metadatas;
    }

    /**
     * Set the multipart metadata flag into the given map
     * @param metadatas map to put the multipart metadata flag into
     */
    public static void asMultipart(Map<String, Object> metadatas){
        metadatas.put(MULTIPART_FLAG, true);
    }
}
