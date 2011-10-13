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

package org.codegist.crest.request.crest;

import org.codegist.crest.request.common.CommonEntityRequestsTest;
import org.junit.runners.Parameterized;

import java.util.Collection;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
//@Ignore
public class PutsTest extends CommonEntityRequestsTest<Puts> {

    public PutsTest(CRestHolder crest) {
        super(crest, Puts.class);
    }

    @Parameterized.Parameters
    public static Collection<CRestHolder[]> getData() {
        return crest(byRestServicesAndCustomContentTypes());
    }


}
