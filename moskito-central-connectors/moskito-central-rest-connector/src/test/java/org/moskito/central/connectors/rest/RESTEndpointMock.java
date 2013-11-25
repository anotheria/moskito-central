package org.moskito.central.connectors.rest;

import org.moskito.central.Central;
import org.moskito.central.Snapshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

/**
 * Mock of Central REST resource for incoming snapshots via HTTP. For connector's tests.
 *
 * @author Vladyslav Bezuhlyi
 */
@Path("/central")
public class RESTEndpointMock {

    /**
     * Stub method to test connector's snapshot adding request.
     */
    @POST
    @Path("/addSnapshot")
    @Consumes({ MediaType.APPLICATION_JSON })
    public void addSnapshot(Snapshot snapshot) {
        System.out.println(snapshot.toString());
    }

}
