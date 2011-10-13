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

package org.codegist.crest.io.http;

import org.codegist.crest.config.MethodConfig;
import org.codegist.crest.config.ParamConfig;
import org.codegist.crest.io.RequestBuilder;
import org.codegist.crest.io.RequestBuilderFactory;
import org.codegist.crest.param.DefaultParam;
import org.codegist.crest.param.Param;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class HttpRequestBuilderFactory implements RequestBuilderFactory {

    /**
     * @inheritDoc
     */
    public RequestBuilder create() {
        return new Builder();
    }

    private static class Builder implements RequestBuilder {

        private final List<Param> headerParams = new ArrayList<Param>();
        private final List<Param> matrixParams = new ArrayList<Param>();
        private final List<Param> queryParams = new ArrayList<Param>();
        private final List<Param> pathParams = new ArrayList<Param>();
        private final List<Param> cookieParams = new ArrayList<Param>();
        private final List<Param> formParams = new ArrayList<Param>();


        public HttpRequest build(MethodConfig methodConfig) {
            return new HttpRequest(
                    methodConfig,
                    headerParams,
                    matrixParams,
                    queryParams,
                    pathParams,
                    cookieParams,
                    formParams
            );
        }

        public Builder addParams(ParamConfig... paramConfigs) {
            for(ParamConfig param : paramConfigs){
                addParam(param);
            }
            return this;
        }

        public Builder addParam(ParamConfig paramConfig) {
            return addParam(paramConfig, paramConfig.getDefaultValue());
        }

        public Builder addParam(ParamConfig paramConfig, Object value) {
            return addParam(paramConfig, Collections.singleton(value));
        }

        public Builder addParam(ParamConfig paramConfig, Collection<Object> values) {
            DefaultParam param = new DefaultParam(paramConfig, values);
            switch(paramConfig.getType()){
                case COOKIE:
                    cookieParams.add(param);
                    break;
                case FORM:
                    formParams.add(param);
                    break;
                case HEADER:
                    headerParams.add(param);
                    break;
                case MATRIX:
                    matrixParams.add(param);
                    break;
                case PATH:
                    pathParams.add(param);
                    break;
                case QUERY:
                    queryParams.add(param);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported param type ! (type=" + paramConfig.getType() + ")");
            }
            return this;
        }
    }
}
