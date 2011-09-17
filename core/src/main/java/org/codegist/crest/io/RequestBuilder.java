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

package org.codegist.crest.io;

import org.codegist.crest.config.MethodConfig;
import org.codegist.crest.config.ParamConfig;

import java.util.Collection;

/**
 * {@link org.codegist.crest.io.Request} builder
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public interface RequestBuilder {

    /**
     * Returns an instance of request for the given method config
     * @param methodConfig request's method config
     * @return request instance
     */
    Request build(MethodConfig methodConfig);

    /**
     * <p>Add the given param configs.</p>
     * <p>Resulting request param values are given by the param config's default values</p>
     * @param paramConfigs param configs to add
     * @return current builder
     * @see org.codegist.crest.config.ParamConfig#getDefaultValue()
     */
    RequestBuilder addParams(ParamConfig... paramConfigs);

    /**
     * <p>Add the given param config.</p>
     * <p>Param config's default value is used as value for the resulting request's parameter.</p>
     * @param paramConfig param configs to add
     * @return current builder
     * @see org.codegist.crest.config.ParamConfig#getDefaultValue()
     */
    RequestBuilder addParam(ParamConfig paramConfig);

    /**
     * <p>Add the given param config.</p>
     * <p>Given value is is used as value for the resulting request's parameter.</p>
     * @param paramConfig param configs to add
     * @param value value to use for the given param config
     * @return current builder
     */
    RequestBuilder addParam(ParamConfig paramConfig, Object value);

    /**
     * <p>Add the given param config.</p>
     * <p>Given values is is used as value for the resulting request's parameter.</p>
     * @param paramConfig param configs to add
     * @param values values to use for the given param config
     * @return current builder
     */
    RequestBuilder addParam(ParamConfig paramConfig, Collection<Object> values);

}
