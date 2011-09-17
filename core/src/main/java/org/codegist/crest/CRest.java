/*
 * Copyright 2010 CodeGist.org
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 * ===================================================================
 *
 * More information at http://www.codegist.org.
 */

package org.codegist.crest;

import java.util.Map;

/**
 * <p><b>CRest</b> is a rest annotated interface builder.</p>
 * <p>Here's a quick sample of how to use it.</p>
 * <p>Given the following interface, reflecting a simple RESTful API:</p>
 * <code><pre>
 * &#64;EndPoint("http://some.server")
 * public interface SimpleRestService {
 *
 *     &#64;POST
 *     &#64;Path("something")
 *     SimpleResult doSomething(&#64;QueryParam("arg1") int p1, &#64;FormParam("arg2")String p2);
 *
 * }
 * </pre></code>
 * <p><code>SimpleResult</code> object is a plain POJO annotated with either <a href="http://jaxb.java.net/nonav/2.1/docs/api/javax/xml/bind/annotation/package-summary.html">JAXB</a>, <a href="http://simple.sourceforge.net/download/stream/doc/javadoc/">SimpleXML</a> or <a href="http://jackson.codehaus.org/1.8.4/javadoc/org/codehaus/jackson/annotate/package-summary.html">Jackson</a> annotations depending of the server response format.</p>
 * <p>And here's how to get a working binded instance of <code>SimpleRestService</code>:</p>
 * <code><pre>
 * CRest crest = CRest.getInstance(); // CRest instance should be created once and re-used to build any rest interface instances
 * SimpleRestService service = crest.build(SimpleRestService.class); // rest interface once built should be kept and re-used for any futur use
 * </pre></code>
 * <p><b>CRest</b> handle annotations from {@link org.codegist.crest.annotate} package as well as <a href="http://jsr311.java.net/">JAX-RS 1.0</a> {@link javax.ws.rs} if the later is available in the classpath.</p>
 * <p>Also note: <b>CRest</b> is an expensive object to create and should be created once at the application bootstrap and re-used. <b>CRest</b> instances are threadsafe.</p>
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 * @see org.codegist.crest.CRestBuilder
 * @see org.codegist.crest.annotate
 * @see javax.ws.rs
 * @see <a href="http://jsr311.java.net/">JAX-RS 1.0</a>
 */
public abstract class CRest {

    /**
     * <p>Build an instance of an annotated interface.</p>
     * <p>Building an instance of an annotated interface is an expensive operation and should be done only once at the application bootstrap, and resulting instances should be re-used. The resulting instances are threadsafe.</p>
     * @return an instance of an annotated interface
     */
    public abstract <T> T build(Class<T> interfaze) throws CRestException;

    /**
     * <p>Build a <b>CRest</b> instance.</p>
     * @return a <b>CRest</b> instance
     */
    public static CRest getInstance(){
        return new CRestBuilder().build();
    }

    /**
     * <p>Build a <b>CRest</b> instance that points by default to the given end-point.</p>
     * <p>It is not required anymore to set the @EndPoint annotation to the interfaces passed to the resulting <b>CRest</b> instance.</p>
     * @param endpoint end point to point at
     * @return a <b>CRest</b> instance
     * @see org.codegist.crest.CRestBuilder#endpoint(String)
     */
    public static CRest getInstance(String endpoint){
        return endpoint(endpoint).build();
    }

    /**
     * <p>Build a <b>CRest</b> instance with the given string-based annotation placeholder replacement map.</p>
     * @param placeholders placeholder map to use for string-based annotation placeholder replacement
     * @return a <b>CRest</b> instance
     * @see org.codegist.crest.CRestBuilder#setPlaceholders(java.util.Map)
     */
    public static CRest getInstance(Map<String,String> placeholders){
        return placeholders(placeholders).build();
    }

    /**
     * <p>Build a <b>CRest</b> instance that authenticate all request using OAuth.</p>
     * @param consumerKey consumer key to use
     * @param consumerSecret consumer secret to use
     * @param accessToken access token to use
     * @param accessTokenSecret access token secret to use
     * @return a <b>CRest</b> instance
     * @see org.codegist.crest.CRestBuilder#oauth(String, String, String, String)
     */
    public static CRest getOAuthInstance(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret){
        return oauth(consumerKey, consumerSecret, accessToken, accessTokenSecret).build();
    }

    /**
     * <p>Build a <b>CRest</b> instance that authenticate all request using OAuth.</p>
     * @param consumerKey consumer key to use
     * @param consumerSecret consumer secret to use
     * @param accessToken access token to use
     * @param accessTokenSecret access token secret to use
     * @param sessionHandle session handle to use to refresh an expired access token
     * @param accessTokenRefreshUrl url to use to refresh an expired access token
     * @return a <b>CRest</b> instance
     * @see org.codegist.crest.CRestBuilder#oauth(String, String, String, String, String, String)
     */
    public static CRest getOAuthInstance(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret, String sessionHandle, String accessTokenRefreshUrl) {
        return oauth(consumerKey, consumerSecret, accessToken, accessTokenSecret, sessionHandle, accessTokenRefreshUrl).build();
    }

    /**
     * <p>Build a <b>CRest</b> instance that authenticate all request using Basic Auth.</p>
     * @param username user name to authenticate the requests with
     * @param password password to authenticate the requests with
     * @return a <b>CRest</b> instance
     * @see org.codegist.crest.CRestBuilder#basicAuth(String, String)
     */
    public static CRest getBasicAuthInstance(String username, String password){
        return basicAuth(username, password).build();
    }

    /**
     * <p>Sets the default endpoint all interfaces build through the resulting <b>CRest</b> instance will point at.</p>
     * @param endpoint end point to point at
     * @return a CRestBuilder instance
     * @see org.codegist.crest.CRestBuilder#endpoint(String) 
     */
    public static CRestBuilder endpoint(String endpoint) {
        return new CRestBuilder().endpoint(endpoint);
    }

    /**
     * <p>Adds given property to the {@link org.codegist.crest.CRestConfig} that will be passed to all <b>CRest</b> components.</p>
     * @param name property name
     * @param value property value
     * @return a CRestBuilder instance
     * @see org.codegist.crest.CRestBuilder#property(String, Object) 
     */
    public static CRestBuilder property(String name, Object value){
        return new CRestBuilder().property(name, value);
    }

    /**
     * <p>Adds the given placeholder to the string-based annotations placeholders replacement map.</p>
     * @param placeholder the placeholder to be replaced
     * @param value the value to replace the placeholder with in string-based annotations
     * @return a CRestBuilder instance
     * @see org.codegist.crest.CRestBuilder#placeholder(String, String)  
     */
    public static CRestBuilder placeholder(String placeholder, String value){
        return new CRestBuilder().placeholder(placeholder, value);
    }

    /**
     * <p>Sets all given placeholders to the string-based annotations placeholders replacement map.</p>
     * @param placeholders placeholder map to use for string-based annotation placeholder replacement
     * @return a CRestBuilder instance
     * @see org.codegist.crest.CRestBuilder#setPlaceholders(java.util.Map)   
     */                                    
    public static CRestBuilder placeholders(Map<String,String> placeholders){
        return new CRestBuilder().setPlaceholders(placeholders);
    }

    /**
     * <p>Configures the resulting <b>CRest</b> instance to authenticate all requests using OAuth 1.0</p>
     * @param consumerKey consumer key to use
     * @param consumerSecret consumer secret to use
     * @param accessToken access token to use
     * @param accessTokenSecret access token secret to use
     * @return a CRestBuilder instance
     * @see org.codegist.crest.CRestBuilder#oauth(String, String, String, String) 
     */
    public static CRestBuilder oauth(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret){
        return new CRestBuilder().oauth(consumerKey, consumerSecret, accessToken, accessTokenSecret);
    }

    /**
     * <p>Configures the resulting <b>CRest</b> instance to authenticate all requests using OAuth 1.0</p>
     * @param consumerKey consumer key to use
     * @param consumerSecret consumer secret to use
     * @param accessToken access token to use
     * @param accessTokenSecret access token secret to use
     * @param sessionHandle session handle to use to refresh an expired access token
     * @param accessTokenRefreshUrl url to use to refresh an expired access token
     * @return a CRestBuilder instance
     * @see org.codegist.crest.CRestBuilder#oauth(String, String, String, String, String, String) 
     */
    public static CRestBuilder oauth(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret, String sessionHandle, String accessTokenRefreshUrl){
        return new CRestBuilder().oauth(consumerKey, consumerSecret, accessToken, accessTokenSecret, sessionHandle, accessTokenRefreshUrl);
    }

    /**
     * <p>Configures the resulting <b>CRest</b> instance to authenticate all requests using Basic Auth</p>
     * @param username user name to authenticate the requests with
     * @param password password to authenticate the requests with
     * @return a CRestBuilder instance
     * @see org.codegist.crest.CRestBuilder#basicAuth(String, String) 
     */
    public static CRestBuilder basicAuth(String username, String password){
        return new CRestBuilder().basicAuth(username, password);
    }
}
