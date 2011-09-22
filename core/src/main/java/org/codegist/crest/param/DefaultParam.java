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
 *  ==================================================================
 *
 *  More information at http://www.codegist.org.
 */

package org.codegist.crest.param;

import org.codegist.common.lang.ToStringBuilder;
import org.codegist.crest.config.ParamConfig;
import org.codegist.crest.param.Param;

import java.util.Collection;

/**
 * Default {@link org.codegist.crest.param.Param} implementation
 */
public class DefaultParam implements Param {

    private final Collection<Object> value;
    private final ParamConfig config;

    /**
     *
     * @param config the param's config
     * @param value the param's values
     */
    public DefaultParam(ParamConfig config, Collection<Object> value) {
        this.config = config;
        this.value = value;
    }

    /**
     * @inheritDoc
     */
    public Collection<Object> getValue() {
        return value;
    }

    /**
     * @inheritDoc
     */
    public ParamConfig getParamConfig() {
        return config;
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append("name", config.getName())
                .append("value", value)
                .toString();
    }
}