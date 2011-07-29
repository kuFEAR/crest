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

package org.codegist.crest.serializer.simplexml;

import org.codegist.crest.CRestConfig;
import org.codegist.crest.NonInstanciableClassTest;
import org.codegist.crest.util.CRestConfigs;
import org.codegist.crest.util.Values;
import org.junit.Test;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;

import static org.codegist.crest.util.Values.FORMAT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.when;


/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class SimpleXmlFactoryTest extends NonInstanciableClassTest {
    public SimpleXmlFactoryTest() {
        super(SimpleXmlFactory.class);
    }

    private final CRestConfig config = CRestConfigs.mockBehavior("myTrue", "myFalse", FORMAT);

    @Test
    public void shouldCreateSerializerWithGivenConfigThroughCreateDeserializer() throws Exception {
        shouldCreateSerializerWithGivenConfig(SimpleXmlFactory.createDeserializer(config, getClass()));
    }
    @Test
    public void shouldCreateSerializerWithGivenConfigThroughCreateSerializer() throws Exception {
        shouldCreateSerializerWithGivenConfig(SimpleXmlFactory.createSerializer(config, getClass()));
    }

    private void shouldCreateSerializerWithGivenConfig(Serializer actual) throws Exception {
        verify(config).getBooleanFalse();
        verify(config).getBooleanTrue();
        verify(config).getDateFormat();
        verify(config).get(getClass().getName() + SimpleXmlFactory.SERIALIZER);

        StringWriter sw = new StringWriter();
        V v1 = new V(true, false, Values.DATE);
        actual.write(v1, sw);
        V read = actual.read(V.class, new StringReader(sw.toString()));

        assertEquals("<root bool=\"myTrue\" bool2=\"myFalse\" date=\"13/03/1983 00:35:10\"/>", sw.toString());
        assertEquals(v1, read);
    }

    @Test
    public void shouldUseConfigSerializer() throws Exception {
        Serializer mock = mock(Serializer.class);
        when(config.get(getClass().getName()  + SimpleXmlFactory.SERIALIZER)).thenReturn(mock);

        assertSame(mock, SimpleXmlFactory.createDeserializer(config, getClass()));

        verify(config, never()).getBooleanFalse();
        verify(config, never()).getBooleanTrue();
        verify(config, never()).getDateFormat();
        verify(config).get(getClass().getName() + SimpleXmlFactory.SERIALIZER);
    }

    @Root(name="root")
    public static class V {
        @Attribute(name="bool")
        public boolean bool;
        @Attribute(name="bool2")
        public Boolean bool2;
        @Attribute(name="date")
        public Date date;

        public V() {

        }
        public V(boolean bool, Boolean bool2, Date date) {
            this.bool = bool;
            this.bool2 = bool2;
            this.date = date;
        }

        public boolean isBool() {
            return bool;
        }

        public Boolean getBool2() {
            return bool2;
        }

        public Date getDate() {
            return date;
        }


        public void setBool(boolean bool) {
            this.bool = bool;
        }

        public void setBool2(Boolean bool2) {
            this.bool2 = bool2;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            V v = (V) o;

            if (bool != v.bool) return false;
            if (bool2 != null ? !bool2.equals(v.bool2) : v.bool2 != null) return false;
            if (date != null ? !date.toString().equals(v.date.toString()) : v.date != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = (bool ? 1 : 0);
            result = 31 * result + (bool2 != null ? bool2.hashCode() : 0);
            result = 31 * result + (date != null ? date.hashCode() : 0);
            return result;
        }
    }
}
