/*
 * Copyright 2010 CodeGist.org
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 * ===================================================================
 *
 * More information at http://www.codegist.org.
 */

package org.codegist.crest;

import java.util.Map;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public abstract class CRest {

    public abstract <T> T build(Class<T> interfaze) throws CRestException;

    public static CRest getInstance(){
        return new CRestBuilder().build();
    }

    public static CRest getInstance(String endpoint){
        return endpoint(endpoint).build();
    }

    public static CRest getInstance(Map<String,String> placeholders){
        return placeholders(placeholders).build();
    }

    public static CRest getOAuthInstance(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret){
        return oauth(consumerKey, consumerSecret, accessToken, accessTokenSecret).build();
    }

    public static CRest getOAuthInstance(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret, String sessionHandle, String accessTokenRefreshUrl) {
        return oauth(consumerKey, consumerSecret, accessToken, accessTokenSecret, sessionHandle, accessTokenRefreshUrl).build();
    }

    public static CRest getBasicAuthInstance(String username, String password){
        return basicAuth(username, password).build();
    }

    public static CRestBuilder endpoint(String endpoint) {
        return new CRestBuilder().endpoint(endpoint);
    }

    public static CRestBuilder property(String name, String value){
        return new CRestBuilder().property(name, value);
    }

    public static CRestBuilder placeholder(String name, String value){
        return new CRestBuilder().placeholder(name, value);
    }

    public static CRestBuilder placeholders(Map<String,String> placeholders){
        return new CRestBuilder().setPlaceholders(placeholders);
    }

    public static CRestBuilder oauth(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret){
        return new CRestBuilder().oauth(consumerKey, consumerSecret, accessToken, accessTokenSecret);
    }

    public static CRestBuilder oauth(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret, String sessionHandle, String accessTokenRefreshUrl){
        return new CRestBuilder().oauth(consumerKey, consumerSecret, accessToken, accessTokenSecret, sessionHandle, accessTokenRefreshUrl);
    }

    public static CRestBuilder basicAuth(String username, String password){
        return new CRestBuilder().basicAuth(username, password);
    }
}
