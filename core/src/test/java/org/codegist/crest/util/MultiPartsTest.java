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

package org.codegist.crest.util;

import org.codegist.crest.NonInstanciableClassTest;
import org.codegist.crest.config.ParamConfig;
import org.codegist.crest.param.Param;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * @author laurent.gilles@codegist.org
 */
public class MultiPartsTest extends NonInstanciableClassTest {

    public MultiPartsTest() {
        super(MultiParts.class);
    }

    @Test
    public void getContentTypeShouldGetContentTypeFromParamConfig(){
        Map<String,Object> metaDatas = Collections.singletonMap("multipart.content-type", (Object)"expected");
        ParamConfig paramConfig = mock(ParamConfig.class);
        when(paramConfig.getMetaDatas()).thenReturn(metaDatas);

        assertEquals("expected", MultiParts.getContentType(paramConfig));
    }

    @Test
    public void getContentTypeShouldReturnNullWhenNoContentTypeIsFoundInParamConfigMetas(){
        Map<String,Object> metaDatas = new HashMap<String, Object>();
        ParamConfig paramConfig = mock(ParamConfig.class);
        when(paramConfig.getMetaDatas()).thenReturn(metaDatas);
        assertNull(MultiParts.getContentType(paramConfig));
    }

    @Test
    public void getFileNameShouldGetFileNameFromParamConfig(){
        Map<String,Object> metaDatas = Collections.singletonMap("multipart.filename", (Object)"expected");
        ParamConfig paramConfig = mock(ParamConfig.class);
        when(paramConfig.getMetaDatas()).thenReturn(metaDatas);

        assertEquals("expected", MultiParts.getFileName(paramConfig));
    }

    @Test
    public void getFileNameShouldReturnNullWhenNoFileNameIsFoundInParamConfigMetas(){
        Map<String,Object> metaDatas = new HashMap<String, Object>();
        ParamConfig paramConfig = mock(ParamConfig.class);
        when(paramConfig.getMetaDatas()).thenReturn(metaDatas);
        assertNull(MultiParts.getFileName(paramConfig));
    }

    @Test
    public void getContentTypeShouldGetContentTypeFromParam(){
        Map<String,Object> metaDatas = Collections.singletonMap("multipart.content-type", (Object)"expected");
        Param param = mock(Param.class);
        ParamConfig paramConfig = mock(ParamConfig.class);
        when(paramConfig .getMetaDatas()).thenReturn(metaDatas);
        when(param.getParamConfig()).thenReturn(paramConfig);

        assertEquals("expected", MultiParts.getContentType(param));
    }

    @Test
    public void getContentTypeShouldReturnNullWhenNoContentTypeIsFoundInParamMetas(){
        Map<String,Object> metaDatas = new HashMap<String, Object>();
        Param param = mock(Param.class);
        ParamConfig paramConfig = mock(ParamConfig.class);
        when(paramConfig .getMetaDatas()).thenReturn(metaDatas);
        when(param.getParamConfig()).thenReturn(paramConfig);
        assertNull(MultiParts.getContentType(param));
    }

    @Test
    public void getFileNameShouldGetFileNameFromParam(){
        Map<String,Object> metaDatas = Collections.singletonMap("multipart.filename", (Object)"expected");
        Param param = mock(Param.class);
        ParamConfig paramConfig = mock(ParamConfig.class);
        when(paramConfig .getMetaDatas()).thenReturn(metaDatas);
        when(param.getParamConfig()).thenReturn(paramConfig);

        assertEquals("expected", MultiParts.getFileName(param));
    }

    @Test
    public void getFileNameShouldReturnNullWhenNoFileNameIsFoundInParamMetas(){
        Map<String,Object> metaDatas = new HashMap<String, Object>();
        Param param = mock(Param.class);
        ParamConfig paramConfig = mock(ParamConfig.class);
        when(paramConfig .getMetaDatas()).thenReturn(metaDatas);
        when(param.getParamConfig()).thenReturn(paramConfig);
        assertNull(MultiParts.getFileName(param));
    }

    @Test
    public void hasMultiPartShouldReturnTrueIfMultipartsAreDetected(){
        Map<String,Object> metaDatas = Collections.singletonMap("multipart.flag", new Object());
        assertTrue(MultiParts.hasMultiPart(metaDatas));
    }

    @Test
    public void hasMultiPartShouldReturnTrueIfMultipartsAreDetectedInParam(){
        Map<String,Object> metaDatas = Collections.singletonMap("multipart.flag", new Object());
        Param param = mock(Param.class);
        ParamConfig paramConfig = mock(ParamConfig.class);
        when(paramConfig .getMetaDatas()).thenReturn(metaDatas);
        when(param.getParamConfig()).thenReturn(paramConfig);

        assertTrue(MultiParts.hasMultiPart(param));
    }

    @Test
    public void putMetaDatasShouldAddMetaDataIntoGivenMap(){
        Map<String,Object> metaDatas = new HashMap<String, Object>();
        MultiParts.putMetaDatas(metaDatas, "contentType", "fileName");
        assertEquals("contentType", metaDatas.get("multipart.content-type"));
        assertEquals("fileName", metaDatas.get("multipart.filename"));
        assertTrue((Boolean)metaDatas.get("multipart.flag"));
    }

    @Test
    public void toMetaDatasShouldReturnAMetaDataMapWithGivenMultiPartInfos(){
        Map<String,Object> metaDatas = MultiParts.toMetaDatas("contentType", "fileName");
        assertEquals("contentType", metaDatas.get("multipart.content-type"));
        assertEquals("fileName", metaDatas.get("multipart.filename"));
        assertTrue((Boolean)metaDatas.get("multipart.flag"));
    }
}
