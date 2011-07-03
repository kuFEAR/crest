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
import org.codegist.crest.CRestBuilder;
import org.codegist.crest.flickr.model.FlickrModelFactory;
import org.codegist.crest.flickr.model.Gallery;
import org.codegist.crest.flickr.model.Uploader;
import org.codegist.crest.flickr.security.MultiPartEntityParamsParser;
import org.codegist.crest.flickr.service.Flickr;
import org.codegist.crest.serializer.jaxb.JaxbDeserializer;

import java.util.HashMap;

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
        String consumerKey       = "e0ecb99bde924245e19d008cd5b73339";
        String consumerSecret    = "388f273a47b5ffad";
        String accessToken       = "72157627098468812-2c989a27b74e71c3";
        String accessTokenSecret = "dd6d6921ab452c5f";

        LOG.debug("consumerKey       = %s", consumerKey);
        LOG.debug("consumerSecret    = %s", consumerSecret);
        LOG.debug("accessToken       = %s", accessToken);
        LOG.debug("accessTokenSecret = %s", accessTokenSecret);

        /* Get the factory */
        CRest crest = new CRestBuilder()
                .parseAuthenticatedMultiPartEntityWith(new MultiPartEntityParamsParser())
                .authenticatesWithOAuth(consumerKey,consumerSecret,accessToken,accessTokenSecret)
                .deserializeXmlWithJaxb(new HashMap<String, Object>(){{
                    put(JaxbDeserializer.MODEL_FACTORY_CLASS, FlickrModelFactory.class);
                }})
                .setDateFormat("Seconds").setBooleanFormat("1", "0")
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
