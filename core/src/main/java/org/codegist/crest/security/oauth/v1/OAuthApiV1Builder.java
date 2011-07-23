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
    private VariantProvider variantProvider = DefaultVariantProvider.INSTANCE;
    private HttpChannelFactory channelFactory = new HttpURLConnectionHttpChannelFactory();
    private MethodType methodType = MethodType.POST;
    private Class<? extends OAuthInterface> oauthInterfaceCls = PostOAuthInterface.class;
    private String requestTokenUrl = "";
    private String accessTokenUrl = "";
    private String refreshAccessTokenUrl = "";



    public OAuthApiV1 build(){
        OAuthInterface oauthInterface = crestBuilder.placeholder("oauth.access-token-path", accessTokenUrl)
                                                    .placeholder("oauth.request-token-path", requestTokenUrl)
                                                    .placeholder("oauth.refresh-access-token-path", refreshAccessTokenUrl)
                                                    .setHttpChannelFactory(channelFactory)
                                                    .build(oauthInterfaceCls);
        return new OAuthApiV1(
                methodType,
                requestTokenUrl,
                accessTokenUrl,
                refreshAccessTokenUrl,
                oauthInterface,
                consumerToken,
                variantProvider);
    }

    public OAuthApiV1Builder(OAuthToken consumerToken){
        this(consumerToken, new CRestBuilder());
    }

    OAuthApiV1Builder(OAuthToken consumerToken, CRestBuilder crestBuilder){
        this.consumerToken = consumerToken;
        this.crestBuilder = crestBuilder;
    }

    public OAuthApiV1Builder getRequestTokenFrom(String url){
        this.requestTokenUrl = url;
        return this;
    }

    public OAuthApiV1Builder getAccessTokenFrom(String url){
        this.accessTokenUrl = url;
        return this;
    }

    public OAuthApiV1Builder refreshAccessTokenFrom(String url){
        this.refreshAccessTokenUrl = url;
        return this;
    }

    public OAuthApiV1Builder using(MethodType methodType){
        this.methodType = methodType;
        this.oauthInterfaceCls = methodType.equals(MethodType.POST) ? PostOAuthInterface.class : GetOAuthInterface.class;
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
