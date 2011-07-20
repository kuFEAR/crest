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

import org.codegist.crest.util.model.Data;
import org.codegist.crest.serializer.StringSerializer;

import java.nio.charset.Charset;

import static java.lang.String.format;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class DataSerializer extends StringSerializer<Data> {

    public String serialize(Data value, Charset charset) {
        return format("Data(val1=%s,val2=%s)", value.getVal1(), value.getVal2());
    }
}

