package org.codegist.crest.request.crest;

import org.codegist.crest.annotate.*;
import org.codegist.crest.request.common.Requests;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */

@EndPoint("{crest.server.end-point}")
@Path("io/get")
public interface Gets extends Requests {

    @GET
    String raw();

    @GET
    @Path("accept")
    @Consumes({"application/custom1", "application/custom2"})
    String accept();

    @GET
    @Path("content-type")
    @Produces("application/custom")
    String contentType();

}
