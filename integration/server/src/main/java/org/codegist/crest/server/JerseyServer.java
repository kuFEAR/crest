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
import com.sun.net.httpserver.HttpServer;
import org.codegist.common.log.Logger;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;

/**
 * @author laurent.gilles@codegist.org
 */
public class JerseyServer implements Server {

    private static final Logger LOG = Logger.getLogger(CxfServer.class);
    private HttpServer server;
    private Object[] services;

    public void start(String address, Object... services) throws IOException {
        if (server != null) {
            throw new IllegalStateException("Server is already running");
        }

        LOG.info("Starting server [address=%s, test %s]", address, Arrays.toString(services));
        this.services = services.clone();
        this.server = HttpServerFactory.create(address, new DefaultResourceConfig(){
            @Override
            public Set<Object> getSingletons() {
                return new HashSet<Object>(asList(JerseyServer.this.services));
            }
        });
        this.server.start();
    }

    public void stop() {
        LOG.info("Stoping server [address=%s, test %s]", server.getAddress(), this.services);
        server.stop(0);
        server = null;
    }
}
