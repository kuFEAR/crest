/*
 * Copyright 2010 CodeGist.org
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 * ===================================================================
 *
 * More information at http://www.codegist.org.
 */

package org.codegist.crest.serializer;

import org.codegist.crest.CRestConfig;

import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @inheritDoc
 * @author laurent.gilles@codegist.org
 */
public class DateSerializer extends StringSerializer<Date> {

    private final DateFormat formatter;

    public DateSerializer(CRestConfig crestConfig) {
        this.formatter = new SimpleDateFormat(crestConfig.getDateFormat());
    }

    /**
     * @inheritDoc
     */
    public String serialize(Date value, Charset charset) {
        synchronized (formatter) {
            return formatter.format(value);
        }
    }

}
