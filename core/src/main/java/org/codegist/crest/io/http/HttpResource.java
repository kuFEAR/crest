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

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
interface HttpResource extends Closeable {

    InputStream getEntity() throws IOException;

    String getContentType() throws IOException;

    Charset getCharset() throws IOException;

    String getHeaderField(String field) throws IOException;

    Map<String, List<String>> getHeaderFields() throws IOException;

    String getContentEncoding() throws IOException;

    String getStatusMessage() throws IOException;

    int getStatusCode() throws IOException;
}
