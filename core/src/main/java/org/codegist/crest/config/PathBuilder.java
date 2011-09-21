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

package org.codegist.crest.config;

/**
 * Path builder, returned by a {@link org.codegist.crest.config.PathTemplate}, holding a URI with placeholders to be merged
 * @author laurent.gilles@codegist.org
 */
public interface PathBuilder {

    /**
     * Merges a key/value pair on the given path, encoding the value if necessary
     * @param templateName placeholder name
     * @param templateValue placeholder value
     * @param encoded if true, the value will be used as given, otherwise will be url-encoded
     * @return current builder
     * @throws Exception Any exception thrown during the merge process
     */
    PathBuilder merge(String templateName, String templateValue, boolean encoded) throws Exception;

    /**
     * Builds the path
     */
    String build();

}
