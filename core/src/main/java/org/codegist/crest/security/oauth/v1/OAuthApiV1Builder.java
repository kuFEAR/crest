/*
 * Copyright 2011 CodeGist.org
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
import org.codegist.crest.io.http.HttpURLConnectionHttpChannelFactory;
import org.codegist.crest.security.oauth.OAuthToken;

/**
 * {@link org.codegist.crest.security.oauth.v1.OAuthApiV1} builder.
 * @author laurent.gilles@codegist.org
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
                                                    .build()
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

    /**
     * @param consumerToken consumer token to use
     * @param oauthEndPoint base oauth endpoint
     */
    public OAuthApiV1Builder(OAuthToken consumerToken, String oauthEndPoint){
        this(consumerToken, oauthEndPoint, new CRestBuilder());
    }

    OAuthApiV1Builder(OAuthToken consumerToken, String oauthEndPoint, CRestBuilder crestBuilder){
        this.consumerToken = consumerToken;
        this.oauthEndPoint = oauthEndPoint;
        this.crestBuilder = crestBuilder;
    }

    /**
     * Indicates the path context to use for request token
     * @param path request token path context
     * @return current builder
     */
    public OAuthApiV1Builder getRequestTokenFrom(String path){
        this.requestTokenPath = path;
        return this;
    }

    /**
     * Indicates the path context to use for access token exchange url
     * @param path access token exchange path context
     * @return current builder
     */
    public OAuthApiV1Builder getAccessTokenFrom(String path){
        this.accessTokenPath = path;
        return this;
    }

    /**
     * Indicates the path context to use for expired token refresh
     * @param path expired token refresh path context
     * @return current builder
     */
    public OAuthApiV1Builder refreshAccessTokenFrom(String path){
        this.refreshAccessTokenPath = path;
        return this;
    }

    /**
     * Resulting OAuthApi will put OAuth parameter in the query-string rather than as header by default.
     * @return current builder
     */
    public OAuthApiV1Builder useGet(){
        this.methodType = MethodType.GET;
        this.oauthInterfaceCls = QueryOAuthInterface.class;
        return this;
    }

    /**
     * Overrides the default http channel factory that will be used during the oauth process
     * @param channelFactory custom channel factory
     * @return current builder
     * @see org.codegist.crest.io.http.HttpURLConnectionHttpChannelFactory
     */
    public OAuthApiV1Builder using(HttpChannelFactory channelFactory){
        this.channelFactory = channelFactory;
        return this;
    }

    /**
     * Overrides the default variant provider that will be used during the oauth process
     * @param variantProvider custom variant provider
     * @return current builder
     */
    public OAuthApiV1Builder using(VariantProvider variantProvider){
        this.variantProvider = variantProvider;
        return this;
    }

}
