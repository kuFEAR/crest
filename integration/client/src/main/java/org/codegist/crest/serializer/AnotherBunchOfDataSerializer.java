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

package org.codegist.crest.serializer;

import org.codegist.crest.model.BunchOfData;
import org.codegist.crest.model.Data;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;

import static java.lang.String.format;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class AnotherBunchOfDataSerializer extends StringSerializer<BunchOfData<Data>> {

    public String serialize(BunchOfData<Data> value, Charset charset) throws SerializerException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return format("AnotherBuchOfData(val1=%s, val2=%s, val3=Data(val1=%s, val2=%s))", sdf.format(value.getVal1()), value.getVal2(), value.getVal3().getVal1(), value.getVal3().getVal2());
    }

}
