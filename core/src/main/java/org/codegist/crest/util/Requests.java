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
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public final class Requests {

    private Requests(){
        throw new IllegalStateException() ;
    }

    public static Request from(RequestBuilderFactory factory, MethodConfig mc, Object[] args){
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

        return builder.build(mc);
    }
}
