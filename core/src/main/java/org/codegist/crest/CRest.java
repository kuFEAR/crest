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
 * CRest rest-bounded instances behave as follow :
 * <p>- methods with a java.io.InputStream or java.io.Reader return type are always considered as expecting the raw server response. Server InputStream/Reader is then return. It is of the responsability of the client to properly call close() on the given Stream in order to free network resources.
 * <p>- otherwise response is auto-marshalled to the method's return type.
 * <p>- method's arguments are serialized as follow for the normal default case :
 * <p>&nbsp;&nbsp;. Objects and primitives types are being serialized using the String.valueOf() method
 * <p>&nbsp;&nbsp;. Primitive Arrays/Object Arrays/Collections are serialized by calling String.valueOf() for each item and joining the result in a comma separated string.
 * <p>&nbsp;&nbsp;. java.util.Date are serialized to the ISO-8601 date format
 * @see CRest#build(Class)
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public abstract class CRest {

    /**
     * Build rest-bounded instances of the given interface
     *
     * @param interfaze Interface class to get the instance from
     * @param <T>       Interface class to get the instance from
     * @return An instance of the given interface
     * @throws CRestException if anything goes wrong
     * @see org.codegist.crest.handler.ResponseHandler
     * @see org.codegist.crest.handler.DefaultResponseHandler
     */
    public abstract <T> T build(Class<T> interfaze) throws CRestException;

    public static CRest getInstance(){
        return new CRestBuilder().build();
    }

    public static CRest getHttpClientInstance(){
        return useHttpClient().build();
    }

    public static CRest getJaxrsAwareInstance(){
        return jaxrsAware().build();
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

    public static CRestBuilder jaxrsAware(){
        return new CRestBuilder().jaxrsAware();
    }

    public static CRestBuilder useHttpClient(){
        return new CRestBuilder().useHttpClient();
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
