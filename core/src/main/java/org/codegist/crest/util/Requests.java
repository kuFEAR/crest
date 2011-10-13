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

package org.codegist.crest.util;

import org.codegist.common.lang.Objects;
import org.codegist.crest.config.MethodConfig;
import org.codegist.crest.config.ParamConfig;
import org.codegist.crest.io.Request;
import org.codegist.crest.io.RequestBuilder;
import org.codegist.crest.io.RequestBuilderFactory;

import java.util.Collection;

import static org.codegist.common.collect.Collections.containsOnlyNulls;

/**
 * <p>Set of utility functions for dealing with {@link org.codegist.crest.io.Request} types.</p>
 * @see org.codegist.crest.io.Request
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public final class Requests {

    private Requests(){
        throw new IllegalStateException() ;
    }

    /**
     * <p>Creates a {@link org.codegist.crest.io.Request} from the given method config and user arguments.</p>
     * <p>Argument array must match and reflects the {@link org.codegist.crest.config.MethodConfig#getParamCount()} and {@link org.codegist.crest.config.MethodConfig#getParamConfig(int)}.</p>
     * <p>This method also calls the {@link org.codegist.crest.config.MethodConfig#getRequestInterceptor()} before building the request.</p>
     * @param factory factory to use to build a {@link org.codegist.crest.io.RequestBuilder}
     * @param mc current method config the given arguments refers to
     * @param args arguments passed by the user to the rest interface
     * @return a request ready to be executed
     * @throws Exception Any exception thrown during the request building
     * @see org.codegist.crest.io.Request
     * @see org.codegist.crest.interceptor.RequestInterceptor
     * @see org.codegist.crest.config.MethodConfig
     */
    public static Request from(RequestBuilderFactory factory, MethodConfig mc, Object[] args) throws Exception {
        RequestBuilder builder = factory.create().addParams(mc.getExtraParams());
        for (int i = 0; i < mc.getParamCount(); i++) {
            Collection<Object> values = Objects.asCollection(args[i]);
            ParamConfig pc = mc.getParamConfig(i);
            if(containsOnlyNulls(values)) {
                if(pc.getDefaultValue() != null) {
                    builder.addParam(pc);
                }
            }else{
                builder.addParam(pc, values);
            }
        }
        mc.getRequestInterceptor().beforeFire(builder, mc, args);
        return builder.build(mc);
    }
}
