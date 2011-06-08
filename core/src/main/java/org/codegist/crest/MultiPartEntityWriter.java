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
import org.codegist.crest.http.HttpParam;
import org.codegist.crest.http.HttpRequest;

import java.io.*;
import java.util.List;

import static java.util.Arrays.asList;
import static org.codegist.common.lang.Strings.defaultIfBlank;
import static org.codegist.common.lang.Strings.isNotBlank;

/**
 * @author laurent.gilles@codegist.org
 */
public class MultiPartEntityWriter implements EntityWriter {

    private final static String MULTIPART = "multipart/form-data; boundary=";
    private final static String BOUNDARY = Randoms.randomAlphaNumeric(24);
    private final static String FULL_BOUNDARY = "--" + BOUNDARY;

    public List<HttpParam> getHeaders(HttpRequest request) {
        return asList(new HttpParam("Content-Type", MULTIPART + BOUNDARY, HttpRequest.DEST_HEADER, true));
    }

    public void writeTo(HttpRequest request, OutputStream outputStream) throws IOException {

        if (!request.getFormParam().isEmpty()) {
            DataOutputStream out = new DataOutputStream(outputStream);

            for (HttpParam param: request.getFormParam()) {
                for(Object value : param.getValue()){
                    InputStream upload = null;
                    String fileName = null;
                    if (value instanceof InputStream) {
                        upload = (InputStream) value;
                    } else if (value instanceof File) {
                        upload = new FileInputStream((File) value);
                        fileName = ((File) value).getName();
                    }else if(value == null) {
                        continue;
                    }

                    String customContentType = MultiParts.getContentType(param);
                    fileName = defaultIfBlank(MultiParts.getFileName(param), fileName);

                    out.writeBytes(FULL_BOUNDARY + "\r\n");
                    out.writeBytes("Content-Disposition: form-data; name=\"" + param.getConfig().getName() + "\"");

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
                        param.getConfig().getSerializer().serialize(value,request.getCharset(), out);
                    }

                    out.writeBytes("\r\n");
                }
            }
            out.writeBytes(FULL_BOUNDARY + "--\r\n");
            out.writeBytes("\r\n");
        }
    }

    
}
