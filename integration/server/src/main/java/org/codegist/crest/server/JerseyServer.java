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

package org.codegist.crest.server;

import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.jersey.api.core.DefaultResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.oauth.server.NonceManager;
import com.sun.jersey.oauth.server.OAuthException;
import com.sun.jersey.oauth.server.OAuthServerRequest;
import com.sun.jersey.oauth.signature.OAuthParameters;
import com.sun.jersey.oauth.signature.OAuthSecrets;
import com.sun.jersey.oauth.signature.OAuthSignature;
import com.sun.jersey.oauth.signature.OAuthSignatureException;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.net.httpserver.HttpServer;
import org.codegist.common.codec.Base64;
import org.codegist.common.log.Logger;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.codegist.common.collect.Collections.asSet;

/**
 * @author laurent.gilles@codegist.org
 */
public class JerseyServer implements Server {

    private static final Logger LOG = Logger.getLogger(JerseyServer.class);
    private HttpServer server;
    private Object[] services;

    public void start(String address, Object... services) throws IOException {
        if (server != null) {
            throw new IllegalStateException("Server is already running");
        }

        LOG.info("Starting server [address=%s, test %s]", address, Arrays.toString(services));
        this.services = services.clone();

        ResourceConfig rc = new DefaultResourceConfig(){
            @Override
            public List getContainerRequestFilters() {
                return super.getContainerRequestFilters();
            }

            @Override
            public Set<Object> getSingletons() {
                return asSet(JerseyServer.this.services);
            }
        };

        rc.getContainerRequestFilters().add(new OAuthAuthenticationFilter());
        rc.getContainerRequestFilters().add(new BasicAuthenticationFilter());
        this.server = HttpServerFactory.create(address, rc);

        this.server.start();
    }

    public void stop() {
        LOG.info("Stoping server [address=%s, test %s]", server.getAddress(), this.services);
        server.stop(0);
        server = null;
    }

    public static class BasicAuthenticationFilter implements ContainerRequestFilter {

        public static final String USERNAME = "My UserName";
        public static final String PASSWORD = "My password";

        public ContainerRequest filter(ContainerRequest containerRequest) {
            String authHeader = containerRequest.getHeaderValue(OAuthParameters.AUTHORIZATION_HEADER);
            if (authHeader == null || !authHeader.toUpperCase().startsWith("BASIC")) {
                return containerRequest;
            }
            String[] split = new String(Base64.decode(authHeader.split(" ")[1])).split(":");
            if(!(USERNAME.equals(split[0]) && PASSWORD.equals(split[1]))) {
                throw newUnauthorizedException();
            }
            return  containerRequest;
        }

        private AuthException newUnauthorizedException() throws OAuthException {
            return new AuthException(Response.Status.UNAUTHORIZED, "Basic realm=\"default\"");
        }
    }
    public static class OAuthAuthenticationFilter implements ContainerRequestFilter {

        private static final long MAX_AGE = 60 * 60 * 1000; // one houre
        private final NonceManager nonces = new NonceManager(MAX_AGE, 100);

        public static final String OAUTH_CONSUMER_KEY = "ConsumerKey";
        public static final String OAUTH_CONSUMER_SECRET = "ConsumerSecret";
        public static final String OAUTH_ACCESS_TOKEN = "AccessToken";
        public static final String OAUTH_ACCESS_SECRET = "AccessTokenSecret";

        public ContainerRequest filter(ContainerRequest containerRequest) {
            String authHeader = containerRequest.getHeaderValue(OAuthParameters.AUTHORIZATION_HEADER);
            if (authHeader == null || !authHeader.toUpperCase().startsWith(OAuthParameters.SCHEME.toUpperCase())) {
                return containerRequest;
            }
            // Read the OAuth parameters from the io
            OAuthServerRequest request = new OAuthServerRequest(containerRequest);
            OAuthParameters params = new OAuthParameters();
            params.readRequest(request);


            // Set the secret(s), against which we will verify the io
            OAuthSecrets secrets = new OAuthSecrets();
            secrets.setConsumerSecret(OAUTH_CONSUMER_SECRET);
            secrets.setTokenSecret(OAUTH_ACCESS_SECRET);
            // ... secret setting code ...

            // Check that the timestamp has not expired
            String timestampStr = params.getTimestamp();
            String nonceStr = params.getNonce();
            // ... timestamp checking code ...

            if(!(OAUTH_CONSUMER_KEY.equals(params.getConsumerKey()) && OAUTH_ACCESS_TOKEN.equals(params.getToken()))){
                throw newUnauthorizedException();
            }

            if (!nonces.verify(params.getToken(), timestampStr, nonceStr)) {
                throw newUnauthorizedException();
            }
            // Verify the signature
            try {
                if(!OAuthSignature.verify(request, params, secrets)) {
                    throw newUnauthorizedException();
                }
            } catch (OAuthSignatureException e) {
                throw newBadRequestException();
            }

            // Return the io
            return containerRequest;
        }
        private static AuthException newBadRequestException() throws OAuthException {
            return new AuthException(Response.Status.BAD_REQUEST, null);
        }

        private AuthException newUnauthorizedException() throws OAuthException {
            return new AuthException(Response.Status.UNAUTHORIZED, "OAuth realm=\"default\"");
        }

    }

    public static class AuthException extends RuntimeException {
        public final Response.Status status;
        public final String wwwAuthHeader;

        public AuthException(Response.Status status, String wwwAuthHeader) {
            this.status = status;
            this.wwwAuthHeader = wwwAuthHeader;
        }

        /** Maps this exception to a response object.
         *
         * @return Response this exception maps to.
         */
        public Response toResponse() {
            Response.ResponseBuilder rb = Response.status(status);
            if (wwwAuthHeader != null) {
                rb.header("WWW-Authenticate", wwwAuthHeader);
            }
            return rb.build();
        }
    }



}
