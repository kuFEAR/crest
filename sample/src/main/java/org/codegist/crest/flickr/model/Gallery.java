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

package org.codegist.crest.flickr.model;

import org.codegist.common.lang.ToStringBuilder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "gallery")
public class Gallery implements Payload {
    @XmlAttribute
    private String id;
    @XmlAttribute
    private String url;

    public String getId() {
        return id;
    }
    public String getGalleryId() {
        return id.split("-")[1];
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("url", url)
                .toString();
    }
}
