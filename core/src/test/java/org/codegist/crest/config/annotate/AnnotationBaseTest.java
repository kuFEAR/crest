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

package org.codegist.crest.config.annotate;

import org.codegist.crest.CRestConfig;
import org.codegist.crest.config.InterfaceConfigBuilder;
import org.codegist.crest.config.MethodConfigBuilder;
import org.codegist.crest.config.ParamConfigBuilder;
import org.codegist.crest.test.util.CRestConfigs;
import org.junit.After;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public abstract class AnnotationBaseTest<A extends Annotation> {

    protected final Map<Pattern,String> placeholders = new HashMap<Pattern, String>();
    {
        placeholders.put(Pattern.compile("\\{" + Pattern.quote("ph.1") + "\\}"), "val-1");
        placeholders.put(Pattern.compile("\\{" + Pattern.quote("ph.2") + "\\}"), "val-2");
    }
    protected final CRestConfig crestConfig = CRestConfigs.mockDefaultBehavior(placeholders);

    protected final A mockAnnotation;
    protected final InterfaceConfigBuilder mockInterfaceConfigBuilder = mock(InterfaceConfigBuilder.class);
    protected final MethodConfigBuilder mockMethodConfigBuilder = mock(MethodConfigBuilder.class);
    protected final ParamConfigBuilder mockParamConfigBuilder = mock(ParamConfigBuilder.class);

    protected static final String VAL_WITH_PH = "a|{ph.1}|{ph.1}|{ph.2}|{ph.3}|a";
    protected static final String EXPECTED_MERGE_VAL = "a|val-1|val-1|val-2|{ph.3}|a";

    public AnnotationBaseTest(Class<A> annotation){
        this.mockAnnotation = mock(annotation);
    }

    public abstract AnnotationHandler<A> getToTest();

    @After
    public void noMoreInteractions(){
        verifyNoMoreInteractions(mockAnnotation, mockParamConfigBuilder, mockInterfaceConfigBuilder, mockMethodConfigBuilder);
    }
}
