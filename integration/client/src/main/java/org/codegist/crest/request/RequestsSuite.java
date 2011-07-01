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

package org.codegist.crest.request;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author laurent.gilles@codegist.org
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        org.codegist.crest.request.crest.GetsTest.class,
        org.codegist.crest.request.crest.PostsTest.class,
        org.codegist.crest.request.crest.PutsTest.class,
        org.codegist.crest.request.crest.DeletesTest.class,
        org.codegist.crest.request.crest.HeadsTest.class,
        org.codegist.crest.request.crest.OptionsTest.class,

        org.codegist.crest.request.jaxrs.GetsTest.class,
        org.codegist.crest.request.jaxrs.PostsTest.class,
        org.codegist.crest.request.jaxrs.PutsTest.class,
        org.codegist.crest.request.jaxrs.DeletesTest.class,
        org.codegist.crest.request.jaxrs.HeadsTest.class,
        org.codegist.crest.request.jaxrs.OptionsTest.class
})
public class RequestsSuite {
}