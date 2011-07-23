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

import org.codegist.crest.param.EncodedPair;
import org.codegist.crest.param.SimpleEncodedPair;
import org.codegist.crest.security.oauth.OAuthToken;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static org.codegist.crest.util.Pairs.join;
import static org.junit.Assert.assertEquals;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class OAuthTokenDeserializerTest {

    private static final List<SimpleEncodedPair> pairs = Arrays.asList(
            new SimpleEncodedPair("oauth_token", "oauthToken"),
            new SimpleEncodedPair("oauth_token_secret", "oauthTokenSecret"),
            new SimpleEncodedPair("extra1", "extra1Val"),
            new SimpleEncodedPair("extra2", "extra2Val")
    );
    private final InputStream DATA = new ByteArrayInputStream(join(pairs,'&', '=').getBytes());
    private final OAuthTokenDeserializer toTest = new OAuthTokenDeserializer();

    @Test
    public void shouldDeserializeUrlFormEncodedToken() throws Exception {
        OAuthToken actual = toTest.deserialize(DATA, Charset.defaultCharset());
        assertEquals("oauthToken", actual.getToken());
        assertEquals("oauthTokenSecret", actual.getSecret());
        assertEquals("extra1", actual.getAttribute("extra1").getName());
        assertEquals("extra1Val", actual.getAttribute("extra1").getValue());
        assertEquals("extra2", actual.getAttribute("extra2").getName());
        assertEquals("extra2Val", actual.getAttribute("extra2").getValue());
    }
}
