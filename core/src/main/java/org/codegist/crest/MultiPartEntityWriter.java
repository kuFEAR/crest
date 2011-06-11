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
import org.codegist.crest.http.Pair;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;

import static java.util.Arrays.asList;
import static org.codegist.common.lang.Strings.defaultIfBlank;
import static org.codegist.common.lang.Strings.isNotBlank;
import static org.codegist.crest.http.HttpParamProcessor.process;

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

        DataOutputStream out = new DataOutputStream(outputStream);
        if (!request.getFormParam().isEmpty()) {
            Charset charset = request.getCharset();

            for (HttpParam param: request.getFormParam()) {
                Class<?> paramClass = param.getConfig().getValueClass();
                String partName = param.getConfig().getName();
                String partContentType = MultiParts.getContentType(param);
                String partFileName = MultiParts.getFileName(param);

                String partialPontentDiposition = FULL_BOUNDARY + "\r\nContent-Disposition: form-data; name=\"" + partName + "\"";

                if(paramClass.isAssignableFrom(File.class) || paramClass.isAssignableFrom(InputStream.class)) {
                    String contentType = defaultIfBlank(partContentType, "application/octet-stream");
                    for(Object value : param.getValue()){
                        if(value == null) continue;

                        InputStream upload = null;
                        String fileName = partFileName;

                        if (value instanceof InputStream) {
                            upload = (InputStream) value;
                        } else if (value instanceof File) {
                            upload = new FileInputStream((File) value);
                            fileName = defaultIfBlank(partFileName, ((File) value).getName());
                        }

                        out.writeBytes(partialPontentDiposition);
                        out.writeBytes(isNotBlank(fileName) ? "; filename=\"" + fileName + "\"\r\n" : "\r\n");
                        out.writeBytes("Content-Type: " + contentType + "\r\n\r\n");
                        BufferedInputStream in = null;
                        try {
                            in = (BufferedInputStream) (upload instanceof BufferedInputStream ? upload : new BufferedInputStream(upload));
                            IOs.copy(in, out);
                        } finally {
                            IOs.close(in);
                        }
                        out.writeBytes("\r\n");
                    }
                } else {
                    String contentDisposition = partialPontentDiposition + (isNotBlank(partFileName) ? "; filename=\"" + partFileName + "\"\r\n" : "\r\n");
                    String contentType = "Content-Type: " + defaultIfBlank(partContentType, "text/plain") + "; charset=" + charset + "\r\n\r\n";
                    for(Pair pair : process(param, charset, false)){
                        out.writeBytes(contentDisposition);
                        out.writeBytes(contentType);
                        out.write(pair.getValue().getBytes(charset));
                        out.writeBytes("\r\n");
                    }
                }
            }
        }
        out.writeBytes(FULL_BOUNDARY + "--\r\n");
        out.writeBytes("\r\n");
    }


}
