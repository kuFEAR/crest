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

package org.codegist.crest.annotate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Identifies the URI path that the method will call.</p>
 * <p>Paths are relative and cumulatives. The base URI is the end-point, see {@link EndPoint}, and if a @Path has been defined at interface level, it will be concatenated to the end-point before the method's specific @Path. Shashes (\) are optional and will be added if needed while concatenating EndPoint and Path values.</p>
 * <code><pre>
 * &#64;EndPoint("http://localhost:90")
 * &#64;Path("base")
 * &#64;GET
 * public class SomeResource {
 *
 *   String getList() {...}
 *
 *   &#64;EndPoint("http://localhost:80")
 *   &#64;Path("{id}")
 *   String getWidget(&#64;PathParam("id") String id) {...}
 * }
 * </pre></code>
 * <p>In the above:</p>
 * <p>* getList will issue a GET request to http://localhost:90/base</p>
 * <p>* getWidget will issue a GET request to http://localhost:80/base/nnn (where nnn is the method parameter value)</p>
 * <p/>
 * <p>The same would apply if the value of either @Path annotation started with '/'.</p>
 * <p>Can contain placeholders, see {@link org.codegist.crest.CRestBuilder#placeholder(String, String)}.</p>
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 * @see org.codegist.crest.CRestBuilder#placeholder(String, String)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Path {

    /**
     * Defines a URI template for the resource class or method, must not
     * include matrix parameters.
     *
     * <p>Embedded template parameters are allowed and are of the form:</p>
     *
     * <pre> param = "{" *WSP name *WSP [ ":" *WSP regex *WSP ] "}"
     * name = (ALPHA / DIGIT / "_")*(ALPHA / DIGIT / "." / "_" / "-" ) ; \w[\w\.-]*
     * regex = *( nonbrace / "{" *nonbrace "}" ) ; where nonbrace is any char other than "{" and "}"</pre>
     *
     * <p>See <a href="http://tools.ietf.org/html/rfc5234">RFC 5234</a>
     * for a description of the syntax used above and the expansions of
     * {@code WSP}, {@code ALPHA} and {@code DIGIT}. In the above {@code name}
     * is the template parameter name and the optional {@code regex} specifies
     * the contents of the capturing group for the parameter. If {@code regex}
     * is not supplied then a default value of {@code [^/]+} which terminates at
     * a path segment boundary, is used. Matching of request URIs to URI
     * templates is performed against encoded path values and implementations
     * will not escape literal characters in regex automatically, therefore any
     * literals in {@code regex} should be escaped by the author according to
     * the rules of
     * <a href="http://tools.ietf.org/html/rfc3986#section-3.3">RFC 3986 section 3.3</a>.
     * Caution is recommended in the use of {@code regex}, incorrect use can
     * lead to a template parameter matching unexpected URI paths. See
     * <a href="http://java.sun.com/j2se/1.5.0/docs/api/java/util/regex/Pattern.html">Pattern</a>
     * for further information on the syntax of regular expressions.
     * Values of template parameters may be extracted using {@link PathParam}.
     *
     * <p>The literal part of the supplied value (those characters
     * that are not part of a template parameter) is automatically percent
     * encoded to conform to the {@code path} production of
     * <a href="http://tools.ietf.org/html/rfc3986#section-3.3">RFC 3986 section 3.3</a>.
     * Note that percent encoded values are allowed in the literal part of the
     * value, an implementation will recognize such values and will not double
     * encode the '%' character.</p>
     */
    String value();
}
