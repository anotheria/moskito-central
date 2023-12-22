package org.moskito.central.connectors.rest;

import org.moskito.central.Snapshot;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;

/**
 * Mock of Central REST resource for incoming snapshots via HTTP. For connector's tests.
 * Is being used when transfer its package name to {@link com.sun.jersey.test.framework.WebAppDescriptor} builder in {@link com.sun.jersey.test.framework.JerseyTest}.
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
