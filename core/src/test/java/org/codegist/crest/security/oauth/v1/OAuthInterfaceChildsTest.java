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

import org.codegist.crest.annotate.*;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.codegist.crest.util.Methods.byName;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class OAuthInterfaceChildsTest {

    @Test
    public void getChildShouldBeProperlyAnnotated() {
        assertNotNull(GetOAuthInterface.class.getAnnotation(GET.class));
        assertNotNull(GetOAuthInterface.class.getAnnotation(Encoded.class));
        assertNotNull(GetOAuthInterface.class.getAnnotation(Deserializer.class));
        assertEquals(OAuthTokenDeserializer.class, GetOAuthInterface.class.getAnnotation(Deserializer.class).value());

        Method getAccessToken = byName(GetOAuthInterface.class, "getAccessToken");
        Method getRequestToken = byName(GetOAuthInterface.class, "getRequestToken");
        Method refreshAccessToken = byName(GetOAuthInterface.class, "refreshAccessToken");

        assertNotNull(getAccessToken.getAnnotation(Path.class));
        assertEquals("{oauth.access-token-path}", getAccessToken.getAnnotation(Path.class).value());

        assertEquals(QueryParam.class, getAccessToken.getParameterAnnotations()[0][0].annotationType());
        assertEquals("oauth_consumer_key", ((QueryParam)getAccessToken.getParameterAnnotations()[0][0]).value());
        assertEquals(QueryParam.class, getAccessToken.getParameterAnnotations()[1][0].annotationType());
        assertEquals("oauth_signature_method", ((QueryParam)getAccessToken.getParameterAnnotations()[1][0]).value());
        assertEquals(QueryParam.class, getAccessToken.getParameterAnnotations()[2][0].annotationType());
        assertEquals("oauth_timestamp", ((QueryParam)getAccessToken.getParameterAnnotations()[2][0]).value());
        assertEquals(QueryParam.class, getAccessToken.getParameterAnnotations()[3][0].annotationType());
        assertEquals("oauth_nonce", ((QueryParam)getAccessToken.getParameterAnnotations()[3][0]).value());
        assertEquals(QueryParam.class, getAccessToken.getParameterAnnotations()[4][0].annotationType());
        assertEquals("oauth_version", ((QueryParam)getAccessToken.getParameterAnnotations()[4][0]).value());
        assertEquals(QueryParam.class, getAccessToken.getParameterAnnotations()[5][0].annotationType());
        assertEquals("oauth_verifier", ((QueryParam)getAccessToken.getParameterAnnotations()[5][0]).value());
        assertEquals(QueryParam.class, getAccessToken.getParameterAnnotations()[6][0].annotationType());
        assertEquals("oauth_signature", ((QueryParam)getAccessToken.getParameterAnnotations()[6][0]).value());

        assertNotNull(getRequestToken.getAnnotation(Path.class));
        assertEquals("{oauth.request-token-path}", getRequestToken.getAnnotation(Path.class).value());

        assertEquals(QueryParam.class, getRequestToken.getParameterAnnotations()[0][0].annotationType());
        assertEquals("oauth_consumer_key", ((QueryParam)getRequestToken.getParameterAnnotations()[0][0]).value());
        assertEquals(QueryParam.class, getRequestToken.getParameterAnnotations()[1][0].annotationType());
        assertEquals("oauth_signature_method", ((QueryParam)getRequestToken.getParameterAnnotations()[1][0]).value());
        assertEquals(QueryParam.class, getRequestToken.getParameterAnnotations()[2][0].annotationType());
        assertEquals("oauth_timestamp", ((QueryParam)getRequestToken.getParameterAnnotations()[2][0]).value());
        assertEquals(QueryParam.class, getRequestToken.getParameterAnnotations()[3][0].annotationType());
        assertEquals("oauth_nonce", ((QueryParam)getRequestToken.getParameterAnnotations()[3][0]).value());
        assertEquals(QueryParam.class, getRequestToken.getParameterAnnotations()[4][0].annotationType());
        assertEquals("oauth_version", ((QueryParam)getRequestToken.getParameterAnnotations()[4][0]).value());
        assertEquals(QueryParam.class, getRequestToken.getParameterAnnotations()[5][0].annotationType());
        assertEquals("oauth_callback", ((QueryParam)getRequestToken.getParameterAnnotations()[5][0]).value());
        assertEquals(QueryParam.class, getRequestToken.getParameterAnnotations()[6][0].annotationType());
        assertEquals("oauth_signature", ((QueryParam)getRequestToken.getParameterAnnotations()[6][0]).value());

        assertNotNull(refreshAccessToken.getAnnotation(Path.class));
        assertEquals("{oauth.refresh-access-token-path}", refreshAccessToken.getAnnotation(Path.class).value());

        assertEquals(QueryParam.class, refreshAccessToken.getParameterAnnotations()[0][0].annotationType());
        assertEquals("oauth_token", ((QueryParam)refreshAccessToken.getParameterAnnotations()[0][0]).value());
        assertEquals(QueryParam.class, refreshAccessToken.getParameterAnnotations()[1][0].annotationType());
        assertEquals("oauth_consumer_key", ((QueryParam)refreshAccessToken.getParameterAnnotations()[1][0]).value());
        assertEquals(QueryParam.class, refreshAccessToken.getParameterAnnotations()[2][0].annotationType());
        assertEquals("oauth_signature_method", ((QueryParam)refreshAccessToken.getParameterAnnotations()[2][0]).value());
        assertEquals(QueryParam.class, refreshAccessToken.getParameterAnnotations()[3][0].annotationType());
        assertEquals("oauth_timestamp", ((QueryParam)refreshAccessToken.getParameterAnnotations()[3][0]).value());
        assertEquals(QueryParam.class, refreshAccessToken.getParameterAnnotations()[4][0].annotationType());
        assertEquals("oauth_nonce", ((QueryParam)refreshAccessToken.getParameterAnnotations()[4][0]).value());
        assertEquals(QueryParam.class, refreshAccessToken.getParameterAnnotations()[5][0].annotationType());
        assertEquals("oauth_version", ((QueryParam)refreshAccessToken.getParameterAnnotations()[5][0]).value());
        assertEquals(QueryParam.class, refreshAccessToken.getParameterAnnotations()[6][0].annotationType());
        assertEquals("oauth_session_handle", ((QueryParam)refreshAccessToken.getParameterAnnotations()[6][0]).value());
        assertEquals(QueryParam.class, refreshAccessToken.getParameterAnnotations()[7][0].annotationType());
        assertEquals("oauth_signature", ((QueryParam)refreshAccessToken.getParameterAnnotations()[7][0]).value());
    }


    @Test
    public void postChildShouldBeProperlyAnnotated() {
        assertNotNull(PostOAuthInterface.class.getAnnotation(POST.class));
        assertNotNull(PostOAuthInterface.class.getAnnotation(Encoded.class));
        assertNotNull(PostOAuthInterface.class.getAnnotation(Deserializer.class));
        assertEquals(OAuthTokenDeserializer.class, PostOAuthInterface.class.getAnnotation(Deserializer.class).value());

        Method getAccessToken = byName(PostOAuthInterface.class, "getAccessToken");
        Method getRequestToken = byName(PostOAuthInterface.class, "getRequestToken");
        Method refreshAccessToken = byName(PostOAuthInterface.class, "refreshAccessToken");

        assertNotNull(getAccessToken.getAnnotation(Path.class));
        assertEquals("{oauth.access-token-path}", getAccessToken.getAnnotation(Path.class).value());

        assertEquals(FormParam.class, getAccessToken.getParameterAnnotations()[0][0].annotationType());
        assertEquals("oauth_consumer_key", ((FormParam)getAccessToken.getParameterAnnotations()[0][0]).value());
        assertEquals(FormParam.class, getAccessToken.getParameterAnnotations()[1][0].annotationType());
        assertEquals("oauth_signature_method", ((FormParam)getAccessToken.getParameterAnnotations()[1][0]).value());
        assertEquals(FormParam.class, getAccessToken.getParameterAnnotations()[2][0].annotationType());
        assertEquals("oauth_timestamp", ((FormParam)getAccessToken.getParameterAnnotations()[2][0]).value());
        assertEquals(FormParam.class, getAccessToken.getParameterAnnotations()[3][0].annotationType());
        assertEquals("oauth_nonce", ((FormParam)getAccessToken.getParameterAnnotations()[3][0]).value());
        assertEquals(FormParam.class, getAccessToken.getParameterAnnotations()[4][0].annotationType());
        assertEquals("oauth_version", ((FormParam)getAccessToken.getParameterAnnotations()[4][0]).value());
        assertEquals(FormParam.class, getAccessToken.getParameterAnnotations()[5][0].annotationType());
        assertEquals("oauth_verifier", ((FormParam)getAccessToken.getParameterAnnotations()[5][0]).value());
        assertEquals(FormParam.class, getAccessToken.getParameterAnnotations()[6][0].annotationType());
        assertEquals("oauth_signature", ((FormParam)getAccessToken.getParameterAnnotations()[6][0]).value());

        assertNotNull(getRequestToken.getAnnotation(Path.class));
        assertEquals("{oauth.request-token-path}", getRequestToken.getAnnotation(Path.class).value());

        assertEquals(FormParam.class, getRequestToken.getParameterAnnotations()[0][0].annotationType());
        assertEquals("oauth_consumer_key", ((FormParam)getRequestToken.getParameterAnnotations()[0][0]).value());
        assertEquals(FormParam.class, getRequestToken.getParameterAnnotations()[1][0].annotationType());
        assertEquals("oauth_signature_method", ((FormParam)getRequestToken.getParameterAnnotations()[1][0]).value());
        assertEquals(FormParam.class, getRequestToken.getParameterAnnotations()[2][0].annotationType());
        assertEquals("oauth_timestamp", ((FormParam)getRequestToken.getParameterAnnotations()[2][0]).value());
        assertEquals(FormParam.class, getRequestToken.getParameterAnnotations()[3][0].annotationType());
        assertEquals("oauth_nonce", ((FormParam)getRequestToken.getParameterAnnotations()[3][0]).value());
        assertEquals(FormParam.class, getRequestToken.getParameterAnnotations()[4][0].annotationType());
        assertEquals("oauth_version", ((FormParam)getRequestToken.getParameterAnnotations()[4][0]).value());
        assertEquals(FormParam.class, getRequestToken.getParameterAnnotations()[5][0].annotationType());
        assertEquals("oauth_callback", ((FormParam)getRequestToken.getParameterAnnotations()[5][0]).value());
        assertEquals(FormParam.class, getRequestToken.getParameterAnnotations()[6][0].annotationType());
        assertEquals("oauth_signature", ((FormParam)getRequestToken.getParameterAnnotations()[6][0]).value());

        assertNotNull(refreshAccessToken.getAnnotation(Path.class));
        assertEquals("{oauth.refresh-access-token-path}", refreshAccessToken.getAnnotation(Path.class).value());

        assertEquals(FormParam.class, refreshAccessToken.getParameterAnnotations()[0][0].annotationType());
        assertEquals("oauth_token", ((FormParam)refreshAccessToken.getParameterAnnotations()[0][0]).value());
        assertEquals(FormParam.class, refreshAccessToken.getParameterAnnotations()[1][0].annotationType());
        assertEquals("oauth_consumer_key", ((FormParam)refreshAccessToken.getParameterAnnotations()[1][0]).value());
        assertEquals(FormParam.class, refreshAccessToken.getParameterAnnotations()[2][0].annotationType());
        assertEquals("oauth_signature_method", ((FormParam)refreshAccessToken.getParameterAnnotations()[2][0]).value());
        assertEquals(FormParam.class, refreshAccessToken.getParameterAnnotations()[3][0].annotationType());
        assertEquals("oauth_timestamp", ((FormParam)refreshAccessToken.getParameterAnnotations()[3][0]).value());
        assertEquals(FormParam.class, refreshAccessToken.getParameterAnnotations()[4][0].annotationType());
        assertEquals("oauth_nonce", ((FormParam)refreshAccessToken.getParameterAnnotations()[4][0]).value());
        assertEquals(FormParam.class, refreshAccessToken.getParameterAnnotations()[5][0].annotationType());
        assertEquals("oauth_version", ((FormParam)refreshAccessToken.getParameterAnnotations()[5][0]).value());
        assertEquals(FormParam.class, refreshAccessToken.getParameterAnnotations()[6][0].annotationType());
        assertEquals("oauth_session_handle", ((FormParam)refreshAccessToken.getParameterAnnotations()[6][0]).value());
        assertEquals(FormParam.class, refreshAccessToken.getParameterAnnotations()[7][0].annotationType());
        assertEquals("oauth_signature", ((FormParam)refreshAccessToken.getParameterAnnotations()[7][0]).value());
    }

}
