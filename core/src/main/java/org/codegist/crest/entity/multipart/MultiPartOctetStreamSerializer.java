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

package org.codegist.crest.entity.multipart;

import org.codegist.crest.config.ParamConfig;
import org.codegist.crest.serializer.Serializer;
import org.codegist.crest.util.MultiParts;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import static org.codegist.common.lang.Strings.defaultIfBlank;
import static org.codegist.common.lang.Strings.isNotBlank;

abstract class MultiPartOctetStreamSerializer<T> implements Serializer<MultiPart<T>> {

    private static final String LRLN = "\r\n";
    
    abstract String getFileName(MultiPart<T> multipart);

    String getContentType(MultiPart<T> multipart) {
        return defaultIfBlank(MultiParts.getContentType(multipart.getParamConfig()), "application/octet-stream");
    }

    public void serialize(MultiPart<T> multipart, Charset charset, OutputStream outputStream) throws Exception {
        ParamConfig pc = multipart.getParamConfig();
        String fileName = getFileName(multipart);
        String contentType = getContentType(multipart);
        DataOutputStream out = new DataOutputStream(outputStream);
        out.writeBytes("--" + multipart.getBoundary() + LRLN + "Content-Disposition: form-data; name=\"" + pc.getName() + "\"");
        out.writeBytes(isNotBlank(fileName) ? "; filename=\"" + fileName + "\"" + LRLN : LRLN);
        out.writeBytes("Content-Type: " + contentType + LRLN + LRLN);
        pc.getSerializer().serialize(multipart.getValue(), charset, out);
        out.writeBytes(LRLN);
    }
}