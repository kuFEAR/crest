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

package org.codegist.crest.param.paths.common;

import java.util.EnumSet;

import static java.lang.String.format;
import static org.codegist.crest.param.common.ISerializersTest.Tests.SerializeNulls;
import static org.junit.Assert.assertEquals;

/**
 * @author laurent.gilles@codegist.org
 */
public class ISerializersTest<T extends ISerializersTest.ISerializers> extends org.codegist.crest.param.common.ISerializersTest<T> {

    public ISerializersTest(CRestHolder crest, Class<T> clazz) {
        super(crest, clazz);
    }

    @Override
    public void assertDefaultSerialize(String expectSerializedBof, String expectSerializedBof21, String expectSerializedBof22, String expectSerializedBof31, String expectSerializedBof32, String actual) {
        assertEquals(format("default() p1=%s p2=%s(p2)%s p3=%s(p3)%s",
                expectSerializedBof,
                expectSerializedBof21,
                expectSerializedBof22,
                expectSerializedBof31,
                expectSerializedBof32), actual);
    }

    @Override
    public void assertConfiguredSerialize(String expectSerializedBof, String expectSerializedBof21, String expectSerializedBof22, String expectSerializedBof31, String expectSerializedBof32, String actual) {
        assertEquals(format("configured() p1=%s p2=%s(p2)%s p3=%s(p3)%s",
                expectSerializedBof,
                expectSerializedBof21,
                expectSerializedBof22,
                expectSerializedBof31,
                expectSerializedBof32), actual);
    }

    @Override
    public EnumSet<Tests> ignores() {
        return EnumSet.of(SerializeNulls); // N/A - Path params do not support nulls
    }

}
