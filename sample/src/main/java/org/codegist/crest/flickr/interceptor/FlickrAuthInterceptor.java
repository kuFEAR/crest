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

package org.codegist.crest.flickr.interceptor;

import org.codegist.common.codec.Hex;
import org.codegist.common.lang.Validate;
import org.codegist.crest.RequestContext;
import org.codegist.crest.http.HttpParam;
import org.codegist.crest.http.HttpRequest;
import org.codegist.crest.http.Pair;
import org.codegist.crest.interceptor.RequestInterceptor;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.codegist.crest.http.HttpParamProcessor.process;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class FlickrAuthInterceptor implements RequestInterceptor {

    public static final String APP_SECRET_PROP = FlickrAuthInterceptor.class.getName() + "#app.secret";
    public static final String API_KEY_PROP = FlickrAuthInterceptor.class.getName() + "#api.key";
    public static final String AUTH_TOKEN_PROP = FlickrAuthInterceptor.class.getName() + "#auth.token";

    private final String appSecret;
    private final String apiKey;
    private final String authToken;

    public FlickrAuthInterceptor(Map<String, Object> properties) {
        this.appSecret = (String) properties.get(APP_SECRET_PROP);
        this.apiKey = (String) properties.get(API_KEY_PROP);
        this.authToken = (String) properties.get(AUTH_TOKEN_PROP);

        Validate.notBlank(this.appSecret, "App secret is required, please pass it in the properties (key=" + APP_SECRET_PROP + ")");
        Validate.notBlank(this.apiKey, "API key is required, please pass it in the properties (key=" + API_KEY_PROP + ")");
        Validate.notBlank(this.authToken, "Authentification token is required, please pass it in the properties (key=" + AUTH_TOKEN_PROP + ")");
    }

    public void beforeFire(HttpRequest.Builder builder, RequestContext context) throws Exception {

        if (builder.getMeth().hasEntity()) {
            builder.addParam("api_key", apiKey, HttpRequest.DEST_FORM, false);
            builder.addParam("auth_token", authToken, HttpRequest.DEST_FORM, false);
        } else {
            builder.addParam("api_key", apiKey, HttpRequest.DEST_QUERY, false);
            builder.addParam("auth_token", authToken, HttpRequest.DEST_QUERY, false);
        }

        List<HttpParam> params = new ArrayList<HttpParam>();
        params.addAll(builder.getQueryParams());
        params.addAll(builder.getFormParams());

        StringBuilder sb = new StringBuilder(appSecret);
        
        for (HttpParam param : params) {
            for(Pair pair : process(param, builder.getCharset())){
                sb.append(pair.getName()).append(param.getValue());
            }
        }

        MessageDigest digest = MessageDigest.getInstance("MD5");
        digest.update(sb.toString().getBytes());
        String hash = Hex.encodeAsString(digest.digest());

        if (builder.getMeth().hasEntity()) {
            builder.addParam("api_sig", hash, HttpRequest.DEST_FORM, false);
        } else {
            builder.addParam("api_sig", hash, HttpRequest.DEST_QUERY, false);
        }

    }

}
