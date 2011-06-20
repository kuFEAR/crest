package org.codegist.crest.request;

import org.codegist.crest.annotate.*;
import org.codegist.crest.request.common.Requests;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */

@EndPoint("{crest.server.end-point}")
@Path("request/get")
@GET
public interface Gets extends Requests {

    String raw();

    @Path("accept")
    @Consumes({"application/custom1", "application/custom2"})
    String accept();

    @Path("content-type")
    @Produces("application/custom")
    String contentType();

}
