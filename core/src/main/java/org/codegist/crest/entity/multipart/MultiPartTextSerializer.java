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
import org.codegist.crest.param.EncodedPair;
import org.codegist.crest.param.Param;
import org.codegist.crest.serializer.Serializer;
import org.codegist.crest.util.MultiParts;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import static org.codegist.common.lang.Strings.defaultIfBlank;
import static org.codegist.common.lang.Strings.isNotBlank;

/**
 * @author laurent.gilles@codegist.org
 */
final class MultiPartTextSerializer implements Serializer<MultiPart<Param>> {

    private static final String LRLN = "\r\n";

    static final MultiPartTextSerializer INSTANCE = new MultiPartTextSerializer();

    public void serialize(MultiPart<Param> multipart, Charset charset, OutputStream outputStream) throws Exception {
        ParamConfig pc = multipart.getParamConfig();
        String partContentType = MultiParts.getContentType(pc);
        String partFileName = MultiParts.getFileName(pc);
        DataOutputStream out = new DataOutputStream(outputStream);
        String partialContentDiposition = "--" + multipart.getBoundary() + LRLN + "Content-Disposition: form-data; name=\"" + pc.getName() + "\"";
        String contentDisposition = partialContentDiposition + (isNotBlank(partFileName) ? "; filename=\"" + partFileName + "\"" + LRLN : LRLN);
        String contentType = "Content-Type: " + defaultIfBlank(partContentType, "text/plain") + "; charset=" + charset.displayName() + LRLN + LRLN;
        for(EncodedPair pair : pc.getParamProcessor().process(multipart.getValue(), charset, false)){
            out.writeBytes(contentDisposition);
            out.writeBytes(contentType);
            out.write(pair.getValue().getBytes(charset));
            out.writeBytes(LRLN);
        }
    }
}
