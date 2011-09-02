/*
 * Copyright 2010 CodeGist.org
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 * ===================================================================
 *
 * More information at http://www.codegist.org.
 */

package org.codegist.crest.flickr;

import org.codegist.common.log.Logger;
import org.codegist.crest.CRest;
import org.codegist.crest.flickr.model.FlickrModelFactory;
import org.codegist.crest.flickr.model.Gallery;
import org.codegist.crest.flickr.model.Uploader;
import org.codegist.crest.flickr.security.MultiPartEntityParamExtractor;
import org.codegist.crest.flickr.serializer.FlickrDateSerializer;
import org.codegist.crest.flickr.service.Flickr;
import org.codegist.crest.serializer.jaxb.JaxbDeserializer;

import java.util.Collections;
import java.util.Date;

import static org.codegist.crest.serializer.jaxb.JaxbDeserializer.MODEL_FACTORY_CLASS_PROP;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class FlickrSample implements Runnable {

    private static final Logger LOG = Logger.getLogger(FlickrSample.class);

    private final String consumerKey;
    private final String consumerSecret;
    private final String accessToken;
    private final String accessTokenSecret;

    public FlickrSample(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret) {
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.accessToken = accessToken;
        this.accessTokenSecret = accessTokenSecret;
    }

    public void run() {
        LOG.debug("consumerKey       = %s", consumerKey);
        LOG.debug("consumerSecret    = %s", consumerSecret);
        LOG.debug("accessToken       = %s", accessToken);
        LOG.debug("accessTokenSecret = %s", accessTokenSecret);

        /* Get the factory */
        CRest crest = CRest.oauth(consumerKey,consumerSecret,accessToken,accessTokenSecret)
                           .extractsEntityAuthParamsWith("multipart/form-data", new MultiPartEntityParamExtractor())
                           .deserializeXmlWith(JaxbDeserializer.class, Collections.<String,Object>singletonMap(MODEL_FACTORY_CLASS_PROP, FlickrModelFactory.class))
                           .bindSerializer(FlickrDateSerializer.class, Date.class)
                           .booleanFormat("1", "0")
                           .build();

        /* Build service instance */
        Flickr flickr = crest.build(Flickr.class);

        /* Use it! */
        Gallery gallery = flickr.newGallery("My Gallery Title", "My Gallery Desc");
        long photoId = flickr.uploadPhoto(FlickrSample.class.getResourceAsStream("photo1.jpg"));
        String ticketId = flickr.asyncUploadPhoto(FlickrSample.class.getResourceAsStream("photo1.jpg"));
        Uploader upload = flickr.checkUploads(ticketId);

        LOG.info("photoId=" + photoId);
        LOG.info("ticketId=" + ticketId);
        LOG.info("upload=" + upload);
        LOG.info("gallery=" + gallery);
    }


    public static void main(String[] args) {
        int i = 0;
        new FlickrSample(args[i++],args[i++],args[i++],args[i++]).run();
    }


}
