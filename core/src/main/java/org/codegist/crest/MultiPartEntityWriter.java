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

package org.codegist.crest;

import org.codegist.common.io.IOs;
import org.codegist.common.lang.Randoms;
import org.codegist.common.log.Logger;

import java.io.*;

import static org.codegist.common.lang.Strings.defaultIfBlank;
import static org.codegist.common.lang.Strings.isNotBlank;

/**
 * @author laurent.gilles@codegist.org
 */
public class MultiPartEntityWriter implements EntityWriter {

    private final static String MULTIPART = "multipart/form-data; boundary=";
    private final static String BOUNDARY = Randoms.randomAlphaNumeric(24);
    private final static String FULL_BOUNDARY = "--" + BOUNDARY;

    public HttpParamMap getHeaders(HttpRequest request) {
        HttpParamMap headers = new HttpParamMap();
        headers.set(new HttpParam("Content-Type", MULTIPART + BOUNDARY, true));
        return headers;
    }

    public void writeTo(HttpRequest request, OutputStream outputStream) throws IOException {

        if (!request.getFormParamMap().isEmpty()) {
            DataOutputStream out = new DataOutputStream(outputStream);

            for (HttpParam param: request.getFormParamMap().allValues()) {
                InputStream upload = null;
                String fileName = null;
                if (param.getValue().getRaw() instanceof InputStream) {
                    upload = (InputStream) param.getValue().getRaw();
                } else if (param.getValue().getRaw() instanceof File) {
                    upload = new FileInputStream((File) param.getValue().getRaw());
                    fileName = ((File) param.getValue().getRaw()).getName();
                }else if(param.getValue().getRaw() == null) {
                    continue;
                }

                String customContentType = MultiParts.getContentType(param.getValue());
                fileName = defaultIfBlank(MultiParts.getFileName(param.getValue()), fileName);

                out.writeBytes(FULL_BOUNDARY + "\r\n");
                out.writeBytes("Content-Disposition: form-data; name=\"" + param.getName() + "\"");

                if(isNotBlank(fileName)) {
                    out.writeBytes("; filename=\"" + fileName + "\"\r\n");
                }else{
                    out.writeBytes("\r\n");
                }

                if(upload != null) {
                    String contentType = defaultIfBlank(customContentType, "application/octet-stream");
                    out.writeBytes("Content-Type: " + contentType + "\r\n\r\n");
                    BufferedInputStream in = null;
                    try {
                        in = (BufferedInputStream) (upload instanceof BufferedInputStream ? upload : new BufferedInputStream(upload));
                        IOs.copy(in, out);
                    } finally {
                        IOs.close(in);
                    }
                }else{
                    String contentType = defaultIfBlank(customContentType, "text/plain");
                    out.writeBytes("Content-Type: " + contentType + "; charset=" + request.getEncoding() + "\r\n\r\n");
                    param.getValue().writeTo(out, request.getEncodingAsCharset());
                }

                out.writeBytes("\r\n");
            }
            out.writeBytes(FULL_BOUNDARY + "--\r\n");
            out.writeBytes("\r\n");
        }
    }

    
}
