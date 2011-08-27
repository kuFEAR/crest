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

package org.codegist.crest.security.oauth.v1;

import org.codegist.crest.CRestBuilder;
import org.codegist.crest.config.MethodType;
import org.codegist.crest.io.http.HttpChannelFactory;
import org.codegist.crest.io.http.platform.HttpURLConnectionHttpChannelFactory;
import org.codegist.crest.security.oauth.OAuthToken;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public final class OAuthApiV1Builder {

    private final CRestBuilder crestBuilder;
    private final OAuthToken consumerToken;
    private final String oauthEndPoint;

    private VariantProvider variantProvider = DefaultVariantProvider.INSTANCE;
    private HttpChannelFactory channelFactory = new HttpURLConnectionHttpChannelFactory();
    private MethodType methodType = MethodType.POST;
    private Class<? extends OAuthInterface> oauthInterfaceCls = FormOAuthInterface.class;
    private String requestTokenPath = "";
    private String accessTokenPath = "";
    private String refreshAccessTokenPath = "";

    public OAuthApiV1 build(){
        OAuthInterface oauthInterface = crestBuilder.placeholder("oauth.end-point", oauthEndPoint)
                                                    .placeholder("oauth.access-token.path", accessTokenPath)
                                                    .placeholder("oauth.request-token.path", requestTokenPath)
                                                    .placeholder("oauth.refresh-access-token.path", refreshAccessTokenPath)
                                                    .setHttpChannelFactory(channelFactory)
                                                    .build(oauthInterfaceCls);
        return new OAuthApiV1(
                methodType,
                oauthEndPoint,
                requestTokenPath,
                accessTokenPath,
                refreshAccessTokenPath,
                oauthInterface,
                consumerToken,
                variantProvider);
    }

    public OAuthApiV1Builder(OAuthToken consumerToken, String oauthEndPoint){
        this(consumerToken, oauthEndPoint, new CRestBuilder());
    }

    OAuthApiV1Builder(OAuthToken consumerToken, String oauthEndPoint, CRestBuilder crestBuilder){
        this.consumerToken = consumerToken;
        this.oauthEndPoint = oauthEndPoint;
        this.crestBuilder = crestBuilder;
    }

    public OAuthApiV1Builder getRequestTokenFrom(String path){
        this.requestTokenPath = path;
        return this;
    }

    public OAuthApiV1Builder getAccessTokenFrom(String path){
        this.accessTokenPath = path;
        return this;
    }

    public OAuthApiV1Builder refreshAccessTokenFrom(String path){
        this.refreshAccessTokenPath = path;
        return this;
    }

    public OAuthApiV1Builder useGet(){
        this.methodType = MethodType.GET;
        this.oauthInterfaceCls = QueryOAuthInterface.class;
        return this;
    }

    public OAuthApiV1Builder using(HttpChannelFactory channelFactory){
        this.channelFactory = channelFactory;
        return this;
    }

    public OAuthApiV1Builder using(VariantProvider variantProvider){
        this.variantProvider = variantProvider;
        return this;
    }

}
