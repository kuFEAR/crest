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

package org.codegist.crest.config;

/**
 * HTTP method types
 * @author laurent.gilles@codegist.org
 */
public enum MethodType {
    /**
     * GET HTTP Method
     */
    GET(false),
    /**
     * POST HTTP Method
     */
    POST(true),
    /**
     * PUT HTTP Method
     */
    PUT(true),
    /**
     * DELETE HTTP Method
     */
    DELETE(false),
    /**
     * OPTIONS HTTP Method
     */
    OPTIONS(false),
    /**
     * HEAD HTTP Method
     */
    HEAD(false);

    private final boolean hasEntity;

    public static MethodType getDefault(){
        return GET;
    }

    MethodType(boolean hasEntity) {
        this.hasEntity = hasEntity;
    }

    /**
     * whether the HTTP method type has an HTTP entity
     */
    public boolean hasEntity() {
        return hasEntity;
    }
}
