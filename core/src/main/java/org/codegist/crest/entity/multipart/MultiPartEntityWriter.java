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

import org.codegist.common.lang.Randoms;
import org.codegist.crest.config.ParamConfig;
import org.codegist.crest.entity.EntityWriter;
import org.codegist.crest.io.Request;
import org.codegist.crest.param.Param;
import org.codegist.crest.serializer.Serializer;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;

import static org.codegist.crest.config.ParamType.FORM;

/**
 * @author laurent.gilles@codegist.org
 */
public class MultiPartEntityWriter implements EntityWriter {

    private static final String MULTIPART = "multipart/form-data; boundary=";
    private static final String BOUNDARY = Randoms.randomAlphaNumeric(24);
    private static final String FULL_BOUNDARY = "--" + BOUNDARY;
    private static final String LRLN = "\r\n";

    private final Serializer<MultiPart<Param>> binarySerializer;
    private final Serializer<MultiPart<Param>> textSerializer;

    public MultiPartEntityWriter() {
        this(MultiPartBinarySerializer.INSTANCE, MultiPartTextSerializer.INSTANCE);
    }

    MultiPartEntityWriter(Serializer<MultiPart<Param>> binarySerializer, Serializer<MultiPart<Param>> textSerializer) {
        this.binarySerializer = binarySerializer;
        this.textSerializer = textSerializer;
    }

    public String getContentType(Request request) {
        return MULTIPART + BOUNDARY;
    }

    public int getContentLength(Request request) {
        return -1;
    }

    public void writeTo(Request request, OutputStream outputStream) throws Exception {
        DataOutputStream out = new DataOutputStream(outputStream);
        List<Param> formParams = request.getParams(FORM);
        Charset charset = request.getMethodConfig().getCharset();

        for (Param param: formParams) {
            ParamConfig pc = param.getParamConfig();
            Class<?> paramClass = pc.getValueClass();
            MultiPart<Param> multiPart = new MultiPart<Param>(pc, param, BOUNDARY);

            if(MultiPartBinarySerializer.isBinary(paramClass)) {
                binarySerializer.serialize(multiPart, charset, out);
            }else{
                textSerializer.serialize(multiPart, charset, out);
            }
        }
        out.writeBytes(FULL_BOUNDARY + "--" + LRLN);
        out.writeBytes(LRLN);
        out.flush();
    }

}
