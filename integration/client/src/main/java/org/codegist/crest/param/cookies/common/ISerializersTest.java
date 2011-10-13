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

package org.codegist.crest.param.cookies.common;

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
//        these value doesn't mean anything, because the serialization generate cookies special characters (coma and semi-colon).
//        this test at least the the serialization process is executed for cookies as well
        assertEquals("default() p1=Data{val1=123 p2=AnotherBuchOfData(val1=20/01/2010 00:00:00 p3=AnotherBuchOfData(val1=23/03/2010 00:00:00", actual);
    }

    @Override
    public void assertConfiguredSerialize(String expectSerializedBof, String expectSerializedBof21, String expectSerializedBof22, String expectSerializedBof31, String expectSerializedBof32, String actual) {
//        these value doesn't mean anything, because the serialization generate cookies special characters (coma and semi-colon).
//        this test at least the the serialization process is executed for cookies as well
        assertEquals("configured() p1=Data(val1=123 p2=MyBuchOfData(val1=20/01/2010 00:00:00 p3=MyBuchOfData(val1=23/03/2010 00:00:00", actual);
    }
}
