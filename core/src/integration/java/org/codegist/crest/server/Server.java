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

import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.codegist.common.log.Logger;

import java.util.Arrays;

public class Server {

    private static final Logger LOG = Logger.getLogger(Server.class);
    private org.apache.cxf.endpoint.Server server;
    private JAXRSServerFactoryBean bean;

    public static Server create(String address, Object... services) {
        final Server server = new Server();
        server.start(address, services);
        return server;
    }

    public void start(String address, Object... services){
        if(server != null) {
            throw new IllegalStateException("Server is already running");
        }
        LOG.info("Starting server [address=%s, test %s]", address, Arrays.toString(services));
        bean = new JAXRSServerFactoryBean();
        bean.setServiceBeans(Arrays.asList(services));
        bean.setAddress(address);
        server = bean.create();
    }

    public void stop(){
        LOG.info("Stoping server [address=%s, test %s]", bean.getAddress(), bean.getResourceClasses());
        server.stop();
        server.destroy();
        server = null;
    }
}