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

package org.codegist.crest.flickr.serializer;

import org.codegist.crest.serializer.Serializer;

import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Date;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class FlickrDateSerializer implements Serializer<Date> {
    public void serialize(Date value, Charset charset, OutputStream out) throws Exception {
       String seconds = Long.toString(value.getTime() / 1000);
        out.write(seconds.getBytes());
    }
}
