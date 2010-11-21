package org.codegist.crest;

import org.codegist.common.io.IOs;
import org.codegist.common.lang.Randoms;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Simple RestService implementation based on JDK's {@link java.net.HttpURLConnection}.
 *
 * @see java.net.HttpURLConnection
 */
public class DefaultRestService implements RestService {


    @Override
    public HttpResponse exec(HttpRequest request) throws HttpException {
        HttpURLConnection connection = null;
        boolean inError = false;
        try {
            connection = toHttpURLConnection(request);
            if (connection.getResponseCode() != 200) {
                throw new HttpException(connection.getResponseMessage(), new HttpResponse(request, connection.getResponseCode()));
            }
            return new HttpResponse(request, connection.getResponseCode(), connection.getInputStream(), connection.getContentEncoding());
        } catch (HttpException e) {
            inError = true;
            throw e;
        } catch (Throwable e) {
            inError = true;
            throw new HttpException(e, new HttpResponse(request, -1));
        } finally {
            if (inError) {
                if (connection != null) connection.disconnect();
            }
        }
    }


    private final static String MULTIPART = "multipart/form-data; boundary=";
    private final static String USER_AGENT = "cRest Agent";

    static HttpURLConnection toHttpURLConnection(HttpRequest request) throws IOException {
        HttpURLConnection con = newConnection(request.getUrl(true));
        con.setRequestMethod(request.getMeth().toString());
        if (request.getConnectionTimeout() != null && request.getConnectionTimeout() >= 0)
            con.setConnectTimeout(request.getConnectionTimeout().intValue());
        if (request.getSocketTimeout() != null && request.getSocketTimeout() >= 0)
            con.setReadTimeout(request.getSocketTimeout().intValue());

        if (request.getHeaders() != null) {
            for (Map.Entry<String, String> header : request.getHeaders().entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }
        }

        switch (request.getMeth()) {
            case POST:
            case PUT:
                if (Params.isForUpload(request.getBodyParams())) {
                    String boundary = Randoms.randomAlphaNumeric(16) + System.currentTimeMillis();
                    con.setRequestProperty("Content-Type", MULTIPART + boundary);
                    if (request.getBodyParams() != null) {
                        boundary = "--" + boundary;
                        con.setDoOutput(true);
                        OutputStream os = con.getOutputStream();
                        DataOutputStream out = new DataOutputStream(os);

                        for (Map.Entry<String, Object> param : request.getBodyParams().entrySet()) {
                            InputStream upload = null;
                            String name = null;
                            if (param.getValue() instanceof InputStream) {
                                upload = (InputStream) param.getValue();
                                name = param.getKey();
                            } else if (param.getValue() instanceof File) {
                                upload = new FileInputStream((File) param.getValue());
                                name = ((File) param.getValue()).getName();
                            }

                            if (upload != null) {
                                out.writeBytes(boundary + "\r\n");
                                out.writeBytes("Content-Disposition: form-data; name=\"" + param.getKey() + "\"; filename=\"" + name + "\"\r\n");
                                out.writeBytes("Content-Type: Content-Type: application/octet-stream\r\n\r\n");
                                BufferedInputStream in = null;
                                try {
                                    in = (BufferedInputStream) (upload instanceof BufferedInputStream ? upload : new BufferedInputStream(upload));
                                    IOs.copy(in, out);
                                    out.writeBytes("\r\n");
                                } finally {
                                    IOs.close(in);
                                }
                            } else if (param.getValue() != null) {
                                out.writeBytes(boundary + "\r\n");
                                out.writeBytes("Content-Disposition: form-data; name=\"" + param.getKey() + "\"\r\n");
                                out.writeBytes("Content-Type: text/plain; charset=" + request.getEncoding() + "\r\n\r\n");
                                out.write(param.getValue().toString().getBytes(request.getEncoding()));
                                out.writeBytes("\r\n");
                            }
                        }
                        out.writeBytes(boundary + "--\r\n");
                        out.writeBytes("\r\n");
                    }
                } else {
                    byte[] data = new byte[0];
                    if (request.getBodyParams() != null) {
                        data = Params.encodeParams(request.getBodyParams(), request.getEncoding()).getBytes(request.getEncoding());
                    }
                    con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=" + request.getEncoding());
                    con.setRequestProperty("Content-Length", Integer.toString(data.length));
                    if (data.length > 0) {
                        con.setDoOutput(true);
                        OutputStream os = con.getOutputStream();
                        DataOutputStream out = new DataOutputStream(os);
                        out.write(data);
                        os.flush();
                        os.close();
                    }
                }
                break;
            default:
        }

        return con;
    }

    protected static HttpURLConnection newConnection(URL url/*, ProxyConf proxyConf*/) throws IOException {
        HttpURLConnection con;
        /*
        if (proxyConf != null) {
            if (proxyConf.hasCredentials()) {
                Authenticator.setDefault(new ProxyConfAuthenticator(proxyConf));
            }
            Proxy proxy = new Proxy(Proxy.Type.HTTP, InetSocketAddress.createUnresolved(proxyConf.getHost(), proxyConf.getPort()));
            con = (HttpURLConnection) url.openConnection(proxy);
        } else {
            con = (HttpURLConnection) url.openConnection();
        }
        */
        con = (HttpURLConnection) url.openConnection();
        con.addRequestProperty("User-Agent", USER_AGENT);
        return con;
    }
    /*
    private static class ProxyConfAuthenticator extends Authenticator {
        private final ProxyConf proxyConf;

        private ProxyConfAuthenticator(ProxyConf proxyConf) {
            this.proxyConf = proxyConf;
        }

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            if (getRequestorType().equals(RequestorType.PROXY)) {
                return new PasswordAuthentication(proxyConf.getUser(), proxyConf.getPwdAsChars());
            } else {
                return null;
            }
        }
    }

    public static class ProxyConf {
        private final String user;
        private final String pwd;
        private final String host;
        private final int port;

        public ProxyConf(String host, int port) {
            this(host, port, null, null);
        }

        public ProxyConf(String host, int port, String user, String pwd) {
            this.user = user;
            this.pwd = pwd;
            this.host = host;
            this.port = port;
        }

        public boolean hasCredentials() {
            return Strings.isNotBlank(user);
        }

        public String getUser() {
            return user;
        }

        public String getPwd() {
            return pwd;
        }

        public char[] getPwdAsChars() {
            return pwd != null ? pwd.toCharArray() : null;
        }

        public String getHost() {
            return host;
        }

        public int getPort() {
            return port;
        }
    }
    */
}
