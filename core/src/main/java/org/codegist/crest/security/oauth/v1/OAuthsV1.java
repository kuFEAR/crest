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

package org.codegist.crest.security.oauth.v1;

import org.codegist.common.codec.Base64;
import org.codegist.common.log.Logger;
import org.codegist.crest.io.http.HttpMethod;
import org.codegist.crest.io.http.Pair;
import org.codegist.crest.security.oauth.OAuthToken;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static org.codegist.common.net.Urls.encode;
import static org.codegist.crest.util.Pairs.join;
import static org.codegist.crest.util.Pairs.sortByNameAndValues;

/**
 * @author laurent.gilles@codegist.org
 */
final class OAuthsV1 {
    private OAuthsV1() {
        throw new IllegalStateException();
    }

    private static final Logger LOGGER = Logger.getLogger(OAuthsV1.class);
    private static final int AFTER_PROTOCOL_INDEX = 8;

    /**
     * The Signature Base String includes the io absolute URL, tying the signature to a specific endpoint. The URL used in the Signature Base String MUST include the scheme, authority, and path, and MUST exclude the query and fragment as defined by [RFC3986] section 3.<br>
     * If the absolute io URL is not available to the Service Provider (it is always available to the Consumer), it can be constructed by combining the scheme being used, the HTTP Host header, and the relative HTTP io URL. If the Host header is not available, the Service Provider SHOULD use the host name communicated to the Consumer in the documentation or other means.<br>
     * The Service Provider SHOULD document the form of URL used in the Signature Base String to avoid ambiguity due to URL normalization. Unless specified, URL scheme and authority MUST be lowercase and include the port number; http default port 80 and https default port 443 MUST be excluded.<br>
     * <br>
     * For example, the io:<br>
     * HTTP://Example.com:80/resource?id=123<br>
     * Is included in the Signature Base String as:<br>
     * http://example.com/resource
     *
     * @param url the url to be normalized
     * @return the Signature Base String
     * @see <a href="http://oauth.net/core/1.0#rfc.section.9.1.2">OAuth Core - 9.1.2.  Construct Request URL</a>
     */
    static String constructRequestURL(String url) {
        int index = url.indexOf('?');

        String retVal = url;
        if (-1 != index) {
            retVal = retVal.substring(0, index);
        }
        int slashIndex = retVal.indexOf('/', AFTER_PROTOCOL_INDEX);
        String baseURL = retVal.substring(0, slashIndex).toLowerCase();
        int colonIndex = baseURL.indexOf(':', AFTER_PROTOCOL_INDEX);
        if (-1 != colonIndex) {
            // url contains port number
            if (baseURL.startsWith("http://") && baseURL.endsWith(":80")) {
                // http default port 80 MUST be excluded
                baseURL = baseURL.substring(0, colonIndex);
            } else if (baseURL.startsWith("https://") && baseURL.endsWith(":443")) {
                // http default port 443 MUST be excluded
                baseURL = baseURL.substring(0, colonIndex);
            }
        }
        return baseURL + retVal.substring(slashIndex);
    }

    static String sign(OAuthToken consumerOAuthToken, OAuthToken accessOAuthToken, String signAlgorithm, String encoding, String url, HttpMethod method, List<Pair> oauthParams) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        // first, sort the list without changing the one given
        List<Pair> sorted = sortByNameAndValues(oauthParams);

        String signMeth = method.toString();
        String signUri = constructRequestURL(url);
        String signParams = join(sorted, '&', '=', false, false);
        String signature = consumerOAuthToken.getSecret() + "&" + accessOAuthToken.getSecret();

        Mac mac = Mac.getInstance(signAlgorithm);
        mac.init(new SecretKeySpec(signature.getBytes(encoding), signAlgorithm));

        String data = signMeth + "&" + encode(signUri, encoding) + "&" + encode(signParams, encoding);
        String encoded = new String(Base64.encodeToByte(mac.doFinal(data.getBytes(encoding))), encoding);

        LOGGER.debug("Signature[data=\"%s\",signature=\"%s\",result=\"%s\"]", data, signature, encoded);
        return encoded;
    }
}
