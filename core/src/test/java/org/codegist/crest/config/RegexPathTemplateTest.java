/*
 * Copyright 2011 CodeGist.org
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

package org.codegist.crest.config;

import org.junit.Test;

import static org.codegist.crest.test.util.Values.UTF8;
import static org.junit.Assert.assertEquals;

/**
 * @author laurent.gilles@codegist.org
 */
public class RegexPathTemplateTest {

    @Test(expected = IllegalArgumentException.class)
    public void pathTemplateCreateShouldFailIfTwoTemplateWithSameNameAreDetected() throws Exception {
        try {
            RegexPathTemplate.create("http://localhost/{template-name}/{template-name:[a-z]{2}}");
        } catch (Exception e) {
            assertEquals("Template name 'template-name' is already defined!", e.getMessage());
            throw e;
        }
    }
    @Test(expected = IllegalArgumentException.class)
    public void pathTemplateCreateShouldFailIfTemplateNameIsNotValid() throws Exception {
        try {
            RegexPathTemplate.create("http://localhost/{template name}");
        } catch (Exception e) {
            assertEquals("Template name 'template name' doesn't match the expected format: ^\\w[-\\w\\.]+$", e.getMessage());
            throw e;
        }
    }
    @Test(expected = IllegalArgumentException.class)
    public void pathTemplateCreateShouldFailIfTemplateNameIsEmpty() throws Exception {
        try {
            RegexPathTemplate.create("http://localhost/{}");
        } catch (Exception e) {
            assertEquals("Template name '' doesn't match the expected format: ^\\w[-\\w\\.]+$", e.getMessage());
            throw e;
        }
    }
    @Test(expected = IllegalArgumentException.class)
    public void pathTemplateCreateShouldFailIfTemplateNameIsEmptyWithValidationRegex() throws Exception {
        try {
            RegexPathTemplate.create("http://localhost/{:[a-z]}");
        } catch (Exception e) {
            assertEquals("Template name '' doesn't match the expected format: ^\\w[-\\w\\.]+$", e.getMessage());
            throw e;
        }
    }

    @Test
    public void pathTemplateCreateShouldWorkWithNoTemplates() throws Exception {
        String expected = "http://localhost:80/hello";
        String actual = RegexPathTemplate.create(expected).getBuilder(UTF8).build();
        assertEquals(expected, actual);
    }

    @Test(expected=IllegalArgumentException.class)
    public void pathBuilderShouldFailIfMergedTwice() throws Exception {
        String urlTemplate = "http://localhost/{aaa}";
        PathTemplate toTest = RegexPathTemplate.create(urlTemplate);
        PathBuilder builder = toTest.getBuilder(UTF8);
        try {
            builder.merge("aaa", "123", false).merge("aaa", "sss", false);
        } catch (Exception e) {
            assertEquals("Path parameters is unknown or has already been provided for base uri 'http://localhost/123' (template:http://localhost/{aaa})! Param: aaa", e.getMessage());
            throw e;
        }
    }

    @Test(expected=IllegalStateException.class)
    public void pathBuilderShouldFailIfSomeParamAreStillToBeMerged() throws Exception {
        String urlTemplate = "http://localhost/{aaa}/{bbb}";
        PathTemplate toTest = RegexPathTemplate.create(urlTemplate);
        PathBuilder builder = toTest.getBuilder(UTF8);
        try {
            builder.merge("aaa", "123", false).build();
        } catch (Exception e) {
            assertEquals("Not all path templates have been merged! (url=http://localhost/123/{bbb})", e.getMessage());
            throw e;
        }
    }


    @Test(expected=IllegalArgumentException.class)
    public void pathBuilderShouldFailIfParamValueDoesNotPassDefaultValidation() throws Exception {
        String urlTemplate = "http://localhost/{aaa}";
        PathTemplate toTest = RegexPathTemplate.create(urlTemplate);
        PathBuilder builder = toTest.getBuilder(UTF8);
        try {
            builder.merge("aaa", "a/h", false).build();
        } catch (Exception e) {
            assertEquals("Path param aaa=a/h don't matches expected format ^[^/]+$", e.getMessage());
            throw e;
        }
    }


    @Test(expected=IllegalArgumentException.class)
    public void pathBuilderShouldFailIfParamValueDoesNotPassCustomValidation() throws Exception {
        String urlTemplate = "http://localhost/{aaa:\\d{3}}";
        PathTemplate toTest = RegexPathTemplate.create(urlTemplate);
        PathBuilder builder = toTest.getBuilder(UTF8);
        try {
            builder.merge("aaa", "1234", false).build();
        } catch (Exception e) {
            assertEquals("Path param aaa=1234 don't matches expected format ^\\d{3}$", e.getMessage());
            throw e;
        }
    }

    @Test
    public void pathBuilderShouldMergeParamsEncodedAndNotEncoded() throws Exception {
        String urlTemplate = "http://localhost/fgfg{aaa:\\d+}/{bbb:.*}/{ccc:\\d{5}}/{ddd}/df";
        PathTemplate toTest = RegexPathTemplate.create(urlTemplate);
        PathBuilder builder = toTest.getBuilder(UTF8);
        String url = builder.merge("aaa", "123", false)
                            .merge("bbb", "sdf df", false)
                            .merge("ccc", "12345", false)
                            .merge("ddd", "d%20d", true)
                            .build();
        assertEquals("http://localhost/fgfg123/sdf%20df/12345/d%20d/df", url);
    }
}
