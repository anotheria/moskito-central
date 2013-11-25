package org.moskito.central.endpoints.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.moskito.central.Central;
import org.moskito.central.Snapshot;

/**
 * Central REST resource for incoming snapshots via HTTP.
 * 
 * @author dagafonov
 * 
 */
@Path("/central")
public class RESTEndpoint {

	/**
	 * Central instance.
	 */
	private Central central;

	/**
	 * Default constructor.
	 */
	public RESTEndpoint() {
		central = Central.getInstance();
	}

    /**
     * Sends {@link Snapshot} to the endpoint client.
     *
     * @return requested {@link Snapshot}
     */
	/*@GET
	@Path("/getSnapshot")
	@Produces({ MediaType.APPLICATION_JSON })
	public Snapshot getSnapshot() {
		Snapshot snapshot = new Snapshot();

		SnapshotMetaData metaData = new SnapshotMetaData();
		metaData.setProducerId("prodId");
		metaData.setCategory("catId");
		metaData.setSubsystem("subSId");

		snapshot.setMetaData(metaData);

		HashMap<String, String> data = new HashMap<String, String>();
		data.put("firstname", "moskito");
		data.put("lastname", "central");
		snapshot.addSnapshotData("test", data);
		snapshot.addSnapshotData("test2", data);
		snapshot.addSnapshotData("test3", data);

		return snapshot;
	}*/

	/**
	 * Receives {@link Snapshot} in order to transfer it to the central.
	 * 
	 * @param snapshot received {@link Snapshot}
	 */
	@POST
	@Path("/addSnapshot")
	@Consumes({ MediaType.APPLICATION_JSON })
	public void addSnapshot(Snapshot snapshot) {
		central.processIncomingSnapshot(snapshot);
	}

}
