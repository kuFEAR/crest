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

import org.codegist.crest.NonInstanciableClassTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class OAuthsV1Test extends NonInstanciableClassTest {

    public OAuthsV1Test() {
        super(OAuthsV1.class);
    }

    @Test
    public void shouldDropPort80FromHttpUrl(){
        String url = "http://localhost:80/test";
        String actual = OAuthsV1.constructRequestURL(url);
        assertEquals("http://localhost/test", actual);
    }

    @Test
    public void shouldKeepAnyPortFromHttpUrl(){
        String url = "http://localhost:81/test";
        String actual = OAuthsV1.constructRequestURL(url);
        assertEquals(url, actual);
    }

    @Test
    public void shouldDropPort443FromHttpsUrl(){
        String url = "https://localhost:443/test";
        String actual = OAuthsV1.constructRequestURL(url);
        assertEquals("https://localhost/test", actual);
    }
    
    @Test
    public void shouldKeepAnyPortFromHttpsUrl(){
        String url = "https://localhost:444/test";
        String actual = OAuthsV1.constructRequestURL(url);
        assertEquals(url, actual);
    }

    @Test
    public void shouldKeepUrlUnchangedWhenNoPort(){
        String url = "http://localhost/test";
        String actual = OAuthsV1.constructRequestURL(url);
        assertEquals(url, actual);
    }

    @Test
    public void shouldDropUrlStringFromUrl(){
        String url = "http://localhost/test?query=string";
        String actual = OAuthsV1.constructRequestURL(url);
        assertEquals("http://localhost/test", actual);
    }

}
